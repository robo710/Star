package com.sonchan.photoretouching.gpu

import android.opengl.GLES20
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter

class ExposureFilter(private val exposure: Float) : GPUImageFilter(
    NO_FILTER_VERTEX_SHADER, """
    precision mediump float;
    varying vec2 textureCoordinate;
    uniform sampler2D inputImageTexture;
    uniform float exposure;

    void main() {
        vec4 color = texture2D(inputImageTexture, textureCoordinate);
        color.rgb = color.rgb * pow(2.0, exposure);
        gl_FragColor = vec4(clamp(color.rgb, 0.0, 1.0), color.a);
    }
""") {
    private var exposureLocation: Int = 0

    override fun onInit() {
        super.onInit()
        exposureLocation = GLES20.glGetUniformLocation(program, "exposure")
    }

    override fun onInitialized() {
        super.onInitialized()
        setFloat(exposureLocation, exposure)
    }
}
