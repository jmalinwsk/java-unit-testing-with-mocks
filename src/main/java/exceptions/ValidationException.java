package exceptions;

public class ValidationException extends Exception {
    String msg = "";

    public ValidationException() {}

    public ValidationException(String msg) {
        super();
        this.msg = msg;
    }

    public String toString() {
        return "VALIDATION FAILED! " + msg;
    }
}
