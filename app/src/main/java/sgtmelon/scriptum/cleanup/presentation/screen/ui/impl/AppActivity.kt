package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl

import sgtmelon.scriptum.cleanup.presentation.screen.ui.ParentActivity

/**
 * Parent activity for application, which need extends when need change theme.
 */
@Deprecated("Merge it with ParentActivity")
abstract class AppActivity : ParentActivity() {

    protected val fm get() = supportFragmentManager
}