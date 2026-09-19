package br.com.motiva.simulacao;

import br.com.motiva.domain.TrechoRodovia;
import br.com.motiva.relatorio.MotorRegrasPrioridade;
import br.com.motiva.relatorio.RelatorioPrioridade;

public class SimuladorCrescimento {

    private final MotorRegrasPrioridade motor;

    public SimuladorCrescimento(MotorRegrasPrioridade motor) {
        this.motor = motor;
    }

    public RelatorioPrioridade simular(TrechoRodovia[] trechos, int dias) {
        if (dias < 1) {
            return motor.gerarRelatorioAtual(trechos);
        }

        for (int dia = 1; dia <= dias; dia++) {
            motor.processarCicloDiario(trechos);
        }

        return motor.gerarRelatorioAtual(trechos);
    }
}
