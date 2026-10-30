# NotasApp

Aplicación Android de notas personales desarrollada con Kotlin, XML Layouts, MVVM, LiveData, Retrofit y OkHttp, conectada a un backend REST.

Evaluación Parcial II - Desarrollo de Aplicaciones Móviles (Virtual) - Universidad Da Vinci de Guatemala.

## Estructura

- `NotasApp/`: aplicación Android.
- `notas-backend/`: API REST con Node.js, Express, SQLite y JWT.

## Backend

Requisitos: Node.js y npm instalados.

Desde la raíz del repositorio, ejecutar:

```powershell
cd notas-backend
npm install
npm start
```

El servidor escucha en el puerto 3000. La base de datos `notas.db` se crea automáticamente al arrancar y no se incluye en el repositorio.

### Usuario de prueba

Con el servidor corriendo, abrir otra terminal de PowerShell y ejecutar:

```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:3000/api/auth/register" -ContentType "application/json" -Body '{"nombre":"Walter","email":"walter123@test.com","password":"123456"}'
```

Después, iniciar sesión en la aplicación con:

- Correo: `walter123@test.com`
- Contraseña: `123456`

El usuario debe registrarse una sola vez por base de datos. Las notas se crean desde la aplicación.

### Endpoints

| Método | Ruta | Autenticación | Descripción |
|---|---|---|---|
| POST | /api/auth/register | No | Crea un usuario |
| POST | /api/auth/login | No | Devuelve el token JWT y los datos del usuario |
| POST | /api/notas | Bearer token | Crea una nota del usuario autenticado |
| GET | /api/notas | Bearer token | Lista las notas del usuario autenticado |

### Base de datos

SQLite almacena los usuarios y sus notas de forma persistente. Cada nota está asociada a su usuario e incluye título, contenido y fecha de creación.

La estructura de las tablas se define en `notas-backend/src/db.js`.

## App Android

Abrir la carpeta `NotasApp` con Android Studio y esperar a que termine la sincronización de Gradle.

La URL del backend se configura en `BASE_URL`, dentro de:

`NotasApp/app/src/main/java/com/example/notasapp/network/RetrofitClient.kt`

### Emulador de Android Studio

Usar esta URL:

```kotlin
private const val BASE_URL = "http://10.0.2.2:3000/"
```

### Celular físico conectado mediante ADB

Usar esta URL:

```kotlin
private const val BASE_URL = "http://localhost:3000/"
```

Conectar el teléfono con la depuración USB habilitada y ejecutar:

```powershell
adb -d reverse tcp:3000 tcp:3000
```

Si PowerShell no reconoce `adb`, usar la ruta del SDK instalado por Android Studio:

```powershell
& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" -d reverse tcp:3000 tcp:3000
```

Volver a ejecutar el comando después de reconectar el teléfono. Mantener el backend corriendo durante las pruebas.

Seleccionar el teléfono en Android Studio y presionar Run.

## Pantallas

- Login: autenticación y mensajes de error.
- Dashboard: bienvenida con el nombre del usuario y acceso a las funciones.
- Crear nota: validación de campos y guardado en el backend.
- Listado de notas: consulta de las notas del usuario mediante RecyclerView y CardView.