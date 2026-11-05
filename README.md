# Apps Android con Kotlin (2º DAM)

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=flat&logo=android&logoColor=white)
![Android Studio](https://img.shields.io/badge/Android_Studio-3DDC84?style=flat&logo=androidstudio&logoColor=white)

Apps Android hechas en **Kotlin** para la asignatura de **Programación Multimedia y Dispositivos Móviles** del 2º curso de DAM. Todas usan vistas XML con `ConstraintLayout` y funcionan desde Android 7.0 (API 24).

## 📱 Apps

### 💡 [LightsOut](LightsOut): juego de lógica

Un tablero de luces: al pulsar una casilla cambian de color ella y sus vecinas (arriba, abajo, izquierda y derecha). Ganas cuando todas están encendidas.

- Tableros de **3x3, 4x4 y 5x5**, generados por código según el tamaño elegido.
- **Contador de movimientos y cronómetro**.
- **Nueva partida aleatoria** que siempre tiene solución.
- **Vibración y sonido** al pulsar y al ganar.
- La partida **no se pierde al girar la pantalla** (`onSaveInstanceState`).
- La lógica del juego (`LightsOutGame.kt`) está separada de la interfaz, sin nada de Android.

### 🧮 [CalculadorasApp](CalculadorasApp): bisiestos y temperaturas

Una pantalla principal con dos botones que abren dos actividades:
- **Calculadora de bisiestos**: escribes un año y te dice si es bisiesto.
- **Conversor de temperaturas** entre Celsius, Fahrenheit y Kelvin.

### 🎨 [SelectorColor](SelectorColor): comunicación entre actividades

La pantalla principal abre un selector de color (rojo, verde o azul) y recibe el color elegido con `registerForActivityResult` para cambiar el fondo. Si vuelves atrás sin elegir, no pasa nada.

## ▶️ Cómo abrirlas

1. Clona el repo: `git clone https://github.com/MarioCarcalete/Programaci-n_multiplataforma_Kotlin_y_dem-s.git`
2. En Android Studio: **File → Open** y elige la carpeta interior de la app (por ejemplo `LightsOut/LightsOut`).
3. Espera al *Gradle Sync* y ejecútala en el emulador o en tu móvil.

---

Hecho por [Mario Carcalete](https://github.com/MarioCarcalete) durante el 2º curso de DAM.
