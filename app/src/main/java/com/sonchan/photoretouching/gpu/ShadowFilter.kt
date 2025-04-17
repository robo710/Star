package com.sonchan.photoretouching.gpu

import android.opengl.GLES20
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter

class ShadowFilter(private val shadowFactor: Float) :
    GPUImageFilter(NO_FILTER_VERTEX_SHADER, """
    precision mediump float;
    varying vec2 textureCoordinate;
    uniform sampler2D inputImageTexture;
    uniform float shadowFactor;

    void main() {
        vec4 color = texture2D(inputImageTexture, textureCoordinate);
        float brightness = (color.r + color.g + color.b) / 3.0;

        // 어두운 영역에 그림자 효과를 적용
        float factor = (brightness < 0.25) ? shadowFactor : 0.0;

        // 그림자 효과를 위한 색상 변경
        color.rgb += (1.0 - color.rgb) * factor;

        // 색상 값이 1을 넘지 않도록 보정
        gl_FragColor = vec4(min(color.rgb, 1.0), color.a);
    }
""") {

    private var shadowFactorLocation: Int = 0

    override fun onInit() {
        super.onInit()
        shadowFactorLocation = GLES20.glGetUniformLocation(program, "shadowFactor")
    }

    override fun onInitialized() {
        super.onInitialized()
        setFloat(shadowFactorLocation, shadowFactor)
    }
}
