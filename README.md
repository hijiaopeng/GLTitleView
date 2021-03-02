# GLTitleView
 自定义标题栏

| 属性                  | 描述                     |
| --------------------- | ------------------------ |
| startImgResource      | 左边图片资源             |
| startImgBounds        | 左边图片的大小           |
| startDrawablePadding  | 左边图片和左边文字的距离 |
| endImgResource        | 右边图片资源             |
| endImgBounds          | 右边图片的大小           |
| endDrawablePadding    | 右边图片和左边文字的距离 |
| startTextString       | 左边文字                 |
| startTextColor        | 左边文字颜色             |
| startTextSize         | 左边文字字体大小         |
| startTextBold         | 左边文字是否加粗         |
| startTextPadding      | 左边到边框距离           |
| centerTextString      | 中间文字                 |
| centerTextColor       | 中间文字颜色             |
| centerTextSize        | 中间文字字体大小         |
| centerTextBold        | 中间文字是否加粗         |
| endTextString         | 右边文字                 |
| endTextColor          | 右边文字颜色             |
| endTextSize           | 右边文字字体大小         |
| endTextBold           | 右边文字是否加粗         |
| endTextPadding        | 右边到边框距离           |
| colorStyle            | 选取自带样式             |
| titleHeight           | 标题栏高度               |
| titleBackgroundColor  | 标题栏背景颜色           |
| customizeLayout       | 自定义布局               |
| isShowBottomLine      | 是否绘制底部线           |
| bottomLineColor       | 绘制底部线颜色           |
| bottomLineHeight      | 绘制底部线高度           |
| bottomLineToBothSides | 绘制底部线到两边的距离   |
|                       |                          |

使用：

直接在xml中调用：

```java
<com.jpeng.customdemo.GLTitleView
    android:id="@+id/glTitle"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
   	.../>
```

调用左侧View的点击事件：

```java
glTitle?.startViewClickListener = {}
```

调用右侧View的点击事件：

```java
glTitle?.endViewClickListener = {}
```

设置沉浸式：需要自己设置状态栏透明

```java
glTitle?.setImmersive(true)
```

设置是否显示底部边线：

```java
glTitle?.isShowBottomLine = true ｜ false
```

其他：

可以使用ActivityContainer控制器来管理Activity，这样就能在Application的onCreate中设置：GLTitleView.initDefaultStartViewClickListener {}，左侧View的全局点击事件。主要用来解决每个页面都要写返回键的冗余。



可以通过设置customizeLayout属性，设置自定义的布局，然后通过

```java
glTitle?.customizeView?.findViewById
```

来获取对应的View。



colorStyle，是我们项目中的两种样式，white和blue，即白色背景和蓝色背景。



ActivityContainer的使用：

在baseActivity中的onCreate中添加：

```java
ActivityContainer.instance.addActivity(this)
```

在onDestroy中添加：

```java
ActivityContainer.instance.removeActivity(this)
```

