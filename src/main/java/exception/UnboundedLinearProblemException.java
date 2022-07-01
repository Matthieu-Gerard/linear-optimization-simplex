package exception;

public class UnboundedLinearProblemException extends RuntimeException {
    
    private String message = "Linear program is unbounded";
    
    public UnboundedLinearProblemException() {
        super();
    }
}
