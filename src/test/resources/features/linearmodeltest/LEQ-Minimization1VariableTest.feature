@LinearModelTest
Feature: LEQ-Minimization1VariableTest

  Scenario: A- LEQ-Minimization1VariableTest
    Given The linear model have 1 variables.
    And The linear cost values are:
      | vector C |
      | -1.0     |
    And The linear constraints are:
      | matrix A | type | value |
      | 1.0      | <=   | 1.0   |
    And The objective strategy is minimizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X | solutionCost |
      | 1.0      | -1.0         |

  Scenario: B- LEQ-Minimization1VariableTest
    Given The linear model have 1 variables.
    And The linear cost values are:
      | vector C |
      | -2.0     |
    And The linear constraints are:
      | matrix A | type | value |
      | 1.0      | <=   | 1.0   |
    And The objective strategy is minimizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X | solutionCost |
      | 1.0      | -2.0         |

  Scenario: C- LEQ-Minimization1VariableTest
    Given The linear model have 1 variables.
    And The linear cost values are:
      | vector C |
      | -0.5     |
    And The linear constraints are:
      | matrix A | type | value |
      | 1.0      | <=   | 1.0   |
    And The objective strategy is minimizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X | solutionCost |
      | 1.0      | -0.5         |

  Scenario: D- LEQ-Minimization1VariableTest
    Given The linear model have 1 variables.
    And The linear cost values are:
      | vector C |
      | -1.0     |
    And The linear constraints are:
      | matrix A | type | value |
      | 1.0      | <=   | 0.0   |
    And The objective strategy is minimizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X | solutionCost |
      | 0.0      | 0.0          |

  Scenario: E- LEQ-Minimization1VariableTest
    Given The linear model have 1 variables.
    And The linear cost values are:
      | vector C |
      | -1.0     |
    And The linear constraints are:
      | matrix A | type | value |
      | 1.0      | <=   | 2.5   |
    And The objective strategy is minimizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X | solutionCost |
      | 2.5      | -2.5         |

  Scenario: F- LEQ-Minimization1VariableTest
    Given The linear model have 1 variables.
    And The linear cost values are:
      | vector C |
      | 1.0      |
    And The linear constraints are:
      | matrix A | type | value |
      | 1.0      | <=   | 1.0   |
    And The objective strategy is minimizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X | solutionCost |
      | 0.0      | 0.0          |
