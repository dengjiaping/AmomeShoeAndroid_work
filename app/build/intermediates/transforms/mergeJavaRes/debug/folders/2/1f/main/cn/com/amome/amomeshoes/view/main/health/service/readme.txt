1、ServiceFragment：日常自诊页面。对应的布局文件fragment_service_main.xml。
该页面包含四个按钮，分别进入量一量（RulerFootRightActivity）、看一看（LookMainActivity）、我的鞋（MainShoesBoxActivity）、爱心助手（AssistMainActivity）。
2、ruler包含量一量相关页面。主要实现测量左右脚脚长脚宽。
3、look包含看一看相关页面。主要实现添加脚大小、脚肤色、脚印、脚型、脚疾、脚痛等信息，并提供预览。
4、shoebox包含我的鞋相关页面。主要实现添加、预览、删除鞋功能。根据添加的鞋的各个参数，生成相应的饼状图。
5、assistant包含爱心助手相关页面。主要实现为不同角色设置看一看和量一量信息。