package com.punkipunk.assets;

import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.entity.item.IronAxe;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

import java.net.URL;

/**
 * <p>
 * Aplicar el patron de fachada (facade pattern) a la estructura de assets puede ayudar a mantener una interfaz simple para
 * acceder a los assets mientras se organiza el codigo de manera mas modular.
 * <p>
 * Aqui esta la explicacion de como se aplica el patron de fachada en este caso:
 * <ol>
 * <li><b>Separacion de responsabilidades</b>:
 * Dividimos los assets en clases especificas ({@link FontAssets}, {@link FontAssets}, {@link SpriteSheetAssets},
 * {@link TextureAssets}). Cada una maneja un tipo especifico de asset.
 * <li><b>Clase fachada</b>: Creamos {@link Assets}, que proporciona una interfaz simplificada para acceder a todos los
 * assets.</b>
 * <li><b>Metodos de acceso</b>: La fachada ofrece metodos como {@code getAudio()} y {@code getFont()}. Estos metodos actuan como
 * punto unico de acceso para todos los assets.
 * <li><b>Encapsulacion</b>: Los detalles de como se almacenan y organizan los assets estan ocultos detras de la fachada. El
 * programador solo necesita conocer el nombre del assets que quiere usar.
 * <li><b>Flexibilidad</b>: Si necesitamos cambiar como se cargan o almacenan los assets, solo necesitamos modificar las clases
 * especificas y la fachada, sin afectar al codigo que utiliza los assets.
 * <li><b>Uso simplificado</b>: Por ejemplo, como se muestra en la clase {@link IronAxe Axe}, el uso de
 * assets se vuelve muy simple y consistente: {@code Assets.getTexture(TextureAssets.AXE)} reemplazando a {@code Assets.axe}.
 * Aunque esta diferencia no parece beneficiosa, ya que la nueva forma es mas larga de escribir, si es simple en terminos de
 * consistencia.
 * </ol>
 * <p>
 * Beneficios de este enfoque:
 * <ul>
 * <li><b>Organizacion mejorada</b>: Los assets estan agrupados logicamente.
 * <li><b>Mantenibilidad</b>: Es mas facil añadir, modificar o eliminar assets sin afectar a todo el sistema.
 * <li><b>Interfaz simplificada</b>: Los objetos tienen una forma clara y consistente de acceder a los assets.
 * <li><b>Desacoplamiento</b>: El codigo que usa los assets no necesita saber como estan organizados internamente.
 * </ul>
 * <p>
 * Consideraciones adicionales:
 * <ul>
 * <li>Podrias usar enums en lugar de strings para los nombres de los assets, lo que proporcionaria mayor seguridad en tiempo de
 * compilacion.
 * <li>Si la carga de assets es costosa, podrias implementar carga perezosa (lazy loading) en la fachada.
 * <li>Para una mayor flexibilidad, podrias considerar usar un sistema de carga de assets basado en archivos de configuracion.
 * </ul>
 * <p>
 * Para muchos casos de uso, especialmente en proyectos mas pequeños o donde la estructura de assets es estable, la importacion
 * estatica directa ({@code import static TextureAssets.axe;}) es una solucion perfectamente valida y a menudo preferible por su
 * simplicidad.
 * <p>
 * El patron de fachada puede ser util en proyectos mas grandes o complejos donde:
 * <ul>
 * <li>Se anticipa que la gestion de assets cambiara significativamente con el tiempo.
 * <li>Se necesita añadir logica adicional alrededor del acceso a los assets (como lazy loading o logging).
 * <li>Se quiere desacoplar completamente el codigo del juego de la estructura de assets.
 * </ul>
 * <p>
 * Links:
 * <a href="https://refactoring.guru/es/design-patterns/facade">Facade</a>
 * <a href="https://es.wikipedia.org/wiki/Facade_(patr%C3%B3n_de_dise%C3%B1o)">Facade (patron de diseño)</a>
 *
 * @author Juan Debenedetti
 */

public final class Assets {

    private Assets() {
    }

    public static Font getFont(FontAssets font) {
        return FontAssets.getFont(font); // Delega la carga y la gestion real de los assets a la clase especializada FontAssets
    }

    public static SpriteSheet getSpriteSheet(SpriteSheetAssets spriteSheet) {
        return SpriteSheetAssets.getSpriteSheet(spriteSheet);
    }

    public static Image getTexture(TextureAssets textureAssets) {
        return TextureAssets.getTexture(textureAssets);
    }

}
