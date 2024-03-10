package Agendamento;

import java.sql.Connection;
import java.sql.SQLException;

public class TesteConexaoDB {
    public static void main(String[] args) {
        try {
            Connection conexao = ConexaoMySQL.getConexao();
            if (conexao != null) {
                System.out.println("Conexão estabelecida com sucesso!");
                // Aqui você pode adicionar mais lógica para interagir com o banco de dados, se necessário
                conexao.close(); // Não se esqueça de fechar a conexão quando terminar de usá-la
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao tentar conectar ao banco de dados: " + e.getMessage());
        }
    }
}