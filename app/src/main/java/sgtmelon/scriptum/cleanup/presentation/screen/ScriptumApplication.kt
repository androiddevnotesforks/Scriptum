package sgtmelon.scriptum.cleanup.presentation.screen

import android.app.Application
import sgtmelon.scriptum.cleanup.dagger.component.DaggerScriptumComponent
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.infrastructure.service.EternalService
import timber.log.Timber

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