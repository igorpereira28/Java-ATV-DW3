package com.autobots.automanager.entidades;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.hateoas.Link;

import lombok.Data;

@Data
@Entity
public class Endereco {
	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = true)
	private String estado;
	@Column(nullable = false)
	private String cidade;
	@Column(nullable = true)
	private String bairro;
	@Column(nullable = false)
	private String rua;
	@Column(nullable = false)
	private String numero;
	@Column(nullable = true)
	private String codigoPostal;
	@Column(unique = false, nullable = true)
	private String informacoesAdicionais;
	
	@Transient
	private List<Link> links = new ArrayList<>();
	
	public void add(Link link) {
        this.links.add(link);
    }
    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}