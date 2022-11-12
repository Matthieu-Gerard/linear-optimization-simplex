package model;

import java.util.ArrayList;
import java.util.List;

import model.matrix.ElementMatrix;
import model.matrix.SparseMatrix;

public class SimplexTableau extends LinearModel {

    private List<Variable> slackVariables;
    private List<Variable> artificialVariables;
    
    public SimplexTableau() {
        super();
        setSlackVariables(new ArrayList<>());
        setArtificialVariables(new ArrayList<>());
    }

    public List<Variable> getSlackVariables() {
        return slackVariables;
    }

    public void setSlackVariables(List<Variable> slackVariables) {
        this.slackVariables = slackVariables;
    }

    public List<Variable> getArtificialVariables() {
        return artificialVariables;
    }

    public void setArtificialVariables(List<Variable> artificialVariables) {
        this.artificialVariables = artificialVariables;
    }
}
