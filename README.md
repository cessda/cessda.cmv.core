# CESSDA Metadata Validator Core

[![Build Status](https://jenkins.cessda.eu/buildStatus/icon?job=cessda.cdc.osmh-indexer.cmm%2Fmaster)](https://jenkins.cessda.eu/job/cessda.cmv.core/job/master/)
[![Quality Gate Status](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=alert_status)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Coverage](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=coverage)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Code Smells](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=code_smells)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Technical Debt](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=sqale_index)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Security Rating](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=security_rating)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Vulnerabilities](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=vulnerabilities)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)
[![Bugs](https://sonarqube.cessda.eu/api/project_badges/measure?project=eu.cessda.cmv%3Acmv-core&metric=bugs)](https://sonarqube.cessda.eu/dashboard?id=eu.cessda.cmv%3Acmv-core)

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
	<version>0.3.0-SNAPSHOT</version>
</dependency>
```
