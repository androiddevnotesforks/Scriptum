package sgtmelon.handynotes.app.view.act;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import sgtmelon.handynotes.R;

public class ActIntro extends AppCompatActivity {

    private static final String TAG = "ActIntro";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_intro);

        setupViewPager();

        Intent intent = new Intent(this, ActMain.class);
        startActivity(intent);
        finish();
    }

    private void setupViewPager(){
        Log.i(TAG, "setupViewPager");


    }
}
