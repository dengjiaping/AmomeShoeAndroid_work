包含实现相关功能的所有工具类。
比较重要的有
BleDev 蓝牙设备类
BleShoes 智能鞋类
BleShoesState 蓝牙连接状态
CalDetection 智能检测模块所需算法
Constants 常量类
DataCleanManager：本应用数据清除管理器
DateUtils：时间管理器，时间转换时间戳，时间戳转换时间等
DialogUtil：等待对话框
Environments：设备管理器，获取当前网络连接状态、获取当前版本号code码、获取当前版本号、检测SD卡是否有效、获取SD卡根路径、获取SD卡路径(应用卸载时此路径下文件也被删除)、获取缓存地址、获取图片存储地址、获取缓存图片存储地址、安装文件
ImageAbsolutePathTransfrom：根据uri获取图片绝对路径，解决Android4.4以上版本Uri转换
ImageUtil：图片管理器
ScrollerProxy：zxing二维码扫描使用到的
Sec：各种加密
SpfUtil：SharedPreferences保存数据类
T：toast弹窗类
Util：工具类。根据手机的分辨率从 dp 的单位 转成为 px(像素)。将sp值转换为px值，保证文字大小不变。 验证手机号
其他工具类见具体文件的注释
