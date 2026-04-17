## GameCardUNO

Este juego consiste en un enfrentamiento entre 1 CPU y 1 jugador, de donde se tiene que seleccionar una carta adecuada en la mesa y asi consecutivamente, el jugador que se quede sin cartas, es el ganador.

## Instrucciones

Los jugadores obtienen 7 cartas en su mesa, aparecerá una carta random en el mazo y los jugadores escogeran sus cartas por el color/número adecuado, en cada turno los jugadores tendrán que elegir sus cartas correctamente hasta vaciar sus cartas por completo.

## Mejoras en la actualización 1.1 (09/03/2026)
* Implementación de comandos con restricción:
- Al añadir un valor negativo (ejemplo: -4) la jugada no se altera y no pierde turno.
- Al NO tener cartas jugables el usuario P1 puede robar cartas de forma manual al poner "NO", si hay cartas jugables, no es válido y no pierde turno.

* Esta versión NO cuenta con cartas especiales.
* Por problemas, el efecto especial de mostrar color en las letras de cartas ha sido eliminado.
* También el comando do/while ha sido eliminado para evitar saturaciones en el proceso.

## Diagrama UML (Versión 1.1):
![image alt]()

## ¿Como puedo jugar con esa versión de código?
Sigue estos pasos:

1. Descarga el archivo .zip y descomprimelo.
2. Obten a la mano un programa ejecutable con lenguaje de código java a tu comodidad (Microsoft VS Code, Apache NetBeans, etc.. "También debes contar con el Java JDK reciente instalado en tu PC.
3. Crea un proyecto nuevo y ve a la carpeta descomprimida.
4. Copia todos los archivos a la carpeta de proyectos de origen de tu programa ejecutable.
5. Abre todos los archivos .java de la carpeta "src", ejecutalos y ¡ENJOY IT!