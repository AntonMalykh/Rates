package io.malykh.anton.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.util.TypedValue

@ColorInt
internal fun getColorByAttrId(context: Context, @AttrRes colorAttrId: Int): Int {
    val typedValue = TypedValue()
    context.theme.resolveAttribute(colorAttrId, typedValue, true)
    return try {
        ContextCompat.getColor(context, typedValue.resourceId)
    } catch (ex: Resources.NotFoundException) {
        Color.WHITE
    }
}