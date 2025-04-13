package com.example.kulubecioglu_lab5;

import android.content.Context;
import android.opengl.GLES32;

// Bu sınıf, gezegen (planet) modelinin sahnede oluşturulmasını sağlar
public class myPlanetMode extends mySimplestMode {

    // Yapıcı metod: Kamera açıları burada belirlenir
    myPlanetMode() {
        super();
        alphaViewAngle = 0;   // Yatay döndürme açısı
        betaViewAngle = 90;   // Dikey bakış açısı
    }

    // Sahne içerisindeki objeleri oluşturan metod
    protected void myCreateObjects() {
        mpObj = new myGraphicPrimitives();   // Geometrik şekilleri çizecek yardımcı sınıf
        int pos = 0;

        // Küre geometrisini önce boş olarak oluşturarak vertex sayısı hesaplanır
        pos = mpObj.addSphereXYZnt(null, pos,
                0, 0, 0,                 // Küre merkez koordinatları
                1.2f, 18, 36,            // Yarıçap ve segment sayıları
                textureHandle, 1.0f, 1.0f, 1.0f);  // Texture ve renk

        // Gerçek vertex dizisi oluşturulur
        arrayVertex = new float[pos];
        pos = 0;

        // Bu kez küre verileri arrayVertex içine doldurulur
        pos = mpObj.addSphereXYZnt(arrayVertex, pos,
                0, 0, 0,
                1.2f, 18, 36,
                textureHandle, 1.0f, 1.0f, 1.0f);
    }

    // Texture yüklemesini gerçekleştiren metod
    @Override
    protected void myInitTextures(Context context) {
        // img_1 adlı planet texture’ı yükleniyor
        textureHandle = myLoadTexture(context, R.drawable.img_1, GLES32.GL_CLAMP_TO_EDGE);
    }

    // Arkaplan rengi siyah olarak ayarlanır
    @Override
    public void clearColor() {
        GLES32.glClearColor(0f, 0f, 0f, 1f);
    }

}
