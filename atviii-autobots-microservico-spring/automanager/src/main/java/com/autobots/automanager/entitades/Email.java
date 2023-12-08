package com.autobots.automanager.entitades;

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
public class Email {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String endereco;
	
	@Transient
    private List<Link> links = new ArrayList<>();

    public void addLinks(Link... newLinks) {
        for (Link link : newLinks) {
            links.add(link);
        }
    }
}