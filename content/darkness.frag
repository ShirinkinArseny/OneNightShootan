#version 330 core

layout (location = 0) out vec4 color;


in DATA
{
	vec2 tc;
} fs_in;

uniform sampler2D tex;

void main()
{
	color = texture(tex, fs_in.tc);
	if (color.w < 1.0) {
		color.w=1.0;
		color.r=0.0;
		color.g=0.0;
		color.b=0.0;
	} else {
		color.w=1.0;
		color.r=1.0;
		color.g=1.0;
		color.b=1.0;
	}
}