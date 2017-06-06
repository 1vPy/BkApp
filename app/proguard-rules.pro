# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in G:\AndroidSDK/tools/proguard/proguard-android.txt
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


-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontskipnonpubliclibraryclasses
-dontpreverify           # 混淆时是否做预校验
-allowaccessmodification

-keep class com.roy.bkapp.model.**{*;}

-keep class com.jude.swipbackhelper.** { *; }

-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }
-keep class cn.bmob.** { *; }
-keep class okhttp3.** { *; }
-keep class rx.** { *; }
-keep class cn.smssdk.**{*;}
-keep class com.mob.**{*;}
-keep class io.reactivex.**{*;}
-keep class retrofit2.**{*;}
-keep class com.google.**{*;}
-keep class com.bumptech.**{*;}
-keep class dagger.**{*;}
-keep class javax.**{*;}

#ignore warning
-dontwarn android.support.**
-dontwarn com.alibaba.fastjson.**
-dontwarn butterknife.internal.**
-dontwarn cn.bmob.**

-dontwarn com.mob.**
-dontwarn cn.smssdk.**
-dontwarn okio.**

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions



-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}


-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

#第三方库
-keep public class com.nineoldandroids.**{*;}
-keep public class * extends android.support.**{*;}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * {
    public <methods>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}


-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}


#保持R文件不被混淆
-keep class **.R$* {
    *;
}

#-keepnames class * implements java.io.Serializable
-keep public class * implements java.io.Serializable {
        public *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

