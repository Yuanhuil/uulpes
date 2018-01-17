package com.njpes.www.action.systeminfo;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Resource;
import com.njpes.www.entity.util.ZTree;
import com.njpes.www.service.baseinfo.ResourceServiceI;

/**
 * 资源目录控制类
 */
@Controller
@RequestMapping(value = "/systeminfo/sys/resource")
public class ResourceController extends BaseController {

    @Autowired
    private ResourceServiceI resourceService;

    public ResourceController() {
        setResourceIdentity("systeminfo:resource");
    }

    /**
     * 展示树形结构资源
     * 
     * @param request
     * @param searchName
     *            检索名称关键字
     * @param async
     *            是否异步，false 非异步 true 异步
     * @param model
     * @author 赵忠诚
     */
    @RequestMapping(value = "tree", method = RequestMethod.GET)
    public String tree(HttpServletRequest request,
            @RequestParam(value = "searchName", required = false) String searchName,
            @RequestParam(value = "async", required = false, defaultValue = "false") boolean async, Model model) {

        List<Resource> models = null;

        if (!StringUtils.isEmpty(searchName)) {
            models = resourceService.findAllByName(searchName);
            if (!async) { // 非异步 查自己和子子孙孙
                List<Resource> children = resourceService.findChildren(models);
                models.removeAll(children);
                models.addAll(children);
            } else { // 异步模式只查自己

            }
        } else {
            if (!async) { // 非异步 查自己和子子孙孙
                models = resourceService.findAllWithSort();
            } else { // 异步模式只查根 和 根一级节点
                models = resourceService.findRootAndChild(new Long(0));
            }
        }

        model.addAttribute("trees", convertToZtreeList(request.getContextPath(), models, async, true));

        return viewName("tree");
    }

    /**
     * 增加父亲节点的孩子节点@PathVariableurl中取得的变量,@ModelAttribute从model中获取变量
     */
    @RequestMapping(value = "{parent}/appendChild", method = RequestMethod.POST)
    public String appendChild(Model model, @PathVariable("parent") Resource parent,
            @ModelAttribute("child") Resource child, BindingResult result, RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            permissionList.assertHasCreatePermission();
        }

        setCommonData(model);

        if (result.hasErrors()) {
            return appendChildForm(parent, model);
        }

