package fatec.backend.AGIL.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fatec.backend.AGIL.dto.NotificacaoDTO;
import fatec.backend.AGIL.service.NotificacaoService;

@RestController
@RequestMapping("/notificacoes")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    public NotificacaoController(
            NotificacaoService notificacaoService) {

        this.notificacaoService = notificacaoService;
    }

    // GET - listar notificações do gestor
    @GetMapping
    public List<NotificacaoDTO> listar(
            @RequestParam Long gestorId) {

        return notificacaoService.listarPorGestor(gestorId);
    }

    // PATCH - marcar notificação como lida
    @PatchMapping("/{id}/lida")
    public ResponseEntity<Void> marcarComoLida(
            @PathVariable Long id) {

        notificacaoService.marcarComoLida(id);

        return ResponseEntity.noContent().build();
    }
}