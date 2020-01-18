# cessda.cmv.core
CESSDA Metadata Validator Core. Contains business logic of all use cases.

## Contents

* PM
	* [Release Planning](https://bitbucket.org/cessda/cessda.cmv.core/src/master/site/docs/release-planning.md)
* OOA
	* [System context map](https://bitbucket.org/cessda/cessda.cmv.core/src/master/site/uml/system-context-map.jpg)
	* [Components overview](https://bitbucket.org/cessda/cessda.cmv.core/src/master/site/uml/architecture-layers.jpg)
	* [Target conflicts](https://bitbucket.org/cessda/cessda.cmv.core/src/master/site/uml/ooa-target-conflict.jpg)
* OOD
	* [Component cessda.cmv.core](https://bitbucket.org/cessda/cessda.cmv.core/src/master/site/uml/component-eu.cessda.cmv.core.jpg)
* OOI
	* [Definition XPathUsedConstraint](https://bitbucket.org/cessda/cessda.cmv.core/src/master/site/uml/definition-XpathUsedConstraint.jpg)


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
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```