package tcc.meuscout.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import tcc.meuscout.R;
import tcc.meuscout.activities.PartidaTabActivity;
import tcc.meuscout.adapter.PartidaListAdapter;
import tcc.meuscout.database.ControleBanco;
import tcc.meuscout.model.Partida;
import tcc.meuscout.model.Usuario;
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.Constantes;

public class PartidaListFragment extends Fragment {

    private static PartidaListFragment instance;
    private RecyclerView recyclerView;
    private ArrayList textos, nomes;
    private FloatingActionButton mFabInserirPartida;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;
    private List<Partida> listaPartida = new ArrayList<>();
    private Usuario usuario;
    private PartidaListAdapter adapter;
    private static boolean carregou = false;
    private View view1;

    public static PartidaListFragment newInstance(boolean carr) {
        instance = new PartidaListFragment();
        carregou = carr;
        return instance;
    }

    public static PartidaListFragment getInstance() {
        if (instance == null)
            instance = new PartidaListFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_partida_list, container, false);
        view1 = container.getRootView();
        try {
            usuario = ControleBanco.getInstance().recuperaUsuarioLogado(getActivity());
            setHasOptionsMenu(true);
            inicializarComponentes(view);
            infoNaFrente(true);
            setAdapter();

            mFabInserirPartida = view.findViewById(R.id.fab_adicionar);
            mFabInserirPartida.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle;
                    Intent it = new Intent(getContext(), PartidaTabActivity.class);
                    Partida partida = new Partida();
                    bundle = new Bundle();
                    bundle.putParcelable(Constantes.PARCELABLE_OBJ, partida);
                    it.putExtras(bundle);
                    it.putExtra(Constantes.OPERACAO, Constantes.INSERIR);
                    startActivity(it);
                }
            });

        } catch (Exception e) {
            Alerta.exibeSnackbarLongo(view, Constantes.MSG_GENERICA_ERRO);
            e.printStackTrace();
        }
        return view;
    }

    public void itemClick(Partida partida) {
        Bundle bundle = new Bundle();
        Intent it = new Intent(getActivity().getBaseContext(), PartidaTabActivity.class);
        bundle.putParcelable(Constantes.PARCELABLE_OBJ, partida);
        it.putExtra(Constantes.OPERACAO, Constantes.EDITAR);
        it.putExtras(bundle);
        startActivity(it);
    }

    public void click(Partida partida) {
        Bundle bundle = new Bundle();
        Intent it = new Intent(getActivity().getBaseContext(), PartidaTabActivity.class);
        bundle.putParcelable(Constantes.PARCELABLE_OBJ, partida);
        it.putExtra(Constantes.OPERACAO, Constantes.VISUALIZAR);
        it.putExtras(bundle);
        startActivity(it);
    }

    private List<Partida> getLista() throws Exception {
        if (carregou)
            listaPartida = ControleBanco.getInstance().recuperaPartidas(getActivity(), usuario);

        if (listaPartida != null && listaPartida.size() != 0) {
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }

        return listaPartida;
    }

    private void setAdapter() throws Exception {
        adapter = new PartidaListAdapter(getLista(), getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setAdapterSearch(List<Partida> lista) {
        adapter = new PartidaListAdapter(lista, getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            setAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void inicializarComponentes(View view) {
        recyclerView = view.findViewById(R.id.recycler_lista);
        progressBar = view.findViewById(R.id.progress_lista);
        frameLayout = view.findViewById(R.id.fLayout_lista);
    }

    private void infoNaFrente(boolean valor) {
        if (valor) {
            progressBar.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        }
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int posicao = viewHolder.getAdapterPosition();
            switch (direction) {
                case ItemTouchHelper.RIGHT:
                    try {
                        Partida partida = listaPartida.get(posicao);
                        listaPartida.remove(partida);
                        adapter.notifyItemRemoved(posicao);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                        dialog.setTitle("Aviso");
                        dialog.setMessage("Deseja excluir esta Partida?");
                        dialog.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                partida.removerPartidaFirebase(getActivity(), usuario, Constantes.EXCLUIR);
                                Toast.makeText(getContext(), "Partida excluída!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listaPartida.add(posicao, partida);
                                adapter.notifyItemInserted(posicao);
                            }
                        });
                        dialog.setCancelable(false);
                        dialog.create();
                        dialog.show();
                    } catch (Exception e) {
                        Alerta.exibeSnackbarCurto(viewHolder.itemView, Constantes.MSG_GENERICA_ERRO);
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }

        }
    };

    public void atualizaLista() throws Exception {
        carregou = true;
        setAdapter();
    }

    private void procuraPartida(MenuItem item) {
        final SearchView mSearch = (SearchView) item.getActionView();
        mSearch.setMaxWidth(Integer.MAX_VALUE);
        int searchEditId = androidx.appcompat.R.id.search_src_text;
        EditText et = mSearch.findViewById(searchEditId);
        et.setHint(getActivity().getString(R.string.pesquisar));
        et.setTextColor(Color.WHITE);
        et.setHintTextColor(Color.WHITE);

        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearch.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                try {
                    if (query.isEmpty() || query.length() > 2) {
                        String userInput = query.toLowerCase();
                        List<Partida> newList = new ArrayList<>();
                        for (Partida _partida : listaPartida) {
                            if (_partida != null && _partida.keyToString(getActivity(), usuario).toLowerCase().contains(userInput)) {
                                newList.add(_partida);
                            }
                        }
                        setAdapterSearch(newList);
                    }
                } catch (Exception e) {
                    Alerta.exibeSnackbarCurto(view1, Constantes.MSG_GENERICA_ERRO);
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.orz_menu_actionbar_lista, menu);
        procuraPartida(menu.findItem(R.id.action_search));
    }

}