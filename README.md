# CESSDA Metadata Validator Core

[![SQA badge](https://api.eu.badgr.io/public/assertions/FXsqkIeLTIGeSB8-1HX9PQ/image)](https://api.eu.badgr.io/public/badges/IDUsZJpNQPuiJpPwmJ8iJw)

[![Build Status](https://jenkins.cessda.eu/buildStatus/icon?job=cessda.cmv.core%2Fmaster)](https://jenkins.cessda.eu/job/cessda.cmv.core/job/master/)
[![Quality Gate Status](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=alert_status)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Coverage](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=coverage)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Code Smells](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=code_smells)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Technical Debt](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=sqale_index)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Security Rating](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=security_rating)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Vulnerabilities](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=vulnerabilities)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Bugs](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=bugs)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)

This repository holds all core domain logic about CESSDA Metadata Validator project.

## Prerequisites

The following SDKs must be installed before the project can be used.

* Java Development Kit (JDK) 8 (or later)

## Usage

This is a short snippet showing how to use the validator.

```java
void validateUsingFiles()
{
	CessdaMetadataValidatorFactory factory = new CessdaMetadataValidatorFactory();
	ValidationService.V10 validationService = factory.newValidationService();
	Resource document = Resource.newResource( new File( "path/to/ddi-document.xml" ) );
	Resource profile = Resource.newResource( new File( "path/to/ddi-profile.xml" ) );
	ValidationReportV0 validationReport = validationService.validate( document, profile, BASIC );
	boolean isValid = validationReport.getConstraintViolations().isEmpty();
	validationReport.getConstraintViolations().forEach( cv -> System.out.println( cv.getMessage() ) );
}
```

For more detailed information, consult the JavaDoc.

## Dependency Information

Add CESSDA Maven Repository to your `pom.xml`:

```xml
<repositories>
	<repository>
		<id>cessda-nexus</id>
		<url>https://nexus.cessda.eu/repository/maven-releases</url>
	</repository>
</repositories>
```

For snapshots add:

```xml
<repositories>
	<repository>
		<id>cessda-nexus-snapshots</id>
		<url>https://nexus.cessda.eu/repository/maven-snapshots</url>
	</repository>
</repositories>
```

Add the dependency to your `pom.xml`:

```xml
<dependency>
	<groupId>eu.cessda.cmv</groupId>
	<artifactId>cmv-core</artifactId>
	<version>1.0.0</version>
</dependency>
```

### Getting started as developer

```shell
# Pull repository and change directory
git clone https://github.com/cessda/cessda.cmv.core.git
cd cessda.cmv.core

# Execute tests
./mvnw clean test
```

## Contributing

Please read [CONTRIBUTING](CONTRIBUTING.md) for details on our code of conduct,
and the process for submitting pull requests to us.

## Versioning

See [Semantic Versioning](https://semver.org/) for guidance.

## Changes

You can find the list of changes made in each release in the
[CHANGELOG](CHANGELOG.md) file.

## License

See the [LICENSE](LICENSE.txt) file.

## Citing

See the [CITATION](CITATION.cff) file.
