package exception;

public class UnboundedLinearProblemException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private String message = "Linear program is unbounded";
    
    public UnboundedLinearProblemException() {
        super();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
