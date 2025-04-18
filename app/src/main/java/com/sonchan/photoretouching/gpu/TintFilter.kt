package com.sonchan.photoretouching.gpu

import android.opengl.GLES20
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter

class TintFilter(private val intensity: Float) :
    GPUImageFilter(NO_FILTER_VERTEX_SHADER, """
    precision mediump float;
    varying vec2 textureCoordinate;
    uniform sampler2D inputImageTexture;
    uniform float intensity;

    void main() {
        vec4 color = texture2D(inputImageTexture, textureCoordinate);

        // 보라색 tint (약간 핑크 섞인 바이올렛 계열)
        vec3 purpleTint = vec3(0.9, 0.7, 1.0);

        // 초록색 tint
        vec3 greenTint = vec3(0.7, 1.0, 0.7);

        vec3 resultColor;
        if (intensity > 0.0) {
            resultColor = mix(color.rgb, purpleTint, intensity);
        } else {
            resultColor = mix(color.rgb, greenTint, -intensity);
        }

        gl_FragColor = vec4(resultColor, color.a);
    }
""") {
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