package com.sonchan.photoretouching.gpu

import android.opengl.GLES20
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter

class ClarityFilter(private val intensity: Float) : GPUImageFilter(
    NO_FILTER_VERTEX_SHADER, """
    precision mediump float;
    varying vec2 textureCoordinate;
    uniform sampler2D inputImageTexture;
    uniform float intensity;

    void main() {
        vec2 offset = vec2(1.0 / 512.0); // 기본 해상도 기준 (조정 가능)

        vec4 center = texture2D(inputImageTexture, textureCoordinate);
        vec4 north = texture2D(inputImageTexture, textureCoordinate + vec2(0.0, offset.y));
        vec4 south = texture2D(inputImageTexture, textureCoordinate - vec2(0.0, offset.y));
        vec4 east = texture2D(inputImageTexture, textureCoordinate + vec2(offset.x, 0.0));
        vec4 west = texture2D(inputImageTexture, textureCoordinate - vec2(offset.x, 0.0));

        vec4 edge = (north + south + east + west) / 4.0;
        vec4 highPass = center - edge;

        vec4 result = center + highPass * intensity;

        gl_FragColor = vec4(clamp(result.rgb, 0.0, 1.0), center.a);
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
