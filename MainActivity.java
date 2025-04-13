package com.example.kulubecioglu_lab5;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.opengl.GLES32;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

// Texturing örnekleri için ana activity sınıfı
public class MainActivity extends AppCompatActivity {

    // GLSurfaceView nesnesi (OpenGL çizim penceresi)
    private GLSurfaceView gLView;

    // Uygulamanın ana contexti
    private Context contextMain;

    // Şu anki aktif çalışma modu (renderer)
    private myWorkMode wmRef = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contextMain = this;

        // GLSurfaceView oluşturuluyor ve set ediliyor
        gLView = new MyGLSurfaceView(this);
        setContentView(gLView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Menüye farklı sahne seçenekleri ekleniyor
        menu.add(0, 1, 0, "Simplest");
        menu.add(0, 2, 0, "Chess board");
        menu.add(0, 3, 0, "Cube");
        menu.add(0, 4, 0, "Cube (atlas tex)");
        menu.add(0, 5, 0, "Skybox");
        menu.add(0, 6, 0, "Sea view");
        menu.add(0, 7, 0, "Planet");
        menu.add(0, 8, 0, "Torus");

        // Diğer menü öğeleri eklenebilir
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Menüden seçilen sahneye göre başlık güncelleniyor
        setTitle(item.getTitle());

        // Seçilen menüye göre uygun mod başlatılıyor
        switch (item.getItemId()) {
            case 1:
                myModeStart(new mySimplestMode(), GLSurfaceView.RENDERMODE_WHEN_DIRTY);
                return true;
            case 2:
                myModeStart(new myChessMode(), GLSurfaceView.RENDERMODE_WHEN_DIRTY);
                return true;
            case 3:
                myModeStart(new myCubeMode(), GLSurfaceView.RENDERMODE_WHEN_DIRTY);
                return true;
            case 4:
                myModeStart(new myCubeAtlasMode(), GLSurfaceView.RENDERMODE_WHEN_DIRTY);
                return true;
            case 5:
                myModeStart(new mySkyBoxMode(), GLSurfaceView.RENDERMODE_WHEN_DIRTY);
                return true;
            case 6:
                myModeStart(new mySeaMode(), GLSurfaceView.RENDERMODE_WHEN_DIRTY);
                return true;
            case 7:
                myModeStart(new myPlanetMode(), GLSurfaceView.RENDERMODE_WHEN_DIRTY);
                return true;
            case 8:
                myModeStart(new myTorusMode(), GLSurfaceView.RENDERMODE_WHEN_DIRTY);
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // Yeni sahne modunu başlatmak için kullanılan yardımcı metot
    public void myModeStart(myWorkMode wmode, int rendermode) {
        wmRef = wmode;
        gLView.setRenderMode(rendermode);
        gLView.requestRender();
    }

    // Özel GLSurfaceView sınıfı
    public class MyGLSurfaceView extends GLSurfaceView {

        public MyGLSurfaceView(Context context){
            super(context);
            // OpenGL ES 2.0 ya da 3.0 kullanımı seçiliyor
            setEGLContextClientVersion(2);

            // Renderer tanımlanıyor
            setRenderer(new MyGLRenderer());

            // RENDERMODE_WHEN_DIRTY: sadece istek geldiğinde yeniden çizer
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            // Kullanıcı dokunma hareketleri burada işleniyor
            if (wmRef == null) return false;
            if (wmRef.onTouchNotUsed()) return false;

            int cx = this.getWidth();
            int cy = this.getHeight();
            float xtouch = e.getX();
            float ytouch = e.getY();

            // Dokunma türüne göre tepki veriliyor
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (wmRef.onActionDown(xtouch, ytouch, cx, cy))
                        requestRender();
                    setTitle(wmRef.stringInfo());
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (wmRef.onActionMove(xtouch, ytouch, cx, cy))
                        requestRender();
                    setTitle(wmRef.stringInfo());
                    break;
                default:
                    break;
            }

            return true;
        }
    }

    // OpenGL Renderer sınıfı
    public class MyGLRenderer implements GLSurfaceView.Renderer {

        private int myRenderHeight = 1, myRenderWidth = 1;

        public void onSurfaceCreated(GL10 unused, EGLConfig config) {
            // Arka plan rengi burada ayarlanıyor (siyah)
            GLES32.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        }

        public void onSurfaceChanged(GL10 unused, int width, int height) {
            // Viewport ekran boyutuna göre ayarlanıyor
            GLES32.glViewport(0, 0, width, height);
            myRenderWidth = width;
            myRenderHeight = height;
        }

        public void onDrawFrame(GL10 unused) {
            // Arka plan temizleniyor
            if (wmRef != null) wmRef.clearColor();
            GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT | GLES32.GL_DEPTH_BUFFER_BIT);

            if (wmRef == null) return;

            // Shader programı doğru şekilde yüklenmiş mi kontrolü
            if (wmRef.getProgramId() < 0) return;

            // Eğer ilk kez çizim yapılacaksa sahne oluşturuluyor
            if (wmRef.getProgramId() == 0) {
                wmRef.myCreateScene(contextMain);
            }

            if (wmRef.getProgramId() <= 0) return;

            // Shader programı aktif hale getiriliyor
            GLES32.glUseProgram(wmRef.getProgramId());

            // Sahne çizimi başlatılıyor
            wmRef.myUseProgramForDrawing(myRenderWidth, myRenderHeight);
        }
    }
}
