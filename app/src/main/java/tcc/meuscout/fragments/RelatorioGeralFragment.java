package tcc.meuscout.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import tcc.meuscout.R;
import tcc.meuscout.database.ControleBanco;
import tcc.meuscout.model.Partida;
import tcc.meuscout.model.Usuario;
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.BaseFragment;
import tcc.meuscout.util.Constantes;

public class RelatorioGeralFragment extends BaseFragment {

    private static RelatorioGeralFragment instance;
    private static List<Partida> listaPartida = new ArrayList<>(),
            listaPartidaPass = new ArrayList<>();
    private int vitorias, empates, derrotas, minutos, jogos, jogosNotaInd, jogosNotaPart, dias;
    private int vitoriasPass, empatesPass, derrotasPass, minutosPass, jogosPass, diasPass,
            jogosNotaIndPass, jogosNotaPartPass;
    private double medMinutos, medMinutosPass, medNotaInd, medNotaIndPass, medNotaPart,
            medNotaPartPass, notaInd, notaPart, notaIndPass, notaPartPass;
    private TextView txtDiasJogo, txtDiasJogoPass, txtPartidas, txtPartidasPass,
            txtMinutos, txtMinutosPass, txtMinPart, txtMinPartPass, txtVitorias, txtVitoriasPass,
            txtEmpates, txtEmpatesPass, txtDerrotas, txtDerrotasPass, txtNotaInd, txtNotaIndPass,
            txtNotaPart, txtNotaPartPass, txtPosicao, txtPosicaoSec, txtLugar, txtTipoAtividade,
            txtMaisVitorias, txtMaisEmpates, txtMaisDerrotas, txtComp;
    private LinearLayout lytGeral, lytMais;
    private Usuario usuario;
    private boolean mesmoDia = false;

    public static RelatorioGeralFragment newInstance(List<Partida> lista, List<Partida> listaPassada) {
        instance = new RelatorioGeralFragment();
        listaPartida = lista;
        listaPartidaPass = listaPassada;
        return instance;
    }

    public static RelatorioGeralFragment getInstance() {
        if (instance == null)
            instance = new RelatorioGeralFragment();
        return instance;
    }

    public void setListaPartida(List<Partida> lista, List<Partida> listaPassada) throws Exception {
        listaPartida = lista;
        listaPartidaPass = listaPassada;
        atualizarStats();
    }

    @Override
    protected int getFragmentLayout() throws Exception {
        return R.layout.fragment_relatorio_geral;
    }

