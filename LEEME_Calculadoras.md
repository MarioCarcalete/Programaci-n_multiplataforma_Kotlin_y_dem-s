# App Calculadoras: bisiestos y temperaturas

**Asignatura:** Programación Multimedia y Dispositivos Móviles (Kotlin)
**Qué se entrega:** la carpeta `CalculadorasApp` (proyecto de Android Studio), o el `.zip`

## De qué va
Una app con una pantalla principal (`MainActivity`) que tiene dos botones. Cada uno abre una actividad distinta:
1. **Calculadora de bisiestos**: escribes un año y te dice si es bisiesto o no.
2. **Calculadora de temperaturas**: escribes una temperatura, eliges la unidad en la que está y la unidad a la que quieres pasarla (Celsius, Fahrenheit o Kelvin).

## Cómo abrirla
1. Android Studio → **File → Open** → selecciona la carpeta `CalculadorasApp`.
2. Espera a que termine el *Gradle Sync*. La primera vez descarga cosas y tarda un poco.
3. Dale a ▶ con un emulador o con tu móvil conectado.

> No pude compilarla en mi entorno porque no tengo el SDK de Android. El código está revisado, pero si al sincronizar te pide actualizar Gradle o el plugin, acepta y listo.

## Archivos importantes
| Archivo | Qué hace |
|---|---|
| `MainActivity.kt` | Menú con dos botones. Cada uno lanza su actividad con un `Intent`. |
| `BisiestoActivity.kt` | Lee el año, lo valida y aplica la regla de los bisiestos. |
| `TemperaturaActivity.kt` | Lee el valor y las dos unidades de los `Spinner` y convierte. |
| `res/layout/*.xml` | Las tres pantallas, hechas con `ConstraintLayout`. |
| `res/values/strings.xml` | Todos los textos y la lista de unidades (`string-array`). |
| `AndroidManifest.xml` | Declara las 3 actividades. Solo `MainActivity` es la de inicio (LAUNCHER). |

## Lógica explicada
**Bisiesto:** un año es bisiesto si es divisible entre 4 y no entre 100, **o** si es divisible entre 400.
```kotlin
(anio % 4 == 0 && anio % 100 != 0) || anio % 400 == 0
```
Ejemplos: 2024 sí es bisiesto, 1900 no y 2000 sí. Si el resultado es "sí", sale en verde, y si es "no", en rojo.

**Temperaturas:** para no hacer 6 fórmulas distintas, primero se pasa todo a **Celsius** y luego de Celsius a la unidad destino.
- F → C: `(F - 32) * 5 / 9`
- K → C: `K - 273.15`
- C → F: `C * 9 / 5 + 32`
- C → K: `C + 273.15`

## Detalles que puedes comentar si te preguntan
- **Validaciones:** si el campo está vacío o el año no es válido, se muestra un error en el propio `EditText` con `.error`. En temperaturas, no deja poner nada por debajo del cero absoluto (-273,15 °C).
- Se acepta **coma o punto** como decimal.
- El botón de **volver** de la barra superior (`setDisplayHomeAsUpEnabled` + `onSupportNavigateUp`) cierra la actividad y regresa al menú.
- Los textos están en `strings.xml`, no escritos directamente en el código.

## Para probar
| Prueba | Resultado esperado |
|---|---|
| 2024 | es bisiesto |
| 1900 | no es bisiesto |
| 2000 | es bisiesto |
| 100 °C → °F | 212,00 °F |
| 0 °C → K | 273,15 K |
| -300 °C → K | error de cero absoluto |
