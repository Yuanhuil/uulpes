
package com.njpes.www.action.assessmentcenter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import com.njpes.www.action.BaseController;
import com.njpes.www.entity.assessmentcenter.InvestExam;
import com.njpes.www.entity.assessmentcenter.InvestStat;
import com.njpes.www.entity.assessmentcenter.InvesttaskWithBLOBs;
import com.njpes.www.entity.assessmentcenter.MyInvesttask;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.entity.baseinfo.Role;
import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.Teacher;
import com.njpes.www.entity.baseinfo.enums.OrganizationType;
import com.njpes.www.entity.baseinfo.enums.XueDuan;
import com.njpes.www.entity.baseinfo.organization.Organization;
import com.njpes.www.entity.baseinfo.util.CurrentOrg;
import com.njpes.www.entity.baseinfo.util.PageAnnotation;
import com.njpes.www.entity.scaletoollib.InvestDispatcherFilterParam;
import com.njpes.www.service.assessmentcenter.InvestServiceI;
import com.njpes.www.service.baseinfo.AccountServiceI;
import com.njpes.www.service.baseinfo.SyslogServiceI;
import com.njpes.www.service.baseinfo.organization.OrganizationServiceI;
import com.njpes.www.service.baseinfo.organization.SchoolServiceI;
import com.njpes.www.service.scaletoollib.ScaleMgrService;
import com.njpes.www.service.util.DictionaryServiceI;
import com.njpes.www.utils.PageParameter;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.digester.ScaleBuilder;
import edutec.scale.exception.DimensionException;
import edutec.scale.model.Option;
import edutec.scale.model.Question;
import edutec.scale.model.QuestionConsts;
import edutec.scale.model.Scale;
import edutec.scale.model.SelectionQuestion;
import edutec.scale.questionnaire.QuestionBlock;
import edutec.scale.questionnaire.Questionnaire;
import heracles.util.OrderValueMap;
import heracles.web.util.HtmlStr;
import jodd.io.StringInputStream;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/assessmentcenter/invest")
public class InvestController extends BaseController {

    @Autowired
    private CachedScaleMgr cachedScaleMgr;

    @Autowired
    private InvestServiceI investService;

    @Autowired
    private ScaleMgrService scaleMgrService;

    @Autowired
    private DictionaryServiceI dictionaryService;
    @Autowired
    private OrganizationServiceI organizationService;
    @Autowired
    private SchoolServiceI schoolService;
    @Autowired
    private AccountServiceI accountService;
    @Autowired
    private SyslogServiceI logservice;

    /**
     * 进入调查问卷列表，判断如果是未完成，则继续，没有做过就开始
     * 
     * @param request
     * @param model
     * @author shibin
     * @return
     */
    @RequestMapping(value = { "/list" })
    public String list(@CurrentOrg Organization orgEntity, InvestExam ie, HttpServletRequest request,
            @PageAnnotation PageParameter page, Model model) {
        // 获得所有调查问卷
        if (null == ie)
            ie = new InvestExam();
        Account account = (Account) request.getSession().getAttribute("user");
        long createuserid = account.getId();
        ie.setOwnerid(createuserid);
        List<InvestExam> invests = investService.getAllInvestPage(ie, page);
        String orgtype = orgEntity.getOrgType();
        model.addAttribute("orgtype", orgtype);
        model.addAttribute("invests", invests);
        model.addAttribute("page", page);
        model.addAttribute("entity", ie);
        List<Map<String, String>> status = new ArrayList<Map<String, String>>();
        Map<String, String> approval = new HashMap<String, String>();
        approval.put("1", "已发布");
        approval.put("0", "审核中");
        status.add(approval);
        model.addAttribute("status", status);
        return "/assessmentcenter/invest/list";
    }

