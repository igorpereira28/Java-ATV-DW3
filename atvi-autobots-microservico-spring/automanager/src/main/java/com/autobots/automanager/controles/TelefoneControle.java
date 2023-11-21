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
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.modelo.TelefoneSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {
	@Autowired
	private TelefoneRepositorio repositorio;
	@Autowired
	private TelefoneSelecionador selecionador;
	
	@Autowired
    private ClienteRepositorio repositorioClientes;
	
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
    public List<Telefone> obterTodosTelefones() {
        return repositorio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Telefone> obterTelefonePorId(@PathVariable long id) {
        Optional<Telefone> telefoneOptional = repositorio.findById(id);

        if (telefoneOptional.isPresent()) {
            return ResponseEntity.ok(telefoneOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
	@PostMapping("/cadastro/{idCliente}")
	public ResponseEntity<String> cadastrarTelefone(@RequestBody Telefone telefone, @PathVariable long idCliente) {

	    // Obtém o cliente associado ao ID fornecido
	    Optional<Cliente> clienteOptional = repositorioClientes.findById(idCliente);

	    // Verifica se o cliente existe
	    if (!clienteOptional.isPresent()) {
	        return ResponseEntity.badRequest().body("Cliente não encontrado.");
	    }

	    Cliente cliente = clienteOptional.get();

	    // Cria um novo telefone
	    Telefone novoTelefone = new Telefone();
	    novoTelefone.setDdd(telefone.getDdd());
	    novoTelefone.setNumero(telefone.getNumero());

	    // Salva o telefone no repositório
	    Telefone telefoneSalvo = repositorio.save(novoTelefone);

	    // Adiciona o telefone ao cliente e salva o cliente
	    cliente.getTelefones().add(telefoneSalvo);
	    repositorioClientes.save(cliente);

	    return ResponseEntity.ok("Telefone cadastrado com sucesso.");
	}

	@PutMapping("/atualizar")
	public void atualizarTelefone(@RequestBody Telefone atualizacao) {
		Telefone telefone = repositorio.getById(atualizacao.getId());
		TelefoneAtualizador atualizador = new TelefoneAtualizador();
		atualizador.atualizar(telefone, atualizacao);
		repositorio.save(telefone);
	}

	@DeleteMapping("/excluir")
	public void excluirTelefone(@RequestBody Telefone exclusao) {
		Telefone telefone = repositorio.getById(exclusao.getId());
		repositorio.delete(telefone);
	}

}
