# SmartHomePlatform
第二次尝试写智能家居平台

该平台通过单一数据库管理用户及厂商。
厂商仅需编写其对应操作的包，即可完成设备接入。

# 厂商需编写的方法(参考GMCompany中GMCompanyDeviceHelper)
1.添加设备到厂商的服务器;

2.根据设备工程和种类获取acts字符和初始化值;

3.创建包含acts内容的BaseDevice;

4.创建一个service，在后台获取设备更新信息，并广播出来。

5.设备的更新、设备的添加；
