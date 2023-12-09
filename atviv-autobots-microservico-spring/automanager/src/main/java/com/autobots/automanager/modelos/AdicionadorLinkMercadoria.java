package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.MercadoriaControle;
import com.autobots.automanager.entidades.Mercadoria;

@Component
public class AdicionadorLinkMercadoria implements AdicionadorLink<Mercadoria> {

    @Override
    public void adicionarLink(Mercadoria objeto) {
        adicionarLinkMercadoria(objeto);
    }

    @Override
    public void adicionarLink(List<Mercadoria> lista) {
        for (Mercadoria mercadoria : lista) {
            adicionarLinkMercadoria(mercadoria);
        }
    }

    private void adicionarLinkMercadoria(Mercadoria mercadoria) {
        long id = mercadoria.getId();

        Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadoria(id))
                .withSelfRel().withType("GET").withRel("self").withTitle("Obter detalhes do mercadoria");

        Link linkListaMercadorias = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadorias())
                .withRel("todosMercadorias").withType("GET").withTitle("Obter lista de mercadorias");

        Link linkNovoMercadoria = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).cadastrarMercadoria(new Mercadoria()))
                .withRel("novoMercadoria").withType("POST").withTitle("Criar um novo mercadoria");

        Link linkAtualizar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).atualizarMercadoria(mercadoria))
                .withRel("atualizar").withType("PUT").withTitle("Atualizar detalhes do mercadoria");

        Link linkExcluir = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).excluirMercadoria(mercadoria)).withRel("excluir")
                .withType("DELETE").withTitle("Excluir mercadoria");

        mercadoria.addLinks(linkProprio, linkListaMercadorias, linkNovoMercadoria, linkAtualizar, linkExcluir);
    }

    public void adicionarLinkListaMercadorias(List<Mercadoria> mercadorias) {
        Link linkListaMercadorias = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadorias()).withRel("mercadorias")
                .withType("GET").withTitle("Obter lista de mercadorias");

        Link linkNovoMercadoria = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).cadastrarMercadoria(new Mercadoria()))
                .withRel("novoMercadoria").withType("POST").withTitle("Criar um novo mercadoria");

        mercadorias.forEach(mercadoria -> mercadoria.addLinks(linkListaMercadorias, linkNovoMercadoria));
    }
}