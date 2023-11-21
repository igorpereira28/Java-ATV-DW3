package com.autobots.automanager;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ClienteControle;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@SpringBootApplication
public class AutomanagerApplication {

    @Component
    public static class Runner implements ApplicationRunner {
        @Autowired
        public ClienteControle clienteRepositorio; // Agora estamos injetando diretamente o ClienteControle

        @Autowired
        public ClienteRepositorio repositorio;

        @Override
        public void run(ApplicationArguments args) throws Exception {
            // Verifica se o cliente já existe
            Cliente clienteExistente = clienteRepositorio.obterCliente(1L);

            // Cria o cliente apenas se não existir
            if (clienteExistente == null) {
                Calendar calendario = Calendar.getInstance();
                calendario.set(2002, 05, 15);

                Cliente cliente = new Cliente();
                cliente.setNome("Pedro");
                cliente.setDataCadastro(Calendar.getInstance().getTime());
                cliente.setDataNascimento(calendario.getTime());
                cliente.setSobrenome("Alcântara de Bragança e Bourbon");

                Telefone telefone = new Telefone();
                telefone.setDdd("21");
                telefone.setNumero("981234576");
                cliente.getTelefones().add(telefone);

                Endereco endereco = new Endereco();
                endereco.setEstado("Rio de Janeiro");
                endereco.setCidade("Rio de Janeiro");
                endereco.setBairro("Copacabana");
                endereco.setRua("Avenida Atlântica");
                endereco.setNumero("1702");
                endereco.setCodigoPostal("22021001");
                endereco.setInformacoesAdicionais("Hotel Copacabana palace");
                cliente.setEndereco(endereco);

                Documento rg = new Documento();
                rg.setTipo("RG");
                rg.setNumero("1500");

                Documento cpf = new Documento();
                cpf.setTipo("CPF");
                cpf.setNumero("00000000001");

                cliente.getDocumentos().add(rg);
                cliente.getDocumentos().add(cpf);

                repositorio.save(cliente);
            }
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(AutomanagerApplication.class, args);
    }
}