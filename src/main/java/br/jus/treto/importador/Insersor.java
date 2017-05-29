package br.jus.treto.importador;

import oracle.jdbc.OracleConnection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Insersor extends Thread {

    private OracleConnection connection;
    private List<String> lines;
    private PreparedStatement statement;

    final String sql = "INSERT INTO AUTO_ATEN_LOCAIS_VOTA_(AUTO_ATEN_LOCAIS_VOTA_ID, CD_PROCESSO_ELEITORAL, " +
            "CD_PLEITO, SG_UF, CD_LOCALIDADE_TSE, NM_LOCALIDADE, NR_ZONA, NR_SECAO, ST_SECAO_USA_LOCVOT_TEMP, " +
            "NR_LOCVOT, NM_LOCVOT, DS_ENDERECO) " +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public Insersor(String host, String sid, String user, String password, List<String> lines) {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            this.connection = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@" + host + ":1521:" + sid,
                    user, password);
        } catch (Exception e) {
        }

        this.lines = lines;
    }

    public void run() {
        try {
            statement = this.connection.prepareStatement(sql);
            for (String line : lines) {
                String[] args = line.replace("\"", "").split(";");
                this.setSQLValues(args);
            }

            statement.executeBatch();
            statement.close();

            this.closeConnection();
            System.out.println("Finished at " + new Date().toString());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() throws SQLException {
        if (!this.connection.isClosed()) {
            this.connection.close();
        }
    }

    private void setSQLValues(String[] args)
            throws SQLException, ClassNotFoundException {

        for (int i = 0; i < 12; i++) {
            statement.setString(i + 1, args[i]);
        }
        statement.addBatch();
    }

}
