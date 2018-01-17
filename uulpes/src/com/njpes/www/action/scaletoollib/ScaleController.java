package com.njpes.www.action.scaletoollib;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.scaletoollib.QueryInfo;
import com.njpes.www.entity.scaletoollib.ScaleInfo;
import com.njpes.www.service.scaletoollib.ScaleService;
import com.njpes.www.utils.FileOperate;

import edutec.admin.ExportUtils;
import edutec.scale.db.CachedScaleMgr;
import edutec.scale.model.Scale;
import edutec.scale.util.ScaleUtils;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import heracles.web.config.ApplicationConfiguration;
import heracles.web.freemarker.FreemarkerCfg;

@Controller
@RequestMapping("/assessmentcenter/scalemanager")
public class ScaleController extends BaseController {
    @Autowired
    ScaleService scaleService;
    @Autowired
    private CachedScaleMgr cachedScaleMgr;

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/scalelist", method = RequestMethod.GET)
    public String scalelist(HttpServletRequest request, HttpServletResponse response, Model model)
            throws IOException, TemplateException {

        List<ScaleInfo> scaleList = scaleService.QueryScaleList(new QueryInfo());
        model.addAttribute("scaleList", scaleList);
        /*
         * //return viewName("scaleList"); //File templateDir =
         * ApplicationConfiguration.getInstance().makeWorkSubDir1(ScaleConstants
         * .SCALE_RPT_DIR); FreemarkerCfg reportCfg =
         * FreemarkerCfg.newInstance(); WebApplicationContext
         * webApplicationContext =
         * ContextLoader.getCurrentWebApplicationContext(); ServletContext
         * servletContext = webApplicationContext.getServletContext();
         * reportCfg.setServletContextForTemplateLoading(servletContext,
         * "/views/ftl");
         * //reportCfg.setDirectoryForTemplateLoading(templateDir); //Template t
         * = reportCfg.getTemplate("scaleList.flt"); Template t =
         * reportCfg.getTemplate("report.flt");
         * response.setCharacterEncoding("utf-8");
         * response.setContentType("application/msword"); //
         * 设置浏览器以下载的方式处理该文件默认名为resume.doc
         * response.addHeader("Content-Disposition",
         * "attachment;filename=resume.doc"); //Writer out =
         * response.getWriter(); ServletOutputStream out =
         * response.getOutputStream(); Map root = new HashMap();
         * 
         * String name = "temp" + (int) (Math.random() * 100000) + ".doc"; File
         * f = new File(name); Writer w = new OutputStreamWriter(new
         * FileOutputStream(f), "utf-8");
         * 
         * root.put("scaleList", scaleList); root.put("name", "");
         * t.process(root, w); w.close(); FileInputStream fin = new
         * FileInputStream(f); IOUtils.copy(fin, out);
         * IOUtils.closeQuietly(out); IOUtils.closeQuietly(fin); return null;
         */
        return viewName("scaletablelist");
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public String download(HttpServletRequest request, HttpServletResponse response, Model model)
            throws IOException, TemplateException {

        // List<ScaleInfo> scaleList = scaleService.QueryScaleList(new
        // QueryInfo());
        // model.addAttribute("scaleList", scaleList);

        // return viewName("scaleList");
        // File templateDir =
        // ApplicationConfiguration.getInstance().makeWorkSubDir1(ScaleConstants.SCALE_RPT_DIR);
        FreemarkerCfg reportCfg = FreemarkerCfg.newInstance();
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        reportCfg.setServletContextForTemplateLoading(servletContext, "/views/ftl");
        // reportCfg.setDirectoryForTemplateLoading(templateDir);
        // Template t = reportCfg.getTemplate("scaleList.flt");
        Template t = reportCfg.getTemplate("report.flt");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/msword");
        // 设置浏览器以下载的方式处理该文件默认名为resume.doc
        response.addHeader("Content-Disposition", "attachment;filename=resume.doc");
        // Writer out = response.getWriter();
        ServletOutputStream out = response.getOutputStream();
        Map root = new HashMap();

        String name = "temp" + (int) (Math.random() * 100000) + ".doc";
        File f = new File(name);
        Writer w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");

        // root.put("scaleList", scaleList);
        root.put("name", "");
        t.process(root, w);
        w.close();
        FileInputStream fin = new FileInputStream(f);
        IOUtils.copy(fin, out);
        IOUtils.closeQuietly(out);
        IOUtils.closeQuietly(fin);
        return null;
        // return viewName("scaletablelist");
    }

    @RequestMapping(value = "/scaleimport", method = RequestMethod.GET)
    public String scaleimport(HttpServletRequest request, Model model) {
        List<ScaleInfo> scaleList = scaleService.QueryScaleList(new QueryInfo());
        model.addAttribute("list", scaleList);
        return viewName("scaleimport");
    }

    @RequestMapping(value = "/importscale", method = RequestMethod.POST)
    public String importScaleFromXls(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model)
            throws Exception {
        // try {
        Account account = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        long userid = account.getId();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile mf = entity.getValue();
            InputStream stream = mf.getInputStream();
            String fileName = mf.getOriginalFilename();

            // if(!StringUtils.endsWith(fileName,".xls")||!StringUtils.endsWith(fileName,".xlsx")){
            // System.out.println("导入的模板不是Excel文件，请重新选择!");
            // break;
            // }

            String saveFilePath = ApplicationConfiguration.getInstance().getWorkDir() + "\\" + ExportUtils.DATA_TMP_DIR;
            // FileOperate.saveFile(saveFilePath, fileName, mf);
            String resultStr = scaleService.ImportScale(stream, saveFilePath + "\\" + fileName, userid, orgEntity);
            model.addAttribute("resultStr", resultStr);
            Scale scale = this.cachedScaleMgr.get(resultStr);

            /*
             * String saveFileName = null; String title = scale.getTitle();
             * if(StringUtils.endsWith(fileName,".xlsx")) saveFileName=
             * title+".xlsx"; if(StringUtils.endsWith(fileName,".xls"))
             * saveFileName= title+".xls";
             * if(StringUtils.endsWith(fileName,".zip")) saveFileName=
             * title+".zip"; FileOperate.saveFile(saveFilePath, saveFileName,
             * mf);
             */
            model.addAttribute("scale", scale);
            String str = scale.toQuestionHTML();
            model.addAttribute("toQuestionHTML", str);
            String dimstr = scale.toDimHTML();
            model.addAttribute("toDimHTML", dimstr);
            return viewName("scaleimportresult");
        }
        // }//catch(Exception e){
        // System.out.println(e.getMessage());
        // }
        return viewName("scaleimportresult");
    }

