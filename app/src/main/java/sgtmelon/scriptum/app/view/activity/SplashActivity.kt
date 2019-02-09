package sgtmelon.scriptum.app.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import sgtmelon.scriptum.office.annot.def.IntentDef
import sgtmelon.scriptum.office.utils.PrefUtils

class SplashActivity : AppCompatActivity() {

    companion object {
        private val TAG = SplashActivity::class.java.simpleName

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
        Log.i(TAG, "onCreate")
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        if (bundle != null && bundle.getBoolean(IntentDef.STATUS_OPEN)) {
            startActivities(arrayOf(
                    Intent(this, MainActivity::class.java),
                    NoteActivity.getIntent(this, bundle.getLong(IntentDef.NOTE_ID))
            ))
        } else {
            startNormal()
        }

        finish()
    }

    private fun startNormal() {
        Log.i(TAG, "startNormal")

        val prefUtils = PrefUtils(context = this)

        val firstStart = prefUtils.firstStart
        if (firstStart) prefUtils.firstStart = false

        val intent = Intent(this, when (firstStart) {
            true -> IntroActivity::class.java
            false -> MainActivity::class.java
        })

        startActivity(intent)
    }

}