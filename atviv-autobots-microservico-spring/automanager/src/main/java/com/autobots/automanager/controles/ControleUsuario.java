package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.jwt.ProvedorJwt;
import com.autobots.automanager.modelos.UsuarioAtualizador;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
public class ControleUsuario {

	@Autowired
	private RepositorioUsuario repositorio;
	
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ProvedorJwt provedorJwt;
    
    public class TokenResponse {
        private String token;

        public TokenResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody Credencial credencial) {
        try {
            Authentication autenticacao = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credencial.getNomeUsuario(), credencial.getSenha())
            );

            UserDetails usuario = (UserDetails) autenticacao.getPrincipal();
            String nomeUsuario = usuario.getUsername();
            String jwt = provedorJwt.proverJwt(nomeUsuario);

            TokenResponse tokenResponse = new TokenResponse(jwt);
            System.out.println(jwt);
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

	@PostMapping("/cadastrar-usuario")
	public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
		BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
		try {
			Credencial credencial = new Credencial();
			credencial.setNomeUsuario(usuario.getCredencial().getNomeUsuario());
			String senha = codificador.encode(usuario.getCredencial().getSenha());
			credencial.setSenha(senha);
			usuario.setCredencial(credencial);
			repositorio.save(usuario);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/obter-usuarios")
	public ResponseEntity<List<Usuario>> obterUsuarios() {
		List<Usuario> usuarios = repositorio.findAll();
		return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.FOUND);
	}
	
	@PutMapping("/atualizar-usuario")
	public void atualizarCliente(@RequestBody Usuario atualizacao) {
		Usuario usuario = repositorio.getById(atualizacao.getId());
		UsuarioAtualizador atualizador = new UsuarioAtualizador();
		atualizador.atualizar(usuario, atualizacao);
		repositorio.save(usuario);
	}
	
	
	@DeleteMapping("/excluir-usuario")
	public void excluirCliente(@RequestBody Usuario exclusao) {
		Usuario usuario = repositorio.getById(exclusao.getId());
		repositorio.delete(usuario);
	}
}