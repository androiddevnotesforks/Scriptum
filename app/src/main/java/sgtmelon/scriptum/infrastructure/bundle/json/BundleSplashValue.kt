package sgtmelon.scriptum.infrastructure.bundle.json

import sgtmelon.extensions.decode
import sgtmelon.extensions.encode
import sgtmelon.scriptum.infrastructure.bundle.BundleJsonValue
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Splash.Key
import sgtmelon.scriptum.infrastructure.screen.splash.SplashOpen

class BundleSplashValue : BundleJsonValue<SplashOpen>(Key.OPEN, SplashOpen.Main) {
    override fun decode(string: String?): SplashOpen? = string?.decode()
    override fun encode(): String? = value.encode()
}