package sgtmelon.scriptum.app.view.parent

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.annot.def.ThemeDef
import sgtmelon.scriptum.office.utils.PrefUtils

abstract class BaseActivityParent : AppCompatActivity() {

    @ThemeDef private var currentTheme: Int = 0

    override fun onResume() {
        super.onResume()

        isThemeChange()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentTheme = PrefUtils(this).theme
        when (currentTheme) {
            ThemeDef.light -> setTheme(R.style.App_Light_UI)
            ThemeDef.dark -> setTheme(R.style.App_Dark_UI)
        }
    }

    fun isThemeChange() {
        if (currentTheme == PrefUtils(this).theme) return

        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        startActivity(intent)
    }

}