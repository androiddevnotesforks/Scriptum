package sgtmelon.scriptum.cleanup.basic.exception

class ThemeException : TypeCastException(DESCRIPTION) {

    companion object {
        private const val DESCRIPTION = "Wrong theme :("
    }
}