package com.sonchan.photoretouching.gpu

import android.opengl.GLES20
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter

class LightBalanceFilter(private val intensity: Float) : GPUImageFilter(
    NO_FILTER_VERTEX_SHADER, """
    precision mediump float;
varying vec2 textureCoordinate;
uniform sampler2D inputImageTexture;
uniform float intensity; // -1.0 ~ 1.0

void main() {
    vec4 color = texture2D(inputImageTexture, textureCoordinate);
    float brightness = dot(color.rgb, vec3(0.299, 0.587, 0.114));

    // 라이트 밸런스 처리 (섀도우/하이라이트 보정)
    if (intensity < 0.0) {
        float shadowBoost = smoothstep(0.0, 0.5, brightness);
        color.rgb = mix(color.rgb * (1.0 + intensity), color.rgb, shadowBoost);
    } else if (intensity > 0.0) {
        float highlightBoost = smoothstep(0.5, 1.0, brightness);
        color.rgb = mix(color.rgb, color.rgb * (1.0 + intensity), highlightBoost);
    }

    // ✨ 부드러운 느낌 추가 (명료도 낮추는 효과)
    vec3 soft = mix(color.rgb, vec3(brightness), 0.1 * abs(intensity)); // 0.1 정도 소프트닝
    color.rgb = mix(color.rgb, soft, 0.5);

    // ✨ 살짝 대비 줄임 (중간 톤 부드럽게)
    float contrast = 1.0 - (0.05 * abs(intensity));
    color.rgb = ((color.rgb - 0.5) * contrast + 0.5);

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
