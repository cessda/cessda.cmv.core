# Release Planning

# Milestone M1

* Slogan: Proof concept driven by (coded )tests
    * Build ubiquitous language (UL)
	* Focus on extendable design (ED)
	* Reduce contract implementations to minimum
* Deadline: 2020/03/31

|                                               | Issues      | AM | CPK | DB | CT |
|-----------------------------------------------|-------------|:--:|:---:|:--:|:--:|
| Missing profiles created, no ddi extension!   |             | I  |  I  | R  |  A |
| **Minimal constraint set defined: Only 1!**   | #2, #29     | R  |  A  | C  |  A |
| OOD for `cmv.core` finished                   |             | R  |  A  | C  |  C |
| OOI for `cmv.core` finished 1)                |             | R  |  A  | I  |  I |
| Java component `cmv.core` is usable           |             | R  |  A  | C  |  C |
| OOA in `cmv.server` finished                  | #20         | R  |  A  | C  |  A |

1) Minimal constraint set implemented against test cases: every constraint with one bad and one good case

# Milestone M2: Minimal Viable Product

* Slogan: Get the product out
    * Consolidate UL and ED
    * Avoid new features!
    * User must have value when using product!
* Deadline: 2020/06/30

|                                            | Issues | AM | CPK | DB | CT |
|--------------------------------------------|--------|:--:|:---:|:--:|:--:|
| Bugs in profiles fixed                     |        |  I |  I  |  R |  A |
| OOD and OOI in `cmv.core` **refactored!**  |        |  R |  A  |  I |  I |
| Bugs in `cmv.core` fixed                   |        |  R |  A  |  I |  I |
| OOD in `cmv.server` finished               |        |  R |  C  |  C |  C |
| OOI in `cmv.server` finished               |        |  R |  A  |  I |  I |
| Server `cmv.server` is usable -> **MVP!**  |        |  R |  A  |  C |  A |

# Milestone M3: Minimal Viable Service

* Deadline: 2020/09/31

|                                            | Issues | AM | CPK | DB | CT |
|--------------------------------------------|--------|:--:|:---:|:--:|:--:|
| Service `cmv.cessda.eu` is usable          |        |  I |  I  |  I |  R |

# Legend

## RACI Matrix
|     | Description        |
|-----|--------------------|
| R   | Responsible person |
| A   | Accountable person |
| C   | Consulted person   |
| I   | Informed person    |


## Team members
|     | Full names         |
|-----|--------------------|
| AM  | Alex Muehlbauer     |
| CPK | Claus-Peter Klas   |
| DB  | Darren Bell        |
| CT  | Carsten Thiel      |
