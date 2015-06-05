#version 330 core

layout (location = 0) out vec4 color;


in DATA
{
	vec2 tc;
} fs_in;

uniform sampler2D tex;
uniform sampler2D tex2;

float sum(float a, float b) {
	float res=a+b;
	if (res>=1.0) return 1.0;
	return res;
}

void main()
{
	vec4 color1 = texture(tex, fs_in.tc);
	vec4 color2 = texture(tex2, fs_in.tc);
	color=vec4(

		sum(color1.r, color2.r),
		sum(color1.g, color2.g),
		sum(color1.b, color2.b),
		1.0



	);
}