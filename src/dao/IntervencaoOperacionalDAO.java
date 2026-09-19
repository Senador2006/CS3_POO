package dao;

import db.ConexaoBD;
import model.IntervencaoOperacional;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class IntervencaoOperacionalDAO {

    public static final String SQL_INSERIR = """
            INSERT INTO T_MOTIVA_INTERVENCAO (
                ID_INTERVENCAO, ID_TRECHO, ID_EQUIPE, DS_TIPO, ST_INTERVENCAO,
                DT_AGENDADA, NR_ALTURA_ANTES, NR_ALTURA_DEPOIS, DS_OBSERVACAO
            ) VALUES (SEQ_MOTIVA_INTERVENCAO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    public static final String SQL_BUSCAR_POR_ID = """
            SELECT ID_INTERVENCAO, ID_TRECHO, ID_EQUIPE, DS_TIPO, ST_INTERVENCAO,
                   DT_AGENDADA, NR_ALTURA_ANTES, NR_ALTURA_DEPOIS, DS_OBSERVACAO
              FROM T_MOTIVA_INTERVENCAO
             WHERE ID_INTERVENCAO = ?
            """;

    public static final String SQL_LISTAR_TODAS = """
            SELECT ID_INTERVENCAO, ID_TRECHO, ID_EQUIPE, DS_TIPO, ST_INTERVENCAO,
                   DT_AGENDADA, NR_ALTURA_ANTES, NR_ALTURA_DEPOIS, DS_OBSERVACAO
              FROM T_MOTIVA_INTERVENCAO
             ORDER BY DT_AGENDADA, ID_INTERVENCAO
            """;

    public static final String SQL_ATUALIZAR = """
            UPDATE T_MOTIVA_INTERVENCAO
               SET ID_TRECHO = ?,
                   ID_EQUIPE = ?,
                   DS_TIPO = ?,
                   ST_INTERVENCAO = ?,
                   DT_AGENDADA = ?,
                   NR_ALTURA_ANTES = ?,
                   NR_ALTURA_DEPOIS = ?,
                   DS_OBSERVACAO = ?
             WHERE ID_INTERVENCAO = ?
            """;

    public static final String SQL_DELETAR = "DELETE FROM T_MOTIVA_INTERVENCAO WHERE ID_INTERVENCAO = ?";

    public IntervencaoOperacionalDAO() {
    }

    public IntervencaoOperacional inserir(IntervencaoOperacional intervencao) {
        PreparedStatement stmt = null;
        try {
            Connection conn = ConexaoBD.getInstancia().conectar();
            stmt = conn.prepareStatement(SQL_INSERIR, new String[] {"ID_INTERVENCAO"});
            preencherDados(stmt, intervencao);
            stmt.executeUpdate();

            Integer id = DaoUtil.obterIdGerado(stmt, "SEQ_MOTIVA_INTERVENCAO");
            if (id != null) {
                return intervencao.comId(id);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DaoUtil.fechar(null, stmt);
        }
        return null;
    }

    public IntervencaoOperacional buscarPorId(int id) {
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

    public List<IntervencaoOperacional> listarTodas() {
        List<IntervencaoOperacional> intervencoes = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection conn = ConexaoBD.getInstancia().conectar();
            stmt = conn.prepareStatement(SQL_LISTAR_TODAS);
            rs = stmt.executeQuery();
            while (rs.next()) {
                intervencoes.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DaoUtil.fechar(rs, stmt);
        }
        return intervencoes;
    }

    public boolean atualizar(IntervencaoOperacional intervencao) {
        PreparedStatement stmt = null;
        try {
            Connection conn = ConexaoBD.getInstancia().conectar();
            stmt = conn.prepareStatement(SQL_ATUALIZAR);
            preencherDados(stmt, intervencao);
            stmt.setInt(9, intervencao.id());
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

    private void preencherDados(PreparedStatement stmt, IntervencaoOperacional intervencao) throws SQLException {
        stmt.setInt(1, intervencao.idTrecho());
        stmt.setInt(2, intervencao.idEquipe());
        stmt.setString(3, intervencao.tipo());
        stmt.setString(4, intervencao.status());
        stmt.setDate(5, Date.valueOf(intervencao.dataAgendada()));
        if (intervencao.alturaAntes() == null) {
            stmt.setNull(6, Types.NUMERIC);
        } else {
            stmt.setDouble(6, intervencao.alturaAntes());
        }
        if (intervencao.alturaDepois() == null) {
            stmt.setNull(7, Types.NUMERIC);
        } else {
            stmt.setDouble(7, intervencao.alturaDepois());
        }
        stmt.setString(8, intervencao.observacao());
    }

    private IntervencaoOperacional mapear(ResultSet rs) throws SQLException {
        Date data = rs.getDate("DT_AGENDADA");
        return new IntervencaoOperacional(
                rs.getInt("ID_INTERVENCAO"),
                rs.getInt("ID_TRECHO"),
                rs.getInt("ID_EQUIPE"),
                rs.getString("DS_TIPO"),
                rs.getString("ST_INTERVENCAO"),
                data != null ? data.toLocalDate() : null,
                DaoUtil.doubleOuNulo(rs, "NR_ALTURA_ANTES"),
                DaoUtil.doubleOuNulo(rs, "NR_ALTURA_DEPOIS"),
                rs.getString("DS_OBSERVACAO"));
    }
}
