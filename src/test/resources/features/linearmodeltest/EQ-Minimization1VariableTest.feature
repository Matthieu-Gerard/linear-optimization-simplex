@LinearModelTest
Feature: EQ-Minimization1VariableTest

  Scenario: A- EQ-Minimization1VariableTest
    Given The linear model have 1 variables.
    And The linear cost values are:
      | vector C |
      | 1.0      |
    And The linear constraints are:
      | matrix A | type | value |
      | 1.0      | =   | 1.0   |
    And The objective strategy is minimizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X | solutionCost |
      | 1.0      | 1.0          |

  Scenario: B- EQ-Minimization1VariableTest
    Given The linear model have 1 variables.
    And The linear cost values are:
      | vector C |
      | 1.0      |
    And The linear constraints are:
      | matrix A | type | value |
      | 1.0      | =   | 0.0   |
    And The objective strategy is minimizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    Then The linear solution is:
      | vector X | solutionCost |
      | 0.0      | 0.0          |

   Scenario: C- EQ-Minimization1VariableTest
    Given The linear model have 1 variables.
    And The linear cost values are:
      | vector C |
      | 1.0      |
    And The linear constraints are:
      | matrix A | type | value |
      | 1.0      | =   | 2.0   |
    And The objective strategy is minimizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    Then The linear solution is:
      | vector X | solutionCost |
      | 2.0      | 2.0          |

   Scenario: D- EQ-Minimization1VariableTest
    Given The linear model have 1 variables.
    And The linear cost values are:
      | vector C |
      | 1.0      |
    And The linear constraints are:
      | matrix A | type | value |
      | 1.0      | =   | 0.5   |
    And The objective strategy is minimizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    Then The linear solution is:
      | vector X | solutionCost |
      | 0.5      | 0.5          |

	Scenario: E- EQ-Minimization1VariableTest
    Given The linear model have 1 variables.
    And The linear cost values are:
      | vector C |
      | 0.5      |
    And The linear constraints are:
      | matrix A | type | value |
      | 1.0      | =   | 1.0   |
    And The objective strategy is minimizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    Then The linear solution is:
      | vector X | solutionCost |
      | 1.0      | 0.5          |
      
  Scenario: F- EQ-Minimization1VariableTest
    Given The linear model have 1 variables.
    And The linear cost values are:
      | vector C |
      | 0.5      |
    And The linear constraints are:
      | matrix A | type | value |
      | 1.0      | =   | 2.0   |
    And The objective strategy is minimizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    Then The linear solution is:
      | vector X | solutionCost |
      | 2.0      | 1.0          |
      