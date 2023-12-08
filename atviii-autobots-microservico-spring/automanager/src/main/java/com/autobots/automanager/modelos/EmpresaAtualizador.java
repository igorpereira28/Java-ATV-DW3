package com.autobots.automanager.modelos;

import com.autobots.automanager.entitades.Empresa;

public class EmpresaAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();
	private EnderecoAtualizador enderecoAtualizador = new EnderecoAtualizador();
	private TelefoneAtualizador telefoneAtualizador = new TelefoneAtualizador();

	private void atualizarDados(Empresa empresa, Empresa atualizacao) {
		if (!verificador.verificar(atualizacao.getNomeFantasia())) {
			empresa.setNomeFantasia(atualizacao.getNomeFantasia());
		}
		if (!verificador.verificar(atualizacao.getRazaoSocial())) {
			empresa.setRazaoSocial(atualizacao.getRazaoSocial());
		}

	}
	
	public void atualizar(Empresa empresa, Empresa atualizacao) {
		atualizarDados(empresa, atualizacao);
		enderecoAtualizador.atualizar(empresa.getEndereco(), atualizacao.getEndereco());
		telefoneAtualizador.atualizar(empresa.getTelefones(), atualizacao.getTelefones());
	}


}