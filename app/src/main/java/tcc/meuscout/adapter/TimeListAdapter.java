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
import tcc.meuscout.fragments.TimeListFragment;
import tcc.meuscout.model.Time;
import tcc.meuscout.model.Usuario;
import tcc.meuscout.util.Alerta;

public class TimeListAdapter extends RecyclerView.Adapter {

    private List<Time> listaTime = new ArrayList<>();
    private Context context;
    private Usuario usuario;
    private Activity activity;

    public TimeListAdapter(Context context, List<Time> listaTime, Activity activity) {
        this.listaTime = listaTime;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_time_list, parent, false);
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
        if (listaTime != null && listaTime.size() != 0) {
            Time time = listaTime.get(position);
            viewHolderClass.texto.setText(time.getNome() + " - " + time.getSigla());
            if (time.getFirebase_id().equals(usuario.getTime_id_firebase()))
                viewHolderClass.imgPerfil.setVisibility(View.VISIBLE);

            viewHolderClass.imgEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimeListFragment.getInstance().itemClick(time);
                }
            });

            viewHolderClass.layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimeListFragment.getInstance().click(time);
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
        if (listaTime != null)
            return listaTime.size();
        else
            return 0;
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder {
        private TextView texto;
        private ImageView imgEditar, imgPerfil;
        private LinearLayout layoutItem;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            texto = itemView.findViewById(R.id.txt_partida_local);
            imgEditar = itemView.findViewById(R.id.layout_listaTimes_ImgEditar);
            imgPerfil = itemView.findViewById(R.id.layout_listaTimes_ImgPerfil);
            layoutItem = itemView.findViewById(R.id.layout_listaTimes_item);
        }
    }
}
