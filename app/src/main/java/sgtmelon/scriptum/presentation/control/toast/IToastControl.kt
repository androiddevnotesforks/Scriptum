package sgtmelon.scriptum.presentation.control.toast

import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Interface for [ToastControl].
 */
interface IToastControl {
    fun show(@StringRes stringId: Int, length: Int = Toast.LENGTH_SHORT)
    fun show(string: String, length: Int = Toast.LENGTH_SHORT)
}