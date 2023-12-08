package com.autobots.automanager.entitades;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.hateoas.Link;

import lombok.Data;

@Data
@Entity
public class Empresa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String razaoSocial;
	@Column
	private String nomeFantasia;
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Telefone> telefones = new HashSet<>();
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Endereco endereco;
	@Column(nullable = false)
	private Date cadastro;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Usuario> usuarios = new HashSet<>();
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Mercadoria> mercadorias = new HashSet<>();
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Servico> servicos = new HashSet<>();
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Venda> vendas = new HashSet<>();
	
	@Transient
    private List<Link> links = new ArrayList<>();

    public void addLinks(Link... newLinks) {
        for (Link link : newLinks) {
            links.add(link);
        }
    }
}