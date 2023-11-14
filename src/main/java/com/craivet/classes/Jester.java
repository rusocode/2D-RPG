package com.craivet.classes;

import com.craivet.utils.Utils;

/**
 * En los confines de la majestuosa corte medieval, donde la realeza y los nobles se codean con intrigas y secretos, el
 * bufon es una figura singular. Vestido con harapos multicolores, su apariencia extravagante y su rostro pintado con
 * una sonrisa perpetua ocultan su verdadera naturaleza. Es un personaje que persigue la ilusion de ser gracioso
 * mientras carga con el peso de la repulsion.
 * <p>
 * Este personaje bufonesco es conocido por su capacidad para encajar bien en cualquier situacion, aunque su humor a
 * menudo cae en el lado torpe y grotesco. La risa que provoca es mas bien una risa incomoda, donde los nobles rien no
 * por su ingenio, sino por la extraña y a menudo incomprensible serie de acrobacias y chistes que realiza.
 * <p>
 * El bufon se encuentra siempre cerca de los nobles y reyes, su presencia es obligada en los banquetes y festividades.
 * Pero detras de sus payasadas y suspirantes intentos de complacer a la corte, yace un astuto observador de la politica
 * palaciega. No desperdicia las oportunidades que la vida en la corte medieval le ofrece, y su lengua afilada puede ser
 * una fuente inesperada de informacion para aquellos que esten dispuestos a escuchar mas alla de sus chistes.
 * <p>
 * Sin embargo, su falta de filtro y su desprecio por las consecuencias pueden llevar a situaciones complicadas. Sus
 * bromas a menudo rozan la irreverencia y el sarcasmo, poniendo en peligro su posicion y, a veces, su vida. Pero el
 * bufon sigue adelante, con su sonrisa pintada y su bufonesca sabiduria, un enigma en la corte medieval que nunca se
 * resuelve por completo.
 */

public class Jester extends Character {

    private static Jester instance = null;

    public static Jester getInstance() {
        if (instance == null) instance = new Jester();
        return instance;
    }

    protected Jester() {
        this.name = "Jester";
        this.magic = true;
    }

    protected int getIncreaseMaxHp() {
        return 2;
        // return Utils.random(4);
    }

    protected int getIncreaseMaxMana() {
        return Utils.random(2);
    }

}