    // 我的分发
    @RequestMapping(value = { "/investlist" })
    public String investlist(@CurrentOrg Organization orgEntity,
            InvestDispatcherFilterParam investDispatcherFilterParam, HttpServletRequest request,
            @PageAnnotation PageParameter page, Model model) {
        // 获得所有调查问卷
        // InvestDispatcherFilterParam
        if (null == investDispatcherFilterParam)
            investDispatcherFilterParam = new InvestDispatcherFilterParam();
        request.setAttribute("investDispatcherFilterParam", investDispatcherFilterParam);
        Account account = (Account) request.getSession().getAttribute("user");
        long createuserid = account.getId();
        investDispatcherFilterParam.setCreateuserid(createuserid);
        long orgid = orgEntity.getId();
        investDispatcherFilterParam.setOrgid(orgid);
        List<InvesttaskWithBLOBs> investTaskList = null;
        if (StringUtils.equals(orgEntity.getOrgType(), OrganizationType.school.getId()))
            investTaskList = investService.getAllInvestTaskForSchoolPage(investDispatcherFilterParam, page);
        if (StringUtils.equals(orgEntity.getOrgType(), OrganizationType.ec.getId()))
            investTaskList = investService.getAllInvestTaskForEduPage(investDispatcherFilterParam, page);
        if (StringUtils.equals(orgEntity.getOrgType(), OrganizationType.taoststion.getId()))
            investTaskList = investService.getAllInvestTaskForEduPage(investDispatcherFilterParam, page);
        investTaskList = investService.requireStatus(investTaskList, orgid, createuserid);
        String orgtype = orgEntity.getOrgType();
        model.addAttribute("orgtype", orgtype);
        int orglevel = orgEntity.getOrgLevel();
        model.addAttribute("orglevel", orglevel);
        model.addAttribute("investTaskList", investTaskList);
        model.addAttribute("page", page);
        model.addAttribute("entity", investDispatcherFilterParam);
        return "/assessmentcenter/invest/investdispense";
    }

    // 我的调查结果
    @RequestMapping(value = { "/myinvestlist" })
    public String myinvestlist(@CurrentOrg Organization orgEntity,
            InvestDispatcherFilterParam investDispatcherFilterParam, HttpServletRequest request,
            @PageAnnotation PageParameter page, Model model) {
        // 获得所有调查问卷
        // InvestDispatcherFilterParam
        if (null == investDispatcherFilterParam)
            investDispatcherFilterParam = new InvestDispatcherFilterParam();
        request.setAttribute("investDispatcherFilterParam", investDispatcherFilterParam);
        Account account = (Account) request.getSession().getAttribute("user");
        long orgid = orgEntity.getId();
        investDispatcherFilterParam.setOrgid(orgid);
        investDispatcherFilterParam.setUserid(account.getId());

        Object userinfo = accountService.getAccountInfo(account.getId(), account.getTypeFlag());
        if (userinfo instanceof Student) {
            Student stu = (Student) userinfo;
            long bjid = stu.getBjid();
            investDispatcherFilterParam.setBjid(bjid);
        }
        if (userinfo instanceof Teacher) {
            Teacher tea = (Teacher) userinfo;
            List roleidList = new ArrayList();
            Set<Role> roles = account.getRoles();
            for (Role role : roles) {
                roleidList.add(role.getId());
            }

            investDispatcherFilterParam.setRoleidList(roleidList);
        }
        List<MyInvesttask> myInvesttaskList = investService.getInvestdoByPage(investDispatcherFilterParam, page);
        String orgtype = orgEntity.getOrgType();
        model.addAttribute("orgtype", orgtype);
        int orglevel = orgEntity.getOrgLevel();
        model.addAttribute("orglevel", orglevel);
        model.addAttribute("myInvesttaskList", myInvesttaskList);
        model.addAttribute("page", page);
        model.addAttribute("entity", investDispatcherFilterParam);
        return "/assessmentcenter/invest/myinvest";
    }

    @RequestMapping(value = { "/doInvest" }, method = RequestMethod.GET)
    public String doInvest(@CurrentOrg Organization orgEntity, HttpServletRequest request, Model model) {
        Account account = (Account) request.getSession().getAttribute("user");
        long userid = account.getId();
        String taskid = request.getParameter("taskid").toString();
        int scaleid = Integer.parseInt(request.getParameter("scaleid").toString());
        // investService.getInvestdoById(id);
        InvestExam IE = investService.selectByPrimaryKey(scaleid);
        String xmlStr = IE.getXmlstr();
        Scale scale = investService.getScaleFromInvestExam(IE);
        String descn = HtmlStr.decodeString(scale.getDescn());
        scale.setDescn(descn);
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setScale(scale);
        questionnaire.openInvest();
        model.addAttribute("questionnaire", questionnaire);
        model.addAttribute("scaleid", scaleid);
        model.addAttribute("taskid", taskid);
        model.addAttribute("userid", userid);
        model.addAttribute("optionLabelMap", resultMap);
        return "/assessmentcenter/invest/myinvestquestionnaire";
    }

