package io.malykh.anton.base

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.FILTER_BITMAP_FLAG
import android.util.AttributeSet
import android.widget.ImageView
import io.malykh.anton.presentation.R

/**
 * Image view that renders image of the circle shape.
 *
 * [R.styleable.CircleImageView_outlineWidth] can be changed to apply outline width to the circle (0 is used by default)
 */
class CircleImageView @JvmOverloads constructor(context: Context,
                                                         attrs: AttributeSet? = null,
                                                         defStyleAttr: Int = 0)
    : ImageView(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        flags = ANTI_ALIAS_FLAG or FILTER_BITMAP_FLAG
    }
    private var mask: Bitmap? = null
    private val outlineWidth: Float =
        with(getContext().obtainStyledAttributes(attrs, R.styleable.CircleImageView)){
            getDimension(R.styleable.CircleImageView_outlineWidth, 0f)
                .also {
                    recycle()
                }
        }

    override fun draw(canvas: Canvas?) {
        if (canvas == null || width == 0 || height == 0)
            return

        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val imageCanvas = Canvas(image)
        super.draw(imageCanvas)

        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val resultCanvas = Canvas(result)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        resultCanvas.drawBitmap(mask, 0f, 0f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        resultCanvas.drawBitmap(image, 0f, 0f, paint)

        if (outlineWidth > 0) {
            paint.apply {
                xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
                color = Color.GRAY
                strokeWidth = outlineWidth
                style = Paint.Style.STROKE
            }
            resultCanvas.drawCircle(
                width/2f,
                height/2f,
                Math.min(width.toFloat(), height.toFloat()) / 2 - outlineWidth / 2,
                paint)
        }

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        canvas.drawBitmap(result, 0f, 0f, paint)
        image.recycle()
        result.recycle()
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
            color = Color.TRANSPARENT
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        }

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        paint.color = Color.WHITE
        canvas.drawCircle(
            width/2f,
            height/2f,
            Math.min(width.toFloat(), height.toFloat()) / 2,
            paint)

        return mask
    }
}