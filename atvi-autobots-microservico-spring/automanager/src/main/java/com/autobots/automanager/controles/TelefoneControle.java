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

        // Concatena DDD e número para a comparação
        String numeroCompleto = telefone.getDdd() + telefone.getNumero();

        if (cliente.getTelefones().stream().anyMatch(
                tel -> (tel.getDdd() + tel.getNumero()).equals(numeroCompleto)
        )) {
            return ResponseEntity.badRequest().body("Telefone já cadastrado.");
        }

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
    public ResponseEntity<?> atualizarTelefone(@RequestBody Telefone atualizacao) {
        // Verificar se já existe um telefone com o mesmo número e DDD
        Telefone telefoneExistente = repositorio.findByNumeroAndDdd(
                atualizacao.getNumero(), atualizacao.getDdd());

        if (telefoneExistente != null && !telefoneExistente.getId().equals(atualizacao.getId())) {
            // Já existe um telefone com a mesma combinação de DDD e número, não atualizar
            return ResponseEntity.badRequest().body("Este telefone já está cadastrado");
        }

        // Continuar com a atualização se não houver conflito de DDD e número
        Telefone telefone = repositorio.getById(atualizacao.getId());
        TelefoneAtualizador atualizador = new TelefoneAtualizador();
        atualizador.atualizar(telefone, atualizacao);
        repositorio.save(telefone);

        // Retornar uma resposta de sucesso, por exemplo, HTTP 200 OK
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/excluir")
    public void excluirTelefone(@RequestBody Telefone exclusao) {
        // Buscar o documento a ser excluído
        Telefone telefone = repositorio.getById(exclusao.getId());

        // Percorrer todos os clientes e remover o documento da lista de documentos
        for (Cliente cliente : repositorioClientes.findAll()) {
            List<Telefone> telefonesDoCliente = cliente.getTelefones();
            telefonesDoCliente.removeIf(doc -> doc.getId().equals(telefone.getId()));
        }

        // Excluir o documento
        repositorio.delete(telefone);
    }

}
