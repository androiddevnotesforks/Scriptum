package sgtmelon.handynotes.office.blank;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.def.DefTheme;

public class BlankAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int valTheme = Help.Pref.getTheme(this);
        switch (valTheme){
            case DefTheme.light:
                setTheme(R.style.App_Light);
                break;
            case DefTheme.dark:
                setTheme(R.style.App_Dark);
                break;
        }
    }
}
