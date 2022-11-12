@SimplexTest
Feature: Simplex1VariableBasicTest

  Scenario: A- Simplex1VariableBasicTest
    Given I have 1 variables and 1 constraints.
    And The cost values are:
      | vector C |
      | 1.0      |
    And The constraints are:
      | matrix A | maximumValue B |
      | 1.0      | 1.0            |
    And The Bland column selection strategy.
    When I execute Simplex method.
    Then Optimal solution is found.
    And Found solution is:
      | vector X | solutionCost |
      | 1.0      | 1.0          |

  Scenario: B- Simplex1VariableBasicTest
    Given I have 1 variables and 1 constraints.
    And The cost values are:
      | vector C |
      | 2.0      |
    And The constraints are:
      | matrix A | maximumValue B |
      | 1.0      | 1.0            |
    And The Bland column selection strategy.
    When I execute Simplex method.
    Then Optimal solution is found.
    And Found solution is:
      | vector X | solutionCost |
      | 1.0      | 2.0          |

  Scenario: C- Simplex1VariableBasicTest
    Given I have 1 variables and 1 constraints.
    And The cost values are:
      | vector C |
      | 0.5      |
    And The constraints are:
      | matrix A | maximumValue B |
      | 1.0      | 1.0            |
    And The Bland column selection strategy.
    When I execute Simplex method.
    Then Optimal solution is found.
    And Found solution is:
      | vector X | solutionCost |
      | 1.0      | 0.5          |

  Scenario: D- Simplex1VariableBasicTest
    Given I have 1 variables and 1 constraints.
    And The cost values are:
      | vector C |
      | 1.0      |
    And The constraints are:
      | matrix A | maximumValue B |
      | 1.0      | 0.0            |
    And The Bland column selection strategy.
    When I execute Simplex method.
    Then Optimal solution is found.
    And Found solution is:
      | vector X | solutionCost |
      | 0.0      | 0.0          |

  Scenario: E- Simplex1VariableBasicTest
    Given I have 1 variables and 1 constraints.
    And The cost values are:
      | vector C |
      | 1.0      |
    And The constraints are:
      | matrix A | maximumValue B |
      | 1.0      | 2.5          |
    And The Bland column selection strategy.
    When I execute Simplex method.
    Then Optimal solution is found.
    And Found solution is:
      | vector X | solutionCost |
      | 2.5      | 2.5          |

  Scenario: F- Simplex1VariableBasicTest
    Given I have 1 variables and 1 constraints.
    And The cost values are:
      | vector C |
      | -1.0      |
    And The constraints are:
      | matrix A | maximumValue B |
      | 1.0      | 1.0            |
    And The Bland column selection strategy.
    When I execute Simplex method.
    Then Optimal solution is found.
    And Found solution is:
      | vector X | solutionCost |
      | 0.0      | 0.0          |
