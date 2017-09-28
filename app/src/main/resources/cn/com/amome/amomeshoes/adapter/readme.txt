包含项目需要的所有自定义adapter，用于实现数据在视图上的展示。自定义适配器继承自BaseAdapter。
几个重写的方法：
getCount()：返回集合或者数组的总数量
getItem(int position)：获取集合或者数组里的对应位置的对象
getItemId(int position)：获取这个Item对象在集合或数组里的下标号 
getView(int position, View convertView, ViewGroup parent)：取得当前欲显示的图像View
第一个参数position：该视图在适配器数据中的位置 
第二个参数convertView：旧视图 
第三个参数parent：此视图最终会被附加到的父级视图

1、AssistMemberListAdapter：爱心助手成员适配器，爱心助手中使用
2、BindShoeListAdapter：绑定的智能鞋适配器，我的智能鞋中使用。
3、BrandListAdapter：鞋品牌适配器，我的鞋中使用
4、CategoryShoesListAdapter：鞋种类适配器，我的智能鞋中使用。
5、EditProblemAdapter：鞋问题适配器。我的鞋添加鞋时使用。
6、FootSecretBasicAdapter：脚的秘密-基本情况适配器，脚的秘密中使用。
7、FootSecretOtherAdapter：脚的秘密-其他情况适配器，脚的秘密中使用。
8、HealthPromotionMainAdapter：健康促进主页面适配器，健康促进中使用。
9、HeelShoesListAdapter：鞋跟适配器，我的鞋中使用。
10、HobbyListAdapter：爱好适配器，个人信息中使用。
11、 JobListAdapter：职业适配器，个人信息中使用。
12、LeDeviceListAdapter：未用到。
13、LookFootcolorAdapter：脚肤色适配器，看看脚-脚色中使用。
14、LookFootdiseasAdapter：脚疾适配器，看看脚-脚疾中使用。
15、LookFootpainAdapter：脚痛适配器，看看脚-脚痛中使用。
16、LookFootprintsAdapter：脚印适配器，看看脚-脚印中使用。
17、LookFootShapAdapter：脚型适配器，看看脚-脚型中使用。
18、MainShoeAdapter：鞋种类适配器，我的鞋中使用。
19、MainViewPagerAdapter：滑动适配器，我的鞋-图表滑动，运动中使用。
20、MaterialShoesListAdapter：鞋材质适配器。未用到。
21、ProblemAdapter：鞋问题适配器。我的鞋中使用。
22、PromotionAddAdapter：健康促进主页面适配器。目前未用到。
23、ShoeDelReasonAdapter：删除鞋问题适配器。删除鞋选择问题时使用。
24、ShoeInfoProblemAdapter：鞋问题适配器。未用。
25、ShoesBoxAdapter：鞋列表适配器。我的鞋-列表中使用。
26、ShoesBoxChartsAdapter：鞋图表适配器。我的鞋-图表页面下方使用。