package br.com.motiva.intervencao;

import br.com.motiva.domain.TrechoRodovia;

public class RocadaManual extends IntervencaoOperacional {

    @Override
    public String executarServico(TrechoRodovia trecho) {
        trecho.aplicarIntervencao(0.25);
        return String.format(
                "Equipe manual concluiu roçada no KM %d (%s) — encosta/acesso restrito tratado com segurança.",
                trecho.getKm(),
                trecho.getRodovia());
    }
}
