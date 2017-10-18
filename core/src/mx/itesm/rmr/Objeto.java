package mx.itesm.rmr;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Representa un elemento gráfico del juego
 */

public class Objeto
{
    protected Sprite sprite;    // Imagen

    public Objeto(Texture textura, float x, float y) {
        sprite = new Sprite(textura);
        sprite.setPosition(x, y);
    }

    public Objeto() {

    }

    public void dibujar(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
