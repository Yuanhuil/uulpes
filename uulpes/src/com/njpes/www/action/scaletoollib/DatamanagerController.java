package com.njpes.www.action.scaletoollib;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.njpes.www.action.BaseController;
import com.njpes.www.dao.baseinfo.OrganizationMapper;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.invoker.DownloadScaleAnswerTmpInvoker;
import com.njpes.www.invoker.ScaleAnswerInvoker;
import com.njpes.www.service.baseinfo.RoleServiceI;
import com.njpes.www.service.scaletoollib.ScaleService;
import com.njpes.www.utils.FileOperate;

import edu.emory.mathcs.backport.java.util.Arrays;
import edutec.scale.db.CachedScaleMgr;
import edutec.scale.util.ScaleUtils;

@Controller
@RequestMapping("/assessmentcenter/datamanager")
public class DatamanagerController extends BaseController {
    @Autowired
    ScaleService scaleService;
    @Autowired
    private CachedScaleMgr cachedScaleMgr;
    @Autowired
    private OrganizationMapper organizationMaper;
    @Autowired
    private RoleServiceI roleService;
    @Autowired
    ScaleAnswerInvoker scaleAnswerInvoker;
    @Autowired
    DownloadScaleAnswerTmpInvoker downloadScaleAnswerTmpInvoker;

