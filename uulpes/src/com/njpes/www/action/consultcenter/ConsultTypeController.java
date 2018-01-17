package com.njpes.www.action.consultcenter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.njpes.www.action.BaseController;
import com.njpes.www.entity.baseinfo.enums.BooleanEnum;
import com.njpes.www.entity.baseinfo.enums.SwitchEnum;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.consultcenter.ConsultType;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.consultcenter.ConsultTypeServiceI;

/**
 * @Description: 咨询类型
 * @author zhangchao
 * @Date 2015-5-18 上午9:10:28
 */
@Controller
@RequestMapping(value = "/consultcenter/consultType")
public class ConsultTypeController extends BaseController {

    @Autowired
    private ConsultTypeServiceI consultTypeService;
    @Autowired
    private SyslogServiceI logservice;

    /**
     * @Description:咨询类型编辑页面跳转
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/addOrUpdate" })
    public String addOrUpdate(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        String id = request.getParameter("id");

        ConsultType consultType = new ConsultType();
        if (id != null) {
            long idL = Long.parseLong(id);
            consultType = consultTypeService.selectByPrimaryKey(idL);
        }

        consultType.setFid(orgEntity.getId());

        BooleanEnum.values();
        model.addAttribute("switchEnum", SwitchEnum.values());
        model.addAttribute("consultType", consultType);
        return "/consultcenter/introduce/consultTypeEditForm";
    }

    /**
     * @Description:获取咨询类型列表
     * @param fid
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/getListByFid" })
    public String getListByFid(@RequestParam("fid") long fid, HttpServletRequest request, Model model) {

        List<ConsultType> consultTypes = consultTypeService.getListByFid(fid);

        model.addAttribute("consultTypes", consultTypes);
        model.addAttribute("fid", fid);

        return "/consultcenter/introduce/consultTypelist";
    }

    /**
     * @Description:保存咨询类型
     * @param consultType
     * @param request
     * @param model
     * @param redirectAttributesModelMap
     * @return
     */
    @RequestMapping(value = { "/save" })
    public String save(ConsultType consultType, HttpServletRequest request, Model model,
            RedirectAttributesModelMap redirectAttributesModelMap) {
        String result = "";
        if (consultType.getId() == null) {
            result = "保存";
        } else {
            result = "更新";
        }
        int str = consultTypeService.saveOrUpdateConsultType(consultType);
        if (str == 1) {
            result += "成功";
            logservice.log(request, "心理辅导中心", "更新或保存咨询类型成功");
        } else {
            result += "失败";
        }
        redirectAttributesModelMap.addAttribute("result", result);
        return redirectToUrl(viewName("getListByFid.do?fid=" + consultType.getFid()));
    }

    /**
     * @Description:更改状态
     * @param consultType
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/updateStatus" })
    public String updateStatus(ConsultType consultType, HttpServletRequest request, Model model) {
        String str = "";
        if (consultType.getId() != null) {
            consultType = consultTypeService.selectByPrimaryKey(consultType.getId());
            if (consultType != null) {
                if (consultType.getStatus().equals("1")) {
                    consultType.setStatus("0");
                } else {
                    consultType.setStatus("1");
                }

                int a = consultTypeService.saveOrUpdateConsultType(consultType);
                if (a == 1) {
                    str = "更新成功";
                    logservice.log(request, "心理辅导中心:心理咨询类型", "更改状态成功");
                }
            } else {
                str = "更新失败";
            }
        } else {
            str = "更新失败";
        }
        request.setAttribute("result", str);
        return redirectToUrl(viewName("getListByFid.do?fid=" + consultType.getFid()));
    }

    /**
     * @Description:删除咨询类型
     * @param consultType
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/delete" })
    public String delete(ConsultType consultType, @CurrentOrg Organization orgEntity, HttpServletRequest request,
            Model model) {
        String str = "";
        if (consultType.getId() != null) {

            int a = consultTypeService.delConsultType(consultType);
            if (a == 1) {
                str = "删除成功";
                logservice.log(request, "心理辅导中心", "删除心理咨询类型成功");
            } else {
                str = "删除失败";
            }
        } else {
            str = "删除失败";
        }
        request.setAttribute("result", str);
        return redirectToUrl(viewName("getListByFid.do?fid=" + orgEntity.getId()));
    }

}