        resourceService.appendChild(parent, child);

        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "添加子节点成功");
        return redirectToUrl(viewName("success"));
    }

    /**
     * 更新父亲节点以及附属节点
     */
    @RequestMapping(value = "{parent}/appendChild", method = RequestMethod.GET)
    public String appendChildForm(@PathVariable("parent") Resource parent, Model model) {

        if (permissionList != null) {
            permissionList.assertHasCreatePermission();
        }

        setCommonData(model);
        if (!model.containsAttribute("child")) {
            model.addAttribute("child", new Resource());
        }

        model.addAttribute(Constants.OP_NAME, "添加子节点");

        return viewName("appendChildForm");
    }

    /**
     * 更新某个节点资源信息
     */
    @RequestMapping(value = "{id}/update", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long resourceId, Model model, RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            permissionList.assertHasUpdatePermission();
        }

        if (resourceId == null) {
            redirectAttributes.addFlashAttribute(Constants.ERROR, "您修改的数据不存在！");
            return redirectToUrl(viewName("success"));
        }

        Resource resource = resourceService.findResourceById(resourceId);
        model.addAttribute("m", resource);
        return viewName("editForm");
    }

    @RequestMapping(value = "{id}/update", method = RequestMethod.POST)
    public String update(Model model, @ModelAttribute("m") Resource m, BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            permissionList.assertHasUpdatePermission();
        }

        if (result.hasErrors()) {
            return updateForm(m.getId(), model, redirectAttributes);
        }
        String actualReskey = resourceService.findActualResourceIdentity(m);
        m.setActualreskey(actualReskey);
        resourceService.update(m);
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "修改成功");
        return redirectToUrl(viewName("success.do"));
    }

    @RequestMapping(value = "{id}/delete", method = RequestMethod.GET)
    public String deleteForm(@PathVariable("id") Resource m, Model model) {

        if (permissionList != null) {
            permissionList.assertHasDeletePermission();
        }

        model.addAttribute("m", m);
        model.addAttribute(Constants.OP_NAME, "删除");
        return viewName("editForm");
    }

    @RequestMapping(value = "{id}/delete", method = RequestMethod.POST)
    public String deleteSelfAndChildren(Model model, @ModelAttribute("m") Resource m, BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            permissionList.assertHasDeletePermission();
        }

        if (m.isRoot()) {
            result.reject("您删除的数据中包含根节点，根节点不能删除");
            return deleteForm(m, model);
        }

        resourceService.deleteSelfAndChild(m);
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
        return redirectToUrl(viewName("success.do"));
    }

    @RequestMapping(value = "batch/delete")
    public String deleteInBatch(@RequestParam(value = "ids", required = false) Long[] ids,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            permissionList.assertHasDeletePermission();
        }

        List<Resource> mList = resourceService.findResourceS(ids);
        for (Resource m : mList) {
            if (m.isRoot()) {
                redirectAttributes.addFlashAttribute(Constants.ERROR, "您删除的数据中包含根节点，根节点不能删除");
                return redirectToUrl(backURL);
            }
        }

        resourceService.deleteSelfAndChild(mList);
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
        return redirectToUrl(backURL);
    }

    @RequestMapping(value = "{parent}/children", method = RequestMethod.GET)
    public String list(HttpServletRequest request, @PathVariable("parent") Resource parent, Model model)
            throws UnsupportedEncodingException {

        if (permissionList != null) {
            permissionList.assertHasViewPermission();
        }

        model.addAttribute("page", resourceService.findResourceById(parent.getId()));

        return viewName("listChildren");
    }

    /**
     * 仅返回表格数据
     * 
     * @param searchable
     * @param model
     * @return
     */
    @RequestMapping(value = "{parent}/children", headers = "table=true", method = RequestMethod.GET)
    public String listTable(HttpServletRequest request, @PathVariable("parent") Resource parent, Model model)
            throws UnsupportedEncodingException {

        list(request, parent, model);
        return viewName("listChildrenTable");

    }

    @RequestMapping(value = "ajax/{parent_id}/appendChild", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object ajaxAppendChild(HttpServletRequest request, @PathVariable("parent_id") String parentId) {

        if (permissionList != null) {
            permissionList.assertHasCreatePermission();
        }

        Resource parent = resourceService.findResourceById(Long.parseLong(parentId));
        Resource child = new Resource();
        child.setResName("新节点");
        resourceService.appendChild(parent, child);
        return convertToZtree(child, true, true);
    }

    @RequestMapping(value = "ajax/{id}/delete", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object ajaxDeleteSelfAndChildren(@PathVariable("id") Long id) {

        if (this.permissionList != null) {
            this.permissionList.assertHasEditPermission();
        }

        Resource tree = resourceService.findResourceById(id);
        resourceService.deleteSelfAndChild(tree);
        return tree;
    }

    @RequestMapping(value = "ajax/{id}/rename", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object ajaxRename(HttpServletRequest request, @PathVariable("id") Long resourceId,
            @RequestParam("newName") String newName) {

        if (permissionList != null) {
            permissionList.assertHasUpdatePermission();
        }

        Resource tree = resourceService.findResourceById(resourceId);
        tree.setResName(newName);
        resourceService.update(tree);
        return convertToZtree(tree, true, true);
    }

    @RequestMapping(value = "ajax/{sourceId}/{targetId}/{moveType}/move", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object ajaxMove(@PathVariable("sourceId") Long sourceId, @PathVariable("targetId") Long targetId,
            @PathVariable("moveType") String moveType) {
        if (this.permissionList != null) {
            this.permissionList.assertHasEditPermission();
        }
        Resource source = resourceService.findResourceById(sourceId);
        Resource target = resourceService.findResourceById(targetId);
        resourceService.move(source, target, moveType);

        return source;
    }

    @RequestMapping(value = "success")
    public String success() {
        return viewName("success");
    }

    @Override
    protected String redirectToUrl(String backURL) {
        if (!StringUtils.isEmpty(backURL)) {
            return super.redirectToUrl(backURL);
        }
        return super.redirectToUrl(viewName("success"));
    }

    private List<ZTree> convertToZtreeList(String contextPath, List<Resource> models, boolean async,
            boolean onlySelectLeaf) {
        List<ZTree> zTrees = Lists.newArrayList();

        if (models == null || models.isEmpty()) {
            return zTrees;
        }

        for (Resource m : models) {
            ZTree zTree = convertToZtree(m, !async, onlySelectLeaf);
            zTrees.add(zTree);
        }
        return zTrees;
    }

    private ZTree convertToZtree(Resource m, boolean open, boolean onlyCheckLeaf) {
        ZTree zTree = new ZTree();
        zTree.setId(m.getId());
        zTree.setpId(m.getParentId());
        zTree.setName(m.getResName());
        zTree.setOpen(open);
        zTree.setRoot(m.isRoot());

        if (onlyCheckLeaf && zTree.isIsParent()) {
            zTree.setNocheck(true);
        } else {
            zTree.setNocheck(false);
        }

        return zTree;
    }

}
