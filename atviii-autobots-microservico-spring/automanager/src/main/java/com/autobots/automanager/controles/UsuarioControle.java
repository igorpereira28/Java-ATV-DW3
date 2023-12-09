package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.modelos.AdicionadorLinkDocumento;
import com.autobots.automanager.modelos.AdicionadorLinkUsuario;
import com.autobots.automanager.modelos.UsuarioAtualizador;
import com.autobots.automanager.modelos.UsuarioSelecionador;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/usuario")
public class UsuarioControle {
	
	@Autowired
	private RepositorioUsuario repositorio;
	
	@Autowired
	private UsuarioSelecionador selecionador;
	
	@Autowired
	private AdicionadorLinkUsuario adicionadorLink;
	
	@GetMapping
	public ResponseEntity<List<Usuario>> obterUsuarios() {
	    List<Usuario> usuarios = repositorio.findAll();
	    
	    if (usuarios.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    } else {
	        adicionadorLink.adicionarLink(usuarios);
	        return ResponseEntity.ok(usuarios);
	    }
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> obterUsuario(@PathVariable long id) {
	    Optional<Usuario> usuarioOptional = repositorio.findById(id);
	    
	    if (usuarioOptional.isPresent()) {
	        Usuario usuario = usuarioOptional.get();
	        adicionadorLink.adicionarLink(usuario);
	        return ResponseEntity.ok(usuario);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	
	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
	    if (usuario.getId() != null) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }

	    repositorio.save(usuario);
	    return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarUsuario(@RequestBody Usuario atualizacao) {
	    Usuario usuario = repositorio.findById(atualizacao.getId()).orElse(null);
	    if (usuario == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    UsuarioAtualizador atualizador = new UsuarioAtualizador();
	    atualizador.atualizar(usuario, atualizacao);
	    repositorio.save(usuario);

	    return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirUsuario(@RequestBody Usuario exclusao) {
	    Usuario usuario = repositorio.findById(exclusao.getId()).orElse(null);
	    if (usuario == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    repositorio.delete(usuario);
	    return new ResponseEntity<>(HttpStatus.OK);
	}
}
