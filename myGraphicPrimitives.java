package com.example.kulubecioglu_lab5;

import android.opengl.GLES32;

// Bu sınıf farklı geometri şekillerini oluşturmak ve çizmek için temel yardımcı yapıdır.
public class myGraphicPrimitives {

    private final int floatPerVertex = 8; // Her vertexin içerdiği float sayısı (x,y,z,nx,ny,nz,xt,yt)
    private int numObj, capacity;
    private int[] indxVertex = null;
    private int[] typeObj = null;
    private int[] textureObj = null;
    private float[] clrObj = null;

    private int currentTexHandle;

    final private double pi = 3.1415926535897932;
    final private double twopi = 6.283185307179586477;

    // Yapıcı metot – başlangıçta tüm diziler tanımlanıyor
    myGraphicPrimitives() {
        numObj = 0;
        capacity = 100;
        indxVertex = new int[capacity];
        typeObj = new int[capacity];
        textureObj = new int[capacity];
        for (int i = 0; i < capacity; i++)
            textureObj[i] = -1;
        clrObj = new float[3 * capacity];
        currentTexHandle = -1;
    }

    // Gerekirse dizi kapasitelerini artırmak için kullanılan metod
    private void reallocArrays(int capnew) {
        if (capnew <= capacity) return;
        int indxVertexNew[] = new int[capnew];
        int typeObjNew[] = new int[capnew];
        int textureObjNew[] = new int[capnew];
        for (int i = 0; i < capnew; i++)
            textureObjNew[i] = -1;
        for (int i = 0; i < capacity; i++) {
            indxVertexNew[i] = indxVertex[i];
            typeObjNew[i] = typeObj[i];
            textureObjNew[i] = textureObj[i];
        }
        indxVertex = indxVertexNew;
        typeObj = typeObjNew;
        textureObj = textureObjNew;

        float clrObjNew[] = new float[3 * capnew];
        capacity *= 3;
        for (int i = 0; i < capacity; i++)
            clrObjNew[i] = clrObj[i];
        clrObj = clrObjNew;
        capacity = capnew;
    }

    // Yeni obje başlatma işlemi – başlangıç vertex bilgisi ve renk
    public int startNewObject(int pos, int typ, int tex, float r, float g, float b) {
        int stv = pos / floatPerVertex;
        if (numObj > capacity - 2) {
            reallocArrays(capacity + 100);
        }
        indxVertex[numObj] = stv;
        typeObj[numObj] = typ;
        textureObj[numObj] = tex;
        int indclr = 3 * numObj;
        clrObj[indclr++] = r;
        clrObj[indclr++] = g;
        clrObj[indclr] = b;
        numObj++;
        indxVertex[numObj] = stv;
        return stv;
    }

    // Obje bitişini işaretler
    public void endObject(int pos) {
        indxVertex[numObj] = pos / floatPerVertex;
    }

    // Toplam obje sayısını döndürür
    public int getNumObj() {
        return numObj;
    }

