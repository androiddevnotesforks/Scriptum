package sgtmelon.textDotAnim

import android.text.Spannable

/**
 * Container for text [value], which may be a simple [String] or a [Spannable]. It's important
 * not to cast [value] to [String], because it may affect to [Spannable] color (reset it).
 */
@JvmInline value class DotText(val value: CharSequence)