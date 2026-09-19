package service;

import dao.RelatorioPrioridadeDAO;
import model.RelatorioPrioridade;
import model.TrechoRodovia;
import br.com.motiva.relatorio.FormatadorRelatorio;
import br.com.motiva.relatorio.ItemRelatorioPrioridade;
import br.com.motiva.relatorio.MotorRegrasPrioridade;
import br.com.motiva.relatorio.NivelRisco;

public class GeradorRelatorio {

    private static final int LIMITE_URGENTE = 80;

    private final MotorRegrasPrioridade motor;
    private final FormatadorRelatorio formatador;
    private final RelatorioPrioridadeDAO daoRelatorio;

    public GeradorRelatorio() {
        this.motor = new MotorRegrasPrioridade();
        this.formatador = new FormatadorRelatorio();
        this.daoRelatorio = new RelatorioPrioridadeDAO();
    }

    public void gerarRelatorio(TrechoRodovia[] trechos) {
        imprimirCabecalho();

        if (trechos == null || trechos.length == 0) {
            System.out.println("Nenhum trecho encontrado para gerar o relatório.");
            return;
        }

        br.com.motiva.domain.TrechoRodovia[] dominio = paraDominio(trechos);
        var relatorio = motor.gerarRelatorioAtual(dominio);

        System.out.println(formatador.formatarPanorama(dominio));
        System.out.println();
        System.out.println(formatador.formatar(relatorio));
        System.out.println();

        int qtUrgente = 0;
        int qtCritico = 0;
        int qtAtencao = 0;

        for (ItemRelatorioPrioridade item : relatorio.getItens()) {
            if (item.getPrioridade() >= LIMITE_URGENTE) {
                qtUrgente++;
            } else if (item.getNivelRisco() == NivelRisco.CRITICO) {
                qtCritico++;
            } else {
                qtAtencao++;
            }
        }

        int qtNormal = trechos.length - relatorio.getItens().size();
        String resumo = montarResumo(trechos.length, qtUrgente, qtCritico, qtAtencao, qtNormal, relatorio);

        System.out.println("--- Persistência do histórico ---");
        RelatorioPrioridade persistido = daoRelatorio.salvarRelatorio(
                qtUrgente, qtCritico, qtAtencao, qtNormal, resumo);

        if (persistido != null) {
            System.out.println("Relatório salvo no Oracle: " + persistido);
        } else {
            System.err.println("Não foi possível salvar o relatório no banco.");
        }
    }

    private void imprimirCabecalho() {
        System.out.println();
        System.out.println("=".repeat(62));
        System.out.println("          GERADOR DE RELATÓRIO — MOTIVA / SPRINT 3");
        System.out.println("     Console (Sprint 2) + persistência Oracle (Sprint 3)");
        System.out.println("=".repeat(62));
        System.out.println();
    }

    private br.com.motiva.domain.TrechoRodovia[] paraDominio(TrechoRodovia[] trechos) {
        br.com.motiva.domain.TrechoRodovia[] dominio = new br.com.motiva.domain.TrechoRodovia[trechos.length];
        for (int i = 0; i < trechos.length; i++) {
            dominio[i] = trechos[i].paraDominio();
        }
        return dominio;
    }

    private String montarResumo(
            int total,
            int qtUrgente,
            int qtCritico,
            int qtAtencao,
            int qtNormal,
            br.com.motiva.relatorio.RelatorioPrioridade relatorio) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(
                "Trechos: %d | Urgente: %d | Crítico: %d | Atenção: %d | Normal: %d",
                total, qtUrgente, qtCritico, qtAtencao, qtNormal));

        var maisCritico = relatorio.getMaisCritico();
        if (maisCritico != null) {
            builder.append(" | Prioridade máxima: ")
                    .append(maisCritico.getIdentificador())
                    .append(" (")
                    .append(maisCritico.getRecomendacao().getDescricao())
                    .append(")");
        } else {
            builder.append(" | Nenhuma intervenção pendente.");
        }

        String texto = builder.toString();
        return texto.length() <= 1000 ? texto : texto.substring(0, 1000);
    }
}
