package com.example.kulubecioglu_lab5;

import android.content.Context;
import android.opengl.GLES32;

// Texture atlas ile kaplanmış küp nesnesini tanımlayan sınıf.
public class myCubeAtlasMode extends mySimplestMode {

    // Küpün 6 yüzü için vertex koordinatları ve texture koordinatları
    protected float[] cubeVertexArray = {
            // position        xt  yt
            -1, -1, -1,   0, 1,    // front face
            -1, -1,  1,   0, 0,
            1, -1,  1,   1, 0,
            1, -1, -1,   1, 1,

            1, -1, -1,   0, 1,    // right face
            1, -1,  1,   0, 0,
            1,  1,  1,   1, 0,
            1,  1, -1,   1, 1,

            -1,  1, -1,   0, 1,    // left face
            -1,  1,  1,   0, 0,
            -1, -1,  1,   1, 0,
            -1, -1, -1,   1, 1,

            1,  1, -1,   0, 1,    // back face
            1,  1,  1,   0, 0,
            -1,  1,  1,   1, 0,
            -1,  1, -1,   1, 1,

            -1, -1,  1,   0, 1,    // top face
            -1,  1,  1,   0, 0,
            1,  1,  1,   1, 0,
            1, -1,  1,   1, 1,

            -1,  1, -1,   0, 1,    // bottom face
            -1, -1, -1,   0, 0,
            1, -1, -1,   1, 0,
            1,  1, -1,   1, 1
    };

    // Yapıcı metod - kamera açıları ve uzaklığı ayarlanır
    myCubeAtlasMode() {
        super();
        alphaViewAngle = 30;
        betaViewAngle = 70;
        zDistance = 8;
    }

    // Grafik nesneleri oluşturuluyor
    protected void myCreateObjects() {
        arrayVertex = new float[200]; // vertex dizisi oluşturuluyor
        mpObj = new myGraphicPrimitives(); // yardımcı grafik nesne sınıfı
        int pos = 0;

        // Her yüz için texture atlas koordinatları hesaplanarak ekleniyor
        for (int i = 0; i < 6; i++) {
            int st = i * 20;
            cubeVertexArray[st + 3]  = cubeVertexArray[st + 8]  = (float)i / 6;      // X texture başlangıcı
            cubeVertexArray[st + 13] = cubeVertexArray[st + 18] = (float)(i + 1) / 6; // X texture bitişi

            pos = mpObj.addQuadXYZnt(
                    arrayVertex, pos,
                    cubeVertexArray, st, textureHandle,
                    1.0f, 1.0f, 1.0f);
        }
    }

    // Texture yüklemesi burada yapılır
    @Override
    protected void myInitTextures(Context context) {
        // Texture atlas dosyası yükleniyor
        textureHandle = myLoadTexture(context, R.drawable.img_8, GLES32.GL_CLAMP_TO_EDGE);
    }

    // Arka plan rengi siyah olarak ayarlanıyor
    @Override
    public void clearColor() {
        GLES32.glClearColor(0f, 0f, 0f, 1f);
    }

}
