package sgtmelon.scriptum.infrastructure.screen.parent

/**
 * Interface with common functions for setup UI screen.
 */
interface UiSetup {

    fun setupUi() {
        setupInsets()
        setupView()
        setupDialogs()
        setupObservers()
    }

    /** Setup spaces from android bars and other staff for current screen. */
    fun setupInsets() = Unit

    fun setupView() = Unit

    /**
     * If some dialogs was displayed before (e.g. before rotation change) need to check it and
     * setup all needed listeners/data
     */
    fun setupDialogs() = Unit

    fun setupObservers() = Unit

}