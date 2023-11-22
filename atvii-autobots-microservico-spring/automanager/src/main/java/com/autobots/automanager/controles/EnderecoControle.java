package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelo.EnderecoAtualizador;
import com.autobots.automanager.modelo.EnderecoSelecionador;
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
	public Endereco obterEndereco(@PathVariable long id) {
		List<Endereco> enderecos = repositorio.findAll();
		return selecionador.selecionar(enderecos, id);
	}

	@GetMapping
	public List<Endereco> obterEnderecos() {
		List<Endereco> enderecos = repositorio.findAll();
		return enderecos;
	}

	@PostMapping("/cadastro/{idCliente}")
	public ResponseEntity<String> cadastrarEndereco(@RequestBody Endereco endereco, @PathVariable Long idCliente) {
	    Optional<Cliente> clienteOptional = clienteRepositorio.findById(idCliente);

	    if (clienteOptional.isPresent()) {
	        Cliente cliente = clienteOptional.get();

	        // Verifica se o cliente já possui um endereço
	        if (cliente.getEndereco() != null) {
	            return ResponseEntity.badRequest().body("Já existe um endereço cadastrado para este cliente.");
	        }

	        // Associa o endereço ao cliente e salva
	        cliente.setEndereco(endereco);
	        clienteRepositorio.save(cliente);

	        return ResponseEntity.ok("Endereço cadastrado com sucesso.");
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}

	@PutMapping("/atualizar")
	public void atualizarEndereco(@RequestBody Endereco atualizacao) {
		Endereco endereco = repositorio.getById(atualizacao.getId());
		EnderecoAtualizador atualizador = new EnderecoAtualizador();
		atualizador.atualizar(endereco, atualizacao);
		repositorio.save(endereco);
	}

	@DeleteMapping("/excluir")
	public void excluirTelefone(@RequestBody Endereco exclusao) {
	    Cliente cliente = clienteRepositorio.findByEnderecoId(exclusao.getId()); // Supondo que você tenha um método findByEnderecoId no repositorioCliente
	    cliente.setEndereco(null);
	    clienteRepositorio.save(cliente);
	    repositorio.delete(exclusao);
	}

}
