package dao;

import db.ConexaoBD;
import model.TrechoRodovia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class TrechoRodoviaDAO {

    public static final String SQL_INSERIR = """
            INSERT INTO T_MOTIVA_TRECHO_RODOVIA (
                ID_TRECHO, DS_RODOVIA, NR_KM, NR_ALTURA_VEGETACAO, DS_TIPO_TERRENO,
                ST_ACESSO_DIFICIL, ST_INFESTACAO_INVASORA, QT_DIAS_SEM_MANUTENCAO,
                ST_MONITORADO_IOT, NR_LEITURA_SENSOR, ID_EQUIPE_RESPONSAVEL
            ) VALUES (SEQ_MOTIVA_TRECHO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    public static final String SQL_BUSCAR_POR_ID = """
            SELECT ID_TRECHO, DS_RODOVIA, NR_KM, NR_ALTURA_VEGETACAO, DS_TIPO_TERRENO,
                   ST_ACESSO_DIFICIL, ST_INFESTACAO_INVASORA, QT_DIAS_SEM_MANUTENCAO,
                   ST_MONITORADO_IOT, NR_LEITURA_SENSOR, ID_EQUIPE_RESPONSAVEL
              FROM T_MOTIVA_TRECHO_RODOVIA
             WHERE ID_TRECHO = ?
            """;

    public static final String SQL_LISTAR_TODAS = """
            SELECT ID_TRECHO, DS_RODOVIA, NR_KM, NR_ALTURA_VEGETACAO, DS_TIPO_TERRENO,
                   ST_ACESSO_DIFICIL, ST_INFESTACAO_INVASORA, QT_DIAS_SEM_MANUTENCAO,
                   ST_MONITORADO_IOT, NR_LEITURA_SENSOR, ID_EQUIPE_RESPONSAVEL
              FROM T_MOTIVA_TRECHO_RODOVIA
             ORDER BY DS_RODOVIA, NR_KM
            """;

    public static final String SQL_ATUALIZAR = """
            UPDATE T_MOTIVA_TRECHO_RODOVIA
               SET DS_RODOVIA = ?,
                   NR_KM = ?,
                   NR_ALTURA_VEGETACAO = ?,
                   DS_TIPO_TERRENO = ?,
                   ST_ACESSO_DIFICIL = ?,
                   ST_INFESTACAO_INVASORA = ?,
                   QT_DIAS_SEM_MANUTENCAO = ?,
                   ST_MONITORADO_IOT = ?,
                   NR_LEITURA_SENSOR = ?,
                   ID_EQUIPE_RESPONSAVEL = ?
             WHERE ID_TRECHO = ?
            """;

    public static final String SQL_DELETAR = "DELETE FROM T_MOTIVA_TRECHO_RODOVIA WHERE ID_TRECHO = ?";

    public TrechoRodoviaDAO() {
    }

    public TrechoRodovia inserir(TrechoRodovia trecho) {
        PreparedStatement stmt = null;
        try {
            Connection conn = ConexaoBD.getInstancia().conectar();
            stmt = conn.prepareStatement(SQL_INSERIR, new String[] {"ID_TRECHO"});
            preencherDados(stmt, trecho);
            stmt.executeUpdate();

            Integer id = DaoUtil.obterIdGerado(stmt, "SEQ_MOTIVA_TRECHO");
            if (id != null) {
                return trecho.comId(id);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DaoUtil.fechar(null, stmt);
        }
        return null;
    }

    public TrechoRodovia buscarPorId(int id) {
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

    public List<TrechoRodovia> listarTodas() {
        List<TrechoRodovia> trechos = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection conn = ConexaoBD.getInstancia().conectar();
            stmt = conn.prepareStatement(SQL_LISTAR_TODAS);
            rs = stmt.executeQuery();
            while (rs.next()) {
                trechos.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DaoUtil.fechar(rs, stmt);
        }
        return trechos;
    }

    public boolean atualizar(TrechoRodovia trecho) {
        PreparedStatement stmt = null;
        try {
            Connection conn = ConexaoBD.getInstancia().conectar();
            stmt = conn.prepareStatement(SQL_ATUALIZAR);
            preencherDados(stmt, trecho);
            stmt.setInt(11, trecho.id());
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

    private void preencherDados(PreparedStatement stmt, TrechoRodovia trecho) throws SQLException {
        stmt.setString(1, trecho.rodovia());
        stmt.setInt(2, trecho.km());
        stmt.setDouble(3, trecho.alturaVegetacao());
        stmt.setString(4, trecho.tipoTerreno());
        stmt.setString(5, DaoUtil.flag(trecho.acessoDificil()));
        stmt.setString(6, DaoUtil.flag(trecho.infestacaoInvasora()));
        stmt.setInt(7, trecho.diasSemManutencao());
        stmt.setString(8, DaoUtil.flag(trecho.monitoradoIot()));
        if (trecho.leituraSensor() == null) {
            stmt.setNull(9, Types.NUMERIC);
        } else {
            stmt.setDouble(9, trecho.leituraSensor());
        }
        if (trecho.idEquipeResponsavel() == null) {
            stmt.setNull(10, Types.NUMERIC);
        } else {
            stmt.setInt(10, trecho.idEquipeResponsavel());
        }
    }

    private TrechoRodovia mapear(ResultSet rs) throws SQLException {
        return new TrechoRodovia(
                rs.getInt("ID_TRECHO"),
                rs.getString("DS_RODOVIA"),
                rs.getInt("NR_KM"),
                rs.getDouble("NR_ALTURA_VEGETACAO"),
                rs.getString("DS_TIPO_TERRENO"),
                DaoUtil.flag(rs.getString("ST_ACESSO_DIFICIL")),
                DaoUtil.flag(rs.getString("ST_INFESTACAO_INVASORA")),
                rs.getInt("QT_DIAS_SEM_MANUTENCAO"),
                DaoUtil.flag(rs.getString("ST_MONITORADO_IOT")),
                DaoUtil.doubleOuNulo(rs, "NR_LEITURA_SENSOR"),
                DaoUtil.integerOuNulo(rs, "ID_EQUIPE_RESPONSAVEL"));
    }
}
