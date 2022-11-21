package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonBackground = 0
    private var buttonText = ""
    private var text = ""
    private var textColor = 0
    private var txtSize = 55f
    private var offset = 0
    private var txtRect = Rect()
    private var progressColor = 0
    private var buttonProgress = 0
    private var circleProgress = 0
    private var circleProgressColor = 0
    private var loadingRect = Rect()
    private var valueAnimator = ValueAnimator()

    private val attrs = context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.LoadingButton,
        defStyleAttr,
        0
    )

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = txtSize
        typeface = Typeface.DEFAULT_BOLD
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                valueAnimator = ValueAnimator.ofInt(0, 360).apply {
                    repeatMode = ValueAnimator.RESTART
                    repeatCount = ValueAnimator.INFINITE
                    duration = 2000
                    interpolator = LinearInterpolator()
                    addUpdateListener {
                        buttonProgress = it.animatedValue as Int
                        circleProgress = ((widthSize / 365) * buttonProgress)
                        invalidate()
                    }
                    addListener(object: AnimatorListenerAdapter(){
                        override fun onAnimationCancel(animation: Animator?) {
                            super.onAnimationCancel(animation)
                            buttonProgress = 0
                            circleProgress = 0
                        }
                    })
                    start()
                }
                requestLayout()
                invalidate()
            }
            ButtonState.Completed -> {
                valueAnimator.cancel()
                buttonProgress = 0
                circleProgress = 0
                invalidate()
            }
            else -> {}
        }
    }


    init {
        isClickable = true
        with(attrs){
            buttonBackground = context.getColor(R.color.colorPrimary)
            textColor = context.getColor(R.color.white)
            progressColor = context.getColor(R.color.colorPrimaryDark)
            circleProgressColor = context.getColor(R.color.colorAccent)
//            recycle()
        }
    }

    fun buttonState(buttonState: ButtonState){
        this.buttonState = buttonState
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.save()
        makeBackground(canvas)
        makeText(canvas, paint)
        if (buttonState == ButtonState.Loading) animate(canvas, paint)
        makeText(canvas, paint)
        canvas?.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    private fun makeBackground(canvas: Canvas?){
        canvas?.drawColor(buttonBackground)
    }

    private fun makeText(canvas: Canvas?, paint: Paint){
        if (buttonState == ButtonState.Loading){
            text = context.getString(R.string.button_loading)
        }else{
            text = "download"
        }

        paint.apply {
            getTextBounds(
                text,
                0,
                text.length,
                txtRect
            )
            color = context.getColor(R.color.white)
        }

        offset = if(buttonState == ButtonState.Loading) 35 else 0

        val textX = widthSize / 2f - txtRect.width() / 2f + 80
        val textY = heightSize / 2f + txtRect.height() / 2f - txtRect.bottom

        canvas?.drawText(text, textX - offset, textY, paint)
    }

    private fun animate(canvas: Canvas?, paint: Paint){
        paint.color = progressColor
        loadingRect.set(
            widthSize * buttonProgress / 360,
            0,
            widthSize,
            heightSize
        )

        canvas?.drawRect(loadingRect, paint)

        animateCircle(canvas, paint)
    }

    private fun animateCircle(canvas: Canvas?, paint: Paint) {
        paint.apply {
            style = Paint.Style.FILL
            color = circleProgressColor
        }

        val circleX = widthSize / 2f + txtRect.width() / 2f + 150
        val circleY = heightSize / 2F - 20

        val rectF = RectF(circleX,
            circleY,
            circleX + 40,
            circleY + 40)

        canvas?.drawArc(
            rectF,
            buttonProgress.toFloat(),
            360f - buttonProgress.toFloat(),
            true,
            paint
        )
    }
}