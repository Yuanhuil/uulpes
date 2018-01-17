package edutec.scale.questionnaire;

import java.util.List;

import edutec.scale.exception.CalcException;
import heracles.util.UtilCollection;

/**
 * 此类主要处理一页面多题的情况，并有一个排序题，排序题排序所选择的题，一个页本身也表示一个题
 * 处理的字符串answer是0,-1,0,1,1,3,-1,-1,-1$3,1,2,0形式
 * $前：第一个数值用来计算整个问卷题目的偏移索引，通过偏移位置+这个数值，偏移位置（题目位置）上的数值为问题的option索引(偏移)
 * $后：的是问题的排序，对应非-1的排序，如上式子 0,1,1,3 - 〉3,1,2,0,即最后一题排在了第一位,第二题排在第二位，...
 * 注意：第一个数值是整个试卷的偏移，表示这个试卷所包含的题目为从这个偏移之后开始（注意：不包括这个偏移），
 * 而这个偏移之后的数字个数就是题目数，其值为此题目的答案; 在解决中学道德量表时，代码有改动 利用数组所包含的两个信息：<br>
 * 1）索引（位置），<br>
 * 2）索引（位置）所包含的值 小学生道德量表
 * 
 * @author Administrator
 */
public class MuiltQuestionCalc extends QuestionCalc {
    public static final char SEP = '$';
    private static final int OFF_START_POS = 1; // 从第二个位置开始访问答题选项
    private static final int NO_SELECT = -1;

    public Number evaluate() throws CalcException {
        String answer = block.getAnswer();
        List<String> answerParts = UtilCollection.toList(answer, SEP);
        if (answerParts.size() == 1) {
            evalGen(answerParts);
        } else if (answerParts.size() == 2) {
            evalEs(answerParts);
        } else if (answerParts.size() == 3) {
            evalHs(answerParts);
        }
        return 0;
    }

    /**
     * 针对小学道德量表所写
     * 
     * @param answerParts
     * @return
     */
    private Number evalEs(List<String> answerParts) {
        try {
            Questionnaire questionnaire = block.getQuestionnaire();
            String[] templateQList = block.getQuestion().getTemplateQeslist();
            // 找到量表题目，使用参数false
            List<QuestionBlock> questionBlocks = questionnaire.getQuestionBlocks(false);
            int optIdxs[] = UtilCollection.toIntArray(answerParts.get(0)); // 题目答案部分
            int orderIdxs[] = UtilCollection.toIntArray(answerParts.get(1)); // 排序部分
            // int needOrderQIdxs[] = new int[orderIdxs.length]; // 需要被排序的题目索引
            int needOrderQIdxs[] = { 0, 1, 2, 3 };
            int startOff = optIdxs[0]; // 第一个数字代表这个问卷题目索引的偏移
            int k = 0;
            final double ADD_V[] = { 5D, 3.67D, 2.33D, 1D };// 小学
            for (int i = 0; i < templateQList.length; ++i) {
                QuestionBlock qblock = questionnaire.findQuestionBlock(templateQList[i]);
                int optIdx = optIdxs[i + 1];
                if (optIdx > NO_SELECT) {
                    qblock.setAnswer(optIdx + "");
                    if (orderIdxs.length > 0) {
                        // needOrderQIdxs[k] = i; //pos->(m)qIdx
                        int j = i > 4 ? i - 4 : i;
                        int pos = orderIdxs[j];
                        Number oldSore = qblock.getScore();
                        Number newSore = oldSore.doubleValue() + ADD_V[pos];
                        qblock.setScore(newSore);
                        k++;
                    }
                } else {
                    qblock.setScore(0);
                }
            }
            // 5分,第二位加3.67分,第三位的加2.33分,第四位的加1分
            // 小学生道德量表做如此计算,需要排序后加分

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return 0;
    }

    /**
     * 针对中学道德量表所写 answerParts含3个字符串，其中第三个位占位串以区分是中学道德量表
     * 
     * @param answerParts
     * @return
     */
    private Number evalHs(List<String> answerParts) {
        try {
            Questionnaire questionnaire = block.getQuestionnaire();
            // 找到量表题目，使用参数false
            List<QuestionBlock> questionBlocks = questionnaire.getQuestionBlocks(false);
            int optIdxs[] = UtilCollection.toIntArray(answerParts.get(0)); // 题目答案部分
            int selOffs[] = UtilCollection.toIntArray(answerParts.get(1)); // 选择出来4道题部分
            int needOrderQIdxs[] = new int[selOffs.length]; // 需要被排序的题目索引

            int startOff = optIdxs[0]; // 第一个数字代表这个问卷题目索引的偏移

            // 1.填充问题的答案
            for (int i = OFF_START_POS; i < optIdxs.length; ++i) {
                int qIdx = startOff + i; // 题目的索引由起始偏移，加上optIdxs的位置所确定
                QuestionBlock qblock = questionBlocks.get(qIdx);
                qblock.setAnswer(optIdxs[i] + "");
            }
            // 2.获得需要排序的问题在问卷中的索引
            for (int i = 0; i < selOffs.length; ++i) {
                needOrderQIdxs[i] = OFF_START_POS + startOff + selOffs[i];
            }
            // 3.给排序的问题加分
            final double ADD_V[] = { 4D, 3D, 2D, 1D }; // 中学
            for (int i = 0; i < needOrderQIdxs.length; ++i) {
                int qIdx = needOrderQIdxs[i];
                QuestionBlock qblock = questionBlocks.get(qIdx);
                Number oldSore = qblock.getScore();
                Number newSore = oldSore.doubleValue() + ADD_V[i];
                qblock.setScore(newSore);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return 0;
    }

    /**
     * 通用的计算方法
     * 
     * @param answerParts
     * @return
     */
    private Number evalGen(List<String> answerParts) {
        String answer = answerParts.get(0);
        Questionnaire questionnaire = block.getQuestionnaire();
        // 找到量表题目，使用参数false
        List<QuestionBlock> questionBlocks = questionnaire.getQuestionBlocks(false);
        int optIdxs[] = UtilCollection.toIntArray(answer);
        int startOff = optIdxs[0]; // 第一个数字代表这个问卷题目索引的偏移
        for (int i = OFF_START_POS; i < optIdxs.length; ++i) {
            int qIdx = startOff + i; // 题目的索引由起始偏移，加上optIdxs的位置所确定
            QuestionBlock qblock = questionBlocks.get(qIdx);
            int optIdx = optIdxs[i];
            qblock.setAnswer(optIdx + "");
        }
        return 0;
    }

    @Override
    protected double evaluateArithmecticExp(String cmputor) {
        return 0;
    }

    @Override
    protected double evaluateDesn() {
        return 0;
    }

    @Override
    protected double[] getVals() {
        return null;
    }

}