    // Belirli aralıktaki objeleri çizen metot
    public void drawObjects(int sto, int eno, int uclrhandle) {
        if ((indxVertex == null) || (numObj < 1)) return;
        for (int numo = sto; numo <= eno; numo++) {
            int stv = indxVertex[numo];
            int nv = indxVertex[numo + 1] - stv;
            if (nv <= 0) return;
            if (uclrhandle > 0) {
                int indclr = 3 * numo;
                float clr[] = {clrObj[indclr], clrObj[indclr + 1], clrObj[indclr + 2]};
                GLES32.glUniform3fv(uclrhandle, 1, clr, 0);
            }
            int texhandle = textureObj[numo];
            if (texhandle >= 0) {
                if (texhandle != currentTexHandle) {
                    GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, texhandle);
                    currentTexHandle = texhandle;
                }
            }
            GLES32.glDrawArrays(typeObj[numo], stv, nv);
        }
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, 0);
        currentTexHandle = -1;
    }

    // Tüm objeleri tek seferde çizer
    public void drawAllObjects(int uclrhandle) {
        if ((indxVertex == null) || (numObj < 1)) return;
        for (int numo = 0; numo < numObj; numo++) {
            int stv = indxVertex[numo];
            int nv = indxVertex[numo + 1] - stv;
            if (nv <= 0) return;
            if (uclrhandle > 0) {
                int indclr = 3 * numo;
                float clr[] = {clrObj[indclr], clrObj[indclr + 1], clrObj[indclr + 2]};
                GLES32.glUniform3fv(uclrhandle, 1, clr, 0);
            }
            int texhandle = textureObj[numo];
            if (texhandle >= 0) {
                if (texhandle != currentTexHandle) {
                    GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, texhandle);
                    currentTexHandle = texhandle;
                }
            }
            GLES32.glDrawArrays(typeObj[numo], stv, nv);
        }
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, 0);
        currentTexHandle = -1;
    }

    // Tek bir vertex ekleme işlemi (pozisyon, normal ve texture koordinatları)
    public int addVertexXYZnt(float[] vdest, int pos,
                              float x, float y, float z,
                              float xn, float yn, float zn,
                              float xt, float yt) {
        if (vdest == null) return pos + floatPerVertex;
        float N = (float) Math.sqrt(xn * xn + yn * yn + zn * zn);
        if (N <= 0) return pos;
        vdest[pos++] = x;
        vdest[pos++] = y;
        vdest[pos++] = z;
        vdest[pos++] = xn / N;
        vdest[pos++] = yn / N;
        vdest[pos++] = zn / N;
        vdest[pos++] = xt;
        vdest[pos++] = yt;
        return pos;
    }

    // Dörtlü geometri (quad) ekleme işlemi
    public int addQuadXYZnt(float[] vdest, int pos,
                            float[] src, int is,
                            int texhandle,
                            float r, float g, float b) {

        if (vdest == null) return pos + floatPerVertex * 4;
        startNewObject(pos, GLES32.GL_TRIANGLE_FAN, texhandle, r, g, b);
        float xn = (src[is+7] - src[is+2]) * (src[is+11] - src[is+1]) - (src[is+6] - src[is+1]) * (src[is+12] - src[is+2]);
        float yn = (src[is+5] - src[is+0]) * (src[is+12] - src[is+2]) - (src[is+7] - src[is+2]) * (src[is+10] - src[is]);
        float zn = (src[is+6] - src[is+1]) * (src[is+10] - src[is])   - (src[is+5] - src[is]) * (src[is+11] - src[is+1]);
        for (int i=0; i<20; i+=5)
            pos = addVertexXYZnt(vdest, pos,
                    src[is+i], src[is+i+1], src[is+i+2],
                    xn, yn, zn,
                    src[is+i+3], src[is+i+4]);
        endObject(pos);
        return pos;
    }

    // Longitude açısını texture koordinatına dönüştür
    private float texX(double dl) {
        return (float)(dl / twopi);
    }

    // Latitude açısını texture koordinatına dönüştür
    private float texY(double sh) {
        double y = 0.5 + sh / pi;
        if (y > 1.0f) y = 1.0f;
        if (y < 0) y = 0;
        return (float)(1.0 - y);
    }

    // Küresel geometri oluşturur
    public int addSphereXYZnt(float[] vdest, int pos,
                              float xc, float yc, float zc,
                              float radius, int seglat, int seglong,
                              int texhandle,
                              float r, float g, float b) {

        if ((seglat < 1) || (seglong < 1)) return pos;
        if (vdest == null) return pos + floatPerVertex * 2 * seglat * (seglong + 1);

        for (int s = 0; s < seglat; s++) {
            startNewObject(pos, GLES32.GL_TRIANGLE_STRIP, texhandle, r, g, b);
            for (int l = 0; l <= seglong; l++) {
                double dl = twopi * (double) l / seglong;
                double sh = -pi * (0.5 - (double) s / seglat);
                float xn = (float) (Math.cos(sh) * Math.cos(dl));
                float yn = (float) (Math.cos(sh) * Math.sin(dl));
                float zn = (float) Math.sin(sh);
                pos = addVertexXYZnt(vdest, pos,
                        xc + xn * radius, yc + yn * radius, zc + zn * radius,
                        xn, yn, zn,
                        texX(dl), texY(sh));

                sh = -pi * (0.5 - (double) (s + 1) / seglat);
                xn = (float) (Math.cos(sh) * Math.cos(dl));
                yn = (float) (Math.cos(sh) * Math.sin(dl));
                zn = (float) Math.sin(sh);
                pos = addVertexXYZnt(vdest, pos,
                        xc + xn * radius, yc + yn * radius, zc + zn * radius,
                        xn, yn, zn,
                        texX(dl), texY(sh));
            }
            endObject(pos);
        }

        return pos;
    }

    // Torus (halka) geometrisini oluşturur
    public int addTorusXYZnt(float[] vdest, int pos,
                             float xc, float yc, float zc,
                             float R, float radius,
                             int seglat, int seglong,
                             int texhandle,
                             float r, float g, float b) {

        if ((seglat < 3) || (seglong < 3)) return pos;
        if (vdest == null) return pos + floatPerVertex * 2 * seglat * (seglong + 1);
        for (int s = 0; s < seglat; s++) {
            startNewObject(pos, GLES32.GL_TRIANGLE_STRIP, texhandle, r, g, b);
            for (int l = 0; l <= seglong; l++) {
                float dl = 6.2831853f * (float) l / seglong;
                float x = R * (float) Math.cos(dl);
                float y = R * (float) Math.sin(dl);

                float sh = 6.2831853f * (float) s / seglat;
                float xn = (float) Math.cos(sh) * (float) Math.cos(dl);
                float yn = (float) Math.cos(sh) * (float) Math.sin(dl);
                float zn = (float) Math.sin(sh);
                float xt = (float) (dl / twopi);
                float yt = (float) (sh / twopi);
                pos = addVertexXYZnt(vdest, pos,
                        xc + xn * radius + x, yc + yn * radius + y, zc + zn * radius,
                        xn, yn, zn,
                        xt, yt);

                sh = 6.2831853f * (float) (s + 1) / seglat;
                xn = (float) Math.cos(sh) * (float) Math.cos(dl);
                yn = (float) Math.cos(sh) * (float) Math.sin(dl);
                zn = (float) Math.sin(sh);
                yt = (float) (sh / twopi);
                pos = addVertexXYZnt(vdest, pos,
                        xc + xn * radius + x, yc + yn * radius + y, zc + zn * radius,
                        xn, yn, zn,
                        xt, yt);
            }
            endObject(pos);
        }
        return pos;
    }

}
