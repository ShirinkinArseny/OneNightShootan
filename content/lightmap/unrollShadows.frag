#version 330 core

layout (location = 0) out vec4 color;


in DATA
{
	vec2 tc;
	vec3 color;
} fs_in;

uniform sampler2D tex;

float getShadow(float angle, float dist) {
	float moAngle=angle/6.2831853;
	if (moAngle>1.0) {
		moAngle-=1.0;
	}
	if (moAngle<0) {
		moAngle+=1.0;
	}
	if (texture(tex, vec2(moAngle, 0)).r>dist) {
		return 1.0;
	} else return 0.0;
}

void main()
{

	float dx=fs_in.tc.x*2-1;
	float dy=fs_in.tc.y*2-1;

	float angle=atan(dy, dx);
	float length=sqrt(dx*dx+dy*dy);
	float dAngle=0.002f;

	float sum = 0.0;
 	sum += getShadow(angle - 4.0*dAngle, length) * 0.05;
    sum += getShadow(angle - 3.0*dAngle, length) * 0.09;
    sum += getShadow(angle - 2.0*dAngle, length) * 0.12;
    sum += getShadow(angle - 1.0*dAngle, length) * 0.15;

    sum += getShadow(angle, length) * 0.16;

    sum +=  getShadow(angle + 1.0*dAngle, length) * 0.15;
    sum +=  getShadow(angle + 2.0*dAngle, length) * 0.12;
    sum +=  getShadow(angle + 3.0*dAngle, length) * 0.09;
    sum +=  getShadow(angle + 4.0*dAngle, length) * 0.05;

	float reLength=(1.0-length);

	sum *= reLength*reLength*reLength;

	color=vec4(sum*fs_in.color.r, sum*fs_in.color.g, sum*fs_in.color.b, 1.0);
}