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
import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.modelos.AdicionadorLinkEmail;
import com.autobots.automanager.modelos.DocumentoAtualizador;
import com.autobots.automanager.modelos.EmailAtualizador;
import com.autobots.automanager.modelos.EmailSelecionador;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioEmail;

@RestController
@RequestMapping("/email")
public class EmailControle {
	@Autowired
	private RepositorioEmail repositorio;
	@Autowired
	private EmailSelecionador selecionador;
	@Autowired
    private RepositorioUsuario usuarioRepositorio;
	@Autowired
	private AdicionadorLinkEmail adicionadorLink;

	@GetMapping("/{id}")
	public ResponseEntity<Email> obterEmailPorId(@PathVariable long id) {
		List<Email> emails = repositorio.findAll();
		Email email = selecionador.selecionar(emails, id);
		if (email == null) {
			ResponseEntity<Email> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(email);
			ResponseEntity<Email> resposta = new ResponseEntity<Email>(email, HttpStatus.FOUND);
			return resposta;
		}
	}

	@GetMapping
	public ResponseEntity<List<Email>> obterEmails() {
		List<Email> emails = repositorio.findAll();
		if (emails.isEmpty()) {
			ResponseEntity<List<Email>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(emails);
			ResponseEntity<List<Email>> resposta = new ResponseEntity<>(emails, HttpStatus.FOUND);
			return resposta;
		}
	}

	@PostMapping("/cadastro/{idUsuario}")
	public ResponseEntity<?> cadastrarEmail(@RequestBody Email email, @PathVariable Long idUsuario) {
		HttpStatus status = HttpStatus.CONFLICT;
		Optional<Usuario> usuarioOptional = usuarioRepositorio.findById(idUsuario);

	    if (usuarioOptional.isPresent()) {
	        Usuario usuario = usuarioOptional.get();

	        // Verifica se o usuario já possui um endereço
	        if (usuario.getEmails() != null) {
	            return ResponseEntity.badRequest().body("Já existe um email cadastrado para este usuario.");
	        }

	        // Associa o endereço ao usuario e salva
	        usuario.setEmails(null);
	        

	        if (email.getId() == null) {
	        	usuarioRepositorio.save(usuario);
	        	status = HttpStatus.CREATED;
	        }

	        return new ResponseEntity<>(status);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarEmail(@RequestBody Email atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Email email = repositorio.getById(atualizacao.getId());
		if (email != null) {
			EmailAtualizador atualizador = new EmailAtualizador();
			atualizador.atualizar(email, atualizacao);
			repositorio.save(email);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirEmail(@RequestBody Email exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Usuario usuario = usuarioRepositorio.findByEmails(exclusao.getId());
	    if (usuario != null) {
		    usuario.setEmails(null);
		    usuarioRepositorio.save(usuario);
		    repositorio.delete(exclusao);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}

}
