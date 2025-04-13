package com.example.kulubecioglu_lab5;

import android.content.Context;
import android.opengl.GLES32;
import android.opengl.Matrix;

// En basit render modunu temsil eden sınıf. Kamerayı kontrol etme, dokularla nesne çizme gibi temel işlemler yapılır.
public class mySimplestMode extends myWorkMode {
    // Kamera kontrolü için açıları ve dokunma bilgileri
    private float alphaAnglePrev, betaAnglePrev;
    protected float zDistance; // Kamera uzaklığı
    private float xTouchDown, yTouchDown;

    protected int textureHandle; // Texture nesnesi tanımı

    // Yapıcı metod - başlangıç açıları ve uzaklık ayarlanıyor
    mySimplestMode() {
        super();
        alphaViewAngle = 0;
        betaViewAngle = 0;
        zDistance = 5;
    }

    // Sahneye eklenecek objeler burada tanımlanır
    protected void myCreateObjects() {
        arrayVertex = new float[100]; // Vertex array için yer açılır
        mpObj = new myGraphicPrimitives(); // Geometrik şekiller için yardımcı sınıf kullanılır
        int pos = 0;
        float[] tmpv = {     // Doku koordinatları ile birlikte kare şekli
                -1, -1, 0,   0, 1,
                -1,  1, 0,   0, 0,
                1,  1, 0,   1, 0,
                1, -1, 0,   1, 1 };
        // Quad nesnesi ekleniyor
        mpObj.addQuadXYZnt(
                arrayVertex, pos, tmpv, 0,
                textureHandle, 1.0f,1.0f,1.0f);
    }

    // Texture yükleme işlemleri burada yapılır
    @Override
    protected void myInitTextures(Context context) {
        textureHandle = myLoadTexture(context, R.drawable.img_17, GLES32.GL_CLAMP_TO_EDGE);
    }

    // Shader programı oluşturulur (Vertex + Fragment Shader)
    public void myCreateShaderProgram() {
        myCompileAndAttachShaders(
                myShadersLibrary.vertexShaderCode8,
                myShadersLibrary.fragmentShaderCode11); // Temel doku shaderları
        // Vertex verisi bağlanır (pozisyon + texture koordinatları)
        myVertexArrayBind3(arrayVertex, 8,
                "vPosition", 0,
                "vTexture", 6*4,
                "", 0);
    }

    // Projeksiyon ve kamera matrisi burada oluşturulur
    protected void myCreateProjection(int width, int height) {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.setIdentityM(projectionMatrix, 0);

        // Kamera konumu ve dönüşleri uygulanır
        Matrix.translateM(viewMatrix, 0, 0, 0, -zDistance);
        Matrix.rotateM(viewMatrix, 0, -betaViewAngle, 1, 0, 0);
        Matrix.rotateM(viewMatrix, 0, -alphaViewAngle, 0, 0, 1);

        float aspect = (float) width / height;
        Matrix.perspectiveM(projectionMatrix, 0, 45, aspect, 0.1f, 30);

        // Shader’a matrisler gönderilir
        int uHandle = GLES32.glGetUniformLocation(gl_Program, "uViewMatrix");
        GLES32.glUniformMatrix4fv(uHandle, 1, false, viewMatrix, 0);
        uHandle = GLES32.glGetUniformLocation(gl_Program, "uProjMatrix");
        GLES32.glUniformMatrix4fv(uHandle, 1, false, projectionMatrix, 0);
        uHandle = GLES32.glGetUniformLocation(gl_Program, "uModelMatrix");
        GLES32.glUniformMatrix4fv(uHandle, 1, false, modelMatrix, 0);
    }

    // Sahnedeki objeler çizdirilir
    protected void myDrawing() {
        GLES32.glBindVertexArray(VAO_id); // Vertex dizisi bağlanır
        mpObj.drawAllObjects(-1); // Tüm objeler çizilir
        GLES32.glBindVertexArray(0); // VAO bağlantısı kesilir
    }

    // Dokunma kontrolü kullanılmıyor bilgisi
    @Override
    public boolean onTouchNotUsed() { return false; }

    // Dokunma başlangıcında yapılacak işlemler
    @Override
    public boolean onActionDown(float x, float y, int cx, int cy) {
        xTouchDown = x;
        yTouchDown = y;
        alphaAnglePrev = alphaViewAngle;
        betaAnglePrev = betaViewAngle;

        // Alt kısma tıklanırsa uzaklığı artır
        if (y > 0.9f*cy) {
            if (zDistance <= 2.0f)
                zDistance += 0.1;
            else zDistance *= 1.1892f;
            return true;
        }

        // Üst kısma tıklanırsa uzaklığı azalt
        if (y < 0.1f*cy) {
            if (zDistance <= 2.0f)
                zDistance -= 0.1;
            else zDistance *= 0.841f;
            if (zDistance < 0) zDistance = 0;
            return true;
        }
        return false;
    }

    // Dokunma hareketi sırasında kamerayı döndür
    @Override
    public boolean onActionMove(float x, float y, int cx, int cy) {
        alphaViewAngle = alphaAnglePrev + 0.2f*(xTouchDown - x);
        betaViewAngle = betaAnglePrev + 0.2f*(yTouchDown - y);
        return true;
    }

    // Arkaplan rengini belirle
    @Override
    public void clearColor() {
        GLES32.glClearColor(0f, 0f, 0f, 1f); // Siyah arkaplan
    }

    // Kamera bilgilerini yazdırır
    public String stringInfo() {
        return String.format("a=%.1f b=%.1f d=%.1f", alphaViewAngle, betaViewAngle, zDistance);
    }
}
