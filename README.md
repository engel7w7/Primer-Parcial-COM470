# UNIVERSIDAD MAYOR REAL Y PONTIFICIA DE SAN FRANCISCO XAVIER DE CHUQUISACA
## FACULTAD DE CIENCIAS Y TECNOLOGÍA

**Carrera**: Ingeniería en Ciencias de la Computación  
**Materia**: COM470 (Prueba y Mantenimiento de Software)  
**Grupo**: 1  
**Universitarios**:  
- Romero Morales Jhojan Erick  
- Ismael Derick Garcia Torricos  
**Parcial**: Primero  
**Docente**: Ing. Bergman Villarroel Juan Carlos  
**Fecha**: 20/09/2026  
**Gestión**: 2026  
**Lugar**: Sucre – Bolivia  

---

# DOCUMENTO DE PROYECTO: PRIMER PARCIAL – SQA Y PRUEBAS UNITARIAS
**Asignatura**: Prueba y Mantenimiento de Software (COM470)  
**Enfoque**: Pruebas Unitarias (Spring Boot + Mockito), Scrum y Diseño Asistido por IA  

---

## 1. Objetivo General
Aplicar técnicas de *Software Quality Assurance* (SQA) para diseñar e implementar casos de prueba unitarios exhaustivos a partir de un Plan de Pruebas, utilizando Inteligencia Artificial como apoyo metodológico. Se evaluará una aplicación bancaria en Spring Boot (`springboot_test`), garantizando la cobertura de código sobre todos los métodos de sus capas centrales (Controladores, Servicios, Repositorios simulados y Modelos), abarcando flujos positivos, nulos (Nulls) y manejo de excepciones de infraestructura bajo un marco ágil.

---

## 2. Objetivos Específicos
1. Analizar el dominio bancario e inferir requerimientos funcionales y Casos de Uso / Historias de Usuario.
2. Diseñar casos de prueba que evalúen todos los métodos disponibles: `transferir`, `revisarSaldo`, `revisarTotalTransferencias`, `findById`, `debito` y `credito`.
3. Implementar pruebas en JUnit 5 utilizando `assertAll` y Mockito para aislar las dependencias, incluyendo simulaciones de caídas del sistema (`thenThrow`/`doThrow`) y valores nulos.
4. Aplicar Scrum simplificado con asignación de roles y control de versiones en GitHub.
5. Generar reportes de bugs documentando anomalías lógicas detectadas por las pruebas negativas.

---

## 3. Trabajo Colaborativo y Organización Ágil (Scrum)
Para cumplir estrictamente con los criterios de evaluación de trabajo colaborativo y la asignación equitativa (50/50), el equipo estructuró el ciclo de desarrollo de la siguiente manera:

* **Jhojan Erick Romero Morales (Scrum Master / QA Tester 1)**:
  * **Rol**: Responsable de la gestión del Sprint Backlog mediante GitHub Issues y GitHub Projects, pruebas de la lógica transaccional crítica (Transferencias), validaciones de fondos, excepciones de BD al escribir (`update`) y pruebas de los endpoints POST en el Controlador.
  * **Rama de GitHub**: `feature-transferencias-JhojanRomero`

* **Ismael Derick Garcia Torricos (Developer / QA Tester 2)**:
  * **Rol**: Responsable del diseño de pruebas de consultas, auditoría bancaria, configuración de la API REST, manejo de casos Nulos (Null) en búsquedas, validación de endpoints GET y la creación de la Colección de Postman/Insomnia con scripts de testing E2E.
  * **Rama de GitHub**: `feature-casosnull-excepcionesbd-GarciaIsmael`

---

## 4. Especificación de Requerimientos del Sistema (Asignación 5 a 5)
A partir del análisis estructural del código base, se deducen 10 requerimientos principales, divididos equitativamente para su cobertura de pruebas:

### Responsabilidad de Jhojan Romero (Lógica Transaccional)
* **REQ-01**: El sistema debe permitir transferencias de fondos entre dos cuentas activas.
* **REQ-02**: El sistema debe impedir transferencias si la cuenta origen posee saldo insuficiente.
* **REQ-03**: El sistema debe prevenir montos ilógicos o negativos en operaciones matemáticas.
* **REQ-04**: El sistema debe asegurar la persistencia y detenerse ante caídas de la BD en escritura.
* **REQ-05**: El API (Controlador) debe manejar correctamente los flujos exitosos y propagar excepciones en transferencias.

