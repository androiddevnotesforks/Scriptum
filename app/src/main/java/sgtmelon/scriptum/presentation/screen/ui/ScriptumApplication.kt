package sgtmelon.scriptum.presentation.screen.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import sgtmelon.scriptum.dagger.component.DaggerScriptumComponent
import sgtmelon.scriptum.dagger.component.ScriptumComponent
import sgtmelon.scriptum.extension.initLazy
import sgtmelon.scriptum.presentation.service.EternalService

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

        startEternalService()
    }

    private fun startEternalService() {
        Log.i("HERE", "start service")
        val intent = Intent(this, EternalService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    companion object {
        lateinit var component: ScriptumComponent
    }
}