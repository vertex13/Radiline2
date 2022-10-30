#version 300 es

#define PI2 6.2831853
#define N 3

precision mediump float;

out highp vec4 fColor;

uniform highp float uTime;
uniform highp vec2 uResolution;
uniform vec4 uBackground;

float[] _center = float[N] (0.5, 0.3, 0.7);
float[] _height = float[N] (0.5, 0.3, 0.4);
float[] _phase = float[N] (0.0, 0.1, 0.5);
float[] _frequency = float[N] (0.3, 0.1, 0.4);
float[] _velocity = float[N] (0.1, 0.03, 0.06);
float[] _power = float[N] (3.0, 4.0, 5.0);
vec4[] _color = vec4[N] (
    vec4(0.98, 0.35, 0.12, 0.5),
    vec4(0.69, 0.30, 0.80, 0.5),
    vec4(0.98, 0.84, 0.12, 0.5)
);

float intensity(
    float x, // [0, 1]
    float y, // [0, 1]
    float time, // [0, +inf)
    float center, // [0, 1]
    float height, // [0, 1]
    float phase, // [0, 1]
    float frequency, // [0, +inf)
    float velocity, // [0, +inf)
    float power // [0, +inf)
) {
    float y1 = center + (0.5 * height * sin(PI2 * (frequency * x + phase + velocity * time)));
    float diff = abs(y1 - y);
    return pow(1.0 - diff, power);
}

vec4 blend(vec4 c1, vec4 c2) {
    float sumA = c1.a + c2.a;
    if (sumA < 0.01) {
        return vec4(0.0);
    }
    float a = c2.a / sumA;
    vec3 mixed = mix(c1.rgb, c2.rgb, a);
    float alpha = c1.a + (1.0 - c1.a) * c2.a;
    return vec4(mixed, alpha);
}

void main() {
    vec2 uv = gl_FragCoord.xy / uResolution;

    vec4 result = uBackground;
    for (int i = 0; i < N; i++) {
        float intns = intensity(
            uv.x, uv.y, uTime,
            _center[i], _height[i], _phase[i],
            _frequency[i], _velocity[i], _power[i]
        );
        vec4 c = vec4(_color[i].rgb, _color[i].a * intns);
        result = blend(result, c);
    }

    fColor = result;
}
