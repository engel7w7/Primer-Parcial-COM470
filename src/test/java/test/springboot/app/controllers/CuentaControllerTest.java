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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import org.springframework.web.util.NestedServletException;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @Test
    void testControllerTransferirPositivo() throws Exception {
        doNothing().when(cuentaService).transferir(1L, 2L, new BigDecimal("100"), 1L);

        mockMvc.perform(post("/api/cuentas/transferir")
                        .param("origen", "1")
                        .param("destino", "2")
                        .param("monto", "100")
                        .param("bancoId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Transferencia realizada con éxito"))
                .andExpect(jsonPath("$.origen").value(1))
                .andExpect(jsonPath("$.destino").value(2));
    }

    @Test
    void testControllerTransferirExcepcion() throws Exception {
        doThrow(new IllegalArgumentException("Monto inválido"))
                .when(cuentaService).transferir(1L, 2L, new BigDecimal("-100"), 1L);

        assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(post("/api/cuentas/transferir")
                    .param("origen", "1")
                    .param("destino", "2")
                    .param("monto", "-100")
                    .param("bancoId", "1"));
        });
    }
}