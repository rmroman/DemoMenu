package mx.itesm.rmr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * Created by roberto on 05/09/17.
 */

public class Texto
{
    private BitmapFont font;
    GlyphLayout glyph = new GlyphLayout();

    public Texto() {
        font = new BitmapFont(Gdx.files.internal("fontCuadro.fnt"));
    }

    public void mostrarTexto(SpriteBatch batch, String mensaje, float x, float y) {
        //GlyphLayout glyph = new GlyphLayout();
        glyph.setText(font, mensaje);
        float anchoTexto = glyph.width;
        font.draw(batch, glyph, x-anchoTexto/2, y);
    }
}
