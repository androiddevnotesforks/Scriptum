package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.IntDef;

/**
 * Аннотация для описания стандартных цветов приложения
 */
@IntDef({
        ColorDef.red,
        ColorDef.purple,
        ColorDef.indigo,
        ColorDef.blue,
        ColorDef.teal,
        ColorDef.green,
        ColorDef.yellow,
        ColorDef.orange,
        ColorDef.brown,
        ColorDef.blueGrey,
        ColorDef.white
})
public @interface ColorDef {

    int red = 0;
    int purple = 1;
    int indigo = 2;
    int blue = 3;
    int teal = 4;
    int green = 5;
    int yellow = 6;
    int orange = 7;
    int brown = 8;
    int blueGrey = 9;
    int white = 10;

}