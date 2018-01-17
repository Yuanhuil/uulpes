package com.njpes.www.action.scaletoollib;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.District;
import com.njpes.www.entity.baseinfo.enums.XueDuan;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.scaletoollib.NormInfo;
import com.njpes.www.entity.scaletoollib.Normobject;
import com.njpes.www.entity.scaletoollib.ScaleFilterParam;
import com.njpes.www.entity.scaletoollib.Scalenorm;
import com.njpes.www.service.baseinfo.DistrictService;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.service.scaletoollib.ScaleService;
import com.njpes.www.utils.FileOperate;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.model.Dimension;
import edutec.scale.model.Scale;

@Controller
@RequestMapping("/scaletoollib/scalelook")
public class ScaleCheckController extends BaseController {
    @Autowired
    private CachedScaleMgr cachedScaleMgr;
    @Autowired
    ScaleService scaleService;
    @Autowired
    DistrictService districtService;
    @Autowired
    private SyslogServiceI logservice;
    @Autowired
    private SchoolServiceI schoolService;

    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/listScales")
    public String listScales(@CurrentOrg Organization orgEntity, HttpServletRequest requst,
            HttpServletResponse response) {
        List scaleList = this.cachedScaleMgr.getScaleList();
        List<XueDuan> xueDuanInSchool = Arrays.asList(new XueDuan[] { XueDuan.prim_school, XueDuan.j_school, XueDuan.s_school });
        String orgType = orgEntity.getOrgType();
        if (orgType.equals("2"))
        {
          xueDuanInSchool = this.schoolService.getXueDuanInSchool(orgEntity.getId().longValue());
          if ((xueDuanInSchool != null) && (xueDuanInSchool.size() > 0))
          {
            StringBuffer xd = new StringBuffer();
            for (XueDuan xueDuan : xueDuanInSchool) {
              xd.append(xueDuan.getInfo()).append(",");
            }
            ScaleFilterParam scaleFilterParam = new ScaleFilterParam();
            scaleFilterParam.setApplicablePerson(xd.toString());
            scaleList = this.cachedScaleMgr.getFilterScaleList(scaleFilterParam);
          }
        }
        List scaleTypes = this.scaleService.getScaleTypes();
        List scaleSources = this.scaleService.getScaleSource();
        requst.setAttribute("xd", xueDuanInSchool);
        requst.setAttribute("scaleTypes", scaleTypes);
        requst.setAttribute("scaleSources", scaleSources);
        requst.setAttribute("scaleList", scaleList);
        requst.setAttribute("scaleListSize", Integer.valueOf(scaleList.size()));
        ScaleFilterParam sfp = new ScaleFilterParam();
        requst.setAttribute("scaleFilterParam", sfp);
        return viewName("lookScales");
    }

    @RequestMapping(value = "/searchScales", method = RequestMethod.POST)
    public String searchScales(@CurrentOrg Organization orgEntity,
            @ModelAttribute("scaleFilterParam") ScaleFilterParam scaleFilterParam, HttpServletRequest requst,
            HttpServletResponse response) {
        List<XueDuan> xueDuanInSchool = this.schoolService.getXueDuanInSchool(orgEntity.getId().longValue());
        List scaleTypes = this.scaleService.getScaleTypes();
        List scaleSources = this.scaleService.getScaleSource();
        List scaleList = this.cachedScaleMgr.getFilterScaleList(scaleFilterParam);
        requst.setAttribute("xd", xueDuanInSchool);
        requst.setAttribute("scaleTypes", scaleTypes);
        requst.setAttribute("scaleSources", scaleSources);
        requst.setAttribute("scaleList", scaleList);
        requst.setAttribute("scaleListSize", Integer.valueOf(scaleList.size()));
        ScaleFilterParam sfp = new ScaleFilterParam();
        requst.setAttribute("scaleFilterParam", sfp);
        return viewName("lookScales");
    }

    @RequestMapping(value = "{scaleid}/view", method = RequestMethod.GET)
    public String scaleView(Model model, @PathVariable("scaleid") int scalecode) {
        Scale scale = this.cachedScaleMgr.get(String.valueOf(scalecode));
        model.addAttribute("scale", scale);
        String str = scale.toQuestionHTML();
        model.addAttribute("toQuestionHTML", str);
        String dimstr = scale.toDimHTML();
        model.addAttribute("toDimHTML", dimstr);
        return viewName("scaledetailinfo");
    }

