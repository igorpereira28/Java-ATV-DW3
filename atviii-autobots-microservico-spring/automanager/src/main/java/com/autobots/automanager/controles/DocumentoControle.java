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

import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.modelos.DocumentoAtualizador;
import com.autobots.automanager.modelos.DocumentoSelecionador;
import com.autobots.automanager.repositorios.RepositorioDocumento;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.modelos.AdicionadorLink;
import com.autobots.automanager.modelos.AdicionadorLinkDocumento;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {
    @Autowired
    private RepositorioDocumento repositorio;
    
    @Autowired
	private DocumentoSelecionador selecionador;
    
    @Autowired
    private RepositorioUsuario repositorioUsuarios;
    
    @Autowired
	private AdicionadorLinkDocumento adicionadorLink;

    @GetMapping
    public ResponseEntity<List<Documento>> obterTodosDocumentos() {
		List<Documento> documentos = repositorio.findAll();
		if (documentos.isEmpty()) {
			ResponseEntity<List<Documento>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(documentos);
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
			adicionadorLink.adicionarLink(documento);
			ResponseEntity<Documento> resposta = new ResponseEntity<Documento>(documento, HttpStatus.FOUND);
			return resposta;
		}
    }
	
    @PostMapping("/cadastro/{idUsuario}")
    public ResponseEntity<?> cadastrarDocumento(@RequestBody Documento documento, @PathVariable long idUsuario) {
    	HttpStatus status = HttpStatus.CONFLICT;
        // Obtém o usuario associado ao ID fornecido
        Optional<Usuario> usuarioOptional = repositorioUsuarios.findById(idUsuario);

        // Verifica se o usuario existe
        if (!usuarioOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Usuario não encontrado.");
        }

        Usuario usuario = usuarioOptional.get();

        // Cria um novo documento
        Documento novoDocumento = new Documento();
        novoDocumento.setTipo(documento.getTipo());
        novoDocumento.setNumero(documento.getNumero());

        // Salva o documento no repositório
        Documento documentoSalvo = repositorio.save(novoDocumento);

        // Adiciona o documento ao usuario e salva o usuario
        usuario.getDocumentos().add(documentoSalvo);
        if (documento.getId() == null) {
        	repositorioUsuarios.save(usuario);
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
        	// Percorrer todos os usuarios e remover o documento da lista de documentos

            // Excluir o documento
            repositorio.delete(documento);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
    }
}
