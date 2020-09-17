# Changelog
All notable changes to the CESSDA Metadata Validator Core component will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

*For each release, use the following sub-sections:*  
*- Added (for new features)*  
*- Changed (for changes in existing functionality)*  
*- Deprecated (for soon-to-be removed features)*  
*- Removed (for now removed features)*  
*- Fixed (for any bug fixes)*  
*- Security (in case of vulnerabilities)*

## [0.3.0] - 2020-09-17

### Added
- Finalized FixedValueNodeConstraint (#13)
- Finalized DescriptiveTermOfControlledVocabularyConstraint (#27)

### Changed
- Included CDC profiles v1.0 
- Upgrade to [org.gesis.commons:commons-xml:5.6.0](https://git.gesis.org/java-commons/commons-xml/tree/v5.6.0)
- Change constraint assignments to validation gates (#70)

### Fixed
- Evaluate XPaths with default namespace (#62)

## [0.2.0] - 07-07-2020

### Added
- Finalized MinimumElementOccuranceValidator
- Finalized CodeValueOfControlledVocabularyConstraint
- Finalized MandatoryNodeIfParentPresent
- Finalized RecommendedNodeConstraint
- Finalized MandatoryNodeConstraint
- Finalized OptionalNodeConstraint
- Added Code of Conduct, Changelog ([#54](https://bitbucket.org/cessda/cessda.cmv.core/issues/54))
- Added badges to REAMDME ([#53](https://bitbucket.org/cessda/cessda.cmv.core/issues/53))


[0.3.0]: https://bitbucket.org/cessda/cessda.cmv.core/src/v0.3.0
[0.2.0]: https://bitbucket.org/cessda/cessda.cmv.core/src/v0.2.0
