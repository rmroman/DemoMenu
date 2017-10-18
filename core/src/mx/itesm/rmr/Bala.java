package mx.itesm.rmr;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by roberto on 03/09/17.
 */

public class Bala
{
    private float x;
    private float y;
    private Texture texturaBala;

    private boolean activa = false;

    public Bala(float x, float y) {
        texturaBala = new Texture("bala.png");
        this.x = x;
        this.y = y;
        activa = false;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texturaBala, x, y);
    }

    public void mover(float dx, float dy) {
        x += dx;
        y += dy;
        if (y>Pantalla.ALTO) {
            activa = false;
        }
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }


    public boolean isActiva() {
        return activa;
    }

    public boolean estaColisionando(Alien alien) {
        if (x>=alien.getX() && x<=alien.getX()+alien.getTextura().getWidth()) {
            if (y>=alien.getY() && y<=alien.getY()+alien.getTextura().getHeight()) {
                return true;
            }
        }
        return false;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
