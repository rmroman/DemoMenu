package mx.itesm.rmr;

import com.badlogic.gdx.Game;

public class Juego extends Game
{
    @Override
    public void create() {
        setScreen(new PantallaCargando(this));  // Splash Screen
    }
}