### Responsabilidad de Ismael Garcia (Consultas y Auditoría)
* **REQ-06**: El sistema debe permitir consultar el saldo en tiempo real de cualquier cuenta.
* **REQ-07**: El sistema debe permitir revisar el total de transferencias de un banco.
* **REQ-08**: El sistema debe tolerar caídas de la BD al ejecutar lecturas (Timeout).
* **REQ-09**: El sistema debe retornar los datos de una cuenta y manejar correctamente búsquedas nulas (null).
* **REQ-10**: El API (Controlador) debe retornar Status 404 (Not Found) al consultar cuentas inexistentes (Null).

---

## 5. Plan de Pruebas: Historias de Usuario (Backlog)
Se han generado 18 Historias de Usuario para cubrir reglas de negocio, fallos de infraestructura y manejo de valores nulos (9 por tester):

| ID | Responsable | Título de la Historia | Criterio de Aceptación (CA) |
|:---|:---|:---|:---|
| **HU-01** | Jhojan Romero | Transferencia Exitosa | El sistema procesa la transferencia restando del origen y sumando al destino. |
| **HU-02** | Jhojan Romero | Protección de Sobregiro | El sistema lanza excepción si el origen no tiene fondos suficientes. |
| **HU-03** | Jhojan Romero | Prevención Montos Negativos | El sistema debe rechazar transferencias con montos negativos. |
| **HU-04** | Jhojan Romero | Persistencia Transaccional | Si la transacción falla, no debe invocarse la actualización de BD. |
| **HU-05** | Jhojan Romero | Débito en Cuenta | La entidad Cuenta disminuye su saldo al aplicar un débito. |
| **HU-06** | Jhojan Romero | Fallo de Escritura (BD) | Si la BD falla al guardar, el sistema propaga una `RuntimeException`. |
| **HU-07** | Jhojan Romero | Fallo de Red Bancaria | Si el banco falla en transferencia, la operación aborta con `RuntimeException`. |
| **HU-08** | Jhojan Romero | API: Transferencia 200 | El controlador POST debe responder con Status 200 OK y JSON de éxito. |
| **HU-09** | Jhojan Romero | API: Transferencia Error | El controlador POST debe propagar las excepciones lanzadas por el servicio. |
| **HU-10** | Ismael Garcia | Consulta de Saldo | El sistema retorna el valor exacto actual de la cuenta consultada. |
| **HU-11** | Ismael Garcia | Auditoría Bancaria | El sistema retorna el contador de operaciones del banco. |
| **HU-12** | Ismael Garcia | Contador de Historial | Se suma automáticamente 1 al historial de transferencias. |
| **HU-13** | Ismael Garcia | Búsqueda de Cliente | El sistema retorna los detalles completos (Nombre, Saldo) por ID. |
| **HU-14** | Ismael Garcia | Crédito en Cuenta | La entidad Cuenta incrementa su saldo al aplicar un crédito. |
| **HU-15** | Ismael Garcia | Fallo Lectura de Saldo | Si la BD no responde, el sistema propaga una `RuntimeException`. |
| **HU-16** | Ismael Garcia | Fallo Historial Auditoría | Si el servidor no responde al consultar banco, propaga `RuntimeException`. |
| **HU-17** | Ismael Garcia | API: Búsqueda 200 | El controlador GET responde Status 200 y los datos del cliente. |
| **HU-18** | Ismael Garcia | API: Manejo Nulos (Null) | Si el servicio retorna null, el controlador GET responde Status 404. |

---

## 6. Casos de Prueba (Plantilla de Ejecución SQA)
Abarcando todos los métodos del servicio (`transferir`, `revisarSaldo`, `revisarTotalTransferencias`, `findById`), controladores y métodos internos de actualización:

