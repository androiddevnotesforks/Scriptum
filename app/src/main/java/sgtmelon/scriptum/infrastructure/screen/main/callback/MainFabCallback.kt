package sgtmelon.scriptum.infrastructure.screen.main.callback

interface MainFabCallback {

    /**
     * When call this func without parameters - it's means just fetch FAB visibility updates.
     */
    fun changeFabVisibility(isVisible: Boolean = true, withGap: Boolean = false)
}