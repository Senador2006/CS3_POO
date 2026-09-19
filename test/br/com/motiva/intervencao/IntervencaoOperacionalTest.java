package br.com.motiva.intervencao;

import br.com.motiva.domain.CrescimentoSeco;
import br.com.motiva.domain.TrechoRodovia;
import br.com.motiva.relatorio.TipoRecomendacao;
import br.com.motiva.util.Assert;

public class IntervencaoOperacionalTest {

    public static void executar() {
        deveSerImpossivelInstanciarClasseAbstrata();
        filhasDevemExecutarServicoComComportamentoEspecifico();
        fabricaDeveCriarTodasAsIntervencoes();
        System.out.println("  OK IntervencaoOperacionalTest");
    }

    private static void deveSerImpossivelInstanciarClasseAbstrata() {
        Assert.assertTrue(
                java.lang.reflect.Modifier.isAbstract(IntervencaoOperacional.class.getModifiers()),
                "IntervencaoOperacional deve ser abstrata e não instanciável com new");
    }

    private static void filhasDevemExecutarServicoComComportamentoEspecifico() {
        TrechoRodovia trecho = new TrechoRodovia(100, 1.50, new CrescimentoSeco(), false, false);

        IntervencaoOperacional rocada = new RocadaMecanizada();
        IntervencaoOperacional pulverizacao = new Pulverizacao();
        IntervencaoOperacional manual = new RocadaManual();

        Assert.assertNotNull(rocada.executarServico(trecho), "Roçada mecanizada deve retornar mensagem");
        Assert.assertNotNull(pulverizacao.executarServico(trecho), "Pulverização deve retornar mensagem");
        Assert.assertNotNull(manual.executarServico(trecho), "Roçada manual deve retornar mensagem");
    }

    private static void fabricaDeveCriarTodasAsIntervencoes() {
        Assert.assertTrue(
                FabricaIntervencao.criar(TipoRecomendacao.ROCADA_MECANIZADA) instanceof RocadaMecanizada,
                "Fábrica deve criar roçada mecanizada");
        Assert.assertTrue(
                FabricaIntervencao.criar(TipoRecomendacao.ROCADA_MANUAL) instanceof RocadaManual,
                "Fábrica deve criar roçada manual");
        Assert.assertTrue(
                FabricaIntervencao.criar(TipoRecomendacao.PULVERIZACAO) instanceof Pulverizacao,
                "Fábrica deve criar pulverização");
    }
}
