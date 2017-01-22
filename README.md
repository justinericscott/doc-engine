#Document Engine Java Prototype
_This is a personal project to help myself sharpen my existing and learn entirely new skills_

##Core Technologies Used
* **[Spring Boot](https://projects.spring.io/spring-boot/)** _(MVC, REST, CDI, servlet, transaction management)_
* **[Hibernate](http://hibernate.org/orm/)** _(ORM & JPA)_
* **[MySQL Connector](https://www.mysql.com/products/community/)** _(Traditional relational database)_
* **[HyperSQL](http://hsqldb.org/)** _(Java-based In-memory relational database)_
* **[Drools](https://www.drools.org/)** _(Logic, decision tables, rules engine)_
* **[Freemarker](http://freemarker.org/)** _(Template engine, token replacement)_
* **[Apache POI](https://poi.apache.org/)** _(Microsoft Office documents manipulator and generator)_
* **[iText](http://itextpdf.com/)** _(PDF manipulator and generator)_

##What's provided
###Content and Instance Repository, Service and Controllers
###Token Dictionary Repository, Service and Controllers
###Business Data Repository, Service and Controllers

##How to configure the Document Engine
###Stateful Server
######The default settings can be overridden to start as a stateful server using an external database with tables created and/or verified at startup.

1. **Create a folder somewhere to house config files and logs.**

  * Example: `/doc-engine`
  
1. **Create a system variable called** `DOC_ENGINE_HOME`**.**

1. **Enter the root directory you just created.**

  * Example: `c:/doc-engine`
  
1. **Create sub folders.**

  * `%DOC_ENGINE_HOME%/config`
  * `%DOC_ENGINE_HOME%/logs`
  
1. **Create the config file.**
  
  * `%DOC_ENGINE_HOME%/config/custom.properties`

1. **You can configure the engine to use an external database or use an internal one.**

  1. <h4>HyperSql Example (default)</h4>
    * Document engine connection (Requires total CRUD-ability):
      ```Ini
      datasource.engine.driverClassName=org.hsqldb.jdbcDriver
      datasource.engine.url=jdbc:hsqldb:mem:docengine;shutdown=false;sql.syntax_ora=true
      datasource.engine.username=SA
      datasource.engine.password= 
      datasource.engine.poolSize=30
      ```
      
    * Business information connection (Can be read-only):
      ```Ini
      datasource.business.driverClassName=org.hsqldb.jdbcDriver
      datasource.business.url=jdbc:hsqldb:mem:datastore;shutdown=false;sql.syntax_ora=true
      datasource.business.username=SA
      datasource.business.password= 
      datasource.business.poolSize=30
      ```
 
  1. <h4>MySQL Example</h4>
    * Document engine connection (Requires total CRUD-ability):
      
      ```Ini
      datasource.engine.driverClassName=com.mysql.jdbc.Driver
      datasource.engine.url=jdbc:mysql://localhost/docengine
      datasource.engine.username=someusername
      datasource.engine.password=somepassword
      datasource.engine.poolSize=30
      ```

1. **Configure SSL.**

  ```Ini
  server.port=443
  server.ssl.enabled=true
  server.ssl.key-store-type=JKS
  server.ssl.key-store=somefileondisk.keystore
  server.ssl.key-store-password=somepassword
  server.ssl.key-alias=somealias
  ```

1. **Follow the guidelines at the <a href="http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html">Spring Boot Reference</a>**

1. **Refer to** `default.properties` **for examples.**

1. **The properties that cannot be overridden are in** `application.properties`**.**

###Stateless Server
######By default, the engine can be started as a stateless server using HyperSQL with empty tables created at startup.

##Operation
```java
DocEngine.run(); // To start the engine.
DocEngine.running(); // To determine if the engine is running.
DocEngine.stop(); // To stop the engine.
```