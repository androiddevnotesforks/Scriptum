package sgtmelon.scriptum.screen.ui

import android.app.Application
import sgtmelon.scriptum.dagger.component.DaggerScriptumComponent
import sgtmelon.scriptum.dagger.component.ScriptumComponent

class ScriptumApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        component = DaggerScriptumComponent.builder()
                .set(application = this)
                .build()
    }

    companion object {
        lateinit var component: ScriptumComponent
    }

}