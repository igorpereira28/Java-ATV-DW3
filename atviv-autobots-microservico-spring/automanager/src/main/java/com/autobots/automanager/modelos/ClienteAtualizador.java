package com.autobots.automanager.modelos;

import com.autobots.automanager.entidades.Cliente;

public class ClienteAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	private void atualizarDados(Cliente cliente, Cliente atualizacao) {
		if (!verificador.verificar(atualizacao.getNome())) {
			cliente.setNome(atualizacao.getNome());
		}
		
	}
}
