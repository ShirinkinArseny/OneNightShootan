#version 330 core

layout ( location = 0 ) in vec4 position;
layout ( location = 1 ) in vec2 tc;

uniform mat4 pr_matrix;
uniform mat4 vw_matrix;

uniform mat4 rot_matrix;
uniform mat4 mov_matrix;

out DATA
{
	vec2 tc;
} vs_out;

void main()
{

	mat4 ml_matrix=mov_matrix*rot_matrix;

	gl_Position = pr_matrix * vw_matrix * ml_matrix * position;
	vs_out.tc = tc;
}