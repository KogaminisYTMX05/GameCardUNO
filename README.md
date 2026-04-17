## GameCardUNO

Este juego consiste en un enfrentamiento entre 1 CPU y 1 jugador, de donde se tiene que seleccionar una carta adecuada en la mesa y asi consecutivamente, el jugador que se quede sin cartas, es el ganador.

## Instrucciones

Los jugadores obtienen 7 cartas en su mesa, aparecerá una carta random en el mazo y los jugadores escogeran sus cartas por el color/número adecuado, en cada turno los jugadores tendrán que elegir sus cartas correctamente hasta vaciar sus cartas por completo.

## Cartas especiales:
- ROBA2: El siguiente jugador roba 2 cartas y pierde su turno.
- ROBA4: El siguiente jugador roba 4 cartas y pierde su turno.
- REVERSA: Cambia al sentido contrario de todos los turnos.
- SALTO: Anula el turno del siguiente jugador.
- COMODIN (CAMBIO_COLOR): El jugador eligirá un color distinto (rojo, amarllo, verde y azúl) y el siguiente tendrá que usar una carta del color indicado.

## Mejoras en la actualización 2.0 (17/03/2026)
* Implementación de comandos con restricción:
- El jugador tiene que decir UNO cuando este tenga una sola carta, sanción al no decirlo: Robar 1 carta.
- Se implementó las cartas especiales (ROBA 2, ROBA 4, SALTO, REVERSA, COMODIN (cambio de color)).

## Diagrama UML (Versión 2):
![image alt](https://github.com/KogaminisYTMX05/GameCardUNO/blob/1c060ae7557c2af41bfa679aea7167d1694b985c/assets/uml_complete_v2.0.png)

## Mejoras en la actualización 1.1 (09/03/2026)
* Implementación de comandos con restricción:
- Al añadir un valor negativo (ejemplo: -4) la jugada no se altera y no pierde turno.
- Al NO tener cartas jugables el usuario P1 puede robar cartas de forma manual al poner "NO", si hay cartas jugables, no es válido y no pierde turno.

* Esta versión NO cuenta con cartas especiales.
* Por problemas, el efecto especial de mostrar color en las letras de cartas ha sido eliminado.
* También el comando do/while ha sido eliminado para evitar saturaciones en el proceso.

## Diagrama UML (Versión 1.1):
![image alt](https://github.com/KogaminisYTMX05/GameCardUNO/blob/6306ba8d6e1566fb5487af1e19ec5dd491091bdb/assets/uml_complete_v1.1.png)

## ¿Como puedo jugar con esa versión de código?
Sigue estos pasos:

1. Descarga el archivo .zip y descomprimelo: https://github.com/user-attachments/files/26837912/GameCardUNO_v2.0.zip
2. Obten a la mano un programa ejecutable con lenguaje de código java a tu comodidad (Microsoft VS Code, Apache NetBeans, etc.. "También debes contar con el Java JDK reciente instalado en tu PC.
3. Crea un proyecto nuevo y ve a la carpeta descomprimida.
4. Copia todos los archivos a la carpeta de proyectos de origen de tu programa ejecutable.
5. Abre todos los archivos .java de la carpeta "src", ejecutalos y ¡ENJOY IT!
