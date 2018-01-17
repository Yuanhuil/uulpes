package edutec.scale.lie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edutec.scale.model.Option;
import edutec.scale.model.SelectionQuestion;
import heracles.util.Resources;
import heracles.util.UtilCollection;
//import edutec.scale.report.ScoreThink;

public class LieItem {
    public enum Detected {
        question, dimension, lies
    }

    private Detected type;
    private int numOfAdded;
    private String reference;
    private List<Option> optionList;
    private String insertPosition = "edutec.scale.lie.CommLieInsertPosition";

    public LieItem() {
        type = Detected.question;
    }

    public LieItem(Detected type, String reference) {
        super();
        this.type = type;
        this.reference = reference;
    }

    public LieItem(int numOfAdded, String reference) {
        this();
        this.numOfAdded = numOfAdded;
        this.reference = reference;
    }

    public LieItem(int numOfAdded, String reference, String optStr) {
        this();
        this.numOfAdded = numOfAdded;
        this.reference = reference;
        List<String> tempList = UtilCollection.toList(optStr, '|');
        optionList = new ArrayList<Option>(tempList.size());
        int idx = 1;
        for (String str : tempList) {
            List<String> tempList1 = UtilCollection.toList(str, ',');
            Option option = new Option();
            option.setId("LieO" + idx);
            option.setValue(tempList1.get(0));
            option.setTitle(tempList1.get(1));
            optionList.add(option);
            idx++;
        }
    }

    public LieItem(int numOfAdded, String reference, String optStr, String insertPosition) {
        this(numOfAdded, reference, optStr);
        this.insertPosition = insertPosition;
    }

    public String getInsertPosition() {
        return insertPosition;
    }

    public void setInsertPosition(String insertPosition) {
        this.insertPosition = insertPosition;
    }

    public Detected getType() {
        return type;
    }

    public void setType(Detected type) {
        this.type = type;
    }

    public int getNumOfAdded() {
        return numOfAdded;
    }

    public void setNumOfAdded(int numOfAdded) {
        this.numOfAdded = numOfAdded;
    }

    public List<Option> getOptionList() {
        if (optionList == null) {
            return Collections.emptyList();
        }
        return optionList;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    // public boolean validate(Questionnaire questionnaire) throws CalcException
    // {
    // if (this.type == Detected.question) {
    // int nLieYes = 0;
    // List<QuestionBlock> qBlks = questionnaire.getQuestionBlocks();
    // for (QuestionBlock qBlk : qBlks) {
    // if (qBlk.getQuestion().isLie()) {
    // nLieYes += qBlk.getScore().intValue();
    // }
    // }
    // return !ScoreThink.contain(this.reference, nLieYes);
    // } else if (this.type == Detected.dimension) {
    // // 目前的情况是维度数目小于10，将来如果有大于等于10的情况，再修改
    // String dimId = this.reference.substring(0, 2);
    // String exp = this.reference.substring(2);
    // DimensionBlock dBlk = questionnaire.findDimensionBlock(dimId);
    // return !ScoreThink.contain(exp, dBlk.getAScore().doubleValue());
    // }
    // return true;
    // }
    public void makeOptions(SelectionQuestion question) {
        if (this.type == Detected.dimension) {
            return;
        }
        for (Option opt : getOptionList()) {
            question.addOption(opt);
        }
    }

    public LieInsertPosition makeLieInsertPosition()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        return (LieInsertPosition) Resources.instantiate(this.insertPosition);
    }
}
