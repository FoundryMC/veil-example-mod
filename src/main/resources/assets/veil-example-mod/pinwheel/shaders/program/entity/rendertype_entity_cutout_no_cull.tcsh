// specify number of control points per patch output
// this value controls the size of the input and output arrays
layout (vertices=4) out;

in float vertexDistance[];
in vec4 vertexColor[];
in vec4 lightMapColor[];
in vec4 overlayColor[];
in vec2 texCoord0[];

out float VertexDistance[];
out vec4 VertexColor[];
out vec4 LightMapColor[];
out vec4 OverlayColor[];
out vec2 TexCoord0[];

void main()
{
    // ----------------------------------------------------------------------
    // pass attributes through
    gl_out[gl_InvocationID].gl_Position = gl_in[gl_InvocationID].gl_Position;
    VertexDistance[gl_InvocationID] = vertexDistance[gl_InvocationID];
    VertexColor[gl_InvocationID] = vertexColor[gl_InvocationID];
    LightMapColor[gl_InvocationID] = lightMapColor[gl_InvocationID];
    OverlayColor[gl_InvocationID] = overlayColor[gl_InvocationID];
    TexCoord0[gl_InvocationID] = texCoord0[gl_InvocationID];

    // ----------------------------------------------------------------------
    // invocation zero controls tessellation levels for the entire patch
    if (gl_InvocationID == 0)
    {
        gl_TessLevelOuter[0] = 16;
        gl_TessLevelOuter[1] = 16;
        gl_TessLevelOuter[2] = 16;
        gl_TessLevelOuter[3] = 16;

        gl_TessLevelInner[0] = 16;
        gl_TessLevelInner[1] = 16;
    }
}