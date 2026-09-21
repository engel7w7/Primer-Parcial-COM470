package test.springboot.app.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.springboot.app.services.CuentaService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestParam Long origen,
                                        @RequestParam Long destino,
                                        @RequestParam BigDecimal monto,
                                        @RequestParam Long bancoId) {
        cuentaService.transferir(origen, destino, monto, bancoId);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Transferencia realizada con éxito");
        response.put("origen", origen);
        response.put("destino", destino);

        return ResponseEntity.ok(response);
    }
}