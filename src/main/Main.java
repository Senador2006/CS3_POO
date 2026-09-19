package main;

import dao.EquipeManutencaoDAO;
import dao.IntervencaoOperacionalDAO;
import dao.RelatorioPrioridadeDAO;
import dao.TrechoRodoviaDAO;
import db.ConexaoBD;
import model.EquipeManutencao;
import model.IntervencaoOperacional;
import model.TrechoRodovia;
import service.GeradorRelatorio;

import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        ConexaoBD conexao = ConexaoBD.getInstancia();
        conexao.conectar();

        if (conexao.getConexao() == null) {
            System.err.println("Não foi possível conectar ao Oracle. Verifique credenciais, VPN/lab e o ojdbc17.jar.");
            return;
        }

        try {
            new db.ExecutorScriptSQL().garantirEsquema();

            EquipeManutencaoDAO daoEquipe = new EquipeManutencaoDAO();
            TrechoRodoviaDAO daoTrecho = new TrechoRodoviaDAO();
            IntervencaoOperacionalDAO daoIntervencao = new IntervencaoOperacionalDAO();
            RelatorioPrioridadeDAO daoRelatorio = new RelatorioPrioridadeDAO();

            garantirCenarioDemonstracao(daoEquipe, daoTrecho);

            testarCrudEquipe(daoEquipe);
            EquipeManutencao equipeRef = primeiraEquipe(daoEquipe);

            limparTrechoDemo(daoTrecho, daoIntervencao);
            TrechoRodovia trechoDemo = testarCrudTrecho(daoTrecho, equipeRef);
            testarCrudIntervencao(daoIntervencao, trechoDemo, equipeRef);

            System.out.println("\n===== 5) Gerar relatório com persistência =====");
            GeradorRelatorio gerador = new GeradorRelatorio();
            TrechoRodovia[] trechos = daoTrecho.listarTodas().toArray(TrechoRodovia[]::new);
            gerador.gerarRelatorio(trechos);

            System.out.println("\n===== 6) Histórico de relatórios =====");
            daoRelatorio.listarTodas().forEach(relatorio -> System.out.println("  " + relatorio));
        } finally {
            conexao.desconectar();
        }
    }

    private static void testarCrudEquipe(EquipeManutencaoDAO daoEquipe) {
        System.out.println("\n===== 2) CRUD EquipeManutencao =====");

        EquipeManutencao nova = daoEquipe.inserir(new EquipeManutencao(
                "Equipe CRUD Demo",
                "ROCADA_MECANIZADA",
                5,
                "Base Teste — Lab FIAP",
                true));
        System.out.println("INSERIR: " + nova);

        if (nova == null) {
            System.err.println("Falha ao inserir equipe de demonstração.");
            return;
        }

        System.out.println("BUSCAR:  " + daoEquipe.buscarPorId(nova.id()));

        System.out.println("LISTAR:");
        daoEquipe.listarTodas().forEach(equipe -> System.out.println("  " + equipe));

        EquipeManutencao atualizada = new EquipeManutencao(
                nova.id(),
                "Equipe CRUD Demo Atualizada",
                nova.especialidade(),
                7,
                nova.baseOperacional(),
                false);
        System.out.println("ATUALIZAR: " + daoEquipe.atualizar(atualizada) + " -> " + daoEquipe.buscarPorId(nova.id()));

        System.out.println("DELETAR: " + daoEquipe.deletar(nova.id()));
        System.out.println("BUSCAR após delete: " + daoEquipe.buscarPorId(nova.id()));
    }

    private static TrechoRodovia testarCrudTrecho(TrechoRodoviaDAO daoTrecho, EquipeManutencao equipe) {
        System.out.println("\n===== 3) CRUD TrechoRodovia =====");

        Integer idEquipe = equipe == null ? null : equipe.id();
        TrechoRodovia novo = daoTrecho.inserir(new TrechoRodovia(
                "BR-000",
                999,
                1.42,
                "UMIDO",
                false,
                false,
                12,
                false,
                null,
                idEquipe));
        System.out.println("INSERIR: " + novo);

        if (novo == null) {
            System.err.println("Falha ao inserir trecho de demonstração.");
            return null;
        }

        System.out.println("BUSCAR:  " + daoTrecho.buscarPorId(novo.id()));

        System.out.println("LISTAR:");
        daoTrecho.listarTodas().forEach(trecho -> System.out.println("  " + trecho));

        TrechoRodovia atualizado = novo.comAltura(1.55);
        System.out.println("ATUALIZAR: " + daoTrecho.atualizar(atualizado) + " -> " + daoTrecho.buscarPorId(novo.id()));

        return daoTrecho.buscarPorId(novo.id());
    }

    private static void testarCrudIntervencao(
            IntervencaoOperacionalDAO daoIntervencao,
            TrechoRodovia trecho,
            EquipeManutencao equipe) {
        System.out.println("\n===== 4) CRUD IntervencaoOperacional =====");

        if (trecho == null || equipe == null) {
            System.err.println("Sem trecho/equipe para testar intervenção.");
            return;
        }

        IntervencaoOperacional nova = daoIntervencao.inserir(new IntervencaoOperacional(
                trecho.id(),
                equipe.id(),
                "ROCADA_MECANIZADA",
                "PENDENTE",
                LocalDate.now().plusDays(1),
                trecho.alturaVegetacao(),
                null,
                "Intervenção de demonstração do Main da Sprint 3"));
        System.out.println("INSERIR: " + nova);

        if (nova == null) {
            return;
        }

        System.out.println("BUSCAR:  " + daoIntervencao.buscarPorId(nova.id()));

        System.out.println("LISTAR:");
        daoIntervencao.listarTodas().forEach(item -> System.out.println("  " + item));

        IntervencaoOperacional executada = nova.comStatus("EXECUTADA", 0.30);
        System.out.println("ATUALIZAR: " + daoIntervencao.atualizar(executada) + " -> " + daoIntervencao.buscarPorId(nova.id()));

        System.out.println("DELETAR: " + daoIntervencao.deletar(nova.id()));
        System.out.println("BUSCAR após delete: " + daoIntervencao.buscarPorId(nova.id()));
    }

    private static void garantirCenarioDemonstracao(EquipeManutencaoDAO daoEquipe, TrechoRodoviaDAO daoTrecho) {
        if (!daoTrecho.listarTodas().isEmpty()) {
            System.out.println("Cenário já existe no banco — reaproveitando trechos persistidos.");
            return;
        }

        System.out.println("Banco vazio: carregando cenário do CatalogoRodovias (Sprint 2).");

        EquipeManutencao mecanizada = daoEquipe.inserir(new EquipeManutencao(
                "Equipe Alpha — Roçadeira", "ROCADA_MECANIZADA", 6, "Base Serra — BR-116", true));
        EquipeManutencao manual = daoEquipe.inserir(new EquipeManutencao(
                "Equipe Beta — Encosta", "ROCADA_MANUAL", 4, "Base Litoral — BR-101", true));
        EquipeManutencao pulverizacao = daoEquipe.inserir(new EquipeManutencao(
                "Equipe Gama — Invasoras", "PULVERIZACAO", 3, "Base Vale — BR-381", true));

        Integer idMecanizada = mecanizada == null ? null : mecanizada.id();
        Integer idManual = manual == null ? null : manual.id();
        Integer idPulverizacao = pulverizacao == null ? null : pulverizacao.id();

        daoTrecho.inserir(new TrechoRodovia("BR-101", 412, 0.85, "SECO", false, false, 22, false, null, idMecanizada));
        daoTrecho.inserir(new TrechoRodovia("BR-101", 413, 1.05, "UMIDO", false, false, 38, false, null, idMecanizada));
        daoTrecho.inserir(new TrechoRodovia("BR-101", 414, 0.95, "UMIDO", true, false, 41, false, null, idManual));
        daoTrecho.inserir(new TrechoRodovia("BR-116", 78, 1.15, "ROCHOSO", true, false, 52, false, null, idManual));
        daoTrecho.inserir(new TrechoRodovia("BR-116", 79, 0.70, "SECO", false, true, 60, true, 1.28, idPulverizacao));
        daoTrecho.inserir(new TrechoRodovia("BR-381", 505, 0.60, "SECO", false, true, 15, false, null, idPulverizacao));
        daoTrecho.inserir(new TrechoRodovia("BR-381", 506, 1.00, "UMIDO", false, false, 33, true, 1.18, idMecanizada));
        daoTrecho.inserir(new TrechoRodovia("BR-381", 507, 1.35, "SECO", false, false, 8, false, null, idMecanizada));
    }

    private static void limparTrechoDemo(TrechoRodoviaDAO daoTrecho, IntervencaoOperacionalDAO daoIntervencao) {
        for (TrechoRodovia trecho : daoTrecho.listarTodas()) {
            if ("BR-000".equals(trecho.rodovia()) && trecho.km() == 999) {
                for (IntervencaoOperacional intervencao : daoIntervencao.listarTodas()) {
                    if (intervencao.idTrecho() == trecho.id()) {
                        daoIntervencao.deletar(intervencao.id());
                    }
                }
                daoTrecho.deletar(trecho.id());
            }
        }
    }

    private static EquipeManutencao primeiraEquipe(EquipeManutencaoDAO daoEquipe) {
        List<EquipeManutencao> equipes = daoEquipe.listarTodas();
        return equipes.isEmpty() ? null : equipes.get(0);
    }
}
