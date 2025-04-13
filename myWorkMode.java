package com.example.kulubecioglu_lab5;

import static android.opengl.GLES20.GL_COMPILE_STATUS;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES32;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

// Bu sınıf, farklı OpenGL örnek modları için temel altyapıyı sağlar.
public class myWorkMode {

    protected int gl_Program = 0;  // Shader program ID

    protected int VAO_id = 0;     // Vertex Array Object ID
    protected int VBO_id = 0;     // Vertex Buffer Object ID

    protected myGraphicPrimitives mpObj = null;  // Grafik nesneleri için referans

    protected float[] arrayVertex = null; // Tüm vertex verilerinin tutulduğu dizi

    // Kamera pozisyonu tanımlanıyor
    protected float cameraPos[] = {0, -4, 1.5f};

    // Işık konumu ve ışık rengi
    protected float LightPos[] =  {0,0,0.5f };
    protected float LightClr[] = {1, 1, 1};

    // Kamera dönüş açıları
    protected float alphaViewAngle = 0;
    protected float betaViewAngle = 0;

    // Dönüşüm matrisleri
    protected float[] modelMatrix;
    protected float[] viewMatrix;
    protected float[] projectionMatrix;

    // Yapıcı metod – tüm temel değişkenler burada başlatılıyor
    myWorkMode() {
        gl_Program = 0;
        VAO_id = VBO_id = 0;
        modelMatrix = new float[16];
        viewMatrix = new float[16];
        projectionMatrix = new float[16];
    }

    // Shader program ID'sini döner
    public int getProgramId() {
        return gl_Program;
    }

    // Shader derleme işlemi – verilen shader kodunu derler
    protected int myCompileShader(int shadertype, String shadercode) {
        int shader_id = GLES32.glCreateShader(shadertype);
        GLES32.glShaderSource(shader_id, shadercode);
        GLES32.glCompileShader(shader_id);

        // Derleme hatası olup olmadığını kontrol eder
        int[] res = new int[1];
        GLES32.glGetShaderiv(shader_id, GL_COMPILE_STATUS, res, 0);
        if (res[0] != 1) return 0;
        return shader_id;
    }

    // Shader’ları derleyip programa bağlama işlemi
    protected void myCompileAndAttachShaders(String vsh, String fsh) {
        gl_Program = -1;
        int vertex_shader_id = myCompileShader(GLES32.GL_VERTEX_SHADER, vsh);
        if (vertex_shader_id == 0) return;
        int fragment_shader_id = myCompileShader(GLES32.GL_FRAGMENT_SHADER, fsh);
        if (fragment_shader_id == 0) return;

        gl_Program = GLES32.glCreateProgram();
        GLES32.glAttachShader(gl_Program, vertex_shader_id);
        GLES32.glAttachShader(gl_Program, fragment_shader_id);
        GLES32.glLinkProgram(gl_Program);

        // Artık shader objelerine ihtiyaç yok
        GLES32.glDeleteShader(vertex_shader_id);
        GLES32.glDeleteShader(fragment_shader_id);
    }

    // VAO ve VBO tanımlama işlemleri burada yapılır
    protected void getId_VAO_VBO() {
        int[] tmp = new int[2];
        GLES32.glGenVertexArrays(1, tmp, 0);
        VAO_id = tmp[0];
        GLES32.glGenBuffers(1, tmp, 0);
        VBO_id = tmp[0];
    }

    // Vertex verilerini GPU’ya gönderme ve attribute tanımlamaları burada yapılır
    protected void myVertexArrayBind3(float[] src, int stride,
                                      String atrib1, int offset1,
                                      String atrib2, int offset2,
                                      String atrib3, int offset3) {
        if (gl_Program <= 0) return;
        ByteBuffer bb = ByteBuffer.allocateDirect(src.length*4);
        bb.order(ByteOrder.nativeOrder());

        FloatBuffer vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(src);
        vertexBuffer.position(0);

        getId_VAO_VBO();

        GLES32.glBindVertexArray(VAO_id);
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, VBO_id);
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, src.length*4, vertexBuffer, GLES32.GL_STATIC_DRAW);

        // Attribute konumlarını bağlama işlemleri
        int handle;
        if (!atrib1.isEmpty()) {
            handle = GLES32.glGetAttribLocation(gl_Program, atrib1);
            GLES32.glEnableVertexAttribArray(handle);
            GLES32.glVertexAttribPointer(handle, 3, GLES32.GL_FLOAT, false,stride*4, offset1);
        }
        if (!atrib2.isEmpty()) {
            handle = GLES32.glGetAttribLocation(gl_Program, atrib2);
            GLES32.glEnableVertexAttribArray(handle);
            GLES32.glVertexAttribPointer(handle, 3, GLES32.GL_FLOAT, false,stride*4, offset2);
        }
        if (!atrib3.isEmpty()) {
            handle = GLES32.glGetAttribLocation(gl_Program, atrib3);
            GLES32.glEnableVertexAttribArray(handle);
            GLES32.glVertexAttribPointer(handle, 3, GLES32.GL_FLOAT, false,stride*4, offset3);
        }

        GLES32.glEnableVertexAttribArray(0); // İlk attribute’yi aktifleştir
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0);
        GLES32.glBindVertexArray(0);
    }

    // Doku yükleme işlemi burada yapılır
    public int myLoadTexture(Context context, int resourceId, int wrap) {
        int[] tmp = new int[2];
        GLES32.glGenTextures(1, tmp, 0);
        int handle = tmp[0];

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false; // bitmap ölçeklenmesin
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, handle);
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_S, wrap);
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_T, wrap);
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MIN_FILTER, GLES32.GL_LINEAR);
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MAG_FILTER, GLES32.GL_LINEAR);

        // Bitmap’i GPU’ya aktar
        GLUtils.texImage2D(GLES32.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle(); // geçici bitmap’i hafızadan sil
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, 0);
        return handle;
    }

    // Sahne oluşturma süreci – sırasıyla texture, obje ve shader hazırlanıyor
    public void myCreateScene(Context context) {
        myInitTextures(context);
        myCreateObjects();
        myCreateShaderProgram();
    }

    // Alt sınıflarda override edilecek metodlar
    protected void myInitTextures(Context context) { }
    protected void myCreateObjects() { }
    protected void myCreateShaderProgram() { }
    protected void myCreateProjection(int width, int height) { }
    protected void myDrawing() { }

    // Sahne çizim işlemleri burada yönetilir
    public void myUseProgramForDrawing(int width, int height) {
        GLES32.glEnable(GLES32.GL_DEPTH_TEST);
        myCreateProjection(width, height);
        GLES32.glBindVertexArray(VAO_id);
        myDrawing();
        GLES32.glBindVertexArray(0);
    }

    // Arka plan rengi tanımlanır
    public void clearColor() {
        GLES32.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);  // Varsayılan beyaz
    }

    // Dokunma olayları – isteğe göre override edilir
    public boolean onTouchNotUsed() { return true; }
    public boolean onActionDown(float x, float y, int cx, int cy) { return false; }
    public boolean onActionMove(float x, float y, int cx, int cy) { return false; }

    // Durum bilgisi döndürür (kamera açıları)
    public String stringInfo() {
        return String.format("a=%.1f b=%.1f", alphaViewAngle, betaViewAngle);
    }

}
