package br.jus.treto.extrator;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import oracle.jdbc.OracleConnection;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class Main {

    private OracleConnection connection;

    public static void main(String[] args) {

        if (args.length < 7) {
            System.out.println("Faltam parâmetros");
            System.out.println("Uso: comando <host> <SID> <usuario> <senha> <tabela> <coluna> <arquivo_destino>");
            System.out.println("Exemplo: comando to1 adm usuario1 senha123 SELO_CNJ_BAIXADOS_G1 CONTEUDO_XML /tmp/relatorio.xml");
            return;
        }
        
        String host = args[0];
        String sid = args[1];
        String usuario = args[2];
        String senha = args[3];
        String tabela = args[4];
        String coluna = args[5];
        String arquivo = args[6];

        Main acesso = new Main();

        try {
            ResultSet rs = acesso.executeQuery("select " + coluna + " from " + tabela, host, sid, usuario, senha);
            BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo));
            writer.write("");
            int i = 0;
            while (rs.next()) {
                System.out.println(i++ + " registros processados.");
                writer.append(rs.getString(coluna));
            }
            writer.close();
            acesso.close();
        } catch (Exception e) {
            System.out.println("Ocorreu um erro.");
            e.printStackTrace();
        }
    }
    
    public void close() throws SQLException {
        if (!this.connection.isClosed()) {
            this.connection.close();
        }
    }

    public ResultSet executeQuery(String sql, String host, String sid, String usuario, String senha) throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.OracleDriver");
        this.connection = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@" + host + ":1521:" + sid, usuario, senha);

        PreparedStatement statement = (PreparedStatement) this.connection.prepareCall(sql);
        return statement.executeQuery();
    }
}
