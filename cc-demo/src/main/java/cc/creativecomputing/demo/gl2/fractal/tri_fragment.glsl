

uniform float time;
uniform vec2 resolution;

float rand(vec2 co)
{
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

float GetLocation(vec2 s, float d)
{
    vec2 f = s*d;

    //s = mix(vec2(0), floor(s*d),step(0.5, f));

    // tris
    f = mod(f, 8.); // because i failed somewhere
    
    f = f + vec2(0,0.5)*floor(f).x;
    s = fract(f);
    f = floor(f);

    d = s.y - 0.5;
    float l = abs(d) + 0.5 * s.x;
    float ff = f.x+f.y;
    f = mix(f, f+sign(d)*vec2(0,0.5), step(0.5, l));
    l = mix(ff, ff+sign(d)*0.5, step(0.5, l));

    return l * rand(vec2(f));
}

vec3 hsv2rgb(float h, float s, float v)
{
    h = fract(h);
    vec3 c = smoothstep(2./6., 1./6., abs(h - vec3(0.5, 2./6., 4./6.)));
    c.r = 1.-c.r;
    /*
    vec3 c = vec3(
    smoothstep(1./6., 2./6., abs(h -0.5)),
        1.-smoothstep(1./6., 2./6., abs(h -2./6.)),
        1.-smoothstep(1./6., 2./6., abs(h -4./6.))
        );*/
    return mix(vec3(s), vec3(1.0), c) * v;
}

vec3 getRandomColor(float f, float t)
{
    return hsv2rgb(f+t, 0.2+cos(sin(f))*0.3, 0.9);
}

void main(){
	float mx = max( resolution.x, resolution.y );
    float t = time * 0.3;
    vec2 s = gl_TexCoord[0].xy / mx + vec2(t, 0) * 0.2;


    float f[3];
    f[0] = GetLocation(s, 12.);
    f[1] = GetLocation(s, 6.);
    f[2] = GetLocation(s, 3.);

    vec3 color = getRandomColor(f[1] *0.05 + 0.01*f[0] + 0.9*f[2], t);

    
	gl_FragData[0] =  vec4(color, 1.);
	gl_FragData[1] = gl_TexCoord[1];
	gl_FragData[2] = gl_TexCoord[2];
	gl_FragData[3] = gl_TexCoord[3];
}
