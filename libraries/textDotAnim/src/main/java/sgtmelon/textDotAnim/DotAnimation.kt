package sgtmelon.textDotAnim

import android.content.Context
import androidx.annotation.StringRes

interface DotAnimation {
    fun start(context: Context?, @StringRes stringId: Int)
    fun stop()
}