package sgtmelon.scriptum.cleanup.basic.exception

@Deprecated("Using of this theme is deprecated")
class ThemeException : TypeCastException(DESCRIPTION) {

    companion object {
        private const val DESCRIPTION = "Wrong theme :("
    }
}