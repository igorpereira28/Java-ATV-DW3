package com.autobots.automanager;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ClienteControle;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@SpringBootApplication
public class AutomanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutomanagerApplication.class, args);
    }

    @Component
    public static class Runner implements ApplicationRunner {

        @Autowired
        public ClienteRepositorio repositorio;

        @Autowired
        public ClienteControle clienteRepositorio;

        @Override
        public void run(ApplicationArguments args) throws Exception {

            Cliente clienteExistente = clienteRepositorio.obterClientePorId(1L);

            // Cria o cliente apenas se não existir
            if (clienteExistente == null) {
                Calendar calendario = Calendar.getInstance();
                calendario.set(2002, 05, 15);

                Cliente cliente = new Cliente();
                cliente.setNome("Pedro AlcÃ¢ntara de BraganÃ§a e Bourbon");
                cliente.setDataCadastro(Calendar.getInstance().getTime());
                cliente.setDataNascimento(calendario.getTime());
                cliente.setSobrenome("Dom Pedro");

                Telefone telefone = new Telefone();
                telefone.setDdd("21");
                telefone.setNumero("981234576");
                cliente.getTelefones().add(telefone);

                Endereco endereco = new Endereco();
                endereco.setEstado("Rio de Janeiro");
                endereco.setCidade("Rio de Janeiro");
                endereco.setBairro("Copacabana");
                endereco.setRua("Avenida AtlÃ¢ntica");
                endereco.setNumero("1702");
                endereco.setCodigoPostal("22021001");
                endereco.setInformacoesAdicionais("Hotel Copacabana palace");
                cliente.setEndereco(endereco);

                Documento rg = new Documento();
                rg.setTipo("RG");
                rg.setNumero("1500");

                Documento cpf = new Documento();
                cpf.setTipo("RG");
                cpf.setNumero("00000000001");

                cliente.getDocumentos().add(rg);
                cliente.getDocumentos().add(cpf);

                repositorio.save(cliente);
            }
        }
    }
}