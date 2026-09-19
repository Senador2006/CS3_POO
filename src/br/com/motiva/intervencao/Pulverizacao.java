package br.com.motiva.intervencao;

import br.com.motiva.domain.TrechoRodovia;

public class Pulverizacao extends IntervencaoOperacional {

    @Override
    public String executarServico(TrechoRodovia trecho) {
        double alturaAnterior = trecho.getAlturaVegetacao();
        trecho.aplicarIntervencao(0.40);
        return String.format(
                "Pulverização no %s — controle de invasoras (altura %.2fm -> 0.40m).",
                trecho.getIdentificador(),
                alturaAnterior);
    }
}
