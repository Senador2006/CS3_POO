package br.com.motiva.relatorio;

import br.com.motiva.domain.CrescimentoUmido;
import br.com.motiva.domain.TipoTerreno;
import br.com.motiva.domain.TrechoRodovia;
import br.com.motiva.domain.TrechoRodoviaMonitorado;

import java.util.ArrayList;
import java.util.List;

public class MotorRegrasPrioridade {

    public RelatorioPrioridade gerarRelatorio(TrechoRodovia[] trechos) {
        processarCicloDiario(trechos);
        return gerarRelatorioAtual(trechos);
    }

    public RelatorioPrioridade gerarRelatorioAtual(TrechoRodovia[] trechos) {
        List<ItemRelatorioPrioridade> itens = new ArrayList<>();
        for (TrechoRodovia trecho : trechos) {
            if (trecho.requerIntervencao()) {
                itens.add(criarItem(trecho));
            }
        }
        return new RelatorioPrioridade(itens);
    }

    public void processarCicloDiario(TrechoRodovia[] trechos) {
        for (TrechoRodovia trecho : trechos) {
            if (trecho instanceof TrechoRodoviaMonitorado trechoMonitorado) {
                trechoMonitorado.simularLeituraSensor();
            } else {
                trecho.simularCrescimento();
            }
        }
    }

    private ItemRelatorioPrioridade criarItem(TrechoRodovia trecho) {
        int prioridade = calcularPrioridade(trecho);
        return new ItemRelatorioPrioridade(
                trecho.getRodovia(),
                trecho.getKm(),
                trecho.getAlturaVegetacao(),
                trecho.getTipoTerreno(),
                prioridade,
                NivelRisco.aPartirDaPrioridade(prioridade),
                definirRecomendacao(trecho),
                trecho.getDiasSemManutencao(),
                trecho.possuiMonitoramentoIot());
    }

    int calcularPrioridade(TrechoRodovia trecho) {
        double excesso = trecho.getAlturaVegetacao() - trecho.getLimiteAlturaCritica();
        int prioridadeBase = (int) Math.ceil(excesso * 100);

        if (trecho.getComportamentoCrescimento() instanceof CrescimentoUmido) {
            prioridadeBase += 20;
        }

        if (trecho.getTipoTerreno() == TipoTerreno.ROCHOSO) {
            prioridadeBase += 10;
        }

        if (trecho.isInfestacaoInvasora()) {
            prioridadeBase += 15;
        }

        if (trecho.getDiasSemManutencao() >= 45) {
            prioridadeBase += 12;
        } else if (trecho.getDiasSemManutencao() >= 30) {
            prioridadeBase += 6;
        }

        if (trecho.possuiMonitoramentoIot()) {
            prioridadeBase += 5;
        }

        return prioridadeBase;
    }

    TipoRecomendacao definirRecomendacao(TrechoRodovia trecho) {
        if (trecho.isInfestacaoInvasora()) {
            return TipoRecomendacao.PULVERIZACAO;
        }

        if (trecho.isAcessoDificil() || trecho.getTipoTerreno() == TipoTerreno.ROCHOSO) {
            return TipoRecomendacao.ROCADA_MANUAL;
        }

        return TipoRecomendacao.ROCADA_MECANIZADA;
    }
}
