package tcc.meuscout.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import tcc.meuscout.R;
import tcc.meuscout.activities.EnderecoActivity;
import tcc.meuscout.activities.TimeActivity;
import tcc.meuscout.adapter.EnderecoListAdapter;
import tcc.meuscout.adapter.TimeListAdapter;
import tcc.meuscout.database.ControleBanco;
import tcc.meuscout.model.Endereco;
import tcc.meuscout.model.Usuario;
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.Constantes;

public class EnderecoListFragment extends Fragment {

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private static EnderecoListFragment instance;
    private EnderecoListAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton mFabInserirEndereco;
    private List<Endereco> listaEndereco = new ArrayList<>();
    private Usuario usuario;
    private Endereco enderecoSelecionado, enderecoAnterior;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;
    private Dialog dialog;
    private View view;

    public static EnderecoListFragment newInstance() {
        instance = new EnderecoListFragment();
        return instance;
    }

    public static EnderecoListFragment getInstance() {
        if (instance == null)
            instance = new EnderecoListFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_partida_list, container, false);
        setHasOptionsMenu(true);
        inicializarComponentes(view);

        try {
            Intent intent = getActivity().getIntent();
            if (intent.hasExtra(Constantes.SERIALIZABLE_OBJ))
                enderecoAnterior = (Endereco) intent.getSerializableExtra(Constantes.SERIALIZABLE_OBJ);

            usuario = ControleBanco.getInstance().recuperaUsuarioLogado(getActivity());
            carregaListaEndereco(usuario, getActivity());

            mFabInserirEndereco = view.findViewById(R.id.fab_adicionar);
            mFabInserirEndereco.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //timeSelecionado = new Time();
                    Intent it = new Intent(getActivity().getBaseContext(), EnderecoActivity.class);
                    it.putExtra(Constantes.OPERACAO, Constantes.INSERIR);
                    startActivityForResult(it, Constantes.REQUESTCODE_100);
                }
            });

        } catch (Exception e) {
            Alerta.exibeSnackbarCurto(view, Constantes.MSG_GENERICA_ERRO);
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (requestCode == Constantes.REQUESTCODE_100 && resultCode == Activity.RESULT_OK) {
                carregaListaEndereco(usuario, getActivity());
            }
        } catch (Exception e) {
            Alerta.exibeSnackbarCurto(view, Constantes.MSG_GENERICA_ERRO);
            e.printStackTrace();
        }
    }

    public void click(Endereco endereco) {
        Intent intent = new Intent();
        intent.putExtra(Constantes.RESULT, (Parcelable) endereco);
        enderecoSelecionado = endereco;
        requireActivity().setResult(Activity.RESULT_OK, intent);
        requireActivity().finish();
    }

    public void itemClick(Endereco endereco) {
        Intent it = new Intent(getActivity().getBaseContext(), EnderecoActivity.class);
        it.putExtra(Constantes.OPERACAO, Constantes.EDITAR);
        enderecoSelecionado = endereco;
        startActivity(it);
    }

    /*
    private List<Time> getLista() throws Exception {
        listaTime = ControleBanco.getInstance().recuperaListaTimes(getActivity(), usuario);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        return listaTime;
    }
    */

    private void setAdapter() throws Exception {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        adapter = new EnderecoListAdapter(getContext(), listaEndereco, getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        if (dialog.isShowing())
            dialog.dismiss();
    }

    private void setAdapterSearch(List<Endereco> lista) {
        adapter = new EnderecoListAdapter(getContext(), lista, getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    public Endereco retornaEndereco() {
        Endereco end = enderecoSelecionado;
        enderecoSelecionado = null;
        return end;
    }

    private void procuraEndereco(MenuItem item) {
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
                if (query.isEmpty() || query.length() > 2) {
                    String userInput = query.toLowerCase();
                    List<Endereco> newList = new ArrayList<>();
                    for (Endereco _endereco : listaEndereco) {
                        if (_endereco != null && _endereco.toString().toLowerCase().contains(userInput)) {
                            newList.add(_endereco);
                        }
                    }
                    setAdapterSearch(newList);
                }
                return false;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.orz_menu_actionbar_lista, menu);
        procuraEndereco(menu.findItem(R.id.action_search));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                if (enderecoAnterior != null) {
                    if (enderecoSelecionado != null &&
                            !enderecoSelecionado.getNomelocal().equals(enderecoAnterior.getNomelocal()))
                        bundle.putSerializable(Constantes.RESULT, enderecoSelecionado);
                    else if (enderecoSelecionado == null)
                        bundle.putSerializable(Constantes.RESULT, enderecoAnterior);
                    intent.putExtras(bundle);
                }

                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
                break;
            default:
                break;
        }
        return true;
    }
/*
    @Override
    public void onResume() {
        super.onResume();
        try {
            if (view != null)
                carregaListaTimes(usuario, getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

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
                        Endereco endereco = listaEndereco.get(posicao);

                        if (ControleBanco.getInstance().existePartidaEndereco(getActivity(), usuario, endereco)) {
                            Alerta.exibeSnackbarCurto(viewHolder.itemView, "Não pode excluir um endereço que tenha partidas registradas.");
                            adapter.notifyDataSetChanged();
                            break;
                        }

                        listaEndereco.remove(endereco);
                        adapter.notifyItemRemoved(posicao);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                        dialog.setTitle("Aviso");
                        dialog.setMessage("Deseja excluir este Endereço?");
                        dialog.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                enderecoSelecionado = endereco;
                                endereco.removerTimeFirebase(getActivity());
                                Toast.makeText(getContext(), "Endereço excluído!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listaEndereco.add(posicao, endereco);
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
    /*
    public void carregaListaEndereco(Usuario usuario, Activity activity) {
        try {
            View view1 = activity.findViewById(android.R.id.content);
            dialogCarregarDados();
            DatabaseReference usuariosFB = reference.child("usuarios").child(usuario.getFirebase_id())
                    .child("enderecos");
            usuariosFB.addValueEventListener(new ValueEventListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        listaEndereco = new ArrayList<>();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Endereco post = postSnapshot.getValue(Endereco.class);
                            post.setFirebase_id(postSnapshot.getKey());
                            post.setUsuario_id_firebase(usuario.getFirebase_id());
                            if (post.atualizarEnderecoBanco(activity) < 1)
                                post.cadastrarEnderecoBanco(activity);
                            listaEndereco.add(post);
                        }
                        listaEndereco.sort();
                        setAdapter();
                        if (dialog != null && dialog.isShowing())
                            dialog.dismiss();
                        usuariosFB.removeEventListener(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Alerta.exibeDialog(activity.getApplicationContext(), "Erro", Constantes.MSG_GENERICA_ERRO,
                                R.string.string_ok);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Alerta.exibeSnackbarCurto(view1, Constantes.MSG_ERRO_CONEXAO);
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Alerta.exibeDialog(activity.getApplicationContext(), "Erro", Constantes.MSG_GENERICA_ERRO,
                    R.string.string_ok);
        }
    }
    */
    public void carregaListaEndereco(Usuario usuario, Activity activity) {
        try {
            dialogCarregarDados();
            listaEndereco = ControleBanco.getInstance().recuperaListaEndereco(activity, usuario);
            setAdapter();
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();

        } catch (Exception e) {
            e.printStackTrace();
            Alerta.exibeDialog(activity.getApplicationContext(), "Erro", Constantes.MSG_GENERICA_ERRO,
                    R.string.string_ok);
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

    private void dialogCarregarDados() {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_baixar_dados);
        dialog.setCancelable(false);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        //dialog.getWindow().setLayout((6 * width)/7, (4 * height)/5);
        //dialog.getWindow().setLayout((6 * width) / 7, height / 4);
        dialog.create();
        dialog.show();
    }
}