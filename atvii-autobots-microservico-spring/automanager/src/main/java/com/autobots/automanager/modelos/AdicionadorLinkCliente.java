package com.autobots.automanager.modelos;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

import com.autobots.automanager.controles.ClienteControle;
import com.autobots.automanager.entidades.Cliente;

@Component
public class AdicionadorLinkCliente implements AdicionadorLink<Cliente> {

    @Override
    public void adicionarLink(List<Cliente> lista) {
        for (Cliente cliente : lista) {
            long id = cliente.getId();
            adicionarLinks(cliente, id);
        }
    }

    @Override
    public void adicionarLink(Cliente objeto) {
        long id = objeto.getId();
        adicionarLinks(objeto, id);
    }

    private void adicionarLinks(Cliente cliente, long id) {
        Link linkDetalhes = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).obterCliente(id))
                .withRel("detalhes");
        cliente.add(linkDetalhes);

        Link linkAtualizar = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).atualizarCliente(cliente))
                .withRel("atualizar");
        cliente.add(linkAtualizar);

        Link linkExcluir = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).excluirCliente(cliente))
                .withRel("excluir");
        cliente.add(linkExcluir);

    }
}