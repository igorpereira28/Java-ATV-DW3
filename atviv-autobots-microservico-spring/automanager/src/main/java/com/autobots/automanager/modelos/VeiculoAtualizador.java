package com.autobots.automanager.modelos;

import com.autobots.automanager.entidades.Veiculo;

public class VeiculoAtualizador {
    private StringVerificadorNulo stringVerificador = new StringVerificadorNulo();
    private UsuarioAtualizador usuarioAtualizador = new UsuarioAtualizador();

    private void atualizarDados(Veiculo veiculo, Veiculo atualizacao) {
        if (atualizacao.getTipo() != null) {
            veiculo.setTipo(atualizacao.getTipo());
        }
        if (!stringVerificador.verificar(atualizacao.getModelo())) {
            veiculo.setModelo(atualizacao.getModelo());
        }
        if (!stringVerificador.verificar(atualizacao.getPlaca())) {
            veiculo.setPlaca(atualizacao.getPlaca());
        }
    }

    public void atualizar(Veiculo veiculo, Veiculo atualizacao) {
        atualizarDados(veiculo, atualizacao);

        if (atualizacao.getProprietario() != null) {
            usuarioAtualizador.atualizar(veiculo.getProprietario(), atualizacao.getProprietario());
        }

        if (atualizacao.getVendas() != null && !atualizacao.getVendas().isEmpty()) {
            veiculo.getVendas().clear();
            veiculo.getVendas().addAll(atualizacao.getVendas());
        }
    }
}