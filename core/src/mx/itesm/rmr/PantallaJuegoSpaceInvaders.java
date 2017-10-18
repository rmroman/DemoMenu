package mx.itesm.rmr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by roberto on 01/09/17.
 */

class PantallaJuegoSpaceInvaders extends Pantalla
{
    private static final float DY_BALA = 5;
    private Juego juego;

    // Los Aliens
    private Array<Alien> arrAliens;
    private float DX = 28;
    private int pasos = 10;  // +20 a la derecha, -20 a la izquierda, +20 derecha, ...
    private float tiempoPasoAlien = 0;
    private final float TIEMPO_PASO = 0.8f;

    // La nave que controla el usuario
    private Nave nave;
    private float vxNave = 0;
    private final float VX_NAVE = 5;

    // Proyectiles
    private Bala bala;  // Única en el juego

    // Puntos
    private Texto texto;
    private int puntos = 0;
    private String cadenaPuntos = "Puntos " + puntos;

    // Efectos de sonido
    private Sound disparo;
    private Sound choque;
    private Music musicaFondo;

    // Botón atrás
    private Texture texturaBtnAtras;

    // Botón pausa
    private Texture texturaBtnPausa;

    // PAUSA
    private EscenaPausa escenaPausa;      // Muestra la pausa como pop-up

    // Estados. ahora el juego puede estar JUGANDO, PAUSADO, etc
    private EstadoJuego estado;


    public PantallaJuegoSpaceInvaders(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        // Crear aliens
        arrAliens = new Array<Alien>(12*5);
        for (int i=0; i<5; i++) {
            for (int j=0; j<12; j++) {
                Alien alien = new Alien(280 + j*60, 350 + i*60);
                arrAliens.add(alien);
            }
        }
        // Crear nave
        nave = new Nave(ANCHO/2, ALTO*0.07f);

        // Crear objeto para imprimir texto
        texto = new Texto();

        // Efectos
        disparo = Gdx.audio.newSound(Gdx.files.internal("audio/disparo.wav"));
        choque = Gdx.audio.newSound(Gdx.files.internal("audio/choque.wav"));

        // Botón atrás
        texturaBtnAtras = new Texture("backBtn.png");

        // Botón pausa
        texturaBtnPausa = new Texture("btnPausa.png");

        estado = EstadoJuego.JUGANDO;

        Gdx.input.setInputProcessor(new ProcesadorEntrada());   // El control es de Screen
    }

    @Override
    public void render(float delta) {
        // Actualizar
        moverAliens();
        moverNave();

        // Colisiones
        if (bala!=null && bala.isActiva()) {
            moverBala();
            verificarColisiones();
        }
        // Dibujar
        borrarPantalla(0,0,0);
        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        for (Alien alien :
                arrAliens) {
            alien.render(batch);
        }
        nave.render(batch);
        // Bala
        if (bala!=null) {
            bala.render(batch);
        }
        // Puntos
        texto.mostrarTexto(batch, cadenaPuntos, 150, ALTO*0.9f);

        // Botón atrás
        batch.draw(texturaBtnAtras,ANCHO-texturaBtnAtras.getWidth(),ALTO-texturaBtnAtras.getHeight());
        // Botón pausa
        batch.draw(texturaBtnPausa, ANCHO*0.75f,ALTO*0.8f);

        batch.end();

        // Botón PAUSA
        if (estado==EstadoJuego.PAUSADO) {
            escenaPausa.draw(); // Solo si está pausado muestra la imagen
        }
    }

    private void verificarColisiones() {
        for (int i=arrAliens.size-1; i>=0; i--) {
            Alien alien = arrAliens.get(i);
            if (alien.getEstado() == Alien.EstadoAlien.MUERTO) {
                arrAliens.removeIndex(i);
                break;
            }
            if (alien.getEstado() == Alien.EstadoAlien.EXPLOTA) {
                continue;
            }
            if (bala.estaColisionando(alien)) {
                // Lo mató!!!
                bala.setActiva(false);
                bala.set(-50, Pantalla.ALTO*2);
                alien.explotar();
                choque.play();
                puntos++;
                cadenaPuntos = "Puntos " + puntos;
                break;
            }
        }
    }

    private void moverBala() {
        bala.mover(0, DY_BALA);
    }

    private void moverNave() {
        nave.mover(vxNave);
    }

