@SimplexTest
Feature: Tests basiques d'affectation automatique avec une ressource, deux activités chacune avec un besoin à couvrir durant une journée.

  Scenario: simple instance with 1 variables et 1 constraints.
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

  Scenario: simple instance with 1 variables et 1 constraints.
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

  Scenario: simple instance with 1 variables et 1 constraints.
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

  Scenario: simple instance with 1 variables et 1 constraints.
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

  Scenario: simple instance with 1 variables et 1 constraints.
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

  Scenario: simple instance with 1 variables et 1 constraints.
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
