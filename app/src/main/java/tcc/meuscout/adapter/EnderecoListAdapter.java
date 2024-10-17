package tcc.meuscout.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tcc.meuscout.R;
import tcc.meuscout.database.ControleBanco;
import tcc.meuscout.fragments.EnderecoListFragment;
import tcc.meuscout.model.Endereco;
import tcc.meuscout.model.Usuario;
import tcc.meuscout.util.Alerta;

public class EnderecoListAdapter extends RecyclerView.Adapter {

    private List<Endereco> listaEndereco = new ArrayList<>();
    private Context context;
    private Usuario usuario;
    private Activity activity;

    public EnderecoListAdapter(Context context, List<Endereco> listaEndereco, Activity activity) {
        this.listaEndereco = listaEndereco;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_endereco_list, parent, false);
        try {
            usuario = ControleBanco.getInstance().recuperaUsuarioLogado(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass = (ViewHolderClass) holder;
        if (listaEndereco != null && listaEndereco.size() != 0) {
            Endereco endereco = listaEndereco.get(position);
            viewHolderClass.textoNome.setText(endereco.getNomelocal());
            viewHolderClass.textoEndereco.setText(endereco.getEndereco());
            /* Habilitar aqui a imagem de favorito
            if (endereco.getFirebase_id().equals(usuario.getEndereco_id_firebase()))

                viewHolderClass.imgPerfil.setVisibility(View.VISIBLE);*/

            viewHolderClass.imgEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EnderecoListFragment.getInstance().itemClick(endereco);
                }
            });

            viewHolderClass.layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EnderecoListFragment.getInstance().click(endereco);
                }
            });
            viewHolderClass.layoutItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Alerta.exibeToastCurto(context, "Arraste o cartão para a direita para excluir!");
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (listaEndereco != null)
            return listaEndereco.size();
        else
            return 0;
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder {
        private TextView textoNome, textoEndereco;
        private ImageView imgEditar, imgPerfil;
        private LinearLayout layoutItem;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            textoNome = itemView.findViewById(R.id.txt_endereco_nomeLocal);
            textoEndereco = itemView.findViewById(R.id.txt_endereco_endereco);
            imgEditar = itemView.findViewById(R.id.layout_listaEndereco_ImgEditar);
            imgPerfil = itemView.findViewById(R.id.layout_listaEndereco_ImgPerfil);
            layoutItem = itemView.findViewById(R.id.layout_listaEndereco_item);
        }
    }
}
