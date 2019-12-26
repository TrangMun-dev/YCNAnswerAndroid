package ml.huytools.ycnanswer.Core.Game.Graphics.Drawing;

import android.graphics.Paint;
import android.graphics.Shader;

import ml.huytools.ycnanswer.Core.Game.Graphics.Color;
import ml.huytools.ycnanswer.Core.Game.Graphics.Texture;
import ml.huytools.ycnanswer.Core.Game.Scene;

public abstract class Drawable extends Scene.Node {
    public enum Style { STROKE, FILL };

    private Color color;
    private Texture texture;
    private int strokeWidth;
    private Style style;
    protected Paint paint;

    public Drawable(){
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        paint.setARGB(color.a, color.r, color.g, color.b);
        this.color = color;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        paint.setShader(texture.getBitmapShader());
        this.texture = texture;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        paint.setStrokeWidth(strokeWidth);
        this.strokeWidth = strokeWidth;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style type) {
        paint.setStyle(type == Style.STROKE ? Paint.Style.STROKE : Paint.Style.FILL);
        this.style = type;
    }

}