[![Build Status](https://jenkins.cessda.eu/buildStatus/icon?job=cessda.cdc.osmh-indexer.cmm%2Fmaster)](https://jenkins.cessda.eu/job/cessda.cmv.core/job/master/)
[![Bugs](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=bugs)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Code Smells](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=code_smells)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Coverage](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=coverage)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Duplicated Lines (%)](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=duplicated_lines_density)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Lines of Code](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=ncloc)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Maintainability Rating](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=sqale_rating)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Quality Gate Status](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=alert_status)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Reliability Rating](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=reliability_rating)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Security Rating](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=security_rating)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Technical Debt](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=sqale_index)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Vulnerabilities](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=vulnerabilities)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)


# cessda.cmv.core - CESSDA Metadata Validator Core

## Contents

* PM
	* [Release Planning](site/docs/release-planning.md)
* OOA
	* [System context map](site/uml/system-context-map.jpg)
	* [Components overview](site/uml/architecture-layers.jpg)
	* [Target conflicts](site/uml/ooa-target-conflict.jpg)
* OOD
	* [Component cessda.cmv.core](site/uml/component-eu.cessda.cmv.core.jpg)
* OOI
	* [Definition XPathUsedConstraint](site/uml/definition-XpathUsedConstraint.jpg)


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
	<version>0.2.0-SNAPSHOT</version>
</dependency>
```
