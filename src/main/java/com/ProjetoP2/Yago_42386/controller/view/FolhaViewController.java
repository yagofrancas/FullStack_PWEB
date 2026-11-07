package com.ProjetoP2.Yago_42386.controller.view;

import com.ProjetoP2.Yago_42386.model.folha_pagamento;
import com.ProjetoP2.Yago_42386.service.colaboradorService;
import com.ProjetoP2.Yago_42386.service.folha_pagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller MVC Server-Side para Folha de Pagamento (Fase 10).
 * Proteção no servidor: Acesso permitido apenas a perfis com papel RH ou ADMIN (Fase 09).
 */
@Controller
@RequestMapping("/folhas")
@PreAuthorize("hasAnyRole('RH', 'ADMIN', 'GESTOR_RH')")
public class FolhaViewController {

	private final folha_pagamentoService folhaService;
	private final colaboradorService colService;

	@Autowired
	public FolhaViewController(folha_pagamentoService folhaService, colaboradorService colService) {
		this.folhaService = folhaService;
		this.colService = colService;
	}

	@GetMapping
	public String listar(Model model) {
		model.addAttribute("folhas", folhaService.listarTodos());
		return "folhas/lista";
	}

	@GetMapping("/novo")
	public String novoFormulario(Model model) {
		folha_pagamento nova = new folha_pagamento();
		model.addAttribute("folha", nova);
		model.addAttribute("colaboradores", colService.listarTodos());
		return "folhas/formulario";
	}

	@GetMapping("/editar/{id}")
	public String editarFormulario(@PathVariable Integer id, Model model, RedirectAttributes redirectAttrs) {
		return folhaService.buscarPorId(id).map(f -> {
			model.addAttribute("folha", f);
			model.addAttribute("colaboradores", colService.listarTodos());
			return "folhas/formulario";
		}).orElseGet(() -> {
			redirectAttrs.addFlashAttribute("erro", "Folha de pagamento não encontrada com ID: " + id);
			return "redirect:/folhas";
		});
	}

	@PostMapping("/salvar")
	public String salvar(@ModelAttribute("folha") folha_pagamento folha,
						 Model model,
						 RedirectAttributes redirectAttrs) {
		try {
			if (folha.getId_folha() != null && folha.getId_folha() > 0) {
				folhaService.atualizar(folha.getId_folha(), folha);
				redirectAttrs.addFlashAttribute("sucesso", "Folha de pagamento atualizada com sucesso!");
			} else {
				folhaService.salvar(folha);
				redirectAttrs.addFlashAttribute("sucesso", "Folha de pagamento registrada com sucesso!");
			}
			return "redirect:/folhas";
		} catch (Exception e) {
			model.addAttribute("folha", folha);
			model.addAttribute("colaboradores", colService.listarTodos());
			model.addAttribute("erro", "Erro ao salvar folha de pagamento: " + e.getMessage());
			return "folhas/formulario";
		}
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
		try {
			folhaService.deletar(id);
			redirectAttrs.addFlashAttribute("sucesso", "Folha de pagamento excluída com sucesso!");
		} catch (Exception e) {
			redirectAttrs.addFlashAttribute("erro", "Erro ao excluir folha: " + e.getMessage());
		}
		return "redirect:/folhas";
	}
}
