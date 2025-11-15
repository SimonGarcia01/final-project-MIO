package exceptions;

public class GraphException extends Exception {
    public GraphException(String message) { super(message); }
    public GraphException(String message, Throwable cause) { super(message, cause); }
}
