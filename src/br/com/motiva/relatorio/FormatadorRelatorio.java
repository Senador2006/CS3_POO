package br.com.motiva.relatorio;

import br.com.motiva.domain.TrechoRodovia;

public class FormatadorRelatorio {

    public String formatarPanorama(TrechoRodovia[] trechos) {
        StringBuilder builder = new StringBuilder();
        builder.append(linha('=')).append('\n');
        builder.append(centralizar("PANORAMA DAS RODOVIAS — MOTIVA")).append('\n');
        builder.append(linha('=')).append('\n');

        for (TrechoRodovia trecho : trechos) {
            builder.append(String.format(
                    "%-14s | %s | %.2fm (%3.0f%%) | %2d dias s/ manutenção%s%n",
                    trecho.getIdentificador(),
                    trecho.gerarBarraVegetacao(18),
                    trecho.getAlturaVegetacao(),
                    trecho.getPercentualLimite(),
                    trecho.getDiasSemManutencao(),
                    trecho.possuiMonitoramentoIot() ? " | IoT" : ""));
            builder.append(String.format(
                    "               %s — %s%n%n",
                    trecho.getTipoTerreno().getDescricao(),
                    trecho.requerIntervencao() ? "ACIMA DO LIMITE" : "Dentro do limite"));
        }

        return builder.toString().trim();
    }

    public String formatar(RelatorioPrioridade relatorio) {
        if (!relatorio.possuiIntervencoesPendentes()) {
            return linha('=') + "\n"
                    + centralizar("RELATÓRIO DE PRIORIDADE") + "\n"
                    + linha('=') + "\n\n"
                    + "  Nenhum trecho requer intervenção no momento.\n"
                    + "  Todos os KMs estão abaixo do limite crítico (1,20m).";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(linha('=')).append('\n');
        builder.append(centralizar("RELATÓRIO DE PRIORIDADE — MOTIVA")).append('\n');
        builder.append(linha('=')).append('\n');

        ResumoRelatorio resumo = relatorio.getResumo();
        builder.append(String.format(
                "Trechos críticos: %d | CRÍTICO: %d | ALTO: %d | MODERADO: %d%n",
                resumo.getTotalTrechos(),
                resumo.getQuantidadePorRisco(NivelRisco.CRITICO),
                resumo.getQuantidadePorRisco(NivelRisco.ALTO),
                resumo.getQuantidadePorRisco(NivelRisco.MODERADO)));
        builder.append(String.format(
                "Intervenções: Mecanizada %d | Manual %d | Pulverização %d%n%n",
                resumo.getQuantidadePorIntervencao(TipoRecomendacao.ROCADA_MECANIZADA),
                resumo.getQuantidadePorIntervencao(TipoRecomendacao.ROCADA_MANUAL),
                resumo.getQuantidadePorIntervencao(TipoRecomendacao.PULVERIZACAO)));

        int posicao = 1;
        for (ItemRelatorioPrioridade item : relatorio.getItens()) {
            builder.append(String.format(
                    "#%d %s%n   Risco: %s (%s)%n   Score: %d | Altura: %.2fm | %s%n   Ação: %s | %d dias sem manutenção%s%n%n",
                    posicao++,
                    item.getIdentificador(),
                    item.getNivelRisco().getRotulo(),
                    item.getNivelRisco().getOrientacao(),
                    item.getPrioridade(),
                    item.getAlturaVegetacao(),
                    item.getTipoTerreno().getDescricao(),
                    item.getRecomendacao().getDescricao(),
                    item.getDiasSemManutencao(),
                    item.isMonitoradoIot() ? " | Atualizado via IoT" : ""));
        }

        ItemRelatorioPrioridade maisCritico = relatorio.getMaisCritico();
        if (maisCritico != null) {
            builder.append(linha('-')).append('\n');
            builder.append(String.format(
                    "PRIORIDADE MÁXIMA: %s — despachar %s%n",
                    maisCritico.getIdentificador(),
                    maisCritico.getRecomendacao().getDescricao()));
        }

        return builder.toString().trim();
    }

    private String linha(char caractere) {
        return String.valueOf(caractere).repeat(62);
    }

    private String centralizar(String texto) {
        int padding = Math.max(0, (62 - texto.length()) / 2);
        return " ".repeat(padding) + texto;
    }
}
