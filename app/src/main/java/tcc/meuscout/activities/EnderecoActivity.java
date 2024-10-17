package tcc.meuscout.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import tcc.meuscout.fragments.EnderecoFragment;
import tcc.meuscout.fragments.TimeFragment;
import tcc.meuscout.toolbar.BaseToolbarCadastro;

public class EnderecoActivity extends BaseToolbarCadastro {

    @Override
    protected Fragment getFrament() {
        return EnderecoFragment.newInstance();
    }
}