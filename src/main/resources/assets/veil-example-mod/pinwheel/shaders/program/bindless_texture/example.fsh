in vec2 texCoord0;

layout(std140) uniform CustomTextures {
    sampler2D textures[128];
};

uniform uint TextureIndex;

out vec4 Color;

void main() {
    Color = texture(textures[TextureIndex], texCoord0);
}