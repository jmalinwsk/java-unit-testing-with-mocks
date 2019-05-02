package exceptions;

public class ValidationException extends Exception {
    private String msg;

    public ValidationException(String msg) {
        super();
        this.msg = msg;
    }

    public String toString() {
        return "VALIDATION FAILED! " + msg;
    }
}
