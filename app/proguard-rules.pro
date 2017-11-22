# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\soft\android\Android-SDK-Tools/tools/proguard/proguard-android.txt
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
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
-renamesourcefileattribute SourceFile #保持log 错误提示 行号
-printmapping build/outputs/mapping/release/mapping.txt #生成mapping文件
#-keepattributes SourceFile,LineNumberTable
#-keepattributes Signature #过滤泛型，防止类型转换错误。
#-keepattributes *Annotation*  #保护注解
#-ignorewarning #忽略警告

-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合 包名不混合大小写
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

-keep public class * extends android.app.Fragment      # 保持哪些类不被混淆
-keep public class * extends android.support.v4.app.Fragment  #如果有引用v4包添加本行
-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆

-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}
#-keepnames class * implements java.io.Serializable #保持 Serializable 不被混淆
-keepclassmembers class * implements java.io.Serializable {#保持 Serializable 不被混淆并且enum 类也不被混淆
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}
-keepclassmembers class * {
    public <init> (org.json.JSONObject);
}
-keepclassmembers class **.R$* { #不混淆资源类
    public static <fields>;
}
-keepclassmembers class **.R$* { #不混淆资源类
    public static final int *;
}

#####混淆保护自己项目的部分代码以及引用的第三方jar包library#######

-dontwarn android.support.** #如果引用了v4或者v7包
-keep class android.support.**{ *; }


-dontwarn com.squareup.okhttp.**

#很特殊(压制警告)
-dontwarn org.apache.http.**

#自己包内的
-keep class cn.com.amome.amomeshoes.model.**
-keep class cn.com.amome.amomeshoes.widget.**


##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

##---------------End: proguard configuration for Gson  ----------

####混淆保护自己项目的部分代码以及引用的第三方jar包library-end###

#原本就有的部分
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.**
-keep class * extends  java.lang.Runnable{*;}
-keep public class com.android.vending.licensing.ILicensingService

-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.uuhelper.Application.** { *; }
-keep class net.sourceforge.zbar.** { *; }
-keep class com.google.android.gms.** { *; }

-keep class android.view.animation.** { *; }
-keep class cn.com.amome.amomeshoes.model.**
-keep class cn.com.amome.amomeshoes.widget.**

-keep class com.android.vending.licensing.ILicensingService
-keep class android.support.v4.** { *; }
-keep class org.apache.commons.net.** { *; }
-keep class com.tencent.** { *; }

 -keep class com.github.mikephil.**{*;}
-keep class com.umeng.** { *; }
-keep class com.umeng.analytics.** { *; }
-keep class com.umeng.common.** { *; }
-keep class com.umeng.newxp.** { *; }

-dontwarn android.support.v4.**
-dontwarn org.apache.commons.net.**
-dontwarn com.tencent.**

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

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

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}
-keepclassmembers class **.R$* {
    public static <fields>;
}
# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}


# Gson specific classes
-dontwarn com.google.gson.**
-dontobfuscate
-dontoptimize
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.gson.** { *;}

#v4
-dontwarn android.support.v4.**
-keep public class android.support.v4.view.** { *; }
-keep public class android.support.v4.accessibilityservice.** { *; }
-keep public class android.support.v4.os.** { *; }
-keep public class android.support.v4.app.** { *; }
-keep public class android.support.v4.widget.** { *; }

#umeng
-dontwarn com.umeng.**
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class cn.com.amome.activity.R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#WEXIN
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}

-dontwarn android.**
-dontwarn com.nostra13.universalimageloader.**
-dontwarn com.loopj.android.http.**
-dontwarn org.apache.http.entity.**

# eventbus混淆
-keepclassmembers class ** {
    public void onEvent(**);
}
-keepclassmembers class ** {
public void onEventMainThread(**);
}
-keepclassmembers class ** {
public void onEventAsync(**);
}