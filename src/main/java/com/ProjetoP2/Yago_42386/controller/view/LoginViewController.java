package com.ProjetoP2.Yago_42386.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginViewController {

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/acesso-negado")
	public String acessoNegado() {
		return "acesso-negado";
	}
}
