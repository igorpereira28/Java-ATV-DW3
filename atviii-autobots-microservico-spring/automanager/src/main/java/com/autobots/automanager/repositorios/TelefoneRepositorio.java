package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entitades.Telefone;

public interface TelefoneRepositorio extends JpaRepository<Telefone, Long>{
    Telefone findByNumeroAndDdd(String numero, String ddd);

}
