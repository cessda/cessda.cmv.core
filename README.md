# cessda.cmv.core
CESSDA Metadata Validator Core. Contains business logic of all use cases.

## Contents


* Use Cases
* Requirements
* [Components Overview](https://bitbucket.org/cessda/cessda.cmv.core/annotate/master/site/uml/architecture-layers.jpg?fileviewer=file-view-default)
* [Component cessda.cmv.core](https://bitbucket.org/cessda/cessda.cmv.core/annotate/master/site/uml/component-eu.cessda.cmv.core.jpg?fileviewer=file-view-default)

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