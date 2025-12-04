package com.acuario.hotel.security;

import com.acuario.hotel.model.EstadoUsuario;
import com.acuario.hotel.model.Usuario;
import com.acuario.hotel.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;

	public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + nombreUsuario));

		if (usuario.getEstado() != EstadoUsuario.ACTIVO) {
			throw new UsernameNotFoundException("Usuario inactivo: " + nombreUsuario);
		}

		return User.builder()
				.username(usuario.getNombreUsuario())
				.password(usuario.getContrasena())
				.authorities(getAuthorities(usuario))
				.accountExpired(false)
				.accountLocked(false)
				.credentialsExpired(false)
				.disabled(usuario.getEstado() != EstadoUsuario.ACTIVO)
				.build();
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Usuario usuario) {
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));
	}
}

