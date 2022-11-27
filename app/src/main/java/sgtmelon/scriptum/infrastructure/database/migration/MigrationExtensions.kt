package sgtmelon.scriptum.infrastructure.database.migration

import android.database.Cursor

fun Cursor.getColumnIndexOrNull(name: String): Int? = getColumnIndex(name).takeIf { it != -1 }

fun String.clearSplit(regex: String = ",") = split(regex.toRegex()).dropLastWhile { it.isEmpty() }