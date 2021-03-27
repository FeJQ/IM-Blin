#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_fejq_blin_MainActivity_stringFromJNI(JNIEnv *env, jobject /* this */)
{

    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jint JNICALL
Java_com_fejq_blin_MainActivity_add(JNIEnv *env, jobject jobj,jint a,jint b)
{
    return a+b;
}