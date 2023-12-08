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
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.modelos.AdicionadorLinkTelefone;
import com.autobots.automanager.modelos.DocumentoAtualizador;
import com.autobots.automanager.modelos.TelefoneAtualizador;
import com.autobots.automanager.modelos.TelefoneSelecionador;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {
	@Autowired
	private TelefoneRepositorio repositorio;
	@Autowired
	private TelefoneSelecionador selecionador;
	
	@Autowired
    private RepositorioUsuario repositorioUsuarios;
	
	@Autowired AdicionadorLinkTelefone adicionadorLink;
	
    public Telefone obterTelefonePorId(Long id) {
        Optional<Telefone> telefoneOpcional = repositorio.findById(id);
        if (telefoneOpcional.isPresent()) {
            return telefoneOpcional.get();
        } else {
            // Se o projeto não for encontrado, retorne null ou uma mensagem de erro
            return null; // Ou retorne uma mensagem de erro como uma String
        }
    }

    @GetMapping
    public ResponseEntity<List<Telefone>> obterTodosTelefones() {
        List<Telefone> telefones = repositorio.findAll();
        if (telefones.isEmpty()) {
			ResponseEntity<List<Telefone>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(telefones);
			ResponseEntity<List<Telefone>> resposta = new ResponseEntity<>(telefones, HttpStatus.FOUND);
			return resposta;
		}
    }

    @GetMapping("/{id}")
    public ResponseEntity<Telefone> obterTelefonePorId(@PathVariable long id) {
        List<Telefone> telefones = repositorio.findAll();
		Telefone telefone = selecionador.selecionar(telefones, id);

		if (telefone == null) {
			ResponseEntity<Telefone> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(telefone);
			ResponseEntity<Telefone> resposta = new ResponseEntity<Telefone>(telefone, HttpStatus.FOUND);
			return resposta;
		}
    }
	
    @PostMapping("/cadastro/{idUsuario}")
    public ResponseEntity<String> cadastrarTelefone(@RequestBody Telefone telefone, @PathVariable long idUsuario) {
    	HttpStatus status = HttpStatus.CONFLICT;
        // Obtém o usuario associado ao ID fornecido
        Optional<Usuario> usuarioOptional = repositorioUsuarios.findById(idUsuario);

        // Verifica se o usuario existe
        if (!usuarioOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Usuario não encontrado.");
        }

        Usuario usuario = usuarioOptional.get();

        // Concatena DDD e número para a comparação
        String numeroCompleto = telefone.getDdd() + telefone.getNumero();

        // Cria um novo telefone
        Telefone novoTelefone = new Telefone();
        novoTelefone.setDdd(telefone.getDdd());
        novoTelefone.setNumero(telefone.getNumero());

        // Salva o telefone no repositório
        Telefone telefoneSalvo = repositorio.save(novoTelefone);

        // Adiciona o telefone ao usuario e salva o usuario
        usuario.getTelefones().add(telefoneSalvo);
        if (telefone.getId() == null) {
            repositorioUsuarios.save(usuario);
        	status = HttpStatus.CREATED;
        }

        return new ResponseEntity<>(status);
        
    }

    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarTelefone(@RequestBody Telefone atualizacao) {
    	HttpStatus status = HttpStatus.CONFLICT;
    	// Verificar se já existe um telefone com o mesmo número e DDD
        Telefone telefoneExistente = repositorio.findByNumeroAndDdd(
                atualizacao.getNumero(), atualizacao.getDdd());

        if (telefoneExistente != null && !telefoneExistente.getId().equals(atualizacao.getId())) {
            // Já existe um telefone com a mesma combinação de DDD e número, não atualizar
            return ResponseEntity.badRequest().body("Este telefone já está cadastrado");
        }

        // Continuar com a atualização se não houver conflito de DDD e número
        Telefone telefone = repositorio.getById(atualizacao.getId());
        if (telefone != null) {
            TelefoneAtualizador atualizador = new TelefoneAtualizador();
            atualizador.atualizar(telefone, atualizacao);
            repositorio.save(telefone);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);

    }

    @DeleteMapping("/excluir")
    public ResponseEntity<?> excluirTelefone(@RequestBody Telefone exclusao) {
    	HttpStatus status = HttpStatus.BAD_REQUEST;
    	// Buscar o documento a ser excluído
        Telefone telefone = repositorio.getById(exclusao.getId());

        if (telefone != null) {
	        // Percorrer todos os usuarios e remover o documento da lista de documentos
	
	        // Excluir o documento
	        repositorio.delete(telefone);
	        status = HttpStatus.OK;
        }
        return new ResponseEntity<>(status);
    }

}
