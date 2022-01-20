# base
一些base和常用工具


在项目的根gradle新增jitpack依赖
allprojects {
    repositories {
        maven {url 'https://jitpack.io'}
    }
}

在使用的模块的gradle使用该依赖

implementation 'com.github.preampls:base:1.0'


