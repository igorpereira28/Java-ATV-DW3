package com.autobots.automanager.modelos;

import com.autobots.automanager.entitades.Servico;

public class ServicoAtualizador {
    private StringVerificadorNulo stringVerificador = new StringVerificadorNulo();

    public void atualizar(Servico servico, Servico atualizacao) {
        if (atualizacao != null) {
            if (stringVerificador.verificar(atualizacao.getNome())) {
                servico.setNome(atualizacao.getNome());
            }
            if (atualizacao.getValor() > 0) {
                servico.setValor(atualizacao.getValor());
            }
            if (stringVerificador.verificar(atualizacao.getDescricao())) {
                servico.setDescricao(atualizacao.getDescricao());
            }
        }
    }
}