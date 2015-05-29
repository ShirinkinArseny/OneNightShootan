#version 330 core

layout (location = 0) out vec4 color;

uniform float darkness;

in DATA
{
	vec2 tc;
	vec4 screenCoordinates;
} fs_in;


uniform sampler2D tex;

void main()
{
	color = texture(tex, fs_in.tc);

	float writeableDarkness=darkness;

	float angle=abs(atan(fs_in.screenCoordinates.x, fs_in.screenCoordinates.y));
	if (angle>0.8) {
		if (angle>1.2) {
			writeableDarkness=0.0;
		} else {
			writeableDarkness=darkness*(1.0-(angle-0.8)*2.5);
		}
	}

	//if (color.w < 1.0)
	//	discard;
	color.r=color.r*writeableDarkness;
	color.g=color.g*writeableDarkness;
	color.b=color.b*writeableDarkness;
}