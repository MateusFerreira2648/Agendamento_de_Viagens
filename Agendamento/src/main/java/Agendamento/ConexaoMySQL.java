package Agendamento;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoMySQL {

    private static final String USUARIO = "agendamentos";
    private static final String SENHA = "dQtUOh~xKgIQa.Bx";
    private static final String URL = "jdbc:mysql://localhost:3306/agendamentos";

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}