    public static Map<String, String> resultMap = new HashMap<String, String>();

    static {
        resultMap.put("0", "A");
        resultMap.put("1", "B");
        resultMap.put("2", "C");
        resultMap.put("3", "D");
        resultMap.put("4", "E");
        resultMap.put("5", "F");
        resultMap.put("6", "G");
        resultMap.put("7", "H");
        resultMap.put("8", "I");
        resultMap.put("9", "J");
        resultMap.put("10", "K");
        resultMap.put("11", "L");
        resultMap.put("12", "M");
        resultMap.put("13", "N");

    }

    /*
     * @RequestMapping(value = { "/investdone" }, method = RequestMethod.POST)
     * public String investdone(HttpServletRequest request, Model model) {
     * Account account = (Account) request.getSession().getAttribute("user");
     * long userid= account.getId(); int scaleid =
     * Integer.parseInt(request.getParameter("scaleid")); long taskid =
     * Integer.parseInt(request.getParameter("taskid"));
     * model.addAttribute("scaleid", scaleid); model.addAttribute("taskid",
     * taskid); InvestExam IE = investService.selectByPrimaryKey(scaleid);
     * String xmlStr = IE.getXmlstr(); Scale scale =
     * investService.getScaleFromInvestExam(IE); Questionnaire questionnaire =
     * new Questionnaire(); questionnaire.setScale(scale);
     * questionnaire.openInvest(); List<QuestionBlock> questions =
     * questionnaire.getQuestionBlocks(); StringBuilder sb=new StringBuilder();
     * for(int i=0;i<questionnaire.getQuestionSize();i++){ QuestionBlock
     * questionBlock = questions.get(i); if
     * (questionBlock.getQuestion().getTypeMode()==QuestionConsts.
     * TYPE_SELECTION_MODE) { SelectionQuestion sQ = (SelectionQuestion)
     * questionBlock.getQuestion(); if(sQ.getChoiceMode() ==
     * QuestionConsts.CHOICE_SINGLE_MODE)//单选题 { String selectedIndex =
     * request.getParameter("r_"+i); sb.append("?");
     * sb.append(sQ.getId()+":"+selectedIndex); sb.append("?"); sb.append("#");
     * } if(sQ.getChoiceMode() == QuestionConsts.CHOICE_MULTI_MODE)//多选题，分数之和 {
     * String[] selectedIndexArray = request.getParameterValues("r_"+i);
     * //sb.append("?"); //sb.append(sQ.getId()+":"); for(int
     * j=0;j<selectedIndexArray.length;j++){ String selectedIndex =
     * selectedIndexArray[j]; sb.append("?"); sb.append(sQ.getId()+":");
     * sb.append(selectedIndex); sb.append("?"); sb.append(","); }
     * sb.deleteCharAt(sb.length()-1); sb.append("#"); }
     * 
     * }
     * 
     * } sb.deleteCharAt(sb.length() - 1); investService.investdone(userid,
     * taskid, scaleid, sb.toString()); return
     * "/assessmentcenter/invest/investdone"; }
     */
    @RequestMapping(value = { "/investdone" }, method = RequestMethod.POST)
    public String investdone(HttpServletRequest request, Model model) {
        Account account = (Account) request.getSession().getAttribute("user");
        long userid = account.getId();
        int scaleid = Integer.parseInt(request.getParameter("scaleid"));
        long taskid = Integer.parseInt(request.getParameter("taskid"));
        model.addAttribute("scaleid", scaleid);
        model.addAttribute("taskid", taskid);
        InvestExam IE = investService.selectByPrimaryKey(scaleid);
        String xmlStr = IE.getXmlstr();
        Scale scale = investService.getScaleFromInvestExam(IE);
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setScale(scale);
        questionnaire.openInvest();
        List<QuestionBlock> questions = questionnaire.getQuestionBlocks();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < questionnaire.getQuestionSize(); i++) {
            QuestionBlock questionBlock = questions.get(i);
            if (questionBlock.getQuestion().getTypeMode() == QuestionConsts.TYPE_SELECTION_MODE) {
                SelectionQuestion sQ = (SelectionQuestion) questionBlock.getQuestion();
                if (sQ.getChoiceMode() == QuestionConsts.CHOICE_SINGLE_MODE)// 单选题
                {
                    String selectedIndex = request.getParameter("r_" + i);
                    questionBlock.setAnswer(selectedIndex);
                }
                if (sQ.getChoiceMode() == QuestionConsts.CHOICE_MULTI_MODE)// 多选题，分数之和
                {
                    String[] selectedIndexArray = request.getParameterValues("r_" + i);
                    for (int j = 0; j < selectedIndexArray.length; j++) {
                        String selectedIndex = selectedIndexArray[j];
                        sb.append(sQ.getId() + ":");
                        sb.append(selectedIndex);
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    questionBlock.setAnswer(sb.toString());
                }
            }
        }
        investService.investdone(userid, taskid, scaleid, questionnaire);
        logservice.log(request, "心理检测中心:问卷调查", "问卷答题完成");
        return "/assessmentcenter/invest/investdone";
    }

