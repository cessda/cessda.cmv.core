# Changelog

All notable changes to the CESSDA Metadata Validator Core component will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

*For each release, use the following sub-sections:*

- *Added (for new features)*
- *Changed (for changes in existing functionality)*
- *Deprecated (for soon-to-be removed features)*
- *Removed (for now removed features)*
- *Fixed (for any bug fixes)*
- *Security (in case of vulnerabilities)*

## [2.0.0] - 2023-10-17

[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.10013270.svg)](https://doi.org/10.5281/zenodo.10013270)

### Added

- Added semantic `equals()` and `hashcode()` methods to MediaType objects 

### Changed

- `InMemoryAnalysisUnit10ControlledVocabularyRepository` and `EmptyControlledVocabularyRepository` have been refactored to be singletons
- `CessdaControlledVocabularyRepositoryV2` now uses Java URL HTTP methods, removing a dependency on Spring's HTTP client
- Many instances of unsafe typecasting have been removed
- The spelling of `MinimumElementOccurrenceValidator` has been corrected

### Removed

- Deprecated public constructors for the various validation gates are now package-private. Accessing the gates from the enum is the only way for API consumers to retrieve them.
- Empty interfaces have been removed, as an interface with no methods isn't particularly useful. The removed interfaces are listed below.
  - `Constraint.V10`
  - `Document.V10`
  - `ValidationReport`
  - `ValidationRequest`
- `Interface.Version` interfaces have been renamed to `Interface`. Default methods are the recommended method for extending interfaces. The renamed interfaces are listed below.
  - `Constraint.V20` → `Constraint`
  - `ControlledVocabularyRepository.V11` → `ControlledVocabularyRepository`
  - `Document.V11` → `Document`
  - `Validator.V10` → `Validator`
  - `Profile.V10` → `Profile`
- Removed redundant class `ControlledVocabularyRepositoryProxy`

## [1.1.0] - 2023-05-23

[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.7961900.svg)](https://doi.org/10.5281/zenodo.7961900)

### Added

- Add an API to allow users to define custom validation
  gates ([PR-121](https://github.com/cessda/cessda.cmv.core/pull/121))

## [1.0.0] - 2023-01-24

[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.545261.svg)](https://doi.org/10.5281/zenodo.545261)

### Added

- Add the Maven wrapper to the repository
  ([#98](https://github.com/cessda/cessda.cmv.core/issues/98))

### Changed

- Replace references to Bitbucket
  ([#107](https://github.com/cessda/cessda.cmv.core/issues/107))
- Improve SQAaaS score
  ([#106](https://github.com/cessda/cessda.cmv.core/issues/106))
- General code refactor
  ([#86](https://github.com/cessda/cessda.cmv.core/issues/86))
- Update the readme to include the GitHub source URL
  ([#97](https://github.com/cessda/cessda.cmv.core/issues/97))

### Fixed

- Fix tests failing on Windows due to differences in platform string conventions
  ([#85](https://github.com/cessda/cessda.cmv.core/issues/85))

## [0.4.2] - 2021-10-05

### Changed

- Updated third-party dependencies

### Fixed

- CessdaControlledVocabularyRepositoryV2 - now returns cause of the
  IllegalArgumentException if an error occurs
  ([#82](https://github.com/cessda/cessda.cmv.core/issues/82))

## [0.4.1] - 2021-04-15

### Changed

- Update third-party dependencies

## [0.4.0] - 2021-04-12

[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.4680640.svg)](https://doi.org/10.5281/zenodo.4680640)

### Added

- Provide ValidationRequest representation
  ([#73](https://github.com/cessda/cessda.cmv.core/issues/73))

### Changed

- Adapt to CVS API V2
  ([#70](https://github.com/cessda/cessda.cmv.core/issues/70))
- By CESSDA requested repairs
  ([#68](https://github.com/cessda/cessda.cmv.core/issues/),
  [#69](https://github.com/cessda/cessda.cmv.core/issues/))
- Blank xml tags are shown as constraint violations
  (e.g. `collDate`, `distDate`)
  ([#66](https://github.com/cessda/cessda.cmv.core/issues/))
- Update CDC profiles to version 1.0.4
  ([#75](https://github.com/cessda/cessda.cmv.core/issues/))

## [0.3.1] - 2020-09-23

### Fixed

- `org.gesis.commons.xml.ddi.DdiInputStream`
  corrupts UTF-16LE encoded xml documents

## [0.3.0] - 2020-09-17

### Added

- Finalized FixedValueNodeConstraint
  ([#13](https://github.com/cessda/cessda.cmv.core/issues/))
- Finalized DescriptiveTermOfControlledVocabularyConstraint
  ([#27](https://github.com/cessda/cessda.cmv.core/issues/))

### Changed

- Included CDC profiles v1.0
- Upgrade to
  [`org.gesis.commons:commons-xml:5.6.0`](https://git.gesis.org/java-commons/commons-xml/tree/v5.6.0)
- Change constraint assignments to validation gates
  ([#70](https://github.com/cessda/cessda.cmv.core/issues/70))

### Fixed

- Evaluate XPaths with default namespace
  ([#62](https://github.com/cessda/cessda.cmv.core/issues/62))

## [0.2.0] - 07-07-2020

### Added

- Finalized MinimumElementOccuranceValidator
- Finalized CodeValueOfControlledVocabularyConstraint
- Finalized MandatoryNodeIfParentPresent
- Finalized RecommendedNodeConstraint
- Finalized MandatoryNodeConstraint
- Finalized OptionalNodeConstraint
- Added Code of Conduct, Changelog
  ([#54](https://github.com/cessda/cessda.cmv.core/issues/54))
- Added badges to REAMDME
  ([#53](https://github.com/cessda/cessda.cmv.core/issues/53))

[2.0.0]: https://github.com/cessda/cessda.cmv.core/releases/tag/2.0.0
[1.1.0]: https://github.com/cessda/cessda.cmv.core/releases/tag/1.1.0
[1.0.0]: https://github.com/cessda/cessda.cmv.core/releases/tag/v1.0.0
[0.4.2]: https://github.com/cessda/cessda.cmv.core/releases/tag/v0.4.2
[0.4.1]: https://github.com/cessda/cessda.cmv.core/releases/tag/v0.4.1
[0.4.0]: https://github.com/cessda/cessda.cmv.core/releases/tag/v0.4.0
[0.3.1]: https://github.com/cessda/cessda.cmv.core/releases/tag/v0.3.1
[0.3.0]: https://github.com/cessda/cessda.cmv.core/releases/tag/v0.3.0
[0.2.0]: https://github.com/cessda/cessda.cmv.core/releases/tag/v0.2.0
