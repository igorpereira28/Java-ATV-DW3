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
public class Documento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String tipo;
	@Column(unique = true)
	private String numero;
	
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