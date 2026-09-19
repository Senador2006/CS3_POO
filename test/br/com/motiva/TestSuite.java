package br.com.motiva;

import br.com.motiva.intervencao.IntervencaoOperacionalTest;
import br.com.motiva.iot.MonitoravelViaIoTTest;
import br.com.motiva.relatorio.MotorRegrasPrioridadeTest;
import br.com.motiva.relatorio.NivelRiscoTest;

public class TestSuite {

    public static void main(String[] args) {
        System.out.println("Executando testes...");

        try {
            IntervencaoOperacionalTest.executar();
            MonitoravelViaIoTTest.executar();
            MotorRegrasPrioridadeTest.executar();
            NivelRiscoTest.executar();
        } catch (AssertionError erro) {
            System.err.println("FALHA: " + erro.getMessage());
            System.exit(1);
        }

        System.out.println("Todos os testes passaram.");
    }
}
