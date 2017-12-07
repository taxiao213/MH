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

# -------------系统类不需要混淆 --------------------------
-keep public class * extends Android.app.Fragment
-keep public class * extends Android.app.Activity
-keep public class * extends Android.app.Application
-keep public class * extends Android.app.Service
-keep public class * extends Android.content.BroadcastReceiver
-keep public class * extends Android.content.ContentProvider
-keep public class * extends Android.app.backup.BackupAgentHelper
-keep public class * extends Android.preference.Preference
-keep public class * extends Android.support.**
-keep public class com.Android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * { # 保持native方法不被混淆
    native <methods>;
}
-keepclasseswithmembernames class * { # 保持自定义控件不被混淆
    public <init>(Android.content.Context, Android.util.AttributeSet);
}
-keepclasseswithmembernames class * { # 保持自定义控件不被混淆
    public <init>(Android.content.Context, Android.util.AttributeSet, int);
}
-keepclassmembers enum * { # 保持枚举enum类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements Android.os.Parcelable { # 保持Parcelable不被混淆
  public static final Android.os.Parcelable$Creator *;
}

# --------- 忽略异常提示 --------------------
-dontwarn butterknife.internal.**
-dontwarn com.alipay.**
-dontwarn com.mikepenz.**
-dontwarn org.apache.**
-dontwarn com.amap.**
-dontwarn com.Android.volley.**
-dontwarn com.rey.**
-dontwarn com.testin.**
-dontwarn jp.wasabeef.**

# ---------- 保持代码 --------------
-keep class com.youyou.uuelectric.renter.Utils.** {*;}
-keep class it.neokree.** {*;}
-keep class org.apache.** {*;}
-keep class com.iflytek.** {*;}
-keep class com.google.protobuf.** { *; }
-keep class com.youyou.uuelectric.renter.pay.** {*;}


# butterknife混淆脚本 ----
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}

# Glide图片框架 ----
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

# eventbus避免混淆 ----
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# greendao ----
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use Rx:
-dontwarn rx.**

# retrofit网络请求 ----
-dontwarn okio.**
-dontwarn javax.annotation.**

# easypermissions ----
-keepclassmembers class * {
    @pub.devrel.easypermissions.AfterPermissionGranted <methods>;
}

# Gson混淆脚本 ----
-keep class com.google.gson.stream.** {*;}
-keep class com.youyou.uuelectric.renter.Network.user.** {*;}
