package test.springboot.app.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.springboot.app.Datos;
import test.springboot.app.exceptions.DineroInsuficienteException;
import test.springboot.app.models.Banco;
import test.springboot.app.models.Cuenta;
import test.springboot.app.repositories.BancoRepository;
import test.springboot.app.repositories.CuentaRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuentaServiceImplTest {

    @Mock
    CuentaRepository cuentaRepository;

    @Mock
    BancoRepository bancoRepository;

    @InjectMocks
    CuentaServiceImpl service;

    Cuenta origen;
    Cuenta destino;
    Banco banco;

    @BeforeEach
    void setUp() {
        origen = Datos.crearCuenta001();
        destino = Datos.crearCuenta002();
        banco = Datos.crearBanco();
    }

    @Test
    void testTransferirPositivo() {
        when(cuentaRepository.findById(1L)).thenReturn(origen);
        when(cuentaRepository.findById(2L)).thenReturn(destino);
        when(bancoRepository.findById(1L)).thenReturn(banco);

        service.transferir(1L, 2L, new BigDecimal("100"), 1L);

        assertAll("Validaciones flujo exitoso Jhojan",
                () -> assertEquals("900", origen.getSaldo().toPlainString()),
                () -> assertEquals("2100", destino.getSaldo().toPlainString()),
                () -> verify(cuentaRepository, times(2)).update(any(Cuenta.class))
        );
    }

    @Test
    void testTransferirFondosInsuficientes() {
        when(cuentaRepository.findById(1L)).thenReturn(origen);

        Exception ex = assertThrows(DineroInsuficienteException.class, () -> {
            service.transferir(1L, 2L, new BigDecimal("1200"), 1L);
        });

        assertAll("Validaciones flujo fallido Jhojan",
                () -> assertEquals("Dinero insuficiente en la cuenta.", ex.getMessage()),
                () -> verify(cuentaRepository, never()).update(any(Cuenta.class))
        );
    }


    @Test
    void testTransferirMontoNegativoLanzaExcepcion() {
        when(cuentaRepository.findById(1L)).thenReturn(origen);
        assertThrows(IllegalArgumentException.class, () -> {
            service.transferir(1L, 2L, new BigDecimal("-100"), 1L);
        }, "El sistema debería bloquear transferencias con montos matemáticamente negativos");
    }

    @Test
    void testDebitoModelo() {
        origen.debito(new BigDecimal("500"));

        assertAll("Validación débito directo en entidad Jhojan",
                () -> assertEquals("500", origen.getSaldo().toPlainString(), "El saldo debería ser 500 tras el débito")
        );
    }

    @Test
    void testRevisarSaldo() {
        when(cuentaRepository.findById(1L)).thenReturn(origen);
        BigDecimal saldo = service.revisarSaldo(1L);

        assertAll("Validación revisión saldo",
                () -> assertEquals("1000", saldo.toPlainString()),
                () -> verify(cuentaRepository).findById(1L)
        );
    }

    @Test
    void testRevisarTotalTransferencias() {
        when(bancoRepository.findById(1L)).thenReturn(banco);
        int trx = service.revisarTotalTransferencias(1L);
        assertEquals(0, trx);
    }

    @Test
    void testFindByIdValido() {
        when(cuentaRepository.findById(1L)).thenReturn(origen);
        assertNotNull(service.findById(1L));
    }

    @Test
    void testCreditoModelo() {
        destino.credito(new BigDecimal("500"));
        assertEquals("2500", destino.getSaldo().toPlainString());
    }

    @Test
    void testRevisarSaldoExcepcionBaseDeDatos() {
        // Simulamos caída de BD al leer
        when(cuentaRepository.findById(1L))
                .thenThrow(new RuntimeException("Timeout de lectura"));

        Exception ex = assertThrows(RuntimeException.class, () -> {
            service.revisarSaldo(1L);
        });
        assertEquals("Timeout de lectura", ex.getMessage());
    }

    @Test
    void testRevisarTotalTransferenciasExcepcionBaseDeDatos() {
        when(bancoRepository.findById(1L))
                .thenThrow(new RuntimeException("Timeout auditoría"));

        assertThrows(RuntimeException.class, () -> {
            service.revisarTotalTransferencias(1L);
        });
    }

    @Test
    void testTransferirExcepcionBaseDeDatos() { // CP-06
        when(cuentaRepository.findById(1L)).thenReturn(origen);

        doThrow(new RuntimeException("Error BD"))
                .when(cuentaRepository).update(any(Cuenta.class));

        Exception ex = assertThrows(RuntimeException.class, () -> {
            service.transferir(1L, 2L, new BigDecimal("100"), 1L);
        });
        assertEquals("Error BD", ex.getMessage());
    }

    @Test
    void testTransferirExcepcionAlBuscarBanco() { // CP-07
        when(cuentaRepository.findById(1L)).thenReturn(origen);
        when(cuentaRepository.findById(2L)).thenReturn(destino);
        when(bancoRepository.findById(1L))
                .thenThrow(new RuntimeException("Servidor banco inaccesible"));

        assertThrows(RuntimeException.class, () -> {
            service.transferir(1L, 2L, new BigDecimal("100"), 1L);
        });
        verify(bancoRepository, never()).update(any(Banco.class));
    }
}