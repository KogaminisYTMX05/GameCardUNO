package juegouno;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    // ===== CAJA NEGRA =====
    @Test
    void test1() {
        Card mesa = new Card(Card.Color.ROJO, 5);
        Card jugada = new Card(Card.Color.ROJO, 1);
        assertTrue(jugada.esJugable(mesa, Card.Color.ROJO));
    }

    @Test
    void test2() {
        Card mesa = new Card(Card.Color.ROJO, 5);
        Card jugada = new Card(Card.Color.AZUL, 5);
        assertTrue(jugada.esJugable(mesa, Card.Color.ROJO));
    }

    @Test
    void test3() {
        Card mesa = new Card(Card.Color.ROJO, 5);
        Card jugada = new Card(Card.Comodin.CAMBIO_COLOR);
        assertTrue(jugada.esJugable(mesa, Card.Color.ROJO));
    }

    @Test
    void test4() {
        Card mesa = new Card(Card.Color.ROJO, 5);
        Card jugada = new Card(Card.Color.AZUL, 7);
        assertFalse(jugada.esJugable(mesa, Card.Color.ROJO));
    }

    // ===== CAJA BLANCA =====
    @Test
    void test5() {
        Card mesa = new Card(Card.Color.ROJO, 5);
        Card jugada = new Card(Card.Color.ROJO, 5);
        assertTrue(jugada.esJugable(mesa, Card.Color.ROJO));
    }

    @Test
    void test6() {
        Card mesa = new Card(Card.Color.ROJO, Card.Accion.SALTO);
        Card jugada = new Card(Card.Color.AZUL, Card.Accion.SALTO);
        assertTrue(jugada.esJugable(mesa, Card.Color.ROJO));
    }

    // ===== DECISIÓN =====
    @Test
    void test7() {
        Card mesa = new Card(Card.Color.ROJO, 5);
        Card jugada = new Card(Card.Color.ROJO, 2);
        assertTrue(jugada.esJugable(mesa, Card.Color.ROJO));
    }

    @Test
    void test8() {
        Card mesa = new Card(Card.Color.ROJO, 5);
        Card jugada = new Card(Card.Color.AZUL, 2);
        assertFalse(jugada.esJugable(mesa, Card.Color.ROJO));
    }

    // ===== COBERTURA =====
    @Test
    void test9() {
        Card mesa = new Card(Card.Color.VERDE, 3);
        Card jugada = new Card(Card.Color.VERDE, 7);
        assertTrue(jugada.esJugable(mesa, Card.Color.VERDE));
    }

    @Test
    void test10() {
        Card mesa = new Card(Card.Color.VERDE, 3);
        Card jugada = new Card(Card.Color.ROJO, 3);
        assertTrue(jugada.esJugable(mesa, Card.Color.VERDE));
    }

    // ===== EXTRA PARA LLEGAR A 24 =====
    @Test
    void test11() {
        Card mesa = new Card(Card.Color.AZUL, 9);
        Card jugada = new Card(Card.Color.AZUL, 2);
        assertTrue(jugada.esJugable(mesa, Card.Color.AZUL));
    }

    @Test
    void test12() {
        Card mesa = new Card(Card.Color.AZUL, 9);
        Card jugada = new Card(Card.Color.ROJO, 9);
        assertTrue(jugada.esJugable(mesa, Card.Color.AZUL));
    }

    @Test
    void test13() {
        Card mesa = new Card(Card.Color.AMARILLO, 1);
        Card jugada = new Card(Card.Color.VERDE, 5);
        assertFalse(jugada.esJugable(mesa, Card.Color.AMARILLO));
    }

    @Test
    void test14() {
        Card mesa = new Card(Card.Color.AMARILLO, 1);
        Card jugada = new Card(Card.Comodin.ROBA4);
        assertTrue(jugada.esJugable(mesa, Card.Color.AMARILLO));
    }

    @Test
    void test15() {
        Card mesa = new Card(Card.Color.ROJO, Card.Accion.REVERSA);
        Card jugada = new Card(Card.Color.AZUL, Card.Accion.REVERSA);
        assertTrue(jugada.esJugable(mesa, Card.Color.ROJO));
    }

    @Test
    void test16() {
        Card mesa = new Card(Card.Color.ROJO, Card.Accion.REVERSA);
        Card jugada = new Card(Card.Color.AZUL, Card.Accion.SALTO);
        assertFalse(jugada.esJugable(mesa, Card.Color.ROJO));
    }

    @Test
    void test17() {
        Card mesa = new Card(Card.Color.VERDE, 6);
        Card jugada = new Card(Card.Color.VERDE, 6);
        assertTrue(jugada.esJugable(mesa, Card.Color.VERDE));
    }

    @Test
    void test18() {
        Card mesa = new Card(Card.Color.VERDE, 6);
        Card jugada = new Card(Card.Color.ROJO, 8);
        assertFalse(jugada.esJugable(mesa, Card.Color.VERDE));
    }

    @Test
    void test19() {
        Card mesa = new Card(Card.Color.AZUL, 4);
        Card jugada = new Card(Card.Color.AZUL, 4);
        assertTrue(jugada.esJugable(mesa, Card.Color.AZUL));
    }

    @Test
    void test20() {
        Card mesa = new Card(Card.Color.AZUL, 4);
        Card jugada = new Card(Card.Color.VERDE, 4);
        assertTrue(jugada.esJugable(mesa, Card.Color.AZUL));
    }

    @Test
    void test21() {
        Card mesa = new Card(Card.Color.ROJO, 2);
        Card jugada = new Card(Card.Color.AMARILLO, 3);
        assertFalse(jugada.esJugable(mesa, Card.Color.ROJO));
    }

    @Test
    void test22() {
        Card mesa = new Card(Card.Color.ROJO, 2);
        Card jugada = new Card(Card.Comodin.CAMBIO_COLOR);
        assertTrue(jugada.esJugable(mesa, Card.Color.ROJO));
    }

    @Test
    void test23() {
        Card mesa = new Card(Card.Color.AMARILLO, 7);
        Card jugada = new Card(Card.Color.AMARILLO, 1);
        assertTrue(jugada.esJugable(mesa, Card.Color.AMARILLO));
    }

    @Test
    void test24() {
        Card mesa = new Card(Card.Color.AMARILLO, 7);
        Card jugada = new Card(Card.Color.VERDE, 8);
        assertFalse(jugada.esJugable(mesa, Card.Color.AMARILLO));
    }
}