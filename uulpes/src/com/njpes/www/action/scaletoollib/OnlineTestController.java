package com.njpes.www.action.scaletoollib;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.WebUtils;

import com.njpes.www.action.BaseController;
import com.njpes.www.dao.scaletoollib.ExamResultMapper;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.CurrentUser;
import com.njpes.www.service.baseinfo.AccountServiceI;
import com.njpes.www.service.baseinfo.StudentServiceI;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.TeacherServiceI;
import com.njpes.www.utils.SpringContextHolder;

import edutec.scale.model.Scale;
import edutec.scale.online.ExamSpace;
import edutec.scale.online.RequestParamConsts;

@Scope("prototype")
@Controller
@RequestMapping("/assessmentcenter/examtask")
public class OnlineTestController extends BaseController {
    // private static final Log logger = LogFactory.getLog(ExamSpace.class);
    private static final String EXAM_SPACE_KEY = ExamSpace.class.getSimpleName();
    // @Autowired
    private ExamSpace examSpace;
    @Autowired
    private AccountServiceI accountService;
    @Autowired
    private ExamResultMapper examResultDao;
    @Autowired
    private StudentServiceI studentService;
    @Autowired
    private TeacherServiceI teacherService;
    @Autowired
    private SyslogServiceI logservice;
    // @Autowired
    // private ExamdoServiceI examdoService;

