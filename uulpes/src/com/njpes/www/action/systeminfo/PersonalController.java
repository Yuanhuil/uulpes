package com.njpes.www.action.systeminfo;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.EcUserWithBLOBs;
import com.njpes.www.entity.baseinfo.Parent;
import com.njpes.www.entity.baseinfo.ParentWithBLOBs;
import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.StudentWithBLOBs;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.TeacherWithBLOBs;
import com.njpes.www.entity.baseinfo.enums.ThemeEnum;
import com.njpes.www.entity.baseinfo.util.CurrentUser;
import com.njpes.www.exception.BaseException;
import com.njpes.www.service.baseinfo.AccountServiceI;
import com.njpes.www.service.baseinfo.EcUserServiceI;
import com.njpes.www.service.baseinfo.ParentServiceI;
import com.njpes.www.service.baseinfo.StudentServiceI;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.TeacherServiceI;

@Controller
@RequestMapping(value = "/systeminfo/sys")
public class PersonalController extends BaseController {
    @Autowired
    private AccountServiceI accountService;
    @Autowired
    private TeacherServiceI teacherService;
    @Autowired
    private StudentServiceI studentService;
    @Autowired
    private EcUserServiceI ecUserService;
    @Autowired
    private ParentServiceI parentService;
    @Autowired
    private SyslogServiceI logservice;

    @RequestMapping("/queryPersonDetail")
    public String queryPersonDetail(@CurrentUser Account account, HttpServletRequest request) {
        // AccountDetail accountDetail = this.accountService
        // .findAccountDetail(request);
        // request.setAttribute("accountDetail", accountDetail);
        //
        if (account.getTypeFlag() == 1) {
            StudentWithBLOBs student = (StudentWithBLOBs) accountService.getAccountInfo(account.getId(),
                    account.getTypeFlag());
            request.setAttribute("student", student);
            String zp = "/pes/themes/theme1/images/user_default.png";
            if (student.getZp() != null) {
                long time = System.currentTimeMillis();
                zp = "../sys/getPersonZp.do?" + time;
            }
            request.setAttribute("zp", zp);
            return viewName("personal/studentInfo");

        } else if (account.getTypeFlag() == 2) {
            TeacherWithBLOBs teacher = (TeacherWithBLOBs) accountService.getAccountInfo(account.getId(),
                    account.getTypeFlag());
            if(teacher == null){
                throw new BaseException("未查询到该老师的个人信息,请核实!");
            }
            request.setAttribute("teacher", teacher);
            String zp = "/pes/themes/theme1/images/user_default.png";
            if (teacher.getZp() != null) {
                long time = System.currentTimeMillis();
                zp = "../sys/getPersonZp.do?" + time;
            }
            request.setAttribute("zp", zp);
            return viewName("personal/teacherInfo");
        } else if (account.getTypeFlag() == 3) {
            ParentWithBLOBs parent = (ParentWithBLOBs) accountService.getAccountInfo(account.getId(),
                    account.getTypeFlag());
            if(parent == null){
                throw new BaseException("未查询到该家长的个人信息,请核实!");
            }
            request.setAttribute("parent", parent);
            String zp = "/pes/themes/theme1/images/user_default.png";
            if (parent.getZp() != null) {
                long time = System.currentTimeMillis();
                zp = "../sys/getPersonZp.do?" + time;
            }
            request.setAttribute("zp", zp);
        } else if (account.getTypeFlag() == 4) {
            EcUserWithBLOBs ecUserWithBLOBs = (EcUserWithBLOBs) accountService.getAccountInfo(account.getId(),
                    account.getTypeFlag());
            request.setAttribute("teacher", ecUserWithBLOBs);
            String zp = "/pes/themes/theme1/images/user_default.png";
            if (ecUserWithBLOBs.getZp() != null) {
                long time = System.currentTimeMillis();
                zp = "../sys/getPersonZp.do?" + time;
            }
            request.setAttribute("zp", zp);
            return viewName("personal/ecUserInfo");
        }

        return viewName("personal/personalinfo");
    }

