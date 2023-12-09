package com.autobots.automanager.modelos;

import com.autobots.automanager.entidades.Credencial;

public class CredencialAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Credencial credencial, Credencial atualizacao) {
		if (atualizacao != null) {
			if (!verificador.verificar(atualizacao.getNomeUsuario())) {
				credencial.setNomeUsuario(atualizacao.getNomeUsuario());
			}
			if (!verificador.verificar(atualizacao.getSenha())) {
				credencial.setSenha(atualizacao.getSenha());
			}
		}
	}
}
