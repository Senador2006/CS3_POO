package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String URL = valorOuPadrao("MOTIVA_ORACLE_URL", "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL");
    private static final String USUARIO = valorOuPadrao("MOTIVA_ORACLE_USER", "RM563448");
    private static final String SENHA = valorOuPadrao("MOTIVA_ORACLE_PASSWORD", "110906");

    private static ConexaoBD instancia;
    private Connection conexao;

    private ConexaoBD() {
    }

    public static synchronized ConexaoBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexaoBD();
        }
        return instancia;
    }

    public Connection conectar() {
        try {
            if (conexao == null || conexao.isClosed()) {
                Class.forName(DRIVER);
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                System.out.println("Conexão com Oracle estabelecida (" + USUARIO + ").");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver not found. Adicione lib/ojdbc17.jar ao classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
        }
        return conexao;
    }

    public void desconectar() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println("Conexão com Oracle encerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao desconectar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Connection getConexao() {
        return conexao;
    }

    private static String valorOuPadrao(String variavel, String padrao) {
        String valor = System.getenv(variavel);
        if (valor == null || valor.isBlank()) {
            valor = System.getProperty(variavel);
        }
        return (valor == null || valor.isBlank()) ? padrao : valor;
    }
}
