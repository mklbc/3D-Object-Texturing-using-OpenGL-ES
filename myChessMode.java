package com.example.kulubecioglu_lab5;

import android.content.Context;
import android.opengl.GLES32;

// Satranç tahtası modunu tanımlayan sınıf.
// Basit bir sahne üzerine satranç görselliğini entegre ediyor.
public class myChessMode extends mySimplestMode {

    // Işık konumu ve rengi tanımlanıyor
    private float[] lightPos = {0.3f, 0, 1.0f};
    private float[] lightColor = {1, 1, 1};

    // Kurucu metot - Kamera açıları ve mesafesi ayarlanıyor
    myChessMode() {
        super();
        alphaViewAngle = 30;
        betaViewAngle = 60;
        zDistance = 5f;
    }

    // Sahneye eklenecek nesneler burada oluşturuluyor
    protected void myCreateObjects() {
        arrayVertex = new float[100]; // Vertex dizisi oluşturuluyor
        mpObj = new myGraphicPrimitives(); // Grafik nesne yardımcı sınıfı çağrılıyor
        int pos = 0;

        // Dörtgenin vertex ve texture koordinatları
        float[] tmpv = {
                // position   xt     yt
                -1, -1, 0,   0.01f,     3.99f,
                -1,  1, 0,   0.01f,     0.01f,
                1,  1, 0,   3.99f,     0.01f,
                1, -1, 0,   3.99f,     3.99f
        };

        // Texture uygulanmış dörtgen ekleniyor
        mpObj.addQuadXYZnt(
                arrayVertex, pos, tmpv, 0,
                textureHandle, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void clearColor() {
        // Arka plan rengi siyah olarak ayarlanıyor
        GLES32.glClearColor(0f, 0f, 0f, 1f);
    }

    // Texture işlemleri burada başlatılıyor
    @Override
    protected void myInitTextures(Context context) {
        // Satranç tahtası için texture yükleniyor
        textureHandle = myLoadTexture(context, R.drawable.img_15, GLES32.GL_REPEAT);
    }

    // Shader programı burada oluşturuluyor
    public void myCreateShaderProgram() {
        // Shader dosyaları derleniyor ve bağlanıyor
        myCompileAndAttachShaders(
                myShadersLibrary.vertexShaderCode9,
                myShadersLibrary.fragmentShaderCode13); // Fragment Shader 13 numaralı kod

        // Vertex array ile pozisyon, normal ve texture bilgileri bağlanıyor
        myVertexArrayBind3(arrayVertex, 8,
                "vPosition", 0,
                "vNormal", 3*4,
                "vTexture", 6*4);
    }

    // Sahne çizimi bu metotla yapılıyor
    protected void myDrawing() {
        GLES32.glBindVertexArray(VAO_id); // VAO aktif ediliyor

        // Işık ve obje renk bilgileri shader'a aktarılıyor
        int uHandle = GLES32.glGetUniformLocation(gl_Program, "vLightColor");
        GLES32.glUniform3fv(uHandle, 1, lightColor, 0);

        uHandle = GLES32.glGetUniformLocation(gl_Program, "vLightPos");
        GLES32.glUniform3fv(uHandle, 1, lightPos, 0);

        uHandle = GLES32.glGetUniformLocation(gl_Program, "vColor");

        // Nesne çizimi başlatılıyor
        mpObj.drawAllObjects(uHandle);

        // VAO bağlantısı kapatılıyor
        GLES32.glBindVertexArray(0);
    }

}
