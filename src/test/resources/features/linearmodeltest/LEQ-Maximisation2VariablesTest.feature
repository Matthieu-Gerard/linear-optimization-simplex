@LinearModelTest
Feature: LEQ-Maximisation2VariablesTest

  Scenario: A- LEQ-Maximisation2VariablesTest
    Given The linear model have 2 variables.
    And The linear cost values are:
      | vector C   |
      | 1.0 ,  1.0 |
    And The linear constraints are:
      | matrix A   | type | value |
      | 1.0 ,  0.0 | <=   | 1.0   |
      | 0.0 ,  1.0 | <=   | 2.0   |
    And The objective strategy is maximizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X  | solutionCost |
      | 1.0 , 2.0 | 3.0          |

  Scenario: B- LEQ-Maximisation2VariablesTest
    Given The linear model have 2 variables.
    And The linear cost values are:
      | vector C   |
      | 1.0 ,  1.0 |
    And The linear constraints are:
      | matrix A   | type | value |
      | 1.0 ,  0.0 | <=   | 2.0   |
      | 0.0 ,  1.0 | <=   | 1.0   |
    And The objective strategy is maximizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X  | solutionCost |
      | 2.0 , 1.0 | 3.0          |

  Scenario: C- LEQ-Maximisation2VariablesTest
    Given The linear model have 2 variables.
    And The linear cost values are:
      | vector C    |
      | 1.0 ,  -1.0 |
    And The linear constraints are:
      | matrix A   | type | value |
      | 1.0 ,  0.0 | <=   | 1.0   |
      | 0.0 ,  1.0 | <=   | 2.0   |
    And The objective strategy is maximizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X  | solutionCost |
      | 1.0 , 0.0 | 1.0          |

  Scenario: D- LEQ-Maximisation2VariablesTest
    Given The linear model have 2 variables.
    And The linear cost values are:
      | vector C    |
      | -1.0 ,  1.0 |
    And The linear constraints are:
      | matrix A   | type | value |
      | 1.0 ,  0.0 | <=   | 2.0   |
      | 0.0 ,  1.0 | <=   | 1.0   |
    And The objective strategy is maximizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X  | solutionCost |
      | 0.0 , 1.0 | 1.0          |

  Scenario: E- LEQ-Maximisation2VariablesTest
    Given The linear model have 2 variables.
    And The linear cost values are:
      | vector C   |
      | 1.0 ,  0.0 |
    And The linear constraints are:
      | matrix A    | type | value |
      | 1.0 ,  -1.0 | <=   | 0.0   |
      | 0.0 ,  1.0  | <=   | 1.0   |
    And The objective strategy is maximizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X  | solutionCost |
      | 1.0 , 1.0 | 1.0          |

  Scenario: F- LEQ-Maximisation2VariablesTest
    Given The linear model have 2 variables.
    And The linear cost values are:
      | vector C    |
      | -1.0 ,  0.0 |
    And The linear constraints are:
      | matrix A    | type | value |
      | 1.0 ,  -1.0 | <=   | 0.0   |
      | 0.0 ,  1.0  | <=   | 1.0   |
    And The objective strategy is maximizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X  | solutionCost |
      | 0.0 , 0.0 | 0.0          |

  Scenario: G- LEQ-Maximisation2VariablesTest
    Given The linear model have 2 variables.
    And The linear cost values are:
      | vector C   |
      | 1.0 ,  0.0 |
    And The linear constraints are:
      | matrix A    | type | value |
      | 1.0 ,  -1.0 | <=   | 0.0   |
      | 0.0 ,  1.0  | <=   | 2.0   |
    And The objective strategy is maximizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X  | solutionCost |
      | 2.0 , 2.0 | 2.0          |

  Scenario: H- LEQ-Maximisation2VariablesTest
    Given The linear model have 2 variables.
    And The linear cost values are:
      | vector C   |
      | 1.0 ,  0.0 |
    And The linear constraints are:
      | matrix A    | type | value |
      | 1.0 ,  -1.0 | <=   | 0.0   |
      | 1.0 ,  0.0  | <=   | 2.0   |
    And The objective strategy is maximizing cost function.
    And The column selection is Bland strategy.
    When The linear problem is solved by Simplex solver.
    Then The found solution is optimal.
    And The linear solution is:
      | vector X  | solutionCost |
      | 2.0 , 2.0 | 2.0          |
