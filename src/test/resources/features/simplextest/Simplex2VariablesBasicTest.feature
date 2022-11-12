@SimplexTest
Feature: Simplex2VariablesBasicTest

  Scenario: A- Simplex2VariablesBasicTest
    Given I have 2 variables and 2 constraints.
    And The cost values are:
      | vector C   |
      | 1.0 ,  1.0 |
    And The constraints are:
      | matrix A   | maximumValue B |
      | 1.0 ,  0.0 | 1.0            |
      | 0.0 ,  1.0 | 2.0            |
    And The Bland column selection strategy.
    When I execute Simplex method.
    Then Optimal solution is found.
    And Found solution is:
      | vector X  | solutionCost |
      | 1.0 , 2.0 | 3.0          |

  Scenario: B- Simplex2VariablesBasicTest
    Given I have 2 variables and 2 constraints.
    And The cost values are:
      | vector C   |
      | 1.0 ,  1.0 |
    And The constraints are:
      | matrix A   | maximumValue B |
      | 1.0 ,  0.0 | 2.0            |
      | 0.0 ,  1.0 | 1.0            |
    And The Bland column selection strategy.
    When I execute Simplex method.
    Then Optimal solution is found.
    And Found solution is:
      | vector X  | solutionCost |
      | 2.0 , 1.0 | 3.0          |

  Scenario: C- Simplex2VariablesBasicTest
    Given I have 2 variables and 2 constraints.
    And The cost values are:
      | vector C    |
      | 1.0 ,  -1.0 |
    And The constraints are:
      | matrix A   | maximumValue B |
      | 1.0 ,  0.0 | 1.0            |
      | 0.0 ,  1.0 | 2.0            |
    And The Bland column selection strategy.
    When I execute Simplex method.
    Then Optimal solution is found.
    And Found solution is:
      | vector X  | solutionCost |
      | 1.0 , 0.0 | 1.0          |

  Scenario: D- Simplex2VariablesBasicTest
    Given I have 2 variables and 2 constraints.
    And The cost values are:
      | vector C    |
      | -1.0 ,  1.0 |
    And The constraints are:
      | matrix A   | maximumValue B |
      | 1.0 ,  0.0 | 2.0            |
      | 0.0 ,  1.0 | 1.0            |
    And The Bland column selection strategy.
    When I execute Simplex method.
    Then Optimal solution is found.
    And Found solution is:
      | vector X  | solutionCost |
      | 0.0 , 1.0 | 1.0          |

  Scenario: E- Simplex2VariablesBasicTest
    Given I have 2 variables and 2 constraints.
    And The cost values are:
      | vector C   |
      | 1.0 ,  0.0 |
    And The constraints are:
      | matrix A    | maximumValue B |
      | 1.0 ,  -1.0 | 0.0            |
      | 0.0 ,  1.0  | 1.0            |
    And The Bland column selection strategy.
    When I execute Simplex method.
    Then Optimal solution is found.
    And Found solution is:
      | vector X  | solutionCost |
      | 1.0 , 1.0 | 1.0          |

        Scenario: F- Simplex2VariablesBasicTest
    Given I have 2 variables and 2 constraints.
    And The cost values are:
      | vector C   |
      | -1.0 ,  0.0 |
    And The constraints are:
      | matrix A    | maximumValue B |
      | 1.0 ,  -1.0 | 0.0            |
      | 0.0 ,  1.0  | 1.0            |
    And The Bland column selection strategy.
    When I execute Simplex method.
    Then Optimal solution is found.
    And Found solution is:
      | vector X  | solutionCost |
      | 0.0 , 0.0 | 0.0          |
      
      
  Scenario: G- Simplex2VariablesBasicTest
    Given I have 2 variables and 2 constraints.
    And The cost values are:
      | vector C   |
      | 1.0 ,  0.0 |
    And The constraints are:
      | matrix A    | maximumValue B |
      | 1.0 ,  -1.0 | 0.0            |
      | 0.0 ,  1.0  | 2.0            |
    And The Bland column selection strategy.
    When I execute Simplex method.
    Then Optimal solution is found.
    And Found solution is:
      | vector X  | solutionCost |
      | 2.0 , 2.0 | 2.0          |
      
	Scenario: H- Simplex2VariablesBasicTest
    Given I have 2 variables and 2 constraints.
    And The cost values are:
      | vector C   |
      | 1.0 ,  0.0 |
    And The constraints are:
      | matrix A    | maximumValue B |
      | 1.0 ,  -1.0 | 0.0            |
      | 1.0 ,  0.0  | 2.0            |
    And The Bland column selection strategy.
    When I execute Simplex method.
    Then Optimal solution is found.
    And Found solution is:
      | vector X  | solutionCost |
      | 2.0 , 2.0 | 2.0          |
      
