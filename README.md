## Historia de Usuario: Registrar usuarios en el sistema

**ID:** 1  
**Título:** Registrar usuarios en el sistema

### Descripción
Como **administrador del sistema**,  
quiero **registrar un nuevo usuario proporcionando sus datos personales básicos (nombres y apellidos por separado)**,  
para **mantener un registro claro y ordenado de los clientes potenciales**.

### Microservicio
- **Nombre:** AUTENTICACION
- **Framework:** WebFlux
- **Arquitectura:** Hexagonal (separando dominio e infraestructura)
- **Endpoint:** `POST /api/v1/users`

### Reglas y Validaciones
- Los campos **nombres, apellidos, correo_electronico y salario_base** no deben ser nulos ni vacíos.
- El **correo_electronico** debe ser único y no estar previamente registrado.
- El **correo_electronico** debe cumplir con un formato válido.
- El **salario_base** debe ser un valor numérico en el rango `0 - 15,000,000`.
- Deben validarse los formatos correctos de los datos de entrada.

### Datos del Solicitante
- `nombres`
- `apellidos`
- `fecha_nacimiento`
- `direccion`
- `telefono`
- `correo_electronico`
- `salario_base`

### Criterios de Aceptación
1. El sistema permite registrar un nuevo solicitante con los campos obligatorios completos.
2. Si alguno de los campos requeridos es nulo o vacío, el sistema devuelve un error controlado.
3. Si el correo ya existe en la base de datos, el sistema devuelve un error de duplicidad.
4. Si los formatos no son válidos (correo, salario numérico, etc.), el sistema devuelve un error de validación.
5. Una vez registrado correctamente, el solicitante queda guardado **permanentemente en la base de datos**.

### Consideraciones Técnicas
- Persistencia transaccional con anotación `@Transactional` para garantizar atomicidad.
- Logs de traza para monitoreo del proceso de registro.
- Manejo centralizado de excepciones, evitando mensajes inesperados para el consumidor de la API.










# Proyecto Base Implementando Clean Architecture

## Antes de Iniciar

Empezaremos por explicar los diferentes componentes del proyectos y partiremos de los componentes externos, continuando con los componentes core de negocio (dominio) y por último el inicio y configuración de la aplicación.

