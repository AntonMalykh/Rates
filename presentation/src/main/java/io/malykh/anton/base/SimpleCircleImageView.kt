package io.malykh.anton.base

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.FILTER_BITMAP_FLAG
import android.util.AttributeSet
import android.widget.ImageView
import io.malykh.anton.presentation.R
import io.malykh.anton.utils.getColorByAttrId

internal class SimpleCircleImageView @JvmOverloads constructor(context: Context,
                                                               attrs: AttributeSet? = null,
                                                               defStyleAttr: Int = 0)
    : ImageView(context, attrs, defStyleAttr) {

    private val maskPaint = Paint().apply {
        flags = ANTI_ALIAS_FLAG or FILTER_BITMAP_FLAG
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
    }
    private var mask: Bitmap? = null
    private val outlineWidth: Float =
        with(getContext().obtainStyledAttributes(attrs, R.styleable.SimpleCircleImageView)){
            getDimension(R.styleable.SimpleCircleImageView_outlineWidth, 0f)
                .also {
                    recycle()
                }
        }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        onDrawForegroundCompat(canvas)
    }

    private fun onDrawForegroundCompat(canvas: Canvas?) {
        if (canvas == null || width == 0 || height == 0)
            return
        if (mask == null)
            mask = createMask(width, height)
        canvas.drawBitmap(mask!!, 0f, 0f, maskPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mask?.let {
            if (!it.isRecycled)
                it.recycle()
        }
        mask = createMask(width, height)
    }

    private fun createMask(width: Int, height: Int): Bitmap {
        val mask = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mask)

        val paint = Paint(ANTI_ALIAS_FLAG).apply {
            color = getColorByAttrId(context, android.R.attr.windowBackground)
        }

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        if (outlineWidth > 0) {
            paint.color = Color.GRAY
            canvas.drawCircle(
                width/2f,
                height/2f,
                Math.min(width.toFloat(), height.toFloat()) / 2,
                paint)
        }

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        canvas.drawCircle(
            width/2f,
            height/2f,
            Math.min(width.toFloat(), height.toFloat()) / 2 - outlineWidth,
            paint)

        return mask
    }
}