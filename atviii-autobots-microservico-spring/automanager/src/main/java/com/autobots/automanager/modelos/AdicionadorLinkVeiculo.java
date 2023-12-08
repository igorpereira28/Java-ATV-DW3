package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.VeiculoControle;
import com.autobots.automanager.entitades.Veiculo;

@Component
public class AdicionadorLinkVeiculo implements AdicionadorLink<Veiculo> {

    @Override
    public void adicionarLink(Veiculo objeto) {
        adicionarLinkVeiculo(objeto);
    }

    @Override
    public void adicionarLink(List<Veiculo> lista) {
        for (Veiculo veiculo : lista) {
            adicionarLinkVeiculo(veiculo);
        }
    }

    private void adicionarLinkVeiculo(Veiculo veiculo) {
        long id = veiculo.getId();

        Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculo(id))
                .withSelfRel().withType("GET").withRel("self").withTitle("Obter detalhes do veiculo");

        Link linkListaVeiculos = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculos()).withRel("todosVeiculos")
                .withType("GET").withTitle("Obter lista de veiculos");

        Link linkNovoVeiculo = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).cadastrarVeiculo(new Veiculo()))
                .withRel("novoVeiculo").withType("POST").withTitle("Criar um novo veiculo");

        Link linkAtualizar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).atualizarVeiculo(veiculo))
                .withRel("atualizar").withType("PUT").withTitle("Atualizar detalhes do veiculo");

        Link linkExcluir = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).excluirVeiculo(veiculo)).withRel("excluir")
                .withType("DELETE").withTitle("Excluir veiculo");

        veiculo.addLinks(linkProprio, linkListaVeiculos, linkNovoVeiculo, linkAtualizar, linkExcluir);
    }

    public void adicionarLinkListaVeiculos(List<Veiculo> veiculos) {
        Link linkListaVeiculos = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculos()).withRel("veiculos")
                .withType("GET").withTitle("Obter lista de veiculos");

        Link linkNovoVeiculo = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).cadastrarVeiculo(new Veiculo()))
                .withRel("novoVeiculo").withType("POST").withTitle("Criar um novo veiculo");

        veiculos.forEach(veiculo -> veiculo.addLinks(linkListaVeiculos, linkNovoVeiculo));
    }
}