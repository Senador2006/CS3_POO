package br.com.motiva;

import br.com.motiva.domain.TrechoRodovia;
import br.com.motiva.intervencao.FabricaIntervencao;
import br.com.motiva.intervencao.IntervencaoOperacional;
import br.com.motiva.relatorio.FormatadorRelatorio;
import br.com.motiva.relatorio.MotorRegrasPrioridade;
import br.com.motiva.relatorio.RelatorioPrioridade;
import br.com.motiva.simulacao.SimuladorCrescimento;

import java.util.Scanner;

public class ConsoleApp {

    private final TrechoRodovia[] trechos;
    private final MotorRegrasPrioridade motor;
    private final SimuladorCrescimento simulador;
    private final FormatadorRelatorio formatador;
    private final Scanner entrada;
    private RelatorioPrioridade ultimoRelatorio;

    public ConsoleApp(TrechoRodovia[] trechos) {
        this.trechos = trechos;
        this.motor = new MotorRegrasPrioridade();
        this.simulador = new SimuladorCrescimento(motor);
        this.formatador = new FormatadorRelatorio();
        this.entrada = new Scanner(System.in);
    }

    public void iniciar() {
        exibirBanner();
        boolean executando = true;

        while (executando) {
            exibirMenu();
            String opcao = entrada.nextLine().trim();

            switch (opcao) {
                case "1" -> exibirPanorama();
                case "2" -> simularCrescimento();
                case "3" -> gerarRelatorio();
                case "4" -> executarIntervencoes();
                case "5" -> exibirRankingRisco();
                case "0" -> {
                    System.out.println("\nEncerrando sistema Motiva. Rodovias mais seguras!");
                    executando = false;
                }
                default -> System.out.println("\nOpção inválida. Tente novamente.");
            }
        }
    }

    private void exibirBanner() {
        System.out.println("""
                
                ╔════════════════════════════════════════════════════════════╗
                ║          MOTIVA — Inteligência em Roçada Rodoviária        ║
                ║     Monitoramento · IoT · Priorização · Intervenção       ║
                ╚════════════════════════════════════════════════════════════╝
                """);
    }

    private void exibirMenu() {
        System.out.println("""
                ┌──────────────── MENU ────────────────┐
                │ 1. Panorama das rodovias             │
                │ 2. Simular crescimento (dias)        │
                │ 3. Gerar relatório de prioridade     │
                │ 4. Executar intervenções pendentes   │
                │ 5. Ranking de risco                  │
                │ 0. Sair                              │
                └──────────────────────────────────────┘
                Escolha:""");
    }

    private void exibirPanorama() {
        System.out.println("\n" + formatador.formatarPanorama(trechos));
    }

    private void simularCrescimento() {
        System.out.print("\nQuantos dias deseja simular? ");
        try {
            int dias = Integer.parseInt(entrada.nextLine().trim());
            ultimoRelatorio = simulador.simular(trechos, dias);
            System.out.printf("%nSimulação de %d dia(s) concluída.%n%n", dias);
            System.out.println(formatador.formatarPanorama(trechos));
            System.out.println("\n" + formatador.formatar(ultimoRelatorio));
        } catch (NumberFormatException e) {
            System.out.println("\nInforme um número inteiro válido.");
        }
    }

    private void gerarRelatorio() {
        ultimoRelatorio = motor.gerarRelatorioAtual(trechos);
        System.out.println("\n" + formatador.formatar(ultimoRelatorio));
    }

    private void executarIntervencoes() {
        if (ultimoRelatorio == null || !ultimoRelatorio.possuiIntervencoesPendentes()) {
            ultimoRelatorio = motor.gerarRelatorioAtual(trechos);
        }

        if (!ultimoRelatorio.possuiIntervencoesPendentes()) {
            System.out.println("\nNenhuma intervenção pendente no momento.");
            return;
        }

        System.out.println("\n--- Despacho de equipes ---");
        for (var item : ultimoRelatorio.getItens()) {
            TrechoRodovia trecho = encontrarTrecho(item.getKm(), item.getRodovia());
            IntervencaoOperacional intervencao = FabricaIntervencao.criar(item.getRecomendacao());
            System.out.println("→ " + intervencao.executarServico(trecho));
        }

        ultimoRelatorio = motor.gerarRelatorioAtual(trechos);
        System.out.println("\nSituação após intervenções:");
        System.out.println(formatador.formatarPanorama(trechos));
    }

    private void exibirRankingRisco() {
        RelatorioPrioridade relatorio = motor.gerarRelatorioAtual(trechos);
        if (!relatorio.possuiIntervencoesPendentes()) {
            System.out.println("\nNenhum trecho acima do limite — ranking vazio.");
            return;
        }

        System.out.println("\n--- RANKING DE RISCO ---");
        int posicao = 1;
        for (var item : relatorio.getItens()) {
            System.out.printf(
                    "%dº %s | Score %d | %s%n",
                    posicao++,
                    item.getIdentificador(),
                    item.getPrioridade(),
                    item.getNivelRisco().getRotulo());
        }
    }

    private TrechoRodovia encontrarTrecho(int km, String rodovia) {
        for (TrechoRodovia trecho : trechos) {
            if (trecho.getKm() == km && trecho.getRodovia().equals(rodovia)) {
                return trecho;
            }
        }
        throw new IllegalArgumentException("Trecho não encontrado: " + rodovia + " KM " + km);
    }
}
