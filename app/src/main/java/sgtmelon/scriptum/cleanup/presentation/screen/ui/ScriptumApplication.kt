package sgtmelon.scriptum.cleanup.presentation.screen.ui

import android.app.Application
import android.content.Context
import sgtmelon.scriptum.cleanup.dagger.component.DaggerScriptumComponent
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.extension.initLazy
import sgtmelon.scriptum.cleanup.presentation.service.EternalService
import timber.log.Timber

/**
 * Guideline for maintain project:
 *
 * ---------------------------------------------
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

        EternalService.start(context = this)
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        /** Variable for detect test running. */
        var isTesting = false
        var skipAnimation = false

        lateinit var component: ScriptumComponent
    }
}