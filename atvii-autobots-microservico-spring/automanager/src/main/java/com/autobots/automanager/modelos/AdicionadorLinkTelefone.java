package com.autobots.automanager.modelos;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

import com.autobots.automanager.controles.EnderecoControle;
import com.autobots.automanager.controles.TelefoneControle;
import com.autobots.automanager.entidades.Telefone;

@Component
public class AdicionadorLinkTelefone implements AdicionadorLink<Telefone> {

    @Override
    public void adicionarLink(List<Telefone> lista) {
        for (Telefone telefone : lista) {
            long id = telefone.getId();
            adicionarLinks(telefone, id);
        }
    }

    @Override
    public void adicionarLink(Telefone objeto) {
        long id = objeto.getId();
        adicionarLinks(objeto, id);
    }

    private void adicionarLinks(Telefone telefone, long id) {
        Link linkDetalhes = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefonePorId(id))
                .withRel("detalhes");
        telefone.add(linkDetalhes);
        
        Link linkAtualizar = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).atualizarTelefone(telefone))
                .withRel("atualizar");
        telefone.add(linkAtualizar);

        Link linkExcluir = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).excluirTelefone(telefone))
                .withRel("excluir");
        telefone.add(linkExcluir);
    }
}
