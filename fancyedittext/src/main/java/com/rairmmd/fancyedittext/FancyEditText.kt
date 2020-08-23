package com.rairmmd.fancyedittext

import android.animation.*
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import android.widget.TextView
import kotlin.random.Random

/**
 * @author Rair
 * @date 2020/8/20
 *
 * desc:
 */
class FancyEditText(context: Context, attributeSet: AttributeSet? = null) :
    EditText(context, attributeSet) {

    constructor(context: Context) : this(context, null)

    private lateinit var mContainer: ViewGroup
    private var mHeight: Int = 0
    private var mCacheText: String = ""

    private var mTextColor: Int = Color.WHITE
    private var mTextSize: Float = 20f
    private var mTextScale: Float = 1.2f
    private var mDuration: Int = 600
    private var mAnimType: Int = 0

    private val mLayoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
    )

    init {
        if (!isInEditMode && attributeSet != null) {
            val typeArray = context.obtainStyledAttributes(attributeSet, R.styleable.FancyEditText)

            mTextColor = typeArray.getColor(R.styleable.FancyEditText_fetTextColor, mTextColor)
            mTextSize =
                typeArray.getDimension(R.styleable.FancyEditText_fetTextSize, mTextSize)
            mTextScale = typeArray.getFloat(R.styleable.FancyEditText_fetTextScale, mTextScale)
            mDuration = typeArray.getInt(R.styleable.FancyEditText_fetDuration, mDuration)
            mAnimType = typeArray.getInt(R.styleable.FancyEditText_fetAnimType, mAnimType)

            typeArray.recycle()

            mContainer = (getContext() as Activity).findViewById(android.R.id.content)
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val point = Point()
            windowManager.defaultDisplay.getSize(point)
            mHeight = point.y

            addTextWatchListener()
        }
    }

    private fun addTextWatchListener() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (mCacheText.length < s.length) {
                    val last = s[s.length - 1]
                    drawText(last, false)
                } else if (mCacheText.isNotEmpty()) {
                    val last = mCacheText[mCacheText.length - 1]
                    drawText(last, true)
                }
                mCacheText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun drawText(last: Char, isOpposite: Boolean) {
        val textView = TextView(context)
        textView.setTextColor(mTextColor)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize)
        textView.text = last.toString()
        textView.gravity = Gravity.CENTER
        mContainer.addView(textView, mLayoutParams)
        textView.measure(0, 0)

        when (mAnimType) {
            0 -> {
                playFlyUpAnimator(textView, isOpposite)
            }
            1 -> {
                playFlyDownAnimator(textView, isOpposite)
            }
        }
    }

    private fun playFlyUpAnimator(textView: TextView, opposite: Boolean) {
        val startX: Float
        val startY: Float
        val endX: Float
        val endY: Float
        val cursorPosition = getCursorPosition()
        if (opposite) {
            startX = cursorPosition[0]
            startY = cursorPosition[1]
            endX = Random.nextInt(mContainer.width).toFloat()
            endY = (mHeight / 3 * 2).toFloat()
        } else {
            startX = cursorPosition[0]
            startY = (mHeight / 3 * 2).toFloat()
            endX = startX
            endY = cursorPosition[1]
        }
        val animatorSet = AnimatorSet().setDuration(mDuration.toLong())
        animatorSet.interpolator = DecelerateInterpolator()
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                mContainer.removeView(textView)
            }
        })
        animatorSet.playTogether(
            ObjectAnimator.ofFloat(textView, View.TRANSLATION_X, startX, endX),
            ObjectAnimator.ofFloat(textView, View.TRANSLATION_Y, startY, endY),
            ObjectAnimator.ofFloat(textView, View.SCALE_X, 1f, mTextScale),
            ObjectAnimator.ofFloat(textView, View.SCALE_Y, 1f, mTextScale)
        )
        animatorSet.start()
    }

    private fun playFlyDownAnimator(textView: TextView, opposite: Boolean) {
        val startX: Float
        val startY: Float
        val endX: Float
        val endY: Float
        val cursorPosition = getCursorPosition()
        if (opposite) {
            startX = cursorPosition[0]
            startY = cursorPosition[1]
            endX = Random.nextInt(mContainer.width).toFloat()
            endY = 0f
        } else {
            startX = cursorPosition[0]
            startY = -100f
            endX = startX
            endY = cursorPosition[1]
        }
        val animatorSet = AnimatorSet().setDuration(mDuration.toLong())
        animatorSet.interpolator = DecelerateInterpolator()
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                mContainer.removeView(textView)
            }
        })
        animatorSet.playTogether(
            ObjectAnimator.ofFloat(textView, View.TRANSLATION_X, startX, endX),
            ObjectAnimator.ofFloat(textView, View.TRANSLATION_Y, startY, endY)
        )
        animatorSet.start()
    }

    private fun getCursorPosition(): FloatArray {
        var xOffset = 0F
        val position = mCacheText.length - 2
        val lineForOffset = layout.getLineForOffset(position)
        val rect = Rect()
        layout.getLineBounds(lineForOffset, rect)
        if (mCacheText.isNotEmpty()) {
            xOffset = layout.getSecondaryHorizontal(position)
        }

        return floatArrayOf(x + xOffset, y + rect.top)
    }
}