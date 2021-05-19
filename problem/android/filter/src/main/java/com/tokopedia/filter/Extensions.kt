package com.tokopedia.filter

import android.text.SpannableStringBuilder
import android.text.style.StrikethroughSpan
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.util.*

fun Float.toRupiahCurrencyFormat(): String {
    val localeID = Locale("in", "ID")
    val format = NumberFormat.getCurrencyInstance(localeID)
    return format.format(this.toDouble())
}

fun Int.toRupiahCurrencyFormat(): String {
    val localeID = Locale("in", "ID")
    val format = NumberFormat.getCurrencyInstance(localeID)
    return format.format(this)
}

fun Int.getSlashedPrice(): SpannableStringBuilder {
    val ssb = SpannableStringBuilder(this.toRupiahCurrencyFormat())
    val span = StrikethroughSpan()
    ssb.setSpan(span, 0, ssb.length, 0)
    return ssb
}

fun AppCompatImageView.loadImageUrl(imageUrl: String) {
    Glide.with(context)
        .load(imageUrl)
        .into(this)
}