package com.njpes.www.service.baseinfo;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.njpes.www.dao.baseinfo.ResourceMapper;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.Menu;
import com.njpes.www.entity.baseinfo.Resource;

@Service("resourceService")
public class ResourceServiceImpl implements ResourceServiceI {

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private UserAuthService userAuthService;

    @Override
    public String findActualResourceIdentity(Resource resource) {

        if (resource == null) {
            return null;
        }

        StringBuilder s = new StringBuilder(resource.getResKey());

        boolean hasResourceIdentity = !StringUtils.isEmpty(resource.getResKey());

        Resource parent = findResourceById(resource.getParentId());
        while (parent != null) {
            if (!StringUtils.isEmpty(parent.getResKey())) {
                s.insert(0, parent.getResKey() + ":");
                hasResourceIdentity = true;
            }
            parent = findResourceById(parent.getParentId());
        }

        // 如果用户没有声明 资源标识 且父也没有，那么就为空
        if (!hasResourceIdentity) {
            return "";
        }

        // 如果最后一个字符是: 因为不需要，所以删除之
        int length = s.length();
        if (length > 0 && s.lastIndexOf(":") == length - 1) {
            s.deleteCharAt(length - 1);
        }

        // 如果有儿子 最后拼一个*
        boolean hasChildren = false;

        if (resourceMapper.findChildrensCnt(resource.getId()) > 0) {
            hasChildren = true;
        }

        if (hasChildren) {
            s.append(":*");
        }

        return s.toString();
    }

    @Override
    public List<Menu> findMenus(Account user) {
        return convertToMenus(findResource(user));
    }

    @Override
    public List<Menu> findAllMenus() {
        return convertToMenus(findAllWithSort());
    }

