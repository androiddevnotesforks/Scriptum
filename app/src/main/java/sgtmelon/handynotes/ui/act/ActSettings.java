package sgtmelon.handynotes.ui.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.Help;
import sgtmelon.handynotes.ui.frg.FrgSettings;

public class ActSettings extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_settings);
        Log.i("ActSettings", "onCreate");

        setupToolbar();

        FrgSettings frgSettings = new FrgSettings();
        getFragmentManager().beginTransaction().replace(R.id.actSettings_fl_container, frgSettings).commit();
    }

    private void setupToolbar(){
        Log.i("FrgBin", "setupToolbar");

        Toolbar toolbar = findViewById(R.id.incToolbar_tb);
        toolbar.setTitle(getString(R.string.title_act_settings));

        toolbar.setNavigationIcon(Help.Icon.getDrawable(this, R.drawable.ic_menu_arrow_back));
        toolbar.setNavigationOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.i("ActSettings", "onClick");

        finish();
    }
}
