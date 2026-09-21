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



    // ==========================================
    // ==========================================

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
}