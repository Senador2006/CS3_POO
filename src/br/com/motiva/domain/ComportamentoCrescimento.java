package br.com.motiva.domain;

public abstract class ComportamentoCrescimento {

    public abstract double calcularIncremento(TrechoRodovia trecho);

    public void aplicar(TrechoRodovia trecho) {
        trecho.registrarAltura(trecho.getAlturaVegetacao() + calcularIncremento(trecho));
    }
}
