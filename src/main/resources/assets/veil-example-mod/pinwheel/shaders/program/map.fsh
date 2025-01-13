uniform sampler2D Sampler0;
uniform vec4 ColorModulator;

in vec2 texCoord;
in vec3 normal;

out vec4 OutColor;

void main() {
    vec4 color = texture(Sampler0, texCoord);
    if (color.a < 0.01) {
        discard;
    }

    // #veil:normal
    vec3 realNormal = normalize(normal.xzy);
    color.rgb *= max(dot(normal, vec3(0.0, 0.0, 1.0)), 0.2);
    OutColor = color * ColorModulator;
}