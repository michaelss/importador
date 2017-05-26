package br.jus.treto.importador;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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

	private void lerArquivo(final Properties props, App acesso) {
		final String nomeArquivo = props.getProperty("arquivo");

        try {
            File f = new File(nomeArquivo);
            BufferedReader b = new BufferedReader(new FileReader(f));
            String readLine = "";
            System.out.println("Reading file using Buffered Reader");

            Class.forName("oracle.jdbc.OracleDriver");
            this.connection = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@" + props.getProperty("host") + ":1521:" + props.getProperty("sid"),
                    props.getProperty("usuario"), props.getProperty("senha"));

            final String sql = "INSERT INTO AUTO_ATEN_LOCAIS_VOTA_(AUTO_ATEN_LOCAIS_VOTA_ID, CD_PROCESSO_ELEITORAL, CD_PLEITO, SG_UF, CD_LOCALIDADE_TSE, NM_LOCALIDADE, NR_ZONA, NR_SECAO, ST_SECAO_USA_LOCVOT_TEMP, NR_LOCVOT, NM_LOCVOT, DS_ENDERECO) " +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            int i = 1;
            while ((readLine = b.readLine()) != null) {
                String[] params = readLine.replace("\"", "").split(";");
                System.out.println(i++);
                acesso.executeInsert(sql, params);
            }

            acesso.close();

        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }


	}

	public void close() throws SQLException {
		if (!this.connection.isClosed()) {
			this.connection.close();
		}
	}

    public void executeInsert(String sql, String[] params)
            throws SQLException, ClassNotFoundException {

        PreparedStatement statement = this.connection.prepareStatement(sql);

        for (int i = 0; i < 12; i++) {
            statement.setString(i+1, params[i]);
        }

        statement.executeUpdate();
        statement.close();
    }

}
