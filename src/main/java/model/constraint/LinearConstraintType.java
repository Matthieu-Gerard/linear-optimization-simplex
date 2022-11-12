package model.constraint;

public enum LinearConstraintType {
    LEQ, GEQ, EQ;

    public static LinearConstraintType getOppositeConstraintType(LinearConstraintType constraintType) {
        switch(constraintType) {
            case LEQ: return GEQ;
            case GEQ: return LEQ;
            case EQ: return EQ;
        }
        return LEQ;
    }
}
