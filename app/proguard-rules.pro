# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Himanshu\ToolBox\Android\android-sdk-windows/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

####### FRESCO PROGUARD #########
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-keep class com.sql.**{ *; }

####### RETROFIT 2  and JACKSON CONVERTER ##########
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions


-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry
-dontwarn okio.**
-dontwarn com.fasterxml.jackson.databind.**

-keepnames class com.fasterxml.jackson.** {
*;
}
-keepnames interface com.fasterxml.jackson.** {
    *;
}

-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.facebook.infer.**
# Works around a bug in the animated GIF module which will be fixed in 0.12.0
-keep class com.facebook.imagepipeline.animated.factory.AnimatedFactoryImpl {
    public AnimatedFactoryImpl(com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory,com.facebook.imagepipeline.core.ExecutorSupplier);
}


########## LEAK CANARY PROGUARD ##############
-dontwarn com.squareup.haha.guava.**
-dontwarn com.squareup.haha.perflib.**
-dontwarn com.squareup.haha.trove.**

-dontwarn com.squareup.leakcanary.**
-keep class com.squareup.haha.** { *; }
-keep class com.squareup.leakcanary.** { *; }

# Marshmallow removed Notification.setLatestEventInfo()
-dontwarn android.app.Notification