| ID CP | HU | Responsable | Título del Caso | Tipo | Pasos de Ejecución (Método) | Resultado Esperado | Estado |
|:---|:---|:---|:---|:---|:---|:---|:---:|
| **CP-01** | HU-01 | Jhojan Romero | Transferencia estándar | Positivo | 1. `transferir(1L, 2L, 100, 1L)` | Saldos actualizados (900 y 2100). | **Pasa** |
| **CP-02** | HU-02 | Jhojan Romero | Exceso de monto | Negativo | 1. `transferir(1L, 2L, 1200, 1L)` | Lanza `DineroInsuficienteException`. | **Pasa** |
| **CP-03** | HU-03 | Jhojan Romero | Monto inválido (Bug fix) | Negativo | 1. `transferir(1L, 2L, -100, 1L)` | Lanza `IllegalArgumentException`. | **Pasa** |
| **CP-04** | HU-04 | Jhojan Romero | Verificación de persistencia | Negativo | 1. Provocar fallo transaccional | 0 interacciones con `update`. | **Pasa** |
| **CP-05** | HU-05 | Jhojan Romero | Operación interna débito | Positivo | 1. `cuenta.debito(100)` | Saldo interno disminuye. | **Pasa** |
| **CP-06** | HU-06 | Jhojan Romero | Caída de BD al guardar | Excepción | 1. Simular `doThrow` en `update()` | Propaga `RuntimeException`. | **Pasa** |
| **CP-07** | HU-07 | Jhojan Romero | Caída de BD (Banco) | Excepción | 1. Simular `thenThrow` en Banco | Propaga `RuntimeException`. | **Pasa** |
| **CP-08** | HU-08 | Jhojan Romero | API: Controller 200 | Positivo | 1. `controller.transferir(...)` | Retorna `HttpStatus.OK`. | **Pasa** |
| **CP-09** | HU-09 | Jhojan Romero | API: Propagación Error | Excepción | 1. Simular excepción, llamar API | Lanza excepción correspondiente. | **Pasa** |
| **CP-10** | HU-10 | Ismael Garcia | Revisar saldo existente | Positivo | 1. `revisarSaldo(1L)` | Retorna el valor exacto. | **Pasa** |
| **CP-11** | HU-11 | Ismael Garcia | Revisar transferencias | Positivo | 1. `revisarTotalTransferencias(1L)` | Retorna el total procesadas. | **Pasa** |
| **CP-12** | HU-12 | Ismael Garcia | Incremento historial | Positivo | 1. Ejecutar transferencia válida | Contador incrementa en 1. | **Pasa** |
| **CP-13** | HU-13 | Ismael Garcia | Buscar ID válido | Positivo | 1. `findById(1L)` | Retorna objeto `Cuenta` completo. | **Pasa** |
| **CP-14** | HU-14 | Ismael Garcia | Operación interna crédito | Positivo | 1. `cuenta.credito(100)` | Saldo interno incrementa. | **Pasa** |
| **CP-15** | HU-15 | Ismael Garcia | Caída de BD lectura | Excepción | 1. Simular `thenThrow` en `findById` | Propaga `RuntimeException`. | **Pasa** |
| **CP-16** | HU-16 | Ismael Garcia | Caída BD auditoría | Excepción | 1. Simular `thenThrow` en Banco | Propaga `RuntimeException`. | **Pasa** |
| **CP-17** | HU-17 | Ismael Garcia | API: Controller 200 GET | Positivo | 1. `controller.findById(1L)` | Retorna `HttpStatus.OK`. | **Pasa** |
| **CP-18** | HU-18 | Ismael Garcia | API: Manejo Nulos (404) | Null | 1. Simular null, llamar API | Retorna `HttpStatus.NOT_FOUND`. | **Pasa** |

---

## 7. Reporte de Error (Bug Report)
Durante la ejecución del Plan de Pruebas se documentó el siguiente fallo de la lógica del sistema:

| Campo | Detalle |
|:---|:---|
| **Número de Bug** | **BC001** |
| **Nombre del Tester** | Jhojan Erick Romero Morales |
| **Título** | Vulnerabilidad lógica permite transferencias con montos matemáticamente negativos alterando los saldos |
| **Día del Reporte** | 20/09/2026 |
| **Sistema Operativo** | Windows 11 |
| **Browser / Entorno** | Back-end API (Postman / Ejecución en IntelliJ) |
| **Prioridad** | Alta / Crítica |
| **Asignado a** | Desarrollador Back-end / Módulo de Servicios (`CuentaServiceImpl`) |

