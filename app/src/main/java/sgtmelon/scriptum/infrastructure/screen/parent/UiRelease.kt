package sgtmelon.scriptum.infrastructure.screen.parent

/**
 * Needed for access the release functions and for fastly implementation of it.
 */
interface UiRelease {
    fun releaseBinding() = Unit
    fun releaseSystem() = Unit
}