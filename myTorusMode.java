package com.example.kulubecioglu_lab5;

import android.content.Context;
import android.opengl.GLES32;

// Bu sınıf, torus (halka) geometrisinin sahnede gösterilmesini sağlar.
public class myTorusMode extends mySimplestMode {

    private int[] texHandles; // Doku tanımlayıcıları burada tutuluyor

    // Yapıcı metod – başlangıçta kamera açılarını ayarlıyoruz
    myTorusMode() {
        super();
        alphaViewAngle = 0;   // Kamera döndürme açısı - Z ekseni
        betaViewAngle = 70;   // Kamera döndürme açısı - X ekseni
    }

    // Sahnede çizilecek nesneleri oluşturan metod
    protected void myCreateObjects() {
        mpObj = new myGraphicPrimitives(); // Grafik nesnelerini tutacak sınıf nesnesi
        int pos = 0;

        // Torus nesnesini sahneye eklemeden önce boyutunu hesaplatıyoruz (null ile)
        pos = mpObj.addTorusXYZnt(null, pos,
                0, 0, 0,
                0.7f, 0.2f,
                18, 36,
                texHandles[0], 0.5f, 1.0f, 0.0f);

        // Hafızada gerekli yer açılıyor
        arrayVertex = new float[pos+100];
        pos = 0;

        // Asıl torus nesnesi sahneye ekleniyor
        pos = mpObj.addTorusXYZnt(arrayVertex, pos,
                0, 0, 0,
                0.7f, 0.2f,
                18, 36,
                texHandles[0], 1.0f, 1.0f, 1.0f);

        // Alt zemine bir kare (quad) doku ekliyoruz
        float[] tmpv = {
                // Konumlar ve texture koordinatları
                -0.5f, -0.5f, 0,   0.01f, 3.99f,
                -0.5f,  0.5f, 0,   0.01f, 0.01f,
                0.5f,  0.5f, 0,   3.99f, 0.01f,
                0.5f, -0.5f, 0,   3.99f, 3.99f };
        mpObj.addQuadXYZnt(
                arrayVertex, pos, tmpv, 0,
                texHandles[1], 1.0f, 1.0f, 1.0f);
    }

    // Dokular bu metodla yükleniyor
    @Override
    protected void myInitTextures(Context context) {
        texHandles = new int[2]; // İki farklı doku kullanılıyor
        texHandles[0] = myLoadTexture(context, R.drawable.img_16, GLES32.GL_REPEAT); // Torus için doku
        texHandles[1] = myLoadTexture(context, R.drawable.img_15, GLES32.GL_REPEAT); // Alt zemin için doku
    }

    // Arka plan rengi siyah olarak ayarlanıyor
    @Override
    public void clearColor() {
        GLES32.glClearColor(0f, 0f, 0f, 1f);
    }

}