    @RequestMapping(value = "/openquestion", method = RequestMethod.GET)
    public String openQuestionnaireAction(@CurrentUser Account account, @CurrentOrg Organization org,
            HttpServletRequest req, Model model) {
        // Account account =
        // (Account)req.getSession().getAttribute(Constants.CURRENT_USER);
        long resultid = Integer.parseInt(req.getParameter("resultid"));
        String xm = "";
        try {
            xm = new String(req.getParameter("xm").getBytes("iso-8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String orgname = org.getName();
        String username = account.getUsername();

        ExamSpace tempExamSpace;
        try {
            ExamSpace examSpace1 = (ExamSpace) WebUtils.getSessionAttribute(req, EXAM_SPACE_KEY);
            /*
             * if (examSpace1 == null) { //examSpace = new ExamSpace();
             * WebUtils.setSessionAttribute(req, EXAM_SPACE_KEY, examSpace);
             * tempExamSpace = examSpace; //logger.
             * info("WebUtils.setSessionAttribute(req, EXAM_SPACE_KEY, examSpace)"
             * ); } else{ tempExamSpace = examSpace1;
             * //examSpace1.clearOnlineExam(); }
             */
            if (examSpace1 == null) {
                ExamSpace es = (ExamSpace) SpringContextHolder.getBean("ExamSpace", ExamSpace.class);
                WebUtils.setSessionAttribute(req, EXAM_SPACE_KEY, es);
                tempExamSpace = es;
            } else {
                tempExamSpace = examSpace1;
            }
            /* 每次都要设置req，保持最新的req */
            // return viewName("firstPage");

            int typeflag = account.getTypeFlag();
            // Object user = accountService.getAccountInfo(account.getId(),
            // typeflag);

            Object user = accountService.getAccountInfo(account.getId(), typeflag);
            if (typeflag == 1) {
                Student stu = (Student) user;
                stu.setOrgid(org.getId());
                stu.setXxmc(org.getName());
            }
            if (typeflag == 2) {
                Teacher tea = (Teacher) user;
                tea.setXxmc(org.getName());
            }
            tempExamSpace.setReq(req);
            tempExamSpace.setUser(user);
            long taskid = Integer.parseInt(req.getParameter("taskid"));
            tempExamSpace.setTaskid(taskid);

            tempExamSpace.setResultid(resultid);
            tempExamSpace.createOnlineExam();

            HashMap<Object, Object> page = new HashMap<Object, Object>();
            page.put("testtype", 1);
            page.put("username", username);
            tempExamSpace.nextPage(page);
            Scale scale = (Scale) page.get("scale");
            model.addAttribute("tester", xm);
            model.addAttribute("testtype", 1);
            model.addAttribute("scale", scale);
            model.addAttribute("s", page.get(RequestParamConsts.EXAM_STATE));
            model.addAttribute("template", page.get("template").toString());
            // return viewName(page.get("template").toString());

            logservice.log(org, account, "个人中心:心理测试", scale.getTitle() + "测试");
            return viewName("examing");
        } catch (Exception e) {
            // logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping(value = "/nextpage", method = RequestMethod.POST)
    public String nextPageAction(HttpServletRequest req, HttpServletResponse resp, Model model) throws Exception {
        try {
            // ExamSpace examSpace = ExamSpace.getInstance(req);
            ExamSpace tempExamSpace;
            ExamSpace examSpace1 = (ExamSpace) WebUtils.getSessionAttribute(req, EXAM_SPACE_KEY);
            /*
             * if (examSpace1 == null) { WebUtils.setSessionAttribute(req,
             * EXAM_SPACE_KEY, examSpace); tempExamSpace = examSpace; } else
             * tempExamSpace = examSpace1;
             */
            if (examSpace1 == null) {
                ExamSpace es = (ExamSpace) SpringContextHolder.getBean("ExamSpace", ExamSpace.class);
                WebUtils.setSessionAttribute(req, EXAM_SPACE_KEY, es);
                tempExamSpace = es;
            } else {
                tempExamSpace = examSpace1;
            }

            tempExamSpace.setReq(req);

            HashMap<Object, Object> page = new HashMap<Object, Object>();
            examSpace1.nextPage(page);
            Scale scale = (Scale) page.get("scale");
            model.addAttribute("scale", scale);
            model.addAttribute("s", page.get(RequestParamConsts.EXAM_STATE));
            System.out.println(page.get("q") + "-------------------------");
            model.addAttribute("q", page.get("q"));

            model.addAttribute("progress", page.get("progress"));
            model.addAttribute("currentQIdx", page.get("currentQIdx"));
            model.addAttribute("totalQNum", page.get("totalQNum"));
            model.addAttribute("tester", page.get("tester"));
            model.addAttribute("testtype", page.get("testtype"));
            model.addAttribute("threeangletest", page.get("threeangletest"));

            // model.addAttribute("starttime",page.get("starttime"));
            model.addAttribute("headtitle", page.get("headtitle"));
            model.addAttribute("title", page.get("title"));
            model.addAttribute("subjecttitle", page.get("subjecttitle"));
            model.addAttribute("subjecttitletype", page.get("subjecttitletype"));
            model.addAttribute("selectionQuestion", page.get("selectionQuestion"));
            model.addAttribute("optionList", page.get("optionList"));
            model.addAttribute("buttonCtl", page.get("buttonCtl"));
            model.addAttribute("recordCtl", page.get("recordCtl"));
            model.addAttribute("haspic", page.get("haspic"));
            model.addAttribute("limittime", page.get("limittime"));
            model.addAttribute("page", page);
            return viewName(page.get("template").toString());
        } catch (Exception e) {
            // logger.error(e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @RequestMapping(value = "/openstudentquestion", method = RequestMethod.GET)
    public String openStudentQuestionnaireAction(@CurrentUser Account account, @CurrentOrg Organization org,
            HttpServletRequest req, Model model) {
        ExamSpace tempExamSpace;
        try {
            String xm = req.getParameter("xm");
            xm = java.net.URLDecoder.decode(xm, "UTF-8");
            String userid = req.getParameter("userid");
            // String resultid = req.getParameter("resultid");
            String gender = req.getParameter("gender");
            String nj = req.getParameter("nj");
            String bj = req.getParameter("bj");
            String threeangletest = req.getParameter("threeangletest");
            // String version = req.getParameter("v");
            // String threeAngleVersion = req.getParameter("typeflag");

            ExamSpace examSpace1 = (ExamSpace) WebUtils.getSessionAttribute(req, EXAM_SPACE_KEY);
            if (examSpace1 == null) {
                ExamSpace es = (ExamSpace) SpringContextHolder.getBean("ExamSpace", ExamSpace.class);
                WebUtils.setSessionAttribute(req, EXAM_SPACE_KEY, es);
                tempExamSpace = es;
            } else {
                tempExamSpace = examSpace1;
                // examSpace1.clearOnlineExam();
            }
            long resultid = Integer.parseInt(req.getParameter("resultid"));
            Student student = studentService.selectByPrimaryKey((long) Integer.parseInt(userid));
            tempExamSpace.setReq(req);
            tempExamSpace.setUser(student);
            long taskid = Integer.parseInt(req.getParameter("taskid"));
            tempExamSpace.setTaskid(taskid);

            tempExamSpace.setResultid(resultid);
            tempExamSpace.createOnlineExam();

            HashMap<Object, Object> page = new HashMap<Object, Object>();
            page.put("testtype", 3);
            page.put("threeangletest", threeangletest);
            tempExamSpace.nextPage(page);
            Scale scale = (Scale) page.get("scale");
            model.addAttribute("testtype", 3);
            model.addAttribute("threeangletest", threeangletest);
            model.addAttribute("scale", scale);
            model.addAttribute("tester", xm);
            model.addAttribute("s", page.get(RequestParamConsts.EXAM_STATE));
            model.addAttribute("template", page.get("template").toString());

            logservice.log(org, account, "心理检测中心:学生数据管理:数据导入", xm + scale.getTitle() + "录入");
            return viewName("examing");
        } catch (Exception e) {
            // logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping(value = "/openteacherquestion", method = RequestMethod.GET)
    public String openTeacherQuestionnaireAction(@CurrentUser Account account, @CurrentOrg Organization org,
            HttpServletRequest req, Model model) {
        try {
            ExamSpace tempExamSpace;
            String xm = req.getParameter("xm");
            xm = java.net.URLDecoder.decode(xm, "UTF-8");
            String userid = req.getParameter("userid");
            String gender = req.getParameter("gender");

            ExamSpace examSpace1 = (ExamSpace) WebUtils.getSessionAttribute(req, EXAM_SPACE_KEY);
            /*
             * if (examSpace1 == null) { WebUtils.setSessionAttribute(req,
             * EXAM_SPACE_KEY, examSpace); tempExamSpace = examSpace; } else{
             * tempExamSpace = examSpace1; }
             */
            if (examSpace1 == null) {
                ExamSpace es = (ExamSpace) SpringContextHolder.getBean("ExamSpace", ExamSpace.class);
                WebUtils.setSessionAttribute(req, EXAM_SPACE_KEY, es);
                tempExamSpace = es;
            } else {
                tempExamSpace = examSpace1;
                // examSpace1.clearOnlineExam();
            }
            Teacher user = teacherService.selectByPrimaryKey((long) Integer.parseInt(userid));
            tempExamSpace.setReq(req);
            tempExamSpace.setUser(user);
            long taskid = Integer.parseInt(req.getParameter("taskid"));
            tempExamSpace.setTaskid(taskid);
            long resultid = Integer.parseInt(req.getParameter("resultid"));
            tempExamSpace.setResultid(resultid);
            tempExamSpace.createOnlineExam();

            HashMap<Object, Object> page = new HashMap<Object, Object>();
            page.put("testtype", 3);
            tempExamSpace.nextPage(page);
            Scale scale = (Scale) page.get("scale");
            model.addAttribute("testtype", 3);
            model.addAttribute("scale", scale);
            model.addAttribute("tester", xm);
            model.addAttribute("s", page.get(RequestParamConsts.EXAM_STATE));
            model.addAttribute("template", page.get("template").toString());
            logservice.log(org, account, "心理检测中心:教师数据管理:数据导入", xm + scale.getTitle() + "录入");
            return viewName("examing");
        } catch (Exception e) {
            // logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

}
