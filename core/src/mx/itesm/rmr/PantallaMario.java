package mx.itesm.rmr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by roberto on 03/10/17.
 */

class PantallaMario extends Pantalla
{
    private static final float ANCHO_MAPA = 6784;
    private final Juego juego;

    // Mapas
    private TiledMap mapa;      // El mapa
    private OrthogonalTiledMapRenderer renderer;    // Dibuja el mapa
    private Personaje mario;    // Mario, lo controla el usuario

    // HUD, otra cámara con la imagen fija
    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;
    // HUD con una escena para los botones y componentes
    private Stage escenaHUD;    // Tendrá un Pad virtual para mover al personaje y el botón de Pausa

    public PantallaMario(Juego juego) {
        this.juego = juego;
    }


    @Override
    public void show() {

        cargarMapa();
        mario = new Personaje(new Texture("mapa/marioSprite.png"));
        crearHUD();

        // El input lo maneja la escena
        Gdx.input.setInputProcessor(escenaHUD);
    }

    private void crearHUD() {
        // Crea la cámara y la vista
        camaraHUD = new OrthographicCamera(ANCHO, ALTO);
        camaraHUD.position.set(ANCHO/2, ALTO/2, 0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(ANCHO, ALTO, camaraHUD);
        // Crea el pad
        Skin skin = new Skin(); // Texturas para el pad
        skin.add("fondo", new Texture("mapa/padBack.png"));
        skin.add("boton", new Texture("mapa/padKnob.png"));
        // Configura la vista del pad
        Touchpad.TouchpadStyle estilo = new Touchpad.TouchpadStyle();
        estilo.background = skin.getDrawable("fondo");
        estilo.knob = skin.getDrawable("boton");
        // Crea el pad
        Touchpad pad = new Touchpad(64,estilo);     // Radio, estilo
        pad.setBounds(16,16,256,256);               // x,y - ancho,alto
        // Comportamiento del pad
        pad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad pad = (Touchpad)actor;
                if (pad.getKnobPercentX() > 0.20) { // Más de 20% de desplazamiento DERECHA
                    mario.setEstadoMover(Personaje.EstadoMovimento.DERECHA);
                } else if ( pad.getKnobPercentX() < -0.20 ) {   // Más de 20% IZQUIERDA
                    mario.setEstadoMover(Personaje.EstadoMovimento.IZQUIERDA);
                } else {
                    mario.setEstadoMover(Personaje.EstadoMovimento.QUIETO);
                }
            }
        });
        pad.setColor(1,1,1,0.7f);   // Transparente
        // Crea la escena y agrega el pad
        escenaHUD = new Stage(vistaHUD);    // Escalar con esta vista
        escenaHUD.addActor(pad);

    }

    private void cargarMapa() {
        AssetManager manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load("mapa/mapaMario.tmx",TiledMap.class);
        manager.finishLoading(); // Espera
        mapa = manager.get("mapa/mapaMario.tmx");
        renderer = new OrthogonalTiledMapRenderer(mapa);
    }

    @Override
    public void render(float delta) {
        // Actualiza todos los objetos
        mario.actualizar(mapa);
        actualizarCamara();
        // Cámara fondo

        borrarPantalla(0.35f,0.55f,1);
        batch.setProjectionMatrix(camara.combined);

        renderer.setView(camara);
        renderer.render();

        batch.begin();
        mario.render(batch);
        batch.end();

        // Cámara HUD
        batch.setProjectionMatrix(camaraHUD.combined);
        escenaHUD.draw();
    }

    private void actualizarCamara() {
        // Depende de la posición del personaje. Siempre sigue al personaje
        float posX = mario.getX();
        // Primera mitad de la pantalla
        if (posX < ANCHO/2 ) {
            camara.position.set(ANCHO/2, ALTO/2, 0);
        } else if (posX > ANCHO_MAPA - ANCHO/2) {   // Última mitad de la pantalla
            camara.position.set(ANCHO_MAPA-ANCHO/2,camara.position.y,0);
        } else {    // En 'medio' del mapa
            camara.position.set(posX,camara.position.y,0);
        }
        camara.update();
    }

    @Override
    public void resize(int width, int height) {
        vista.update(width, height);
        vistaHUD.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        mapa.dispose();
        escenaHUD.dispose();
    }
}
