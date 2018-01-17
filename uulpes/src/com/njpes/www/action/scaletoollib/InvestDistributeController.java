package com.njpes.www.action.scaletoollib;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.njpes.www.action.BaseController;
import com.njpes.www.entity.baseinfo.enums.OrganizationType;
import com.njpes.www.entity.baseinfo.enums.XueDuan;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.service.baseinfo.organization.OrganizationServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;

@Controller
@RequestMapping("/assessmentcenter/investdispense")
public class InvestDistributeController extends BaseController {
    @Autowired
    private OrganizationServiceI organizationService;
    @Autowired
    private SchoolServiceI schoolService;

    /**
     * 量表群体分发开始
     */
    @RequestMapping(value = "/schooldispenseview", method = RequestMethod.GET)
    public String schooldispenseAction(@CurrentOrg Organization orgEntity, HttpServletRequest req, Model model) {
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        int orglevel = orgEntity.getOrgLevel();
        List<XueDuan> xdList = schoolService.getXueDuanInSchool(orgEntity.getId());
        List xdidList = new ArrayList<Integer>();
        for (int i = 0; i < xdList.size(); i++) {
            XueDuan xd = xdList.get(i);
            xdidList.add(xd.getId());
        }
        // model.addAttribute("orgtype", orgType.value());
        // model.addAttribute("orglevel", orglevel);
        model.addAttribute("orgtype", "1");
        model.addAttribute("orglevel", 2);
        model.addAttribute("xdlist", xdidList);
        return viewName("groupdispenseview");

    }
}