    private boolean hasPermission(Resource resource, Set<String> userPermissions) {
        String actualResourceIdentity = resource.getActualreskey();
        if (StringUtils.isEmpty(actualResourceIdentity)) {
            return true;
        }
        for (String permission : userPermissions) {
            if (hasPermission(permission, actualResourceIdentity)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasPermission(String permission, String actualResourceIdentity) {

        // 得到权限字符串中的 资源部分，如a:b:create --->资源是a:b
        String permissionResourceIdentity = permission.substring(0, permission.lastIndexOf(":"));

        // 如果权限字符串中的资源 是 以资源为前缀 则有权限 如a:b 具有a:b的权限
        if (permissionResourceIdentity.startsWith(actualResourceIdentity)) {
            return true;
        }

        // 模式匹配
        WildcardPermission p1 = new WildcardPermission(permissionResourceIdentity);
        WildcardPermission p2 = new WildcardPermission(actualResourceIdentity);

        return p1.implies(p2) || p2.implies(p1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Menu> convertToMenus(List<Resource> resources) {

        if (resources.size() == 0) {
            return Collections.EMPTY_LIST;
        }

        Menu root = convertToMenu(resources.remove(resources.size() - 1));

        recursiveMenu(root, resources);
        List<Menu> menus = root.getChildren();
        removeNoLeafMenu(menus);

        return menus;
    }

    private void removeNoLeafMenu(List<Menu> menus) {
        if (menus.size() == 0) {
            return;
        }
        for (int i = menus.size() - 1; i >= 0; i--) {
            Menu m = menus.get(i);
            removeNoLeafMenu(m.getChildren());
            if (!m.isHasChildren() && StringUtils.isEmpty(m.getUrl())) {
                menus.remove(i);
            }
        }
    }

    private void recursiveMenu(Menu menu, List<Resource> resources) {
        for (int i = resources.size() - 1; i >= 0; i--) {
            Resource resource = resources.get(i);
            if (resource.getParentId().longValue() == menu.getId()) {
                menu.getChildren().add(convertToMenu(resource));
                resources.remove(i);
            }
        }

        for (Menu subMenu : menu.getChildren()) {
            recursiveMenu(subMenu, resources);
        }
    }

    private Menu convertToMenu(Resource resource) {
        return new Menu(resource.getId(), resource.getResName(), resource.getIcon(), resource.getResUrl(),
                resource.getIsleaf());
    }

    @Override
    public Resource findResourceById(Long id) {
        return resourceMapper.selectByPrimaryKey(id);
    }

    @Override
    public int save(Resource res) {
        return resourceMapper.insert(res);
    }

    @Transactional
    @Override
    public int update(Resource res) {
        return resourceMapper.updateByPrimaryKey(res);
    }

    @Transactional
    @Override
    public int deleteSelfAndChild(Resource m) {
        return resourceMapper.deleteSelfAndChild(m.getId(), m.makeSelfAsNewParentIds());
    }

    @Transactional
    @Override
    public void deleteSelfAndChild(List<Resource> mList) {
        for (Resource m : mList) {
            deleteSelfAndChild(m);
        }
    }

    @Override
    public List<Resource> findResourceS(Long[] ids) {
        return resourceMapper.findResourceInIds(ids);
    }

    @Override
    public List<Resource> findResource(Account user) {
        List<Resource> resources = resourceMapper.findAll();// 查看所有资源

        Set<String> userPermissions = userAuthService.findStringPermissions(user);

        Iterator<Resource> iter = resources.iterator();
        while (iter.hasNext()) {
            if (!hasPermission(iter.next(), userPermissions)) {
                iter.remove();
            }
        }
        return resources;
    }

    @Override
    public List<Resource> findAllByName(String name) {
        return resourceMapper.findResourceByName(name);
    }

    @Override
    public List<Resource> findChildren(List<Resource> resources) {
        if (resources.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        String[] orQuery = new String[resources.size()];

        for (int i = 0; i < resources.size(); i++) {
            orQuery[i] = resources.get(i).makeSelfAsNewParentIds();
        }

        List<Resource> children = resourceMapper.findResourceByParentids(orQuery);
        return children;
    }

    @Override
    public List<Resource> findAllWithSort() {
        return resourceMapper.findAll();
    }

    @Override
    public List<Resource> findRootAndChild(Long id) {
        List<Resource> models = resourceMapper.findChildrens(id);

        if (models.size() == 0) {
            return models;
        }
        List<Long> ids = Lists.newArrayList();
        for (int i = 0; i < models.size(); i++) {
            ids.add(models.get(i).getId());
        }

        models.addAll(resourceMapper.findResourceInParentId(ids));

        return models;
    }

    @Override
    public int findNextSortAuto(Long parentid) {
        return resourceMapper.findnextSortAuto(parentid);
    }

    @Override
    public void appendChild(Resource parent, Resource child) {
        child.setResUrl("");
        child.setResKey("");
        child.setIsShow("1");
        child.setParentId(parent.getId());
        child.setParentIds(parent.makeSelfAsNewParentIds());
        child.setResSort(findNextSortAuto(parent.getId()));
        save(child);
    }

    @Override
    public void move(Resource source, Resource target, String moveType) {
        if (source == null || target == null || source.isRoot()) { // 根节点不能移动
            return;
        }

        // 如果是相邻的兄弟 直接交换weight即可
        boolean isSibling = source.getParentId().equals(target.getParentId());
        boolean isNextOrPrevMoveType = "next".equals(moveType) || "prev".equals(moveType);
        if (isSibling && isNextOrPrevMoveType && Math.abs(source.getResSort() - target.getResSort()) == 1) {

            // 无需移动
            if ("next".equals(moveType) && source.getResSort() > target.getResSort()) {
                return;
            }
            if ("prev".equals(moveType) && source.getResSort() < target.getResSort()) {
                return;
            }

            int sourceWeight = source.getResSort();
            source.setResSort(target.getResSort());
            target.setResSort(sourceWeight);
            return;
        }

        // 移动到目标节点之后
        if ("next".equals(moveType)) {
            List<Resource> siblings = findSelfAndNextSiblings(target.getParentIds(), target.getResSort());
            siblings.remove(0);// 把自己移除

            if (siblings.size() == 0) { // 如果没有兄弟了 则直接把源的设置为目标即可
                int nextWeight = resourceMapper.findnextSortAuto(target.getParentId());
                updateSelftAndChild(source, target.getParentId(), target.getParentIds(), nextWeight);
                return;
            } else {
                moveType = "prev";
                target = siblings.get(0); // 否则，相当于插入到实际目标节点下一个节点之前
            }
        }

        // 移动到目标节点之前
        if ("prev".equals(moveType)) {

            List<Resource> siblings = findSelfAndNextSiblings(target.getParentIds(), target.getResSort());
            // 兄弟节点中包含源节点
            if (siblings.contains(source)) {
                // 1 2 [3 source] 4
                siblings = siblings.subList(0, siblings.indexOf(source) + 1);
                int firstWeight = siblings.get(0).getResSort();
                for (int i = 0; i < siblings.size() - 1; i++) {
                    siblings.get(i).setResSort(siblings.get(i + 1).getResSort());
                }
                siblings.get(siblings.size() - 1).setResSort(firstWeight);
            } else {
                // 1 2 3 4 [5 new]
                int nextWeight = resourceMapper.findnextSortAuto(target.getParentId());
                int firstWeight = siblings.get(0).getResSort();
                for (int i = 0; i < siblings.size() - 1; i++) {
                    siblings.get(i).setResSort(siblings.get(i + 1).getResSort());
                }
                siblings.get(siblings.size() - 1).setResSort(nextWeight);
                source.setResSort(firstWeight);
                updateSelftAndChild(source, target.getParentId(), target.getParentIds(), source.getResSort());
            }

            return;
        }
        // 否则作为最后孩子节点
        int nextWeight = resourceMapper.findnextSortAuto(target.getParentId());
        updateSelftAndChild(source, target.getId(), target.makeSelfAsNewParentIds(), nextWeight);
    }

    /**
     * 查找目标节点及之后的兄弟 注意：值与越小 越排在前边
     *
     * @param parentIds
     * @param currentWeight
     * @return
     */
    protected List<Resource> findSelfAndNextSiblings(String parentIds, int sort) {
        return resourceMapper.findSelfAndNextSiblings(parentIds, sort);
    }

    /**
     * 把源节点全部变更为目标节点
     *
     * @param source
     * @param newParentIds
     */
    private void updateSelftAndChild(Resource source, Long newParentId, String newParentIds, int newWeight) {
        String oldSourceChildrenParentIds = source.makeSelfAsNewParentIds();
        source.setParentId(newParentId);
        source.setParentIds(newParentIds);
        source.setResSort(newWeight);
        update(source);
        String newSourceChildrenParentIds = source.makeSelfAsNewParentIds();
        resourceMapper.updateSelftAndChild(newSourceChildrenParentIds, oldSourceChildrenParentIds);
    }

}
