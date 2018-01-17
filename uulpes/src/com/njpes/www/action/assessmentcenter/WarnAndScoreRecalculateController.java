package com.njpes.www.action.assessmentcenter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.njpes.www.action.BaseController;
import com.njpes.www.service.scaletoollib.ExamAnswerServiceI;
import com.njpes.www.service.scaletoollib.ScaleService;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.model.Scale;

@Controller
@RequestMapping(value = "/assessmentcenter/datamanager")
public class WarnAndScoreRecalculateController extends BaseController {
    @Autowired
    ExamAnswerServiceI examAnswerService;
    @Autowired
    ScaleService scaleService;
    @Autowired
    CachedScaleMgr cachedScaleMgr;

    @RequestMapping(value = "scaleWarningRecalculateview", method = RequestMethod.GET)
    public String scaleWarningRecalculateview(HttpServletRequest request) {
        List<Scale> scaleList = cachedScaleMgr.getScaleList();
        request.setAttribute("scaleList", scaleList);
        return viewName("warningandscorerecal");
    }

    @RequestMapping(value = "scaleWarningRecalculate", method = RequestMethod.POST)
    @ResponseBody
    public String scaleWarningRecalculate(HttpServletRequest request) {
        String scaleid = request.getParameter("scaleid");
        String typeflag = request.getParameter("typeflag");
        examAnswerService.ScaleWarningReCalculate(scaleid, Integer.parseInt(typeflag));
        return "success";
    }
}
