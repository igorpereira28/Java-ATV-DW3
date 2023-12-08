package com.autobots.automanager.modelos;

import java.util.List;
import java.util.Set;

import com.autobots.automanager.entitades.Telefone;

public class TelefoneAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Telefone telefone, Telefone atualizacao) {
		if (atualizacao != null) {
		    if (!verificador.verificar(atualizacao.getDdd()) && atualizacao.getDdd() != null) {
		        telefone.setDdd(atualizacao.getDdd());
		    }
		    if (!verificador.verificar(atualizacao.getNumero()) && atualizacao.getNumero() != null) {
		        telefone.setNumero(atualizacao.getNumero());
		    }
		}
	}

	public void atualizar(Set<Telefone> telefones, Set<Telefone> atualizacoes) {
        for (Telefone atualizacao : atualizacoes) {
            for (Telefone telefone : telefones) {
                if (atualizacao.getId() != null && atualizacao.getId().equals(telefone.getId())) {
                    atualizar(telefone, atualizacao);
                }
            }
        }
    }
}