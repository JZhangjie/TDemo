# gislib
* 包名：cn.jdz.glib.*
## 功能
### 定位：location

### 传感器： sensor

### 拍照、图片预览：capture

### 工具类：urils
* 网络工具类，HttpHelper。使用OkHttp3开源库。get/post/put对文件、字符串的操作。
* 文件帮助类，FileHelper。图片的压缩、文件的打包、根据path获取File、assert下文件复制、文本文件读取。

### 数据：data
* dbaccess 
    * db文件读写-SqliteManager.使用具体的格式的db文件的时候，可以继承该类。
    * excel文件读写ExcelManager
* gis WMTS图层显示-TWMTSLayer
*  键值对类-KeyValueItem 。可用于构建树形结构的节点

### 控件
* ExpandContainer一个可收起、打开的容器
* ContainerContract是MVP模式的可动态改变高度的容器