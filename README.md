### FancyEditText

输入时文本飞来飞去的EditText

### 使用
### 引入
```
implementation 'com.rairmmd:fancyedittext:1.0.0'
```
### XML中添加
```xml
<com.rairmmd.fancyedittext.FancyEditText
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="请输入内容"
    android:textSize="16sp"
    app:fetAnimType="UP"
    app:fetDuration="1000"
    app:fetTextColor="@color/colorAccent"
    app:fetTextScale="2"
    app:fetTextSize="18sp" />
```
### 属性

| 属性 | 类型 | 说明 |
| --  | --  | --   |
| fetDuration | int | 动画时长 |
| fetAnimType | 枚举 | 动画类型 UP/DOWN |
| fetTextColor | color | 飞动的文本颜色 |
| fetTextScale | float | 飞动的文本放大倍数 |
| fetTextSize | dimension | 飞动的文本初始大小 |

使用同EditText


