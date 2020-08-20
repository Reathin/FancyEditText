package com.rair.fancyedittext

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

/*
     *以下通过反射获取光标cursor的坐标。
     * 首先观察到TextView的invalidateCursorPath()方法，它是光标闪动时重绘的方法。
     * 方法的最后有个invalidate(bounds.left + horizontalPadding, bounds.top + verticalPadding,
                 bounds.right + horizontalPadding, bounds.bottom + verticalPadding);
     *即光标重绘的区域，由此可得到光标的坐标
     * 具体的坐标在TextView.mEditor.mCursorDrawable里，获得Drawable之后用getBounds()得到Rect。
     * 之后还要获得偏移量修正，通过以下三个方法获得：
     * getVerticalOffset(),getCompoundPaddingLeft(),getExtendedPaddingTop()。
     *
    */
//        int xOffset = 0;
//        int yOffset = 0;
//        Class<?> clazz = EditText.class.getSuperclass();
//        clazz = clazz.getSuperclass();
//        try {
//            Field editor = clazz.getDeclaredField("mEditor");
//            editor.setAccessible(true);
//            Object mEditor = editor.get(this);
//            Class<?> editorClazz = Class.forName("android.widget.Editor");
//            Field drawables = editorClazz.getDeclaredField("mCursorDrawable");
//            drawables.setAccessible(true);
//            Drawable[] drawable = (Drawable[]) drawables.get(mEditor);
//
//            Method getVerticalOffset = clazz.getDeclaredMethod("getVerticalOffset", boolean.class);
//            Method getCompoundPaddingLeft = clazz.getDeclaredMethod("getCompoundPaddingLeft");
//            Method getExtendedPaddingTop = clazz.getDeclaredMethod("getExtendedPaddingTop");
//            getVerticalOffset.setAccessible(true);
//            getCompoundPaddingLeft.setAccessible(true);
//            getExtendedPaddingTop.setAccessible(true);
//            if (drawable != null) {
//                Rect bounds = drawable[0].getBounds();
//                Log.d(TAG, bounds.toString());
//                xOffset = (int) getCompoundPaddingLeft.invoke(this) + bounds.left;
//            }
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        float x = this.getX() + xOffset;
//        float y = this.getY();
//
//        return new float[]{x, y};