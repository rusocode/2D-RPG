package com.craivet.classes;

import com.craivet.utils.Utils;

/**
 * En los oscuros callejones y rincones sombrios de la tierra medieval, el Ladron se desliza como una sombra misma. Este
 * habilidoso delincuente no es un mago, pero posee una habilidad que parece magica: puede desaparecer de tu vista antes
 * de que te des cuenta, dejandote con las manos vacias y sin haber notado su partida. La destreza y sigilo son sus
 * armas mas poderosas.
 * <p>
 * Nunca confies en ellos, pues son maestros en el arte de la mentira y la engañosa cortesia. Pueden presentarse como
 * amigos y compañeros leales, ocultando sus verdaderas intenciones bajo una mascara de camaraderia. Sus palabras son
 * dulces como la miel, pero su corazon es tan oscuro como la noche en la que operan.
 * <p>
 * Cuerdas de seda para escalar muros, ganzuas para abrir cerraduras, y dagas afiladas como colmillos de serpiente,
 * suelen ser el equipo ideal del Ladron. Se deslizan silenciosamente por los techos y las sombras de los bosques,
 * acechando a sus presas con ojos avizores. Ninguna boveda es segura, ningún tesoro esta fuera de su alcance.
 * <p>
 * Pero ten cuidado, pues si cruzas el camino de un Ladron, tu riqueza y posesiones pueden desvanecerse como el humo. En
 * un mundo medieval donde la traicion y el engaño estan a la orden del dia, el Ladron es un maestro en su oficio, un
 * enigma envuelto en sombras y misterio.
 */

public class Thief extends Character {

    private static Thief instance = null;

    public static Thief getInstance() {
        if (instance == null) instance = new Thief();
        return instance;
    }

    protected Thief() {
        this.name = "Thief";
    }

    protected int getIncreaseMaxHp() {
        return 4;
        // return Utils.random(6);
    }

}
