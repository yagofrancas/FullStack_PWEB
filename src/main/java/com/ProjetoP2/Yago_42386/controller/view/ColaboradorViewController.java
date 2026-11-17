package com.ProjetoP2.Yago_42386.controller.view;

import com.ProjetoP2.Yago_42386.model.colaborador;
import com.ProjetoP2.Yago_42386.model.colaborador_beneficio;
import com.ProjetoP2.Yago_42386.service.beneficioService;
import com.ProjetoP2.Yago_42386.service.cargoService;
import com.ProjetoP2.Yago_42386.service.colaboradorService;
import com.ProjetoP2.Yago_42386.service.colaborador_beneficioService;
import com.ProjetoP2.Yago_42386.service.departamentoService;
import com.ProjetoP2.Yago_42386.service.unidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	private final beneficioService benService;
	private final colaborador_beneficioService colBenService;

	@Autowired
	public ColaboradorViewController(colaboradorService colService,
									 departamentoService depService,
									 cargoService carService,
									 unidadeService uniService,
									 beneficioService benService,
									 colaborador_beneficioService colBenService) {
		this.colService = colService;
		this.depService = depService;
		this.carService = carService;
		this.uniService = uniService;
		this.benService = benService;
		this.colBenService = colBenService;
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

	@GetMapping("/detalhes/{id}")
	public String detalhes(@PathVariable Integer id, Model model, RedirectAttributes redirectAttrs) {
		return colService.buscarPorId(id).map(col -> {
			model.addAttribute("colaborador", col);
			model.addAttribute("beneficiosVinculados", colBenService.buscarPorColaborador(id));
			return "colaboradores/detalhes";
		}).orElseGet(() -> {
			redirectAttrs.addFlashAttribute("erro", "Colaborador não encontrado com ID: " + id);
			return "redirect:/colaboradores";
		});
	}

	@GetMapping("/novo")
	@PreAuthorize("hasAnyRole('RH', 'ADMIN', 'GESTOR_RH', 'FUNCIONARIO')")
	public String novoFormulario(Model model) {
		colaborador novo = new colaborador();
		novo.setStatus_colaborador("Ativo");
		model.addAttribute("colaborador", novo);
		model.addAttribute("beneficiosSelecionados", Collections.emptyList());
		popularAuxiliares(model);
		return "colaboradores/formulario";
	}

	@GetMapping("/editar/{id}")
	@PreAuthorize("hasAnyRole('RH', 'ADMIN', 'GESTOR_RH', 'FUNCIONARIO')")
	public String editarFormulario(@PathVariable Integer id, Model model, RedirectAttributes redirectAttrs) {
		return colService.buscarPorId(id).map(col -> {
			List<Integer> selecionados = colBenService.buscarPorColaborador(id).stream()
					.map(colaborador_beneficio::getId_beneficio)
					.toList();
			model.addAttribute("colaborador", col);
			model.addAttribute("beneficiosSelecionados", selecionados);
			popularAuxiliares(model);
			return "colaboradores/formulario";
		}).orElseGet(() -> {
			redirectAttrs.addFlashAttribute("erro", "Colaborador não encontrado com ID: " + id);
			return "redirect:/colaboradores";
		});
	}

	@PostMapping("/salvar")
	@PreAuthorize("hasAnyRole('RH', 'ADMIN', 'GESTOR_RH', 'FUNCIONARIO')")
	public String salvar(@ModelAttribute("colaborador") colaborador col,
						 @RequestParam(value = "beneficiosIds", required = false) List<Integer> beneficiosIds,
						 Model model,
						 RedirectAttributes redirectAttrs) {
		List<Integer> selecionados = beneficiosIds != null ? beneficiosIds : Collections.emptyList();
		try {
			colaborador salvo;
			if (col.getId_colaborador() != null && col.getId_colaborador() > 0) {
				salvo = colService.atualizar(col.getId_colaborador(), col);
				redirectAttrs.addFlashAttribute("sucesso", "Colaborador atualizado com sucesso!");
			} else {
				salvo = colService.salvar(col);
				redirectAttrs.addFlashAttribute("sucesso", "Colaborador cadastrado com sucesso!");
			}
			sincronizarBeneficios(salvo.getId_colaborador(), selecionados);
			return "redirect:/colaboradores";
		} catch (IllegalArgumentException e) {
			// Erro de validação de transição de status (ex: colaborador Desligado não pode ser reativado)
			model.addAttribute("colaborador", col);
			model.addAttribute("beneficiosSelecionados", selecionados);
			model.addAttribute("erro", e.getMessage());
			popularAuxiliares(model);
			return "colaboradores/formulario";
		} catch (Exception e) {
			model.addAttribute("colaborador", col);
			model.addAttribute("beneficiosSelecionados", selecionados);
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

	private void sincronizarBeneficios(Integer idColaborador, List<Integer> desejados) {
		Set<Integer> novos = new HashSet<>(desejados);
		List<colaborador_beneficio> atuais = colBenService.buscarPorColaborador(idColaborador);
		Set<Integer> existentes = new HashSet<>();

		for (colaborador_beneficio vinculo : atuais) {
			Integer idBen = vinculo.getId_beneficio();
			if (!novos.contains(idBen)) {
				colBenService.remover(idColaborador, idBen);
			} else {
				existentes.add(idBen);
			}
		}

		for (Integer idBen : novos) {
			if (!existentes.contains(idBen)) {
				colBenService.associar(idColaborador, idBen);
			}
		}
	}

	private void popularAuxiliares(Model model) {
		model.addAttribute("departamentos", depService.listarTodos());
		model.addAttribute("cargos", carService.listarTodos());
		model.addAttribute("unidades", uniService.listarTodos());
		model.addAttribute("beneficios", benService.listarTodos());
	}
}
