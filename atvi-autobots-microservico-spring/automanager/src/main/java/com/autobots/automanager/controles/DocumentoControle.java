package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {
    @Autowired
    private DocumentoRepositorio repositorio;

    @GetMapping
    public List<Documento> obterDocumento() {
        List<Documento> documentos = repositorio.findAll();
        return documentos;
    }

    // @GetMapping("/{id}")
    // public Documento obterDocumentosIndividual(@PathVariable long id) {
    //     List<Documento> documentos = repositorio.findAll();
    //     return selecionador.selecionar(cliente, id)
    // }
}
