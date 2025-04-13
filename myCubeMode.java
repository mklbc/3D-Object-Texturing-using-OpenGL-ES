package com.example.kulubecioglu_lab5;

import android.content.Context;
import android.opengl.GLES32;

// Her bir yüzey için ayrı ayrı texture kullanan küp modunu tanımlayan sınıf.
public class myCubeMode extends myCubeAtlasMode {

    // Her yüzeye ait texture handle dizisi tanımlanıyor
    protected int[] texHandles = null;

    // Yapıcı metot - Üst sınıf özelliklerini alır
    myCubeMode() {
        super();
    }

    // Grafik objeleri oluşturuluyor (her yüz için ayrı texture atanıyor)
    protected void myCreateObjects() {
        arrayVertex = new float[200]; // vertex bilgileri için dizi tanımı
        mpObj = new myGraphicPrimitives(); // grafik nesne yöneticisi atanıyor
        int pos = 0;

        // 6 yüz için döngüyle ayrı ayrı quad çizimi yapılıyor
        for (int i = 0; i < 6; i++)
            pos = mpObj.addQuadXYZnt(arrayVertex, pos,
                    cubeVertexArray, i * 20, texHandles[i],
                    1.0f, 1.0f, 1.0f);
    }

    // Her bir yüzey için farklı bir görsel texture olarak yükleniyor
    @Override
    protected void myInitTextures(Context context) {
        texHandles = new int[6];

        // Her yüzeye karşılık gelen resim dosyası atanıyor
        texHandles[0] = myLoadTexture(context, R.drawable.img_2, GLES32.GL_CLAMP_TO_EDGE);   // Front face
        texHandles[1] = myLoadTexture(context, R.drawable.img_3, GLES32.GL_CLAMP_TO_EDGE);   // Right face
        texHandles[2] = myLoadTexture(context, R.drawable.img_4, GLES32.GL_CLAMP_TO_EDGE);   // Left face
        texHandles[3] = myLoadTexture(context, R.drawable.img_5, GLES32.GL_CLAMP_TO_EDGE);   // Back face
        texHandles[4] = myLoadTexture(context, R.drawable.img_6, GLES32.GL_CLAMP_TO_EDGE);   // Top face
        texHandles[5] = myLoadTexture(context, R.drawable.img_7, GLES32.GL_CLAMP_TO_EDGE);   // Bottom face
    }

}
