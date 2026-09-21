
Proyecto de pruebas unitarias (SQA) para el primer parcial de la materia **COM470 – Prueba y Mantenimiento de Software**.

**Universidad**: USFX – Facultad de Ciencias y Tecnología
**Carrera**: Ingeniería en Ciencias de la Computación
**Gestión**: 2026-II
**Grupo**: 1
**Integrantes**:
- Jhojan Erick Romero Morales
- Ismael Derick Garcia Torricos

---

## Descripción del trabajo

Se aplican técnicas de *Software Quality Assurance* (SQA) sobre una aplicación bancaria en **Spring Boot** (`springboot_test`), diseñando e implementando casos de prueba unitarios exhaustivos con **JUnit 5** y **Mockito**, a partir de un plan de pruebas basado en 10 requerimientos funcionales y 10 historias de usuario (reparto 5-5 entre los integrantes).

El sistema maneja cuentas bancarias y transferencias. La capa de servicios (`CuentaServiceImpl`) implementa `transferir`, `revisarSaldo`, `revisarTotalTransferencias` y `findById`; los modelos `Cuenta` y `Banco` implementan operaciones internas de débito/crédito e historial.

### División de responsabilidades (5 a 5)

| Responsable | Parte | Requerimientos / Casos |
|---|---|---|
| **Jhojan Romero** | Lógica transaccional | REQ-01 a REQ-05 · CP-01 a CP-05 (`transferir`, validación de fondos, débito) |
| **Ismael Garcia** | Consultas y auditoría | REQ-06 a REQ-10 · CP-06 a CP-10 (`revisarSaldo`, `revisarTotalTransferencias`, `findById`, crédito) + API REST y colección Postman/Insomnia |

---

## Estructura del proyecto

```
src/main/java/test/springboot/app/
├── controllers/      CuentaController (API REST /api)
├── services/         CuentaService + CuentaServiceImpl
├── repositories/     CuentaRepository, BancoRepository
├── models/           Cuenta, Banco
├── exceptions/       DineroInsuficienteException
└── Datos.java        Datos de prueba (crearCuenta001/002, crearBanco)

src/test/java/test/springboot/app/
├── services/         CuentaServiceImplTest   (9 tests)
└── controllers/      CuentaControllerTest    (4 tests, MockMvc)
```

Colecciones de API: `Coleccion_COM470_Equipo.json` (Postman) y `Coleccion_COM470_Equipo_Insomnia.json` (Insomnia), con scripts de validación embebidos.

---

## API

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/cuentas/transferir?origen&destino&monto&bancoId` | Ejecuta una transferencia entre cuentas |
| GET | `/api/cuentas/{id}/saldo` | Consulta el saldo de una cuenta |
| GET | `/api/cuentas/{id}` | Recupera los detalles de una cuenta |
| GET | `/api/bancos/{id}/transferencias` | Consulta el total de transferencias del banco |

---

## Pruebas (13 en total)

| Clase | Casos | Resultado |
|---|---|---|
| `CuentaServiceImplTest` | 9 (positivos y negativos, `assertAll`, Mockito) | Pasan |
| `CuentaControllerTest` | 4 (MockMvc standalone) | Pasan |

Los casos negativos validan `DineroInsuficienteException`, ausencia de persistencia en fallo (`never().update`) y respuestas 404.

---

## Instrucciones

### Requisitos

- JDK 15+ (el proyecto compila con `java.version = 15`)
- Maven 3.6+ (o usar el *wrapper* `mvnw`)
- Opcional: Postman o Insomnia para pruebas E2E

### Ejecutar la aplicación

```bash
# Windows (con la variable JAVA_HOME configurada)
mvnw.cmd spring-boot:run

# Linux/macOS
./mvnw spring-boot:run
```

### Ejecutar las pruebas

```bash
# Windows
mvnw.cmd test

# Linux/macOS
./mvnw test
```

### Importar la colección de API

1. Abrir Postman (o Insomnia) → **Import**.
2. Arrastrar `Coleccion_COM470_Equipo.json` (Postman) o `Coleccion_COM470_Equipo_Insomnia.json` (Insomnia).
3. Tener la aplicación corriendo en `http://localhost:8080` y ejecutar los requests.

---

## Herramientas

- Java 15 · Spring Boot 2.4.4
- Maven (con `spring-boot-starter-test`: JUnit 5 / Jupiter, Mockito, MockMvc)
- IntelliJ IDEA
- Postman / Insomnia
- Git + GitHub

## Problemas encontrados y soluciones

1. **Bug BC001 – Monto negativo en transferencias** (ver `COM470_primer-parcial_Reporte_de_Bug.pdf`): la validación de `debito`/`credito` no rechazaba montos `<= 0`, permitiendo saldos inflados. **Solución**: validación en los modelos que lanza `IllegalArgumentException` y test de regresión (`testTransferirMontoNegativoLanzaExcepcion`).
2. **Strict stubbing en Mockito**: un stub innecesario causaba `UnnecessaryStubbingException`. **Solución**: uso de `lenient()` en el stub específico sin debilitar el resto de la verificación.
3. **Dependencias externas**: `JAVA_HOME` requerido en Windows para el *wrapper* de Maven.