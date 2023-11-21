package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

import javax.swing.text.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.modelo.DocumentoSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {
    @Autowired
    private DocumentoRepositorio repositorio;
    
    @Autowired
	private DocumentoSelecionador selecionador;
    
    @Autowired
    private ClienteRepositorio repositorioClientes;

    @GetMapping
    public List<Documento> obterTodosDocumentos() {
        return repositorio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Documento> obterDocumentoPorId(@PathVariable long id) {
        Optional<Documento> documentoOptional = repositorio.findById(id);

        if (documentoOptional.isPresent()) {
            return ResponseEntity.ok(documentoOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
    @PostMapping("/cadastro/{idCliente}")
    public ResponseEntity<String> cadastrarDocumento(@RequestBody Documento documento, @PathVariable long idCliente) {

        // Obtém o cliente associado ao ID fornecido
        Optional<Cliente> clienteOptional = repositorioClientes.findById(idCliente);

        // Verifica se o cliente existe
        if (!clienteOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Cliente não encontrado.");
        }

        Cliente cliente = clienteOptional.get();

        // Verifica se o tipo de documento é CPF e se já existe um CPF associado a este cliente
        if (documento.getTipo().equalsIgnoreCase("CPF") && cliente.getDocumentos().stream().anyMatch(doc -> doc.getTipo().equalsIgnoreCase("CPF"))) {
            return ResponseEntity.badRequest().body("Já existe um CPF cadastrado para este cliente.");
        }
        
        // Verifica se o documento já existe para este cliente
        if (cliente.getDocumentos().stream().anyMatch(doc -> doc.getTipo().equals(documento.getTipo()) && doc.getNumero().equals(documento.getNumero()))) {
            return ResponseEntity.badRequest().body("Documento já cadastrado para este cliente.");
        }

        // Cria um novo documento
        Documento novoDocumento = new Documento();
        novoDocumento.setTipo(documento.getTipo());
        novoDocumento.setNumero(documento.getNumero());

        // Salva o documento no repositório
        Documento documentoSalvo = repositorio.save(novoDocumento);

        // Adiciona o documento ao cliente e salva o cliente
        cliente.getDocumentos().add(documentoSalvo);
        repositorioClientes.save(cliente);

        return ResponseEntity.ok("Documento cadastrado com sucesso.");
    }

	@PutMapping("/atualizar")
	public void atualizarDocumento(@RequestBody Documento atualizacao) {
		Documento documento = repositorio.getById(atualizacao.getId());
		DocumentoAtualizador atualizador = new DocumentoAtualizador();
		atualizador.atualizar(documento, atualizacao);
		repositorio.save(documento);
	}

	@DeleteMapping("/excluir")
	public void excluirDocumento(@RequestBody Documento exclusao) {
		Documento documento = repositorio.getById(exclusao.getId());
		repositorio.delete(documento);
	}
}