    @RequestMapping(value = "/uploadStudentAnswer", method = RequestMethod.POST)
    public String uploadStudentAnswer(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        Map<Object, Object> page = new HashMap<Object, Object>();
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
                MultipartFile mf = entity.getValue();
                String fileName = mf.getOriginalFilename();

                if (StringUtils.endsWith(fileName, ".xls") || StringUtils.endsWith(fileName, ".xlsx")) {
                    InputStream inputStream = mf.getInputStream();
                    // Map<String,Object> page = new HashMap<String,Object>();

                    // scaleService.importStudentAnswerFromXls(orgEntity.getId(),inputStream,page);
                    scaleAnswerInvoker.importStudentAnswerFromXls(orgEntity.getId(), inputStream, page);
                    model.addAttribute("resultMsgList", page.get("resultMsgList"));
                    break;
                    // doRead(inputStream);
                } else {
                    System.out.println("导入的答案非Excel文件");
                }
            }
        } catch (Exception e) {
            // System.out.println(e.getMessage());
            // throw new RuntimeException(e.getMessage());
            model.addAttribute("resultMsgList", page.get("resultMsgList"));
            return viewName("answeruploadresult");
        }

        return viewName("answeruploadresult");
    }

    @RequestMapping(value = "/uploadTeacherAnswer", method = RequestMethod.POST)
    public String uploadTeacherAnswer(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        Map<Object, Object> page = new HashMap<Object, Object>();
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
                MultipartFile mf = entity.getValue();
                String fileName = mf.getOriginalFilename();

                if (!StringUtils.endsWith(fileName, ".xls") || !StringUtils.endsWith(fileName, ".xlsx")) {
                    // modelAndView.getModel().put(key, value);
                    System.out.println("导入的答案非Excel文件");
                }
                InputStream inputStream = mf.getInputStream();
                // Map<String,Object> page = new HashMap<String,Object>();

                // scaleService.importTeacherAnswerFromXls(orgEntity.getId(),inputStream,page);
                scaleAnswerInvoker.importTeacherAnswerFromXls(orgEntity.getId(), inputStream, page);
                model.addAttribute("resultMsgList", page.get("resultMsgList"));
                break;
                // doRead(inputStream);
            }
        } catch (Exception e) {
            // System.out.println(e.getMessage());
            model.addAttribute("resultMsgList", page.get("resultMsgList"));
            return viewName("answeruploadresult");
        }

        return viewName("answeruploadresult");
    }

    @RequestMapping(value = "/downloadStudentAnswerTemp", method = RequestMethod.POST)
    public void downloadStudentAnswerTemp(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        String scaleid = request.getParameter("scaleId");
        String scaleName = cachedScaleMgr.get(scaleid).getTitle();
        long orgid, nj, bj;
        String filename = FileOperate.encodeFilename(scaleName + "答案导入模板.xlsx", request);
        try {
            if (ScaleUtils.isThirdAngleScale(scaleid)) {
                if(ScaleUtils.isThirdAngleScaleP(scaleid)){
                    filename = FileOperate.encodeFilename("小学生心理健康量表答案导入模板.zip", request);
                }else if(ScaleUtils.isThirdAngleScaleM(scaleid)){
                    filename = FileOperate.encodeFilename("中学生心理健康量表答案导入模板.zip", request);
                }
                response.setContentType("application/x-msdownload;charset=UTF-8");
            } else
                response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream outputStream = response.getOutputStream();
            {
                orgid = orgEntity.getId();
                nj = Integer.parseInt(request.getParameter("nj"));
                bj = Integer.parseInt(request.getParameter("bj"));
                downloadScaleAnswerTmpInvoker.downloadStudentAnswerTemp(orgid, nj, bj, scaleid, 1, outputStream);
            }
            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @RequestMapping(value = "/downloadTeacherAnswerTemp", method = RequestMethod.POST)
    public void downloadTeacherAnswerTemp(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            HttpServletResponse response, Model model) {

        String scaleid = request.getParameter("scaleName");
        String scaleName = cachedScaleMgr.get(scaleid).getTitle();
        long orgid;
        String filename = FileOperate.encodeFilename(scaleName + "答案导入模板.xlsx", request);
        try {
            if (ScaleUtils.isThirdAngleScale(scaleid)) {
                if(ScaleUtils.isThirdAngleScaleP(scaleid)){
                    filename = FileOperate.encodeFilename("小学生心理健康量表答案导入模板.zip", request);
                }else if(ScaleUtils.isThirdAngleScaleM(scaleid)){
                    filename = FileOperate.encodeFilename("中学生心理健康量表答案导入模板.zip", request);
                }
                response.setContentType("application/x-msdownload;charset=UTF-8");
            } else
                response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream outputStream = response.getOutputStream();
            {
                int teacherRole = Integer.parseInt(request.getParameter("roleid"));
                orgid = orgEntity.getId();
                String roleName = null;
                if (teacherRole != -1) {
                    Role role = roleService.selectRole(teacherRole);
                    roleName = role.getRoleName();
                }
                downloadScaleAnswerTmpInvoker.downloadTeacherAnswerTemp(orgid, roleName, teacherRole, scaleid,
                        outputStream);
            }
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @RequestMapping(value = "/downloadStuAnswerForSch", method = RequestMethod.GET)
    public void downloadStuAnswerForSch(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            HttpServletResponse response, Model model) {

    }

    @RequestMapping(value = "/downloadTchAnswerForSch", method = RequestMethod.GET)
    public void downloadTchAnswerForSch(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            HttpServletResponse response, Model model) {

    }

    @RequestMapping(value = "/downloadStuAnswerForCityEdu", method = RequestMethod.GET)
    public void downloadStuAnswerForCityEdu(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        String[] areaIdstr = request.getParameterValues("");
        List areaids = Arrays.asList(areaIdstr);
        List<Organization> schools = organizationMaper.getSchoolOrgByCountyIds(areaids);
    }

    @RequestMapping(value = "/downloadTchAnswerForCityEdu", method = RequestMethod.GET)
    public void downloadTchAnswerForCityEdu(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            HttpServletResponse response, Model model) {

    }

    @RequestMapping(value = "/downloadStuAnswerForCountyEdu", method = RequestMethod.GET)
    public void downloadStuAnswerForCountyEdu(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            HttpServletResponse response, Model model) {

    }

    @RequestMapping(value = "/downloadTchAnswerForCountyEdu", method = RequestMethod.GET)
    public void downloadTchAnswerForCountyEdu(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            HttpServletResponse response, Model model) {

    }

}
