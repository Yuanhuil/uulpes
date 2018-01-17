package com.njpes.www.action.consultcenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.njpes.www.action.BaseController;
import com.njpes.www.constant.Constants;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.FieldConfig;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.TeacherQueryParam;
import com.njpes.www.entity.baseinfo.enums.BooleanEnum;
import com.njpes.www.entity.baseinfo.enums.SwitchEnum;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.CurrentUser;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.consultcenter.Email;
import com.njpes.www.service.baseinfo.AccountServiceI;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.TeacherServiceI;
import com.njpes.www.service.consultcenter.EmailServiceI;
import com.njpes.www.service.util.FieldServiceI;
import com.njpes.www.utils.DateUtil;
import com.njpes.www.utils.PageParameter;

/**
 * @Description: 心理在线--信箱
 * @author zhangchao
 * @Date 2015-5-18 上午9:17:48
 */
@Controller
@RequestMapping(value = "/consultcenter/email")
public class EmailController extends BaseController {
    /** 草稿 */
    public static String email_status_draft = "0";
    /** 已发送 */
    public static String email_status_sent = "1";
    /** 已读 */
    public static String email_status_read = "2";
    /** 发送者已删除 */
    public static String email_status_sdel = "3";
    /** 发送者已删除 */
    public static String email_status_rdel = "4";
    /** 收信人已删除 */
    public static String email_status_del = "5";
    @Autowired
    private EmailServiceI emailService;
    @Autowired
    private TeacherServiceI teacherService;
    @Autowired
    private FieldServiceI fieldService;
    @Autowired
    private AccountServiceI accountService;
    @Autowired
    private SyslogServiceI logservice;

