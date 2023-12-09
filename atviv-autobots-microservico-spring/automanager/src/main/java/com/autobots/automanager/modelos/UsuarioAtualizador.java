package com.autobots.automanager.modelos;

import com.autobots.automanager.entidades.Usuario;

public class UsuarioAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();
	private CredencialAtualizador credencialAtualizador = new CredencialAtualizador();

	private void atualizarDados(Usuario usuario, Usuario atualizacao) {
		if (!verificador.verificar(atualizacao.getNome())) {
			usuario.setNome(atualizacao.getNome());
		}
	}

	public void atualizar(Usuario usuario, Usuario atualizacao) {
		atualizarDados(usuario, atualizacao);
		credencialAtualizador.atualizar(usuario.getCredencial(), atualizacao.getCredencial());
	}
}
