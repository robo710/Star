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
        vec3 tintColor = vec3(1.0, 0.0, 1.0); // 보라색 Tint
        vec3 tinted = mix(color.rgb, tintColor, intensity);
        gl_FragColor = vec4(tinted, color.a);
    }
""".trimIndent()) {

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
