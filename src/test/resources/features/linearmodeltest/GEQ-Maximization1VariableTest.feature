@LinearModelTest
Feature: GEQ-Maximization1VariableTest

  Scenario: A- GEQ-Maximization1VariableTest
    Given The linear model have 1 variables.
    And The linear cost values are:
      | vector C |
      | 1.0      |
    And The linear constraints are:
      | matrix A | type | value |
      | 1.0      | >=   | 1.0   |
    And The objective strategy is maximizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X | solutionCost |
      | 0.0      | 0.0          |

  Scenario: B- GEQ-Maximization1VariableTest
    Given The linear model have 1 variables.
    And The linear cost values are:
      | vector C |
      | -1.0     |
    And The linear constraints are:
      | matrix A | type | value |
      | 1.0      | >=   | 0.0   |
    And The objective strategy is maximizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X | solutionCost |
      | 0.0      | 0.0          |

  Scenario: C- GEQ-Maximization1VariableTest
    Given The linear model have 1 variables.
    And The linear cost values are:
      | vector C |
      | -1.0     |
    And The linear constraints are:
      | matrix A | type | value |
      | 1.0      | >=   | 1.0   |
    And The objective strategy is maximizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X | solutionCost |
      | 1.0      | -1.0         |

  Scenario: D- GEQ-Maximization1VariableTest
    Given The linear model have 1 variables.
    And The linear cost values are:
      | vector C |
      | -1.0     |
    And The linear constraints are:
      | matrix A | type | value |
      | 1.0      | >=   | 0.5   |
    And The objective strategy is maximizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X | solutionCost |
      | 0.5      | -0.5         |

  Scenario: E- GEQ-Maximization1VariableTest
    Given The linear model have 1 variables.
    And The linear cost values are:
      | vector C |
      | -1.0     |
    And The linear constraints are:
      | matrix A | type | value |
      | 0.5      | >=   | 1.0   |
    And The objective strategy is maximizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X | solutionCost |
      | 2.0      | -2.0         |
