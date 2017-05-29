package br.jus.treto.importador;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import oracle.jdbc.OracleConnection;
import org.apache.commons.io.FileUtils;

public class App {

	public static void main(String[] args) throws IOException {

		System.out.println("-------------------------------------------------------");
		System.out.println(" Utilitário de importação de dados");
		System.out.println("-------------------------------------------------------");
		
//		if (args.length != 1 && args.length != 7) {
//			System.out.println("Faltam parâmetros");
//			System.out.println("Usos possíveis: ");
//			System.out.println("1> java -jar <arquivo>.jar <arquivo_com_configuracoes>");
//			System.out.println(
//					"2> java -jar <arquivo>.jar <host> <SID> <usuario> <senha> <tabela> <coluna> <arquivo_destino>");
//			System.out.println(
//					"Exemplo: java -jar extrator.jar maquina1 xe usuario1 senha123 produtos preco /tmp/relatorio.xml");
//			System.out.printf("\nO arquivo com as configurações deve estar no formato 'chave=valor', como a seguir:\n");
//			System.out.println("host=maquina1");
//			System.out.println("sid=xe");
//			System.out.println("usuario=usuario1");
//			System.out.println("senha=senha123");
//			System.out.println("tabela=produtos");
//			System.out.println("coluna=preco");
//			System.out.println("arquivo=/tmp/resultado.txt");
//			return;
//		}

		Properties props;
//		if (args.length == 1) {
//			props = lerArqConfiguracao(args);
//		} else {
			props = new Properties();
			props.put("host", args[0]);
			props.put("sid", args[1]);
			props.put("usuario", args[2]);
			props.put("senha", args[3]);
//			props.put("tabela", args[4]);
//			props.put("coluna", args[5]);
//			props.put("arquivo", args[6]);
//		}

        props.put("arquivo", args[4]);

        App acesso = new App();
		acesso.lerArquivo(props, acesso);
	}

	private void lerArquivo(final Properties props, App acesso) throws IOException {
		final String nomeArquivo = props.getProperty("arquivo");

        final File f = new File(nomeArquivo);
        final List<String> lines = FileUtils.readLines(f, "UTF-8");

        final int numThreads = 50;
        final int sublinhas = lines.size() / numThreads;
        System.out.println("Sublinhas: " + sublinhas);

        System.out.println("Iniciou... " + new Date().toString());

        for (int i = 0; i < numThreads; i++) {

                new Insersor(i+1, props.getProperty("host"),
                        props.getProperty("sid"),
                        props.getProperty("usuario"),
                        props.getProperty("senha"),
                        lines.subList(i * sublinhas, ((i + 1) * sublinhas))).start();

//            System.out.printf("%d - %d \n", i * sublinhas, ((i + 1) * sublinhas));

        }

        int resto = lines.size() % numThreads;

        if (resto != 0) {
//            System.out.printf("%d - %d \n", lines.size() - resto, lines.size());
            new Insersor(numThreads, props.getProperty("host"),
                    props.getProperty("sid"),
                    props.getProperty("usuario"),
                    props.getProperty("senha"),
                    lines.subList(lines.size() - resto, lines.size())).start();
        }
    }

}
