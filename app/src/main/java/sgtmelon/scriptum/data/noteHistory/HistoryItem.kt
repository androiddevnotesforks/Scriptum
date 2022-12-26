package sgtmelon.scriptum.data.noteHistory

/**
 * Model for [NoteHistoryImpl]
 */
data class HistoryItem(
    /*@HistoryAction*/ val tag: Int,
                       private val valueFrom: String,
                       private val valueTo: String,
                       val cursor: Cursor? = ND_CURSOR,
                       val p: Int = ND_POSITION
) {
    //
    //    init {
    //        if (tag == HistoryAction.NAME || tag == HistoryAction.TEXT_CHANGE || tag == HistoryAction.ROLL_CHANGE) {
    //            if (cursor == null) {
    //                throw NullPointerException(HistoryItem::class.java.simpleName + "#cursor is null")
    //            }
    //        }
    //    }

    operator fun get(isUndo: Boolean) = if (isUndo) valueFrom else valueTo

    override fun toString(): String {
        val stringPosition = if (p != -1) " | p = $p" else ""

        val stringValueFrom = "from = " + if (valueFrom != "") valueFrom else "empty"
        val stringCursorFrom = cursor?.valueFrom?.toString() ?: "(null)"

        val stringValueTo = "to = " + if (valueTo != "") valueTo else "empty"
        val stringCursorTo = cursor?.valueTo?.toString() ?: "(null)"

        return tag.toString() + stringPosition +
                " | " + stringValueFrom + " / " + stringCursorFrom +
                " | " + stringValueTo + " / " + stringCursorTo
    }

    /**
     * Model for save text cursor inside [HistoryItem]
     */
    data class Cursor(val valueFrom: Int, val valueTo: Int) {

        companion object {
            operator fun Cursor?.get(isUndo: Boolean) = if (this != null) {
                if (isUndo) valueFrom else valueTo
            } else ND_VALUE

            const val ND_VALUE = 0
        }

        override fun toString() = "valueFrom = $valueFrom | valueTo = $valueTo"

    }

    companion object {
        val ND_CURSOR = null
        const val ND_POSITION = -1
    }

}