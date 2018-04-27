# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
  public *;
}
################common###############
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.preference.Preference
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.annotation.**
-keep public class * extends android.support.v7.**

# 如果引用了v4或者v7包
-dontwarn android.support.**
#不要混淆Utils的所有属性与方法
-keep class net.bupt.paylibrary.utils.BuptPayManager{
    public *;
}
#==========zxing============
-keep class com.google.** {*;}
-dontwarn com.google.**
#==========================
-keep class retrofit2.** {*;}
-dontwarn retrofit2.**

-keep class okhttp3.** {*;}
-dontwarn okhttp3.**

-keep class okio.** {*;}
-dontwarn okio.**