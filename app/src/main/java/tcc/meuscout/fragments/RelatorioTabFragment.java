package tcc.meuscout.fragments;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import tcc.meuscout.R;
import tcc.meuscout.model.Partida;
import tcc.meuscout.util.BaseTabViewPageFragment;

public class RelatorioTabFragment extends BaseTabViewPageFragment {

    private static final int TAB_QTD = 4;
    private static final int TAB_GERAL = 0;
    private static final int TAB_ATAQUE = 1;
    private static final int TAB_DEFESA = 2;
    private static final int TAB_DISCIPLINA = 3;
    private static RelatorioTabFragment instance;
    private Fragment f;
    private Partida mPartida;
    private String operacao;
    private static List<Partida> listaPartida = new ArrayList<>(), listaPartidaPass = new ArrayList<>();

    public RelatorioTabFragment(int layoutFragment, int numItens) {
        super(layoutFragment, numItens);
    }

    public static RelatorioTabFragment newInstance(List<Partida> lista, List<Partida> listaPassada) {
        instance = new RelatorioTabFragment(R.layout.fragment_partida_tab, TAB_QTD);
        listaPartida = lista;
        listaPartidaPass = listaPassada;
        return instance;
    }

    public static RelatorioTabFragment getInstance() {
        if (instance == null)
            instance = new RelatorioTabFragment(R.layout.fragment_partida_tab, TAB_QTD);
        return instance;
    }

    public void setListaPartida(List<Partida> lista, List<Partida> listaPassada) throws Exception {
        listaPartida = lista;
        listaPartidaPass = listaPassada;
        atualizaLista();
    }

    public List<Partida> getListaPartida() {
        return listaPartida;
    }
    public List<Partida> getListaPartidaPass() {
        return listaPartida;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void inicializar() throws Exception {
        super.inicializar();
    }

    private void atualizaLista() throws Exception {
        RelatorioGeralFragment.getInstance().setListaPartida(listaPartida, listaPartidaPass);
        RelatorioAtaqueFragment.getInstance().setListaPartida(listaPartida, listaPartidaPass);
        RelatorioDefesaFragment.getInstance().setListaPartida(listaPartida, listaPartidaPass);
        RelatorioDisciplinaFragment.getInstance().setListaPartida(listaPartida, listaPartidaPass);
    }

    @Override
    protected Fragment getItemAba(int position) {
        String name = makeFragmentName(position);
        f = getFragmentManager().findFragmentByTag(name);
        if (position == TAB_GERAL) {
            if (f == null)
                f = RelatorioGeralFragment.newInstance(listaPartida, listaPartidaPass);
        } else if (position == TAB_ATAQUE) {
            if (f == null)
                f = RelatorioAtaqueFragment.newInstance(listaPartida, listaPartidaPass);
        } else if (position == TAB_DEFESA) {
            if (f == null)
                f = RelatorioDefesaFragment.newInstance(listaPartida, listaPartidaPass);
        } else if (position == TAB_DISCIPLINA) {
            if (f == null)
                f = RelatorioDisciplinaFragment.newInstance(listaPartida, listaPartidaPass);
        }
        return f;
    }

    @Override
    protected CharSequence getPageTitleAbas(int position) {
        switch (position) {
            case TAB_GERAL:
                return ("GERAL");
            case TAB_ATAQUE:
                return ("ATAQUE");
            case TAB_DEFESA:
                return ("DEFESA");
            case TAB_DISCIPLINA:
                return ("DISCIPLINA");
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
        /*if (operacao.equalsIgnoreCase(Constantes.VISUALIZAR))
            menu.findItem(R.id.cadastrar).setVisible(false);*/
        //inflar menu com filtro -> FUTURO
    }

}