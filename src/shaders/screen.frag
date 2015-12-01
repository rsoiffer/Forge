uniform sampler2D texture1;

void main(){
    float a = texture2D(texture1, gl_TexCoord[0].st).r;
    if (a > .8)
        gl_FragColor = vec4(gl_Color.rgb,1);
    else
        gl_FragColor = vec4(0,0,0,0);
/*
    if (a > .95)
        gl_FragColor = vec4(0,0,1,1);
    else if (a > .9)
        gl_FragColor = vec4(1,1,1,1);
    else if (a > .8)
        gl_FragColor = vec4(1,1,0,1);
    else if (a > .6)
        gl_FragColor = vec4(1,.5,0,1);
    else if (a > .4)
        gl_FragColor = vec4(1,0,0,1);
    else
        gl_FragColor = vec4(0,0,0,0);
*/
}