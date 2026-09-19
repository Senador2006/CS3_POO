package br.com.motiva.relatorio;

import br.com.motiva.domain.TipoTerreno;

public class ItemRelatorioPrioridade implements Comparable<ItemRelatorioPrioridade> {

    private final String rodovia;
    private final int km;
    private final double alturaVegetacao;
    private final TipoTerreno tipoTerreno;
    private final int prioridade;
    private final NivelRisco nivelRisco;
    private final TipoRecomendacao recomendacao;
    private final int diasSemManutencao;
    private final boolean monitoradoIot;

    public ItemRelatorioPrioridade(
            String rodovia,
            int km,
            double alturaVegetacao,
            TipoTerreno tipoTerreno,
            int prioridade,
            NivelRisco nivelRisco,
            TipoRecomendacao recomendacao,
            int diasSemManutencao,
            boolean monitoradoIot) {
        this.rodovia = rodovia;
        this.km = km;
        this.alturaVegetacao = alturaVegetacao;
        this.tipoTerreno = tipoTerreno;
        this.prioridade = prioridade;
        this.nivelRisco = nivelRisco;
        this.recomendacao = recomendacao;
        this.diasSemManutencao = diasSemManutencao;
        this.monitoradoIot = monitoradoIot;
    }

    public String getRodovia() {
        return rodovia;
    }

    public int getKm() {
        return km;
    }

    public double getAlturaVegetacao() {
        return alturaVegetacao;
    }

    public TipoTerreno getTipoTerreno() {
        return tipoTerreno;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public NivelRisco getNivelRisco() {
        return nivelRisco;
    }

    public TipoRecomendacao getRecomendacao() {
        return recomendacao;
    }

    public int getDiasSemManutencao() {
        return diasSemManutencao;
    }

    public boolean isMonitoradoIot() {
        return monitoradoIot;
    }

    public String getIdentificador() {
        return rodovia + " KM " + km;
    }

    @Override
    public int compareTo(ItemRelatorioPrioridade outro) {
        return Integer.compare(outro.prioridade, this.prioridade);
    }

    @Override
    public String toString() {
        return String.format(
                "%s | %s | Altura: %.2fm | Score: %d | %s | %s%s",
                getIdentificador(),
                tipoTerreno.getDescricao(),
                alturaVegetacao,
                prioridade,
                nivelRisco.getRotulo(),
                recomendacao.getDescricao(),
                monitoradoIot ? " | IoT" : "");
    }
}
