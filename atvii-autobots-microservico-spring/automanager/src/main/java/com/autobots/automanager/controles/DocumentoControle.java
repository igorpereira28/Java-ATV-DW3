package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

import javax.swing.text.Document;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelos.ClienteAtualizador;
import com.autobots.automanager.modelos.DocumentoAtualizador;
import com.autobots.automanager.modelos.DocumentoSelecionador;
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
    public ResponseEntity<List<Documento>> obterTodosDocumentos() {
		List<Documento> documentos = repositorio.findAll();
		if (documentos.isEmpty()) {
			ResponseEntity<List<Documento>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			ResponseEntity<List<Documento>> resposta = new ResponseEntity<>(documentos, HttpStatus.FOUND);
			return resposta;
		}
    }

    @GetMapping("/{id}")
    public ResponseEntity<Documento> obterDocumentoPorId(@PathVariable long id) {
		List<Documento> documentos = repositorio.findAll();
		Documento documento = selecionador.selecionar(documentos, id);
		if (documento == null) {
			ResponseEntity<Documento> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			ResponseEntity<Documento> resposta = new ResponseEntity<Documento>(documento, HttpStatus.FOUND);
			return resposta;
		}
    }
	
    @PostMapping("/cadastro/{idCliente}")
    public ResponseEntity<?> cadastrarDocumento(@RequestBody Documento documento, @PathVariable long idCliente) {
    	HttpStatus status = HttpStatus.CONFLICT;
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
        if (documento.getId() == null) {
        	repositorioClientes.save(cliente);
        	status = HttpStatus.CREATED;
        }

        return new ResponseEntity<>(status);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarDocumento(@RequestBody Documento atualizacao) {
    	HttpStatus status = HttpStatus.CONFLICT;
    	// Verificar se já existe um documento com o novo número
        Documento documentoExistente = repositorio.findByNumero(atualizacao.getNumero());

        if (documentoExistente != null && !documentoExistente.getId().equals(atualizacao.getId())) {
            // Já existe um documento com o mesmo número, não atualizar
            return ResponseEntity.badRequest().body("Já existe um documento com o mesmo número. Não é possível atualizar.");
        }

        // Continuar com a atualização se não houver conflito de números
        Documento documento = repositorio.getById(atualizacao.getId());

        if (documento != null) {
            DocumentoAtualizador atualizador = new DocumentoAtualizador();
            atualizador.atualizar(documento, atualizacao);
            repositorio.save(documento);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
    }

    @DeleteMapping("/excluir")
    public ResponseEntity<?> excluirDocumento(@RequestBody Documento exclusao) {
    	HttpStatus status = HttpStatus.BAD_REQUEST;
        // Buscar o documento a ser excluído
        Documento documento = repositorio.getById(exclusao.getId());

        if (documento != null) {
        	// Percorrer todos os clientes e remover o documento da lista de documentos
            for (Cliente cliente : repositorioClientes.findAll()) {
                List<Documento> documentosDoCliente = cliente.getDocumentos();
                documentosDoCliente.removeIf(doc -> doc.getId().equals(documento.getId()));
            }

            // Excluir o documento
            repositorio.delete(documento);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
    }
}
