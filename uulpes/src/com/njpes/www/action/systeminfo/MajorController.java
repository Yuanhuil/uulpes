package com.njpes.www.action.systeminfo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.organization.Major;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.service.baseinfo.MajorServiceI;
import com.njpes.www.utils.PageParameter;

@Controller
@RequestMapping(value = "/systeminfo/sys/major")
public class MajorController extends BaseController {

    @Autowired
    private MajorServiceI majorService;
    
    @RequestMapping("/queryMajor")
    public String querySyslogs(@CurrentOrg Organization orgEntity, HttpServletRequest request,@PageAnnotation PageParameter page,
            HttpServletResponse response, Model model) throws Exception {
            List<Major> allMajors = majorService.selectAllMajors();
            model.addAttribute("page", page);
            model.addAttribute("allMajors", allMajors);
        return viewName("list");
    }
    
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(@CurrentOrg Organization org, Model model) {
        Major major = new Major();
        
        System.out.println("去添加");
        model.addAttribute("entity", major);
        return viewName("editform");
    }

    @ResponseBody
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public String create(HttpServletRequest request, @CurrentOrg Organization org,
            Major entity, String majorname,Model model) {
        System.out.println("添加专业");
//        Major major = new Major();
//        major.setMajorName(majorname);
        String insert = String.valueOf(majorService.insert(entity));
        return insert;
    }
}
