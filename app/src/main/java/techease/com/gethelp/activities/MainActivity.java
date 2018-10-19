package techease.com.gethelp.activities;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import techease.com.gethelp.R;
import techease.com.gethelp.fragments.OnBoardFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = new OnBoardFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

    }
}
