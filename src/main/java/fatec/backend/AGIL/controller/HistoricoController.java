package fatec.backend.AGIL.controller;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import fatec.backend.AGIL.dto.HistoricoDTO;
import fatec.backend.AGIL.service.HistoricoService;

@RestController
@RequestMapping("/historico")
@CrossOrigin(origins = "http://localhost:5173")
public class HistoricoController {

    private final HistoricoService historicoService;

    public HistoricoController(HistoricoService historicoService) {
        this.historicoService = historicoService;
    }

    @GetMapping
    public List<HistoricoDTO> listar(
            @RequestParam Long gestorId) {

        return historicoService.listarPorGestor(gestorId);
    }
}