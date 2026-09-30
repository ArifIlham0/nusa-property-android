---
trigger: always_on
---

# Project Overview & Context
- **Project Name**: NusaProperty
- **Description**: NusaProperty Android App untuk perusahaan NusaProperty yang terhubung dengan backend REST API.
- **Primary Tech Stack**: Kotlin, Jetpack Compose, Material 3, Retrofit/Ktor.

## Architecture & Folder Structure
- `app/src/main/java/com/nusaproperty/app/` : Root package aplikasi.
    - `data/` : Layer data (`model/`).
    - `ui/` : berisi `theme/`, `components/`, layer UI per fitur (`screens/`), definisi NavHost & route (`navigation/`).
- `app/src/main/res/` : Resource (`values/`, `drawable/`, `font/`, dll).

## Coding & Style Conventions
- Composable function: `PascalCase` (contoh: `HomeScreen`, `UserCard`).
- Class, interface, object, enum: `PascalCase`.
- Function, variable, property: `camelCase`.
- Konstanta: `UPPER_SNAKE_CASE` (di `companion object` atau top-level `const val`).
- Package name: lowercase, tanpa underscore (`feature.login`, bukan `feature_login`).
- Nama file mengikuti class utama di dalamnya (`LoginScreen.kt`, `UserRepository.kt`).
- Gunakan **Material 3** (`androidx.compose.material3.*`) sebagai design system default.
- Gunakan `Modifier` sebagai parameter pertama opsional di setiap Composable.
- Pisahkan Composable **stateful** (yang akses ViewModel) dan **stateless** (yang hanya terima parameter) untuk memudahkan preview & testing.
- Gunakan `@Preview` untuk setiap Composable UI utama.
- State di ViewModel pakai `StateFlow` / `MutableStateFlow`, collect di UI pakai `collectAsStateWithLifecycle()`.
- Side effect di Compose pakai `LaunchedEffect`, `rememberCoroutineScope`, atau `DisposableEffect` — bukan di dalam body Composable.
- Gunakan `sealed interface` / `sealed class` untuk `UiState` (Loading, Success, Error) dan hasil operasi (`Result`-style).

## Hard Guardrails (Dilarang Keras)
1. **DILARANG** mengubah file kredensial (`local.properties`, `keystore.jks`, `google-services.json`, `secrets.properties`) secara sepihak.
2. **DILARANG** menambahkan `@Composable` di `MainActivity` secara berlebihan — `MainActivity` hanya sebagai entry point + `setContent { AppTheme { AppNavHost() } }`.
3. **DILARANG** menginstal dependency Gradle baru (via `build.gradle.kts` / `libs.versions.toml`) tanpa konfirmasi terlebih dahulu.
4. **DILARANG** menghapus file existing atau mengubah struktur modul utama (misal pecah jadi multi-module) tanpa instruksi eksplisit.
5. **DILARANG** melakukan git commit, checkout branch baru, atau push otomatis.
7. **DILARANG** melakukan network call langsung di Composable atau di `init` Activity tanpa lifecycle-aware scope.