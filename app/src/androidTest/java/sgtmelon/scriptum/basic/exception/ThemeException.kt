package sgtmelon.scriptum.basic.exception

class ThemeException : TypeCastException(DESCRIPTION) {

    companion object {
        private const val DESCRIPTION = "Wrong theme :("
    }
}