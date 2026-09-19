package dao;

import db.ConexaoBD;
import model.RelatorioPrioridade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RelatorioPrioridadeDAO {

    public static final String SQL_INSERIR = """
            INSERT INTO T_MOTIVA_RELATORIO_PRIORIDADE
                (ID_RELATORIO, QT_URGENTE, QT_CRITICO, QT_ATENCAO, QT_NORMAL, DS_RESUMO)
            VALUES (SEQ_MOTIVA_RELATORIO.NEXTVAL, ?, ?, ?, ?, ?)
            """;

    public static final String SQL_BUSCAR_POR_ID = """
            SELECT ID_RELATORIO, DT_GERACAO, QT_URGENTE, QT_CRITICO, QT_ATENCAO, QT_NORMAL, DS_RESUMO
              FROM T_MOTIVA_RELATORIO_PRIORIDADE
             WHERE ID_RELATORIO = ?
            """;

    public static final String SQL_LISTAR_TODAS = """
            SELECT ID_RELATORIO, DT_GERACAO, QT_URGENTE, QT_CRITICO, QT_ATENCAO, QT_NORMAL, DS_RESUMO
              FROM T_MOTIVA_RELATORIO_PRIORIDADE
             ORDER BY DT_GERACAO DESC, ID_RELATORIO DESC
            """;

    public static final String SQL_ATUALIZAR = """
            UPDATE T_MOTIVA_RELATORIO_PRIORIDADE
               SET QT_URGENTE = ?,
                   QT_CRITICO = ?,
                   QT_ATENCAO = ?,
                   QT_NORMAL = ?,
                   DS_RESUMO = ?
             WHERE ID_RELATORIO = ?
            """;

    public static final String SQL_DELETAR = "DELETE FROM T_MOTIVA_RELATORIO_PRIORIDADE WHERE ID_RELATORIO = ?";

    public RelatorioPrioridadeDAO() {
    }

    public RelatorioPrioridade salvarRelatorio(
            int qtUrgente,
            int qtCritico,
            int qtAtencao,
            int qtNormal,
            String resumo) {
        return inserir(new RelatorioPrioridade(qtUrgente, qtCritico, qtAtencao, qtNormal, resumo));
    }

    public RelatorioPrioridade inserir(RelatorioPrioridade relatorio) {
        PreparedStatement stmt = null;
        try {
            Connection conn = ConexaoBD.getInstancia().conectar();
            stmt = conn.prepareStatement(SQL_INSERIR, new String[] {"ID_RELATORIO"});
            stmt.setInt(1, relatorio.qtUrgente());
            stmt.setInt(2, relatorio.qtCritico());
            stmt.setInt(3, relatorio.qtAtencao());
            stmt.setInt(4, relatorio.qtNormal());
            stmt.setString(5, relatorio.resumo());
            stmt.executeUpdate();

            Integer id = DaoUtil.obterIdGerado(stmt, "SEQ_MOTIVA_RELATORIO");
            if (id != null) {
                RelatorioPrioridade persistido = buscarPorId(id);
                if (persistido != null) {
                    return persistido;
                }
                return relatorio.comId(id, LocalDateTime.now());
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DaoUtil.fechar(null, stmt);
        }
        return null;
    }

    public RelatorioPrioridade buscarPorId(int id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection conn = ConexaoBD.getInstancia().conectar();
            stmt = conn.prepareStatement(SQL_BUSCAR_POR_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DaoUtil.fechar(rs, stmt);
        }
        return null;
    }

    public List<RelatorioPrioridade> listarTodas() {
        List<RelatorioPrioridade> relatorios = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection conn = ConexaoBD.getInstancia().conectar();
            stmt = conn.prepareStatement(SQL_LISTAR_TODAS);
            rs = stmt.executeQuery();
            while (rs.next()) {
                relatorios.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DaoUtil.fechar(rs, stmt);
        }
        return relatorios;
    }

    public boolean atualizar(RelatorioPrioridade relatorio) {
        PreparedStatement stmt = null;
        try {
            Connection conn = ConexaoBD.getInstancia().conectar();
            stmt = conn.prepareStatement(SQL_ATUALIZAR);
            stmt.setInt(1, relatorio.qtUrgente());
            stmt.setInt(2, relatorio.qtCritico());
            stmt.setInt(3, relatorio.qtAtencao());
            stmt.setInt(4, relatorio.qtNormal());
            stmt.setString(5, relatorio.resumo());
            stmt.setInt(6, relatorio.id());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DaoUtil.fechar(null, stmt);
        }
        return false;
    }

    public boolean deletar(int id) {
        PreparedStatement stmt = null;
        try {
            Connection conn = ConexaoBD.getInstancia().conectar();
            stmt = conn.prepareStatement(SQL_DELETAR);
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DaoUtil.fechar(null, stmt);
        }
        return false;
    }

    private RelatorioPrioridade mapear(ResultSet rs) throws SQLException {
        Timestamp geracao = rs.getTimestamp("DT_GERACAO");
        return new RelatorioPrioridade(
                rs.getInt("ID_RELATORIO"),
                geracao != null ? geracao.toLocalDateTime() : null,
                rs.getInt("QT_URGENTE"),
                rs.getInt("QT_CRITICO"),
                rs.getInt("QT_ATENCAO"),
                rs.getInt("QT_NORMAL"),
                rs.getString("DS_RESUMO"));
    }
}