    @Override
    protected void inicializar() throws Exception {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            usuario = ControleBanco.getInstance().recuperaUsuarioLogado(getActivity());
            inicializarComponentes(view);
            atualizarStats();
        } catch (Exception e) {
            Alerta.exibeSnackbarLongo(view, Constantes.MSG_GENERICA_ERRO);
            e.printStackTrace();
        }
    }

    public void atualizarStats() throws Exception {
        resetarStats();
        somaStatsPassados();
        somaStats();
        if (listaPartida.size() == 0 && listaPartidaPass.size() == 0) {
            lytGeral.setVisibility(View.GONE);
            txtComp.setVisibility(View.VISIBLE);
        } else {
            lytGeral.setVisibility(View.VISIBLE);
            txtComp.setVisibility(View.GONE);
            defineNegrito();

        }
    }

    private void defineStatsMais() throws Exception {
        String anoMesMin = listaPartida.get(0).getData().substring(0, 4) + "/" + listaPartida.get(0).getData().substring(5, 7) + "/00";
        String anoMesMax = listaPartida.get(0).getData().substring(0, 4) + "/" + listaPartida.get(0).getData().substring(5, 7) + "/31";
        /*
        Partida partida = ControleBanco.getInstance().recuperaPartidaMaisResultado(getActivity(), usuario, anoMesMin, anoMesMax, Constantes.ORDEM_VITORIAS);
        txtMaisVitorias.setText(partida.getVitorias() + "V : " + partida.getData().substring(0, 5));
        partida = ControleBanco.getInstance().recuperaPartidaMaisResultado(getActivity(), usuario, anoMesMin, anoMesMax, Constantes.ORDEM_EMPATES);
        txtMaisEmpates.setText(partida.getEmpates() + "E : " + partida.getData().substring(0, 5));
        partida = ControleBanco.getInstance().recuperaPartidaMaisResultado(getActivity(), usuario, anoMesMin, anoMesMax, Constantes.ORDEM_DERROTAS);
        txtMaisDerrotas.setText(partida.getDerrotas() + "D : " + partida.getData().substring(0, 5));
         */
        String max = ControleBanco.getInstance().recuperaPartidaMaisPosicao(getActivity(), usuario, anoMesMin, anoMesMax);
        txtPosicao.setText(max);
        max = ControleBanco.getInstance().recuperaPartidaMaisPosicaoSec(getActivity(), usuario, anoMesMin, anoMesMax);
        txtPosicaoSec.setText(max);
        max = ControleBanco.getInstance().recuperaPartidaMaisLugar(getActivity(), usuario, anoMesMin, anoMesMax);
        txtLugar.setText(max);
        max = ControleBanco.getInstance().recuperaPartidaMaisAtividade(getActivity(), usuario, anoMesMin, anoMesMax);
        txtTipoAtividade.setText(max);
    }

    private void defineNegrito() {
        defineMaior(listaPartida.size(), listaPartidaPass.size(), txtDiasJogo, txtDiasJogoPass);
        defineMaior(jogos, jogosPass, txtPartidas, txtPartidasPass);
        defineMaior(minutos, minutosPass, txtMinutos, txtMinutosPass);
        defineMaior(Math.round(medMinutos), Math.round(medMinutosPass), txtMinPart, txtMinPartPass);
        defineMaior(vitorias, vitoriasPass, txtVitorias, txtVitoriasPass);
        defineMaior(empates, empatesPass, txtEmpates, txtEmpatesPass);
        defineMaior(derrotas, derrotasPass, txtDerrotas, txtDerrotasPass);
        defineMaior(medNotaInd, medNotaIndPass, txtNotaInd, txtNotaIndPass);
        defineMaior(medNotaPart, medNotaPartPass, txtNotaPart, txtNotaPartPass);
    }

    private void defineMaior(Number atual, Number passado, TextView txtAtual, TextView txtPassado) {
        if (atual.doubleValue() > passado.doubleValue()) {
            txtAtual.setTypeface(null, Typeface.BOLD);
            txtPassado.setTypeface(null, Typeface.NORMAL);
        } else if (atual.doubleValue() < passado.doubleValue()) {
            txtAtual.setTypeface(null, Typeface.NORMAL);
            txtPassado.setTypeface(null, Typeface.BOLD);
        } else {
            txtAtual.setTypeface(null, Typeface.NORMAL);
            txtPassado.setTypeface(null, Typeface.NORMAL);
        }
    }

    private void somaStats() throws Exception {
        if (listaPartida.size() > 0) {
            lytMais.setVisibility(View.VISIBLE);
            defineStatsMais();
            String data = "";
            for (Partida partida : listaPartida) {
                vitorias += partida.getVitorias();
                empates += partida.getEmpates();
                derrotas += partida.getDerrotas();
                minutos += partida.getDuracao() *
                        (partida.getVitorias() + partida.getEmpates() + partida.getDerrotas());
                if (notaInd > -1) {
                    notaInd += partida.getNotaInd();
                    jogosNotaInd += 1;
                }
                if (notaPart > -1) {
                    notaPart += partida.getNotaPart();
                    jogosNotaPart += 1;
                }
                dias++;
                if (partida.getTipoRegistro().equals("Partida Única"))
                    if (!data.isEmpty()) {
                        if (data.equals(partida.getData().substring(0, 10)))
                            dias--;
                        else
                            data = partida.getData().substring(0, 10);
                    } else
                        data = partida.getData().substring(0, 10);

            }
        } else {
            lytMais.setVisibility(View.GONE);
        }
        jogos = vitorias + empates + derrotas;
        txtDiasJogo.setText(String.valueOf(dias));
        txtDiasJogoPass.setText(String.valueOf(diasPass));
        txtPartidas.setText(String.valueOf(jogos));
        txtPartidasPass.setText(String.valueOf(jogosPass));
        txtMinutos.setText(String.valueOf(minutos));
        txtMinutosPass.setText(String.valueOf(minutosPass));
        txtVitorias.setText(String.valueOf(vitorias));
        txtVitoriasPass.setText(String.valueOf(vitoriasPass));
        txtEmpates.setText(String.valueOf(empates));
        txtEmpatesPass.setText(String.valueOf(empatesPass));
        txtDerrotas.setText(String.valueOf(derrotas));
        txtDerrotasPass.setText(String.valueOf(derrotasPass));
        txtNotaInd.setText(String.valueOf(notaInd));
        txtNotaIndPass.setText(String.valueOf(notaIndPass));
        txtNotaPart.setText(String.valueOf(notaPart));
        txtNotaPartPass.setText(String.valueOf(notaPartPass));

        calculaMediasMinutos();
        calculaMediasNotas();

    }

    private void somaStatsPassados() {
        if (listaPartidaPass.size() > 0) {
            String data = "";
            for (Partida partida : listaPartidaPass) {
                if (!partida.getTipoRegistro().equals("Treino")) {
                    vitoriasPass += partida.getVitorias();
                    empatesPass += partida.getEmpates();
                    derrotasPass += partida.getDerrotas();
                    minutosPass += partida.getDuracao() *
                            (partida.getVitorias() + partida.getEmpates() + partida.getDerrotas());
                    if (notaIndPass > -1) {
                        notaIndPass += partida.getNotaInd();
                        jogosNotaIndPass += 1;
                    }
                    if (notaPartPass > -1) {
                        notaPartPass += partida.getNotaPart();
                        jogosNotaPartPass += 1;
                    }
                    diasPass++;
                    if (partida.getTipoRegistro().equals("Partida Única"))
                        if (!data.isEmpty())
                            if (data.equals(partida.getData().substring(0, 10)))
                                diasPass--;
                            else
                                data = partida.getData().substring(0, 10);
                }
            }
            jogosPass = vitoriasPass + empatesPass + derrotasPass;
        }
    }

    private void calculaMediasMinutos() {
        long med = 0;
        if (jogos > 0) {
            medMinutos = (double) minutos / jogos;
            med = Math.round(medMinutos);
        }
        txtMinPart.setText(String.valueOf(med));
        med = 0;
        if (jogosPass > 0) {
            medMinutosPass = (double) minutosPass / jogosPass;
            med = Math.round(medMinutosPass);
        }
        txtMinPartPass.setText(String.valueOf(med));
    }

    private void calculaMediasNotas() {
        long med = 0;
        if (notaInd > -1) {
            medNotaInd = (double) notaInd / jogosNotaInd;
            med = Math.round(medNotaInd);
        }
        txtNotaInd.setText(String.valueOf(med));
        med = 0;
        if (notaPart > -1) {
            medNotaPart = (double) notaPart / jogosNotaPart;
            med = Math.round(medNotaPart);
        }
        txtNotaPart.setText(String.valueOf(med));
        med = 0;
        if (notaIndPass > -1) {
            medNotaIndPass = (double) notaIndPass / jogosNotaIndPass;
            med = Math.round(medNotaIndPass);
        }
        txtNotaIndPass.setText(String.valueOf(med));
        med = 0;
        if (notaPartPass > -1) {
            medNotaPartPass = (double) notaPartPass / jogosNotaPartPass;
            med = Math.round(medNotaPartPass);
        }
        txtNotaPartPass.setText(String.valueOf(med));
    }

    private void resetarStats() {
        vitorias = 0;
        empates = 0;
        derrotas = 0;
        minutos = 0;
        jogos = 0;
        notaInd = 0;
        notaPart = 0;
        jogosNotaInd = 0;
        jogosNotaPart = 0;
        medNotaInd = 0;
        medNotaPart = 0;
        dias = 0;

        vitoriasPass = 0;
        empatesPass = 0;
        derrotasPass = 0;
        minutosPass = 0;
        jogosPass = 0;
        notaIndPass = 0;
        notaPartPass = 0;
        jogosNotaIndPass = 0;
        jogosNotaPartPass = 0;
        medNotaIndPass = 0;
        medNotaPartPass = 0;
        diasPass = 0;

        txtDiasJogo.setText(String.valueOf(dias));
        txtDiasJogoPass.setText(String.valueOf(diasPass));
        txtPartidas.setText(String.valueOf(jogos));
        txtPartidasPass.setText(String.valueOf(jogosPass));
        txtMinutos.setText(String.valueOf(minutos));
        txtMinutosPass.setText(String.valueOf(minutosPass));
        txtVitorias.setText(String.valueOf(vitorias));
        txtVitoriasPass.setText(String.valueOf(vitoriasPass));
        txtEmpates.setText(String.valueOf(empates));
        txtEmpatesPass.setText(String.valueOf(empatesPass));
        txtDerrotas.setText(String.valueOf(derrotas));
        txtDerrotasPass.setText(String.valueOf(derrotasPass));
        txtNotaInd.setText(String.valueOf(notaInd));
        txtNotaIndPass.setText(String.valueOf(notaIndPass));
        txtNotaPart.setText(String.valueOf(notaPart));
        txtNotaPartPass.setText(String.valueOf(notaPartPass));
    }

    private void inicializarComponentes(View view) {
        txtDiasJogo = view.findViewById(R.id.txt_relatorio_diasJogo);
        txtDiasJogoPass = view.findViewById(R.id.txt_relatorio_diasJogoPass);
        txtPartidas = view.findViewById(R.id.txt_relatorio_partidas);
        txtPartidasPass = view.findViewById(R.id.txt_relatorio_partidasPass);
        txtMinutos = view.findViewById(R.id.txt_relatorio_minutos);
        txtMinutosPass = view.findViewById(R.id.txt_relatorio_minutosPass);
        txtMinPart = view.findViewById(R.id.txt_relatorio_minutosPartida);
        txtMinPartPass = view.findViewById(R.id.txt_relatorio_minutosPartidaPass);
        txtVitorias = view.findViewById(R.id.txt_relatorio_vitorias);
        txtVitoriasPass = view.findViewById(R.id.txt_relatorio_vitoriasPass);
        txtEmpates = view.findViewById(R.id.txt_relatorio_empates);
        txtEmpatesPass = view.findViewById(R.id.txt_relatorio_empatesPass);
        txtDerrotas = view.findViewById(R.id.txt_relatorio_derrotas);
        txtDerrotasPass = view.findViewById(R.id.txt_relatorio_derrotasPass);
        txtNotaInd = view.findViewById(R.id.txt_relatorio_notaInd);
        txtNotaIndPass = view.findViewById(R.id.txt_relatorio_notaIndPass);
        txtNotaPart = view.findViewById(R.id.txt_relatorio_notaPart);
        txtNotaPartPass = view.findViewById(R.id.txt_relatorio_notaPartPass);
        txtPosicao = view.findViewById(R.id.txt_relatorio_posicao);
        txtPosicaoSec = view.findViewById(R.id.txt_relatorio_posicaoSec);
        txtLugar = view.findViewById(R.id.txt_relatorio_lugar);
        txtTipoAtividade = view.findViewById(R.id.txt_relatorio_tipoAtividade);
        txtMaisVitorias = view.findViewById(R.id.txt_relatorio_maisVitorias);
        txtMaisEmpates = view.findViewById(R.id.txt_relatorio_maisEmpates);
        txtMaisDerrotas = view.findViewById(R.id.txt_relatorio_maisDerrotas);
        lytGeral = view.findViewById(R.id.layout_relatorio_geral);
        lytMais = view.findViewById(R.id.layout_relatorio_mais);
        txtComp = view.findViewById(R.id.txt_relatorio_comp);
    }
}