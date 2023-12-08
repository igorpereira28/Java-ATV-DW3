package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.modelos.AdicionadorLinkVenda;
import com.autobots.automanager.modelos.VendaSelecionador;
import com.autobots.automanager.repositorios.RepositorioVenda;
import com.autobots.automanager.repositorios.RepositorioUsuario;

public class VendaControle {
	@Autowired
	private RepositorioVenda repositorio;
	
	@Autowired
	private RepositorioUsuario repositorioUsuario;
	
	@Autowired
	private VendaSelecionador selecionador;
	
	@Autowired
	private AdicionadorLinkVenda adicionadorLink;
	
	@GetMapping
	public ResponseEntity<List<Venda>> obterVendas(){
		List<Venda> vendas = repositorio.findAll();
		if (vendas.isEmpty()) {
			ResponseEntity<List<Venda>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(vendas);
			ResponseEntity<List<Venda>> resposta = new ResponseEntity<>(vendas, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Venda> obterVenda(@PathVariable long id) {
		List<Venda> vendas = repositorio.findAll();
		Venda venda = selecionador.selecionar(vendas, id);
		if (venda == null) {
			ResponseEntity<Venda> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(venda);
			ResponseEntity<Venda> resposta = new ResponseEntity<Venda>(venda, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarVenda(@RequestBody Venda venda) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (venda.getId() == null) {
			repositorio.save(venda);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);

	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirVenda(@RequestBody Venda exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Venda venda = repositorio.getById(exclusao.getId());
		if (venda != null) {
			repositorio.delete(venda);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}
