package com.sonchan.photoretouching.gpu

import android.opengl.GLES20
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter

class LightBalanceFilter(private val intensity: Float) : GPUImageFilter(
    NO_FILTER_VERTEX_SHADER, """
    precision mediump float;
    varying vec2 textureCoordinate;
    uniform sampler2D inputImageTexture;
    uniform float intensity;

    void main() {
        vec4 color = texture2D(inputImageTexture, textureCoordinate);
        float brightness = (color.r + color.g + color.b) / 3.0;

        // 섀도우 강화 (intensity < 0)
        if (intensity < 0.0) {
            float factor = 1.0 + intensity; // 0.0 ~ 1.0
            if (brightness < 0.5) {
                color.rgb *= factor;
            }
        }

        // 하이라이트 강화 (intensity > 0)
        else if (intensity > 0.0) {
            float factor = 1.0 + intensity;
            if (brightness > 0.5) {
                color.rgb *= factor;
            }
        }

        gl_FragColor = vec4(clamp(color.rgb, 0.0, 1.0), color.a);
    }
"""
) {
    private var intensityLocation: Int = 0

    override fun onInit() {
        super.onInit()
        intensityLocation = GLES20.glGetUniformLocation(program, "intensity")
    }

    override fun onInitialized() {
        super.onInitialized()
        setFloat(intensityLocation, intensity)
    }
}
