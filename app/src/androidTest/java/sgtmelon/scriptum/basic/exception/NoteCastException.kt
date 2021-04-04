package sgtmelon.scriptum.basic.exception

class NoteCastException : TypeCastException(DESCRIPTION) {

    companion object {
        private const val DESCRIPTION = "Wrong noteItem type"
    }
}