package com.njpes.www.action.scaletoollib;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.njpes.www.action.BaseController;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.CurrentUser;
import com.njpes.www.service.baseinfo.AccountServiceI;
import com.njpes.www.service.baseinfo.StudentServiceI;
import com.njpes.www.service.scaletoollib.ReportLookService;
import com.njpes.www.service.scaletoollib.ScaleService;
import com.njpes.www.utils.Zip;

import edutec.scale.model.Scale;
import edutec.scale.util.ScaleUtils;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import heracles.jfree.JChartCreator;
import heracles.jfree.JChartParam;
import heracles.jfree.bean.ChartParamBean;
import heracles.web.config.ApplicationConfiguration;
import heracles.web.freemarker.FreemarkerCfg;

@Scope("prototype")
@Controller
@RequestMapping("/assessmentcenter/report")
public class ReportController extends BaseController {
    @Autowired
    StudentServiceI studentService;
    @Autowired
    ScaleService scaleService;
    @Autowired
    AccountServiceI accountService;
    @Autowired
    private ReportLookService reportLookService;

    @RequestMapping(value = "/studentPersonalReport", method = RequestMethod.GET)
    public String getStudentReport(@CurrentUser Account account, HttpServletRequest request,
            HttpServletResponse response, Model model) throws Exception {
        String subjectUserIdStr = request.getParameter("userid");
        long subjectUserId = -1;
        long resultId = -1;
        if (subjectUserIdStr != null)
            subjectUserId = Integer.parseInt(subjectUserIdStr);
        long accountid = account.getId();
        int observerTypeFlag = account.getTypeFlag();
        Object observerUser = accountService.getAccountInfo(accountid, observerTypeFlag);
        String resultIdStr = request.getParameter("resultId");
        if (resultIdStr != null)
            resultId = Integer.parseInt(resultIdStr);
        String download = request.getParameter("download");// 是否含有download参数，即是否下载
        boolean isDownload;
        if (!"yes".equals(download))
            isDownload = false;
        else
            isDownload = true;
        Map<Object, Object> page = null;

        page = scaleService.getStudentPersonalReporter(subjectUserId, observerUser, resultId, null, isDownload);

        if (!"yes".equals(download)) {
            model.addAttribute("page", page);
            return viewName("studentsinglereport");
        } else {
            try {
                // response.setContentType("application/vnd.ms-word");
                Map userinfo = (Map) page.get("userinfo");
                Scale scale = (Scale) page.get("scale");
                String scaleTitle = scale.getTitle();
                String reportTitle = userinfo.get("name").toString() + "《" + scaleTitle + "》个体检测报告.doc";
                response.setHeader("Content-disposition",
                        "attachment;   filename=" + new String((reportTitle).getBytes("gb2312"), "iso-8859-1"));
                // model.addAttribute("page", page);
                // return viewName("studentsinglereport");
                FreemarkerCfg reportCfg = FreemarkerCfg.newInstance();
                WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
                ServletContext servletContext = webApplicationContext.getServletContext();
                reportCfg.setServletContextForTemplateLoading(servletContext, "/views/ftl");
                Template t = reportCfg.getTemplate("studentpersonalreport.flt");

                ServletOutputStream out = response.getOutputStream();
                Writer w = new OutputStreamWriter(out, "utf-8");
                t.process(page, w);
                w.close();
                IOUtils.closeQuietly(out);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @RequestMapping(value = "/studentThreeAngleReportForStudent", method = RequestMethod.GET)
    public String studentThreeAngleReportForStudent(@CurrentUser Account account, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        String subjectUserIdStr = request.getParameter("userid");
        long subjectUserId = -1;
        long resultId = -1;
        if (subjectUserIdStr != null)
            subjectUserId = Integer.parseInt(subjectUserIdStr);
        long accountid = account.getId();
        int observerTypeFlag = account.getTypeFlag();
        Object observerUser = accountService.getAccountInfo(accountid, observerTypeFlag);
        String resultIdStr = request.getParameter("resultId");
        if (resultIdStr != null)
            resultId = Integer.parseInt(resultIdStr);
        String download = request.getParameter("download");// 是否含有download参数，即是否下载
        boolean isDownload;
        if (!"yes".equals(download))
            isDownload = false;
        else
            isDownload = true;
        Map<Object, Object> page = scaleService.GetThreeAngleReporterForStudent(subjectUserId, observerUser, resultId,
                null, isDownload);

        if (!"yes".equals(download)) {
            model.addAttribute("page", page);
            return viewName("studentsinglereport");
        } else {
            try {
                // response.setContentType("application/pdf");
                Map userinfo = (Map) page.get("userinfo");
                Scale scale = (Scale) page.get("scale");
                String scaleTitle = scale.getTitle();
                String reportTitle = userinfo.get("name").toString() + "《" + scaleTitle + "》个体检测报告.doc";
                response.setHeader("Content-disposition",
                        "attachment;   filename=" + new String((reportTitle).getBytes("gb2312"), "iso-8859-1"));

                FreemarkerCfg reportCfg = FreemarkerCfg.newInstance();
                WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
                ServletContext servletContext = webApplicationContext.getServletContext();
                reportCfg.setServletContextForTemplateLoading(servletContext, "/views/ftl");
                Template t = reportCfg.getTemplate("personalreport.flt");

                // Writer out = response.getWriter();
                ServletOutputStream out = response.getOutputStream();
                Writer w = new OutputStreamWriter(out, "utf-8");
                t.process(page, w);
                w.close();
                IOUtils.closeQuietly(out);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @RequestMapping(value = "/studentThreeAngleReportForTeacher", method = RequestMethod.GET)
    public String studentThreeAngleReportForTeacher(@CurrentUser Account account, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        String subjectUserIdStr = request.getParameter("userid");
        long subjectUserId = -1;
        long resultId = -1;
        if (subjectUserIdStr != null)
            subjectUserId = Integer.parseInt(subjectUserIdStr);
        long accountid = account.getId();
        int observerTypeFlag = account.getTypeFlag();
        Object observerUser = accountService.getAccountInfo(accountid, observerTypeFlag);
        String resultIdStr = request.getParameter("resultId");
        if (resultIdStr != null)
            resultId = Integer.parseInt(resultIdStr);
        String download = request.getParameter("download");// 是否含有download参数，即是否下载
        boolean isDownload;
        if (!"yes".equals(download))
            isDownload = false;
        else
            isDownload = true;
        Map<Object, Object> page = scaleService.GetThreeAngleReporterForTeacher(subjectUserId, observerUser, resultId,
                null, isDownload);

        if (!"yes".equals(download)) {
            model.addAttribute("page", page);
            return viewName("studentsinglereport");
        } else {
            try {
                // response.setContentType("application/pdf");
                Map userinfo = (Map) page.get("userinfo");
                Scale scale = (Scale) page.get("scale");
                String scaleTitle = scale.getTitle();
                String reportTitle = userinfo.get("name").toString() + "《" + scaleTitle + "》个体检测报告.doc";
                response.setHeader("Content-disposition",
                        "attachment;   filename=" + new String((reportTitle).getBytes("gb2312"), "iso-8859-1"));

                FreemarkerCfg reportCfg = FreemarkerCfg.newInstance();
                WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
                ServletContext servletContext = webApplicationContext.getServletContext();
                reportCfg.setServletContextForTemplateLoading(servletContext, "/views/ftl");
                Template t = reportCfg.getTemplate("personalreport.flt");

                // Writer out = response.getWriter();
                ServletOutputStream out = response.getOutputStream();
                Writer w = new OutputStreamWriter(out, "utf-8");
                t.process(page, w);
                w.close();
                IOUtils.closeQuietly(out);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @RequestMapping(value = "/studentThreeAngleReportForParent", method = RequestMethod.GET)
    public String studentThreeAngleReportForParent(@CurrentUser Account account, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        String subjectUserIdStr = request.getParameter("userid");
        long subjectUserId = -1;
        long resultId = -1;
        if (subjectUserIdStr != null)
            subjectUserId = Integer.parseInt(subjectUserIdStr);
        long accountid = account.getId();
        int observerTypeFlag = account.getTypeFlag();
        Object observerUser = accountService.getAccountInfo(accountid, observerTypeFlag);
        String resultIdStr = request.getParameter("resultId");
        if (resultIdStr != null)
            resultId = Integer.parseInt(resultIdStr);
        String download = request.getParameter("download");// 是否含有download参数，即是否下载
        boolean isDownload;
        if (!"yes".equals(download))
            isDownload = false;
        else
            isDownload = true;
        Map<Object, Object> page = scaleService.GetThreeAngleReporterForParent(subjectUserId, observerUser, resultId,
                null, isDownload);

        if (!"yes".equals(download)) {
            model.addAttribute("page", page);
            return viewName("studentsinglereport");
        } else {
            try {
                // response.setContentType("application/pdf");
                Map userinfo = (Map) page.get("userinfo");
                Scale scale = (Scale) page.get("scale");
                String scaleTitle = scale.getTitle();
                String reportTitle = userinfo.get("name").toString() + "《" + scaleTitle + "》个体检测报告.doc";
                response.setHeader("Content-disposition",
                        "attachment;   filename=" + new String((reportTitle).getBytes("gb2312"), "iso-8859-1"));

                FreemarkerCfg reportCfg = FreemarkerCfg.newInstance();
                WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
                ServletContext servletContext = webApplicationContext.getServletContext();
                reportCfg.setServletContextForTemplateLoading(servletContext, "/views/ftl");
                Template t = reportCfg.getTemplate("personalreport.flt");

                // Writer out = response.getWriter();
                ServletOutputStream out = response.getOutputStream();
                Writer w = new OutputStreamWriter(out, "utf-8");
                t.process(page, w);
                w.close();
                IOUtils.closeQuietly(out);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @RequestMapping(value = "/delstudentPersonalReport", method = RequestMethod.POST)
    @ResponseBody
    public String delstudentPersonalReport(HttpServletRequest request) {
        String resultid = request.getParameter("resultid");
        String scaleid = request.getParameter("scaleid");
        int result = 0;
        if (ScaleUtils.isThirdAngleScale(scaleid)) {
            result = reportLookService.deleteStudentMhExamResult((long) Integer.parseInt(resultid));
        } else
            result = reportLookService.deleteStudentExamResult((long) Integer.parseInt(resultid));
        if (result == 1)
            return "success";
        else
            return "failed";
    }

    @RequestMapping(value = "/delSelectedStudentPersonalReports", method = RequestMethod.POST)
    @ResponseBody
    public String delSelectedStudentPersonalReports(HttpServletRequest request) {
        String resultids = request.getParameter("resultids");
        String scaleid = request.getParameter("scaleid");
        String[] resultArray = resultids.split(",");
        int result = 0;
        if (ScaleUtils.isThirdAngleScale(scaleid)) {
            result = reportLookService.deleteSelectedStudentMhExamResults(resultArray);
        } else
            result = reportLookService.deleteSelectedStudentExamResults(resultArray);
        if (result == 1)
            return "success";
        else
            return "failed";
    }

    @RequestMapping(value = "/downloadSelectedStudentPersonalReports", method = RequestMethod.GET)
    @ResponseBody
    public void downloadSelectedStudentPersonalReports(@CurrentUser Account account, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            String resultids = request.getParameter("resultids");
            String[] resultArray = resultids.split(",");
            // String scaleid = request.getParameter("scaleid");
            // String subjectUserIdStr = request.getParameter("userid");
            long subjectUserId = -1;
            long resultId = -1;
            // if(subjectUserIdStr!=null)
            // subjectUserId = Integer.parseInt(subjectUserIdStr);
            long accountid = account.getId();
            int observerTypeFlag = account.getTypeFlag();
            Object observerUser = accountService.getAccountInfo(accountid, observerTypeFlag);
            Map<Object, Object> page = null;
            String tempFold = UUID.randomUUID().toString();
            String folder = ApplicationConfiguration.getInstance().getWorkDir() + File.separator + "download"
                    + File.separator + tempFold;

            File myfolder = new File(folder);
            boolean canmake = false;
            if (!myfolder.exists()) {
                canmake = myfolder.mkdirs();
            }

            for (int i = 0; i < resultArray.length; i++) {
                resultId = Integer.parseInt(resultArray[i]);
                page = scaleService.getStudentPersonalReporter(subjectUserId, observerUser, resultId, null, true);
                Map userinfo = (Map) page.get("userinfo");
                Scale scale = (Scale) page.get("scale");
                String scaleTitle = scale.getTitle();
                String reportTitle = userinfo.get("name").toString() + "《" + scaleTitle + "》个体检测报告.doc";

                FreemarkerCfg reportCfg = FreemarkerCfg.newInstance();
                WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
                ServletContext servletContext = webApplicationContext.getServletContext();
                reportCfg.setServletContextForTemplateLoading(servletContext, "/views/ftl");
                Template t = reportCfg.getTemplate("studentpersonalreport.flt");
                if (resultArray.length == 1) {
                    response.setHeader("Content-disposition",
                            "attachment;   filename=" + new String((reportTitle).getBytes("gb2312"), "iso-8859-1"));
                    ServletOutputStream out = response.getOutputStream();
                    Writer w = new OutputStreamWriter(out, "utf-8");
                    t.process(page, w);
                    w.close();
                    IOUtils.closeQuietly(out);
                    return;
                }

                File f = new File(myfolder + File.separator + reportTitle);
                if (f.exists()) {
                    f.delete();
                } else {
                    f.createNewFile();
                }
                OutputStream os = new FileOutputStream(f);

                // ServletOutputStream out = response.getOutputStream();
                Writer w = new OutputStreamWriter(os, "utf-8");
                t.process(page, w);
                w.close();
                IOUtils.closeQuietly(os);

                if (i == resultArray.length - 1) {
                    Zip.zip(folder, folder + ".zip");
                    StringBuilder uri = new StringBuilder();
                    uri.append(folder);
                    uri.append(".zip");
                    File file = new File(uri.toString());
                    FileInputStream is = new FileInputStream(file);
                    response.setContentType("application/octet-stream");
                    // response.setContentType("application/x-msdownload;charset=UTF-8");
                    response.setHeader("Content-disposition",
                            "attachment;   filename=" + new String(("个人报告批量下载.zip").getBytes("gb2312"), "iso-8859-1"));

                    OutputStream out = response.getOutputStream();
                    // IOUtils.copy(is, out);
                    byte[] b = new byte[2048];
                    int length;
                    while ((length = is.read(b)) > 0) {
                        out.write(b, 0, length);
                    }

                    out.flush();
                    IOUtils.closeQuietly(is);
                    IOUtils.closeQuietly(out);

                }
            }
        } catch (Exception e) {
            // throw new Exception("下载报告出错");
            return;
        }

    }

    @RequestMapping(value = "/teacherPersonalReport", method = RequestMethod.GET)
    public String getTeacherReport(@CurrentUser Account account, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        String subjectUserIdStr = request.getParameter("userid");
        long subjectUserId = -1;
        long resultId = -1;
        if (subjectUserIdStr != null)
            subjectUserId = Integer.parseInt(subjectUserIdStr);
        long accountid = account.getId();
        int observerTypeFlag = account.getTypeFlag();
        Object observerUser = accountService.getAccountInfo(accountid, observerTypeFlag);
        String resultIdStr = request.getParameter("resultId");
        if (resultIdStr != null)
            resultId = Integer.parseInt(resultIdStr);
        String download = request.getParameter("download");// 是否含有download参数，即是否下载
        boolean isDownload;
        if (!"yes".equals(download))
            isDownload = false;
        else
            isDownload = true;
        Map<Object, Object> page = scaleService.getTeacherPersonalReporter(subjectUserId, observerUser, resultId,
                isDownload);

        if (!"yes".equals(download)) {
            model.addAttribute("page", page);

            return viewName("teachersinglereport");
        } else {
            try {
                // response.setContentType("application/pdf");
                Map userinfo = (Map) page.get("userinfo");
                Scale scale = (Scale) page.get("scale");
                String scaleTitle = scale.getTitle();
                String reportTitle = userinfo.get("name").toString() + "《" + scaleTitle + "》个体检测报告.doc";
                response.setHeader("Content-disposition",
                        "attachment;   filename=" + new String((reportTitle).getBytes("gb2312"), "iso-8859-1"));

                FreemarkerCfg reportCfg = FreemarkerCfg.newInstance();
                WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
                ServletContext servletContext = webApplicationContext.getServletContext();
                reportCfg.setServletContextForTemplateLoading(servletContext, "/views/ftl");
                Template t = reportCfg.getTemplate("teacherpersonalreport.flt");

                // Writer out = response.getWriter();
                ServletOutputStream out = response.getOutputStream();
                Writer w = new OutputStreamWriter(out, "utf-8");
                t.process(page, w);
                w.close();
                IOUtils.closeQuietly(out);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @RequestMapping(value = "/delTeacherPersonalReport", method = RequestMethod.POST)
    @ResponseBody
    public String delTeacherPersonalReport(HttpServletRequest request) {
        String resultid = request.getParameter("resultid");
        int result = reportLookService.deleteTeacherExamResult((long) Integer.parseInt(resultid));
        if (result == 1)
            return "success";
        else
            return "failed";
    }

    @RequestMapping(value = "/delSelectedTeacherPersonalReports", method = RequestMethod.POST)
    @ResponseBody
    public String delSelectedTeacherPersonalReports(HttpServletRequest request) {
        String resultids = request.getParameter("resultids");
        String[] resultArray = resultids.split(",");
        int result = reportLookService.deleteSelectedTeacherExamResults(resultArray);
        if (result == 1)
            return "success";
        else
            return "failed";
    }

    @RequestMapping(value = "/downloadSelectedTeacherPersonalReports", method = RequestMethod.GET)
    @ResponseBody
    public void downloadSelectedTeacherPersonalReports(@CurrentUser Account account, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            String resultids = request.getParameter("resultids");
            String[] resultArray = resultids.split(",");
            // String scaleid = request.getParameter("scaleid");
            // String subjectUserIdStr = request.getParameter("userid");
            long subjectUserId = -1;
            long resultId = -1;
            // if(subjectUserIdStr!=null)
            // subjectUserId = Integer.parseInt(subjectUserIdStr);
            long accountid = account.getId();
            int observerTypeFlag = account.getTypeFlag();
            Object observerUser = accountService.getAccountInfo(accountid, observerTypeFlag);
            Map<Object, Object> page = null;
            String tempFold = UUID.randomUUID().toString();
            String folder = ApplicationConfiguration.getInstance().getWorkDir() + File.separator + "download"
                    + File.separator + tempFold;

            File myfolder = new File(folder);
            boolean canmake = false;
            if (!myfolder.exists()) {
                canmake = myfolder.mkdirs();
            }

            for (int i = 0; i < resultArray.length; i++) {
                resultId = Integer.parseInt(resultArray[i]);
                page = scaleService.getTeacherPersonalReporter(subjectUserId, observerUser, resultId, true);

                Map userinfo = (Map) page.get("userinfo");
                Scale scale = (Scale) page.get("scale");
                String scaleTitle = scale.getTitle();
                String reportTitle = userinfo.get("xm").toString() + "《" + scaleTitle + "》个体检测报告.doc";

                FreemarkerCfg reportCfg = FreemarkerCfg.newInstance();
                WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
                ServletContext servletContext = webApplicationContext.getServletContext();
                reportCfg.setServletContextForTemplateLoading(servletContext, "/views/ftl");
                Template t = reportCfg.getTemplate("teacherpersonalreport.flt");
                if (resultArray.length == 1) {
                    response.setHeader("Content-disposition",
                            "attachment;   filename=" + new String((reportTitle).getBytes("gb2312"), "iso-8859-1"));
                    ServletOutputStream out = response.getOutputStream();
                    Writer w = new OutputStreamWriter(out, "utf-8");
                    t.process(page, w);
                    w.close();
                    IOUtils.closeQuietly(out);
                    return;
                }

                File f = new File(myfolder + File.separator + reportTitle);
                if (f.exists()) {
                    f.delete();
                } else {
                    f.createNewFile();
                }
                OutputStream os = new FileOutputStream(f);

                // ServletOutputStream out = response.getOutputStream();
                Writer w = new OutputStreamWriter(os, "utf-8");
                t.process(page, w);
                w.close();
                IOUtils.closeQuietly(os);

                if (i == resultArray.length - 1) {
                    Zip.zip(folder, folder + ".zip");
                    StringBuilder uri = new StringBuilder();
                    uri.append(folder);
                    uri.append(".zip");
                    File file = new File(uri.toString());
                    FileInputStream is = new FileInputStream(file);
                    response.setContentType("application/octet-stream");
                    // response.setContentType("application/x-msdownload;charset=UTF-8");
                    response.setHeader("Content-disposition",
                            "attachment;   filename=" + new String(("个人报告批量下载.zip").getBytes("gb2312"), "iso-8859-1"));

                    OutputStream out = response.getOutputStream();
                    // IOUtils.copy(is, out);
                    byte[] b = new byte[2048];
                    int length;
                    while ((length = is.read(b)) > 0) {
                        out.write(b, 0, length);
                    }

                    out.flush();
                    IOUtils.closeQuietly(is);
                    IOUtils.closeQuietly(out);

                }
            }
        } catch (Exception e) {
            // throw new Exception("下载报告出错");
            return;
        }

    }

    @RequestMapping(value = "/studentCompositeReport1", method = RequestMethod.GET)
    public String getStudentCompositeReport1(@CurrentUser Account account, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        String download = request.getParameter("download");// 是否含有export参数，即是否下载

        String subjectUserIdStr = request.getParameter("userid");
        long subjectUserId = -1;
        if (subjectUserIdStr != null)
            subjectUserId = Integer.parseInt(subjectUserIdStr);
        long accountid = account.getId();
        int observerTypeFlag = account.getTypeFlag();
        Object observerUser = accountService.getAccountInfo(accountid, observerTypeFlag);
        Map<String, Object> page = scaleService.getStudentCompositeReport(subjectUserId, observerUser);
        if (!"yes".equals(download)) {
        } else {
            try {
                // response.setContentType("application/pdf");
                Map userinfo = (Map) page.get("userinfo");
                response.setContentType("application/msword");
                // response.setHeader("Content-disposition", "inline;
                // filename="+ new
                // String((userinfo.get("name").toString()+"的复合报告.doc").getBytes("gb2312"),
                // "iso-8859-1"));
                response.setHeader("Content-disposition", "attachment;   filename="
                        + new String((userinfo.get("name").toString() + "的复合报告.doc").getBytes("gb2312"), "iso-8859-1"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        // page.put("template", "closure");
        model.addAttribute("page", page);
        return viewName("studentcompositereport");
    }

    @RequestMapping(value = "/studentCompositeReport", method = RequestMethod.GET)
    public String getStudentCompositeReport(@CurrentUser Account account, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        String download = request.getParameter("download");// 是否含有export参数，即是否下载

        String subjectUserIdStr = request.getParameter("userid");
        long subjectUserId = -1;
        if (subjectUserIdStr != null)
            subjectUserId = Integer.parseInt(subjectUserIdStr);
        long accountid = account.getId();
        int observerTypeFlag = account.getTypeFlag();
        Object observerUser = accountService.getAccountInfo(accountid, observerTypeFlag);
        Map<String, Object> page = scaleService.getStudentCompositeReport(subjectUserId, observerUser);
        if (!"yes".equals(download)) {
        } else {
            try {
                String name = page.get("xm").toString();
                response.setContentType("application/msword");
                response.setHeader("Content-disposition",
                        "attachment;   filename=" + new String((name + "的复合报告.doc").getBytes("gb2312"), "iso-8859-1"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("page", page);
        return viewName("studentcompositereport");
    }

    @RequestMapping(value = "/studentRemarkReport", method = RequestMethod.GET)
    public String getStudentRemarkReport(@CurrentUser Account account, HttpServletRequest request,
            HttpServletResponse response, Model model) throws Exception{
        String download = request.getParameter("download");// 是否含有export参数，即是否下载
        String subjectUserIdStr = request.getParameter("userid");
        long subjectUserId = -1;
        if (subjectUserIdStr != null)
            subjectUserId = Integer.parseInt(subjectUserIdStr);
        long accountid = account.getId();
        int observerTypeFlag = account.getTypeFlag();
        Map<String, Object> page = new HashMap<String, Object>();
        Object observerUser = accountService.getAccountInfo(accountid, observerTypeFlag);
        if(observerUser == null || observerUser.equals("")){
            throw new Exception("教师人员管理没有该老师信息");
        }else{
            page = scaleService.getStudentRemarkReport(subjectUserId, observerUser);
            if(page == null || page.equals("")){
                throw new Exception("学生信息采集没有该学生信息");
            }else{
                if (!"yes".equals(download)) {
                } else {
                    try {
                        String name = page.get("xm").toString();
                        response.setContentType("application/msword");
                        response.setHeader("Content-disposition",
                                "attachment;   filename=" + new String((name + "的个人评语.doc").getBytes("gb2312"), "iso-8859-1"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        model.addAttribute("page", page);
        return viewName("studentremarkreport");
    }

    @RequestMapping(value = "/downloadStuCompositeReport", method = RequestMethod.GET)
    public String downloadStuCompositeReport(@CurrentUser Account account, HttpServletRequest request,
            HttpServletResponse response, Model model) throws IOException, TemplateException {
        String download = request.getParameter("download");// 是否含有export参数，即是否下载
        String subjectUserIdStr = request.getParameter("userid");
        long subjectUserId = -1;
        if (subjectUserIdStr != null)
            subjectUserId = Integer.parseInt(subjectUserIdStr);
        long accountid = account.getId();
        int observerTypeFlag = account.getTypeFlag();
        Object observerUser = accountService.getAccountInfo(accountid, observerTypeFlag);
        Map<String, Object> page = scaleService.downloadStudentCompositeReport(subjectUserId, observerUser);
        try {
            String name = page.get("xm").toString();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");
            // response.setHeader("Content-disposition", "inline; filename="+
            // new
            // String((userinfo.get("name").toString()+"的复合报告.doc").getBytes("gb2312"),
            // "iso-8859-1"));
            response.setHeader("Content-disposition",
                    "attachment;   filename=" + new String((name + "的复合报告.doc").getBytes("gb2312"), "iso-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        FreemarkerCfg reportCfg = FreemarkerCfg.newInstance();
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        reportCfg.setServletContextForTemplateLoading(servletContext, "/views/ftl");
        Template t = reportCfg.getTemplate("compositereport.flt");

        // Writer out = response.getWriter();
        ServletOutputStream out = response.getOutputStream();
        Writer w = new OutputStreamWriter(out, "utf-8");
        t.process(page, w);
        w.close();
        IOUtils.closeQuietly(out);
        return null;
    }

    @RequestMapping(value = "/studentGroupReport", method = RequestMethod.GET)
    public String getStudentGroupReport(@CurrentOrg Organization org, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        // String gradeId = (String) request.getParameter("gradeId"); //
        // 得到分析的所有班级
        // String[] classId = (String[])
        // request.getParameterValues("gradeClassId"); // 得到分析的所有班级
        // String scaleId = (String) request.getParameter("scaleId"); // 得到量表
        // String startDate = (String)request.getParameter("startDate"); // 开始时间
        // String endDate = (String) request.getParameter("endDate"); // 结束时间
        String startDate = "2015-10-13 13:38:25";
        String endDate = "2015-11-17 13:38:25";
        String njmc = "高中2015级";
        String[] bjmcArray = { "1501班" };
        String scaleId = "120100001";
        long orgid = org.getId();
        Map<Object, Object> page = scaleService.getStudentGroupReportForSchool(orgid, njmc, bjmcArray, scaleId,
                startDate, endDate);
        model.addAttribute("page", page);
        return viewName("studentgroupreport");
    }

    /*
     * @RequestMapping(value="/teacherPersonalReport", method=RequestMethod.GET)
     * public String getTeacherPersonalReport(@CurrentUser Account
     * account,HttpServletRequest request, HttpServletResponse response){ String
     * subjectUserIdStr = request.getParameter("userid"); long subjectUserId =
     * -1; long resultId = -1; if(subjectUserIdStr!=null) subjectUserId =
     * Integer.parseInt(subjectUserIdStr); long accountid = account.getId();
     * Object observerUser = accountService.getAccountInfo(accountid, 2); String
     * resultIdStr = request.getParameter("resultId");
     * if(resultIdStr!=null)resultId = Integer.parseInt(resultIdStr);
     * Map<Object, Object> page =
     * scaleService.getTeacherPersonalReporter(subjectUserId, observerUser,
     * resultId); page.put("template", "closure"); return "closure"; }
     */
    @RequestMapping(value = "/teacherCompositeReport", method = RequestMethod.GET)
    public String getTeacherCompositeReport(@CurrentUser Account account, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        String download = request.getParameter("download");// 是否含有export参数，即是否下载
        String subjectUserIdStr = request.getParameter("userid");
        long subjectUserId = -1;
        if (subjectUserIdStr != null)
            subjectUserId = Integer.parseInt(subjectUserIdStr);
        long accountid = account.getId();
        int observerTypeFlag = account.getTypeFlag();
        Object observerUser = accountService.getAccountInfo(accountid, observerTypeFlag);
        Map<String, Object> page = scaleService.getTeacherCompositeReport(subjectUserId, observerUser);
        if (!"yes".equals(download)) {
        } else {
            try {
                String name = page.get("xm").toString();
                response.setContentType("application/msword");
                response.setHeader("Content-disposition",
                        "attachment;   filename=" + new String((name + "的复合报告.doc").getBytes("gb2312"), "iso-8859-1"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("page", page);
        return viewName("teachercompositereport");
    }

    @RequestMapping(value = "/downloadTeacherCompositeReport", method = RequestMethod.GET)
    public String downloadTeacherCompositeReport(@CurrentUser Account account, HttpServletRequest request,
            HttpServletResponse response, Model model) throws IOException, TemplateException {
        String download = request.getParameter("download");// 是否含有export参数，即是否下载
        String subjectUserIdStr = request.getParameter("userid");
        long subjectUserId = -1;
        if (subjectUserIdStr != null)
            subjectUserId = Integer.parseInt(subjectUserIdStr);
        long accountid = account.getId();
        int observerTypeFlag = account.getTypeFlag();
        Object observerUser = accountService.getAccountInfo(accountid, observerTypeFlag);
        Map<String, Object> page = scaleService.downloadTeacherCompositeReport(subjectUserId, observerUser);
        try {
            String name = page.get("xm").toString();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");
            response.setHeader("Content-disposition",
                    "attachment;   filename=" + new String((name + "的复合报告.doc").getBytes("gb2312"), "iso-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        FreemarkerCfg reportCfg = FreemarkerCfg.newInstance();
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        reportCfg.setServletContextForTemplateLoading(servletContext, "/views/ftl");
        Template t = reportCfg.getTemplate("teachercompositereport.flt");
        ServletOutputStream out = response.getOutputStream();
        Writer w = new OutputStreamWriter(out, "utf-8");
        t.process(page, w);
        w.close();
        IOUtils.closeQuietly(out);
        return null;
    }

    @RequestMapping(value = "/reportchart", method = RequestMethod.GET)
    public void reportChart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        OutputStream out = response.getOutputStream();
        try {
            JFreeChart chart = null;
            int w = 0;
            int h = 0;
            String qryStr = request.getQueryString();
            if (StringUtils.startsWithIgnoreCase(qryStr, "bean=")) {
                ChartParamBean paramBean = new ChartParamBean();
                // qryStr = StringEscapeUtils.unescapeHtml(qryStr);
                qryStr = URLDecoder.decode(qryStr, "UTF-8");
                paramBean.urlDecode(qryStr);
                Dimension dimension = paramBean.getSize();
                w = (int) dimension.getWidth();
                h = (int) dimension.getHeight();
                chart = paramBean.createChartBuilder().doBuilder();
            } else {
                JChartCreator chartCreator = new JChartCreator();
                JChartParam chartParam = new JChartParam().parseString(qryStr);
                chartCreator.setChartParam(chartParam);
                chart = chartCreator.getChart();
                w = chartParam.getWidth() == 0 ? 200 : chartParam.getWidth();
                h = chartParam.getHeight() == 0 ? 125 : chartParam.getHeight();
            }
            if (chart != null) {
                response.setContentType("image/png");
                // *** CHART SIZE ***
                ChartUtilities.writeChartAsPNG(out, chart, w, h);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }

    }
}
