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
	vec4 currentColor=texture(tex, motableTexCoord);

	while (currentColor.w<0.9 && motableTexCoord.y<1) {
		currentColor = texture(tex, motableTexCoord);
		motableTexCoord.y+=0.00097656;
	}

	vec4 outColor=vec4(1.0);
	outColor.r=motableTexCoord.y;

	gl_FragColor = outColor;

}