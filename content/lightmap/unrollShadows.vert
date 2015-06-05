#version 330 core

layout ( location = 0 ) in vec4 position;
layout ( location = 1 ) in vec2 tc;

uniform mat4 pr_matrix;
uniform mat4 vw_matrix;
uniform mat4 ml_matrix;
uniform float color_r;
uniform float color_g;
uniform float color_b;

out DATA
{
	vec2 tc;
	vec3 color;
} vs_out;

void main()
{
	gl_Position = pr_matrix * vw_matrix * ml_matrix * position;
	vs_out.tc = tc;
	vs_out.color = vec3(color_r, color_g, color_b);
}