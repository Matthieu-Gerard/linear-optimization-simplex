package cucumber.datatabletype;

import java.util.Map;

import cucumber.model.CostsParameters;
import io.cucumber.java.DataTableType;

public class CostsTableType {

    @DataTableType
    public CostsParameters schedulePrametersEntry(Map<String, String> entry) {

        String[] costInStringFormat = entry.get("vector C").split(",");
        double[] coefficients = new double[costInStringFormat.length];
        int index = 0;
        for(String coefficient : costInStringFormat) {
            double value = Double.parseDouble(coefficient);
            coefficients[index] = value;
            index ++;
        }
        
        return new CostsParameters(coefficients);
    }
}