    @RequestMapping(value = "{scaleid}/delete", method = RequestMethod.GET)
    public String deleteScale(@CurrentOrg Organization orgEntity, HttpServletRequest requst, Model model,
            @PathVariable("scaleid") String scalecode) {
        Scale scale = cachedScaleMgr.get(scalecode);
        cachedScaleMgr.removeScale(scale);
        List<XueDuan> xueDuanInSchool = schoolService.getXueDuanInSchool(orgEntity.getId());
        List scaleTypes = this.scaleService.getScaleTypes();
        List scaleSources = this.scaleService.getScaleSource();
        List scaleList = this.cachedScaleMgr.getScaleList();
        requst.setAttribute("xd", xueDuanInSchool);
        requst.setAttribute("scaleTypes", scaleTypes);
        requst.setAttribute("scaleSources", scaleSources);
        requst.setAttribute("scaleList", scaleList);
        requst.setAttribute("scaleListSize", scaleList.size());
        ScaleFilterParam sfp = new ScaleFilterParam();
        requst.setAttribute("scaleFilterParam", sfp);
        logservice.log(requst, "心理检测中心:专业量表", scale.getTitle() + "删除");
        return viewName("lookScales");
    }

    @RequestMapping(value = "{scaleid}/scalenorm", method = RequestMethod.GET)
    public String scalenorm(HttpServletRequest requst, Model model, @PathVariable("scaleid") String scalecode) {
        // Scale scale = cachedScaleMgr.get(scalecode);
        // String scalename = scale.getTitle();
        // List<ScaleNormLog> scaleNormLogList =
        // scaleService.getScaleNormLog(Integer.parseInt(scalecode));
        // requst.setAttribute("scaleNormLogList", scaleNormLogList);
        // requst.setAttribute("scalename", scalename);
        requst.setAttribute("userlevel", Constants.APPLICATION_USERLEVEL);
        requst.setAttribute("areacode", Constants.APPLICATION_CITYCODE);
        requst.setAttribute("scaleid", scalecode);
        return viewName("scalenorm");
    }

    @RequestMapping(value = "{scaleid}/{orglevel}/{normtype}/scalenormloglist", method = RequestMethod.GET)
    public String scalenormlist(@CurrentOrg Organization orgEntity, HttpServletRequest requst, Model model,
            @PathVariable("scaleid") String scalecode, @PathVariable("orglevel") String orglevel,
            @PathVariable("normtype") String normtype) {
        Scale scale = cachedScaleMgr.get(scalecode);
        String scalename = scale.getTitle();
        NormInfo norminfo = new NormInfo();
        int level = Integer.parseInt(orglevel);
        norminfo.setOrglevel(level);
        norminfo.setType(Integer.parseInt(normtype));
        norminfo.setScaleId(Integer.parseInt(scalecode));
        if (level == 6)
            norminfo.setOrgid(orgEntity.getId());
        List<NormInfo> scaleNormLogList = scaleService.getScaleNorminfo(norminfo);
        requst.setAttribute("scaleNormLogList", scaleNormLogList);
        requst.setAttribute("scalename", scalename);
        requst.setAttribute("orglevel", orglevel);
        requst.setAttribute("oplevel", orgEntity.getOrgLevel());
        requst.setAttribute("normtype", normtype);
        requst.setAttribute("userlevel", Constants.APPLICATION_USERLEVEL);
        requst.setAttribute("areacode", Constants.APPLICATION_CITYCODE);
        if (Constants.APPLICATION_USERLEVEL.equals("3")) {
            List<District> countyList = districtService.getCounties(Constants.APPLICATION_CITYCODE);
            requst.setAttribute("countylist", countyList);
        }
        return viewName("scalenormcon");
    }

    @RequestMapping(value = "{scaleid}/importscalenorm", method = RequestMethod.GET)
    public String importscalenorm(HttpServletRequest requst, Model model, @PathVariable("scaleid") String scaleid) {
        model.addAttribute(Constants.OP_NAME, Constants.IMPORT_NAME);
        model.addAttribute("scaleid", scaleid);
        return viewName("editform");
    }

    @RequestMapping(value = "{scaleid}/createscalenorm", method = RequestMethod.GET)
    public String createscalenorm(HttpServletRequest requst, Model model, @PathVariable("scaleid") String scaleid) {
        model.addAttribute(Constants.OP_NAME, Constants.ADD_NAME);
        model.addAttribute("scaleid", scaleid);
        Scale scale = cachedScaleMgr.get(scaleid);
        model.addAttribute("scalename", scale.getTitle());
        return viewName("editform");
    }

