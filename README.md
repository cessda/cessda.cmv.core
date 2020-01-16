# cessda.cmv.core
CESSDA Metadata Validator Core. Contains business logic of all use cases.

# Dependency Information

* Add GESIS Maven Repositories to your `pom.xml`:
```xml
<repositories>
	<repository>
		<id>cessda-nexus</id>
		<url>https://nexus.cessda.eu/repository/maven-releases</url>
	</repository>
</repositories>
```  

* Add the dependency to your `pom.xml`:  
```xml
<dependency>
	<groupId>eu.cessda.cmv</groupId>
	<artifactId>cmv-core</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```