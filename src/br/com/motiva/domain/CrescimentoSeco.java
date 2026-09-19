package br.com.motiva.domain;

public class CrescimentoSeco extends ComportamentoCrescimento {

    private static final double TAXA_CRESCIMENTO = 0.15;

    @Override
    public double calcularIncremento(TrechoRodovia trecho) {
        return TAXA_CRESCIMENTO;
    }
}
