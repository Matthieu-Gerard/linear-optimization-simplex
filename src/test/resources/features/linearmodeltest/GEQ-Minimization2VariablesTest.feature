@LinearModelTest
Feature: GEQ-Minimization2VariablesTest

  Scenario: A- GEQ-Minimization2VariablesTest
    Given The linear model have 2 variables.
    And The linear cost values are:
      | vector C |
      | 1.0 , 1.0    |
    And The linear constraints are:
      | matrix A | type | value |
      | 1.0 , 1.0   | >=   | 0.0   |
    And The objective strategy is minimizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X | solutionCost |
      | 0.0 , 0.0     | 0.0          |

        Scenario: B- GEQ-Minimization2VariablesTest
    Given The linear model have 2 variables.
    And The linear cost values are:
      | vector C |
      | 0.5 , 1.0    |
    And The linear constraints are:
      | matrix A | type | value |
      | 1.0 , 1.0   | >=   | 1.0   |
    And The objective strategy is minimizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X | solutionCost |
      | 1.0 , 0.0     | 0.5          |
      
              Scenario: C- GEQ-Minimization2VariablesTest
    Given The linear model have 2 variables.
    And The linear cost values are:
      | vector C |
      | 1.0 , 0.5    |
    And The linear constraints are:
      | matrix A | type | value |
      | 1.0 , 1.0   | >=   | 1.0   |
    And The objective strategy is minimizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X | solutionCost |
      | 0.0 , 1.0     | 0.5          |
      
      
      