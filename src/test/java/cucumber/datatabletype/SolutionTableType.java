package cucumber.datatabletype;

import java.util.Map;

import cucumber.model.SolutionParameters;
import io.cucumber.java.DataTableType;

public class SolutionTableType {

    @DataTableType
    public SolutionParameters schedulePrametersEntry(Map<String, String> entry) {

        String[] solutionInStringFormat = entry.get("vector X").split(",");
        double[] values = new double[solutionInStringFormat.length];
        int index = 0;
        for(String coefficient : solutionInStringFormat) {
            double value = Double.parseDouble(coefficient);
            values[index] = value;
            index ++;
        }
        
        double solutionCost = Double.parseDouble(entry.get("solutionCost"));
        
        return new SolutionParameters(values,solutionCost);
    }
}