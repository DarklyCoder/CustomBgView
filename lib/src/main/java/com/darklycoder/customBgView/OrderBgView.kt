package com.darklycoder.customBgView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * @author DarklyCoder
 * @Description 订单背景
 * @date 2018/8/3
 */
class OrderBgView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    private val paintBg: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintTop: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintBottom: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintDash: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintCircle: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintShadow: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectBg: RectF = RectF()
    private val rectTop: RectF = RectF()
    private val rectBottom: RectF = RectF()
    private val rectShadow: RectF = RectF()
    private var bottomHeight: Float = 50f
    private var radius: Float = 10f
    private var radiusBottom: Float = 8f
    private var lineHeight: Float = 8f
    private var shadowHeight: Float = 8f
    private var mDashPath = Path()
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    private val rectArc: RectF = RectF()
    private var shader: LinearGradient? = null

    init {
        lineHeight = dp2px(context, 4)
        radius = dp2px(context, 10)
        radiusBottom = dp2px(context, 8)
        bottomHeight = dp2px(context, 50)
        shadowHeight = dp2px(context, 8)

        paintBg.color = Color.WHITE
        paintBg.style = Paint.Style.FILL

        paintTop.color = Color.parseColor("#FEDE73")
        paintTop.style = Paint.Style.STROKE
        paintTop.strokeWidth = lineHeight

        paintBottom.color = Color.parseColor("#F3F3F3")
        paintBottom.style = Paint.Style.FILL

        paintDash.color = Color.GRAY
        paintDash.style = Paint.Style.STROKE
        paintDash.strokeWidth = dp2px(context, 1)
        paintDash.pathEffect = DashPathEffect(floatArrayOf(lineHeight, lineHeight), 0f)

        paintCircle.color = Color.WHITE
        paintCircle.style = Paint.Style.FILL

        paintShadow.style = Paint.Style.FILL
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (width > 0 && height > 0) {
            val sc = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)

            //1、绘制头部圈
            rectTop.set(lineHeight / 2, lineHeight / 2, width - lineHeight / 2, radius * 2 - lineHeight / 2)
            canvas.drawRoundRect(rectTop, radius / 1.5f, radius / 1.5f, paintTop)

            //2、绘制白色背景
            rectBg.set(radius, lineHeight, width - radius, height - bottomHeight)
            canvas.drawRect(rectBg, paintBg)

            //3、绘制底部
            rectBottom.set(radius, height - bottomHeight, width - radius, height.toFloat())
            canvas.drawRoundRect(rectBottom, radiusBottom, radiusBottom, paintBottom)

            //4、绘制阴影
            rectShadow.set(radius, lineHeight, width - radius, lineHeight + shadowHeight)
            if (null == shader) {
                shader = LinearGradient(
                    0f, rectShadow.top, 0f, rectShadow.bottom,
                    intArrayOf(Color.parseColor("#AAE2D39E"), Color.parseColor("#00E2D39E")),
                    null,
                    Shader.TileMode.CLAMP
                )

                paintShadow.shader = shader
            }
            canvas.drawRect(rectShadow, paintShadow)

            //5、绘制虚线
            mDashPath.reset()
            mDashPath.moveTo(radius * 2, height - bottomHeight)
            mDashPath.lineTo(width - radius * 2, height - bottomHeight)
            canvas.drawPath(mDashPath, paintDash)

            //6、绘制下方圆孔
            paintCircle.xfermode = xfermode

            rectArc.set(0f, height - bottomHeight - radius, radius * 2, height - bottomHeight + radius)
            canvas.drawArc(rectArc, 270f, 180f, false, paintCircle)

            rectArc.set(
                width - radius * 2,
                height - bottomHeight - radius,
                width.toFloat(),
                height - bottomHeight + radius
            )
            canvas.drawArc(rectArc, 90f, 180f, false, paintCircle)

            //7、绘制上方圆孔
            rectArc.set(0f, bottomHeight - radius, radius * 2, bottomHeight + radius)
            canvas.drawArc(rectArc, 270f, 180f, false, paintCircle)

            rectArc.set(width - radius * 2, bottomHeight - radius, width.toFloat(), bottomHeight + radius)
            canvas.drawArc(rectArc, 90f, 180f, false, paintCircle)

            paintCircle.xfermode = null

            canvas.restoreToCount(sc)
        }

    }

    private fun dp2px(context: Context?, dpValue: Int): Float {
        if (null == context || dpValue == 0) {
            return dpValue.toFloat()
        }

        val scale = context.resources.displayMetrics.density
        return dpValue * scale + 0.5f
    }

}