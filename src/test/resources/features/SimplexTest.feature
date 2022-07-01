Feature: Tests basiques d'affectation automatique avec une ressource, deux activités chacune avec un besoin à couvrir durant une journée.

  Scenario: cas très simple avec 3 variables et 5 constraints.
    Given I have 3 variables and 5 constraints.
    And The cost values :
      | vector C      |
      | 1.0, 1.0, 1.0 |
    And The constraints :
      | matrix A        | maximumValue B |
      | -1.0,  1.0, 0.0 | 8.0            |
      | 1.0,  4.0,  0.0 | 45.0           |
      | 2.0,  1.0,  0.0 | 27.0           |
      | 3.0, -4.0,  0.0 | 24.0           |
      | 0.0,  0.0,  1.0 | 4.0            |
    When I execute Simplex method.
    Then Optimal solution is found.
    And Found solution is :
      | vector X      | solutionCost |
      | 9.0, 9.0, 4.0 | 22.0         |

  Scenario: cas très simple avec 2 variables et 3 constraints.
    Given I have 2 variables and 3 constraints.
    And The cost values :
      | vector C    |
      | 13.0,  23.0 |
    And The constraints :
      | matrix A   | maximumValue B |
      | 5.0, 15.0  | 480.0          |
      | 4.0,  4.0  | 160.0          |
      | 35.0, 20.0 | 1190.0         |
    When I execute Simplex method.
    Then Optimal solution is found.
    And Found solution is :
      | vector X | solutionCost |
      | 12.0, 28.0 | 800.0        |

  Scenario: problème non borné.
    Given I have 4 variables and 2 constraints.
    And The cost values :
      | vector C              |
      | 2.0, 3.0, -1.0, -12.0 |
    And The constraints :
      | matrix A               | maximumValue B |
      | -2.0, -9.0,  1.0,  9.0 | 3.0            |
      | 1.0,  1.0, -1.0, -2.0  | 2.0            |
    When I execute Simplex method.
    Then Linear problem is unbounded.

  Scenario: degenerate test with 4 variables and 3 constraints - cycles if you choose most positive objective function coefficient.
    Given I have 4 variables and 3 constraints.
    And The cost values :
      | vector C                 |
      | 10.0, -57.0, -9.0, -24.0 |
    And The constraints :
      | matrix A             | maximumValue B |
      | 0.5, -5.5, -2.5, 9.0 | 0.0            |
      | 0.5, -1.5, -0.5, 1.0 | 0.0            |
      | 1.0,  0.0,  0.0, 0.0 | 1.0            |
    When I execute Simplex method.
    Then Optimal solution is found.
    And Found solution is :
      | vector X           | solutionCost |
      | 9.0, 9.0, 4.0, 4.0 | 1.0          |

  Scenario: test with 2 variables and 2 constraints and negative coefficients.
    Given I have 2 variables and 2 constraints.
    And The cost values :
      | vector C  |
      | 1.0 , 4.0 |
    And The constraints :
      | matrix A  | maximumValue B |
      | 1.0 , 1.0 | 1.0            |
      | -1.0, 0.0 | -1.0           |
    When I execute Simplex method.
    Then Optimal solution is found.
    And Found solution is :
      | vector X  | solutionCost |
      | 1.0 , 0.0 | 1.0          |
