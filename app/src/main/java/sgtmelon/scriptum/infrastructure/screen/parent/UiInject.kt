package sgtmelon.scriptum.infrastructure.screen.parent

import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.presentation.screen.ScriptumApplication

/**
 * Needed for access the inject function and fastly implement it.
 */
interface UiInject {

    fun inject() = inject(ScriptumApplication.component)

    fun inject(component: ScriptumComponent)

}