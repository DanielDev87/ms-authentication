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
- **Endpoint:** `POST /api/v1/usuarios`

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
