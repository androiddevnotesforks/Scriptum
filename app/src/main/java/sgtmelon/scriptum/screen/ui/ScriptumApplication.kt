package sgtmelon.scriptum.screen.ui

import android.app.Application
import android.content.Context
import sgtmelon.scriptum.dagger.component.DaggerScriptumComponent
import sgtmelon.scriptum.dagger.component.ScriptumComponent
import sgtmelon.scriptum.extension.initLazy

/**
 * Guideline for maintain project:
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