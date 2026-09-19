package br.com.motiva.relatorio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RelatorioPrioridade {

    private final List<ItemRelatorioPrioridade> itens;
    private final ResumoRelatorio resumo;

    public RelatorioPrioridade(List<ItemRelatorioPrioridade> itens) {
        this.itens = new ArrayList<>(itens);
        Collections.sort(this.itens);
        this.resumo = new ResumoRelatorio(this.itens);
    }

    public List<ItemRelatorioPrioridade> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public ResumoRelatorio getResumo() {
        return resumo;
    }

    public boolean possuiIntervencoesPendentes() {
        return !itens.isEmpty();
    }

    public ItemRelatorioPrioridade getMaisCritico() {
        return itens.isEmpty() ? null : itens.get(0);
    }
}
