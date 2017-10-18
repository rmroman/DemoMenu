package mx.itesm.rmr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by roberto on 01/09/17.
 */

class Alien
{
    private Texture texturaAlienArriba;
    private Texture texturaAlienAbajo;
    private Texture texturaAlienExplota;

    private EstadoAlien estado;

    // timer explotando
    float timerExplota = 0;

    private float x, y;   // Posición

    public Alien(float x, float y) {
        texturaAlienArriba = new Texture("enemigoArriba.png");
        texturaAlienAbajo = new Texture("enemigoAbajo.png");
        texturaAlienExplota = new Texture("enemigoExplota.png");
        estado = EstadoAlien.ARRIBA;
        this.x = x;
        this.y = y;
    }

    public void render(SpriteBatch batch) {
        switch (estado) {
            case ARRIBA:
                batch.draw(texturaAlienArriba, x, y);
                break;
            case ABAJO:
                batch.draw(texturaAlienAbajo, x, y);
                break;
            case EXPLOTA:
                timerExplota += Gdx.graphics.getDeltaTime();
                if (timerExplota>=0.3f) {
                    estado = EstadoAlien.MUERTO;
                }
                batch.draw(texturaAlienExplota, x, y);
                break;
        }
    }

    public void mover(float dx, float dy) {
        x += dx;
        y += dy;
        if (estado==EstadoAlien.ARRIBA) {
            estado = EstadoAlien.ABAJO;
        } else if (estado==EstadoAlien.ABAJO){
            estado = EstadoAlien.ARRIBA;
        }
    }

    public void explotar() {
        estado = EstadoAlien.EXPLOTA;
        timerExplota = 0;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Texture getTextura() {
        return texturaAlienArriba;
    }

    public EstadoAlien getEstado() {
        return estado;
    }

    public void setEstado(EstadoAlien estado) {
        this.estado = estado;
    }

    public enum EstadoAlien {
        ARRIBA,
        ABAJO,
        EXPLOTA,
        MUERTO
    }
}
