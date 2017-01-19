package br.jus.treto.extrator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import oracle.jdbc.OracleConnection;

public class App {
	private OracleConnection connection;

	public static void main(String[] args) {

		System.out.println("-------------------------------------------------------");
		System.out.println(" Utilitário de extração de dados");
		System.out.println("-------------------------------------------------------");
		
		if (args.length != 1 && args.length != 7) {
			System.out.println("Faltam parâmetros");
			System.out.println("Usos possíveis: ");
			System.out.println("1> java -jar <arquivo>.jar <arquivo_com_configuracoes>");
			System.out.println(
					"2> java -jar <arquivo>.jar <host> <SID> <usuario> <senha> <tabela> <coluna> <arquivo_destino>");
			System.out.println(
					"Exemplo: java -jar extrator.jar maquina1 xe usuario1 senha123 produtos preco /tmp/relatorio.xml");
			System.out.printf("\nO arquivo com as configurações deve estar no formato 'chave=valor', como a seguir:\n");
			System.out.println("host=maquina1");
			System.out.println("sid=xe");
			System.out.println("usuario=usuario1");
			System.out.println("senha=senha123");
			System.out.println("tabela=produtos");
			System.out.println("coluna=preco");
			System.out.println("arquivo=/tmp/resultado.txt");
			return;
		}

		Properties props;
		if (args.length == 1) {
			props = lerArqConfiguracao(args);
		} else {
			props = new Properties();
			props.put("host", args[0]);
			props.put("sid", args[1]);
			props.put("usuario", args[2]);
			props.put("senha", args[3]);
			props.put("tabela", args[4]);
			props.put("coluna", args[5]);
			props.put("arquivo", args[6]);
		}

		lerDadosEEscreverArq(props);
	}

	private static void lerDadosEEscreverArq(Properties props) {
		App acesso = new App();

		try {
			String sql = "select " + props.getProperty("coluna") + " from " + props.getProperty("tabela");
			ResultSet rs = acesso.executeQuery(
					sql,
					props.getProperty("host"), 
					props.getProperty("sid"), 
					props.getProperty("usuario"),
					props.getProperty("senha"));
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(props.getProperty("arquivo")));
			writer.write("");
			int i = 0;
			while (rs.next()) {
				System.out.println(i++ + " registros processados.");
				writer.append(rs.getString(props.getProperty("coluna")));
			}
			writer.close();
			acesso.close();
		} catch (Exception e) {
			System.out.println("Ocorreu um erro.");
			e.printStackTrace();
		}
	}

	private static Properties lerArqConfiguracao(String[] args) {
		Properties props = new Properties();
		File arquivo = new File(args[0].toString());
		final String path = arquivo.getAbsolutePath();
		InputStream isDir;
		try {
			isDir = new FileInputStream(path);
			props.load(isDir);
		} catch (FileNotFoundException e) {
			System.err.println("Arquivo não encontrado.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println(
					"Erro ao analisar o arquivo. Por favor, verifique se ele está correto e se ele possui as devidas permissões");
			System.exit(1);
		}
		return props;
	}

	public void close() throws SQLException {
		if (!this.connection.isClosed()) {
			this.connection.close();
		}
	}

	public ResultSet executeQuery(String sql, String host, String sid, String usuario, String senha)
			throws SQLException, ClassNotFoundException {
		Class.forName("oracle.jdbc.OracleDriver");
		this.connection = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@" + host + ":1521:" + sid,
				usuario, senha);

		PreparedStatement statement = (PreparedStatement) this.connection.prepareCall(sql);
		return statement.executeQuery();
	}
}
