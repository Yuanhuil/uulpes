package edutec.scale.model;

public enum ScaleModuleTitleEnum {
    HOMEPAGE("首页"), QUESTION("题本"), DIMENSION("维度"), SCORE("分数"), SCORING("计分"), CLASSIFY("得分水平划分"), PREWARN(
            "预警级别"), NORM("常模"), EXPL("结果解释"), INSTR("指导建议"), EXPL_STU("结果解释-学生版"), EXPL_TEAC("结果解释-教师版"), EXPL_PAR(
                    "结果解释-家长版"), INSTR_STU("指导建议-学生版"), INSTR_PAR("指导建议-家长版"), INSTR_TEAC("指导建议-教师版"), HIDDEN(
                            "配置"), IMAGE_PARAM("图表参数"), EXPL_INSTR(
                                    "0"), EXPL_INSTR_STU("1"), EXPL_INSTR_PAR("3"), EXPL_INSTR_TEAC("2");
    private String title;

    private ScaleModuleTitleEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public static void main(String[] args) {
        System.out.println(ScaleModuleTitleEnum.CLASSIFY.getTitle());
    }
}
