版本发布注意事项
1、发布渠道：
需要修改AndroidManifest.xml中的友盟配置。
其中UMENG_APPKEY的值修改为5864699e3eae255903000663，测试版本为5747b64ae0f55ad8c700055c;
其中UMENG_CHANNEL的值改为对应的渠道号，发布版本的渠道号具体如下，需要依次修改，分别打包，目前测试版本的是test。
安卓园           anzhuoyuan
安粉网           anfenwang
安贝市场         anbeishichang
应用汇           yingyonghui
360应用开放平台  360yingyong
安智市场         anzhishichang
应用宝           yingyongbao
二维码下载       erweima

2、发布URL的修改：
修改ClientConstant.java的BASE_URL为"http://www.iamome.com/AmomeWebApp/"，测试版本为"http://www.iamome.cn/AmomeWebApp/"。

3、bugly配置修改：
在AmomeApp.java修改“CrashReport.initCrashReport(getApplicationContext(), "db81603aa6", true);”的最后一个参数为false。true:实时上报崩溃日志;false:按天上报崩溃日志。

记得关闭调试绑定入口
activity_setting_main 317:gone

manifest    修改版本号   和版本名称