包含登录前涉及到的相关页面，具体为：
WelcomeActivity 欢迎页面。打开软件的第一个页面。
IntroductionActivity 引导页面。如果第一次启动软件，会从欢迎页面跳转到引导页面。
LoginActivity 登录页面。未登录时会由引导页面（第一次启动）或欢迎页面（非第一次启动）跳转至登录页面。
RegisterActivity 注册页面。
RegProtoActivity 注册协议页面。
ForgetPwActivity 忘记密码页面。
ChoosePayActivity 选择支付方式页面。包含扫码支付和微信支付两种支付方式。微信支付需要release签名才可以支付成功。
SweepActivity 二维码支付扫描页面。扫描指定的二维码就可以完成验证，使用相应的功能。
PayActivity 支付页面。微信支付确认页面。
PayResultActivity 支付结果页面。支付完成后的跳转页面。
