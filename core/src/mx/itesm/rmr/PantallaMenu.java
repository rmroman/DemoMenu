package mx.itesm.rmr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * El menú principal del juego.
 */

class PantallaMenu extends Pantalla
{
    private Juego juego;

    // Contenedor de los botones
    private Stage escenaMenu;

    // Texturas de los botones
    private Texture texturaBtnJugar;
    private Texture texturaBtnAyuda;

    public PantallaMenu(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        cargarTexturas();   // Carga imágenes
        crearEscenaMenu();  // Crea la escena
        Gdx.input.setInputProcessor(escenaMenu);
    }

    private void cargarTexturas() {
        texturaBtnJugar = new Texture("jugar.png");
        texturaBtnAyuda = new Texture("ayuda.png");
    }

    private void crearEscenaMenu() {
        escenaMenu = new Stage(vista);  // Para escalar su contenido
        // Botón jugar
        TextureRegionDrawable trdJugar = new TextureRegionDrawable(new TextureRegion(texturaBtnJugar));
        ImageButton btnJugar = new ImageButton(trdJugar);
        btnJugar.setPosition(ANCHO/2 - btnJugar.getWidth()/2, 0.6f*ALTO);
        // Agregar el listener para atender el touch
        btnJugar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                // Iniciar la otra pantalla
                juego.setScreen(new PantallaJuegoSpaceInvaders(juego));
            }
        });
        // Agregar el botón a la escena
        escenaMenu.addActor(btnJugar);

        // Botón ayuda
        TextureRegionDrawable trdAyuda = new TextureRegionDrawable(new TextureRegion(texturaBtnAyuda));
        ImageButton btnAyuda = new ImageButton(trdAyuda);
        btnAyuda.setPosition(ANCHO/2 - btnAyuda.getWidth()/2, 0.3f*ALTO);
        btnAyuda.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                juego.setScreen(new PantallaMario(juego));
            }
        });
        // Agregar el botón a la escena
        escenaMenu.addActor(btnAyuda);
    }

    @Override
    public void render(float delta) {
        borrarPantalla(0.8f,0.45f,0.2f);
        batch.setProjectionMatrix(camara.combined);

        escenaMenu.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        texturaBtnJugar.dispose();
        texturaBtnAyuda.dispose();
        escenaMenu.dispose();
    }
}
