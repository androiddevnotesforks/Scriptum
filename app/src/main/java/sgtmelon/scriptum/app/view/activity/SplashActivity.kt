package sgtmelon.scriptum.app.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import sgtmelon.scriptum.office.annot.def.IntentDef
import sgtmelon.scriptum.office.utils.PrefUtils

class SplashActivity : AppCompatActivity() {

    companion object {
        fun getInstance(context: Context, noteId: Long): Intent {
            return Intent(context, SplashActivity::class.java)
                    .putExtra(IntentDef.STATUS_OPEN, true)
                    .putExtra(IntentDef.NOTE_ID, noteId)
        }
    }

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        if (bundle != null && bundle.getBoolean(IntentDef.STATUS_OPEN)) {
            startActivities(arrayOf(
                    Intent(this, MainActivity::class.java),
                    NoteActivity.getIntent(this, bundle.getLong(IntentDef.NOTE_ID))
            ))
        } else {
            val prefUtils = PrefUtils(context = this)

            val firstStart = prefUtils.firstStart
            if (firstStart) {
                prefUtils.firstStart = false
            }

            startActivity(Intent(this, when (firstStart) {
                true -> IntroActivity::class.java
                false -> MainActivity::class.java
            }))
        }

        finish()
    }

}