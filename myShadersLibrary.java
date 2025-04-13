package com.example.kulubecioglu_lab5;

// Shader kodlarının yer aldığı yardımcı sınıf
public class myShadersLibrary {

    // === Vertex Shaders ===

    // Basit doku koordinatlarını kullanan vertex shader
    public static final String vertexShaderCode8 =
            "#version 300 es\n" +
                    "in vec3 vPosition;\n" +               // Tepe noktası pozisyonu
                    "in vec2 vTexture;\n" +                 // Texture koordinatları

                    "uniform mat4 uModelMatrix;\n" +        // Model matrisi
                    "uniform mat4 uViewMatrix;\n" +         // Görünüm matrisi
                    "uniform mat4 uProjMatrix;\n" +         // Projeksiyon matrisi

                    "out vec2 currentTexCoord;\n" +         // Fragment shader’a gönderilen texture koordinatı

                    "void main() {\n" +
                    "   gl_Position = uProjMatrix * uViewMatrix * uModelMatrix * vec4(vPosition, 1.0f);\n" +
                    "   currentTexCoord = vec2(vTexture.x, vTexture.y);\n" +
                    "}\n";

    // Aydınlatma (normal, pozisyon ve texture) bilgilerini içeren gelişmiş vertex shader
    public static final String vertexShaderCode9 =
            "#version 300 es\n" +
                    "in vec3 vPosition;\n" +               // Tepe noktası pozisyonu
                    "in vec3 vNormal;\n" +                 // Normaller
                    "in vec2 vTexture;\n" +                // Texture koordinatları

                    "uniform mat4 uModelMatrix;\n" +       // Model matrisi
                    "uniform mat4 uViewMatrix;\n" +        // Görünüm matrisi
                    "uniform mat4 uProjMatrix;\n" +        // Projeksiyon matrisi

                    "out vec3 currentPos;\n" +             // Fragment shader’a aktarılacak pozisyon
                    "out vec3 currentNormal;\n" +          // Normal bilgisi
                    "out vec2 currentTexCoord;\n" +        // Texture bilgisi

                    "void main() {\n" +
                    "   gl_Position = uProjMatrix * uViewMatrix * uModelMatrix * vec4(vPosition, 1.0f);\n" +
                    "   currentPos = mat3(uModelMatrix) * vPosition;\n" +
                    "   currentNormal = mat3(uModelMatrix) * vNormal;\n" +
                    "   currentTexCoord = vec2(vTexture.x, vTexture.y);\n" +
                    "}\n";


    // === Fragment Shaders ===

    // Sadece texture görüntüsünü gösteren temel fragment shader
    public static final String fragmentShaderCode11 =
            "#version 300 es\n" +
                    "precision mediump float;\n" +          // Orta hassasiyet

                    "in vec2 currentTexCoord;\n" +          // Texture koordinatları
                    "uniform sampler2D vTextureSample;\n" + // Texture örnekleyicisi
                    "out vec4 resultColor;\n" +             // Çıktı rengi

                    "void main() {\n" +
                    "    resultColor = texture(vTextureSample, currentTexCoord);\n" +
                    "}\n";

    // Ambient ve diffuse bileşenlerini içeren gelişmiş fragment shader
    public static final String fragmentShaderCode13 =
            "#version 300 es\n" +
                    "precision mediump float;\n" +          // Orta hassasiyet

                    "in vec3 currentPos;\n" +               // Piksel pozisyonu
                    "in vec3 currentNormal;\n" +            // Piksel normali
                    "in vec2 currentTexCoord;\n" +          // Texture koordinatı

                    "uniform vec3 vColor;\n" +              // Nesne rengi
                    "uniform vec3 vLightColor;\n" +         // Işık rengi
                    "uniform vec3 vLightPos;\n" +           // Işık konumu

                    "uniform sampler2D vTextureSample;\n" + // Texture örnekleyicisi

                    "out vec4 resultColor;\n" +             // Çıktı rengi

                    "void main() {\n" +
                    "    vec3 norm = normalize(currentNormal);\n" +    // Normali normalize et
                    "    vec3 lightDir = normalize(vLightPos - currentPos);\n" + // Işık yönü
                    "    float diffuse = max(dot(norm, lightDir), 0.0);\n" +     // Diffuse hesaplama
                    "    vec3 vClr = 0.95f*vColor + 0.0f*diffuse*vLightColor*vColor;\n" + // Işık etkisiyle renk
                    "    resultColor = texture(vTextureSample, currentTexCoord) * vec4(vClr, 1.0f);\n" +
                    "}\n";
}