    private void moverAliens() {
        float dy = 0;
        tiempoPasoAlien += Gdx.graphics.getDeltaTime();
        if (tiempoPasoAlien>=TIEMPO_PASO) {
            tiempoPasoAlien = 0;
            if (pasos >= 20) {
                DX = -DX;
                pasos = 1;
                dy = -32;
            }
            float dx = DX;
            if (pasos==1) {
                dx = 0;
            }
            for (Alien alien :
                    arrAliens) {
                alien.mover(dx, dy);
            }
            pasos++;
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        texturaBtnAtras.dispose();
        texturaBtnPausa.dispose();
        escenaPausa.dispose();
    }

    private class ProcesadorEntrada implements InputProcessor
    {

        @Override
        public boolean keyDown(int keycode) {
            if (keycode== Input.Keys.RIGHT) {
                vxNave = VX_NAVE;
            }
            if (keycode==Input.Keys.LEFT) {
                vxNave = -VX_NAVE;
            }
            return true;
        }

        @Override
        public boolean keyUp(int keycode) {
            vxNave = 0;
            return true;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            // Botón atrás???
            Vector3 v = new Vector3(screenX, screenY, 0);
            camara.unproject(v);
            if (v.x>=ANCHO-texturaBtnAtras.getWidth() && v.y>=ALTO-texturaBtnAtras.getHeight()) {
                // Regresar!
                juego.setScreen(new PantallaMenu(juego));
            } else if (v.x>=ANCHO*0.75f && v.x<=ANCHO*0.75f+texturaBtnPausa.getWidth()
                && v.y>=ALTO*0.75f && v.y<=ALTO*0.75f+texturaBtnPausa.getHeight()) {
                // Botón pausa!!
                if (escenaPausa == null) {
                    escenaPausa = new EscenaPausa(vista, batch);
                }
                // PASA EL CONTROL A LA ESCENA
                estado = EstadoJuego.PAUSADO;
                Gdx.input.setInputProcessor(escenaPausa);   // Ya ni detecta touch fuera de la escena
            } else {
                // Disparar
                if (bala == null) {
                    bala = new Bala(nave.getX() + nave.getWidth() / 2, nave.getY());
                }
                if (!bala.isActiva()) {
                    bala.set(nave.getX() + nave.getWidth() / 2, nave.getY());
                    bala.setActiva(true);
                    disparo.play();
                }
            }
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }

    enum EstadoJuego {
        JUGANDO,
        PAUSADO
    }

    private class EscenaPausa extends Stage
    {
        // La escena que se muestra cuando está pausado
        public EscenaPausa(Viewport vista, SpriteBatch batch) {
            super(vista,batch);
            // Crear rectángulo transparente
            Pixmap pixmap = new Pixmap((int)(ANCHO*0.7f), (int)(ALTO*0.8f), Pixmap.Format.RGBA8888 );
            pixmap.setColor( 1f, 1f, 1f, 0.65f );
            pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
            Texture texturaRectangulo = new Texture( pixmap );
            pixmap.dispose();
            Image imgRectangulo = new Image(texturaRectangulo);
            imgRectangulo.setPosition(0.15f*ANCHO, 0.1f*ALTO);
            this.addActor(imgRectangulo);

            // Salir
            Texture texturaBtnSalir = new Texture("comun/btnSalir.png");
            TextureRegionDrawable trdSalir = new TextureRegionDrawable(
                    new TextureRegion(texturaBtnSalir));
            ImageButton btnSalir = new ImageButton(trdSalir);
            btnSalir.setPosition(ANCHO/2-btnSalir.getWidth()/2, ALTO/2);
            btnSalir.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Regresa al menú
                    juego.setScreen(new PantallaMenu(juego));
                }
            });
            this.addActor(btnSalir);

            // Continuar
            Texture texturaBtnContinuar = new Texture("comun/btnContinuar.png");
            TextureRegionDrawable trdContinuar = new TextureRegionDrawable(
                    new TextureRegion(texturaBtnContinuar));
            ImageButton btnContinuar = new ImageButton(trdContinuar);
            btnContinuar.setPosition(ANCHO/2-btnContinuar.getWidth()/2, ALTO/4);
            btnContinuar.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Regresa al juego
                    estado = EstadoJuego.JUGANDO;
                    Gdx.input.setInputProcessor(new ProcesadorEntrada()); // No debería crear uno nuevo
                }
            });
            this.addActor(btnContinuar);
        }
    }
}
