package br.com.motiva.relatorio;

import br.com.motiva.domain.CrescimentoSeco;
import br.com.motiva.domain.CrescimentoUmido;
import br.com.motiva.domain.TipoTerreno;
import br.com.motiva.domain.TrechoRodovia;
import br.com.motiva.domain.TrechoRodoviaMonitorado;
import br.com.motiva.simulacao.SimuladorCrescimento;
import br.com.motiva.util.Assert;

public class MotorRegrasPrioridadeTest {

    private static final MotorRegrasPrioridade MOTOR = new MotorRegrasPrioridade();

    public static void executar() {
        deveGerarRelatorioComRocadaMecanizadaEManual();
        deveAtualizarTrechoMonitoradoViaIotAntesDePriorizar();
        trechoSecoComBaixaAlturaNaoDeveEntrarNoRelatorio();
        trechoUmidoDeveTerPrioridadeMaiorQueTrechoSecoComMesmaAltura();
        trechoRochosoDeveRecomendarRocadaManual();
        simuladorDeveEvoluirCrescimentoAoLongoDosDias();
        intervencaoDeveZerarContadorDeManutencao();
        System.out.println("  OK MotorRegrasPrioridadeTest");
    }

    private static void deveGerarRelatorioComRocadaMecanizadaEManual() {
        TrechoRodovia[] trechos = {
            new TrechoRodovia(10, 1.25, new CrescimentoSeco(), false, false),
            new TrechoRodovia(11, 1.30, new CrescimentoUmido(), true, false),
            new TrechoRodovia(12, 0.80, new CrescimentoSeco(), false, false)
        };

        RelatorioPrioridade relatorio = MOTOR.gerarRelatorio(trechos);

        Assert.assertTrue(relatorio.possuiIntervencoesPendentes(), "Relatório deve conter intervenções");
        Assert.assertEquals(2, relatorio.getItens().size(), "Devem existir 2 trechos prioritários");
        Assert.assertEquals(
                TipoRecomendacao.ROCADA_MANUAL,
                relatorio.getItens().get(0).getRecomendacao(),
                "Trecho com maior prioridade deve ser roçada manual");
        Assert.assertEquals(
                TipoRecomendacao.ROCADA_MECANIZADA,
                relatorio.getItens().get(1).getRecomendacao(),
                "Segundo trecho deve ser roçada mecanizada");
    }

    private static void deveAtualizarTrechoMonitoradoViaIotAntesDePriorizar() {
        TrechoRodovia[] trechos = {
            new TrechoRodoviaMonitorado(20, 0.50, new CrescimentoSeco(), false, true, 1.40)
        };

        RelatorioPrioridade relatorio = MOTOR.gerarRelatorio(trechos);

        Assert.assertEquals(1, relatorio.getItens().size(), "Trecho IoT acima do limite deve entrar no relatório");
        Assert.assertEquals(
                TipoRecomendacao.PULVERIZACAO,
                relatorio.getItens().get(0).getRecomendacao(),
                "Trecho com invasoras deve recomendar pulverização");
        Assert.assertTrue(relatorio.getItens().get(0).isMonitoradoIot(), "Item deve indicar monitoramento IoT");
    }

    private static void trechoSecoComBaixaAlturaNaoDeveEntrarNoRelatorio() {
        TrechoRodovia[] trechos = {
            new TrechoRodovia(30, 0.90, new CrescimentoSeco(), false, false)
        };

        RelatorioPrioridade relatorio = MOTOR.gerarRelatorio(trechos);

        Assert.assertFalse(relatorio.possuiIntervencoesPendentes(), "Trecho abaixo do limite não deve aparecer");
    }

    private static void trechoUmidoDeveTerPrioridadeMaiorQueTrechoSecoComMesmaAltura() {
        TrechoRodovia umido = new TrechoRodovia(1, 1.30, new CrescimentoUmido(), false, false);
        TrechoRodovia seco = new TrechoRodovia(2, 1.30, new CrescimentoSeco(), false, false);

        Assert.assertTrue(
                MOTOR.calcularPrioridade(umido) > MOTOR.calcularPrioridade(seco),
                "Trecho úmido deve ter prioridade maior que trecho seco");
    }

    private static void trechoRochosoDeveRecomendarRocadaManual() {
        TrechoRodovia trecho = new TrechoRodovia("BR-116", 90, 1.40, TipoTerreno.ROCHOSO, false, false, 10);
        Assert.assertEquals(
                TipoRecomendacao.ROCADA_MANUAL,
                MOTOR.definirRecomendacao(trecho),
                "Trecho rochoso deve recomendar roçada manual");
    }

    private static void simuladorDeveEvoluirCrescimentoAoLongoDosDias() {
        TrechoRodovia[] trechos = {
            new TrechoRodovia("BR-101", 1, 0.70, TipoTerreno.UMIDO, false, false, 0)
        };

        double alturaInicial = trechos[0].getAlturaVegetacao();
        new SimuladorCrescimento(MOTOR).simular(trechos, 5);

        Assert.assertTrue(
                trechos[0].getAlturaVegetacao() > alturaInicial,
                "Simulador deve aumentar altura da vegetação");
    }

    private static void intervencaoDeveZerarContadorDeManutencao() {
        TrechoRodovia trecho = new TrechoRodovia("BR-381", 10, 1.50, TipoTerreno.SECO, false, false, 40);
        trecho.aplicarIntervencao(0.30);
        Assert.assertEquals(0, trecho.getDiasSemManutencao(), "Intervenção deve zerar dias sem manutenção");
    }
}
