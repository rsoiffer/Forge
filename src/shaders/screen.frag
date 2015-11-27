uniform sampler2D texture1;

void main(){
    if (texture2D(texture1, gl_TexCoord[0].st).a > .8)
        gl_FragColor = vec4(gl_Color.rgb, 1);
    else
        gl_FragColor = vec4(0,0,0,0);
}