package com.autobots.automanager.modelos;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

import com.autobots.automanager.controles.DocumentoControle;
import com.autobots.automanager.entidades.Documento;

@Component
public class AdicionadorLinkDocumento implements AdicionadorLink<Documento> {

    @Override
    public void adicionarLink(List<Documento> lista) {
        for (Documento documento : lista) {
            long id = documento.getId();
            adicionarLinks(documento, id);
        }
    }

    @Override
    public void adicionarLink(Documento objeto) {
        long id = objeto.getId();
        adicionarLinks(objeto, id);
    }

    private void adicionarLinks(Documento documento, long id) {
        Link linkDetalhes = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumentoPorId(id))
                .withRel("detalhes");
        documento.add(linkDetalhes);

        Link linkAtualizar = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).atualizarDocumento(documento))
                .withRel("atualizar");
        documento.add(linkAtualizar);

        Link linkExcluir = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).excluirDocumento(documento))
                .withRel("excluir");
        documento.add(linkExcluir);

    }
}