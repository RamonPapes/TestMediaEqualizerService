#include <jni.h>
#include <string>

extern "c"
JNIEXPORT jint JNICALL
Java_com_ramonpapes_testmediaequalizerservice_AudioEqualizer_applyEqualization(
        JNIEnv *env,
        jobject this,
        jshortArray audioData,
        jintArray gains
        ){
    jshort* audioDataPtr = env->GetShortArrayElements(audioData, 0);
    jint* gainsPtr = env->GetIntArrayElements(gains, 0);
    int numSamples = env->GetArrayLength(audioData);
    int numBands = env->GetArrayLength(gains);


    //Aplicando a equalização (Exemplo simples de ganho)
    for (int i = 0; i < numSamples; i++) {
        for (int j = 0; j < numBands; j++) {
            audioDataPtr[i] *= (double)(gainsPtr[j] / 1000.0); //Ajuste o ganho
        }
    }

    env->ReleaseShortArrayElements(audioData, audioDataPtr, 0);
    env->ReleaseIntArrayElements(gains, gainsPtr, 0);

    return numSamples;
}