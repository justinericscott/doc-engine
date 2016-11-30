# Document Engine #

**This is a personal project to help myself sharpen my existing and learn entirely new skills**

### Core Technologies Used ###
- [Spring Boot](https://projects.spring.io/spring-boot/) (MVC, REST, CDI, Servlet, Transaction Management)
- [Hibernate](http://hibernate.org/orm/) (ORM & JPA)
- [MySQL Connector](https://www.mysql.com/products/community/) (Traditional Relational Database)
- [HyperSQL](http://hsqldb.org/) (100% Java In-Memeory Relational Database)
- [Drools](https://www.drools.org/) (Logic, Descision Tables, Rules Engine)
- [Freemarker](http://freemarker.org/) (Template Engine, Token Replacement)
- [iText](http://itextpdf.com/) (PDF Generator)
- [Apache POI](https://poi.apache.org/) (Microsoft Document API)

### What's provided ###
#### Content Repository and Service ####
- Repository provides access to the database table _CONTENT_.
- Service provides null checking and exception handling using the repository. 
- All unchecked expressions are caught and thrown into the logger
- Content table contains all information relating to templates and/or static content.

#### Advanced Content Repository and Service ####

### How to use the Document Engine library: ###
#### Stateless Server ####
By default, the engine can be started as a stateless server using HyperSQL with empty entity tables created at startup.

- DocEngine.run() to start the engine.
- DocEngine.running() to determine if the engine is running.
- DocEngine.stop() to stop the engine.

#### Stateful Server ####
The default settings can be overridden and can be started as a stateful server using MySQL with entity tables created or verified at startup.

- Create a folder on the file system of the server, the name is unimportant.
- Create a system variable named DOC_ENGINE_HOME and enter the path of the newly created folder for the value
- Create a file named custom.properties
- Follow the guidlines at the [Spring Boot Reference](http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)
- Refer to default.properties for examples.
- The properties that cannot be overridden are in application.properties.
- DocEngine.run() to start the engine.
- DocEngine.running() to determine if the engine is running.
- DocEngine.stop() to stop the engine.
