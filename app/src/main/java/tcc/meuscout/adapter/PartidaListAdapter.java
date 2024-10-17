package tcc.meuscout.adapter;

import android.annotation.SuppressLint;
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

import java.util.List;

import tcc.meuscout.R;
import tcc.meuscout.database.ControleBanco;
import tcc.meuscout.fragments.PartidaListFragment;
import tcc.meuscout.model.Partida;
import tcc.meuscout.model.Time;
import tcc.meuscout.model.Usuario;
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.Constantes;

public class PartidaListAdapter extends RecyclerView.Adapter {

    private List<Partida> listaPartida;
    private Activity mActivity;
    private int scout1 = 0, scout2 = 0;
    private int imgScout1 = 0, imgScout2 = 0;
    private Usuario usuario;

    public PartidaListAdapter(List<Partida> listaPartida, Activity activity) {
        this.listaPartida = listaPartida;
        mActivity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_partidas_list, parent, false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);
        return viewHolderClass;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass = (ViewHolderClass) holder;
        try {
            usuario = ControleBanco.getInstance().recuperaUsuarioLogado(mActivity);
            if (listaPartida != null && listaPartida.size() != 0) {
                Partida partida = listaPartida.get(position);
                viewHolderClass.txtLocal.setText(partida.getNomeLocal());
                String data = partida.getData().substring(8, 10) + "/" + partida.getData().substring(5, 7) + "/" + partida.getData().substring(0, 4) + " " +
                        partida.getData().substring(11, 13) + ":" + partida.getData().substring(14, 16);
                viewHolderClass.txtData.setText(data);
                viewHolderClass.txtDuracao.setText(partida.getDuracao() + "min");
                viewHolderClass.txtRegistro.setText(partida.getTipoRegistro());
                String placar = "";
                int casa = 0, fora = 0;
                if (partida.getTipoRegistro().equals("Partida Única")) {
                    casa = partida.getPlacarCasa();
                    fora = partida.getPlacarFora();
                    placar = casa + "x" + fora;
                    if (partida.getTime_id_firebase() != null && !partida.getTime_id_firebase().isEmpty()) {
                        Time time, timeAdv;
                        time = ControleBanco.getInstance().recuperaTimePorId(mActivity, usuario, partida.getTime_id_firebase());
                        placar = time.getSigla() + " " + placar;
                        if (partida.getTime_adv_id_firebase() != null && !partida.getTime_adv_id_firebase().isEmpty()) {
                            timeAdv = ControleBanco.getInstance().recuperaTimePorId(mActivity, usuario, partida.getTime_adv_id_firebase());
                            placar = placar + " " + timeAdv.getSigla();
                        }
                    }
                    viewHolderClass.txtPlacar.setText(placar);
                    viewHolderClass.txtPlacar.setTextColor(defineCorPlacar(casa - fora));

                } else if (partida.getTipoRegistro().equals("Dia de Jogos")) {
                    casa = partida.getVitorias();
                    fora = partida.getDerrotas();
                    int diferenca = casa - fora;
                    String vit = partida.getVitorias() + "V ";
                    String emp = partida.getEmpates() + "E ";
                    String der = partida.getDerrotas() + "D";
                    placar = vit + emp + der;
                    viewHolderClass.txtPlacar.setText(placar);
                    viewHolderClass.txtPlacar.setTextColor(defineCorPlacar(diferenca));
                } else {
                    viewHolderClass.txtPlacar.setVisibility(View.GONE);
                }

                viewHolderClass.txtPosicao1.setText(partida.getPosicao_sigla());
                viewHolderClass.txtPosicao1.setBackgroundResource(defineCorPosicao(partida.getPosicao_num()));

                defineScouts(partida);

                viewHolderClass.txtScout1.setText(String.valueOf(scout1));
                viewHolderClass.imgScout1.setImageResource(imgScout1);
                viewHolderClass.txtScout2.setText(String.valueOf(scout2));
                viewHolderClass.imgScout2.setImageResource(imgScout2);

                if (partida.getPosicaosec_nome() != null && !partida.getPosicaosec_nome().isEmpty()) {
                    viewHolderClass.txtPosicao2.setText(partida.getPosicaosec_sigla());
                    viewHolderClass.txtPosicao2.setBackgroundResource(defineCorPosicao(partida.getPosicaosec_num()));
                    viewHolderClass.txtPosicao2.setVisibility(View.VISIBLE);
                }

                viewHolderClass.layoutItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PartidaListFragment.getInstance().click(partida);
                    }
                });

                viewHolderClass.layoutItem.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Alerta.exibeToastCurto(mActivity.getApplicationContext(), "Arraste o cartão para a direita para excluir!");
                        return true;
                    }
                });

                viewHolderClass.layoutEditar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PartidaListFragment.getInstance().itemClick(partida);
                    }
                });

            }
        } catch (Exception e) {
            Alerta.exibeSnackbarCurto(holder.itemView, Constantes.MSG_GENERICA_ERRO);
            e.printStackTrace();
        }

    }


    private int defineCorPlacar(int diferenca) {
        if (diferenca > 0)
            return R.color.Cor_Acentuada;
        else if (diferenca < 0)
            return R.color.Cor_Atacante;
        else
            return R.color.Cor_Texto_Nao_Selecionado;
    }

    private int defineCorPosicao(int pos) {
        switch (pos) {
            case 1:
                return R.color.Cor_Goleiro;
            case 2:
                return R.color.Cor_Defensor;
            case 3:
                return R.color.Cor_Meia;
            default:
                return R.color.Cor_Atacante;
        }
    }

    @Override
    public int getItemCount() {
        if (listaPartida != null)
            return listaPartida.size();
        else
            return 0;
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder {
        private TextView txtLocal, txtData, txtScout1, txtScout2, txtPosicao1, txtPosicao2,
                txtDuracao, txtRegistro, txtPlacar;
        private LinearLayout layoutItem, layoutEditar;
        private ImageView imgScout1, imgScout2;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            txtLocal = itemView.findViewById(R.id.txt_partida_local);
            txtData = itemView.findViewById(R.id.txt_partida_data);
            txtScout1 = itemView.findViewById(R.id.txt_partida_scout1);
            txtScout2 = itemView.findViewById(R.id.txt_partida_scout2);
            txtPosicao1 = itemView.findViewById(R.id.txt_partida_posicao1);
            txtPosicao2 = itemView.findViewById(R.id.txt_partida_posicao2);
            txtDuracao = itemView.findViewById(R.id.txt_partida_duracao);
            txtRegistro = itemView.findViewById(R.id.txt_partida_registro);
            txtPlacar = itemView.findViewById(R.id.txt_partida_placar);
            layoutItem = itemView.findViewById(R.id.layout_partida_item);
            layoutEditar = itemView.findViewById(R.id.layout_partida_editar);
            imgScout1 = itemView.findViewById(R.id.img_partida_scout1);
            imgScout2 = itemView.findViewById(R.id.img_partida_scout2);
        }
    }


    private void defineScouts(Partida partida) {
        if (partida.getPosicao_num() == 4) { //ATACANTE
            scout1 = partida.getGolsFeitos();
            imgScout1 = (R.drawable.ic_gol);
            if (partida.getAssistencias() != 0) {
                scout2 = partida.getAssistencias();
                imgScout2 = R.drawable.ic_assistencia;
            } else {
                scout2 = partida.getFinalTotal();
                imgScout2 = R.drawable.ic_chuteira;
            }

        } else if (partida.getPosicao_num() == 3) { //MEIA
            if (partida.getGolsFeitos() != 0) {
                scout1 = partida.getGolsFeitos();
                imgScout1 = R.drawable.ic_gol;
                if (partida.getAssistencias() != 0) {
                    scout2 = partida.getAssistencias();
                    imgScout2 = R.drawable.ic_assistencia;
                } else if (partida.getDesarmes() >= partida.getFinalTotal()) {
                    scout2 = partida.getDesarmes();
                    imgScout2 = R.drawable.ic_desarme;
                } else {
                    scout2 = partida.getFinalTotal();
                    imgScout2 = R.drawable.ic_chuteira;
                }
            } else if (partida.getAssistencias() != 0) {
                scout1 = partida.getAssistencias();
                imgScout1 = R.drawable.ic_assistencia;
                if (partida.getDesarmes() >= partida.getFinalTotal()) {
                    scout2 = partida.getDesarmes();
                    imgScout2 = R.drawable.ic_desarme;
                } else {
                    scout2 = partida.getFinalTotal();
                    imgScout2 = R.drawable.ic_chuteira;
                }
            } else if (partida.getFinalTotal() != 0) {
                scout1 = partida.getFinalTotal();
                imgScout1 = R.drawable.ic_chuteira;
                if (partida.getDesarmes() != 0) {
                    scout2 = partida.getDesarmes();
                    imgScout2 = R.drawable.ic_desarme;
                } else if (partida.getBloqueios() >= partida.getCortes()) {
                    scout2 = partida.getBloqueios();
                    imgScout2 = R.drawable.ic_bloqueio;
                } else {
                    scout2 = partida.getCortes();
                    imgScout2 = R.drawable.ic_cortes;
                }
            } else {
                scout1 = partida.getDesarmes();
                imgScout1 = R.drawable.ic_desarme;
                if (partida.getBloqueios() >= partida.getCortes()) {
                    scout2 = partida.getBloqueios();
                    imgScout2 = R.drawable.ic_bloqueio;
                } else {
                    scout2 = partida.getCortes();
                    imgScout2 = R.drawable.ic_cortes;
                }
            }
        } else if (partida.getPosicao_num() == 2) { //DEFENSOR
            if (partida.getGolsFeitos() != 0) {
                scout1 = partida.getGolsFeitos();
                imgScout1 = R.drawable.ic_gol;
                if (partida.getAssistencias() >= 3 ||
                        (double) partida.getAssistencias() / partida.getDesarmes() > 0.3) {
                    scout2 = partida.getAssistencias();
                    imgScout2 = R.drawable.ic_assistencia;
                } else if (partida.getDesarmes() != 0) {
                    scout2 = partida.getDesarmes();
                    imgScout2 = R.drawable.ic_desarme;
                } else if (partida.getBloqueios() >= partida.getCortes()) {
                    scout2 = partida.getBloqueios();
                    imgScout2 = R.drawable.ic_bloqueio;
                } else {
                    scout2 = partida.getCortes();
                    imgScout2 = R.drawable.ic_cortes;
                }

            } else if (partida.getAssistencias() != 0) {
                scout1 = partida.getAssistencias();
                imgScout1 = R.drawable.ic_assistencia;
                if (partida.getDesarmes() != 0) {
                    scout2 = partida.getDesarmes();
                    imgScout2 = R.drawable.ic_desarme;
                } else if (partida.getBloqueios() >= partida.getCortes()) {
                    scout2 = partida.getBloqueios();
                    imgScout2 = R.drawable.ic_bloqueio;
                } else {
                    scout2 = partida.getCortes();
                    imgScout2 = R.drawable.ic_cortes;
                }
            } else if (partida.getDesarmes() != 0) {
                scout1 = partida.getDesarmes();
                imgScout1 = R.drawable.ic_desarme;
                if (partida.getBloqueios() >= partida.getCortes()) {
                    scout2 = partida.getBloqueios();
                    imgScout2 = R.drawable.ic_bloqueio;
                } else {
                    scout2 = partida.getCortes();
                    imgScout2 = R.drawable.ic_cortes;
                }
            } else if (partida.getBloqueios() != 0) {
                scout1 = partida.getBloqueios();
                imgScout1 = R.drawable.ic_bloqueio;
                scout2 = partida.getCortes();
                imgScout2 = R.drawable.ic_cortes;
            }

        } else { //GOLEIRO
            if (partida.getGolsFeitos() != 0) {
                scout1 = partida.getGolsFeitos();
                imgScout1 = R.drawable.ic_gol;
                if (partida.getDefesasPen() != 0) {
                    scout2 = partida.getDefesasPen();
                    imgScout2 = R.drawable.ic_penalti;
                } else {
                    scout2 = partida.getDefesas();
                    imgScout2 = R.drawable.ic_defesa;
                }

            } else {
                scout1 = partida.getDefesas();
                imgScout1 = R.drawable.ic_defesa;
                if (partida.getDefesasPen() != 0) {
                    scout2 = partida.getDefesasPen();
                    imgScout2 = R.drawable.ic_penalti;
                } else if (partida.getAssistencias() >= 3 ||
                        (double) partida.getAssistencias() / partida.getCortes() >= 0.25) {
                    scout2 = partida.getAssistencias();
                    imgScout2 = R.drawable.ic_assistencia;
                } else {
                    scout2 = partida.getCortes();
                    imgScout2 = R.drawable.ic_cortes;
                }
            }

        }
    }
}
