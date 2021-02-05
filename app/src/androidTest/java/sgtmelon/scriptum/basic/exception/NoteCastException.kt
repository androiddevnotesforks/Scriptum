package sgtmelon.scriptum.basic.exception

class NoteCastException : TypeCastException(NOTE_CAST_EXCEPTION) {

    companion object {
        const val NOTE_CAST_EXCEPTION = "Wrong noteItem type"
    }
}