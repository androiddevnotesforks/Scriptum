package sgtmelon.scriptum.app.screen.parent

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.annot.def.ThemeDef
import sgtmelon.scriptum.office.utils.PrefUtils

class ParentViewModel(application: Application): AndroidViewModel(application) {

    private val prefUtils = PrefUtils(application.applicationContext)

    lateinit var callback: ParentCallback

    @ThemeDef private var currentTheme: Int = 0

    fun onSetupTheme() {
        currentTheme = prefUtils.theme

        when (currentTheme) {
            ThemeDef.light -> callback.setTheme(R.style.App_Light_UI)
            ThemeDef.dark -> callback.setTheme(R.style.App_Dark_UI)
        }
    }

    fun isThemeChange() = currentTheme != prefUtils.theme

}