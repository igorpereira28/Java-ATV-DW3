package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ControleUsuario;
import com.autobots.automanager.entidades.Usuario;

@Component
public class AdicionadorLinkUsuario implements AdicionadorLink<Usuario> {

    @Override
    public void adicionarLink(Usuario objeto) {
        adicionarLinkUsuario(objeto);
    }

    @Override
    public void adicionarLink(List<Usuario> lista) {
        for (Usuario usuario : lista) {
            adicionarLinkUsuario(usuario);
        }
    }

    private void adicionarLinkUsuario(Usuario usuario) {
        long id = usuario.getId();

        Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ControleUsuario.class).obterUsuario(id))
                .withSelfRel().withType("GET").withRel("self").withTitle("Obter detalhes do usuario");

        Link linkListaUsuarios = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ControleUsuario.class).obterUsuarios()).withRel("todosUsuarios")
                .withType("GET").withTitle("Obter lista de usuarios");

        Link linkNovoUsuario = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ControleUsuario.class).cadastrarUsuario(new Usuario()))
                .withRel("novoUsuario").withType("POST").withTitle("Criar um novo usuario");

        Link linkAtualizar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ControleUsuario.class).atualizarUsuario(usuario))
                .withRel("atualizar").withType("PUT").withTitle("Atualizar detalhes do usuario");

        Link linkExcluir = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ControleUsuario.class).excluirUsuario(usuario)).withRel("excluir")
                .withType("DELETE").withTitle("Excluir usuario");

        usuario.addLinks(linkProprio, linkListaUsuarios, linkNovoUsuario, linkAtualizar, linkExcluir);
    }

    public void adicionarLinkListaUsuarios(List<Usuario> usuarios) {
        Link linkListaUsuarios = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ControleUsuario.class).obterUsuarios()).withRel("usuarios")
                .withType("GET").withTitle("Obter lista de usuarios");

        Link linkNovoUsuario = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ControleUsuario.class).cadastrarUsuario(new Usuario()))
                .withRel("novoUsuario").withType("POST").withTitle("Criar um novo usuario");

        usuarios.forEach(usuario -> usuario.addLinks(linkListaUsuarios, linkNovoUsuario));
    }
}