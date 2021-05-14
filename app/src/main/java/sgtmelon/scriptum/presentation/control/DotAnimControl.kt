package sgtmelon.scriptum.presentation.control

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.annotation.Size
import androidx.annotation.StringRes
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.key.DotAnimType
import sgtmelon.scriptum.extension.addIdlingListener

/**
 * Class for help animate [TextView] ending with loading dots.
 */
class DotAnimControl(private val type: DotAnimType, private val callback: Callback) {

    private var animator: ValueAnimator? = null

    fun start(context: Context, @StringRes stringId: Int) = apply {
        val textList = when (type) {
            DotAnimType.COUNT -> getCountList(context, stringId)
            DotAnimType.SPAN -> getSpanList(context, stringId)
        }

        animator = getAnimator(context, textList)
        animator?.start()
    }

    private fun getCountList(context: Context, @StringRes stringId: Int): List<String> {
        val simpleText = context.getString(stringId)
        val dotText = context.getString(R.string.dot)

        val textList = mutableListOf<String>()
        for (i in 0 until DOT_COUNT + 1) {
            val text = StringBuilder(simpleText).apply {
                repeat(i) { append(dotText) }
            }.toString()

            textList.add(text)
        }

        return textList
    }

    private fun getSpanList(context: Context, @StringRes stringId: Int): List<SpannableString> {
        val simpleText = context.getString(stringId)
        val dotText = context.getString(R.string.dot)

        val resultText = StringBuilder(simpleText).apply {
            repeat(DOT_COUNT) { append(dotText) }
        }.toString()

        val textList = mutableListOf<SpannableString>()
        for (i in 0 until DOT_COUNT + 1) {
            val spannable = SpannableString(resultText)

            val start = resultText.length - (DOT_COUNT - i)
            val end = resultText.length
            val flag = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            spannable.setSpan(ForegroundColorSpan(Color.TRANSPARENT), start, end, flag)

            textList.add(spannable)
        }

        return textList
    }

    private fun getAnimator(
        context: Context,
        @Size(value = DOT_COUNT + 1L) list: List<CharSequence>
    ): ValueAnimator {
        val valueTo = DOT_COUNT + 1

        return ValueAnimator.ofInt(0, valueTo).apply {
            this.interpolator = LinearInterpolator()
            this.duration = context.resources.getInteger(R.integer.dots_anim_time).toLong()
            this.repeatCount = ObjectAnimator.INFINITE
            this.repeatMode = ObjectAnimator.RESTART

            addIdlingListener()
            addUpdateListener {
                val value = it.animatedValue as? Int


                /**
                 * Sometimes [ValueAnimator] give a corner value.
                 */
                if (value == null || value == valueTo) return@addUpdateListener

                val text = list.getOrNull(value)
                if (text != null) {
                    callback.onDotAnimUpdate(text)
                }
            }
        }
    }

    fun stop() {
        animator?.cancel()
        animator = null
    }

    /**
     * Inside this callback need call [TextView.setText] or something similar.
     */
    interface Callback {
        fun onDotAnimUpdate(text: CharSequence)
    }

    companion object {
        const val DOT_COUNT = 3
    }
}