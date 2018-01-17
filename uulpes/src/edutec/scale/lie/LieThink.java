package edutec.scale.lie;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.njpes.www.constant.Constants;

import edutec.scale.lie.LieItem.Detected;
import edutec.scale.model.Option;
import edutec.scale.model.SelectionQuestion;
import edutec.scale.questionnaire.Questionnaire;
import edutec.scale.util.ScaleConstants;
import heracles.spring.MainBeanFactory;
import heracles.util.Pools;
import heracles.util.UtilCollection;
import heracles.web.config.ApplicationConfiguration;

public class LieThink {
    private static final String LIE_PREFIX = "lie_";
    final static String FILE_NAME = "lie_questions.xml";
    final static String YES_NO = "1,是|0,否";

    final static Map<String, LieItem> lieQItems;
    final static Map<String, List<SelectionQuestion>> sepciLieQues;
    final static LieDictory lieDictory;

    // 目前有11个量表加入测谎范畴
    static {
        lieQItems = new HashMap<String, LieItem>();
        sepciLieQues = new HashMap<String, List<SelectionQuestion>>();

        // 抑郁自评量表
        lieQItems.put("4", new LieItem(6, ">=4", "0,偶无|0,有时|0,经常|1,持续"));
        // SCL-90
        lieQItems.put("6", new LieItem(9, ">=6", YES_NO));
        // 16PF
        lieQItems.put("10", new LieItem(15, ">=10", YES_NO));
        // 焦虑自评量表
        lieQItems.put("11", new LieItem(6, ">4", "0,没有或很少时间|0,小部分时间|0,相当多时间|1,绝大部分或全部时间"));
        // MMT霍兰德职业兴趣
        lieQItems.put("12", new LieItem(15, ">=10", YES_NO, "edutec.scale.lie.HcitInsertPosition"));
        // 艾森克人格问卷(EPQ)(儿童)
        lieQItems.put("13", new LieItem(LieItem.Detected.dimension, "W4>61.5"));
        // 艾森克人格问卷(EPQ)(成人)
        lieQItems.put("14", new LieItem(LieItem.Detected.dimension, "W4>61.5"));
        // 青少年生活事件量表
        lieQItems.put("41", new LieItem(6, ">=4", YES_NO));
        // 父母教养方式量表
        lieQItems.put("49", new LieItem(6, ">=4", "0,从不|0,偶尔|0,经常|1,总是"));
        // 教师职业倦怠量表
        lieQItems.put("901", new LieItem(6, ">=4", "0,从不|0,极少|0,偶尔|0,有时|0,经常|0,极多|1,总是"));
        // 学生心理健康测验
        lieQItems.put("902", new LieItem(LieItem.Detected.dimension, "W9>=7"));

        // 九型人格
        lieQItems.put("903", new LieItem(10, ">=7"));

        // 读取题目
        File dir = ApplicationConfiguration.getInstance().makeWorkSubDir(ScaleConstants.SCALE_FILE_DIR);
        File file = new File(dir, FILE_NAME);
        Resource resource = new FileSystemResource(file);
        MainBeanFactory factory = new MainBeanFactory(resource);
        lieDictory = factory.getBean("LieDictory");

        extractSepciLies();

    }

    // static public boolean validate(Questionnaire questionnaire) throws
    // CalcException {
    // if (lieQItems.containsKey(questionnaire.getScaleId())) {
    // return lieQItems.get(questionnaire.getScaleId()).validate(questionnaire);
    // }
    // return true;
    // }

    static public LieItem getLieItem(String scaleId) {
        return lieQItems.get(scaleId);
    }

    static public List<SelectionQuestion> getLieQuestions(Questionnaire questionnaire) {
        if (!lieQItems.containsKey(questionnaire.getScaleId())) {
            return Collections.emptyList();
        }
        LieItem lieItem = lieQItems.get(questionnaire.getScaleId());
        if (lieItem.getType() == Detected.dimension) {
            return Collections.emptyList();
        }
        List<SelectionQuestion> qlist = selectTemplateLies(lieItem, questionnaire);
        List<SelectionQuestion> result = makeLieQuestions(lieItem, qlist);
        return result;
    }

    private static List<SelectionQuestion> makeLieQuestions(LieItem lieItem, List<SelectionQuestion> qlist) {
        List<SelectionQuestion> result = new ArrayList<SelectionQuestion>(qlist.size());
        for (int idx = 0; idx < qlist.size(); ++idx) {
            SelectionQuestion q = qlist.get(idx);
            SelectionQuestion newQ = new SelectionQuestion(LIE_PREFIX + (idx + 1));
            newQ.setTitle(q.getTitle());
            newQ.setFlag(q.getFlag());
            newQ.setLie(true);
            if (CollectionUtils.isNotEmpty(q.getOptions())) {
                for (Option opt : q.getOptions()) {
                    newQ.addOption(opt);
                }
            } else {
                lieItem.makeOptions(newQ);
            }
            result.add(newQ);
        }
        return result;
    }

    /**
     * 提取用于特定量表的测谎题目，scaleId，针对指定的量表的。
     */
    private static void extractSepciLies() {
        List<String> temp = null;
        try {
            temp = Pools.getInstance().borrowStringList();
            List<SelectionQuestion> ques = lieDictory.getQuestions();
            for (SelectionQuestion question : ques) {
                Map<String, String> props = question.getDescnProps();
                String scaleIds = props.get(Constants.SCALEID_PROP);
                if (StringUtils.isEmpty(scaleIds)) {
                    List<SelectionQuestion> list = sepciLieQues.get("others");
                    if (list == null) {
                        list = new ArrayList<SelectionQuestion>();
                        sepciLieQues.put("others", list);
                    }
                    list.add(question);
                } else {
                    UtilCollection.toList(scaleIds, UtilCollection.COMMA, temp);
                    for (String scaleId : temp) {
                        List<SelectionQuestion> list = sepciLieQues.get(scaleId);
                        if (list == null) {
                            list = new ArrayList<SelectionQuestion>();
                            sepciLieQues.put(scaleId, list);
                        }
                        list.add(question);
                    }
                }
            }
        } finally {
            Pools.getInstance().returnStringList(temp);
        }
    }

    /**
     * 选择出样本测谎题
     * 
     * @param lieItem
     * @param questionnaire
     * @return
     */
    private static List<SelectionQuestion> selectTemplateLies(LieItem lieItem, Questionnaire questionnaire) {
        List<SelectionQuestion> result = new ArrayList<SelectionQuestion>(lieItem.getNumOfAdded());
        Object scaleId = questionnaire.getScaleId();
        List<SelectionQuestion> lies = null;
        List<SelectionQuestion> sepIdlist = sepciLieQues.get(scaleId);
        if (sepIdlist != null) {
            lies = new ArrayList<SelectionQuestion>(sepIdlist);
        } else {
            lies = new ArrayList<SelectionQuestion>(sepciLieQues.get("others"));
        }
        while (result.size() < lieItem.getNumOfAdded()) {
            int idx = RandomUtils.nextInt(lies.size());
            result.add(lies.get(idx));
            lies.remove(idx);
        }
        return result;
    }
}
