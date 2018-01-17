package edutec.group.domain;

public class DimWarningAndGradeStore {
    private long id;
    private String dim_score;
    private String question_score;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDim_score() {
        return dim_score;
    }

    public void setDim_score(String dim_score) {
        this.dim_score = dim_score;
    }

    public String getQuestion_score() {
        return question_score;
    }

    public void setQuestion_score(String question_score) {
        this.question_score = question_score;
    }

}