    @RequestMapping(value = "updateTeacherInfo", method = RequestMethod.POST)
    public @ResponseBody void updateTeacherInfo(Teacher teacher, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String result = "success";
        try {
            if (teacher.getId() != null) {
                Teacher teacher1 = teacherService.selectByPrimaryKey(teacher.getId());
                teacher1.setLxdh(teacher.getLxdh());
                teacher1.setGzny(teacher.getGzny());
                teacher1.setXzz(teacher.getXzz());
                teacher1.setDzxx(teacher.getDzxx());
                teacher1.setHkszd(teacher.getHkszd());
                teacher1.setZyrkxd(teacher.getZyrkxd());
                // teacher1.setZp(null);
                teacherService.updateByPrimaryKey(teacher1);
                logservice.log(request, "个人中心", teacher1.getXm() + "教师信息更新");
            } else {
                result = "fail";
            }

        } catch (Exception e) {
            result = "fail";
        }
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    @RequestMapping(value = "updateEcUserInfo", method = RequestMethod.POST)
    public @ResponseBody void updateEcUserInfo(EcUserWithBLOBs ecUserWithBLOBs, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String result = "success";
        try {
            if (ecUserWithBLOBs.getId() != null) {
                EcUserWithBLOBs ecUserWithBLOBs1 = ecUserService.selectById(ecUserWithBLOBs.getId());
                ecUserWithBLOBs1.setLxdh(ecUserWithBLOBs.getLxdh());
                ecUserWithBLOBs1.setGzny(ecUserWithBLOBs.getGzny());
                ecUserWithBLOBs1.setXzz(ecUserWithBLOBs.getXzz());
                ecUserWithBLOBs1.setDzxx(ecUserWithBLOBs.getDzxx());
                ecUserWithBLOBs1.setHkszd(ecUserWithBLOBs.getHkszd());

                ecUserService.update(ecUserWithBLOBs1);
                logservice.log(request, "个人中心", ecUserWithBLOBs1.getXm() + "教委人员信息更新");
            } else {
                result = "fail";
            }

        } catch (Exception e) {
            result = "fail";
        }
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    @RequestMapping(value = "updateStudentInfo", method = RequestMethod.POST)
    public @ResponseBody void updateStudentInfo(Student student, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String result = "success";
        try {
            if (student.getId() != null) {
                StudentWithBLOBs student1 = (StudentWithBLOBs) studentService.selectByPrimaryKey(student.getId());
                student1.setXzz(student.getXzz());
                student1.setHkszd(student.getHkszd());
                student1.setLxdh(student.getLxdh());
                student1.setRxny(student.getRxny());
                student1.setDszybz(student.getDszybz());
                student1.setDzxx(student.getDzxx());
                studentService.updateByPrimaryKeyWithBLOBs(student1);
                logservice.log(request, "个人中心", student1.getXm() + "学生个人信息更新");
            } else {
                result = "fail";
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = "fail";
        }
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    @RequestMapping(value = "updatePersonDetail", method = RequestMethod.POST)
    public @ResponseBody void updatePersonDetail(Parent parent, HttpServletRequest request, HttpServletResponse response)throws IOException {
        String result = "success";
        try {
            parentService.updateByPrimaryKey(parent);
            logservice.log(request, "个人中心", parent.getCyxm() + "个人详细信息更新");
        } catch (Exception e) {
            result = "fail";
        }
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    @RequestMapping(value = "updatePersonPassword", method = RequestMethod.POST)
    public @ResponseBody void updatePersonPassword(@RequestParam("sourcePassword") String sourcePassword,
            @RequestParam("nowPassword") String nowPassword, @RequestParam("dogid") int dogid,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        String result = this.accountService.updateAccountPassword(request, sourcePassword, nowPassword, dogid);
        logservice.log(request, "个人中心", "个人登录密码更新");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    @RequestMapping(value = "updatePersonUserName", method = RequestMethod.POST)
    public @ResponseBody void updatePersonUserName(@RequestParam("username") String username,
            @RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String result = this.accountService.updateAccountUsername(request, username, password);
        logservice.log(request, "个人中心", username + "登录用户名密码更新");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    @RequestMapping(value = "checkPersonUserNameExist", method = RequestMethod.POST)
    public @ResponseBody void checkPersonUserNameExist(@RequestParam("username") String username,

            HttpServletRequest request, HttpServletResponse response) throws IOException {
        String result = this.accountService.checkPersonUserNameExist(request, username);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    @RequestMapping("modifypassword")
    public String modifypassword(HttpServletRequest request, Model model, @CurrentUser Account user) {
        model.addAttribute("iddoglogin", user.getIsdoglongin());
        return viewName("personal/modifypass");
    }

    @RequestMapping("changeTheme")
    public String changeTheme(@CurrentUser Account account, HttpServletRequest request, Model model) {
        account = accountService.findOne(account.getId());
        model.addAttribute("account", account);
        model.addAttribute("themeEnum", ThemeEnum.values());
        return viewName("personal/changeTheme");
    }

    @RequestMapping(value = "updateTheme", method = RequestMethod.POST)
    public @ResponseBody void updateTheme(@CurrentUser Account account, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        account = accountService.findOne(account.getId());

        String theme = request.getParameter("theme");
        account.setTheme(theme);
        String result = "";
        int a = this.accountService.update(account);
        if (a == 1) {
            result = "更新主题成功";
            Account user = (Account) request.getSession().getAttribute(Constants.CURRENT_USER);
            user.setTheme(theme);
            request.getSession().setAttribute(Constants.CURRENT_USER, user);
            logservice.log(request, "个人中心", account.getUsername() + "皮肤更换");
        } else {
            result = "更新主题失败";
        }
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    @RequestMapping("/updatePersonZp")
    @ResponseBody
    public void updatePersonZp(@CurrentUser Account account, HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest mul = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = mul.getFiles("file");
        MultipartFile file = files.get(0);
        int a = 0;
        try {
            if (account.getTypeFlag() == 1) {
                StudentWithBLOBs student = (StudentWithBLOBs) studentService.getStudentByAccountId(account.getId());
                student.setZp(file.getBytes());
                a = studentService.updateByPrimaryKeyWithBLOBs(student);
                logservice.log(request, "个人中心", student.getXm() + "照片更换");
            } else if (account.getTypeFlag() == 2) {

                TeacherWithBLOBs teacherWithBLOBs = (TeacherWithBLOBs) teacherService
                        .getTeacherByAccountId(account.getId());

                teacherWithBLOBs.setZp(file.getBytes());

                a = teacherService.update(teacherWithBLOBs);
                logservice.log(request, "个人中心", teacherWithBLOBs.getXm() + "照片更换");
            } else if (account.getTypeFlag() == 4) {
                EcUserWithBLOBs ecUserWithBLOBs = (EcUserWithBLOBs) ecUserService.getEcUserByAccountId(account.getId());
                ecUserWithBLOBs.setZp(file.getBytes());
                a = ecUserService.update(ecUserWithBLOBs);
                logservice.log(request, "个人中心", ecUserWithBLOBs.getXm() + "照片更换");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out;
        try {
            out = response.getWriter();
            out.print(a);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping("/getPersonZp")
    @ResponseBody
    public void getPersonZp(@CurrentUser Account account, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        byte[] file = null;

        if (account.getTypeFlag() == 1) {
            StudentWithBLOBs student = (StudentWithBLOBs) studentService.getStudentByAccountId(account.getId());
            file = student.getZp();
        } else if (account.getTypeFlag() == 2) {

            TeacherWithBLOBs teacherWithBLOBs = (TeacherWithBLOBs) teacherService
                    .getTeacherByAccountId(account.getId());

            file = teacherWithBLOBs.getZp();
        } else if (account.getTypeFlag() == 4) {
            EcUserWithBLOBs ecUserWithBLOBs = (EcUserWithBLOBs) ecUserService.getEcUserByAccountId(account.getId());
            file = ecUserWithBLOBs.getZp();

        }
        if (null != file) {
            OutputStream os = null;
            try {
                os = response.getOutputStream();
                os.write(file);
                os.flush();
            } catch (IOException e) {

            } finally {
                if (null != os) {
                    try {
                        os.close();
                    } catch (IOException e) {

                    }
                }
            }
        }

    }

    @RequestMapping("/getZp")
    @ResponseBody
    public void getZp(@CurrentUser Account account, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        byte[] file = null;
        String id = request.getParameter("id");
        String typeflag = request.getParameter("typeflag");
        if (typeflag.equals("1")) {
            StudentWithBLOBs student = (StudentWithBLOBs) studentService
                    .selectStudentWithBlobInfoById(Long.parseLong(id));
            file = student.getZp();
        } else if (typeflag.equals("2")) {

            TeacherWithBLOBs teacherWithBLOBs = (TeacherWithBLOBs) teacherService
                    .selectByPrimaryKey(Long.parseLong(id));

            file = teacherWithBLOBs.getZp();
        }
        if (null != file) {
            OutputStream os = null;
            try {
                os = response.getOutputStream();
                os.write(file);
                os.flush();
            } catch (IOException e) {

            } finally {
                if (null != os) {
                    try {
                        os.close();
                    } catch (IOException e) {

                    }
                }
            }
        }

    }

}
