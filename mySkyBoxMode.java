package com.example.kulubecioglu_lab5;

import android.content.Context;
import android.opengl.GLES32;

// SkyBox modu için kullanılan sınıf. Arka planı kaplayan bir küp görseli oluşturur.
public class mySkyBoxMode extends myCubeMode {

    // Yapıcı metod – başlangıç kamera açıları ve uzaklık tanımlanıyor
    mySkyBoxMode() {
        super();
        alphaViewAngle = 40;  // Kamera Z ekseni etrafında döndürülme açısı
        betaViewAngle = 90;   // Kamera X ekseni etrafında döndürülme açısı
        zDistance = 0;        // Skybox için genellikle mesafe sıfırdır
    }

    // Skybox dokularını yükleyen metod
    @Override
    protected void myInitTextures(Context context) {
        texHandles = new int[6]; // Altı yüz için altı ayrı doku tanımlanır

        // Her yüz için uygun görsel dosyaları atanıyor
        texHandles[0] = myLoadTexture(context, R.drawable.img_9, GLES32.GL_CLAMP_TO_EDGE);   // Ön yüz
        texHandles[1] = myLoadTexture(context, R.drawable.img_10, GLES32.GL_CLAMP_TO_EDGE);  // Sağ yüz
        texHandles[2] = myLoadTexture(context, R.drawable.img_11, GLES32.GL_CLAMP_TO_EDGE);  // Sol yüz
        texHandles[3] = myLoadTexture(context, R.drawable.img_12, GLES32.GL_CLAMP_TO_EDGE);  // Arka yüz
        texHandles[4] = myLoadTexture(context, R.drawable.img_13, GLES32.GL_CLAMP_TO_EDGE);  // Üst yüz
        texHandles[5] = myLoadTexture(context, R.drawable.img_14, GLES32.GL_CLAMP_TO_EDGE);  // Alt yüz
    }
}
