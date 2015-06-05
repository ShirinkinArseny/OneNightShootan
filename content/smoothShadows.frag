#version 330 core

layout (location = 0) out vec4 color;


in DATA
{
	vec2 tc;
} fs_in;

uniform sampler2D tex;

void main()
{
	vec2 motableTexCoord=fs_in.tc;
	motableTexCoord.y=0.0f;

	color = texture(tex, motableTexCoord);
	while (color.w<0.9 && motableTexCoord.y<fs_in.tc.y) {
		color = texture(tex, motableTexCoord);
		motableTexCoord.y+=0.01f;
	}
}