    @RequestMapping(value = { "{taskid}/{scaleid}/investresult" })
    public String investresult(HttpServletRequest request, @PathVariable("taskid") long taskid,
            @PathVariable("scaleid") int scaleid, Model model) {

        InvestExam IE = investService.selectByPrimaryKey(scaleid);
        String xmlStr = IE.getXmlstr();
        Scale scale = investService.getScaleFromInvestExam(IE);
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setScale(scale);
        questionnaire.openInvest();
        List<InvestStat> investStatList = investService.getInvestResult(taskid);
        if (investStatList == null || investStatList.size() == 0) {
            model.addAttribute("investInfoArray", null);
            model.addAttribute("questionnaire", questionnaire);
            model.addAttribute("taskid", taskid);
            return "/assessmentcenter/invest/investresult";
        }
        List<QuestionBlock> questions = questionnaire.getQuestionBlocks();
        List<Map<String, Object>> investInfoArray = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < questionnaire.getQuestionSize(); i++) {
            Map<String, Object> investInfo = new HashMap<String, Object>();
            QuestionBlock questionBlock = questions.get(i);
            String id = questionBlock.getDisplayId();
            id = id.replace("Q", "");
            investInfo.put("qid", id);
            investInfo.put("questionBlock", questionBlock);
            investInfo.put("qtitle", questionBlock.getQuestion().getTitle());
            InvestStat investStat = investStatList.get(i);
            investInfo.put("investStat", investStat);
            Map<String, Object> countMap = new HashMap<String, Object>();
            for (int j = 0; j < 10; j++) {
                countMap.put(String.valueOf(j), investStat.getOptValue(j));
            }
            Map<String, Object> countpMap = new HashMap<String, Object>();
            for (int j = 0; j < 10; j++) {
                if (investStat.getSum() > 0) {
                    double p = (investStat.getOptValue(j) * 100) / investStat.getSum();
                    DecimalFormat df = new DecimalFormat(".##");

                    String pstr = df.format(p);
                    String pStr = pstr + "%";
                    countpMap.put(String.valueOf(j), pStr);
                } else
                    countpMap.put(String.valueOf(j), "0.00%");
            }
            investInfo.put("countMap", countMap);
            investInfo.put("countpMap", countpMap);
            investInfo.put("sum", investStat.getSum());
            if (questionBlock.getQuestion().getTypeMode() == QuestionConsts.TYPE_SELECTION_MODE) {
                SelectionQuestion sQ = (SelectionQuestion) questionBlock.getQuestion();
                String qType = "";
                if (sQ.getChoiceMode() == QuestionConsts.CHOICE_SINGLE_MODE)// 单选题
                    qType = "单选题";
                if (sQ.getChoiceMode() == QuestionConsts.CHOICE_MULTI_MODE)// 多选题，分数之和
                    qType = "多选题";
                investInfo.put("qtype", qType);
            }
            investInfoArray.add(investInfo);
        }

