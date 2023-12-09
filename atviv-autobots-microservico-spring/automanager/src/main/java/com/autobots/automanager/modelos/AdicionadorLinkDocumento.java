package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.DocumentoControle;
import com.autobots.automanager.entidades.Documento;

@Component
public class AdicionadorLinkDocumento implements AdicionadorLink<Documento> {

    @Override
    public void adicionarLink(Documento objeto) {
        adicionarLinkDocumento(objeto);
    }

    @Override
    public void adicionarLink(List<Documento> lista) {
        for (Documento documento : lista) {
            adicionarLinkDocumento(documento);
        }
    }

    private void adicionarLinkDocumento(Documento documento) {
        long id = documento.getId();

        Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumentoPorId(id))
                .withSelfRel().withType("GET").withRel("self").withTitle("Obter detalhes do documento");

        Link linkListaDocumentos = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterTodosDocumentos())
                .withRel("todosDocumentos").withType("GET").withTitle("Obter lista de clientes");

        Link linkNovoDocumento = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).cadastrarDocumento(new Documento(), 0)) // Corrigido aqui
                .withRel("novoDocumento").withType("POST").withTitle("Criar um novo documento");

        Link linkAtualizar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).atualizarDocumento(documento))
                .withRel("atualizar").withType("PUT").withTitle("Atualizar detalhes do documento");

        Link linkExcluir = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).excluirDocumento(documento)).withRel("excluir")
                .withType("DELETE").withTitle("Excluir documento");

        documento.addLinks(linkProprio, linkListaDocumentos, linkNovoDocumento, linkAtualizar, linkExcluir);
    }

    public void adicionarLinkListaDocumentos(List<Documento> clientes) {
        Link linkListaDocumentos = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterTodosDocumentos()).withRel("documentos")
                .withType("GET").withTitle("Obter lista de documentos");

        Link linkNovoDocumento = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).cadastrarDocumento(new Documento(), 0)) // Corrigido aqui
                .withRel("novoDocumento").withType("POST").withTitle("Criar um novo documento");

        clientes.forEach(documento -> documento.addLinks(linkListaDocumentos, linkNovoDocumento));
    }
}