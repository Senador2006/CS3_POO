package br.com.motiva.domain;

public class CrescimentoRochoso extends ComportamentoCrescimento {

    private static final double TAXA_CRESCIMENTO = 0.10;

    @Override
    public double calcularIncremento(TrechoRodovia trecho) {
        return TAXA_CRESCIMENTO;
    }
}
