# ✂️ EliteCut — Barbershop Management App

EliteCut es una aplicación Android de gestión de barbería que permite a los clientes explorar barberos, agendar citas y comunicarse con el equipo, mientras que los administradores gestionan toda la operación desde un panel dedicado.

---
## 🚀 Funcionalidades

### 👤 Clientes
- **Login y Registro** con validación de campos (correo Gmail, contraseña, teléfono con formato, fecha de ingreso)
- **Home** con listado de barberos disponibles, búsqueda en tiempo real y navegación a su galería de estilos
- **Galería de Estilos** por barbero con grid de imágenes y botón directo para agendar
- **Agendar Cita** seleccionando fecha, horario disponible (con receso visual) y datos personales
- **Método de Pago** — tarjeta de crédito/débito (con validación de número, vencimiento y CVV) o pago en establecimiento
- **Confirmación de Cita** con resumen detallado del barbero, fecha, hora y método de pago
- **Mis Citas** con filtros por estado (Todas, Pendiente, Confirmada, Completada, Cancelada) y opción de cancelar
- **Detalle de Cita** con información completa del barbero, cliente y pago
- **Soporte** — chat en tiempo real con el equipo de administración
- **Perfil** con datos de la cuenta y cierre de sesión

### 🛠️ Administradores
- **Dashboard** con métricas clave: total de clientes, tickets vendidos, ingresos generados y actividad reciente
- **Gestión de Barberos** — crear, modificar y eliminar barberos con foto de perfil, galería de estilos, especialidad y disponibilidad
- **Gestión de Usuarios** — listado de clientes registrados con búsqueda y eliminación
- **Gestión de Citas** — visualización de todas las citas con cambio de estado
- **Soporte Admin** — bandeja de tickets de soporte con filtros por estado (Pendiente, Respondido, Cerrado) y chat de respuesta directa al cliente
- **Perfil** con datos de la cuenta y cierre de sesión

---

## 🏗️ Arquitectura

EliteCut sigue los principios de **Clean Architecture** con separación en tres capas:

```
app/
├── data/
│   ├── local/          # Room Database, DAOs, entidades
│   ├── remote/         # Retrofit, DTOs, DataSources
│   ├── mapper/         # Conversión entre capas
│   └── repository/     # Implementaciones de repositorios
├── domain/
│   ├── model/          # Modelos de dominio
│   ├── repository/     # Interfaces de repositorios
│   ├── usecase/        # Casos de uso
│   └── validation/     # Validaciones de negocio
└── presentation/
    ├── tareas/
    │   ├── auth/           # Login, Registro
    │   ├── administradores/ # Módulo admin
    │   └── clientes/       # Módulo cliente
    ├── utils/          # Utilidades compartidas (colores, date picker, etc.)
    └── components/     # Componentes reutilizables (BottomBars, formularios)
```

El patrón de UI utilizado es **MVI (Model-View-Intent)** mediante:
- `UiState` — estado inmutable de cada pantalla
- `UiEvent` — acciones del usuario
- `ViewModel` con `StateFlow` para exponer el estado

---

## 🛠️ Stack Tecnológico

| Categoría | Tecnología |
|---|---|
| Lenguaje | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Arquitectura | Clean Architecture + MVI |
| Inyección de dependencias | Hilt (Dagger) |
| Base de datos local | Room |
| Red | Retrofit + OkHttp |
| Imágenes asíncronas | Coil |
| Navegación | Navigation Compose |
| Estado | StateFlow + collectAsStateWithLifecycle |
| Persistencia de sesión | DataStore (TokenManager) |
| Offline-first | Estrategia local + sincronización con API |

---

## 👥 Equipo

| Rol | Nombre |
|---|---|
| Desarrollador |Jendri Hidalgo |

---
