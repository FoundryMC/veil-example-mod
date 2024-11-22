layout(location = 0) in vec3 Position;
layout(location = 1) in vec2 UV;

uniform vec3 Scale;

out vec2 texCoord;
out vec3 normal;

void main() {
    gl_Position = vec4(Scale * Position, 1.0);
    texCoord = UV;
    normal = vec3(0.0, 1.0, 0.0);
}