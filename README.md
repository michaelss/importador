# Extrator

Este projeto é um utilitário para buscar informações do banco de dados e escrevê-las em um arquivo de texto. A motivação inicial para este projeto foi a extração de milhares de registros do banco de dados para montar um dos relatórios para o CNJ.


## Geração do arquivo .jar

O projeto usa o [Apache Maven](http://maven.apache.org) como gerenciador de build/dependências. Assim, para gerar o arquivo .jar, use um dos comandos abaixo.

``# mvn clean package``

ou 

``# mvn clean install``

O segundo, além de gerar o .jar no diretório target, também o copiará para o seu repositório local do Maven, localizado em ~/.m2/repository/

## Execução

``java -jar extrator.jar <parametros>``
