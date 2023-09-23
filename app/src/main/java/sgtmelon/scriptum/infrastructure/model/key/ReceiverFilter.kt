package sgtmelon.scriptum.infrastructure.model.key

import android.content.IntentFilter
import sgtmelon.scriptum.BuildConfig

/** appId needed for preventing calls between different builds (e.g. ".debug") */
private const val PREFIX = "${BuildConfig.APPLICATION_ID}_RECEIVER_FILTER"

enum class ReceiverFilter(val action: String) {
    RANK(action = "${PREFIX}_RANK"),
    NOTES(action = "${PREFIX}_NOTES"),
    BIN(action = "${PREFIX}_BIN"),
    NOTE(action = "${PREFIX}_NOTE"),
    NOTIFICATION(action = "${PREFIX}_NOTIFICATION"),
    ALARM(action = "${PREFIX}_ALARM"),
    SYSTEM(action = "${PREFIX}_SYSTEM"),
    ETERNAL(action = "${PREFIX}_ETERNAL"),
    DEVELOP(action = "${PREFIX}_DEVELOP");

    val value = IntentFilter(action)

}