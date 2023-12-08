package com.autobots.automanager.modelos;

import com.autobots.automanager.entitades.Usuario;

public class UsuarioAtualizador {
    private StringVerificadorNulo stringVerificador = new StringVerificadorNulo();
    private EnderecoAtualizador enderecoAtualizador = new EnderecoAtualizador();
    private TelefoneAtualizador telefoneAtualizador = new TelefoneAtualizador();

    private void atualizarDados(Usuario usuario, Usuario atualizacao) {
        if (stringVerificador.verificar(atualizacao.getNome())) {
            usuario.setNome(atualizacao.getNome());
        }
        if (stringVerificador.verificar(atualizacao.getNomeSocial())) {
            usuario.setNomeSocial(atualizacao.getNomeSocial());
        }
    }

    public void atualizar(Usuario usuario, Usuario atualizacao) {
        if (atualizacao != null) {
            atualizarDados(usuario, atualizacao);

            if (atualizacao.getPerfis() != null && !atualizacao.getPerfis().isEmpty()) {
                usuario.getPerfis().clear();
                usuario.getPerfis().addAll(atualizacao.getPerfis());
            }

            if (atualizacao.getTelefones() != null && !atualizacao.getTelefones().isEmpty()) {
                usuario.getTelefones().clear();
                usuario.getTelefones().addAll(atualizacao.getTelefones());
            }

            if (atualizacao.getEndereco() != null) {
                enderecoAtualizador.atualizar(usuario.getEndereco(), atualizacao.getEndereco());
            }

            if (atualizacao.getDocumentos() != null && !atualizacao.getDocumentos().isEmpty()) {
                usuario.getDocumentos().clear();
                usuario.getDocumentos().addAll(atualizacao.getDocumentos());
            }

            if (atualizacao.getEmails() != null && !atualizacao.getEmails().isEmpty()) {
                usuario.getEmails().clear();
                usuario.getEmails().addAll(atualizacao.getEmails());
            }

            if (atualizacao.getCredenciais() != null && !atualizacao.getCredenciais().isEmpty()) {
                usuario.getCredenciais().clear();
                usuario.getCredenciais().addAll(atualizacao.getCredenciais());
            }
        }
    }
}