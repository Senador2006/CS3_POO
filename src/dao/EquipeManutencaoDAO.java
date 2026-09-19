package dao;

import db.ConexaoBD;
import model.EquipeManutencao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipeManutencaoDAO {

    public static final String SQL_INSERIR = """
            INSERT INTO T_MOTIVA_EQUIPE_MANUTENCAO
                (ID_EQUIPE, NM_EQUIPE, DS_ESPECIALIDADE, QT_MEMBROS, DS_BASE_OPERACIONAL, ST_DISPONIVEL)
            VALUES (SEQ_MOTIVA_EQUIPE.NEXTVAL, ?, ?, ?, ?, ?)
            """;

    public static final String SQL_BUSCAR_POR_ID = """
            SELECT ID_EQUIPE, NM_EQUIPE, DS_ESPECIALIDADE, QT_MEMBROS, DS_BASE_OPERACIONAL, ST_DISPONIVEL
              FROM T_MOTIVA_EQUIPE_MANUTENCAO
             WHERE ID_EQUIPE = ?
            """;

    public static final String SQL_LISTAR_TODAS = """
            SELECT ID_EQUIPE, NM_EQUIPE, DS_ESPECIALIDADE, QT_MEMBROS, DS_BASE_OPERACIONAL, ST_DISPONIVEL
              FROM T_MOTIVA_EQUIPE_MANUTENCAO
             ORDER BY ID_EQUIPE
            """;

    public static final String SQL_ATUALIZAR = """
            UPDATE T_MOTIVA_EQUIPE_MANUTENCAO
               SET NM_EQUIPE = ?,
                   DS_ESPECIALIDADE = ?,
                   QT_MEMBROS = ?,
                   DS_BASE_OPERACIONAL = ?,
                   ST_DISPONIVEL = ?
             WHERE ID_EQUIPE = ?
            """;

    public static final String SQL_DELETAR = "DELETE FROM T_MOTIVA_EQUIPE_MANUTENCAO WHERE ID_EQUIPE = ?";

    public EquipeManutencaoDAO() {
    }

    public EquipeManutencao inserir(EquipeManutencao equipe) {
        PreparedStatement stmt = null;
        try {
            Connection conn = ConexaoBD.getInstancia().conectar();
            stmt = conn.prepareStatement(SQL_INSERIR, new String[] {"ID_EQUIPE"});
            stmt.setString(1, equipe.nome());
            stmt.setString(2, equipe.especialidade());
            stmt.setInt(3, equipe.quantidadeMembros());
            stmt.setString(4, equipe.baseOperacional());
            stmt.setString(5, DaoUtil.flag(equipe.disponivel()));
            stmt.executeUpdate();

            Integer id = DaoUtil.obterIdGerado(stmt, "SEQ_MOTIVA_EQUIPE");
            if (id != null) {
                return equipe.comId(id);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DaoUtil.fechar(null, stmt);
        }
        return null;
    }

    public EquipeManutencao buscarPorId(int id) {
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

    public List<EquipeManutencao> listarTodas() {
        List<EquipeManutencao> equipes = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection conn = ConexaoBD.getInstancia().conectar();
            stmt = conn.prepareStatement(SQL_LISTAR_TODAS);
            rs = stmt.executeQuery();
            while (rs.next()) {
                equipes.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DaoUtil.fechar(rs, stmt);
        }
        return equipes;
    }

    public boolean atualizar(EquipeManutencao equipe) {
        PreparedStatement stmt = null;
        try {
            Connection conn = ConexaoBD.getInstancia().conectar();
            stmt = conn.prepareStatement(SQL_ATUALIZAR);
            stmt.setString(1, equipe.nome());
            stmt.setString(2, equipe.especialidade());
            stmt.setInt(3, equipe.quantidadeMembros());
            stmt.setString(4, equipe.baseOperacional());
            stmt.setString(5, DaoUtil.flag(equipe.disponivel()));
            stmt.setInt(6, equipe.id());
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

    private EquipeManutencao mapear(ResultSet rs) throws SQLException {
        return new EquipeManutencao(
                rs.getInt("ID_EQUIPE"),
                rs.getString("NM_EQUIPE"),
                rs.getString("DS_ESPECIALIDADE"),
                rs.getInt("QT_MEMBROS"),
                rs.getString("DS_BASE_OPERACIONAL"),
                DaoUtil.flag(rs.getString("ST_DISPONIVEL")));
    }
}
