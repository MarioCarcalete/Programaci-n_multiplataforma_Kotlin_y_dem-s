# App Selector de color (registerForActivityResult)

**Qué se entrega:** el proyecto `SelectorColor` (carpeta o `.zip`).

## De qué va
Hay dos pantallas:
1. **MainActivity:** muestra "Color actual: Predeterminado" y un botón **"Elegir Color de Fondo"**.
2. **ColorPickerActivity:** tres botones (**Rojo, Verde, Azul**). Al pulsar uno, devuelve el color a la primera pantalla y se cierra.

La pantalla principal cambia su fondo al color elegido y el texto pasa a ser, por ejemplo, "Color actual: Verde". Si vuelves atrás sin elegir nada, no cambia nada y no da error.

## Cómo abrirlo
Android Studio → **File → Open** → carpeta `SelectorColor` → espera al *Gradle Sync* → ▶.

> No pude compilarlo en mi entorno porque no tengo el SDK de Android. Si Android Studio te pide actualizar Gradle o el plugin, acepta.

## Cómo funciona, paso a paso
**1. Registrar el launcher (MainActivity):**
```kotlin
private val elegirColorLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
        if (resultado.resultCode == Activity.RESULT_OK) { ... }
    }
```
Se declara como **propiedad de la clase**, porque `registerForActivityResult` tiene que llamarse antes de que la Activity arranque. Si se llama dentro de un clic, la app falla.

**2. Abrir la segunda pantalla:**
```kotlin
elegirColorLauncher.launch(Intent(this, ColorPickerActivity::class.java))
```

**3. Devolver el resultado (ColorPickerActivity):**
```kotlin
val intent = Intent()
intent.putExtra("COLOR_HEX", "#4CAF50")
intent.putExtra("COLOR_NAME", "Verde")
setResult(Activity.RESULT_OK, intent)
finish()
```

**4. Recibirlo:** en la lambda del launcher se leen los extras con `getStringExtra`, se cambia el fondo con `setBackgroundColor(Color.parseColor(hex))` y se actualiza el texto.

**5. Botón atrás:** si el usuario sale sin elegir, Android devuelve `RESULT_CANCELED`. Como solo actuamos con `RESULT_OK`, el color se queda igual.

## Colores usados
| Botón | COLOR_HEX | COLOR_NAME |
|---|---|---|
| Rojo | `#F44336` | Rojo |
| Verde | `#4CAF50` | Verde |
| Azul | `#2196F3` | Azul |

## Detalles extra
- Las claves `"COLOR_HEX"` y `"COLOR_NAME"` están en constantes (`ColorPickerActivity.EXTRA_COLOR_HEX`) para no equivocarse escribiéndolas.
- Si giras la pantalla, el color no se pierde (`onSaveInstanceState`).
- `Color.parseColor` va dentro de un `try` por si llegara un código no válido.

## Si te preguntan
- **¿Por qué no `startActivityForResult`?** Está obsoleto (*deprecated*). La API nueva separa el contrato (qué se pide) del callback (qué hacer con la respuesta) y funciona bien con el ciclo de vida.
- **¿Qué es `StartActivityForResult()`?** Un contrato genérico: recibe un `Intent` y devuelve un `ActivityResult`, que contiene `resultCode` y `data`.
