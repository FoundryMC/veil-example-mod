uniform sampler2D Sampler0;
uniform vec4 ColorModulator;

in vec2 fragTexCoord;

out vec4 OutColor;

void main() {
    OutColor = texture(Sampler0, fragTexCoord) * ColorModulator;
}