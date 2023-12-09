package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.TelefoneControle;
import com.autobots.automanager.entidades.Telefone;

@Component
public class AdicionadorLinkTelefone implements AdicionadorLink<Telefone> {

    @Override
    public void adicionarLink(Telefone objeto) {
        adicionarLinkTelefone(objeto);
    }

    @Override
    public void adicionarLink(List<Telefone> lista) {
        for (Telefone telefone : lista) {
            adicionarLinkTelefone(telefone);
        }
    }

    private void adicionarLinkTelefone(Telefone telefone) {
        long id = telefone.getId();

        Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefonePorId(id))
                .withSelfRel().withType("GET").withRel("self").withTitle("Obter detalhes do telefone");

        Link linkListaTelefones = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTodosTelefones()).withRel("todosTelefones")
                .withType("GET").withTitle("Obter lista de telefones");

        Link linkNovoTelefone = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).cadastrarTelefone(new Telefone(), 0L))
                .withRel("novoTelefone").withType("POST").withTitle("Criar um novo telefone");

        Link linkAtualizar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).atualizarTelefone(telefone))
                .withRel("atualizar").withType("PUT").withTitle("Atualizar detalhes do telefone");

        Link linkExcluir = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).excluirTelefone(telefone)).withRel("excluir")
                .withType("DELETE").withTitle("Excluir telefone");

        telefone.addLinks(linkProprio, linkListaTelefones, linkNovoTelefone, linkAtualizar, linkExcluir);
    }

    public void adicionarLinkListaTelefones(List<Telefone> telefones) {
        Link linkListaTelefones = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTodosTelefones()).withRel("telefones")
                .withType("GET").withTitle("Obter lista de telefones");

        Link linkNovoTelefone = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).cadastrarTelefone(new Telefone(), 0L))
                .withRel("novoTelefone").withType("POST").withTitle("Criar um novo telefone");

        telefones.forEach(telefone -> telefone.addLinks(linkListaTelefones, linkNovoTelefone));
    }
}