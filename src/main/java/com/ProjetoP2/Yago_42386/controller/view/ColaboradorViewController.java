package com.ProjetoP2.Yago_42386.controller.view;

import com.ProjetoP2.Yago_42386.model.colaborador;
import com.ProjetoP2.Yago_42386.service.cargoService;
import com.ProjetoP2.Yago_42386.service.colaboradorService;
import com.ProjetoP2.Yago_42386.service.departamentoService;
import com.ProjetoP2.Yago_42386.service.unidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller MVC Server-Side para Thymeleaf (Fase 10).
 * Integração com dados dinâmicos e controle de permissões (Fase 09).
 */
@Controller
@RequestMapping("/colaboradores")
public class ColaboradorViewController {

	private final colaboradorService colService;
	private final departamentoService depService;
	private final cargoService carService;
	private final unidadeService uniService;

	@Autowired
	public ColaboradorViewController(colaboradorService colService,
									 departamentoService depService,
									 cargoService carService,
									 unidadeService uniService) {
		this.colService = colService;
		this.depService = depService;
		this.carService = carService;
		this.uniService = uniService;
	}

	@GetMapping
	public String listar(@RequestParam(value = "status", required = false) String status, Model model) {
		List<colaborador> lista;
		if (status != null && !status.trim().isEmpty()) {
			lista = colService.buscarPorStatus(status.trim());
		} else {
			lista = colService.listarTodos();
		}
		model.addAttribute("colaboradores", lista);
		return "colaboradores/lista";
	}

	@GetMapping("/novo")
	@PreAuthorize("hasAnyRole('RH', 'ADMIN', 'GESTOR_RH')")
	public String novoFormulario(Model model) {
		colaborador novo = new colaborador();
		novo.setStatus_colaborador("Ativo");
		model.addAttribute("colaborador", novo);
		popularAuxiliares(model);
		return "colaboradores/formulario";
	}

	@GetMapping("/editar/{id}")
	@PreAuthorize("hasAnyRole('RH', 'ADMIN', 'GESTOR_RH')")
	public String editarFormulario(@PathVariable Integer id, Model model, RedirectAttributes redirectAttrs) {
		return colService.buscarPorId(id).map(col -> {
			model.addAttribute("colaborador", col);
			popularAuxiliares(model);
			return "colaboradores/formulario";
		}).orElseGet(() -> {
			redirectAttrs.addFlashAttribute("erro", "Colaborador não encontrado com ID: " + id);
			return "redirect:/colaboradores";
		});
	}

	@PostMapping("/salvar")
	@PreAuthorize("hasAnyRole('RH', 'ADMIN', 'GESTOR_RH')")
	public String salvar(@ModelAttribute("colaborador") colaborador col,
						 Model model,
						 RedirectAttributes redirectAttrs) {
		try {
			if (col.getId_colaborador() != null && col.getId_colaborador() > 0) {
				colService.atualizar(col.getId_colaborador(), col);
				redirectAttrs.addFlashAttribute("sucesso", "Colaborador atualizado com sucesso!");
			} else {
				colService.salvar(col);
				redirectAttrs.addFlashAttribute("sucesso", "Colaborador cadastrado com sucesso!");
			}
			return "redirect:/colaboradores";
		} catch (IllegalArgumentException e) {
			// Erro de validação de transição de status (ex: colaborador Desligado não pode ser reativado)
			model.addAttribute("colaborador", col);
			model.addAttribute("erro", e.getMessage());
			popularAuxiliares(model);
			return "colaboradores/formulario";
		} catch (Exception e) {
			model.addAttribute("colaborador", col);
			model.addAttribute("erro", "Erro ao salvar colaborador: " + e.getMessage());
			popularAuxiliares(model);
			return "colaboradores/formulario";
		}
	}

	@GetMapping("/excluir/{id}")
	@PreAuthorize("hasAnyRole('RH', 'ADMIN', 'GESTOR_RH')")
	public String excluir(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
		try {
			colService.deletar(id);
			redirectAttrs.addFlashAttribute("sucesso", "Colaborador excluído com sucesso!");
		} catch (Exception e) {
			redirectAttrs.addFlashAttribute("erro", "Erro ao excluir colaborador: " + e.getMessage());
		}
		return "redirect:/colaboradores";
	}

	private void popularAuxiliares(Model model) {
		model.addAttribute("departamentos", depService.listarTodos());
		model.addAttribute("cargos", carService.listarTodos());
		model.addAttribute("unidades", uniService.listarTodos());
	}
}
