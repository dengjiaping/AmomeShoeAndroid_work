1、DetectionFragment：智能检测页面。对应的布局文件fragment_health_main.xml。
该页面包含四个按钮，分别进入蹲一蹲（SquatDetectionActivity）、站一站（StandDetectionActivity）、摇一摇（ShakeDetectionActivity）、走一走（WalkDetectionActivity）。
2、stand包含站一站相关页面。主要实现连接智能鞋、站一站测试到生成简略报告这个流程。
3、squat包含蹲一蹲相关页面。主要实现连接智能鞋、蹲一蹲测试到生成简略报告这个流程。
4、shake包含摇一摇相关页面。主要实现连接智能鞋、摇一摇测试到生成简略报告这个流程。
5、walk包含走一走相关页面。主要实现连接智能鞋、走一走测试到生成简略报告这个流程。
6、ReconnectionActivity：蓝牙重连页面。