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

# 所以使用proguard时，我们需要有个配置文件告诉proguard 那些java 元素是不能混淆的。----------

# proguard 配置最常用的配置选项

# -dontwarn 缺省proguard 会检查每一个引用是否正确，但是第三方库里面往往有些不会用到的类，没有正确引用。如果不配置的话，系统就会报错。

# -keep 指定的类和类成员被保留作为入口 。

# -keepclassmembers 指定的类成员被保留。

# -keepclasseswithmembers 指定的类和类成员被保留，假如指定的类成员存在的话。


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
-dontwarn org.codehaus.**
-dontwarn java.nio.**
-dontwarn java.lang.invoke.**

# ---------- 保持代码 --------------


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
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

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
-dontwarn retrofit2.converter.gson.**
-dontwarn retrofit2.converter.scalars.**
-dontwarn retrofit2.adapter.rxjava2.**

# OKHttp3拦截器
-dontwarn okhttp3.logging.**

# easypermissions ----
-keepclassmembers class * {
    @pub.devrel.easypermissions.AfterPermissionGranted <methods>;
}

# PermissionsDispatcher ----
-dontwarn permissions.dispatcher.**
-keep public class permissions.dispatcher.**{*;}

# Gson混淆脚本 ----
-keep class com.google.gson.stream.**{*;}
-keep class com.youyou.uuelectric.renter.Network.user.**{*;}

# Bugly ----
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

# Loggger ----
-dontwarn com.orhanobut.logger.**
-keep public class com.orhanobut.logger.**{*;}

# reactivex.rxjava2:rxandroid ----
-dontwarn io.reactivex.android.**
-dontwarn io.reactivex.**
-keep public class io.reactivex.android.**{*;}
-keep public class io.reactivex.**{*;}

# rxjava ----
-dontwarn io.reactivex.**
-keep public class io.reactivex.**{*;}

# rxlifecycle2 ----
-dontwarn com.trello.rxlifecycle2.**
-dontwarn com.trello.rxlifecycle2.android.**
-dontwarn com.trello.rxlifecycle2.components.**
-keep public class com.trello.rxlifecycle2.**{*;}
-keep public class com.trello.rxlifecycle2.android.**{*;}
-keep public class com.trello.rxlifecycle2.components.**{*;}

# facebook.stetho 查看数据库 拦截网络请求 ----
-dontwarn com.facebook.stetho.**
-keep public class com.facebook.stetho.**{*;}

# 格式化拼音 ----
-dontwarn net.sourceforge.pinyin4j.**
-dontwarn com.hp.hpl.sparta.**
-keep public class net.sourceforge.pinyin4j.**{*;}
-keep public class com.hp.hpl.sparta.**{*;}



