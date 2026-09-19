package db;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExecutorScriptSQL {

    public void garantirEsquema() {
        if (tabelaExiste("T_MOTIVA_TRECHO_RODOVIA")) {
            return;
        }

        System.out.println("Tabelas Motiva não encontradas. Executando seu-script-criacao.sql...");
        executarArquivo("seu-script-criacao.sql");

        if (tabelaVazia("T_MOTIVA_EQUIPE_MANUTENCAO")) {
            System.out.println("Carregando dados de teste (seu-script-dados.sql)...");
            executarArquivo("seu-script-dados.sql");
        }
    }

    public void executarArquivo(String nomeArquivo) {
        Path caminho = localizar(nomeArquivo);
        if (caminho == null) {
            System.err.println("Script não encontrado: " + nomeArquivo);
            return;
        }

        Connection conn = ConexaoBD.getInstancia().conectar();
        Statement stmt = null;
        try {
            String conteudo = Files.readString(caminho, StandardCharsets.UTF_8);
            stmt = conn.createStatement();
            for (String comando : separarComandos(conteudo)) {
                stmt.execute(comando);
            }
            System.out.println("Script executado: " + caminho.getFileName());
        } catch (Exception e) {
            System.err.println("Erro ao executar " + nomeArquivo + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar Statement: " + e.getMessage());
                }
            }
        }
    }

    private boolean tabelaExiste(String nomeTabela) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection conn = ConexaoBD.getInstancia().conectar();
            stmt = conn.prepareStatement("SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = ?");
            stmt.setString(1, nomeTabela);
            rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Erro SQL: " + e.getMessage());
            return false;
        } finally {
            fechar(rs, stmt);
        }
    }

    private boolean tabelaVazia(String nomeTabela) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Connection conn = ConexaoBD.getInstancia().conectar();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM " + nomeTabela);
            return rs.next() && rs.getInt(1) == 0;
        } catch (SQLException e) {
            return true;
        } finally {
            fechar(rs, stmt);
        }
    }

    private Path localizar(String nomeArquivo) {
        Path[] candidatos = {
            Path.of(nomeArquivo),
            Path.of(System.getProperty("user.dir"), nomeArquivo)
        };
        for (Path candidato : candidatos) {
            if (Files.exists(candidato)) {
                return candidato.toAbsolutePath();
            }
        }
        return null;
    }

    static List<String> separarComandos(String script) {
        List<String> comandos = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        boolean blocoPlsql = false;

        for (String linha : script.split("\\R")) {
            String trim = linha.trim();
            if (trim.isEmpty()) {
                continue;
            }
            if (!blocoPlsql && trim.startsWith("--")) {
                continue;
            }
            if (trim.equals("/")) {
                adicionar(comandos, buffer);
                blocoPlsql = false;
                continue;
            }
            if (trim.toUpperCase().startsWith("BEGIN")) {
                blocoPlsql = true;
            }
            buffer.append(linha).append('\n');
            if (!blocoPlsql && trim.endsWith(";")) {
                adicionar(comandos, buffer);
            }
        }
        adicionar(comandos, buffer);
        return comandos;
    }

    private static void adicionar(List<String> comandos, StringBuilder buffer) {
        String comando = buffer.toString().trim();
        buffer.setLength(0);
        if (comando.isEmpty()) {
            return;
        }
        if (!comando.toUpperCase().startsWith("BEGIN") && comando.endsWith(";")) {
            comando = comando.substring(0, comando.length() - 1).trim();
        }
        if (!comando.isEmpty()) {
            comandos.add(comando);
        }
    }

    private void fechar(ResultSet rs, Statement stmt) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ignored) {
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException ignored) {
        }
    }
}
