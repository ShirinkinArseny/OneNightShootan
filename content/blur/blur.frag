#version 330 core

layout (location = 0) out vec4 color;


in DATA
{
	vec2 tc;
	vec2 v_blurTexCoords[6];
} fs_in;

uniform sampler2D tex;

void main()
{
/*	color = texture(tex, fs_in.tc);
	if (color.w < 1.0)
		discard;*/

	color = vec4(0.0);
    /*color += texture(tex, fs_in.v_blurTexCoords[ 0])*0.11;
    color += texture(tex, fs_in.v_blurTexCoords[ 1])*0.22;
    color += texture(tex, fs_in.tc                 )*0.33;
    color += texture(tex, fs_in.v_blurTexCoords[ 2])*0.22;
    color += texture(tex, fs_in.v_blurTexCoords[ 3])*0.11;*/

    color += texture(tex, fs_in.v_blurTexCoords[ 0])*0.05;
    color += texture(tex, fs_in.v_blurTexCoords[ 1])*0.1;
    color += texture(tex, fs_in.v_blurTexCoords[ 2])*0.15;
    color += texture(tex, fs_in.tc                 )*0.2;
    color += texture(tex, fs_in.v_blurTexCoords[ 3])*0.15;
    color += texture(tex, fs_in.v_blurTexCoords[ 4])*0.1;
    color += texture(tex, fs_in.v_blurTexCoords[ 5])*0.05;

    color/=0.8;

}