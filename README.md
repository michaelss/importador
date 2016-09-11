# Extrator

Este projeto é um utilitário para buscar informações do banco de dados e escrevê-las em um arquivo de texto. A motivação inicial para este projeto foi a extração de milhares de registros do banco de dados para montar um dos relatórios para o CNJ.


## Geração do arquivo .jar

O projeto não usa nenhum gerenciador de build/dependências. Assim, a geração do .jar final é feita com as linhas abaixo.

``javac -cp lib/ojdbc6.jar br/jus/treto/extrator/Main.java``

``jar cvfm extrator.jar Manifest.txt br/jus/treto/extrator/Main.class lib``

## Criação do Manifest

Basta adicionar o caminho da classe principal e das libs que estarão no classpath (no caso, apenas a ojdbc6.jar).

## Execução

``java -jar extrator.jar <parametros>``
