uniform sampler2D Sampler0;
uniform vec4 ColorModulator;

uniform mat3 NormalMat;

in vec2 texCoord;
// #veil:normal
in vec3 normal;

out vec4 OutColor;

void main() {
    vec4 color = texture(Sampler0, texCoord);
    if (color.a < 0.01) {
        discard;
    }

    color.rgb *= max(dot(normal, NormalMat * vec3(0.0, 1.0, 0.0)), 0.2);
    OutColor = color * ColorModulator;
}