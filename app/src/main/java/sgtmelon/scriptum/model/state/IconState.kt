package sgtmelon.scriptum.model.state

/**
 * State for control icon animation
 */
class IconState {

    var animate = true

    fun notAnimate(func: () -> Unit) {
        animate = false
        func()
        animate = true
    }

}