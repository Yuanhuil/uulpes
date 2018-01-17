package heracles.excel;

public class ExcelException extends Exception {

    private static final long serialVersionUID = 1L;

    public ExcelException() {
        super();
    }

    /**
     * ExcelException constructor
     * 
     * @param s
     *            exception clause
     */
    public ExcelException(String s) {
        super(s);
    }
}
