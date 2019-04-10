package sgtmelon.scriptum.app.screen.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.screen.callback.AppCallback
import sgtmelon.scriptum.app.screen.vm.AppViewModel

/**
 * Родительская activity приложения, от которой нужно наследоваться при необходимости обработки темы
 *
 * @author SerjantArbuz
 */
abstract class AppActivity : AppCompatActivity(), AppCallback {

    private val viewModel: AppViewModel by lazy {
        ViewModelProviders.of(this).get(AppViewModel::class.java).apply {
            callback = this@AppActivity
        }
    }

    override fun onResume() {
        super.onResume()

        checkThemeChange()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onSetupTheme()
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