    /**
     * @Description:
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/main" })
    public String main(@CurrentOrg Organization orgEntity, @CurrentUser Account account, HttpServletRequest request,
            Model model) {

        Email email = new Email();
        email.setToid(account.getId());
        PageParameter page = new PageParameter(1, 10);
        page.setUrl(null);
        email.setEmailstatus(email_status_sent);
        List<Email> emails = getListByEntity(email, page, request, null, null, model);
        model.addAttribute("list", emails);
        model.addAttribute("email", email);
        model.addAttribute("userid", account.getId());
        model.addAttribute("page", page);
        model.addAttribute("accountid", account.getId());
        return viewName("main");
    }

    /**
     * @Description:信件编辑页面跳转
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/addOrUpdate" })
    public String addOrUpdate(@CurrentOrg Organization orgEntity, @CurrentUser Account account,
            HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        String fid = request.getParameter("fid");
        Email email = new Email();
        Email receiveEmail = new Email();
        String view = request.getParameter("view");
        model.addAttribute("view", view);
        TeacherQueryParam teacherQueryParam = new TeacherQueryParam();
        teacherQueryParam.setRoleId("21");
        List<Teacher> teachers = teacherService.getTeachersByPage(teacherQueryParam, orgEntity.getId(),
                new PageParameter(0, 10));
        if (id != null) {
            long idL = Long.parseLong(id);

            if (view.equals("-1")) {
                Email email1 = emailService.selectByPrimaryKey(idL);
                email.setToid(email1.getFromid());

            } else {
                email = emailService.selectByPrimaryKey(idL);
            }

            if (email.getParentId() != null) {
                receiveEmail = emailService.selectByPrimaryKey(email.getParentId());
                model.addAttribute("viewStatus", 3);
            } else {
                model.addAttribute("viewStatus", 2);
            }
        } else {
            email.setEmailstatus("0");
            model.addAttribute("viewStatus", 2);
        }
        email.setFromid(account.getId());
        BooleanEnum.values();
        model.addAttribute("sentUserName", account.getUsername());
        model.addAttribute("teachers", teachers);
        model.addAttribute("booleanEnum", SwitchEnum.values());
        model.addAttribute("email", email);
        model.addAttribute("receiveEmail", receiveEmail);
        model.addAttribute("fid", fid);
        return viewName("add");
    }

    /**
     * @Description:信件编辑页面跳转
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/reply" })
    public String reply(@CurrentOrg Organization orgEntity, @CurrentUser Account account, HttpServletRequest request,
            Model model) {
        String id = request.getParameter("id");
        String fid = request.getParameter("fid");
        Email email = new Email();
        Email receiveEmail = new Email();
        String view = request.getParameter("view");
        model.addAttribute("view", view);
        Account account1 = null;

        if (id != null) {
            long idL = Long.parseLong(id);

            Email email1 = emailService.selectByPrimaryKey(idL);
            email.setToid(email1.getFromid());
            email.setFromid(email1.getToid());

            account1 = accountService.findOne(email.getToid());

            if (email.getParentId() != null) {
                receiveEmail = emailService.selectByPrimaryKey(email.getParentId());
                model.addAttribute("viewStatus", 3);
            } else {
                model.addAttribute("viewStatus", 2);
            }
        } else {
            email.setEmailstatus("0");
            model.addAttribute("viewStatus", 2);
        }
        email.setFromid(account.getId());
        BooleanEnum.values();
        model.addAttribute("sentUserName", account.getRealname());
        model.addAttribute("toUserName", account1.getRealname());

        model.addAttribute("booleanEnum", SwitchEnum.values());
        model.addAttribute("email", email);
        model.addAttribute("receiveEmail", receiveEmail);
        model.addAttribute("fid", fid);
        return viewName("reply");
    }

    /**
     * @Description:信件编辑页面跳转
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/view" })
    public String view(@CurrentOrg Organization orgEntity, HttpServletRequest request, @CurrentUser Account account,
            Model model) {
        String id = request.getParameter("id");
        String fid = request.getParameter("fid");
        Email receiveEmail = new Email();
        Email email = new Email();

        if (id != null) {
            long idL = Long.parseLong(id);
            receiveEmail = emailService.selectByPrimaryKey(idL);

        } else {
            receiveEmail.setEmailstatus("0");
        }
        BooleanEnum.values();
        model.addAttribute("booleanEnum", SwitchEnum.values());

        email.setParentId(receiveEmail.getId());
        email.setParentIds(receiveEmail.getParentIds() + "," + receiveEmail.getId());
        model.addAttribute("receiveEmail", receiveEmail);
        email.setFromid(receiveEmail.getToid());
        email.setToid(receiveEmail.getFromid());
        model.addAttribute("email", email);
        model.addAttribute("viewStatus", 1);
        model.addAttribute("fid", fid);

        return viewName("add");
    }

    /**
     * @Description:根据entity查询list
     * @param email
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/list" })
    public String list(Email email, @PageAnnotation PageParameter page, HttpServletRequest request,
            @CurrentUser Account account, Model model, Date beginDate, Date endDate) {
        List<Email> emails = getListByEntity(email, page, request, beginDate, endDate, model);
        model.addAttribute("list", emails);
        model.addAttribute("page", page);
        model.addAttribute("accountid", account.getId());
        return viewName("list");
    }

    /**
     * @Description:根据entity查询list
     * @param email
     * @param request
     * @param model
     * @return
     */
    private List<Email> getListByEntity(Email email, PageParameter page, HttpServletRequest request, Date beginDate,
            Date endDate, Model model) {
        endDate = DateUtil.dateAdd(endDate, Calendar.DATE, 1);
        if (page == null) {
            int currentPage;
            int pageSize;
            String currentPageStr = request.getParameter("currentPage");
            String pageSizeStr = request.getParameter("pageSize");
            if (currentPageStr == null) {
                currentPage = 1;
            } else {
                currentPage = Integer.parseInt(currentPageStr);
            }
            pageSizeStr = Constants.PAGE_LIST_NUM;
            pageSize = Integer.parseInt(pageSizeStr);
            String url = request.getRequestURI();
            page = new PageParameter(currentPage, pageSize);
            page.setUrl(url);
        }

        List<Email> emails = emailService.selectListByEntity(email, page, beginDate, endDate);
        // select id ,xm from account a JOIN (select 1 as type_flag ,xm
        // ,account_id from student UNION
        // select 2 as type_flag ,xm ,account_id from teacher) as b on
        // a.type_flag=b.type_flag and a.id=b.account_id;
        List<FieldConfig> NFTW = new ArrayList<FieldConfig>();
        FieldConfig typeMap = new FieldConfig("fromid", "id", "xm",
                " account a JOIN (select 1 as type_flag ,xm ,account_id from student UNION	select 2 as type_flag ,xm ,account_id from teacher) as b on a.type_flag=b.type_flag and a.id=b.account_id",
                false, "");
        NFTW.add(typeMap);
        FieldConfig teacheridMap = new FieldConfig("toid", "id", "xm",
                " account a JOIN (select 1 as type_flag ,xm ,account_id from student UNION	select 2 as type_flag ,xm ,account_id from teacher) as b on a.type_flag=b.type_flag and a.id=b.account_id",
                false, "");
        NFTW.add(teacheridMap);
        Map<String, Map> m = fieldService.getFieldName(emails, NFTW);
        for (String field : m.keySet()) {
            model.addAttribute(field, m.get(field));
        }

        return emails;
    }