    @RequestMapping(value = "{scaleid}/{normid}/viewnorm", method = RequestMethod.GET)
    public String viewnorm(HttpServletRequest requst, Model model, @PathVariable("scaleid") String scaleid,
            @PathVariable("normid") String normid) {
        Scale scale = cachedScaleMgr.get(scaleid);

        List<Scalenorm> normlist = scaleService.getNormById(scaleid, Integer.parseInt(normid));
        Map normMap = new HashMap<String, List>();
        Map normobjMap = null;
        for (int i = 0; i < normlist.size(); i++) {
            Scalenorm norm = normlist.get(i);
            int grade = norm.getGradeOrderId();
            int gender = norm.getGender();
            String wid = norm.getwId();
            float m = norm.getM();
            float sd = norm.getSd();
            Dimension dim = scale.findDimension(wid);
            String dimtitle = dim.getTitle();
            Normobject no = new Normobject();
            no.setDimtitle(dimtitle);

            no.setM(String.valueOf(m));
            no.setSd(String.valueOf(sd));
            String key = grade + "_" + gender;
            if (normMap.get(key) == null) {
                normobjMap = new HashMap();
                normMap.put(key, normobjMap);
            } else {
                normobjMap = (Map) normMap.get(key);
            }
            normobjMap.put(dimtitle, no);

        }
        model.addAttribute("dimlist", scale.getDimensions());
        model.addAttribute("normMap", normMap);
        return viewName("normdetail");
    }

    @RequestMapping(value = "{scaleid}/{normid}/exportnorm", method = RequestMethod.GET)
    @ResponseBody
    public void exportnorm(HttpServletRequest request, HttpServletResponse response, Model model,
            @PathVariable("scaleid") String scaleid, @PathVariable("normid") String normid) {
        Scale scale = cachedScaleMgr.get(scaleid);
        String scalename = scale.getTitle();
        String filename = "";
        try {
            OutputStream outputStream = response.getOutputStream();

            filename = FileOperate.encodeFilename(scalename + "常模表.xls", request);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            scaleService.exportScaleNorm(scale, Integer.parseInt(normid), outputStream);
            // response.setHeader("Content-disposition", "attachment;filename="
            // + filename);

            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {

        }

    }

    @RequestMapping(value = "deletenorm", method = RequestMethod.POST)
    @ResponseBody
    public String deletenorm(HttpServletRequest requst, Model model) {
        // Scale scale = cachedScaleMgr.get(scaleid);
        String scaleid = requst.getParameter("scaleid");
        String normid = requst.getParameter("normid");
        int r = scaleService.deleteScaleNorm(scaleid, Integer.parseInt(normid));
        logservice.log(requst, "心理检测中心:专业量表", "常模删除");
        if (r > 0)
            return "success";
        else
            return "failed";
    }

    @RequestMapping(value = "/redirectToImportScale", method = RequestMethod.GET)
    public String redirectToImportScale() {
        // return viewName("uploadscale");
        return viewName("scaleimport");
    }

    @RequestMapping(value = "{scaleid}/download", method = RequestMethod.GET)
    public @ResponseBody void downloadScale(HttpServletRequest requst, HttpServletResponse response, Model model,
            @PathVariable("scaleid") String scalecode) throws Exception {
        String scaletitle = cachedScaleMgr.get(scalecode).getTitle();
        // response.setContentType("application/vnd.ms-excel");
        response.setContentType("application/vnd.ms-word");
        // response.setHeader("Content-disposition","attachment; filename="+ new
        // String((scaletitle+".xlsx").getBytes("gb2312"), "iso-8859-1"));
        Map saveinfo = new HashMap<String, String>();
        FileInputStream fileInputStream = scaleService.downloadScale(scaletitle, saveinfo);
        String ext = saveinfo.get("extendname").toString();
        // if(ext.equals("docx")){
        response.setContentType("application/vnd.ms-word");
        response.setHeader("Content-disposition",
                "attachment;   filename=" + new String((scaletitle + "." + ext).getBytes("gb2312"), "iso-8859-1"));
        // }
        // if(ext.equals("doc")){
        // response.setContentType("application/ostet-stream");
        // response.setHeader("Content-disposition","attachment; filename="+ new
        // String((scaletitle+".zip").getBytes("gb2312"), "iso-8859-1"));
        // }

        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[4096];
        int i = -1;
        while ((i = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, i);
        }
        outputStream.flush();
        outputStream.close();
    }

    @RequestMapping(value = "/updateShowTitle", method = RequestMethod.POST)
    @ResponseBody
    public boolean updateScaleShowTitle(String code, String showtitle) {
        boolean updateShowtitle = scaleService.updateShowTitle(code, showtitle);
        if (updateShowtitle == true) {
            cachedScaleMgr.refresh(code);
            return true;
        } else {
            return false;
        }
    }
}
