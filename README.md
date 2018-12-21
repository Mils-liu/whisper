# whisper

Bmob提供的免费后台让我能够去写这一个希望已久的社交APP，但是。。。大概是免费的原因，Bmob经常间隙性报错，时灵时不灵，总能报出让我难以理解的内部错误，还时而倒序时而正序地给我显示数据。。。给我感觉是我不是在写代码，而是在受Bmob的折磨，最后果断放弃。但总得来说有这么一个免费的后台对于尝试编写社交APP的我来说已经足够了。

在这期间，我第一次对Activity，Fragment，DiaglogFragment进行封装，学会了Butterknife，多种item的Adapter等等。我试着用MVP的模式敲这个APP，想想也是脑残。。。
# 轻语
(https://upload-images.jianshu.io/upload_images/7019098-d56f5797ed00cf3f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
### 1.注册与登录
首先是注册用户，Bmob其实提供了邮箱注册与手机注册，但由于我没注意到免费的用户只能用15条短信，导致我在测试的时候一不小心用多了只能改成最基本的用户名和密码注册，没有短信，连忘了密码这个功能都放弃了。
![注册](https://upload-images.jianshu.io/upload_images/7019098-d4e9b1b0f8563f3d.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![登录](https://upload-images.jianshu.io/upload_images/7019098-0b2f9f43b12a5302.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 2.主页
主页主要分三部分，用户界面、推荐文字和动态界面。
![用户界面](https://upload-images.jianshu.io/upload_images/7019098-4fb4fd0c7689ed71.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![推荐文字](https://upload-images.jianshu.io/upload_images/7019098-f00ed9fb635c78d5.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![动态界面](https://upload-images.jianshu.io/upload_images/7019098-77d6eefb840c0870.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##### 2.1用户界面
用户界面可以创建文字，查看和编辑用户信息，以及查看自己的和收藏的文字列表。
![创建文字](https://upload-images.jianshu.io/upload_images/7019098-53dc3b78e8c66e4d.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![账户设置](https://upload-images.jianshu.io/upload_images/7019098-74939ecd407f38c1.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![我的文字](https://upload-images.jianshu.io/upload_images/7019098-8165bf4927d91b0e.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![我的最爱](https://upload-images.jianshu.io/upload_images/7019098-d2bd76d07ba6e3c1.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![粉丝列表](https://upload-images.jianshu.io/upload_images/7019098-0c1ca71f30b3ba4a.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![关注列表](https://upload-images.jianshu.io/upload_images/7019098-13147123127589df.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##### 2.2推荐文字
推荐文字界面通过轮播图搭配cardview列表显示推荐的文字。还能搜索文字和用户。

![搜索文字](https://upload-images.jianshu.io/upload_images/7019098-90f69ad6ec92e7c2.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![搜索用户](https://upload-images.jianshu.io/upload_images/7019098-9e087cd07b63d56a.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##### 2.3动态界面
动态界面主要显示自己关注的用户的最新动态
![动态界面](https://upload-images.jianshu.io/upload_images/7019098-77d6eefb840c0870.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 3.文字
文字界面有点模仿BiliBili的专栏界面，可收藏、点赞文字。
![文字](https://upload-images.jianshu.io/upload_images/7019098-00637439c19da467.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

# 结语
这个APP其实在我上个学期就开始着手，当时已经完成了一个音乐播放器，但由于代码的不规范导致我想往音乐播放器里添加新功能时却困难重重。而这一次的轻语APP，我开始封装自己的Activity、Fragment、DialogFragment，学习MVP开发模式，用Butterkinfe绑定控件，并尽量自己用代码画图做控件背景，算是学到了许多。但由于期末考以及暑假到导师实验室敲JavaWeb(┬＿┬)，轻语在这个学期才得以完成。但Bmob的无故报错和生病，让我只有纠缠于到底是自己的错还是Bmob的错的痛苦中，完全没有学习到新知识的喜悦。所以就。。。放弃了，嗯。

# END
简书地址：https://www.jianshu.com/p/3a5761a0cf4d
