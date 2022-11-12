package cucumber.datatabletype;

import java.util.Map;

import cucumber.model.LinearConstraintParameters;
import io.cucumber.java.DataTableType;
import model.constraint.LinearConstraintType;

public class LinearConstraintParametersTableType {

    @DataTableType
    public LinearConstraintParameters schedulePrametersEntry(Map<String, String> entry) {
        
        double value = Double.parseDouble(entry.get("value"));
        
        String[] coefficientsInStringFormat = entry.get("matrix A").split(",");
        if (0<coefficientsInStringFormat.length) {
            double[] coefficients = new double[coefficientsInStringFormat.length];
            int index = 0;
            for (String coefficientInstringFormat : coefficientsInStringFormat) {
                coefficients[index] = Double.parseDouble(coefficientInstringFormat);
                index ++;
            }
            
            String type = entry.get("type");
            LinearConstraintType constraintType = null;
            if (type.compareTo("<=")==0) {
                constraintType = LinearConstraintType.LEQ;
            }
            else if (type.compareTo(">=")==0) {
                constraintType = LinearConstraintType.GEQ;
            }
            else if (type.compareTo("=")==0) {
                constraintType = LinearConstraintType.EQ;
            }
            
            if (constraintType!=null) {
                return new LinearConstraintParameters(coefficients,constraintType,value);
            }
        }
        return null;
    }
}