    @RequestMapping(value = "/importscalequestionxls", method = RequestMethod.POST)
    public String importScaleQuestionFromXls(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            Model model) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile mf = entity.getValue();
            InputStream stream = mf.getInputStream();
            String fileName = mf.getOriginalFilename();
            String saveFilePath = ApplicationConfiguration.getInstance().getWorkDir() + "\\" + ExportUtils.DATA_TMP_DIR;
            // String resultStr =
            // scaleService.ImportScale(stream,saveFilePath+"\\"+fileName,userid,orgEntity);
            String resultStr = scaleService.ImportScaleQuestion(stream, saveFilePath + "\\" + fileName, orgEntity);
            Scale scale = this.cachedScaleMgr.get(resultStr);

            String saveFileName = null;
            String title = scale.getTitle();
            if (StringUtils.endsWith(fileName, ".xlsx"))
                saveFileName = title + ".xlsx";
            if (StringUtils.endsWith(fileName, ".xls"))
                saveFileName = title + ".xls";
            if (StringUtils.endsWith(fileName, ".zip"))
                saveFileName = title + ".zip";
            FileOperate.saveFile(saveFilePath, saveFileName, mf);
            return viewName("scaleimportresult");
        }
        return viewName("scaleimportresult");
    }

    @RequestMapping(value = "/importscalequestionword", method = RequestMethod.POST)
    public String importScaleQuestionFromWord(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            Model model) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile mf = entity.getValue();
            InputStream stream = mf.getInputStream();
            String fileName = mf.getOriginalFilename();
            String saveFilePath = ApplicationConfiguration.getInstance().getWorkDir() + "\\"
                    + ExportUtils.SCALE_TMP_DIR;
            String saveFileName = null;
            FileOperate.saveFile(saveFilePath, fileName, mf);
            return viewName("scalequestionimportresult");
        }
        return viewName("scalequestionimportresult");
    }

    @RequestMapping(value = "{scaleid}/importscalenorm", method = RequestMethod.POST)
    public String importScaleNormFromXls(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model)
            throws Exception {
        // try {
        long orgid = orgEntity.getId();
        Account account = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
        long userid = account.getId();
        String scaleid = request.getParameter("scaleid");
        int orglevel = orgEntity.getOrgLevel();
        String editer = request.getParameter("editer");
        String edittime = request.getParameter("edittime");
        int areaid = 0;
        String countyid = orgEntity.getCountyid();
        String cityid = orgEntity.getCityid();
        if (orglevel == 3)
            areaid = Integer.parseInt(cityid);
        if (orglevel == 4)
            areaid = Integer.parseInt(countyid);

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile mf = entity.getValue();
            InputStream stream = mf.getInputStream();
            String fileName = mf.getOriginalFilename();

            if (StringUtils.endsWith(fileName, ".xlsx") || StringUtils.endsWith(fileName, ".xls")) {
                String saveFilePath = ApplicationConfiguration.getInstance().getWorkDir() + "\\" + ExportUtils.DATA_TMP_DIR;
                String resultStr = scaleService.ImportScaleNorm(stream, orgid, orglevel, scaleid, areaid, userid, null,
                        editer, edittime, saveFilePath + "\\" + fileName);
            }else{
                throw new Exception("导入的模板不是Excel文件，请重新选择!");
            }
            // return viewName("scaleimportresult");

        }
        String returnurl = "../../scaletoollib/scalelook/" + scaleid + "/scalenorm";
        return viewName(returnurl);
    }

    @RequestMapping(value = "createnorm", method = RequestMethod.POST)
    @ResponseBody
    public String createnorm(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        Account account = (Account) request.getSession().getAttribute("user");
        long userid = account.getId();
        String scaleid = request.getParameter("scaleid");
        String normname = request.getParameter("normname");
        String description = request.getParameter("description");
        String starttime = request.getParameter("starttime");
        String endtime = request.getParameter("endtime");
        // Scale scale = cachedScaleMgr.get(scaleid);
        return scaleService.createNorm(scaleid, orgEntity, userid, normname, description, starttime, endtime);
        // String returnUrl =
        // "../../scaletoollib/scalelook/"+scaleid+"/"+orgEntity.getOrgLevel()+"/2/scalenormloglist.do";
        // return redirectToUrl(viewName(returnUrl));
        // return "success";
        // String returnurl =
        // "../../scaletoollib/scalelook/"+scaleid+"/scalenorm";
        // return viewName(returnurl);
    }

    @RequestMapping(value = "/uploadStudentAnswerXls", method = RequestMethod.POST)
    public String uploadStudentAnswerXls(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            HttpServletResponse response) {
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
                Map<Object, Object> page = new HashMap<Object, Object>();
                scaleService.importStudentAnswerFromXls(orgEntity.getId(), inputStream, page);
                break;
                // doRead(inputStream);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return viewName("");
    }

    @RequestMapping(value = "/uploadTeacherAnswerXls", method = RequestMethod.POST)
    public String uploadTeacherAnswerXls(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            HttpServletResponse response) {
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
                Map<Object, Object> page = new HashMap<Object, Object>();
                scaleService.importTeacherAnswerFromXls(orgEntity.getId(), inputStream, page);
                break;
                // doRead(inputStream);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return viewName("");
    }

    /*
     * @RequestMapping(value="/download_scale", method=RequestMethod.GET) public
     * void download_scale(HttpServletRequest request,HttpServletResponse
     * response){ String scaleId =request.getParameter("scaleId"); String title
     * = request.getParameter("title"); //Scale scale =
     * CachedScaleMgr.getInstance().get(scaleId); File root = new
     * File(ApplicationConfiguration.getInstance().getWebappRoot()); File store
     * = new File(root, "scalefiles"); //File tempF = new File(store,
     * scale.getId() + ".doc"); File tempF = new File(store, scaleId + ".doc");
     * //String wordName = scale.getTitle() + ".doc"; String wordName = title +
     * ".doc"; OutputStream output = null; InputStream input = null; try {
     * ServletRequestUtils.setDownLoadDocHead(response, wordName); input =
     * FileUtils.openInputStream(tempF); output = response.getOutputStream();
     * IOUtils.copy(input, output); output.flush(); } catch (Exception e) {
     * 
     * } finally { IOUtils.closeQuietly(output); IOUtils.closeQuietly(input); }
     * }
     */
    @RequestMapping(value = "/downloadanswertemp", method = RequestMethod.GET)
    public void downloadAnswerTemp(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            HttpServletResponse response, Model model) {

        String type = request.getParameter("type");
        String scaleid = request.getParameter("scaleid");
        long teacherRole, orgid, nj, bj;
        String njmc, bjmc;
        String filename = FileOperate.encodeFilename("答案导出模板.xls", request);
        // Map<String, Object> paramets = new HashMap<String, Object>();
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
            if (type.equals("1"))// 导出小学生答案模板
            {
                orgid = orgEntity.getId();
                njmc = request.getParameter("njmc");
                bjmc = request.getParameter("bjmc");
                nj = Integer.parseInt(request.getParameter("nj"));
                bj = Integer.parseInt(request.getParameter("bj"));
                scaleService.downloadAnswerModelForStu(scaleid, orgid, nj, bj, outputStream);
            }
            if (type.equals("2"))// 导出教师答案模板
            {
                // teacherRole =
                // Integer.parseInt(request.getParameter("teacherRole"));
                // AccountOrgJob org = account.getOrganizationJobs().get(0);
                // xxdm = org.getOrgId();
                // scaleService.downloadAnswerModelForTeach(scaleid,xxdm,teacherRole,outputStream);
            }
            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @RequestMapping(value = "{scaleid}/downloadscalenormtemp", method = RequestMethod.GET)
    public void downloadScaleNormTemp(@CurrentOrg Organization orgEntity, HttpServletRequest request,
            HttpServletResponse response, Model model, @PathVariable("scaleid") String scaleid) {
        Scale scale = cachedScaleMgr.get(scaleid);
        String scalename = scale.getTitle();
        String gradegroups = scale.getApplicablePerson();

        String filename = "";
        try {
            OutputStream outputStream = response.getOutputStream();
            if (ScaleUtils.isThirdAngleScale(scaleid)) {
                response.setContentType("application/x-msdownload;charset=UTF-8");

                InputStream in = null;
                ApplicationConfiguration cfg = ApplicationConfiguration.getInstance();
                String tempFileName = "";
                if (ScaleUtils.isThirdAngleScaleP(scaleid)) {
                    tempFileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/小学生心理健康量表常模导入模板.zip");
                    filename = FileOperate.encodeFilename("小学生心理健康量表常模导入模板.zip", request);
                }
                if (ScaleUtils.isThirdAngleScaleM(scaleid)) {
                    tempFileName = FilenameUtils.concat(cfg.getWorkDir(), "resource/中学生心理健康量表常模导入模板.zip");
                    filename = FileOperate.encodeFilename("中学生心理健康量表常模导入模板.zip", request);
                }
                in = new BufferedInputStream(new FileInputStream(tempFileName));
                response.setHeader("Content-disposition", "attachment;filename=" + filename);
                IOUtils.copy(in, outputStream);

            } else {
                filename = FileOperate.encodeFilename(scalename + "常模导入模板.xls", request);
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-disposition", "attachment;filename=" + filename);
                scaleService.downloadscalenormtemp(scale, outputStream);
                // response.setHeader("Content-disposition",
                // "attachment;filename=" + filename);
            }
            // response.setHeader("Content-disposition", "attachment;filename="
            // + filename);

            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