### Descripción
* **Historia**: Transferencia Exitosa (HU-01) y Débito en Cuenta (HU-05).
* **Criterio Afectado**: REQ-05 ("El sistema debe poder aplicar un débito directo previniendo montos ilógicos") y el CA de HU-01 ("El sistema procesa la transferencia restando del origen y sumando al destino").
* **Detalle técnico**: El método `transferir` en la clase `CuentaServiceImpl` no implementaba una validación para verificar que el parámetro `monto` fuera estrictamente mayor a cero antes de procesar la transacción. Al inyectar un valor numérico negativo, el método interno `debito` realizaba una resta matemática de un número negativo (ej. `saldo - (-100)`), lo cual invertía la operación sumando dinero a la cuenta de origen de forma irregular e impactando la integridad financiera.

### Pasos para la reproducción
1. Iniciar el entorno de pruebas unitarias en IntelliJ para la clase `CuentaServiceImplTest`.
2. Diseñar un caso de prueba que invoque el método `service.transferir`.
3. Enviar los parámetros con un monto en negativo.
4. Consultar los saldos posteriores a la transacción mediante aserciones.
* **Datos de prueba utilizados**: `origen`: 1 (Saldo inicial: 1000) \| `destino`: 2 (Saldo inicial: 2000) \| `monto`: -100 \| `bancoId`: 1.

### Resultado Esperado
El sistema debe interceptar el valor ilógico antes de la transacción, abortar la operación sin invocar a la base de datos y lanzar una excepción controlada (ej. `IllegalArgumentException`) indicando que no se admiten montos negativos ni cero.

### Resultado Actual
El sistema procesaba exitosamente la solicitud como válida. La cuenta de origen terminaba con un saldo inflado de 1100 (creación de dinero irreal) y la cuenta de destino sufría un descuento injustificado bajando a 1900, vulnerando las reglas del negocio bancario.

---

## 8. Problemas Encontrados y Soluciones
1. **Problema 1: Vulnerabilidad BC001 debido a la falta de validación de montos negativos en la entidad `Cuenta.java` al realizar transferencias y débitos.**  
   * **Solución**: Inclusión de una validación explícita que verifica que el monto sea estrictamente mayor a cero en la lógica de negocio (`monto.compareTo(BigDecimal.ZERO) <= 0`), lanzando `IllegalArgumentException` cuando el valor es negativo o cero.
2. **Problema 2: Manejo impreciso de excepciones de infraestructura y respuestas nulas al consultar la API REST.**  
   * **Solución**: Simulación exhaustiva de escenarios límite mediante Mockito (`thenThrow`/`doThrow`) y validación de códigos de respuesta HTTP (como `404 NOT FOUND` para búsquedas nulas) con MockMvc y colecciones de Postman/Insomnia.

---

## 9. Herramientas Usadas
* **Framework de Desarrollo**: Spring Boot 2.4.4 y Java 15 / 17.
* **Pruebas Unitarias y Aislamiento**: JUnit 5 (Jupiter), Assertions (`assertAll`) y Mockito.
* **Gestión y Trabajo Colaborativo**: GitHub, GitHub Issues, GitHub Projects (Scrum) y Git.
* **Pruebas de Integración / API**: Postman e Insomnia (creación de colecciones exportadas en JSON: `Coleccion_COM470_Equipo.json` y `Coleccion_COM470_Equipo_Insomnia.json` con scripts embebidos de validación E2E).
* **Asistentes de IA**: ChatGPT y Google Gemini para apoyo metodológico en SQA.

---

## 10. Reflexión sobre el uso de Inteligencia Artificial en SQA
La integración de la Inteligencia Artificial generativa en los procesos de *Software Quality Assurance* (SQA) aceleró significativamente el diseño de casos de prueba y la inferencia de requerimientos a partir del código fuente. Permitió identificar escenarios límite que no habían sido considerados inicialmente en la fase analítica.

Sin embargo, la IA actúa como una herramienta de soporte y no como reemplazo del criterio técnico del analista de QA. Es indispensable revisar, refinar y adaptar el código de pruebas generado para asegurar el cumplimiento exacto del contexto del negocio y prevenir falsos positivos en las validaciones de cobertura.

---

## Instrucciones de Ejecución de Pruebas

```bash
# Ejecutar suite de pruebas unitarias en Windows
mvnw.cmd test

# O usando Maven instalado localmente
mvn test
```

### Colecciones de API para Postman / Insomnia
* **Postman**: Importar el archivo `Coleccion_COM470_Equipo.json`.
* **Insomnia**: Importar el archivo `Coleccion_COM470_Equipo_Insomnia.json`.