package sgtmelon.scriptum.app.view.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.view.fragment.PreferenceFragment;
import sgtmelon.scriptum.app.view.parent.BaseActivityParent;
import sgtmelon.scriptum.office.Help;

public final class PreferenceActivity extends BaseActivityParent {

    private static final String TAG = PreferenceActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        setupToolbar();

        PreferenceFragment preferenceFragment = new PreferenceFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, preferenceFragment).commit();
    }

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_preference));

        Drawable arrowBack = Help.Draw.get(this, R.drawable.ic_cancel_off, R.attr.clIcon);
        toolbar.setNavigationIcon(arrowBack);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

}
