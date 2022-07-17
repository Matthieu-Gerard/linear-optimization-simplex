package cucumber.datatabletype;

import java.util.Map;

import cucumber.model.SimplexConstraintLeqParameters;
import io.cucumber.java.DataTableType;

public class SimplexConstraintLeqParametersTableType {

    @DataTableType
    public SimplexConstraintLeqParameters schedulePrametersEntry(Map<String, String> entry) {

        double maximumValue = Double.parseDouble(entry.get("maximumValue B"));
        
        String[] coefficientsInStringFormat = entry.get("matrix A").split(",");
        if (0<coefficientsInStringFormat.length) {
            double[] coefficients = new double[coefficientsInStringFormat.length];
            int index = 0;
            for (String coefficientInstringFormat : coefficientsInStringFormat) {
                coefficients[index] = Double.parseDouble(coefficientInstringFormat);
                index ++;
            }
            return new SimplexConstraintLeqParameters(coefficients,maximumValue);
        }

        return null;
    }
}