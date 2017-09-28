1、AppSettingActivity 为设置主页面。该页面包含软件设置（帐号管理、清理缓存），智能鞋设置（绑定智能鞋、我的智能鞋、重启智能鞋、恢复出厂设置、固件升级、电量检测），关于魔秘（软件升级、联系我们、用户帮助）。其中重启智能鞋，恢复出厂设置，固件升级，电量检测使用时，如果智能鞋未连接会先连接。连接成功后，会执行相应的功能。
2、AccountManagemntActivity 帐号管理页面，可以进行修改密码，忘记密码，退出魔秘的操作。其中退出魔秘要清除用户相关信息，如果智能鞋处于连接状态，需要断开连接。
3、ChangPwActivity 修改密码页面。
4、ContactUsActivity 联系我们页面。
5、JDWebviewActivity 京东众筹页面。
6、MyShoesActivity 我的智能鞋页面。展示用户添加的所有智能鞋。可以进行多个智能鞋之间的切换。切换后，需要断开当前蓝牙连接。点击底部的添加智能鞋会进入绑定页面（BindActivity）。
7、UpgradeAmomeActivity 软件升级页面。点击按钮进行版本检测，没有新版本给出相应提示，有新版本提示是否下载，确认后开始下载和安装。
8、UserHelpActivity 用户帮助页面。
9、WeiboWebviewActivity 微博页面。

注意：版本发布前需要屏蔽activity_setting_main.xml中的rl_setting_more_shoes控件。