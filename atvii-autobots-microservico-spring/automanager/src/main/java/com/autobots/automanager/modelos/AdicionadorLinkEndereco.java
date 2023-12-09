package com.autobots.automanager.modelos;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

import com.autobots.automanager.controles.EnderecoControle;
import com.autobots.automanager.entidades.Endereco;

@Component
public class AdicionadorLinkEndereco implements AdicionadorLink<Endereco> {

    @Override
    public void adicionarLink(List<Endereco> lista) {
        for (Endereco endereco : lista) {
            long id = endereco.getId();
            adicionarLinks(endereco, id);
        }
    }

    @Override
    public void adicionarLink(Endereco objeto) {
        long id = objeto.getId();
        adicionarLinks(objeto, id);
    }

    private void adicionarLinks(Endereco endereco, long id) {
        Link linkDetalhes = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).obterEnderecoPorId(id))
                .withRel("detalhes");
        endereco.add(linkDetalhes);

        Link linkAtualizar = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).atualizarEndereco(endereco))
                .withRel("atualizar");
        endereco.add(linkAtualizar);

        Link linkExcluir = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).excluirEndereco(endereco))
                .withRel("excluir");
        endereco.add(linkExcluir);

    }
}