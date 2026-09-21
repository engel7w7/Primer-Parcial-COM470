package test.springboot.app.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import test.springboot.app.models.Cuenta;
import test.springboot.app.services.CuentaService;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuentaControllerTest {

    @Mock
    CuentaService cuentaService;

    @InjectMocks
    CuentaController controller;




    @Test
    void testFindByIdPositivoController() {
        Cuenta cuentaMock = new Cuenta(1L, "Andrés", new BigDecimal("1000"));
        when(cuentaService.findById(1L)).thenReturn(cuentaMock);

        ResponseEntity<?> response = controller.findById(1L);

        assertAll("Validación HTTP 200 OK Ismael",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody())
        );
    }

    @Test
    void testFindByIdCasoNullController() {
        //Simular caso Null
        when(cuentaService.findById(99L)).thenReturn(null);

        ResponseEntity<?> response = controller.findById(99L);

        assertAll("Validación HTTP 404 Nulos Ismael",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNull(response.getBody())
        );
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