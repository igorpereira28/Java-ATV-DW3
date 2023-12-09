package com.autobots.automanager.modelos;

import com.autobots.automanager.entidades.Mercadoria;

public class MercadoriaAtualizador {
    private StringVerificadorNulo stringVerificador = new StringVerificadorNulo();

    public void atualizar(Mercadoria mercadoria, Mercadoria atualizacao) {
        if (atualizacao != null) {
            if (stringVerificador.verificar(atualizacao.getNome())) {
                mercadoria.setNome(atualizacao.getNome());
            }
            if (atualizacao.getQuantidade() > 0) {
                mercadoria.setQuantidade(atualizacao.getQuantidade());
            }
            if (atualizacao.getValor() > 0) {
                mercadoria.setValor(atualizacao.getValor());
            }
            if (stringVerificador.verificar(atualizacao.getDescricao())) {
                mercadoria.setDescricao(atualizacao.getDescricao());
            }
        }
    }
}