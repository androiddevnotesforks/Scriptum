package sgtmelon.scriptum.app.screen.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import sgtmelon.scriptum.R

/**
 * Родительская activity приложения, от которой нужно наследоваться при необходимости обработки темы
 *
 * @author SerjantArbuz
 */
abstract class AppActivity : AppCompatActivity(), AppCallback{

    private val viewModel: AppViewModel by lazy {
        ViewModelProviders.of(this).get(AppViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()

        checkThemeChange()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.apply { callback = this@AppActivity }.onSetupTheme()
    }

    fun checkThemeChange() {
        if (!viewModel.isThemeChange()) return

        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        startActivity(intent)
    }

}