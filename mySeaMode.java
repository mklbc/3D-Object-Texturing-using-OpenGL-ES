package com.example.kulubecioglu_lab5;

import android.content.Context;
import android.opengl.GLES32;

// Deniz manzarası için özel olarak oluşturulmuş sahne modu
public class mySeaMode extends myCubeMode {

    // Yapıcı metod: Kamera açıları ve uzaklık değeri ayarlanıyor
    mySeaMode() {
        super();
        alphaViewAngle = 21;     // Yatay döndürme açısı
        betaViewAngle = 80;      // Dikey bakış açısı
        zDistance = 0.0f;        // Kamera uzaklığı
    }

    // Sahne objelerinin oluşturulduğu kısım
    protected void myCreateObjects() {
        mpObj = new myGraphicPrimitives();  // Geometrik nesne yöneticisi
        int pos = 0;

        // Torus nesnesi oluşturuluyor (ilk hesaplama için null ile)
        pos = mpObj.addTorusXYZnt(null, pos,
                0, 0, 0,
                0.2f, 0.05f,
                18, 36,
                texHandles[6], 1.0f, 1.0f, 1.0f);

        // Vertex dizisi hazırlanıyor
        arrayVertex = new float[pos + 300];

        // Küp yüzeyleri sahneye ekleniyor
        pos = 0;
        for (int i = 0; i < 6; i++)
            pos = mpObj.addQuadXYZnt(arrayVertex, pos,
                    cubeVertexArray, i * 20, texHandles[i],
                    1.0f, 1.0f, 1.0f);

        // Torus objesi sahneye ekleniyor (konumlandırılmış)
        float xc = -1.0f;
        float yc = 3.0f;
        float zc = -0.95f;
        pos = mpObj.addTorusXYZnt(arrayVertex, pos,
                xc, yc, zc,
                0.2f, 0.05f,
                18, 36,
                texHandles[6], 1.0f, 1.0f, 1.0f);

        // Satranç tahtası benzeri bir yüzey ekleniyor
        float[] tmpv = {
                // Konum ve texture koordinatları
                -0.15f + xc, -0.15f + yc, zc, 0.01f, 3.99f,
                -0.15f + xc, 0.15f + yc,  zc, 0.01f, 0.01f,
                0.15f + xc, 0.15f + yc,  zc, 3.99f, 0.01f,
                0.15f + xc, -0.15f + yc,  zc, 3.99f, 3.99f};
        mpObj.addQuadXYZnt(
                arrayVertex, pos, tmpv, 0,
                texHandles[7], 1.0f, 1.0f, 1.0f);
    }

    // Gerekli texture’ların yüklendiği metod
    @Override
    protected void myInitTextures(Context context) {
        texHandles = new int[8];

        // Küp yüzeyleri için texture’lar
        texHandles[0] = myLoadTexture(context, R.drawable.img_9, GLES32.GL_CLAMP_TO_EDGE);   // front
        texHandles[1] = myLoadTexture(context, R.drawable.img_10, GLES32.GL_CLAMP_TO_EDGE);  // right
        texHandles[2] = myLoadTexture(context, R.drawable.img_11, GLES32.GL_CLAMP_TO_EDGE);  // left
        texHandles[3] = myLoadTexture(context, R.drawable.img_12, GLES32.GL_CLAMP_TO_EDGE);  // back
        texHandles[4] = myLoadTexture(context, R.drawable.img_13, GLES32.GL_CLAMP_TO_EDGE);  // top
        texHandles[5] = myLoadTexture(context, R.drawable.img_14, GLES32.GL_CLAMP_TO_EDGE);  // bottom

        // Torus ve chessboard yüzeyleri için özel texture’lar
        texHandles[6] = myLoadTexture(context, R.drawable.img_16, GLES32.GL_REPEAT);  // torus
        texHandles[7] = myLoadTexture(context, R.drawable.img_15, GLES32.GL_REPEAT);  // chessboard
    }

    // Çizim işlemleri burada yapılır
    protected void myDrawing() {
        GLES32.glBindVertexArray(VAO_id);        // VAO bağlanıyor

        GLES32.glDepthMask(false);               // Derinlik yazımı kapatılıyor
        mpObj.drawObjects(0, 5, -1);              // İlk 6 nesne çiziliyor (örneğin küp yüzeyleri)

        GLES32.glDepthMask(true);                // Derinlik yazımı tekrar açılıyor
        mpObj.drawObjects(6, mpObj.getNumObj() - 1, -1);  // Kalan objeler çiziliyor (torus, chessboard)

        GLES32.glBindVertexArray(0);             // VAO çözülüyor
    }

}
