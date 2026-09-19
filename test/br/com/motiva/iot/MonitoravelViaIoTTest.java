package br.com.motiva.iot;

import br.com.motiva.domain.CrescimentoSeco;
import br.com.motiva.domain.TrechoRodoviaMonitorado;
import br.com.motiva.util.Assert;

public class MonitoravelViaIoTTest {

    static class SensorMock implements MonitoravelViaIoT {

        private final double alturaCapturada;

        SensorMock(double alturaCapturada) {
            this.alturaCapturada = alturaCapturada;
        }

        @Override
        public double transmitirDadosSensor() {
            return alturaCapturada;
        }
    }

    public static void executar() {
        mockDeveTransmitirDadosDeCrescimento();
        trechoMonitoradoDeveAtualizarAlturaComDadosDoSensor();
        System.out.println("  OK MonitoravelViaIoTTest");
    }

    private static void mockDeveTransmitirDadosDeCrescimento() {
        MonitoravelViaIoT sensor = new SensorMock(1.42);
        Assert.assertEquals(1.42, sensor.transmitirDadosSensor(), "Mock deve transmitir altura capturada");
    }

    private static void trechoMonitoradoDeveAtualizarAlturaComDadosDoSensor() {
        TrechoRodoviaMonitorado trecho = new TrechoRodoviaMonitorado(
                50, 0.60, new CrescimentoSeco(), false, false, 1.30);

        trecho.sincronizarComSensor();

        Assert.assertEquals(1.30, trecho.getAlturaVegetacao(), "Trecho deve sincronizar com sensor IoT");
    }
}
