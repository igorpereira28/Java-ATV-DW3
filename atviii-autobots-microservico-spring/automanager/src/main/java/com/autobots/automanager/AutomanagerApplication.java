package com.autobots.automanager;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.autobots.automanager.enumeracoes.TipoDocumento;
import com.autobots.automanager.enumeracoes.TipoVeiculo;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@SpringBootApplication
public class AutomanagerApplication {

	public static void main(String[] args) {
        SpringApplication.run(AutomanagerApplication.class, args);
    }

    @Autowired
    public RepositorioEmpresa repositorioEmpresa;

    @Autowired
    public RepositorioUsuario repositorio;

    @Autowired
    public UsuarioControle usuarioControle;
	
    @Component
    public static class Runner implements ApplicationRunner {

        @Autowired
        private RepositorioEmpresa repositorioEmpresa;

        @Autowired
        private RepositorioUsuario repositorio;

        @Autowired
        private UsuarioControle usuarioControle;

        @Override
        @Transactional
        public void run(ApplicationArguments args) throws Exception {
        	
        	if (!repositorio.findAll().isEmpty()) {
                return;
            }
	
			Empresa empresa = new Empresa();
			empresa.setRazaoSocial("Car service toyota ltda");
			empresa.setNomeFantasia("Car service manutenção veicular");
			empresa.setCadastro(new Date());
	
			Endereco enderecoEmpresa = new Endereco();
			enderecoEmpresa.setEstado("São Paulo");
			enderecoEmpresa.setCidade("São Paulo");
			enderecoEmpresa.setBairro("Centro");
			enderecoEmpresa.setRua("Av. São João");
			enderecoEmpresa.setNumero("00");
			enderecoEmpresa.setCodigoPostal("01035-000");
	
			empresa.setEndereco(enderecoEmpresa);
	
			Telefone telefoneEmpresa = new Telefone();
			telefoneEmpresa.setDdd("011");
			telefoneEmpresa.setNumero("986454527");
	
			empresa.getTelefones().add(telefoneEmpresa);
	
			Usuario funcionario = new Usuario();
			funcionario.setNome("Pedro Alcântara de Bragança e Bourbon");
			funcionario.setNomeSocial("Dom Pedro");
			funcionario.getPerfis().add(PerfilUsuario.FUNCIONARIO);
	
			Email emailFuncionario = new Email();
			emailFuncionario.setEndereco("a@a.com");
	
			funcionario.getEmails().add(emailFuncionario);
	
			Endereco enderecoFuncionario = new Endereco();
			enderecoFuncionario.setEstado("São Paulo");
			enderecoFuncionario.setCidade("São Paulo");
			enderecoFuncionario.setBairro("Jardins");
			enderecoFuncionario.setRua("Av. São Gabriel");
			enderecoFuncionario.setNumero("00");
			enderecoFuncionario.setCodigoPostal("01435-001");
	
			funcionario.setEndereco(enderecoFuncionario);
	
			empresa.getUsuarios().add(funcionario);
	
			Telefone telefoneFuncionario = new Telefone();
			telefoneFuncionario.setDdd("011");
			telefoneFuncionario.setNumero("9854633728");
	
			funcionario.getTelefones().add(telefoneFuncionario);
	
			Documento cpf = new Documento();
			cpf.setDataEmissao(new Date());
			cpf.setNumero("856473819229");
			cpf.setTipo(TipoDocumento.CPF);
	
			funcionario.getDocumentos().add(cpf);
	
			CredencialUsuarioSenha credencialFuncionario = new CredencialUsuarioSenha();
			credencialFuncionario.setInativo(false);
			credencialFuncionario.setNomeUsuario("dompedrofuncionario");
			credencialFuncionario.setSenha("123456");
			credencialFuncionario.setCriacao(new Date());
			credencialFuncionario.setUltimoAcesso(new Date());
	
			funcionario.getCredenciais().add(credencialFuncionario);
			
			repositorioEmpresa.save(empresa);
	
		}
    }
}