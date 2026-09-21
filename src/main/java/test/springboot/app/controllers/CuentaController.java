package test.springboot.app.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.springboot.app.models.Cuenta;
import test.springboot.app.services.CuentaService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    // ==========================================
    // ==========================================

    @PostMapping("/cuentas/transferir")
    public ResponseEntity<?> transferir(@RequestParam Long origen,
                                        @RequestParam Long destino,
                                        @RequestParam BigDecimal monto,
                                        @RequestParam Long bancoId) {
        cuentaService.transferir(origen, destino, monto, bancoId);
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Transferencia realizada con éxito");
        response.put("origen", origen);
        response.put("destino", destino);
        response.put("monto", monto);
        return ResponseEntity.ok(response);
    }

    // ==========================================
    // ==========================================

    @GetMapping("/cuentas/{id}/saldo")
    public ResponseEntity<?> revisarSaldo(@PathVariable Long id) {
        BigDecimal saldo = cuentaService.revisarSaldo(id);
        Map<String, Object> response = new HashMap<>();
        response.put("cuentaId", id);
        response.put("saldo", saldo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cuentas/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Cuenta cuenta = cuentaService.findById(id);
        if(cuenta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cuenta);
    }

    @GetMapping("/bancos/{id}/transferencias")
    public ResponseEntity<?> revisarTotalTransferencias(@PathVariable Long id) {
        int total = cuentaService.revisarTotalTransferencias(id);
        Map<String, Object> response = new HashMap<>();
        response.put("bancoId", id);
        response.put("totalTransferencias", total);
        return ResponseEntity.ok(response);
    }
}