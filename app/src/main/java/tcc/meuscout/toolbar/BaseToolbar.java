package tcc.meuscout.toolbar;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import tcc.meuscout.R;

public abstract class BaseToolbar extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            savedInstanceState = null;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_fragment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fg1, getFrament());
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.orz_menu_actionbar_tab, menu);
        return true;
    }

    protected abstract Fragment getFrament();
}
