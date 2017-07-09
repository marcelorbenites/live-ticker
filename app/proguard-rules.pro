# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/marcelobenites/Library/Android/sdk/tools/proguard/proguard-android.txt
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

## For all libraries that have Generics and Annotations
-keepattributes Signature, *Annotation*, Exceptions

# Okio
-dontwarn okio.**

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Butter Knife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# Picasso
-dontwarn com.squareup.okhttp.**

# Retrofit
-dontwarn retrofit.**
-keep class retrofit.** { *; }

# Jackson
-keepattributes EnclosingMethod

-keep class org.codehaus.** { *; }
-keep class com.fasterxml.jackson.annotation.** { *; }
-dontwarn com.fasterxml.jackson.databind.ext.DOMSerializer

-keep class LiveTickerEntry { *; }

-keep class Match { *; }

-keep class Team { * ;}

# RxJava
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
   long producerNode;
   long consumerNode;
}
-dontwarn sun.misc.**

# Timber
-dontwarn org.jetbrains.annotations.**
