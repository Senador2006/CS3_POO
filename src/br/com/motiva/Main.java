package br.com.motiva;

import br.com.motiva.domain.TrechoRodovia;

public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--demo")) {
            executarDemonstracaoAutomatica();
            return;
        }

        ConsoleApp app = new ConsoleApp(CatalogoRodovias.criarCenarioDemonstracao());
        app.iniciar();
    }

    private static void executarDemonstracaoAutomatica() {
        TrechoRodovia[] trechos = CatalogoRodovias.criarCenarioDemonstracao();
        var motor = new br.com.motiva.relatorio.MotorRegrasPrioridade();
        var simulador = new br.com.motiva.simulacao.SimuladorCrescimento(motor);
        var formatador = new br.com.motiva.relatorio.FormatadorRelatorio();

        System.out.println(formatador.formatarPanorama(trechos));
        System.out.println();

        var relatorio = simulador.simular(trechos, 7);
        System.out.println("Após 7 dias de crescimento:\n");
        System.out.println(formatador.formatar(relatorio));
        System.out.println();

        System.out.println("--- Despacho automático ---");
        for (var item : relatorio.getItens()) {
            var intervencao = br.com.motiva.intervencao.FabricaIntervencao.criar(item.getRecomendacao());
            for (var trecho : trechos) {
                if (trecho.getKm() == item.getKm() && trecho.getRodovia().equals(item.getRodovia())) {
                    System.out.println("→ " + intervencao.executarServico(trecho));
                    break;
                }
            }
        }
    }
}
