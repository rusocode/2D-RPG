package com.punkipunk.gfx;

import com.punkipunk.entity.Entity;
import com.punkipunk.gfx.opengl.Texture;

import static com.punkipunk.utils.Global.tile;

/**
 * Sistema de sprite sheets para OpenGL.
 * <p>
 * En lugar de crear texturas separadas para cada frame (ineficiente), este sistema guarda una unica textura y las coordenadas UV
 * de cada frame.
 * <p>
 * TODO No deberia ser estaticos estos metodos?
 */

public class SpriteSheet {

    /**
     * Arrays de regiones (coordenadas UV) para cada frame. Antes se almacenaban N texturas en GPU (utilizando el array de tipo
     * Image), ahora se utiliza una textura (array de tipo SpriteRegion en donde se almacenan las coordenadas) y el objeto de tipo
     * Texture.
     */
    public SpriteRegion[] movement, attack, item, weapon;
    public SpriteRegion[] down, up, left, right; // Para entidades con multiples frames por direccion
    /** Frame actual (region). */
    public SpriteRegion frame; // TODO No tendria que ser un objeto de tipo Texture? Ya que solo almacena la primera imagen del sprite sheet
    /** Contadores de frames. */
    public int movementNum = 1, attackNum = 1;
    /** Textura completa del sprite sheet. */
    private Texture texture;

    public SpriteSheet() {
    }

    // TODO Este constructor ya seria innecesario ya que la textura se crea en los metodos especificados
    public SpriteSheet(Texture texture) {
        this.texture = texture;
    }

