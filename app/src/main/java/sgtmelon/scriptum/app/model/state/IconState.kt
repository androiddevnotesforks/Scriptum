package sgtmelon.scriptum.app.model.state

/**
 * Состояние для иконок, для их лучшего контроля
 */
class IconState {

    var animate = true

    fun notAnimate(func: () -> Unit) {
        animate = false
        func.invoke()
        animate = true
    }

}