package sgtmelon.scriptum.cleanup.basic.exception

class NoteCastException : TypeCastException(DESCRIPTION) {

    companion object {
        private const val DESCRIPTION = "Wrong noteItem type"
    }
}