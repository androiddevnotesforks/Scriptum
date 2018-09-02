package sgtmelon.handynotes.app.view.act;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import sgtmelon.handynotes.office.Help;

public class ActSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent;
        if (Help.Pref.isFirstStart(this)) {
            intent = new Intent(this, ActIntro.class);
        } else intent = new Intent(this, ActMain.class);

        startActivity(intent);
        finish();
    }
}
