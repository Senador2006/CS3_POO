package br.com.motiva.domain;

import br.com.motiva.iot.MonitoravelViaIoT;

public class TrechoRodoviaMonitorado extends TrechoRodovia implements MonitoravelViaIoT {

    private double leituraSensor;

    public TrechoRodoviaMonitorado(
            int km,
            double alturaVegetacao,
            ComportamentoCrescimento comportamentoCrescimento,
            boolean acessoDificil,
            boolean infestacaoInvasora,
            double leituraSensor) {
        super(km, alturaVegetacao, comportamentoCrescimento, acessoDificil, infestacaoInvasora);
        this.leituraSensor = leituraSensor;
    }

    public TrechoRodoviaMonitorado(
            String rodovia,
            int km,
            double alturaVegetacao,
            TipoTerreno tipoTerreno,
            boolean acessoDificil,
            boolean infestacaoInvasora,
            int diasSemManutencao,
            double leituraSensor) {
        super(rodovia, km, alturaVegetacao, tipoTerreno, acessoDificil, infestacaoInvasora, diasSemManutencao);
        this.leituraSensor = leituraSensor;
    }

    @Override
    public boolean possuiMonitoramentoIot() {
        return true;
    }

    @Override
    public double transmitirDadosSensor() {
        return leituraSensor;
    }

    public void sincronizarComSensor() {
        registrarAltura(transmitirDadosSensor());
    }

    public void simularLeituraSensor() {
        leituraSensor += getComportamentoCrescimento().calcularIncremento(this);
        sincronizarComSensor();
        avancarDiaSemCrescimento();
    }

    @Override
    public void aplicarIntervencao(double novaAltura) {
        super.aplicarIntervencao(novaAltura);
        leituraSensor = novaAltura;
    }
}
