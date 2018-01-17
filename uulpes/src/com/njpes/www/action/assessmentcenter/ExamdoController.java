package com.njpes.www.action.assessmentcenter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.njpes.www.action.BaseController;
import com.njpes.www.entity.assessmentcenter.Examdo;
import com.njpes.www.entity.baseinfo.Account;
import com.njpes.www.service.assessmentcenter.ExamdoServiceI;
import com.njpes.www.service.scaletoollib.ScaleMgrService;
import com.njpes.www.service.util.DictionaryServiceI;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.model.Option;
import edutec.scale.model.Question;
import edutec.scale.model.QuestionConsts;
import edutec.scale.model.Scale;
import edutec.scale.model.SelectionQuestion;
import heracles.util.OrderValueMap;

@Controller
@RequestMapping(value = "/assessmentcenter/examdo")
public class ExamdoController extends BaseController {

    @Autowired
    private CachedScaleMgr cachedScaleMgr;

    @Autowired
    private ExamdoServiceI examdoService;

    @Autowired
    private ScaleMgrService scaleMgrService;

    @Autowired
    private DictionaryServiceI dictionaryService;

    /**
     * 进入用户分发的量表列表，判断如果是未完成，则继续，没有做过就开始
     * 
     * @param request
     * @param model
     * @author shibin
     * @return
     */
    @RequestMapping(value = { "/list" })
    public String list(HttpServletRequest request, Model model) {
        // 获得用户
        Account account = (Account) request.getSession().getAttribute("user");
        if (null != account) {
            List<Examdo> examdos = examdoService.getExamdoOfAccount(account);
            model.addAttribute("examdos", examdos);
            return "/assessmentcenter/examdo/view";
        } else {
            return "";
        }
    }

    /**
     * 选择了某一张量表后进入start页，点击开始或继续跳到第一页，然后点开始测试进入题本
     * 
     * @param request
     * @param model
     * @param scaleId
     * @author shibin
     * @return
     */
    @RequestMapping(value = { "{scaleid}/start" }, method = RequestMethod.GET)
    public String start(HttpServletRequest request, Model model, @PathVariable("scaleid") String scaleId) {
        // 获得用户
        Account account = (Account) request.getSession().getAttribute("user");
        if (null != account) {
            // 根据scaleid获得scale对象
            Scale scale = cachedScaleMgr.get(scaleId, true);
            // 把scale放入session带到页面
            if (null != scale) {
                request.getSession().setAttribute("scale", scale);
                // model.addAttribute("scale", scale);
                return "/assessmentcenter/examdo/start";
            }
        }
        return "";
    }

    /**
     * 从第一个题目到最后一个题目都在此页，显示题目，记录用户答案，计时
     * 
     * @param request
     * @param model
     * @param scaleId
     * @param qnum
     * @author shibin
     * @return
     */
    @RequestMapping(value = { "{scaleid}/{qnum}" }, method = RequestMethod.GET)
    public String examdo(HttpServletRequest request, Model model, @PathVariable("scaleid") String scaleId,
            @PathVariable("qnum") int qnum) {
        // 获得用户
        Account account = (Account) request.getSession().getAttribute("user");
        Scale scale = (Scale) request.getSession().getAttribute("scale");
        if (null != account) {
            // 根据scaleid获得scale对象
            Question question = scale.getQuestions().get(qnum);
            if (question.getTypeMode() == QuestionConsts.TYPE_SELECTION_MODE) {
                List<Option> options = ((SelectionQuestion) question).getOptions();
                model.addAttribute("opsize", options.size());
                OrderValueMap<String, Option> optionMap = ((SelectionQuestion) question).getOptionMap();
                model.addAttribute("optionMap", optionMap);
                model.addAttribute("choiceMode", question.getChoiceMode());
            }
            // 把question放入session带到页面
            model.addAttribute("title", question.getTitle());
            model.addAttribute("descn", question.getDescn());
            model.addAttribute("qnum", qnum);
            return "/assessmentcenter/examdo/examdo";
        }
        return "";
    }

    /**
     * 每五道题跳到这里计算题目分数，和答案一起保存数据库，然后跳回到题本页
     * 
     * @param request
     * @param model
     * @param scaleId
     * @param qnum
     * @author shibin
     * @return
     */
    @RequestMapping(value = { "{scaleid}/{qnum}/save" }, method = RequestMethod.GET)
    public String save(HttpServletRequest request, Model model, @PathVariable("scaleid") String scaleId,
            @PathVariable("qnum") int qnum) {

        return "/assessmentcenter/examdo/examdo";
    }

    /**
     * 结束答题，计算维度分数，跳到报告页
     * 
     * @param request
     * @param model
     * @param scaleId
     * @author shibin
     * @return
     */
    @RequestMapping(value = { "{scaleid}/complete" }, method = RequestMethod.GET)
    public String complete(HttpServletRequest request, Model model, @PathVariable("scaleid") String scaleId) {

        return "/assessmentcenter/scaletoollib/report";
    }

}
