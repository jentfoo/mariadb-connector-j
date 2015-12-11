# mariadb-connector-j
<p align="center">
  <a href="http://gulpjs.com">
    <img height="129" width="413" src="http://badges.mariadb.org/logo/Mariadb-seal-shaded-browntext.png">
  </a>
</p>

Fork Differences:
This for was made to fix several issues that exist in the current branch.  Some of them are race conditions which will prevent apps from being able to run for extended periods of time.  Others are race conditions which may exhibit themselves in unexpected behavior.
These fixes were used using the threadly project (<a href='https://github.com/threadly/threadly'>https://github.com/threadly/threadly</a>), so equievelent changes will need to be re-made before we can pull request these changes to the parent project.
This also includes some performance improvements, some of which can be pull requested back, others because of the threadly usage will not be able to be returned to the project.

MariaDB Connector/J is used to connect applications developed in Java to MariaDB and MySQL databases. MariaDB Connector/J is LGPL licensed.

Tracker link <a href="https://mariadb.atlassian.net/projects/CONJ/issues/">https://mariadb.atlassian.net/projects/CONJ/issues/</a>

## Status
[![Build Status](https://travis-ci.org/MariaDB/mariadb-connector-j.svg?branch=master)](https://travis-ci.org/MariaDB/mariadb-connector-j)

## Obtaining the driver
The driver (jar) can be downloaded from [mariadb connector download](https://mariadb.com/products/connectors-plugins)

or maven : 

```script
<dependency>
	<groupId>org.mariadb.jdbc</groupId>
	<artifactId>mariadb-java-client</artifactId>
	<version>1.3.3</version>
</dependency>
```

Development snapshot are available on sonatype nexus repository  
```script
<repositories>
    <repository>
        <id>sonatype-nexus-snapshots</id>
        <name>Sonatype Nexus Snapshots</name>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.mariadb.jdbc</groupId>
        <artifactId>mariadb-java-client</artifactId>
        <version>1.3.4-SNAPSHOT</version>
    </dependency>
</dependencies>
```

## Documentation

For a Getting started guide, API docs, recipes,  etc. see the 
* [About MariaDB connector/J](/documentation/About-MariaDB-Connector-J.md)
* [Use MariaDB connector/j driver](/documentation/Use-MariaDB-Connector-j-driver.md)
* [Failover and high-availability](/documentation/Failover-and-high-availability.md)


## Contributing
To get started with a development installation and learn more about contributing, please follow the instructions at our 
[Developers Guide.](/documentation/Developers-Guide.md)

