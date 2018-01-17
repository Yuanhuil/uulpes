package edutec.scale.model;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.text.StrBuilder;

import heracles.util.UtilCollection;

public class MatrixQuestion extends Question implements Serializable, Calculable {
    /**
     * 
     */
    private static final long serialVersionUID = 7562964268265504149L;
    private static final char SEPOR = '#';
    private String columnTitles;
    private String rowTitles;
    private String values;
    private String choice;
    private String columnTitlesExt;
    private String rowTitlesExt;
    private String selectOpts;
    private String calcExp;
    private String defval;
    private String rtx;
    private String rowParts;

    public String getRowParts() {
        return rowParts;
    }

    public void setRowParts(String rowParts) {
        this.rowParts = rowParts;
    }

    public String getRtx() {
        return rtx;
    }

    public void setRtx(String rtx) {
        this.rtx = rtx;
    }

    public MatrixQuestion() {
        this.setType(QuestionConsts.TYPE_MATRIX);
        this.setChoice(QuestionConsts.CHOICE_SINGLE);
    }

    public String getColumnTitlesExt() {
        return columnTitlesExt;
    }

    public void setColumnTitlesExt(String columnTitlesExt) {
        this.columnTitlesExt = columnTitlesExt;
    }

    public String getRowTitlesExt() {
        return rowTitlesExt;
    }

    public void setRowTitlesExt(String rowTitlesExt) {
        this.rowTitlesExt = rowTitlesExt;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public String getColumnTitles() {
        return columnTitles;
    }

    public void setColumnTitles(String columnTitles) {
        this.columnTitles = columnTitles;
    }

    public String getRowTitles() {
        return rowTitles;
    }

    public void setRowTitles(String rowTitles) {
        this.rowTitles = rowTitles;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getSelectOpts() {
        return selectOpts;
    }

    public void setSelectOpts(String selectOpts) {
        this.selectOpts = selectOpts;
    }

    public String[] columnTitles() {
        return UtilCollection.toArray(columnTitles, MatrixQuestion.SEPOR);
    }

    public String[] rowTitles() {
        return UtilCollection.toArray(rowTitles, MatrixQuestion.SEPOR);

    }

    public String[] values() {
        return UtilCollection.toArray(values, MatrixQuestion.SEPOR);
    }

    public int[] selectOpts() {
        final String array[] = StringUtils.split(selectOpts, MatrixQuestion.SEPOR);
        if (ArrayUtils.isEmpty(array)) {
            return null;
        }
        final int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = NumberUtils.toInt(array[i], 1);
        }
        return result;
    }

    public String getCalcExp() {
        return calcExp;
    }

    public void setCalcExp(String calcExp) {
        this.calcExp = calcExp;
    }

    public MxPart getMxPart() {
        if (StringUtils.isBlank(rowParts)) {
            return null;
        }
        String strs[] = StringUtils.split(rowParts, SEPOR);
        return new MxPart(NumberUtils.toInt(strs[0]), strs[1]);
    }

    public MxInfo getMxInfo() {
        return new MxInfo(this);
    }

    public String getDefval() {
        return defval;
    }

    public void setDefval(String defval) {
        this.defval = defval;
    }

    @Override
    protected void xmlInter(StrBuilder sb) {
        sb.append("\t").append("<t>").append(this.getTitle()).append("</t>\n");
        if (StringUtils.isNotBlank(getCalcExp()))
            sb.append("\t").append("<c>").append(getCalcExp()).append("</c>\n");
        sb.append("\t").append("<cts>").append(getColumnTitles()).append("</cts>\n");
        sb.append("\t").append("<rts>").append(getRowTitles()).append("</rts>\n");
        sb.append("\t").append("<vas>").append(this.getValues()).append("</vas>\n");
        if (StringUtils.isNotBlank(this.getSelectOpts()))
            sb.append("\t").append("<sel>").append(getSelectOpts()).append("</sel>\n");
    }

    public class MxInfo {
        private String[] coltitles;
        private String[] rowtitles;
        private int limits[];
        private int score[];

        MatrixQuestion question;

        public MxInfo(MatrixQuestion question) {
            this.question = question;
            coltitles = question.columnTitles();
            rowtitles = question.rowTitles();
            limits = question.selectOpts();
            String v = question.getDefval();
            /* 如果没有默认值 */
            if (StringUtils.isBlank(v)) {
                String values[] = question.values();
                score = UtilCollection.toIntArray(values);
            }
        }

        public String[] getColtitles() {
            return coltitles;
        }

        public String getId() {
            return question.getId();
        }

        public int[] getLimits() {
            return limits;
        }

        public String[] getRowtitles() {
            return rowtitles;
        }

        public int score(int idx) {
            String v = getQuestion().getDefval();
            if (StringUtils.isNotBlank(v)) {
                return NumberUtils.toInt(v);
            }
            return score[idx];
        }

        public int rowsize() {
            return rowtitles.length;
        }

        public int colsize() {
            return coltitles.length;
        }

        public String rowTitle(int rowIdx) {
            return rowtitles[rowIdx];
        }

        public String colTitle(int rowIdx) {
            return coltitles[rowIdx];
        }

        public int limit(int rowIdx) {
            try {
                return limits[rowIdx];
            } catch (Exception ex) {
                return -1;
            }
        }

        public int rowStartIdx(int rowIdx) {
            return rowIdx * colsize();
        }

        public int rowEndIdx(int rowIdx) {
            return rowStartIdx(rowIdx) + colsize();
        }

        public String getChoice() {
            return question.getChoice();
        }

        public MatrixQuestion getQuestion() {
            return question;
        }

        public MxPart getMxPart() {
            return question.getMxPart();
        }
    }

    public class MxPart {
        private int position;
        private String holdPlace;

        public MxPart(int position, String holdPlace) {
            this.position = position;
            this.holdPlace = holdPlace;
        }

        public MxPart() {
        }

        public String getHoldPlace() {
            return holdPlace;
        }

        public void setHoldPlace(String holdPlace) {
            this.holdPlace = holdPlace;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    @Override
    public String toHTML() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toAnswerHtml(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toHeadtitleHTML() {
        // TODO Auto-generated method stub
        return null;
    }

}