        // investResultMapper
        // List<InvestStat> investStatList =
        // investService.getInvestResult(taskid);
        model.addAttribute("investInfoArray", investInfoArray);
        model.addAttribute("questionnaire", questionnaire);
        model.addAttribute("taskid", taskid);

        return "/assessmentcenter/invest/investresult";
    }

    /*
     * @RequestMapping(value = { "/preadd" }) public String
     * preadd(HttpServletRequest request, Model model) {
     * model.addAttribute("invest", new InvestExam());
     * model.addAttribute("questions", new ArrayList<Question>()); return
     * "/assessmentcenter/invest/add"; }
     */
    @RequestMapping(value = { "/preadd" })
    public String preadd(HttpServletRequest request, Model model) {
        // model.addAttribute("invest", new InvestExam());
        // model.addAttribute("questions", new ArrayList<Question>());
        return "/assessmentcenter/invest/createBlankInvest";
    }

    @RequestMapping(value = { "/createInvest" }, method = RequestMethod.POST)
    public String createInvest(HttpServletRequest request, Model model) {
        String investName = request.getParameter("investname");
        String investGuide = request.getParameter("content");
        model.addAttribute("title", investName);
        model.addAttribute("guide", investGuide);
        return "/assessmentcenter/invest/createInvest";
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public String delete(HttpServletRequest request) {
        String idstr = request.getParameter("ids");
        JSONArray ids = JSONArray.fromObject(idstr);
        if (null != ids) {
            investService.delInvests(ids);
        }
        return "success";
    }

    @RequestMapping(value = "{itemId}/del", method = RequestMethod.POST)
    @ResponseBody
    public String delete(HttpServletRequest request, @PathVariable("itemId") String itemId) {
        if (StringUtils.isNoneEmpty(itemId)) {
            investService.delInvest(itemId);
        }
        logservice.log(request, "心理检测中心:问卷调查", "删除问卷");
        return "success";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(HttpServletRequest request) throws IOException {
        Account account = (Account) request.getSession().getAttribute("user");
        long userid = account.getId();
        InvestExam IE = new InvestExam();
        // 解析json
        String questionsStr = request.getParameter("questions");
        String investTitle = request.getParameter("title");
        String descn = request.getParameter("descn");
        IE.setName(investTitle);
        JSONArray questions = null;
        Scale invest = new Scale();
        invest.setTitle(investTitle);
        invest.setDescn(descn);
        if (StringUtils.isNotEmpty(questionsStr)) {
            questions = JSONArray.fromObject(questionsStr);
            IE.setQnum(questions.size());
            // 默认没有审核通过
            IE.setStatus(Byte.valueOf("0"));
            for (int i = 0; i < questions.size(); i++) {
                JSONObject question = (JSONObject) questions.get(i);
                String qtitle = question.getString("title");
                String type = question.getString("type");
                SelectionQuestion q = new SelectionQuestion();
                q.setId("Q" + (i + 1));
                q.setTitle(qtitle);
                if ("1".equals(type)) {
                    q.setChoice(QuestionConsts.CHOICE_SINGLE);
                } else {
                    q.setChoice(QuestionConsts.CHOICE_MULTI);
                }
                JSONArray options = question.getJSONArray("options");
                for (int j = 0; j < options.size(); j++) {
                    Option o = new Option();
                    o.setId(Integer.toString(j + 1));
                    o.setTitle(options.getString(j));
                    q.addOption(o);
                }
                invest.addQuestion(q);
            }
            invest.setDimensionMap(new OrderValueMap());
            IE.setXmlstr(invest.toXml(false));
            IE.setOwnerid(userid);

            // JSONObject investObj = JSONObject.fromObject(invest);
            // IE.setXmlstr(investObj.toString());
            investService.insertInvestExam(IE);
        }

        return "success";
    }

    @RequestMapping(value = "{itemId}/update", method = RequestMethod.GET)
    // @ResponseBody
    public String update(HttpServletRequest request, @PathVariable("itemId") Integer itemId, Model model)
            throws IOException, SAXException, DimensionException {
        // 解析json
        InvestExam IE = investService.selectByPrimaryKey(itemId);
        String xmlStr = IE.getXmlstr();
        xmlStr = xmlStr.replaceAll("&", "&amp;");
        StringInputStream input = new StringInputStream(xmlStr);
        Scale scale = new Scale();
        ScaleBuilder.doBuild(input, scale);
        JSONObject sobj = new JSONObject();
        sobj.accumulate("id", itemId);
        sobj.accumulate("title", scale.getTitle());
        String descn = HtmlStr.decodeString(scale.getDescn());
        sobj.accumulate("descn", descn);
        JSONArray qArr = new JSONArray();
        OrderValueMap<String, Question> qmap = scale.getQuestionMap();
        Iterator<String> qit = qmap.keySet().iterator();
        while (qit.hasNext()) {
            SelectionQuestion sq = (SelectionQuestion) qmap.get(qit.next());
            JSONObject q = new JSONObject();
            q.accumulate("id", sq.getId());
            q.accumulate("title", sq.getTitle());
            JSONArray ops = new JSONArray();
            for (int i = 0; i < sq.getOptions().size(); i++) {
                ops.add(sq.getOptions().get(i));
            }
            q.accumulate("options", ops);
            qArr.add(q);
        }
        sobj.accumulate("questions", qArr);
        model.addAttribute("invest", sobj.toString());
        logservice.log(request, "心理检测中心:问卷调查", "问卷修改");
        return "/assessmentcenter/invest/update";
    }

    @RequestMapping(value = "{itemId}/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(HttpServletRequest request, @PathVariable("itemId") Integer itemId)
            throws IOException, SAXException, DimensionException {
        InvestExam IE = new InvestExam();
        // 解析json
        String questionsStr = request.getParameter("questions");
        String investTitle = request.getParameter("title");
        IE.setName(investTitle);
        JSONArray questions = null;
        Scale invest = new Scale();
        invest.setTitle(investTitle);
        IE.setId(itemId);
        if (StringUtils.isNotEmpty(questionsStr)) {
            questions = JSONArray.fromObject(questionsStr);
            IE.setQnum(questions.size());
            // 默认没有审核通过
            IE.setStatus(Byte.valueOf("0"));
            for (int i = 0; i < questions.size(); i++) {
                JSONObject question = (JSONObject) questions.get(i);
                String qtitle = question.getString("title");
                String type = question.getString("type");
                SelectionQuestion q = new SelectionQuestion();
                q.setId("Q" + i);
                q.setTitle(qtitle);
                if ("1".equals(type)) {
                    q.setChoice(QuestionConsts.CHOICE_SINGLE);
                } else {
                    q.setChoice(QuestionConsts.CHOICE_MULTI);
                }
                JSONArray options = question.getJSONArray("options");
                for (int j = 0; j < options.size(); j++) {
                    Option o = new Option();
                    o.setId(Integer.toString(j + 1));
                    o.setTitle(options.getString(j));
                    q.addOption(o);
                }
                invest.addQuestion(q);
            }
            IE.setXmlstr(invest.toXml(false));
            investService.updateByPrimaryKeyWithBLOBs(IE);
            logservice.log(request, "心理检测中心:问卷调查", "问卷创建成功");
            return "success";
        } else {
            return "false";
        }
    }

    @RequestMapping(value = "{itemId}/view", method = RequestMethod.GET)
    public String view(HttpServletRequest request, @PathVariable("itemId") Integer itemId, Model model)
            throws IOException, SAXException, DimensionException {
        InvestExam IE = investService.selectByPrimaryKey(itemId);
        String xmlStr = IE.getXmlstr();
        xmlStr = xmlStr.replaceAll("&", "&amp;");
        StringInputStream input = new StringInputStream(xmlStr);
        Scale scale = new Scale();
        ScaleBuilder.doBuild(input, scale);
        scale.setQuestionNum(scale.getQuestionMap().size());
        model.addAttribute("title", scale.getTitle());
        model.addAttribute("questionHtml", scale.toQuestionHTML());
        // JSONObject invest = new JSONObject();
        // invest.accumulate("title", scale.getTitle());
        // invest.accumulate("questionHtml", scale.toQuestionHTML());
        return "/assessmentcenter/invest/view";
    }

    /**
     * 学校问卷分发开始
     */
    @RequestMapping(value = "{id}/schooldispenseview", method = RequestMethod.GET)
    public String schooldispenseview(@CurrentOrg Organization orgEntity, HttpServletRequest req,
            @PathVariable("id") long id, Model model) {
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
        model.addAttribute("scaleid", id);
        model.addAttribute("orglevel", 2);
        model.addAttribute("xdlist", xdidList);
        return viewName("schooldispenseview");

    }

    @RequestMapping(value = "/schooldistribute", method = RequestMethod.POST)
    public String schooldispenseAction(@CurrentOrg Organization orgEntity, HttpServletRequest req, Model model) {
        long orgid = orgEntity.getId();
        Account account = (Account) req.getSession().getAttribute("user");
        long createuserid = account.getId();
        // String countyid = orgEntity.getCountyid();
        // String cityid = orgEntity.getCityid();
        String taskname = req.getParameter("taskname");
        // String flag= req.getParameter("flag"); //时间限制
        String starttime = req.getParameter("starttime"); // 开始时间
        String endtime = req.getParameter("endtime"); // 结束时间
        String createtime = req.getParameter("createtime");
        int objecttype = Integer.parseInt(req.getParameter("objectType").toString()); // 分发对象
        String xd = req.getParameter("gradepart");// 学段
        String[] gradeClassIds = req.getParameterValues("gradeClassId"); // 分发班级
        int scaleid = Integer.parseInt(req.getParameter("scaleid").toString()); // 分发量表
        String[] teacherroleIds = req.getParameterValues("teacherRole"); // 分发教师权限
        List resultMsgList = new ArrayList();

        investService.schooldistribute(orgid, objecttype, scaleid, createuserid, taskname, starttime, endtime,
                createtime, gradeClassIds, teacherroleIds, resultMsgList);
        // schooldistribute(orgid, objecttype, scaleid, taskname, starttime,
        // endtime, createtime, classIds, teacherroleIds,resultMsgList);
        model.addAttribute("resultmsglist", resultMsgList);
        logservice.log(req, "心理检测中心:问卷调查", "问卷分发");
        return viewName("dispenseclose");
    }

    /**
     * 教委问卷分发开始
     */
    @RequestMapping(value = "{id}/edudispenseview", method = RequestMethod.GET)
    public String edudispenseview(@CurrentOrg Organization orgEntity, HttpServletRequest req,
            @PathVariable("id") long id, Model model) {
        OrganizationType orgType = orgEntity.returnOrgTypeEnum();
        int orglevel = orgEntity.getOrgLevel();
        String cityid = orgEntity.getCityid();
        String countyid = orgEntity.getCountyid();
        model.addAttribute("orgtype", orgType.value());
        model.addAttribute("orglevel", orglevel);
        model.addAttribute("cityid", cityid);
        model.addAttribute("countyid", countyid);
        // model.addAttribute("orgtype", "1");
        model.addAttribute("scaleid", id);
        // model.addAttribute("orglevel", 2);
        return viewName("edudispenseview");
    }

    @RequestMapping(value = "/edudistribute", method = RequestMethod.POST)
    public String edudispenseAction(@CurrentOrg Organization orgEntity, HttpServletRequest req, Model model) {
        long orgid = orgEntity.getId();
        int orglevel = orgEntity.getOrgLevel();
        Account account = (Account) req.getSession().getAttribute("user");
        long createuserid = account.getId();
        long creater_orgid = orgEntity.getId();
        String taskname = req.getParameter("taskname"); // 时间限制
        String starttime = req.getParameter("starttime"); // 开始时间
        String endtime = req.getParameter("endtime"); // 结束时间
        int objecttype = Integer.parseInt(req.getParameter("objectType").toString()); // 分发对象
        String xd = req.getParameter("gradepart");// 学段
        String[] areaIds = req.getParameterValues("areaid"); // 分发区县、乡镇
        String[] gradeIds = null;
        if (objecttype == 1) {
            if (xd.equals("1"))
                gradeIds = req.getParameterValues("nj1"); // 分发年级
            if (xd.equals("2"))
                gradeIds = req.getParameterValues("nj2"); // 分发年级
            if (xd.equals("3"))
                gradeIds = req.getParameterValues("nj3"); // 分发年级
        }
        // String teacherRole = req.getParameter("teacherRole"); //分发教师权限
        int scaleid = Integer.parseInt(req.getParameter("scaleid").toString()); // 分发量表
        String[] teacherroleIds = req.getParameterValues("teacherRole"); // 分发教师权限
        String[] schoolIds = null;
        if (req.getParameter("schoolarray") != null) {
            String schoolArrayStr = req.getParameter("schoolarray");
            schoolIds = schoolArrayStr.split(",");
        }
        if (req.getParameterValues("subschoolid") != null)
            schoolIds = req.getParameterValues("subschoolid"); // 分发直属学校
        long[] orgIds = null;
        if (areaIds != null) {// 县级以上教委，机构集合就是选择的行政区所属教委
            orgIds = new long[areaIds.length];
            for (int i = 0; i < areaIds.length; i++) {
                if (orgEntity.getOrgLevel() == 3) {// 市教委
                    Organization orgnization = organizationService.getEduOrganizationByCountyId(areaIds[i]);
                    if (orgnization == null)
                        continue;
                    orgIds[i] = orgnization.getId();
                }

            }
        }
        if (orgEntity.getOrgLevel() == 4) {// 县教委,机构集合就是下发学校的机构集合
            // Organization orgnization =
            // organizationService.getEduOrganizationByCountyId(areaIds[i]);
            if (schoolIds != null && schoolIds.length > 0) {
                orgIds = new long[schoolIds.length];
                for (int i = 0; i < schoolIds.length; i++) {
                    orgIds[i] = Integer.parseInt(schoolIds[i]);
                }
            }
        }
        List resultMsgList = new ArrayList();
        if (objecttype == 1)
            investService.eduDistributeForStu(orglevel, taskname, createuserid, creater_orgid, areaIds, orgIds,
                    starttime, endtime, gradeIds, scaleid, schoolIds, resultMsgList);
        if (objecttype == 2)
            investService.eduDistributeForTch(orglevel, taskname, createuserid, creater_orgid, areaIds, orgIds,
                    starttime, endtime, teacherroleIds, scaleid, schoolIds, resultMsgList);
        model.addAttribute("resultmsglist", resultMsgList);
        logservice.log(req, "心理检测中心:问卷调查", "问卷分发");
        return viewName("dispenseclose");
    }

    // 查看进程是否可以撤销的接口
    @RequestMapping(value = "/checkProcessingStatus", method = RequestMethod.POST)
    @ResponseBody
    public boolean checkProcessingStatus(HttpServletRequest request, @RequestParam("itid") int itid,
            @RequestParam("stype") String stype) {
        Account account = (Account) request.getSession().getAttribute("user");
        long createuserid = account.getId();
        boolean res = investService.checkProcessingStatus(itid, createuserid);
        return res;
    }

    // 回撤操作
    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    @ResponseBody
    public String withdraw(HttpServletRequest requst, @RequestParam("itid") int itid,
            @RequestParam("stype") String stype) {
        String result = "success";
        try {
            this.investService.withdraw(itid, stype);
            logservice.log(requst, "心理检测中心:问卷调查", "问卷撤回");
        } catch (Exception e) {
            result = "fail";
        }
        return result;
    }

    // 分发操作
    @RequestMapping(value = "/investTransfer", method = RequestMethod.POST)
    @ResponseBody
    public String investTransfer(@CurrentOrg Organization orgEntity, HttpServletRequest requst,
            @RequestParam("itid") long itid, @RequestParam("stype") String stype) {
        String result = "success";
        try {
            long orgid = orgEntity.getId();
            result = investService.investTransfer(itid, orgid, stype);
            logservice.log(requst, "心理检测中心:问卷调查", "问卷下发");
        } catch (Exception e) {
            result = "fail";
        }
        return result;
    }

}
