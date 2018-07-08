package sgtmelon.handynotes.app.view.act;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.view.frg.FrgSettings;
import sgtmelon.handynotes.office.Help;

public class ActSettings extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ActSettings";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_settings);
        Log.i(TAG, "onCreate");

        setupToolbar();

        FrgSettings frgSettings = new FrgSettings();
        getFragmentManager().beginTransaction().replace(R.id.actSettings_fl_container, frgSettings).commit();
    }

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        Toolbar toolbar = findViewById(R.id.incToolbar_tb);
        toolbar.setTitle(getString(R.string.title_act_settings));

        toolbar.setNavigationIcon(Help.Icon.getDrawable(this, R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick");

        finish();
    }
}
