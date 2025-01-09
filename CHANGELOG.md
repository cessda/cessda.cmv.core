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

## [4.0.0] - 2025-01-09

[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.14620708.svg)](https://doi.org/10.5281/zenodo.14620708)

### Migration Notes

Due to changes in XML namespace support previous DDI profiles will validate differently. For correct validation, use the following profiles:

* CDC DDI 2.5 profiles - use version 3.0.0 or newer
* CDC DDI 2.6 profiles - use version 2.0.0 or newer
* CDC DDI 3.2 profile - use version 2.0.0 or newer
* CDC DDI 3.3 profile - use version 2.0.0 or newer
* EQB DDI 2.5 profile - use version 1.0.0 or newer

Built-in profiles have been updated accordingly.

### Breaking Changes

Before prefixes were not treated separately from the rest of the element's name. Now prefixes are mapped onto namespaces using the mappings defined in the profile.

Prefix-to-namespace mapping is extracted from the DDI profile using `XMLPrefixMap` elements.

```xml
<pr:DDIProfile xmlns:pr="ddi:ddiprofile:3_2">
...
    <pr:XMLPrefixMap>
        <pr:XMLPrefix>ddi</pr:XMLPrefix>
        <pr:XMLNamespace>ddi:codebook:2_5</pr:XMLNamespace>
    </pr:XMLPrefixMap>
...
</pr:DDIProfile>
```

Referencing elements in XPaths that don't have a namespace prefix (e.g. `/element`) always refers to the empty namespace "". Attempting to map such a prefix will have no effect. This is a limitation of the XPath 1.0 specification.

The JAXBProfile serialisations and corresponding schemas have been updated.

```xml
<Profile xmlns="cmv:profile:v0">
...
	<PrefixMaps>
		<PrefixMap>
			<Prefix>ddi</Prefix>
			<Namespace>ddi:codebook:2_5</Namespace>
		</PrefixMap>
		<PrefixMap>
			<Prefix>xsi</Prefix>
			<Namespace>http://www.w3.org/2001/XMLSchema-instance</Namespace>
		</PrefixMap>
	</PrefixMaps>
...
</Profile>
```

### Added

* Added a NamespaceContext implementation for mapping prefixes to XML namespaces

## [3.0.0] - 2024-04-30

[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.11083094.svg)](https://doi.org/10.5281/zenodo.11083094)

### Migration Notes

Because `org.gesis.commons.resource.Resource` can no longer be used as a source for `Document` and `Profile` instances, uses of these must be migrated to use another type.

Applications must provide their own SLF4J implementation for logging to work properly. A warning message will be printed if no SLF4J implementation is found.

### Added

* Added a public `Node` interface
* Added the ability to cached parsed `Document` and `Profile` instances by creating `CessdaMetadataValidatorFactory.newDocument()` and `CessdaMetadataValidatorFactory.newProfile()` for common source type
* Added implementations for `equals()` and `hashCode()` for profiles and all constraints
* Enabled tests for mediatype classes (`ValidationRequest` and `ValidationReport`) to ensure that the JSON and XML representations are generated and parsed correctly
* Added `Profile.getProfileName()` and `Profile.getProfileVersion()` so that the name and version of a profile can be programatically retrieved

### Changed

* Refactored `ConstraintViolation.getMessage()` to only return the message without location information
	* The previous behaviour has been moved to `ConstraintViolation.toString()`
* Renamed the `Node` class to `NodeImpl`
* Optimise parsing documents that are wrapped in an OAI-PMH wrapper by avoiding serializing and parsing the contents of the `<metadata>` element

### Fixed

* Fixed missing constraints when using `DomSemiStructuredDdiProfile.toJaxbProfile()`
* Fixed incomplete parsing of `eu.cessda.cmv.core.mediatype.profile.Profile`
* Fixed `DomProfile` missing constraints

### Removed

* Removed the ability to use `org.gesis.commons.resource.Resource` as a source for `Document` and `Profile` instances
* Removed `ProfileResourceLabelProvider`

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

[4.0.0]: https://github.com/cessda/cessda.cmv.core/releases/tag/4.0.0
[3.0.0]: https://github.com/cessda/cessda.cmv.core/releases/tag/3.0.0
[2.0.0]: https://github.com/cessda/cessda.cmv.core/releases/tag/2.0.0
[1.1.0]: https://github.com/cessda/cessda.cmv.core/releases/tag/1.1.0
[1.0.0]: https://github.com/cessda/cessda.cmv.core/releases/tag/v1.0.0
[0.4.2]: https://github.com/cessda/cessda.cmv.core/releases/tag/v0.4.2
[0.4.1]: https://github.com/cessda/cessda.cmv.core/releases/tag/v0.4.1
[0.4.0]: https://github.com/cessda/cessda.cmv.core/releases/tag/v0.4.0
[0.3.1]: https://github.com/cessda/cessda.cmv.core/releases/tag/v0.3.1
[0.3.0]: https://github.com/cessda/cessda.cmv.core/releases/tag/v0.3.0
[0.2.0]: https://github.com/cessda/cessda.cmv.core/releases/tag/v0.2.0
