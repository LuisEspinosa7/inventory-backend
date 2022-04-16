package com.lsoftware.inventory.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {
	
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping("/greet")
	public String greet() {
		return "Saludos";
	}
}