Lee el artículo [Clean Architecture — Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

# Arquitectura

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

Es el módulo más interno de la arquitectura, pertenece a la capa del dominio y encapsula la lógica y reglas del negocio mediante modelos y entidades del dominio.

## Usecases

Este módulo gradle perteneciente a la capa del dominio, implementa los casos de uso del sistema, define lógica de aplicación y reacciona a las invocaciones desde el módulo de entry points, orquestando los flujos hacia el módulo de entities.

## Infrastructure

### Helpers

En el apartado de helpers tendremos utilidades generales para los Driven Adapters y Entry Points.

Estas utilidades no están arraigadas a objetos concretos, se realiza el uso de generics para modelar comportamientos
genéricos de los diferentes objetos de persistencia que puedan existir, este tipo de implementaciones se realizan
basadas en el patrón de diseño [Unit of Work y Repository](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006)

Estas clases no puede existir solas y debe heredarse su compartimiento en los **Driven Adapters**

### Driven Adapters

Los driven adapter representan implementaciones externas a nuestro sistema, como lo son conexiones a servicios rest,
soap, bases de datos, lectura de archivos planos, y en concreto cualquier origen y fuente de datos con la que debamos
interactuar.

### Entry Points

Los entry points representan los puntos de entrada de la aplicación o el inicio de los flujos de negocio.

## Application

Este módulo es el más externo de la arquitectura, es el encargado de ensamblar los distintos módulos, resolver las dependencias y crear los beans de los casos de use (UseCases) de forma automática, inyectando en éstos instancias concretas de las dependencias declaradas. Además inicia la aplicación (es el único módulo del proyecto donde encontraremos la función “public static void main(String[] args)”.

**Los beans de los casos de uso se disponibilizan automaticamente gracias a un '@ComponentScan' ubicado en esta capa.**

# Microservicio de Autenticación (`ms-authentication`)

Este documento detalla la implementación y las pruebas para el microservicio de autenticación, responsable de registrar nuevos solicitantes en el sistema.

## Prerrequisitos
- El microservicio debe estar ejecutándose en `localhost:8080`.
- La base de datos PostgreSQL debe estar activa y accesible.
- La tabla `users` debe existir (creada automáticamente por Flyway al iniciar la aplicación).

---
## Cómo Probar la API 🧪

Puedes probar el endpoint de creación de usuarios usando Swagger UI (interactivo) o Postman (manual).

### Usando Swagger UI
1.  **Abre la URL** en tu navegador: `http://localhost:8080/swagger-ui.html`
2.  **Busca el endpoint** `POST /api/v1/users`.
3.  **Haz clic en "Try it out"**.
4.  **Modifica el JSON de ejemplo** en el campo "Request body" con los datos del nuevo solicitante.
5.  **Haz clic en "Execute"**. La respuesta aparecerá abajo.



### Usando Postman

#### 1. Petición Exitosa (201 Created)
Esta petición creará un nuevo usuario correctamente.

- **Método:** `POST`
- **URL:** `http://localhost:8080/api/v1/users`
- **Headers:**
    - `Content-Type`: `application/json`
- **Body** (raw, JSON):
```json
{
  "firstName": "Daniel",
  "lastName": "Agudelo",
  "birthDate": "1990-05-15",
  "password": "PasswordSeguro123!",
  "address": "Calle 10 # 43A-20",
  "phoneNumber": "3012345678",
  "email": "daniel.agudelo@example.com",
  "baseSalary": 4500000
}

## Ejemplos de Peticiones y Respuestas

### 1. Petición Exitosa (201 Created)

**Body de la petición:**
```json
{
  "firstName": "Daniel",
  "lastName": "Agudelo",
  "birthDate": "1990-05-15",
  "password": "PasswordSeguro123!",
  "address": "Calle 10 # 43A-20",
  "phoneNumber": "3012345678",
  "email": "daniel.agudelo@example.com",
  "baseSalary": 4500000
}

Respuesta Esperada (Status 201 Created):

{
    "id": 1,
    "firstName": "Daniel",
    "lastName": "Agudelo",
    "birthDate": "1990-05-15",
    "password": null,
    "role": "APPLICANT",
    "address": "Calle 10 # 43A-20",
    "phoneNumber": "3012345678",
    "email": "daniel.agudelo@example.com",
    "baseSalary": 4500000.00
}

Nota: La contraseña se retorna como null por seguridad.

<hr></hr>
2. Petición Fallida (400 Bad Request)
Esta petición fallará porque el email ya está registrado.

Body de la petición:

{
  "firstName": "Otro",
  "lastName": "Usuario",
  "birthDate": "1992-08-20",
  "password": "OtraPassword456!",
  "address": "Carrera 70 # 1-10",
  "phoneNumber": "3219876543",
  "email": "daniel.agudelo@example.com",
  "baseSalary": 3000000
}

Respuesta Esperada (Status 400 Bad Request):

{
    "timestamp": "2025-08-25T19:25:00.123456Z",
    "status": 400,
    "error": "Bad Request",
    "message": "El correo electrónico ya está en uso."
}

Token de Cliente

Asegúrate de tener un usuario con el rol APPLICANT (o CLIENTE) en la base de datos de ms-authentication. Si no tienes uno, usa el INSERT que te di anteriormente.

En Postman, haz una petición POST a http://localhost:8080/api/v1/login con las credenciales de ese cliente.

Copia el token JWT que recibas. Guárdalo como "TOKEN_CLIENTE_A".

Token de Administrador

Haz una petición POST a http://localhost:8080/api/v1/login con las credenciales del usuario admin@domain.com.

Copia este token JWT y guárdalo como "TOKEN_ADMIN".

2. Prueba los Escenarios en ms-requests
Ahora, cambia al microservicio ms-requests (que debe estar corriendo, usualmente en el puerto 8081).

✅ Caso 1: Creación Exitosa (Cliente A para sí mismo)

Método: POST

URL: http://localhost:8081/api/v1/requests

Authorization: Selecciona Bearer Token y pega el TOKEN_CLIENTE_A.

Body (JSON): Asegúrate de que el documentNumber coincida con el del cliente A.

JSON
{
  "documentNumber": "112233",
  "amount": 5000000,
  "loanTypeId": 1
}
Resultado Esperado: Un 201 Created o un 200 OK. La solicitud se crea exitosamente porque el rol es correcto y el documento del token coincide con el del body.

❌ Caso 2: Fallo por Rol (Admin intenta crear)

Método y URL: Los mismos que en el caso 1.

Authorization: Selecciona Bearer Token y pega el TOKEN_ADMIN.

Body (JSON): El mismo que en el caso 1.

Resultado Esperado: Un 403 Forbidden. Spring Security bloqueará la petición porque el token no tiene el rol CLIENTE.

❌ Caso 3: Fallo por Propiedad (Cliente A para Cliente B)

Método y URL: Los mismos.

Authorization: Selecciona Bearer Token y pega el TOKEN_CLIENTE_A.

Body (JSON): Usa un documentNumber que no corresponda al del Cliente A.

JSON
{
  "documentNumber": "999999", // Un documento diferente
  "amount": 2000000,
  "loanTypeId": 2
}
Resultado Esperado: Un 4xx (probablemente 400 Bad Request o 401 Unauthorized, dependiendo de cómo mapees la BusinessException) con el cuerpo del error:

JSON
{
    "error": "No tiene permisos para crear una solicitud para otro cliente."
}
Esto confirma que la validación dentro de tu UseCase está funcionando correctamente.