    /**
     * @Description:保存
     * @param email
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/save" })
    public String save(@CurrentUser Account account, Email email, HttpServletRequest request, Model model,
            RedirectAttributes redirectAttributes) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String title1 = request.getParameter("title1");
        if (title1 != null && title1.toString().length() > 0) {
            map.put("title", title1.toString());
        }
        String beginDate = request.getParameter("beginDate");
        if (beginDate != null && beginDate.toString().length() > 0) {
            map.put("beginDate", beginDate.toString());
        }
        String endDate = request.getParameter("endDate");
        if (endDate != null && endDate.toString().length() > 0) {
            map.put("endDate", endDate.toString());
        }

        Date date = new Date();
        email.setData(date);
        if (email.getEmailstatus().equals("1") || email.getEmailstatus().equals("0")) {
            email.setSendDate(date);
        }
        if (email.getFromid() == account.getId()) {
            map.put("toid", account.getId());
        } else {
            map.put("fromid", account.getId());
        }
        if (email.getId() == null) {
            emailService.saveEmail(email);
            logservice.log(request, "心理辅导中心:心理在线", "新建信件");
        } else {
            emailService.updateEmail(email);
            logservice.log(request, "心理辅导中心:心理在线", "信件更新");
        }

        map.put("emailstatus", email.getEmailstatus());
        redirectAttributes.addAllAttributes(map);
        return redirectToUrl(viewName("list.do"));

    }

    @RequestMapping(value = { "/parentsEmails" })
    public String parentsEmails(Email email, HttpServletRequest request, Model model) {
        List<Email> emails = new ArrayList<Email>();

        if (email.getId() != null) {
            email = emailService.selectByPrimaryKey(email.getId());
            emails = emailService.selectParentEmailList(email.getParentIds());
        }

        model.addAttribute("list", emails);
        return viewName("parents");
    }

    /**
     * @Description:删除信件 （实际是更改信件状态）
     * @param email
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = { "/del" })
    public String del(@CurrentUser Account account, Email email, HttpServletRequest request, Model model,
            RedirectAttributes redirectAttributes) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String str = "";
        String title1 = null;
        /*
         * try { title1 = URLDecoder.decode(request.getParameter("title11"),
         * "UTF-8"); } catch (UnsupportedEncodingException e1) { // TODO
         * Auto-generated catch block e1.printStackTrace(); }
         */

        if (title1 != null && title1.toString().length() > 0) {
            map.put("title", title1.toString());
        }
        String beginDate = request.getParameter("beginDate");
        if (beginDate != null && beginDate.toString().length() > 0) {
            map.put("beginDate", beginDate.toString());
        }
        String endDate = request.getParameter("endDate");
        if (endDate != null && endDate.toString().length() > 0) {
            map.put("endDate", endDate.toString());
        }
        if (email.getId() != null) {
            Email email1 = emailService.selectByPrimaryKey(email.getId());
            if (email1 != null) {
                map.put("emailstatus", email1.getEmailstatus());
                if (email1.getFromid() == account.getId()) {
                    map.put("fromid", email1.getFromid());
                } else {
                    map.put("toid", email1.getToid());
                }
                long userid = account.getId();
                if (userid == email1.getFromid()) {
                    if (email1.getEmailstatus().equals(email_status_rdel)) {
                        email1.setEmailstatus(email_status_del);

                    } else if (email1.getEmailstatus().equals(email_status_draft)) {
                        emailService.delEmail(email1);
                    } else {
                        email1.setEmailstatus(email_status_sdel);
                    }
                } else {
                    if (email1.getEmailstatus().equals(email_status_sdel)) {
                        email1.setEmailstatus(email_status_del);
                    } else {
                        email1.setEmailstatus(email_status_rdel);
                    }
                }
                int a = emailService.updateEmail(email1);
                if (a == 1) {
                    str = "删除成功";
                    logservice.log(request, "心理辅导中心:心理在线", "删除信件");
                }
            } else {
                str = "删除失败";
            }
        } else {
            str = "删除失败";
        }
        request.setAttribute("result", str);
        redirectAttributes.addAllAttributes(map);
        return redirectToUrl(viewName("list.do"));
    }

}
