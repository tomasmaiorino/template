package com.tsm.template.controller;

import com.tsm.template.dto.AuthTokenDTO;
import com.tsm.template.dto.UserDTO;
import com.tsm.template.model.User;
import com.tsm.template.service.JwtTokenUtil;
import com.tsm.template.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.groups.Default;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@Slf4j
@RequestMapping(value = "/api/v1/users")
public class UsersController extends BaseController {

	@Autowired
	private UserService service;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@RequestMapping(method = POST, consumes = JSON_VALUE, produces = JSON_VALUE, value = "/auth")
	@ResponseStatus(OK)
	public AuthTokenDTO auth(@RequestBody final UserDTO resource) {

		log.info("Recieved a request to auth a resource email [{}] pass informed ? [{}].", resource.getEmail(),
				!StringUtils.isEmpty(resource.getPassword()));

		validate(resource, Default.class);

		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(resource.getEmail(), resource.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		final User customer = service.findByEmail(resource.getEmail());
		final String token = jwtTokenUtil.doGenerateToken(customer.getEmail());
		return new AuthTokenDTO(token);
	}

}
