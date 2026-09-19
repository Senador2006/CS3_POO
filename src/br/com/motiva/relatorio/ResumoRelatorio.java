package br.com.motiva.relatorio;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ResumoRelatorio {

    private final int totalTrechos;
    private final Map<NivelRisco, Long> porRisco;
    private final Map<TipoRecomendacao, Long> porIntervencao;

    public ResumoRelatorio(List<ItemRelatorioPrioridade> itens) {
        this.totalTrechos = itens.size();
        this.porRisco = new EnumMap<>(NivelRisco.class);
        this.porIntervencao = new EnumMap<>(TipoRecomendacao.class);

        for (NivelRisco risco : NivelRisco.values()) {
            porRisco.put(risco, 0L);
        }
        for (TipoRecomendacao tipo : TipoRecomendacao.values()) {
            porIntervencao.put(tipo, 0L);
        }

        for (ItemRelatorioPrioridade item : itens) {
            porRisco.merge(item.getNivelRisco(), 1L, Long::sum);
            porIntervencao.merge(item.getRecomendacao(), 1L, Long::sum);
        }
    }

    public int getTotalTrechos() {
        return totalTrechos;
    }

    public long getQuantidadePorRisco(NivelRisco risco) {
        return porRisco.getOrDefault(risco, 0L);
    }

    public long getQuantidadePorIntervencao(TipoRecomendacao tipo) {
        return porIntervencao.getOrDefault(tipo, 0L);
    }
}
