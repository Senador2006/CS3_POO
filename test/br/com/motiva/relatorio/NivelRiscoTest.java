package br.com.motiva.relatorio;

import br.com.motiva.util.Assert;

public class NivelRiscoTest {

    public static void executar() {
        deveClassificarPrioridades();
        System.out.println("  OK NivelRiscoTest");
    }

    private static void deveClassificarPrioridades() {
        Assert.assertEquals(NivelRisco.CRITICO, NivelRisco.aPartirDaPrioridade(75), "Score alto deve ser CRÍTICO");
        Assert.assertEquals(NivelRisco.ALTO, NivelRisco.aPartirDaPrioridade(40), "Score médio deve ser ALTO");
        Assert.assertEquals(NivelRisco.MODERADO, NivelRisco.aPartirDaPrioridade(20), "Score baixo deve ser MODERADO");
    }
}
