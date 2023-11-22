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

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelos.DocumentoAtualizador;
import com.autobots.automanager.modelos.EnderecoAtualizador;
import com.autobots.automanager.modelos.EnderecoSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {
	@Autowired
	private EnderecoRepositorio repositorio;
	@Autowired
	private EnderecoSelecionador selecionador;
	@Autowired
    private ClienteRepositorio clienteRepositorio;
	
    public Endereco obterEnderecoPorId(Long id) {
        Optional<Endereco> enderecoOpcional = repositorio.findById(id);
        if (enderecoOpcional.isPresent()) {
            return enderecoOpcional.get();
        } else {
            // Se o projeto não for encontrado, retorne null ou uma mensagem de erro
            return null; // Ou retorne uma mensagem de erro como uma String
        }
    }

	@GetMapping("/{id}")
	public ResponseEntity<Endereco> obterEnderecoPorId(@PathVariable long id) {
		List<Endereco> enderecos = repositorio.findAll();
		Endereco endereco = selecionador.selecionar(enderecos, id);
		if (endereco == null) {
			ResponseEntity<Endereco> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			ResponseEntity<Endereco> resposta = new ResponseEntity<Endereco>(endereco, HttpStatus.FOUND);
			return resposta;
		}
	}

	@GetMapping
	public ResponseEntity<List<Endereco>> obterEnderecos() {
		List<Endereco> enderecos = repositorio.findAll();
		if (enderecos.isEmpty()) {
			ResponseEntity<List<Endereco>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			ResponseEntity<List<Endereco>> resposta = new ResponseEntity<>(enderecos, HttpStatus.FOUND);
			return resposta;
		}
	}

	@PostMapping("/cadastro/{idCliente}")
	public ResponseEntity<?> cadastrarEndereco(@RequestBody Endereco endereco, @PathVariable Long idCliente) {
		HttpStatus status = HttpStatus.CONFLICT;
		Optional<Cliente> clienteOptional = clienteRepositorio.findById(idCliente);

	    if (clienteOptional.isPresent()) {
	        Cliente cliente = clienteOptional.get();

	        // Verifica se o cliente já possui um endereço
	        if (cliente.getEndereco() != null) {
	            return ResponseEntity.badRequest().body("Já existe um endereço cadastrado para este cliente.");
	        }

	        // Associa o endereço ao cliente e salva
	        cliente.setEndereco(endereco);
	        

	        if (endereco.getId() == null) {
	        	clienteRepositorio.save(cliente);
	        	status = HttpStatus.CREATED;
	        }

	        return new ResponseEntity<>(status);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarEndereco(@RequestBody Endereco atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Endereco endereco = repositorio.getById(atualizacao.getId());
		if (endereco != null) {
			EnderecoAtualizador atualizador = new EnderecoAtualizador();
			atualizador.atualizar(endereco, atualizacao);
			repositorio.save(endereco);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirTelefone(@RequestBody Endereco exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Cliente cliente = clienteRepositorio.findByEnderecoId(exclusao.getId()); // Supondo que você tenha um método findByEnderecoId no repositorioCliente
	    if (cliente != null) {
		    cliente.setEndereco(null);
		    clienteRepositorio.save(cliente);
		    repositorio.delete(exclusao);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}

}
