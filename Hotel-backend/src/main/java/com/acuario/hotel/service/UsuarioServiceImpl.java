package com.acuario.hotel.service;

import com.acuario.hotel.dto.AuthResponseDTO;
import com.acuario.hotel.dto.LoginRequestDTO;
import com.acuario.hotel.dto.RegisterRequestDTO;
import com.acuario.hotel.exception.BusinessException;
import com.acuario.hotel.exception.ErrorCode;
import com.acuario.hotel.model.EstadoUsuario;
import com.acuario.hotel.model.Usuario;
import com.acuario.hotel.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final com.acuario.hotel.security.JwtTokenProvider jwtTokenProvider;

	public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager,
			com.acuario.hotel.security.JwtTokenProvider jwtTokenProvider) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public AuthResponseDTO login(LoginRequestDTO loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getNombreUsuario(), loginRequest.getContrasena()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtTokenProvider.generateToken(authentication);
		Usuario usuario = usuarioRepository.findByNombreUsuario(loginRequest.getNombreUsuario())
				.orElseThrow(() -> new BusinessException("Usuario no encontrado", ErrorCode.RESOURCE_NOT_FOUND.getCode()));

		if (usuario.getEstado() != EstadoUsuario.ACTIVO) {
			throw new BusinessException("Usuario inactivo", ErrorCode.BUSINESS_RULE_VIOLATION.getCode());
		}

		return new AuthResponseDTO(token, usuario.getId(), usuario.getNombreUsuario(), usuario.getRol());
	}

	@Override
	public AuthResponseDTO register(RegisterRequestDTO registerRequest) {
		if (usuarioRepository.existsByNombreUsuario(registerRequest.getNombreUsuario())) {
			throw new BusinessException("El nombre de usuario ya existe", ErrorCode.BUSINESS_RULE_VIOLATION.getCode());
		}

		Usuario usuario = new Usuario();
		usuario.setNombreUsuario(registerRequest.getNombreUsuario());
		usuario.setContrasena(passwordEncoder.encode(registerRequest.getContrasena()));
		usuario.setRol(registerRequest.getRol());
		usuario.setEstado(EstadoUsuario.ACTIVO);

		Usuario usuarioGuardado = usuarioRepository.save(usuario);

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(registerRequest.getNombreUsuario(),
						registerRequest.getContrasena()));

		String token = jwtTokenProvider.generateToken(authentication);

		return new AuthResponseDTO(token, usuarioGuardado.getId(), usuarioGuardado.getNombreUsuario(),
				usuarioGuardado.getRol());
	}
}

