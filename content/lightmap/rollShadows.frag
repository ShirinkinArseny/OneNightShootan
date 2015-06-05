#version 330 core

layout (location = 0) out vec4 color;


in DATA
{
	vec2 tc;
} fs_in;

uniform sampler2D tex;

void main()
{

	/*float dx=fs_in.tc.x-0.5;
	float dy=fs_in.tc.y-0.5;

	float angle=atan(dy, dx);

	float length=sqrt(dx*dx+dy*dy);

	vec2 texCoord=vec2(angle/6.28, length);*/

	float angle=fs_in.tc.x*6.2831853;
	float length=fs_in.tc.y;

	float x=0.5+length*cos(angle)*0.5;
	float y=0.5+length*sin(angle)*0.5;

	vec2 texCoord=vec2(x, y);


	color = texture(tex, texCoord);
}