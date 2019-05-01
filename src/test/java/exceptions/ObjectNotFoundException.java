package exceptions;

public class ObjectNotFoundException extends Exception {

    public ObjectNotFoundException() {
    }

    public String toString() {
        return "Given type of object doesn't exist!";
    }
}
