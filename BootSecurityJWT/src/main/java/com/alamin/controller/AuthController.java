package com.alamin.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alamin.dto.AuthResponseDTO;
import com.alamin.dto.LoginDto;
import com.alamin.dto.RegisterDto;
import com.alamin.models.Role;
import com.alamin.models.UserEntity;
import com.alamin.repository.RoleRepository;
import com.alamin.repository.UserRepository;
import com.alamin.security.JwtGenerator;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtGenerator jwtGenerator;
	
	@RequestMapping("login")
	public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDto loginDto){
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDto.getUsername(), 
						loginDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtGenerator.generateToken(authentication);
		return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
	}
	
	@PostMapping("register")
	public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
		if(userRepository.existsByUsername(registerDto.getUsername())) {
			return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
		}
		
		UserEntity user = new UserEntity();
		user.setUsername(registerDto.getUsername());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		
		Role roles = roleRepository.findByName("USER").get();
		user.setRoles(Collections.singletonList(roles));
		
		userRepository.save(user);
		
		return new ResponseEntity<>("User registered success!", HttpStatus.CREATED);
	}

}
