package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EmailControle;
import com.autobots.automanager.entitades.Email;

@Component
public class AdicionadorLinkEmail implements AdicionadorLink<Email> {

    @Override
    public void adicionarLink(Email objeto) {
        adicionarLinkEmail(objeto);
    }

    @Override
    public void adicionarLink(List<Email> lista) {
        for (Email email : lista) {
            adicionarLinkEmail(email);
        }
    }

    private void adicionarLinkEmail(Email email) {
        long id = email.getId();

        Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).obterEmailPorId(id))
                .withSelfRel().withType("GET").withRel("self").withTitle("Obter detalhes do email");

        Link linkListaEmails = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).obterEmails())
                .withRel("todosEmails").withType("GET").withTitle("Obter lista de emails");

        Link linkNovoEmail = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).cadastrarEmail(new Email(), 0L))
                .withRel("novoEmail").withType("POST").withTitle("Criar um novo email");

        Link linkAtualizar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).atualizarEmail(email))
                .withRel("atualizar").withType("PUT").withTitle("Atualizar detalhes do email");

        Link linkExcluir = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).excluirEmail(email))
                .withRel("excluir").withType("DELETE").withTitle("Excluir email");

        email.addLinks(linkProprio, linkListaEmails, linkNovoEmail, linkAtualizar, linkExcluir);
    }

    public void adicionarLinkListaEmails(List<Email> emails) {
        Link linkListaEmails = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).obterEmails()).withRel("emails")
                .withType("GET").withTitle("Obter lista de emails");

        Link linkNovoEmail = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).cadastrarEmail(new Email(), 0L))
                .withRel("novoEmail").withType("POST").withTitle("Criar um novo email");

        emails.forEach(email -> email.addLinks(linkListaEmails, linkNovoEmail));
    }
}