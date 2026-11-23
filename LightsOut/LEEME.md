# Proyecto Lights Out (Android, Kotlin)

**Qué se entrega:**
1. El proyecto `LightsOut` (carpeta o `.zip`).
2. La memoria en **PDF**: abre `Memoria_LightsOut.odt`, pon tu nombre y la fecha, mete las 3 capturas en los huecos y expórtala con *Archivo → Exportar como PDF*.
3. La defensa oral de 10 minutos (abajo tienes una chuleta).

## De qué va
Es un tablero de botones. Al pulsar uno, cambia de color él y sus vecinos de arriba, abajo, izquierda y derecha (no las diagonales). Empiezan todos en **gris (color primario)** y ganas cuando están todos en **amarillo (color secundario)**.

## Qué tiene hecho
### Requisitos básicos (4 puntos)
| Requisito | Dónde está |
|---|---|
| Tablero 3x3 adaptado a la pantalla | `activity_main.xml`: `GridLayout` con ratio 1:1 dentro de un `ConstraintLayout` |
| Lógica de vecinos y cambio de color | `LightsOutGame.pulsar()` / `alternar()` |
| Botón Reiniciar (en rojo, bien diferenciado) | `btnReiniciar` → `juego.reiniciar()` |
| Detección de victoria con aviso | `hayVictoria()` + `AlertDialog` "¡Has ganado!" |

### Mejoras hechas (hasta 4 puntos)
| Mejora | Puntos | Cómo |
|---|---|---|
| Contador de movimientos y tiempo | 1 | `tvMovimientos` y `tvTiempo` (Handler + `SystemClock`) |
| Nueva partida aleatoria con solución | 1 | `nuevaPartida()`: N clics aleatorios desde el estado inicial |
| Vibración y sonido | 1 | `Vibrator` de 30 ms + `ToneGenerator` (pitido al pulsar y al ganar) |
| Tamaños 3x3, 4x4 y 5x5 | 2 | `Spinner`. El tablero se crea por código según el tamaño |

En total las mejoras suman 5 puntos, pero cuentan hasta 4, así que vas sobrado. **Ojo: sin defensa no te cuentan las mejoras.**

Extra: si giras la pantalla no se pierde la partida (`onSaveInstanceState`).

## Cómo abrirlo
1. Android Studio → **File → Open** → carpeta `LightsOut` → espera al *Gradle Sync*.
2. Ejecuta en el emulador o en tu móvil. Para notar la vibración tiene que ser un móvil real.
3. Para la memoria, haz las capturas con el botón de cámara del emulador:
   - **Inicial:** nada más abrir (todo gris).
   - **Intermedia:** después de pulsar 2 o 3 casillas.
   - **Victoria:** en 3x3, pulsa las 4 esquinas y el centro. Con eso ganas desde el estado inicial, y es una forma rápida de sacar la captura del mensaje.

> No pude compilarlo en mi entorno porque no tengo el SDK de Android. Si Android Studio te pide actualizar Gradle o el plugin, acepta.

## Archivos
| Archivo | Qué hace |
|---|---|
| `LightsOutGame.kt` | Solo la lógica: matriz, vecinos, victoria, reiniciar y nueva partida. Sin nada de Android. |
| `MainActivity.kt` | Interfaz: crea los botones, listeners, colores, temporizador, vibración, sonido y guardado de estado. |
| `res/layout/activity_main.xml` | Pantalla: título, Spinner, contadores, tablero y botones. |
| `res/values/colors.xml` | Color primario `#37474F` y secundario `#FFC107`. |
| `AndroidManifest.xml` | Permiso `VIBRATE`. |

## Chuleta para la defensa
**¿Cómo mapeas la matriz?**
Uso un `BooleanArray` de tamaño×tamaño. La casilla (fila, col) está en `fila * tamano + col`. `false` es el color primario y `true` el secundario.

**¿Cómo funciona el algoritmo de adyacencia?**
Tengo un array de desplazamientos: propia (0,0), arriba (-1,0), abajo (1,0), izquierda (0,-1) y derecha (0,1). Para cada uno sumo a la fila y la columna y, **si está dentro del tablero** (`f in 0 until tamano`), invierto el valor con `!`. Así las esquinas cambian 3 casillas, los bordes 4 y el centro 5, sin hacer `if` para cada caso.

**¿Cómo detectas la victoria?**
Después de cada clic llamo a `hayVictoria()`, que hace `casillas.all { it }`: devuelve true si todas son true.

**¿Por qué la nueva partida siempre tiene solución?**
Porque parto del estado inicial y aplico clics aleatorios. Cada clic se deshace pulsando la misma casilla otra vez, así que siempre se puede volver atrás.

**¿Cómo gestionas los listeners?**
Cada botón se crea en un bucle y tiene `setOnClickListener { alPulsarCasilla(fila, col) }`. La lambda "captura" la fila y la columna de ese botón. El Spinner usa `OnItemSelectedListener`.

**¿Y el ciclo de vida?**
Al girar la pantalla la Activity se destruye y se vuelve a crear (`onCreate` otra vez). Guardo el estado en `onSaveInstanceState(Bundle)` y lo recupero en `onCreate(savedInstanceState)`. En `onDestroy` quito los callbacks del Handler y libero el `ToneGenerator` para no dejar recursos abiertos.

**¿Cómo haces el tablero cuadrado?**
El `GridLayout` tiene `layout_constraintDimensionRatio="1:1"` con ancho y alto a `0dp` (match constraint). Cada botón tiene peso 1 en fila y columna (`GridLayout.spec(fila, 1f)`), así que se reparten el espacio por igual.

**¿Cómo cambias el color?**
`boton.backgroundTintList = ColorStateList.valueOf(color)`, con los colores sacados de `colors.xml` mediante `ContextCompat.getColor`.
