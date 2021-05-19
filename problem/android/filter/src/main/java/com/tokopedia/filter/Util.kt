package com.tokopedia.filter

import android.content.Context
import android.util.DisplayMetrics
import com.google.gson.Gson
import com.tokopedia.filter.model.ProductsDataItem
import java.io.IOException

object Util {

    fun getProductJson(ctx: Context): ProductsDataItem =
        Gson().fromJson(readFromRes(ctx) ?: "{}", ProductsDataItem::class.java)

    private fun readFromRes(ctx: Context): String? {
        return try {
            val inputStream =
                ctx.resources.openRawResource(R.raw.products)
            val bytes = ByteArray(inputStream.available())
            inputStream.read(bytes, 0, bytes.size)
            return String(bytes)
        } catch (e: IOException) {
            null
        }
    }

    fun dpToPx(dp: Int, context: Context): Int {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT).toInt()
    }
}