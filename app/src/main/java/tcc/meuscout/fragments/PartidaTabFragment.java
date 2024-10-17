package tcc.meuscout.fragments;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import tcc.meuscout.R;
import tcc.meuscout.database.ControleBanco;
import tcc.meuscout.model.Partida;
import tcc.meuscout.model.Usuario;
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.BaseTabViewPageFragment;
import tcc.meuscout.util.Constantes;

public class PartidaTabFragment extends BaseTabViewPageFragment {

    private static final int TAB_QTD = 2;
    private static final int TAB_STATS = 0;
    private static final int TAB_INFOS = 1;
    private static PartidaTabFragment instance;
    private Fragment f;
    private Partida mPartida;
    private String operacao;

    public PartidaTabFragment(int layoutFragment, int numItens) {
        super(layoutFragment, numItens);
    }

    public static PartidaTabFragment newInstance() {
        instance = new PartidaTabFragment(R.layout.fragment_partida_tab, TAB_QTD);
        return instance;
    }

    public static PartidaTabFragment getInstance() {
        if (instance == null)
            instance = new PartidaTabFragment(R.layout.fragment_partida_tab, TAB_QTD);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(Constantes.PARCELABLE_OBJ))
                mPartida = (Partida) intent.getParcelableExtra(Constantes.PARCELABLE_OBJ);

            if (intent.hasExtra(Constantes.OPERACAO))
                operacao = intent.getStringExtra(Constantes.OPERACAO);
        }

        if (mPartida == null)
            mPartida = new Partida();
        setHasOptionsMenu(true);
    }

    @Override
    protected void inicializar() throws Exception {
        super.inicializar();

        // Redifinir o título da tela como Registro de Partida caso a Operação seja INSERIR


    }

    @Override
    protected Fragment getItemAba(int position) {
        String name = makeFragmentName(position);
        f = getFragmentManager().findFragmentByTag(name);
        if (position == TAB_STATS) {
            if (f == null)
                f = PartidaStatsFragment.newInstance(mPartida, operacao);
        } else if (position == TAB_INFOS) {
            if (f == null)
                f = PartidaInfosFragment.newInstance(mPartida, operacao);
        }
        return f;
    }

    @Override
    protected CharSequence getPageTitleAbas(int position) {
        switch (position) {
            case TAB_STATS:
                return getString(R.string.txt_partida_aba_stats);
            case TAB_INFOS:
                return getString(R.string.txt_partida_aba_infos);
            default:
                return "";
        }
    }

    @Override
    protected int getMyPagerView() {
        return R.id.pager;
    }

    @Override
    protected int getMyTabLayout() {
        return R.id.insp_veicular_tablayout;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (operacao.equalsIgnoreCase(Constantes.VISUALIZAR))
            menu.findItem(R.id.cadastrar).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
            case R.id.cadastrar:

                try {
                    if (PartidaInfosFragment.getInstance().validarDados()) {
                        Partida partida = PartidaInfosFragment.getInstance().executar();
                        partida = PartidaStatsFragment.getInstance().executar(partida);
                        Usuario usuario = ControleBanco.getInstance().recuperaUsuarioLogado(getActivity());
                        if (operacao.equals(Constantes.INSERIR))
                            partida.cadastrarPartidaFirebase(usuario, getActivity());
                        else
                            partida.atualizarPartidaFirebase(getActivity(), usuario, operacao);
                    }
                } catch (Exception e) {
                    Alerta.exibeSnackbarCurto(f.getView(), Constantes.MSG_GENERICA_ERRO);
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        return true;
    }


}