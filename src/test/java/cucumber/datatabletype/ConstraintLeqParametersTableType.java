package cucumber.datatabletype;

import java.util.Map;

import cucumber.model.ConstraintLeqParameters;
import io.cucumber.java.DataTableType;

public class ConstraintLeqParametersTableType {

    @DataTableType
    public ConstraintLeqParameters schedulePrametersEntry(Map<String, String> entry) {

        double maximumValue = Double.parseDouble(entry.get("maximumValue B"));
        
        String[] coefficientsInStringFormat = entry.get("matrix A").split(",");
        if (0<coefficientsInStringFormat.length) {
            double[] coefficients = new double[coefficientsInStringFormat.length];
            int index = 0;
            for (String coefficientInstringFormat : coefficientsInStringFormat) {
                coefficients[index] = Double.parseDouble(coefficientInstringFormat);
                index ++;
            }
            return new ConstraintLeqParameters(coefficients,maximumValue);
        }

        return null;
    }
}