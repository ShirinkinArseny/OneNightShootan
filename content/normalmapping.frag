#version 330 core

layout (location = 0) out vec4 color;


in DATA
{
	vec2 tc;
	vec2 worldCoordinates;
} fs_in;

uniform sampler2D tex;
uniform sampler2D tex2;

uniform int lightSourceNumbers;
uniform vec3[16] lightColors;
uniform vec3[16] lightPositions;

void main()
{
	color = texture(tex, fs_in.tc);
	color.rgb*=color.a;

    vec3 light=vec3(0);

    float dx;
    float dy;
    float dz;
	float length;

    for (int i=0; i<lightSourceNumbers; i++) {
        dx=fs_in.worldCoordinates.x-lightPositions[i].x;
        dy=fs_in.worldCoordinates.y-lightPositions[i].y;
        dz=lightPositions[i].z;
	    float length=dx*dx + dy*dy;
	    if (length<100) {
            light+=(1.5 - dot(vec3(dx, dy, dz), texture(tex2, fs_in.tc).rgb-0.5))*lightColors[i]*(100-length)*0.01;
	    }
    }
    color.rgb*=light.rgb*0.2;

}