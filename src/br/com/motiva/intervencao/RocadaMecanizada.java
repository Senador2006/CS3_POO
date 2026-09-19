package br.com.motiva.intervencao;

import br.com.motiva.domain.TrechoRodovia;

public class RocadaMecanizada extends IntervencaoOperacional {

    @Override
    public String executarServico(TrechoRodovia trecho) {
        double alturaAnterior = trecho.getAlturaVegetacao();
        trecho.aplicarIntervencao(0.30);
        return String.format(
                "Roçadeira mecanizada no %s — altura reduzida de %.2fm para 0.30m.",
                trecho.getIdentificador(),
                alturaAnterior);
    }
}
