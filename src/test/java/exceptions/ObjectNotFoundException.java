package exceptions;

public class ObjectNotFoundException extends Exception {

    public String toString() {
        return "Given type of object doesn't exist!";
    }
}
