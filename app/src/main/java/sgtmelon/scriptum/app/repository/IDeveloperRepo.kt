package sgtmelon.scriptum.app.repository

import android.widget.TextView

interface IDeveloperRepo {

    fun listRankTable(textView: TextView)

    fun listNoteTable(textView: TextView)

    fun listRollTable(textView: TextView)

}