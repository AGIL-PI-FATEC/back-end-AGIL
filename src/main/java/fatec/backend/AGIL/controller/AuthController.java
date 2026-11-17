package fatec.backend.AGIL.controller;

import fatec.backend.AGIL.dto.GestorRespostaDTO;
import fatec.backend.AGIL.dto.LoginDTO;
import fatec.backend.AGIL.service.GestorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private GestorService gestorService;

    @PostMapping("/login/gestor")
    public ResponseEntity<GestorRespostaDTO> loginGestor(@RequestBody @Valid LoginDTO dto) {
        return ResponseEntity.ok(gestorService.login(dto));
    }
}