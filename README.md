**When creating Java applications or plugins for other applications, there is usually a lot of repeated code that needs to be re-written over and over.**

**Developer Tools is designed to make working with common objects and technologies much easier without needing to read specifications or spend hours scrolling through Google results.**

_This is early on in its development and requests are welcome. If you have any questions or feedback, feel free to send an email - justin.scott@itgfirm.com._

# Available tools:

**Excel Tools**: Excel is a spreadsheet program created by Microsoft. This tools allows you to read from and write to Excel files. Custom Annotations are available to simply convert your Java objects into spreadsheet rows. 
[HOW TO USE: ExcelWriter, ExcelReader, ExcelUtilities](https://nexus.itgfirm.net/content/sites/itg/dev-tools/excel-tools/apidocs/index.html)

**File Tools**: Working with Files in Java can be tedious to say the least. There are many operations/methods and many of those throw exceptions when something goes wrong causing your program to halt. This tool simplies this operations and puts any problems encountered into the log and a null is returned. This tool also includes a service to act a repository for files.
[HOW TO USE: ](https://nexus.itgfirm.net/content/sites/itg/dev-tools/file-tools/apidocs/index.html)

**JSON Tools**: JSON objects are easy to read and commonly used methods to pass data around. This tool is designed to make getting values or working with entire objects much easier. This tool includes a service to act as a repository for JSON files and string.
[HOW TO USE: ](https://nexus.itgfirm.net/content/sites/itg/dev-tools/json-tools/apidocs/index.html)

**JAXB Tools**: Java and XML Binding (JAXB) or Java and XML Parsing (JAXP) are used to read properly formed XML and XSD files and turn them into Plain Old Java Objects (POJOs). At the present time, you will need to create the Java class yourself and "xpath" the information you're looking for. Upgrades are intended in order to reduce the amount coding required to deal with XML objects (like the ones found in Appian.) This tool also includes a service to act as a repository for XML objects.
[HOW TO USE: ](https://nexus.itgfirm.net/content/sites/itg/dev-tools/jaxb-tools/apidocs/index.html)

**Reflection Tools**: Reflection is commonly used by programs which require the ability to examine or modify the runtime behavior of applications running in the Java virtual machine. This tools is designed to make it easier to work with.
[HOW TO USE: ](https://nexus.itgfirm.net/content/sites/itg/dev-tools/reflection-tools/apidocs/index.html)

**Salesforce Tools**: The Salesforce.com API is already a well-documented and easy to use API. 
Salesforce Tools is designed to handle null checking, performing basic CRUD operations and pushing any exceptions into the logs, a dev-tools design practice. 
The version of the force-partner-api is set in the dev-tools parent, it is currently set to 33.0.0. 
When using the Salesforce SOAP API, the version of the dependency and the version in th WSL address must match.
The dependency declared here can be overridden in the POM declaring this as a dependency. 
[_"Salesforce Developer Site"_](https://developer.salesforce.com/en/)
[HOW TO USE: ](https://nexus.itgfirm.net/content/sites/itg/dev-tools/salesforce-tools/apidocs/index.html)

**Database Tools**: Basic methods to create a valid DataSource.  Will work with JDBC and JNDI.
[_"Trail: JDBC(TM) Database Access"_](https://docs.oracle.com/javase/tutorial/jdbc/)
[_"Java Naming and Directory Interface (JNDI)"_](http://www.oracle.com/technetwork/java/jndi/index.html)

# Tools in devlopment:

**Freemarker Tools**: [_"FreeMarker is a template engine: a Java library to generate text output (HTML web pages, e-mails, configuration files, source code, etc.) based on templates and changing data."_](http://freemarker.org/)

**Tidy Tools**: [_"JTidy is a Java port of HTML Tidy, a HTML syntax checker and pretty printer. Like its non-Java cousin, JTidy can be used as a tool for cleaning up malformed and faulty HTML. In addition, JTidy provides a DOM interface to the document that is being processed, which effectively makes you able to use JTidy as a DOM parser for real-world HTML."_](http://jtidy.sourceforge.net/)

**DOCX4J Tools**: [_"DOCX4J is an open source (ASLv2) Java library for creating and manipulating Microsoft Open XML (Word docx, Powerpoint pptx, and Excel xlsx) files."_](http://www.docx4java.org/trac/docx4j)

**Hibernate Tools**: [_"Hibernate ORM enables developers to more easily write applications whose data outlives the application process. As an Object/Relational Mapping (ORM) framework, Hibernate is concerned with data persistence as it applies to relational databases (via JDBC)." and "In addition to its own "native" API, Hibernate is also an implementation of the Java Persistence API (JPA) specification. As such, it can be easily used in any environment supporting JPA including Java SE applications, Java EE application servers, Enterprise OSGi containers, etc."_](http://hibernate.org/orm/)