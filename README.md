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
