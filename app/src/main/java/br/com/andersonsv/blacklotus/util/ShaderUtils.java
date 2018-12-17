package br.com.andersonsv.blacklotus.util;

import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

import static com.google.common.primitives.Floats.min;

public class ShaderUtils {

    public static PaintDrawable radialGradientBackground(int [] colors, float positionX, float positionY,
                                                   float size)  {
        PaintDrawable radialGradientBackground = new PaintDrawable();
        radialGradientBackground.setShape(new RectShape());
        radialGradientBackground.setShaderFactory(new RadialShaderFactory(colors, positionX, positionY, size));
        return radialGradientBackground;
    }


    public static class RadialShaderFactory extends ShapeDrawable.ShaderFactory{
        int[] colors;
        private Float positionX = 0.5F;
        private Float positionY = 0.5F;
        private Float size = 1.0F;

        public RadialShaderFactory(int[] colors, Float positionX, Float positionY, Float size){
            this.colors = colors;
            this.positionX = positionX;
            this.positionY = positionY;
            this.size = size;
        }
        @Override
        public Shader resize(int width, int height) {
            return new RadialGradient(
                    width * positionX,
                    height * positionY,
                    min(width, height) * size,
                    colors,
                    null,
                    Shader.TileMode.CLAMP);
        }
    }
}



/*


object ShaderUtils {
    private class RadialShaderFactory(private val colors: IntArray, val positionX: Float,
                                      val positionY: Float, val size: Float): ShapeDrawable.ShaderFactory() {

        override fun resize(width: Int, height: Int): Shader {
            return RadialGradient(
                    width * positionX,
                    height * positionY,
                    minOf(width, height) * size,
                    colors,
                    null,
                    Shader.TileMode.CLAMP)
        }
    }

    fun radialGradientBackground(vararg colors: Int, positionX: Float = 0.5f, positionY: Float = 0.5f,
                                 size: Float = 1.0f): PaintDrawable {
        val radialGradientBackground = PaintDrawable()
        radialGradientBackground.shape = RectShape()
        radialGradientBackground.shaderFactory = RadialShaderFactory(colors, positionX, positionY, size)
        return radialGradientBackground
    }
}
 */



