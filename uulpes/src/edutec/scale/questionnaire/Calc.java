package edutec.scale.questionnaire;

import edutec.scale.exception.CalcException;

public interface Calc {
    public static final char[] ARITH_OPERATOR = { '+', '-', '*', '/' };
    public static final String PRODUCT_LEXEME = "product";
    public static final String ARG_LEXEME = "arg";
    public static final String SUM_LEXEME = "sum";
    public static final char OPEN_BLOCK = '{';
    public static final char CLOSE_BLOCK = '}';
    public static final char EQ = '=';
    public static final char STMT_SEPARATOR = ';';
    public static final char VARS_PROC_GAP = '#';

    public Number evaluate() throws CalcException;
}