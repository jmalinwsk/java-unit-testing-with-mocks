package exceptions;

public class ElementNotFoundException extends Exception {
    private String msg;

    public ElementNotFoundException(String msg) {
        super();
        this.msg = msg;
    }

    public String toString() {
        return "ELEMENT NOT FOUND IN DATABASE! " + msg;
    }
}
