package service.solutionanalyser;

public class SolutionState {
    
    private SolutionStateEnum state = SolutionStateEnum.IS_NOT_ANALYZED;
    private String message = "analyse is not launch";
    
    public SolutionState() {}

    public SolutionStateEnum getState() {
        return state;
    }

    public void setState(SolutionStateEnum state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "FeasibilityAnalyserServiceState [state=" + state + ", message=" + message + "]";
    }
}
