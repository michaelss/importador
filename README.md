# Extrator

Este projeto é um utilitário para buscar informações do banco de dados e escrevê-las em um arquivo de texto. A motivação inicial para este projeto foi a extração de milhares de registros do banco de dados para montar um dos relatórios para o CNJ.


## Geração do arquivo .jar

O projeto usa o [Apache Maven](http://maven.apache.org) como gerenciador de build/dependências. 

Como o BD adotado é o Oracle, a dependência para ele foi adicionada no pom.xml. No entanto, tal dependência não está disponível em repositórios públicos. 
Portanto, instale-a no seu repositório local (~/.m2/repository). [Este post mostra como fazer isso](http://michaelss.org/post/138926569132/instalando-lib-no-reposit%C3%B3rio-maven-local). 
Tanto ``groupId`` quanto ``artifactId`` terão o valor “ojdbc”, enquando ``version`` será “14”.

Após isso, para gerar o arquivo .jar, use um dos comandos abaixo.

``# mvn clean package``

ou 

``# mvn clean install``

O segundo, além de gerar o .jar no diretório target, também o copiará para o seu repositório local do Maven, localizado em ~/.m2/repository/

**O nome do arquivo .jar gerado será extrator-<versao>-jar-with-dependencies.jar**

## Execução

``java -jar extrator.jar <parametros>``
