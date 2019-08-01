package sgtmelon.scriptum.model.state

import sgtmelon.scriptum.repository.preference.IPreferenceRepo

/**
 * State for control signal without use [IPreferenceRepo.signalCheck]
 *
 * @author SerjantArbuz
 */
class SignalState(val isMelody: Boolean, val isVibration: Boolean, val isLight: Boolean)