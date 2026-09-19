package br.com.motiva.domain;

public class CrescimentoUmido extends ComportamentoCrescimento {

    private static final double TAXA_CRESCIMENTO = 0.35;

    @Override
    public double calcularIncremento(TrechoRodovia trecho) {
        return TAXA_CRESCIMENTO;
    }
}
