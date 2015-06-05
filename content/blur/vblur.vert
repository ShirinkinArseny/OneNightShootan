#version 330 core

layout ( location = 0 ) in vec4 position;
layout ( location = 1 ) in vec2 tc;

uniform mat4 pr_matrix;
uniform mat4 vw_matrix;
uniform mat4 ml_matrix;

out DATA
{
	vec2 tc;
	vec2 v_blurTexCoords[14];
} vs_out;

void main()
{
	gl_Position = pr_matrix * vw_matrix * ml_matrix * position;

   /* vs_out.v_blurTexCoords[ 0] = tc + vec2(0.0, -0.004);
    vs_out.v_blurTexCoords[ 1] = tc + vec2(0.0, -0.002);
    vs_out.v_blurTexCoords[ 2] = tc + vec2(0.0,  0.002);
    vs_out.v_blurTexCoords[ 3] = tc + vec2(0.0,  0.004);*/

    vs_out.v_blurTexCoords[ 0] = tc + vec2(0.0, -0.014);
    vs_out.v_blurTexCoords[ 1] = tc + vec2(0.0, -0.012);
    vs_out.v_blurTexCoords[ 2] = tc + vec2(0.0, -0.010);
    vs_out.v_blurTexCoords[ 3] = tc + vec2(0.0, -0.008);
    vs_out.v_blurTexCoords[ 4] = tc + vec2(0.0, -0.006);
    vs_out.v_blurTexCoords[ 5] = tc + vec2(0.0, -0.004);
    vs_out.v_blurTexCoords[ 6] = tc + vec2(0.0, -0.002);
    vs_out.v_blurTexCoords[ 7] = tc + vec2(0.0,  0.002);
    vs_out.v_blurTexCoords[ 8] = tc + vec2(0.0,  0.004);
    vs_out.v_blurTexCoords[ 9] = tc + vec2(0.0,  0.006);
    vs_out.v_blurTexCoords[10] = tc + vec2(0.0,  0.008);
    vs_out.v_blurTexCoords[11] = tc + vec2(0.0,  0.010);
    vs_out.v_blurTexCoords[12] = tc + vec2(0.0,  0.012);
    vs_out.v_blurTexCoords[13] = tc + vec2(0.0,  0.014);

	vs_out.tc = tc;
}