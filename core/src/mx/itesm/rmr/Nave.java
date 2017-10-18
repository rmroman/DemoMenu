package mx.itesm.rmr;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by roberto on 01/09/17.
 */

class Nave
{
    private Texture texturaNave;

    private float x, y;

    public Nave(float x, float y) {
        texturaNave = new Texture("nave.png");
        this.x = x;
        this.y = y;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texturaNave, x, y);
    }

    public void mover(float dx) {
        x += dx;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return texturaNave.getWidth();
    }
}
