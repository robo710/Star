package com.sonchan.photoretouching.gpu

import android.opengl.GLES20
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter

class BrightnessFilter(private val brightness: Float) : GPUImageFilter(
    NO_FILTER_VERTEX_SHADER, """
    precision mediump float;
    varying vec2 textureCoordinate;
    uniform sampler2D inputImageTexture;
    uniform float brightness;

    void main() {
        vec4 color = texture2D(inputImageTexture, textureCoordinate);
        color.rgb += brightness;
        gl_FragColor = vec4(clamp(color.rgb, 0.0, 1.0), color.a);
    }
""") {
    private var brightnessLocation: Int = 0

    override fun onInit() {
        super.onInit()
        brightnessLocation = GLES20.glGetUniformLocation(program, "brightness")
    }

    override fun onInitialized() {
        super.onInitialized()
        setFloat(brightnessLocation, brightness)
    }
}
