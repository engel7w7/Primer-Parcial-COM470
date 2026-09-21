package test.springboot.app.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import test.springboot.app.Datos;
import test.springboot.app.services.CuentaService;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CuentaControllerTest {

    @Mock
    CuentaService cuentaService;

    @InjectMocks
    CuentaController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // ==========================================
    // ==========================================

    @Test
    void testControllerRevisarSaldo() throws Exception {
        when(cuentaService.revisarSaldo(1L)).thenReturn(new BigDecimal("1000"));

        mockMvc.perform(get("/api/cuentas/1/saldo"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.cuentaId").value(1))
            .andExpect(jsonPath("$.saldo").value(1000));
    }

    @Test
    void testControllerFindByIdValido() throws Exception {
        when(cuentaService.findById(1L)).thenReturn(Datos.crearCuenta001());

        mockMvc.perform(get("/api/cuentas/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.persona").value("Andrés"))
            .andExpect(jsonPath("$.saldo").value(1000));
    }

    @Test
    void testControllerFindByIdNoExiste() throws Exception {
        when(cuentaService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/cuentas/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testControllerRevisarTotalTransferencias() throws Exception {
        when(cuentaService.revisarTotalTransferencias(1L)).thenReturn(1);

        mockMvc.perform(get("/api/bancos/1/transferencias"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.bancoId").value(1))
            .andExpect(jsonPath("$.totalTransferencias").value(1));
    }
}