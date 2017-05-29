package br.jus.treto.importador;

import oracle.jdbc.OracleConnection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Insersor extends Thread {

    private OracleConnection connection;
    private List<String> linhas;
    private PreparedStatement statement;
    private int threadNum;

    final String sql = "INSERT INTO AUTO_ATEN_LOCAIS_VOTA_(AUTO_ATEN_LOCAIS_VOTA_ID, CD_PROCESSO_ELEITORAL, CD_PLEITO, SG_UF, CD_LOCALIDADE_TSE, NM_LOCALIDADE, NR_ZONA, NR_SECAO, ST_SECAO_USA_LOCVOT_TEMP, NR_LOCVOT, NM_LOCVOT, DS_ENDERECO) " +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public Insersor(int threadNum, String host, String sid, String usuario, String senha, List<String> linhas) {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            this.connection = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@" + host + ":1521:" + sid,
                    usuario, senha);
        }
        catch (Exception e) {}

        this.threadNum = threadNum;
        this.linhas = linhas;
    }

    public Insersor() {}

    public void run() {
        try {
//            int i = 0;
            statement = this.connection.prepareStatement(sql);
            for (String linha : linhas) {
                String[] args = linha.replace("\"", "").split(";");
                this.definirArgumentos(args);
//                System.out.printf("Thread %d, i: %d\n", this.threadNum, i++);
            }

            statement.executeBatch();
            statement.close();

            this.close();
            System.out.println("Finalizou... " + new Date().toString());
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void close() throws SQLException {
        if (!this.connection.isClosed()) {
            this.connection.close();
        }
    }

    private void definirArgumentos(String[] args)
            throws SQLException, ClassNotFoundException {

        for (int i = 0; i < 12; i++) {
            statement.setString(i+1, args[i]);
        }
        statement.addBatch();
//        statement.clearParameters();

    }

}
