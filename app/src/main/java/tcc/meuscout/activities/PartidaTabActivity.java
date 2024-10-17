package tcc.meuscout.activities;

import androidx.fragment.app.Fragment;

import tcc.meuscout.fragments.PartidaTabFragment;
import tcc.meuscout.toolbar.BaseToolbarCadastro;

public class PartidaTabActivity extends BaseToolbarCadastro {

    @Override
    protected Fragment getFrament() {
        return PartidaTabFragment.newInstance();
    }
}