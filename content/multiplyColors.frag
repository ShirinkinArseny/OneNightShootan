#version 330 core

layout (location = 0) out vec4 color;


in DATA
{
	vec2 tc;
} fs_in;

uniform sampler2D tex;
uniform sampler2D tex2;

void main()
{
	color = texture(tex, fs_in.tc)*
			texture(tex2, fs_in.tc);
}