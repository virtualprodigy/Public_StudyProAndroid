# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/virtualprodigyllc/Documents/Android_Studio_SDK_FOLDER/sdk/tools/proguard/proguard-android.txt
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


#start
-keepattributes SourceFile,LineNumberTable
-printmapping build/outputs/mapping/release/mapping.txt

#Butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#Otto
-dontwarn com.squareup.okhttp.**

-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}
#achartengine
-keep public class org.achartengine.. { *; }
-keep public class org.achartengine.** { *; }

#dropbox
-dontwarn com.dropbox.core.**
-keep class com.dropbox.** {*;}
-keep public class com.dropbox.** {
  public protected *;
}

#jackson
-keepattributes *Annotation*,EnclosingMethod,Signature
-keepnames class com.fasterxml.jackson.** { *; }
 -dontwarn com.fasterxml.jackson.databind.**
 -keep class org.codehaus.** { *; }
 -keepclassmembers public final enum org.codehaus.jackson.annotate.JsonAutoDetect$Visibility {
 public static final org.codehaus.jackson.annotate.JsonAutoDetect$Visibility *; }
-keep public class your.class.** {
  public void set*(***);
  public *** get*();
}

# Gson
-keep public class com.google.gson
-keepattributes Signature
# Add any classes the interact with gson
-keep class com.virtualprodigy.LittleBlackBook.Models.** { *; }

# OrmLite uses reflection
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }

#Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

-dontwarn rx.**
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-keep class sun.misc.Unsafe { *; }
#your package path where your gson models are stored
-keep class com.example.models.** { *; }

#stetho
-dontwarn com.facebook.stetho.**
-keep class com.facebook.stetho.** { *; }

#Don't warn additions from compiling
-dontwarn com.google.android.gms.**
-dontwarn okio.**Subscribe

#in-app billing Google
-keep class com.android.vending.billing.**

#Ads
-keep class com.startapp.** {
      *;
}

-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile,LineNumberTable, *Annotation*, EnclosingMethod
-dontwarn android.webkit.JavascriptInterface
-dontwarn com.startapp.**