    /**
     * Extrae subimagenes de iconos de un sprite sheet.
     */
    public static SpriteRegion[] getIconsSubimages(Texture texture, int frameWidth, int frameHeight) {
        int cols = texture.getWidth() / frameWidth;
        int rows = texture.getHeight() / frameHeight;

        SpriteRegion[] subimages = new SpriteRegion[cols * rows];

        int i = 0;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                subimages[i++] = SpriteRegion.fromPixels(
                        x * frameWidth,
                        y * frameHeight,
                        frameWidth,
                        frameHeight,
                        texture.getWidth(),
                        texture.getHeight()
                );
            }
        }

        return subimages;
    }

    /**
     * Carga los frames de items desde un sprite sheet.
     */
    public void loadItemFrames(String spriteSheetPath, int frameWidth, int frameHeight, int scale) {
        texture = new Texture(spriteSheetPath);

        int cols = texture.getWidth() / frameWidth;
        int rows = texture.getHeight() / frameHeight;

        item = new SpriteRegion[cols * rows];

        int i = 0;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                item[i++] = SpriteRegion.fromPixels(
                        x * frameWidth,
                        y * frameHeight,
                        scale * tile,
                        scale * tile,
                        texture.getWidth(),
                        texture.getHeight()
                );
            }
        }

        frame = item[0];
    }

    /**
     * Carga los frames de movimiento desde un sprite sheet.
     * <p>
     * La textura se divide en una cuadricula uniforme de frames.
     *
     * @param spriteSheetPath ruta a la textura del sprite sheet
     * @param frameWidth      ancho de cada frame en pixeles
     * @param frameHeight     alto de cada frame en pixeles
     * @param scale           factor de escala para el renderizado
     */
    public void loadMovementFrames(String spriteSheetPath, int frameWidth, int frameHeight, int scale) {
        texture = new Texture(spriteSheetPath);

        int cols = texture.getWidth() / frameWidth;
        int rows = texture.getHeight() / frameHeight;

        movement = new SpriteRegion[cols * rows];

        int i = 0;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                movement[i++] = SpriteRegion.fromPixels(
                        x * frameWidth,
                        y * frameHeight,
                        frameWidth * scale,
                        frameHeight * scale,
                        texture.getWidth(),
                        texture.getHeight()
                );
            }
        }

        // Guarda el primer frame
        frame = movement[0];
    }

    /**
     * Carga los frames de ataque desde un sprite sheet.
     * <p>
     * Layout especial para frames de ataque (pueden ser de diferente tamaño).
     */
    public void loadAttackFrames(String spriteSheetPath, int frameWidth, int frameHeight, int scale) {
        texture = new Texture(spriteSheetPath);

        int cols = texture.getWidth() / frameWidth;
        int rows = texture.getHeight() / frameHeight;

        attack = new SpriteRegion[cols * rows];

        int i = 0;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (y < 2) {
                    // Frames verticales (2x altura)
                    attack[i++] = SpriteRegion.fromPixels(
                            x * frameWidth,
                            y == 0 ? 0 : frameHeight * 2,
                            tile * scale,
                            tile * scale * 2,
                            texture.getWidth(),
                            texture.getHeight()
                    );
                } else {
                    // Frames horizontales (2x ancho)
                    attack[i++] = SpriteRegion.fromPixels(
                            0,
                            y == 2 ? frameHeight * (4 + x) : frameHeight * (6 + x),
                            tile * scale * 2,
                            tile * scale,
                            texture.getWidth(),
                            texture.getHeight()
                    );
                }
            }
        }
    }

    /**
     * Carga los frames de movimiento del jugador (layout especial con 4 direcciones).
     */
    public void loadPlayerMovementFrames(String spriteSheetPath, int scale) {
        texture = new Texture(spriteSheetPath);

        final int cols = 6, rows = 4;
        final int framesDown = 6, framesUp = 6, framesLeft = 5, framesRight = 5;

        int w = texture.getWidth() / cols;
        int h = texture.getHeight() / rows;

        down = new SpriteRegion[framesDown];
        up = new SpriteRegion[framesUp];
        left = new SpriteRegion[framesLeft];
        right = new SpriteRegion[framesRight];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                switch (y) {
                    case 0 ->
                            down[x] = SpriteRegion.fromPixels(x * w, 0, w * scale, h * scale, texture.getWidth(), texture.getHeight());
                    case 1 ->
                            up[x] = SpriteRegion.fromPixels(x * w, y * h, w * scale, h * scale, texture.getWidth(), texture.getHeight());
                    case 2 -> {
                        if (x < framesLeft)
                            left[x] = SpriteRegion.fromPixels(x * w, y * h, w * scale, h * scale, texture.getWidth(), texture.getHeight());
                    }
                    case 3 -> {
                        if (x < framesRight)
                            right[x] = SpriteRegion.fromPixels(x * w, y * h, w * scale, h * scale, texture.getWidth(), texture.getHeight());
                    }
                }
            }
        }

        frame = down[0];
    }

    /**
     * Carga los frames de movimiento del Orco (layout similar al player).
     */
    public void loadOrcMovementFrames(String spriteSheetPath, int scale) {
        texture = new Texture(spriteSheetPath);

        final int cols = 6, rows = 4;
        int framesDown = 6, framesUp = 6, framesLeft = 5, framesRight = 5;

        int w = texture.getWidth() / cols;
        int h = texture.getHeight() / rows;

        down = new SpriteRegion[framesDown];
        up = new SpriteRegion[framesUp];
        left = new SpriteRegion[framesLeft];
        right = new SpriteRegion[framesRight];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                switch (y) {
                    case 0 ->
                            down[x] = SpriteRegion.fromPixels(x * w, 0, w * scale, h * scale, texture.getWidth(), texture.getHeight());
                    case 1 ->
                            up[x] = SpriteRegion.fromPixels(x * w, y * h, w * scale, h * scale, texture.getWidth(), texture.getHeight());
                    case 2 -> {
                        if (x < framesLeft)
                            left[x] = SpriteRegion.fromPixels(x * w, y * h, w * scale, h * scale, texture.getWidth(), texture.getHeight());
                    }
                    case 3 -> {
                        if (x < framesRight)
                            right[x] = SpriteRegion.fromPixels(x * w, y * h, w * scale, h * scale, texture.getWidth(), texture.getHeight());
                    }
                }
            }
        }

        frame = down[0];
    }

    /**
     * Carga frames del Oldman (NPC con 3 frames por dirección).
     */
    public void loadOldmanFrames(String texturePath, int scale) {
        texture = new Texture(texturePath);

        final int cols = 3, rows = 4, frames = 3;
        int w = texture.getWidth() / cols;
        int h = texture.getHeight() / rows;

        down = new SpriteRegion[frames];
        up = new SpriteRegion[frames];
        left = new SpriteRegion[frames];
        right = new SpriteRegion[frames];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                switch (y) {
                    case 0 ->
                            down[x] = SpriteRegion.fromPixels(x * w, 0, w * scale, h * scale, texture.getWidth(), texture.getHeight());
                    case 1 ->
                            up[x] = SpriteRegion.fromPixels(x * w, 48, w * scale, h * scale, texture.getWidth(), texture.getHeight());
                    case 2 ->
                            left[x] = SpriteRegion.fromPixels(x * w, 96, w * scale, h * scale, texture.getWidth(), texture.getHeight());
                    case 3 ->
                            right[x] = SpriteRegion.fromPixels(x * w, 144, w * scale, h * scale, texture.getWidth(), texture.getHeight());
                }
            }
        }

        frame = down[0];
    }

    /**
     * Carga frames de BurstOfFire (spell con layout especial).
     */
    public void loadBurstOfFireFrames(String texturePath, int scale) {
        texture = new Texture(texturePath);

        final int cols = 5, rows = 7, frames = 5;
        int w = texture.getWidth() / cols;
        int h = texture.getHeight() / rows;

        down = new SpriteRegion[frames];
        up = new SpriteRegion[frames];
        left = new SpriteRegion[frames];
        right = new SpriteRegion[frames];

        /* Indices para controlar los frames up y left, en donde estos empiezan desde el "ultimo" (en realidad es el
         * primer frame) frame en el SpriteSheet. */
        int iUp = 4, iLeft = 4;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                switch (y) {
                    case 0 ->
                            down[x] = SpriteRegion.fromPixels(0, x * w, w * scale, h * scale, texture.getWidth(), texture.getHeight());
                    case 1 ->
                            up[x] = SpriteRegion.fromPixels(w, iUp-- * w, w * scale, h * scale, texture.getWidth(), texture.getHeight());
                    case 2 ->
                            left[x] = SpriteRegion.fromPixels(iLeft-- * w, 160, w * scale, h * scale, texture.getWidth(), texture.getHeight());
                    case 3 ->
                            right[x] = SpriteRegion.fromPixels(x * w, 192, w * scale, h * scale, texture.getWidth(), texture.getHeight());
                }
            }
        }

        frame = up[0];
    }

    /**
     * Carga los frames de armas (animacion de ataque del player).
     */
    public void loadWeaponFrames(String texturePath, int frameWidth, int frameHeight, int scale) {
        texture = new Texture(texturePath);

        int cols = texture.getWidth() / frameWidth;
        int rows = texture.getHeight() / frameHeight;

        weapon = new SpriteRegion[cols * rows];

        int i = 0;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                weapon[i++] = SpriteRegion.fromPixels(
                        x * frameWidth,
                        y * frameHeight,
                        frameWidth * scale,
                        frameHeight * scale,
                        texture.getWidth(),
                        texture.getHeight()
                );
            }
        }
    }

    /**
     * Obtiene el frame actual de animacion con MAS de dos frames por direccion.
     */
    public SpriteRegion getCurrentAnimationFrame2(Entity entity) {
        switch (entity.direction) {
            case DOWN -> entity.currentFrame = entity.flags.colliding ? down[0] : down[entity.animationIndex];
            case UP -> entity.currentFrame = entity.flags.colliding ? up[0] : up[entity.animationIndex];
            case LEFT -> entity.currentFrame = entity.flags.colliding ? left[0] : left[entity.animationIndex];
            case RIGHT -> entity.currentFrame = entity.flags.colliding ? right[0] : right[entity.animationIndex];
        }
        return entity.currentFrame;
    }

    /**
     * Obtiene el frame actual de la animacion con dos frames para cada direccion.
     */
    public SpriteRegion getCurrentAnimationFrame(Entity entity) {
        int i = 0;

        // Si la entidad no esta atacando
        if (!entity.flags.hitting) {
            // Si la entidad tiene dos frames iguales para todas las direcciones (por ejemplo, el Slime)
            if (movement.length == 2) {
                switch (entity.direction) {
                    case DOWN, UP, LEFT, RIGHT -> i = movementNum == 1 || entity.flags.colliding ? 0 : 1;
                }
            } else { // Si la entidad tiene dos frames diferentes para todas las direcciones (por ejemplo, el Bat)
                switch (entity.direction) {
                    case DOWN -> i = movementNum == 1 || entity.flags.colliding ? 0 : 1;
                    case UP -> i = movementNum == 1 || entity.flags.colliding ? 2 : 3;
                    case LEFT -> i = movementNum == 1 || entity.flags.colliding ? 4 : 5;
                    case RIGHT -> i = movementNum == 1 || entity.flags.colliding ? 6 : 7;
                }
            }
        } else { // Si la entidad esta atacando (por ahora solo seria para el boss)
            switch (entity.direction) {
                case DOWN -> i = attackNum == 1 ? 0 : 1;
                case UP -> {
                    // The width of the image is subtracted in case the frame is larger than the tile
                    entity.tempScreenY -= frame.height;
                    i = attackNum == 1 ? 2 : 3;
                }
                case LEFT -> {
                    entity.tempScreenX -= frame.width;
                    i = attackNum == 1 ? 4 : 5;
                }
                case RIGHT -> i = attackNum == 1 ? 6 : 7;
            }
        }

        // Si la entidad no esta atacando, entonces devuelve el frame de movimiento en el indice calculado
        return !entity.flags.hitting ? movement[i] : attack[i];
    }

    public void loadStaticFrame(String texturePath, int width, int height) {
        texture = new Texture(texturePath);
        // Crea una region que abarca toda la textura
        frame = new SpriteRegion(
                0.0f, 0.0f, 1.0f, 1.0f,  // UV completa (toda la textura)
                width, height            // Tamaño de renderizado
        );
    }

    /**
     * Obtiene la textura del sprite sheet.
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Limpia recursos.
     * <p>
     * TODO Hace falta realmente este metodo?
     */
    public void cleanup() {
        if (texture != null) texture.cleanup();
    }

    /** Para sprite sheets, guarda las coordenadas UV en lugar de crear texturas separadas. */
    public static class SpriteRegion {

        public float u0, v0, u1, v1; // Coordenadas UV normalizadas
        public int width, height;

        public SpriteRegion(float u0, float v0, float u1, float v1, int width, int height) {
            this.u0 = u0;
            this.v0 = v0;
            this.u1 = u1;
            this.v1 = v1;
            this.width = width;
            this.height = height;
        }

        /**
         * Crea una region desde coordenadas de pixeles. En otras palabras, corta la subimagen del sprite sheet.
         * <p>
         * Seria como el metodo crop().
         */
        public static SpriteRegion fromPixels(int x, int y, int width, int height, int textureWidth, int textureHeight) {
            float u0 = (float) x / textureWidth;
            float v0 = (float) y / textureHeight;
            float u1 = (float) (x + width) / textureWidth;
            float v1 = (float) (y + height) / textureHeight;
            return new SpriteRegion(u0, v0, u1, v1, width, height);
        }

    }

}
