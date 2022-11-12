package service.solutionanalyser;

public enum SolutionStateEnum {
    IS_NOT_ANALYZED,
    IS_NOT_PRIMAL_FEASIBLE,
    IS_PRIMAL_FEASIBLE,
    IS_NOT_DUAL_FEASIBLE,
    IS_DUAL_FEASIBLE,
    IS_NOT_OPTIMAL,
    IS_OPTIMAL;
}
