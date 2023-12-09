package com.autobots.automanager.modelos;

import com.autobots.automanager.entitades.Email;

public class EmailAtualizador {
    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    public void atualizar(Email email, Email atualizacao) {
        if (atualizacao != null) {
            if (!verificador.verificar(atualizacao.getEndereco())) {
                email.setEndereco(atualizacao.getEndereco());
            }
        }
    }
}