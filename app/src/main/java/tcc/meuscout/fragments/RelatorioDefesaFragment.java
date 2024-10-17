package tcc.meuscout.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tcc.meuscout.R;
import tcc.meuscout.database.ControleBanco;
import tcc.meuscout.model.Partida;
import tcc.meuscout.model.Usuario;
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.BaseFragment;
import tcc.meuscout.util.Constantes;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class RelatorioDefesaFragment extends BaseFragment {

    private static RelatorioDefesaFragment instance;
    private PieChart pieChartSDef;
    private HorizontalBarChart barChartSDef1, barChartSDef2, barChartSDef3, barChartSDef4, barChartSDef5,
            barChartDefGol, barChartDefesas, barChartGolSof, barChartFinCertas,
            barChartPenEnc, barChartPenEnc1, barChartPenEnc2, barChartPenEnc3;
    private static List<Partida> listaPartida,
            listaPartidaPass;
    private int defesaScouts, desarmes, cortes, bloqueios, erros,
            defesas, golsSof, penEncarados, penDefendidos, penLevados,
            vitorias, empates, derrotas, minutos, jogos, dias;
    private int defesaScoutsPass, desarmesPass, cortesPass, bloqueiosPass, errosPass,
            defesasPass, golsSofPass, penEncaradosPass, penDefendidosPass, penLevadosPass,
            vitoriasPass, empatesPass, derrotasPass, minutosPass, jogosPass, diasPass;
    private double medMinuto, medJogo, medDia;
    private TextView txtScoutDef, txtQtdSDef1, txtQtdSDef2, txtQtdSDef3, txtQtdSDef4, txtSDefComp,
            txtDesMinuto, txtDesJogo, txtDesDia, txtCorMinuto, txtCorJogo, txtCorDia, txtDefGolSComp,
            txtBloqMinuto, txtBloqJogo, txtBloqDia, txtErrosMinuto, txtErrosJogo, txtErrosDia,
            txtDefGolS1, txtDefGolS2, txtFinCertas,
            txtDefMinuto, txtDefJogo, txtDefDia, txtGolSMinuto, txtGolSJogo, txtGolSDia,
            txtPenEnc, txtPenMinuto, txtPenJogo, txtPenDia, txtPenEnc1, txtPenEnc2, txtPenEncComp;
    private LinearLayout lytQtdScoutsDef, lytMedScoutsDef2,
            lytMedDesarmes, lytMedCortes, lytMedBloq, lytMedErros, lytGrafDefGolS,
            lytQtdSDef1, lytQtdSDef2, lytQtdSDef3, lytQtdSDef4, lytTipoSDefMes, lytGrafSDef,
            lytDefGolS1, lytDefGolS2, lytMedDefesas, lytMedGolSof, lytTipoDefGolS, lytMedDefGolS,
            lytGrafPenEnc, lytTipoPenEncMes, lytPenEnc1, lytPenEnc2;
    private View view1;
    private Usuario usuario;

    public static RelatorioDefesaFragment newInstance(List<Partida> lista, List<Partida> listaPassada) {
        instance = new RelatorioDefesaFragment();
        listaPartida = lista;
        listaPartidaPass = listaPassada;
        return instance;
    }

    public static RelatorioDefesaFragment getInstance() {
        if (instance == null)
            instance = new RelatorioDefesaFragment();
        return instance;
    }

    public void setListaPartida(List<Partida> lista, List<Partida> listaPassada) {
        listaPartida = lista;
        listaPartidaPass = listaPassada;
        if (view1 != null)
            atualizarGraficos();
    }

    @Override
    protected int getFragmentLayout() throws Exception {
        return R.layout.fragment_relatorio_defesa;
    }

    @Override
    protected void inicializar() throws Exception {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            usuario = ControleBanco.getInstance().recuperaUsuarioLogado(getActivity());
            inicializarComponentes(view);
            view1 = view;
            atualizarGraficos();
        } catch (Exception e) {
            Alerta.exibeSnackbarLongo(view, Constantes.MSG_GENERICA_ERRO);
            e.printStackTrace();
        }
    }

    public void atualizarGraficos() {
        resetarStats();
        somaStats();
        mostrarGraficoScoutsDefensivos();
        mostrarGraficoDefesasGolSof();
        mostrarGraficoPenEncarados();
        if (listaPartidaPass.size() > 0)
            somaStatsPassados();
        mostrarGraficoCompScoutsDef();
        mostrarGraficoCompDefesasGolSof();
        mostrarGraficoCompPenEncarados();
    }

    private void mostrarGraficoCompPenEncarados() {
        if (penEncarados == 0 && penEncaradosPass == 0) {
            txtPenEncComp.setVisibility(View.VISIBLE);
            lytTipoPenEncMes.setVisibility(View.GONE);
            barChartPenEnc1.setVisibility(View.GONE);
            barChartPenEnc2.setVisibility(View.GONE);
            barChartPenEnc3.setVisibility(View.GONE);
            return;
        } else {
            txtPenEncComp.setVisibility(View.GONE);
            lytTipoPenEncMes.setVisibility(View.VISIBLE);
            barChartPenEnc1.setVisibility(View.VISIBLE);
            barChartPenEnc2.setVisibility(View.VISIBLE);
            barChartPenEnc3.setVisibility(View.VISIBLE);
        }

        Legend l = barChartPenEnc1.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.add(new BarEntry(0,
                new float[]{penEncaradosPass, penEncarados}));
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        yVals2.add(new BarEntry(0,
                new float[]{penDefendidosPass, penDefendidos}));
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        yVals3.add(new BarEntry(0,
                new float[]{penLevadosPass, penLevados}));

        configChartCompSimples(barChartPenEnc1, yVals1);
        configChartCompSimples(barChartPenEnc2, yVals2);
        configChartCompSimples(barChartPenEnc3, yVals3);
        barChartPenEnc1.getLegend().setEnabled(true);
    }

    private void mostrarGraficoPenEncarados() {
        if (penEncarados == 0) {
            barChartPenEnc.setVisibility(View.GONE);
            lytGrafPenEnc.setVisibility(View.GONE);
            return;
        } else {
            barChartPenEnc.setVisibility(View.VISIBLE);
            lytGrafPenEnc.setVisibility(View.VISIBLE);
        }

        Legend l = barChartPenEnc.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.add(new BarEntry(0,
                new float[]{penLevados, penDefendidos}));

        String[] ordem = new String[]{"Levados", "Defendidos"};
        defineOrdemPenEncarados(ordem, 2);
        printarOrdemPenEncarados(ordem);

        YAxis leftAxis = barChartPenEnc.getAxisLeft();
        //leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setDrawGridLines(false);
        leftAxis.setEnabled(false);

        XAxis xLabels = barChartPenEnc.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setEnabled(false);
        xLabels.setDrawGridLines(false);

        BarDataSet set1;
        set1 = new BarDataSet(yVals1, "");
        set1.setDrawIcons(false);
        set1.setColors(getColorsErradoCerto());
        set1.setStackLabels(new String[]{"Levados", "Defendidos"});

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                long med = Math.round((value / penEncarados) * 100);
                if (med == 0)
                    return "";
                else
                    return "" + (int) med + "%";
            }
        });
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(12f);

        barChartPenEnc.setData(data);
        barChartPenEnc.getLegend().setEnabled(false);
        barChartPenEnc.setPinchZoom(false);
        barChartPenEnc.setDrawValueAboveBar(false);
        barChartPenEnc.getAxisRight().setEnabled(false);
        barChartPenEnc.setFitBars(true);
        barChartPenEnc.getDescription().setEnabled(false);
        barChartPenEnc.setDrawGridBackground(false);
        barChartPenEnc.setDrawBarShadow(false);
        barChartPenEnc.setHighlightFullBarEnabled(false);
        barChartPenEnc.setClickable(false);
        barChartPenEnc.setHighlightPerTapEnabled(false);
        barChartPenEnc.setDoubleTapToZoomEnabled(false);
        barChartPenEnc.setHighlightPerDragEnabled(false);
        barChartPenEnc.animateX(1000);
        barChartPenEnc.animateY(1000);
        barChartPenEnc.invalidate();
        barChartPenEnc.getLegend().setEnabled(true);
    }

    private void defineOrdemPenEncarados(String[] arr, int n) {
        if (n == 1)
            return;

        for (int i = 0; i < n - 1; i++) {
            if ("Defendidos".equals(arr[i])) {
                if (arr[i + 1].equals("Levados")) {
                    if (penDefendidos >= penLevados) {
                        String temp = arr[i];
                        arr[i] = arr[i + 1];
                        arr[i + 1] = temp;
                    }
                }
            } else {
                if (arr[i + 1].equals("Defendidos")) {
                    if (penLevados > penDefendidos) {
                        String temp = arr[i];
                        arr[i] = arr[i + 1];
                        arr[i + 1] = temp;
                    }
                }
            }
        }
        defineOrdemPenEncarados(arr, n - 1);
    }

    private void printarOrdemPenEncarados(String[] arr) {
        printOrdemPenEncarados(lytPenEnc2, txtPenEnc2, arr[0]);
        printOrdemPenEncarados(lytPenEnc1, txtPenEnc1, arr[1]);
    }

    private void printOrdemPenEncarados(LinearLayout layout, TextView texto, String tipo) {
        String txt = null;
        boolean zerado = true;
        if ("Defendidos".equals(tipo)) {
            if (penDefendidos != 0) {
                zerado = false;
                txt = "Defendidos: " + penDefendidos;
            }
        } else {
            if (penLevados != 0) {
                zerado = false;
                txt = "Levados: " + penLevados;
            }
        }
        if (!zerado) {
            texto.setText(txt);
            texto.setVisibility(View.VISIBLE);
            layout.setBackgroundResource(defineCorPosicaoPenEnc(tipo));
            layout.setVisibility(View.VISIBLE);
        } else {
            texto.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
        }
    }

    private int defineCorPosicaoPenEnc(String pos) {
        if ("Defendidos".equals(pos)) {
            return R.color.Cor_Meia;
        }
        return R.color.Cor_Atacante;
    }

    private void mostrarGraficoCompDefesasGolSof() {
        if (defesas + golsSof == 0 && defesasPass + golsSofPass == 0) {
            txtDefGolSComp.setVisibility(View.VISIBLE);
            lytTipoDefGolS.setVisibility(View.GONE);
            barChartFinCertas.setVisibility(View.GONE);
            barChartDefesas.setVisibility(View.GONE);
            barChartGolSof.setVisibility(View.GONE);
            return;
        } else {
            txtDefGolSComp.setVisibility(View.GONE);
            lytTipoDefGolS.setVisibility(View.VISIBLE);
            barChartFinCertas.setVisibility(View.VISIBLE);
            barChartDefesas.setVisibility(View.VISIBLE);
            barChartGolSof.setVisibility(View.VISIBLE);
        }

        Legend l = barChartFinCertas.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.add(new BarEntry(0,
                new float[]{defesasPass + golsSofPass, defesas + golsSof}));
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        yVals2.add(new BarEntry(0,
                new float[]{defesasPass, defesas}));
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        yVals3.add(new BarEntry(0,
                new float[]{golsSofPass, golsSof}));

        configChartCompSimples(barChartFinCertas, yVals1);
        configChartCompSimples(barChartDefesas, yVals2);
        configChartCompSimples(barChartGolSof, yVals3);
        barChartFinCertas.getLegend().setEnabled(true);
    }

    private void mostrarGraficoDefesasGolSof() {
        if (defesas + golsSof == 0) {
            barChartDefGol.setVisibility(View.GONE);
            lytGrafDefGolS.setVisibility(View.GONE);
            return;
        } else {
            barChartDefGol.setVisibility(View.VISIBLE);
            lytGrafDefGolS.setVisibility(View.VISIBLE);
        }

        Legend l = barChartDefGol.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.add(new BarEntry(0,
                new float[]{golsSof, defesas}));

        String[] ordem = new String[]{"Gols Sofridos", "Defesas"};
        defineOrdemDefGolS(ordem, 2);
        printarOrdemDefGolS(ordem);

        YAxis leftAxis = barChartDefGol.getAxisLeft();
        //leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setDrawGridLines(false);
        leftAxis.setEnabled(false);

        XAxis xLabels = barChartDefGol.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setEnabled(false);
        xLabels.setDrawGridLines(false);

        BarDataSet set1;
        set1 = new BarDataSet(yVals1, "");
        set1.setDrawIcons(false);
        set1.setColors(getColorsErradoCerto());
        set1.setStackLabels(new String[]{"Gols Sofridos", "Defesas"});

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                long med = Math.round((value / (defesas + golsSof)) * 100);
                if (med == 0)
                    return "";
                else
                    return "" + (int) med + "%";
            }
        });
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(12f);

        barChartDefGol.setData(data);
        barChartDefGol.getLegend().setEnabled(false);
        barChartDefGol.setPinchZoom(false);
        barChartDefGol.setDrawValueAboveBar(false);
        barChartDefGol.getAxisRight().setEnabled(false);
        barChartDefGol.setFitBars(true);
        barChartDefGol.getDescription().setEnabled(false);
        barChartDefGol.setDrawGridBackground(false);
        barChartDefGol.setDrawBarShadow(false);
        barChartDefGol.setHighlightFullBarEnabled(false);
        barChartDefGol.setClickable(false);
        barChartDefGol.setHighlightPerTapEnabled(false);
        barChartDefGol.setDoubleTapToZoomEnabled(false);
        barChartDefGol.setHighlightPerDragEnabled(false);
        barChartDefGol.animateX(1000);
        barChartDefGol.animateY(1000);
        barChartDefGol.invalidate();
        barChartDefGol.getLegend().setEnabled(true);
    }

    private void defineOrdemDefGolS(String[] arr, int n) {
        if (n == 1)
            return;

        for (int i = 0; i < n - 1; i++) {
            if ("Defesas".equals(arr[i])) {
                if (arr[i + 1].equals("Gols Sofridos")) {
                    if (defesas >= golsSof) {
                        String temp = arr[i];
                        arr[i] = arr[i + 1];
                        arr[i + 1] = temp;
                    }
                }
            } else {
                if (arr[i + 1].equals("Defesas")) {
                    if (golsSof > defesas) {
                        String temp = arr[i];
                        arr[i] = arr[i + 1];
                        arr[i + 1] = temp;
                    }
                }
            }
        }
        defineOrdemDefGolS(arr, n - 1);
    }

    private int[] getColorsErradoCerto() {
        // have as many colors as stack-values per entry
        int[] colors = new int[2];
        System.arraycopy(coresErradoCerto, 0, colors, 0, 2);
        return colors;
    }

    public final int[] coresErradoCerto = {
            rgb("#ED0000"), rgb("#64AF30")
    };

    private void printarOrdemDefGolS(String[] arr) {
        printOrdemDefGolS(lytDefGolS2, txtDefGolS2, arr[0]);
        printOrdemDefGolS(lytDefGolS1, txtDefGolS1, arr[1]);
    }

    private void printOrdemDefGolS(LinearLayout layout, TextView texto, String tipo) {
        String txt = null;
        boolean zerado = true;
        if ("Defesas".equals(tipo)) {
            if (defesas != 0) {
                zerado = false;
                txt = "Defesas: " + defesas;
            }
        } else {
            if (golsSof != 0) {
                zerado = false;
                txt = "Gols Sofridos: " + golsSof;
            }
        }
        if (!zerado) {
            texto.setText(txt);
            texto.setVisibility(View.VISIBLE);
            layout.setBackgroundResource(defineCorPosicaoDefGolS(tipo));
            layout.setVisibility(View.VISIBLE);
        } else {
            texto.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
        }
    }

    private int defineCorPosicaoDefGolS(String pos) {
        if ("Defesas".equals(pos)) {
            return R.color.Cor_Meia;
        }
        return R.color.Cor_Atacante;
    }

    private void mostrarGraficoCompScoutsDef() {
        if (defesaScouts == 0 && defesaScoutsPass == 0) {
            txtSDefComp.setVisibility(View.VISIBLE);
            lytTipoSDefMes.setVisibility(View.GONE);
            barChartSDef1.setVisibility(View.GONE);
            barChartSDef2.setVisibility(View.GONE);
            barChartSDef3.setVisibility(View.GONE);
            barChartSDef4.setVisibility(View.GONE);
            barChartSDef5.setVisibility(View.GONE);
            return;
        } else {
            txtSDefComp.setVisibility(View.GONE);
            lytTipoSDefMes.setVisibility(View.VISIBLE);
            barChartSDef1.setVisibility(View.VISIBLE);
            barChartSDef2.setVisibility(View.VISIBLE);
            barChartSDef3.setVisibility(View.VISIBLE);
            barChartSDef4.setVisibility(View.VISIBLE);
            barChartSDef5.setVisibility(View.VISIBLE);
        }

        Legend l = barChartSDef1.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.add(new BarEntry(0,
                new float[]{defesaScoutsPass, defesaScouts}));
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        yVals2.add(new BarEntry(0,
                new float[]{desarmesPass, desarmes}));
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        yVals3.add(new BarEntry(0,
                new float[]{cortesPass, cortes}));
        ArrayList<BarEntry> yVals4 = new ArrayList<BarEntry>();
        yVals4.add(new BarEntry(0,
                new float[]{bloqueiosPass, bloqueios}));
        ArrayList<BarEntry> yVals5 = new ArrayList<BarEntry>();
        yVals5.add(new BarEntry(0,
                new float[]{errosPass, erros}));

        configChartCompSimples(barChartSDef1, yVals1);
        configChartCompSimples(barChartSDef2, yVals2);
        configChartCompSimples(barChartSDef3, yVals3);
        configChartCompSimples(barChartSDef4, yVals4);
        configChartCompSimples(barChartSDef5, yVals5);
        barChartSDef1.getLegend().setEnabled(true);
    }

    private void configChartCompSimples(HorizontalBarChart chart, ArrayList<BarEntry> yVals) {
        // change the position of the y-labels
        YAxis leftAxis = chart.getAxisLeft();
        //leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setDrawGridLines(false);
        leftAxis.setEnabled(false);

        XAxis xLabels = chart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setEnabled(false);
        xLabels.setDrawGridLines(false);

        BarDataSet set1;
        set1 = new BarDataSet(yVals, "");
        set1.setDrawIcons(false);
        set1.setColors(getColorsComp());
        set1.setStackLabels(new String[]{"Mês passado", "Este mês"});

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0)
                    return "";
                else
                    return "" + (int) value;
            }
        });
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(12f);

        chart.setData(data);
        chart.getLegend().setEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawValueAboveBar(false);
        chart.getAxisRight().setEnabled(false);
        chart.setFitBars(true);
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setHighlightFullBarEnabled(false);
        chart.setClickable(false);
        chart.setHighlightPerTapEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setHighlightPerDragEnabled(false);
        chart.animateX(1000);
        chart.animateY(1000);
        chart.invalidate();
    }

    private int[] getColorsComp() {
        // have as many colors as stack-values per entry
        int[] colors = new int[2];
        System.arraycopy(cores, 0, colors, 0, 2);
        return colors;
    }

    public final int[] cores = {
            rgb("#2196F3"), rgb("#64AF30")
    };

    private void mostrarGraficoScoutsDefensivos() {
        if (defesaScouts == 0) {
            lytGrafSDef.setVisibility(View.GONE);
            pieChartSDef.setVisibility(View.GONE);
            return;
        } else {
            lytGrafSDef.setVisibility(View.VISIBLE);
            pieChartSDef.setVisibility(View.VISIBLE);
        }

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "";
        String[] ordem = new String[]{"Erros", "Bloq", "Cortes", "Desarmes"};

        //initializing data
        Map<String, Integer> typeAmountMap = new HashMap<>();
        ArrayList<Integer> colors = new ArrayList<>();

        typeAmountMap.put(ordem[3], desarmes);
        typeAmountMap.put(ordem[2], cortes);
        typeAmountMap.put(ordem[1], bloqueios);
        typeAmountMap.put(ordem[0], erros);

        colors.add(Color.parseColor("#2196F3")); // Amarelo
        colors.add(Color.parseColor("#64AF30")); // Verde
        colors.add(Color.parseColor("#ED0000")); // Azul
        colors.add(Color.parseColor("#FFEB3B")); // Vermelho

        defineOrdemScoutsDef(ordem, 4);
        printarOrdemScoutDef(ordem);

        //input data and fit data into pie chart entry
        for (String type : typeAmountMap.keySet())
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));

        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries, label);
        pieDataSet.setValueTextSize(12f); //setting text size of the value
        pieDataSet.setColors(colors); //providing color list for coloring different entries

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getPieLabel(float value, PieEntry pieEntry) {
                int v = Math.round(value);
                if (v == 0) {
                    pieEntry.setLabel("");
                    return "";
                } else
                    return "" + v;
            }
        });
        pieData.setDrawValues(true);
        pieData.setValueTextSize(16f);

        pieChartSDef.setData(pieData);
        pieChartSDef.setCenterText("Tipos\n%");
        pieChartSDef.setDrawCenterText(true);
        pieChartSDef.setCenterTextSize(16f);
        pieChartSDef.setDrawEntryLabels(false);
        pieChartSDef.getLegend().setEnabled(false);
        pieChartSDef.getDescription().setEnabled(false);
        pieChartSDef.setRotationEnabled(false);
        pieChartSDef.setUsePercentValues(true);
        pieChartSDef.setEntryLabelColor(R.color.black);
        pieChartSDef.animateX(1000);
        pieChartSDef.animateY(1000);
        pieChartSDef.invalidate();
    }

    private void defineOrdemScoutsDef(String[] arr, int n) {
        if (n == 1)
            return;

        for (int i = 0; i < n - 1; i++) {
            switch (arr[i]) {
                case "Desarmes":
                    if (arr[i + 1].equals("Cortes")) {
                        if (desarmes >= cortes) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else if (arr[i + 1].equals("Bloq")) {
                        if (desarmes >= bloqueios) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else {
                        if (desarmes >= erros) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    }
                    break;
                case "Cortes":
                    if (arr[i + 1].equals("Desarmes")) {
                        if (cortes > desarmes) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else if (arr[i + 1].equals("Bloq")) {
                        if (cortes >= bloqueios) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else {
                        if (cortes >= erros) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    }
                    break;
                case "Bloq":
                    if (arr[i + 1].equals("Desarmes")) {
                        if (bloqueios > desarmes) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else if (arr[i + 1].equals("Cortes")) {
                        if (bloqueios > cortes) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else {
                        if (bloqueios >= erros) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    }
                    break;
                default:
                    if (arr[i + 1].equals("Desarmes")) {
                        if (erros > desarmes) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else if (arr[i + 1].equals("Cortes")) {
                        if (erros > cortes) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else {
                        if (erros > bloqueios) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    }
                    break;
            }
        }
        defineOrdemScoutsDef(arr, n - 1);
    }

    private void printarOrdemScoutDef(String[] arr) {
        printOrdemScoutDef(lytQtdSDef4, txtQtdSDef4, arr[0]);
        printOrdemScoutDef(lytQtdSDef3, txtQtdSDef3, arr[1]);
        printOrdemScoutDef(lytQtdSDef2, txtQtdSDef2, arr[2]);
        printOrdemScoutDef(lytQtdSDef1, txtQtdSDef1, arr[3]);
    }

    private void printOrdemScoutDef(LinearLayout layout, TextView texto, String tipo) {
        String txt = null;
        boolean zerado = true;
        if (tipo.equals("Desarmes")) {
            if (desarmes != 0) {
                zerado = false;
                txt = "Desarmes: " + desarmes;
            }
        } else if (tipo.equals("Cortes")) {
            if (cortes != 0) {
                zerado = false;
                txt = "Cortes: " + cortes;
            }
        } else if (tipo.equals("Bloq")) {
            if (bloqueios != 0) {
                zerado = false;
                txt = "Bloq: " + bloqueios;
            }
        } else {
            if (erros != 0) {
                zerado = false;
                txt = "Erros: " + erros;
            }
        }
        if (!zerado) {
            texto.setText(txt);
            texto.setVisibility(View.VISIBLE);
            layout.setBackgroundResource(defineCorPosicaoScoutsDef(tipo));
            layout.setVisibility(View.VISIBLE);
        } else {
            texto.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
        }
    }

    private int defineCorPosicaoScoutsDef(String pos) {
        switch (pos) {
            case "Cortes":
                return R.color.Cor_Goleiro;
            case "Bloq":
                return R.color.Cor_Defensor;
            case "Desarmes":
                return R.color.Cor_Meia;
            default:
                return R.color.Cor_Atacante;
        }
    }

    private void somaStats() {
        String data = "";
        if (listaPartida.size() > 0)
            for (Partida partida : listaPartida) {
                desarmes += partida.getDesarmes();
                cortes += partida.getCortes();
                bloqueios += partida.getBloqueios();
                erros += partida.getErros();
                defesas += partida.getDefesas();
                golsSof += partida.getGolsSofridos();
                vitorias += partida.getVitorias();
                empates += partida.getEmpates();
                derrotas += partida.getDerrotas();
                minutos += partida.getDuracao() *
                        (partida.getVitorias() + partida.getEmpates() + partida.getDerrotas());
                penDefendidos += partida.getDefesasPen();
                penLevados += partida.getGolsSofridosPen();
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
        jogos = vitorias + empates + derrotas;
        defesaScouts = desarmes + cortes + bloqueios + erros;
        penEncarados = penDefendidos + penLevados;
        txtScoutDef.setText(String.valueOf(defesaScouts));
        txtPenEnc.setText(String.valueOf(penEncarados));
        txtFinCertas.setText(String.valueOf(defesas + golsSof));

        if (defesaScouts > 0) {
            lytGrafSDef.setVisibility(View.VISIBLE);
            pieChartSDef.setVisibility(View.VISIBLE);
            calculaMediasDesarmes();
            if (cortes + bloqueios + erros > 0) {
                lytMedScoutsDef2.setVisibility(View.VISIBLE);
                calculaMediasCortes();
                calculaMediasBloqueios();
                calculaMediasErros();
            } else {
                lytMedScoutsDef2.setVisibility(View.GONE);
            }
        } else {
            lytGrafSDef.setVisibility(View.GONE);
            pieChartSDef.setVisibility(View.GONE);
        }
        if (defesas + golsSof > 0) {
            lytGrafDefGolS.setVisibility(View.VISIBLE);
            calculaMediasDefesas();
            calculaMediasGolSof();
        } else {
            lytGrafDefGolS.setVisibility(View.GONE);
        }
        calculaMediasPenaltis();

    }

    private void somaStatsPassados() {
        String data = "";
        for (Partida partida : listaPartidaPass) {
            desarmesPass += partida.getDesarmes();
            cortesPass += partida.getCortes();
            bloqueiosPass += partida.getBloqueios();
            errosPass += partida.getErros();
            defesasPass += partida.getDefesas();
            golsSofPass += partida.getGolsSofridos();
            vitoriasPass += partida.getVitorias();
            empatesPass += partida.getEmpates();
            derrotasPass += partida.getDerrotas();
            minutosPass += partida.getDuracao() *
                    (partida.getVitorias() + partida.getEmpates() + partida.getDerrotas());
            penDefendidosPass += partida.getDefesasPen();
            penLevadosPass += partida.getGolsSofridosPen();
            diasPass++;
            if (partida.getTipoRegistro().equals("Partida Única"))
                if (!data.isEmpty())
                    if (data.equals(partida.getData().substring(0, 10)))
                        diasPass--;
                    else
                        data = partida.getData().substring(0, 10);
        }
        jogosPass = vitoriasPass + empatesPass + derrotasPass;
        defesaScoutsPass = desarmesPass + cortesPass + bloqueiosPass + errosPass;
        penEncaradosPass = penDefendidosPass + penLevadosPass;
    }

    private void calculaMediasDesarmes() {
        long med = 0;
        if (desarmes > 0) {
            lytMedDesarmes.setVisibility(View.VISIBLE);
            medMinuto = (double) minutos / desarmes;
            med = Math.round(medMinuto);
            if (med > 1) {
                txtDesMinuto.setText("1 desarme a cada " + med + "min");
            } else if (med == 1) {
                txtDesMinuto.setText(med + " desarme por min");
            } else {
                medMinuto = (double) desarmes / minutos;
                med = Math.round(medMinuto);
                txtDesMinuto.setText(med + " desarmes por min");
            }
            medJogo = (double) jogos / desarmes;
            med = Math.round(medJogo);
            if (med > 1) {
                txtDesJogo.setText("1 desarme a cada " + med + " jogos");
            } else if (med == 1) {
                txtDesJogo.setText(med + " desarme por jogo");
            } else {
                medJogo = (double) desarmes / jogos;
                med = Math.round(medJogo);
                txtDesJogo.setText(med + " desarmes por jogo");
            }
            medDia = (double) desarmes / dias;
            med = Math.round(medDia);
            if (med > 1) {
                txtDesDia.setText(med + " desarmes por dia");
            } else if (med == 1) {
                txtDesDia.setText(med + " desarme por dia");
            } else {
                medDia = (double) dias / desarmes;
                med = Math.round(medDia);
                txtDesDia.setText("1 desarme a cada " + med + " dias");
            }

        } else {
            lytMedDesarmes.setVisibility(View.GONE);
        }

    }

    private void calculaMediasCortes() {
        long med = 0;
        if (cortes > 0) {
            lytMedCortes.setVisibility(View.VISIBLE);
            medMinuto = (double) minutos / cortes;
            med = Math.round(medMinuto);
            if (med > 1) {
                txtCorMinuto.setText("1 corte a cada\n" + med + "min");
            } else if (med == 1) {
                txtCorMinuto.setText(med + " corte por min");
            } else {
                medMinuto = (double) cortes / minutos;
                med = Math.round(medMinuto);
                txtCorMinuto.setText(med + " cortes por min");
            }
            medJogo = (double) jogos / cortes;
            med = Math.round(medJogo);
            if (med > 1) {
                txtCorJogo.setText("1 corte a cada\n" + med + " jogos");
            } else if (med == 1) {
                txtCorJogo.setText(med + " corte por jogo");
            } else {
                medJogo = (double) cortes / jogos;
                med = Math.round(medJogo);
                txtCorJogo.setText(med + " cortes por jogo");
            }
            medDia = (double) cortes / dias;
            med = Math.round(medDia);
            if (med > 1) {
                txtCorDia.setText(med + " cortes por dia");
            } else if (med == 1) {
                txtCorDia.setText(med + " corte por dia");
            } else {
                medDia = (double) dias / cortes;
                med = Math.round(medDia);
                txtCorDia.setText("1 corte a cada\n" + med + " dias");
            }

        } else {
            lytMedCortes.setVisibility(View.GONE);
        }
    }

    private void calculaMediasBloqueios() {
        long med = 0;
        if (bloqueios > 0) {
            lytMedBloq.setVisibility(View.VISIBLE);
            medMinuto = (double) minutos / bloqueios;
            med = Math.round(medMinuto);
            if (med > 1) {
                txtBloqMinuto.setText("1 bloq a cada\n" + med + "min");
            } else if (med == 1) {
                txtBloqMinuto.setText(med + " bloq por min");
            } else {
                medMinuto = (double) bloqueios / minutos;
                med = Math.round(medMinuto);
                txtBloqMinuto.setText(med + " bloqs por min");
            }
            medJogo = (double) jogos / bloqueios;
            med = Math.round(medJogo);
            if (med > 1) {
                txtBloqJogo.setText("1 bloq a cada\n" + med + " jogos");
            } else if (med == 1) {
                txtBloqJogo.setText(med + " bloq por jogo");
            } else {
                medJogo = (double) bloqueios / jogos;
                med = Math.round(medJogo);
                txtBloqJogo.setText(med + " bloqs por jogo");
            }
            medDia = (double) bloqueios / dias;
            med = Math.round(medDia);
            if (med > 1) {
                txtBloqDia.setText(med + " bloqs por dia");
            } else if (med == 1) {
                txtBloqDia.setText(med + " bloq por dia");
            } else {
                medDia = (double) dias / bloqueios;
                med = Math.round(medDia);
                txtBloqDia.setText("1 bloq a cada\n" + med + " dias");
            }

        } else {
            lytMedBloq.setVisibility(View.GONE);
        }
    }

    private void calculaMediasErros() {
        long med = 0;
        if (erros > 0) {
            lytMedErros.setVisibility(View.VISIBLE);
            medMinuto = (double) minutos / erros;
            med = Math.round(medMinuto);
            if (med > 1) {
                txtErrosMinuto.setText("1 erro a cada\n" + med + "min");
            } else if (med == 1) {
                txtErrosMinuto.setText(med + " erro por min");
            } else {
                medMinuto = (double) erros / minutos;
                med = Math.round(medMinuto);
                txtErrosMinuto.setText(med + " erros por min");
            }
            medJogo = (double) jogos / erros;
            med = Math.round(medJogo);
            if (med > 1) {
                txtErrosJogo.setText("1 erro a cada\n" + med + " jogos");
            } else if (med == 1) {
                txtErrosJogo.setText(med + " erro por jogo");
            } else {
                medJogo = (double) erros / jogos;
                med = Math.round(medJogo);
                txtErrosJogo.setText(med + " erros por jogo");
            }
            medDia = (double) erros / dias;
            med = Math.round(medDia);
            if (med > 1) {
                txtErrosDia.setText(med + " erros por dia");
            } else if (med == 1) {
                txtErrosDia.setText(med + " erro por dia");
            } else {
                medDia = (double) dias / erros;
                med = Math.round(medDia);
                txtErrosDia.setText("1 erro a cada\n" + med + " dias");
            }

        } else {
            lytMedErros.setVisibility(View.GONE);
        }
    }

    private void calculaMediasDefesas() {
        long med = 0;
        if (defesas > 0) {
            lytMedDefesas.setVisibility(View.VISIBLE);
            medMinuto = (double) minutos / defesas;
            med = Math.round(medMinuto);
            if (med > 1) {
                txtDefMinuto.setText("1 defesa a cada " + med + "min");
            } else if (med == 1) {
                txtDefMinuto.setText(med + " defesa por min");
            } else {
                medMinuto = (double) defesas / minutos;
                med = Math.round(medMinuto);
                txtDefMinuto.setText(med + " defesas por min");
            }
            medJogo = (double) jogos / defesas;
            med = Math.round(medJogo);
            if (med > 1) {
                txtDefJogo.setText("1 defesa a cada " + med + " jogos");
            } else if (med == 1) {
                txtDefJogo.setText(med + " defesa por jogo");
            } else {
                medJogo = (double) defesas / jogos;
                med = Math.round(medJogo);
                txtDefJogo.setText(med + " defesas por jogo");
            }
            medDia = (double) defesas / dias;
            med = Math.round(medDia);
            if (med > 1) {
                txtDefDia.setText(med + " defesas por dia");
            } else if (med == 1) {
                txtDefDia.setText(med + " defesa por dia");
            } else {
                medDia = (double) dias / defesas;
                med = Math.round(medDia);
                txtDefDia.setText("1 defesa a cada " + med + " dias");
            }

        } else {
            lytMedDefesas.setVisibility(View.GONE);
        }
    }

    private void calculaMediasGolSof() {
        long med = 0;
        if (golsSof > 0) {
            lytMedGolSof.setVisibility(View.VISIBLE);
            medMinuto = (double) minutos / golsSof;
            med = Math.round(medMinuto);
            if (med > 1) {
                txtGolSMinuto.setText("1 gol a cada " + med + "min");
            } else if (med == 1) {
                txtGolSMinuto.setText(med + " gol por min");
            } else {
                medMinuto = (double) golsSof / minutos;
                med = Math.round(medMinuto);
                txtGolSMinuto.setText(med + " gols por min");
            }
            medJogo = (double) jogos / golsSof;
            med = Math.round(medJogo);
            if (med > 1) {
                txtGolSJogo.setText("1 gol a cada " + med + " jogos");
            } else if (med == 1) {
                txtGolSJogo.setText(med + " gol por jogo");
            } else {
                medJogo = (double) golsSof / jogos;
                med = Math.round(medJogo);
                txtGolSJogo.setText(med + " gols por jogo");
            }
            medDia = (double) golsSof / dias;
            med = Math.round(medDia);
            if (med > 1) {
                txtGolSDia.setText(med + " gols por dia");
            } else if (med == 1) {
                txtGolSDia.setText(med + " gol por dia");
            } else {
                medDia = (double) dias / golsSof;
                med = Math.round(medDia);
                txtGolSDia.setText("1 gol a cada " + med + " dias");
            }

        } else {
            lytMedGolSof.setVisibility(View.GONE);
        }
    }

    private void calculaMediasPenaltis() {
        long med = 0;
        if (penEncarados > 0) {
            lytGrafPenEnc.setVisibility(View.VISIBLE);
            medMinuto = (double) minutos / penEncarados;
            med = Math.round(medMinuto);
            if (med > 1) {
                txtPenMinuto.setText("1 pênalti a cada " + med + "min");
            } else if (med == 1) {
                txtPenMinuto.setText(med + " pênalti por min");
            } else {
                medMinuto = (double) penEncarados / minutos;
                med = Math.round(medMinuto);
                txtPenMinuto.setText(med + " pênaltis por min");
            }
            medJogo = (double) jogos / penEncarados;
            med = Math.round(medJogo);
            if (med > 1) {
                txtPenJogo.setText("1 pênalti a cada " + med + " jogos");
            } else if (med == 1) {
                txtPenJogo.setText(med + " pênalti por jogo");
            } else {
                medJogo = (double) penEncarados / jogos;
                med = Math.round(medJogo);
                txtPenJogo.setText(med + " pênaltis por jogo");
            }
            medDia = (double) penEncarados / dias;
            med = Math.round(medDia);
            if (med > 1) {
                txtPenDia.setText(med + " pênaltis por dia");
            } else if (med == 1) {
                txtPenDia.setText(med + " pênalti por dia");
            } else {
                medDia = (double) dias / penEncarados;
                med = Math.round(medDia);
                txtPenDia.setText("1 pênalti a cada " + med + " dias");
            }

        } else {
            lytGrafPenEnc.setVisibility(View.GONE);
        }
    }

    private void resetarStats() {
        defesaScouts = 0;
        desarmes = 0;
        cortes = 0;
        bloqueios = 0;
        erros = 0;
        vitorias = 0;
        empates = 0;
        derrotas = 0;
        minutos = 0;
        jogos = 0;
        defesas = 0;
        penEncarados = 0;
        penDefendidos = 0;
        penLevados = 0;
        golsSof = 0;
        dias = 0;

        defesaScoutsPass = 0;
        desarmesPass = 0;
        cortesPass = 0;
        bloqueiosPass = 0;
        errosPass = 0;
        vitoriasPass = 0;
        empatesPass = 0;
        derrotasPass = 0;
        minutosPass = 0;
        jogosPass = 0;
        defesasPass = 0;
        penEncaradosPass = 0;
        penDefendidosPass = 0;
        penLevadosPass = 0;
        golsSofPass = 0;
        diasPass = 0;

        txtScoutDef.setText(String.valueOf(defesaScouts));
        txtPenEnc.setText(String.valueOf(penEncarados));
        txtFinCertas.setText(String.valueOf(defesas + golsSof));
    }

    private void inicializarComponentes(View view) {
        txtScoutDef = view.findViewById(R.id.txt_relatorio_scoutsDefesa);
        pieChartSDef = view.findViewById(R.id.pieChart_relatorio_scoutsDef);
        lytQtdScoutsDef = view.findViewById(R.id.layout_relatorio_qtdScoutsDef);
        lytGrafSDef = view.findViewById(R.id.layout_relatorio_grafScoutDef);
        lytQtdSDef1 = view.findViewById(R.id.layout_relatorio_sdef1);
        lytQtdSDef2 = view.findViewById(R.id.layout_relatorio_sdef2);
        lytQtdSDef3 = view.findViewById(R.id.layout_relatorio_sdef3);
        lytQtdSDef4 = view.findViewById(R.id.layout_relatorio_sdef4);
        txtQtdSDef1 = view.findViewById(R.id.txt_relatorio_sdef1);
        txtQtdSDef2 = view.findViewById(R.id.txt_relatorio_sdef2);
        txtQtdSDef3 = view.findViewById(R.id.txt_relatorio_sdef3);
        txtQtdSDef4 = view.findViewById(R.id.txt_relatorio_sdef4);
        txtDesMinuto = view.findViewById(R.id.txt_relatorio_desMinuto);
        txtDesJogo = view.findViewById(R.id.txt_relatorio_desJogo);
        txtDesDia = view.findViewById(R.id.txt_relatorio_desDia);
        lytMedScoutsDef2 = view.findViewById(R.id.layout_relatorio_medias2);
        lytMedCortes = view.findViewById(R.id.layout_relatorio_mediasCortes);
        lytMedDesarmes = view.findViewById(R.id.layout_relatorio_medias1);
        txtCorMinuto = view.findViewById(R.id.txt_relatorio_corMinuto);
        txtCorJogo = view.findViewById(R.id.txt_relatorio_corJogo);
        txtCorDia = view.findViewById(R.id.txt_relatorio_corDia);
        lytMedBloq = view.findViewById(R.id.layout_relatorio_mediasBloqueios);
        txtBloqMinuto = view.findViewById(R.id.txt_relatorio_bloqMinuto);
        txtBloqJogo = view.findViewById(R.id.txt_relatorio_bloqJogo);
        txtBloqDia = view.findViewById(R.id.txt_relatorio_bloqDia);
        lytMedErros = view.findViewById(R.id.layout_relatorio_mediasErros);
        txtErrosMinuto = view.findViewById(R.id.txt_relatorio_errMinuto);
        txtErrosJogo = view.findViewById(R.id.txt_relatorio_errJogo);
        txtErrosDia = view.findViewById(R.id.txt_relatorio_errDia);
        lytTipoSDefMes = view.findViewById(R.id.layout_relatorio_tipoSDefMes);
        barChartSDef1 = view.findViewById(R.id.barChart_relatorio_sDefMes1);
        barChartSDef2 = view.findViewById(R.id.barChart_relatorio_sDefMes2);
        barChartSDef3 = view.findViewById(R.id.barChart_relatorio_sDefMes3);
        barChartSDef4 = view.findViewById(R.id.barChart_relatorio_sDefMes4);
        barChartSDef5 = view.findViewById(R.id.barChart_relatorio_sDefMes5);
        txtSDefComp = view.findViewById(R.id.txt_relatorio_scoutsDefComp);

        lytGrafDefGolS = view.findViewById(R.id.layout_relatorio_qtdGrafDefGolS);
        barChartDefGol = view.findViewById(R.id.barChart_relatorio_defGolSof);
        lytDefGolS1 = view.findViewById(R.id.layout_relatorio_gol1);
        lytDefGolS2 = view.findViewById(R.id.layout_relatorio_gol2);
        txtFinCertas = view.findViewById(R.id.txt_relatorio_finCertasEncaradas);
        txtDefGolS1 = view.findViewById(R.id.txt_relatorio_gol1);
        txtDefGolS2 = view.findViewById(R.id.txt_relatorio_gol2);
        lytMedDefesas = view.findViewById(R.id.layout_relatorio_mediasDefesas);
        txtDefMinuto = view.findViewById(R.id.txt_relatorio_defMinuto);
        txtDefJogo = view.findViewById(R.id.txt_relatorio_defJogo);
        txtDefDia = view.findViewById(R.id.txt_relatorio_defDia);
        lytMedGolSof = view.findViewById(R.id.layout_relatorio_mediasGolSof);
        txtGolSMinuto = view.findViewById(R.id.txt_relatorio_gSofMinuto);
        txtGolSJogo = view.findViewById(R.id.txt_relatorio_gSofJogo);
        txtGolSDia = view.findViewById(R.id.txt_relatorio_gSofDia);
        txtDefGolSComp = view.findViewById(R.id.txt_relatorio_defGolComp);
        lytTipoDefGolS = view.findViewById(R.id.layout_relatorio_defGolMes);
        barChartDefesas = view.findViewById(R.id.barChart_relatorio_defesas);
        barChartGolSof = view.findViewById(R.id.barChart_relatorio_golSof);
        barChartFinCertas = view.findViewById(R.id.barChart_relatorio_finCertas);

        txtPenEnc = view.findViewById(R.id.txt_relatorio_penEncarados);
        lytGrafPenEnc = view.findViewById(R.id.layout_relatorio_penEncQtdMedias);
        barChartPenEnc = view.findViewById(R.id.barChart_relatorio_penEncarados);
        lytPenEnc1 = view.findViewById(R.id.layout_relatorio_pen1);
        lytPenEnc2 = view.findViewById(R.id.layout_relatorio_pen2);
        txtPenEnc1 = view.findViewById(R.id.txt_relatorio_pen1);
        txtPenEnc2 = view.findViewById(R.id.txt_relatorio_pen2);
        txtPenMinuto = view.findViewById(R.id.txt_relatorio_penEncaradosMinuto);
        txtPenJogo = view.findViewById(R.id.txt_relatorio_penEncaradosJogo);
        txtPenDia = view.findViewById(R.id.txt_relatorio_penEncaradosDia);
        txtPenEncComp = view.findViewById(R.id.txt_relatorio_penCobComp);
        lytTipoPenEncMes = view.findViewById(R.id.layout_relatorio_penTiposMes);
        barChartPenEnc1 = view.findViewById(R.id.barChart_relatorio_penCobMes1);
        barChartPenEnc2 = view.findViewById(R.id.barChart_relatorio_penCobMes2);
        barChartPenEnc3 = view.findViewById(R.id.barChart_relatorio_penCobMes3);
    }
}