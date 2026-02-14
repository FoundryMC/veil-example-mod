layout(location = 0) in vec3 Position;
layout(location = 1) in vec2 UV;

uniform mat3 NormalMat;
uniform vec3 Scale;

#ifndef USE_TESSELLATION
uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

uniform sampler2D Sampler0;
#endif

out vec2 texCoord;
out vec3 normal;

void main() {
    #ifdef USE_TESSELLATION
    gl_Position = vec4(Scale * Position, 1.0);
    #else
    float height = texture(Sampler0, UV).y;
    gl_Position = ProjMat * ModelViewMat * vec4(Scale * (Position + vec3(0.0, height, 0.0)), 1.0);
    #endif
    texCoord = UV;
    normal = NormalMat * vec3(0.0, 1.0, 0.0);
}