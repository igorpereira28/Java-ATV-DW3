package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ServicoControle;
import com.autobots.automanager.entidades.Servico;

@Component
public class AdicionadorLinkServico implements AdicionadorLink<Servico> {

    @Override
    public void adicionarLink(Servico objeto) {
        adicionarLinkServico(objeto);
    }

    @Override
    public void adicionarLink(List<Servico> lista) {
        for (Servico servico : lista) {
            adicionarLinkServico(servico);
        }
    }

    private void adicionarLinkServico(Servico servico) {
        long id = servico.getId();

        Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServico(id))
                .withSelfRel().withType("GET").withRel("self").withTitle("Obter detalhes do servico");

        Link linkListaServicos = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServicos())
                .withRel("todosServicos").withType("GET").withTitle("Obter lista de servicos");

        Link linkNovoServico = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).cadastrarServico(new Servico()))
                .withRel("novoServico").withType("POST").withTitle("Criar um novo servico");

        Link linkAtualizar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).atualizarServico(servico))
                .withRel("atualizar").withType("PUT").withTitle("Atualizar detalhes do servico");

        Link linkExcluir = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).excluirServico(servico)).withRel("excluir")
                .withType("DELETE").withTitle("Excluir servico");

        servico.addLinks(linkProprio, linkListaServicos, linkNovoServico, linkAtualizar, linkExcluir);
    }

    public void adicionarLinkListaServicos(List<Servico> servicos) {
        Link linkListaServicos = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServicos()).withRel("servicos")
                .withType("GET").withTitle("Obter lista de servicos");

        Link linkNovoServico = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).cadastrarServico(new Servico()))
                .withRel("novoServico").withType("POST").withTitle("Criar um novo servico");

        servicos.forEach(servico -> servico.addLinks(linkListaServicos, linkNovoServico));
    }
}