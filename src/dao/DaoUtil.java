package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

final class DaoUtil {

    private DaoUtil() {
    }

    static String flag(boolean valor) {
        return valor ? "S" : "N";
    }

    static boolean flag(String valor) {
        return "S".equalsIgnoreCase(valor);
    }

    static Integer integerOuNulo(ResultSet rs, String coluna) throws SQLException {
        int valor = rs.getInt(coluna);
        return rs.wasNull() ? null : valor;
    }

    static Double doubleOuNulo(ResultSet rs, String coluna) throws SQLException {
        double valor = rs.getDouble(coluna);
        return rs.wasNull() ? null : valor;
    }

    static Integer obterIdGerado(PreparedStatement stmt, String sequence) throws SQLException {
        ResultSet keys = stmt.getGeneratedKeys();
        try {
            if (keys != null && keys.next()) {
                return keys.getInt(1);
            }
        } finally {
            if (keys != null) {
                keys.close();
            }
        }

        PreparedStatement currval = stmt.getConnection().prepareStatement(
                "SELECT " + sequence + ".CURRVAL FROM DUAL");
        ResultSet rs = currval.executeQuery();
        try {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } finally {
            fechar(rs, currval);
        }
        return null;
    }

    static void fechar(ResultSet rs, Statement stmt) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar ResultSet: " + e.getMessage());
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage());
        }
    }
}
