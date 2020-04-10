package sgtmelon.scriptum.presentation.screen.ui

import android.app.Application
import android.content.Context
import sgtmelon.iconanim.widget.SwitchButton
import sgtmelon.iconanim.widget.SwitchButtonAnim
import sgtmelon.scriptum.dagger.component.DaggerScriptumComponent
import sgtmelon.scriptum.dagger.component.ScriptumComponent
import sgtmelon.scriptum.extension.initLazy

/**
 * Guideline for maintain project:
 *
 *
 * Why need use separate animation classes like [SwitchButton]/[SwitchButtonAnim]:
 *
 * 1. Low API devices will crash if class has animation import. Because of that need exclude any
 *    declaration for low APIs.
 *
 * 2. Need create separate layouts where implement view classes for different APIs.
 *
 *
 * Need to be careful with lazy properties!
 *
 * 1. Inside fragment setup view's ONLY manually. Inside activity setup view's with lazy func.
 *    Need setup manually because after rotation lazy function will return null.
 *
 * 2. Use [initLazy] for properties which contains [Context] in constructor.
 *    Troubles happen after rotation if property wasn't initialized.
 */
class ScriptumApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        component = DaggerScriptumComponent.builder().set(application = this).build()
    }

    companion object {
        lateinit var component: ScriptumComponent
    }

}