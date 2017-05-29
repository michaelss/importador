<<<<<<< HEAD
# Importador

This project is a template to help import large CSV files into Oracle databases. Table e column names must be adjusted to fit specific needs.

## .jar file generation

The project adopts [Apache Maven](http://maven.apache.org) as build/dependencies manager. 

The dependency of ojdbc (Oracle JDBC-compliant driver) was added to pom.xml. However, it is not available on public repositories. So, install it in your local Maven repository (~/.m2/repository). [This post shows how to do it](http://michaelss.org/post/138926569132/instalando-lib-no-reposit%C3%B3rio-maven-local). 

To generate the .jar file, use one of the following commands.

``# mvn clean package``

or 

``# mvn clean install``

The second command will place a copy of the .jar file in your local Maven repository.

**The .jar file name will be importador-<version>-jar-with-dependencies.jar**

## Running

``java -jar importador-<version>-jar-with-dependencies.jar <parameters>``
