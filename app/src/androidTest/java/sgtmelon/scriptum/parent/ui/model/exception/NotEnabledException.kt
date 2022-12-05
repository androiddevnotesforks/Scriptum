package sgtmelon.scriptum.parent.ui.model.exception

class NotEnabledException(why: String) :
    IllegalAccessException("Apply button not enabled: $why")