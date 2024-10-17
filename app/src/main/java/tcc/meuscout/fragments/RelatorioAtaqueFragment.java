package tcc.meuscout.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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
import tcc.meuscout.model.Ranking;
import tcc.meuscout.model.Usuario;
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.BaseFragment;
import tcc.meuscout.util.Constantes;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class RelatorioAtaqueFragment extends BaseFragment {

    private static RelatorioAtaqueFragment instance;
    private PieChart pieChartGols, pieChartFinal;
    private HorizontalBarChart barChartGolsMes, barChartFinal1, barChartFinal2,
            barChartFinal3, barChartFinal4, barChartFinal5,
            barChartPenCob, barChartPenCob1, barChartPenCob2, barChartPenCob3,
            barChartAssistComp;
    private static List<Partida> listaPartida = new ArrayList<>(), listaPartidaPass = new ArrayList<>();
    private static List<Ranking> listaRankATA = new ArrayList<>(), listaRankMEI = new ArrayList<>(),
            listaRankDEF = new ArrayList<>(), listaRankGOL = new ArrayList<>();
    private int golsFeitos, golsPD, golsPE, golsCA, golsOU,
            finalizacoes, finalCer, finalErr, finalBloq, finalTrave,
            assists, penCobrados, penCertos, penErrados,
            vitorias, empates, derrotas, minutos, jogos, dias;
    private int golsFeitosPass, golsPDPass, golsPEPass, golsCAPass, golsOUPass,
            finalizacoesPass, finalCerPass, finalErrPass, finalBloqPass, finalTravePass,
            assistsPass, penCobradosPass, penCertosPass, penErradosPass,
            vitoriasPass, empatesPass, derrotasPass, minutosPass, jogosPass, diasPass;
    private double medMinuto, medJogo, medDia;
    private boolean carregou;
    private TextView txtGols, txtGol1, txtGol2, txtGol3, txtGol4,
            txtGolMinuto, txtGolJogos, txtGolDia, txtGolsComp,
            txtFinalizacoes, txtFin1, txtFin2, txtFin3, txtFin4,
            txtFinalMinuto, txtFinalJogos, txtFinalDia, txtFinalComp,
            txtPenCob, txtPenCobComp, txtPenCobJogos, txtPenCobDia, txtPenCobMinuto,
            txtPen1, txtPen2,
            txtAssist, txtAssistMinuto, txtAssistJogos, txtAssistDia, txtAssistComp;
    private LinearLayout lytGol1, lytGol2, lytGol3, lytGol4, lytTiposGol,
            lytFin1, lytFin2, lytFin3, lytFin4, lytTiposFin, lytLabelTpFinalMes,
            lytGrafPenCob, lytGrafPenCobComp, lytPenCobMedias, lytPen1, lytPen2,
            lytAssistMedias;
    private Usuario usuario;

    public static RelatorioAtaqueFragment newInstance(List<Partida> lista, List<Partida> listaPassada) {
        instance = new RelatorioAtaqueFragment();
        listaPartida = lista;
        listaPartidaPass = listaPassada;
        return instance;
    }

    public static RelatorioAtaqueFragment getInstance() {
        if (instance == null)
            instance = new RelatorioAtaqueFragment();
        return instance;
    }

    public void setListaPartida(List<Partida> lista, List<Partida> listaPassada) {
        listaPartida = lista;
        listaPartidaPass = listaPassada;
        atualizarGraficos();
    }

    @Override
    protected int getFragmentLayout() throws Exception {
        return R.layout.fragment_relatorio_ataque;
    }

    @Override
    protected void inicializar() throws Exception {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            usuario = ControleBanco.getInstance().recuperaUsuarioLogado(getActivity());
            inicializarComponentes(view);
            atualizarGraficos();
            listaRankMEI = ControleBanco.getInstance().reuperaRankings(getActivity(), "MEI");
            listaRankATA = ControleBanco.getInstance().reuperaRankings(getActivity(), "ATA");
            listaRankDEF = ControleBanco.getInstance().reuperaRankings(getActivity(), "DEF");
            listaRankGOL = ControleBanco.getInstance().reuperaRankings(getActivity(), "GOL");
        } catch (Exception e) {
            Alerta.exibeSnackbarLongo(view, Constantes.MSG_GENERICA_ERRO);
            e.printStackTrace();
        }
    }

    public void atualizarGraficos() {
        resetarStats();
        if (listaPartida.size() > 0)
            somaStats();
        mostrarGraficoGols();
        mostrarGraficoFinalizacoes();
        mostrarGraficoPenCobrados();
        if (listaPartidaPass.size() > 0)
            somaStatsPassados();
        mostrarGraficoCompGols();
        mostrarGraficoCompFinalizacoes();
        mostrarGraficoCompPenCobrados();
        mostrarGraficoCompAssist();
    }

    private void mostrarGraficoCompAssist() {
        if (assistsPass == 0) {
            if (assists == 0) {
                txtAssistComp.setVisibility(View.VISIBLE);
                barChartAssistComp.setVisibility(View.GONE);
                lytAssistMedias.setVisibility(View.GONE);
                return;
            } else {
                txtAssistComp.setVisibility(View.GONE);
                lytAssistMedias.setVisibility(View.VISIBLE);
                barChartAssistComp.setVisibility(View.VISIBLE);
            }
        } else {
            txtAssistComp.setVisibility(View.GONE);
            barChartAssistComp.setVisibility(View.VISIBLE);
            if (assists == 0) {
                lytAssistMedias.setVisibility(View.GONE);
            } else {
                lytAssistMedias.setVisibility(View.VISIBLE);
            }
        }

        Legend l = barChartAssistComp.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.add(new BarEntry(0,
                new float[]{assistsPass, assists}));

        configChartCompSimples(barChartAssistComp, yVals1);
        barChartAssistComp.getLegend().setEnabled(true);
    }

    private void mostrarGraficoPenCobrados() {
        if (penCobrados == 0) {
            barChartPenCob.setVisibility(View.GONE);
            lytGrafPenCob.setVisibility(View.GONE);
            return;
        } else {
            barChartPenCob.setVisibility(View.VISIBLE);
            lytGrafPenCob.setVisibility(View.VISIBLE);
        }

        Legend l = barChartPenCob.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.add(new BarEntry(0,
                new float[]{penErrados, penCertos}));

        String[] ordem = new String[]{"Errados", "Certos"};
        defineOrdemPen(ordem, 2);
        printarOrdemPen(ordem);

        YAxis leftAxis = barChartPenCob.getAxisLeft();
        //leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setDrawGridLines(false);
        leftAxis.setEnabled(false);

        XAxis xLabels = barChartPenCob.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setEnabled(false);
        xLabels.setDrawGridLines(false);

        BarDataSet set1;
        set1 = new BarDataSet(yVals1, "");
        set1.setDrawIcons(false);
        set1.setColors(getColorsPen());
        set1.setStackLabels(new String[]{"Errados", "Certos"});

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                long med = Math.round((value / penCobrados) * 100);
                if (med == 0)
                    return "";
                else
                    return "" + (int) med + "%";
            }
        });
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(12f);

        barChartPenCob.setData(data);
        barChartPenCob.getLegend().setEnabled(false);
        barChartPenCob.setPinchZoom(false);
        barChartPenCob.setDrawValueAboveBar(false);
        barChartPenCob.getAxisRight().setEnabled(false);
        barChartPenCob.setFitBars(true);
        barChartPenCob.getDescription().setEnabled(false);
        barChartPenCob.setDrawGridBackground(false);
        barChartPenCob.setDrawBarShadow(false);
        barChartPenCob.setHighlightFullBarEnabled(false);
        barChartPenCob.setClickable(false);
        barChartPenCob.setHighlightPerTapEnabled(false);
        barChartPenCob.setDoubleTapToZoomEnabled(false);
        barChartPenCob.setHighlightPerDragEnabled(false);
        barChartPenCob.animateX(1000);
        barChartPenCob.animateY(1000);
        barChartPenCob.invalidate();
        barChartPenCob.getLegend().setEnabled(true);
    }

    private void mostrarGraficoCompPenCobrados() {
        if (penCobrados == 0 && penCobradosPass == 0) {
            txtPenCobComp.setVisibility(View.VISIBLE);
            lytGrafPenCobComp.setVisibility(View.GONE);
            barChartPenCob1.setVisibility(View.GONE);
            barChartPenCob2.setVisibility(View.GONE);
            barChartPenCob3.setVisibility(View.GONE);
            return;
        } else {
            txtPenCobComp.setVisibility(View.GONE);
            lytGrafPenCobComp.setVisibility(View.VISIBLE);
            barChartPenCob1.setVisibility(View.VISIBLE);
            barChartPenCob2.setVisibility(View.VISIBLE);
            barChartPenCob3.setVisibility(View.VISIBLE);
        }

        Legend l = barChartPenCob1.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.add(new BarEntry(0,
                new float[]{penCobradosPass, penCobrados}));
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        yVals2.add(new BarEntry(0,
                new float[]{penCertosPass, penCertos}));
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        yVals3.add(new BarEntry(0,
                new float[]{penErradosPass, penErrados}));

        configChartCompSimples(barChartPenCob1, yVals1);
        configChartCompSimples(barChartPenCob2, yVals2);
        configChartCompSimples(barChartPenCob3, yVals3);
        barChartPenCob1.getLegend().setEnabled(true);
    }

    private void mostrarGraficoFinalizacoes() {
        if (finalizacoes == 0) {
            lytTiposFin.setVisibility(View.GONE);
            pieChartFinal.setVisibility(View.GONE);
            return;
        } else {
            lytTiposFin.setVisibility(View.VISIBLE);
            pieChartFinal.setVisibility(View.VISIBLE);
        }

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "";
        String[] ordem = new String[]{"Trave", "Bloq", "Erradas", "Certas"};

        //initializing data
        Map<String, Integer> typeAmountMap = new HashMap<>();
        ArrayList<Integer> colors = new ArrayList<>();

        //if (finalCer != 0) {
        typeAmountMap.put("Certas", finalCer);
        colors.add(Color.parseColor("#ED0000")); // Erradas
        // }
        //  if (finalErr != 0) {
        typeAmountMap.put("Erradas", finalErr);
        colors.add(Color.parseColor("#2196F3")); // Bloq
        // }
        // if (finalBloq != 0) {
        typeAmountMap.put("Bloq", finalBloq);
        colors.add(Color.parseColor("#FFEB3B")); // Trave
        // }
        //  if (finalTrave != 0) {
        typeAmountMap.put("Trave", finalTrave);
        colors.add(Color.parseColor("#64AF30")); // Certas

        // }

        defineOrdemFinal(ordem, 4);
        printarOrdemFinal(ordem);
        //initializing colors for the entries

        //input data and fit data into pie chart entry
        for (String type : typeAmountMap.keySet()) {
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries, label);
        pieDataSet.setValueTextSize(12f); //setting text size of the value
        pieDataSet.setColors(colors); //providing color list for coloring different entries
        //pieDataSet.setValueTextColor(R.color.white);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
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

        pieChartFinal.setData(pieData);
        pieChartFinal.setCenterText("Tipos\n%");
        pieChartFinal.setDrawCenterText(true);
        pieChartFinal.setCenterTextSize(16f);
        pieChartFinal.setDrawEntryLabels(false);
        pieChartFinal.getLegend().setEnabled(false);
        pieChartFinal.getDescription().setEnabled(false);
        pieChartFinal.setRotationEnabled(false);
        pieChartFinal.setUsePercentValues(true);
        pieChartFinal.setEntryLabelColor(R.color.black);
        pieChartFinal.animateX(1000);
        pieChartFinal.animateY(1000);
        pieChartFinal.invalidate();
    }

    private void mostrarGraficoCompFinalizacoes() {
        if (finalizacoes == 0 && finalizacoesPass == 0) {
            txtFinalComp.setVisibility(View.VISIBLE);
            lytLabelTpFinalMes.setVisibility(View.GONE);
            barChartFinal1.setVisibility(View.GONE);
            barChartFinal2.setVisibility(View.GONE);
            barChartFinal3.setVisibility(View.GONE);
            barChartFinal4.setVisibility(View.GONE);
            barChartFinal5.setVisibility(View.GONE);
            return;
        } else {
            txtFinalComp.setVisibility(View.GONE);
            lytLabelTpFinalMes.setVisibility(View.VISIBLE);
            barChartFinal1.setVisibility(View.VISIBLE);
            barChartFinal2.setVisibility(View.VISIBLE);
            barChartFinal3.setVisibility(View.VISIBLE);
            barChartFinal4.setVisibility(View.VISIBLE);
            barChartFinal5.setVisibility(View.VISIBLE);
        }

        Legend l = barChartFinal1.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.add(new BarEntry(0,
                new float[]{finalizacoesPass, finalizacoes}));
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        yVals2.add(new BarEntry(0,
                new float[]{finalCerPass, finalCer}));
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        yVals3.add(new BarEntry(0,
                new float[]{finalErrPass, finalErr}));
        ArrayList<BarEntry> yVals4 = new ArrayList<BarEntry>();
        yVals4.add(new BarEntry(0,
                new float[]{finalBloqPass, finalBloq}));
        ArrayList<BarEntry> yVals5 = new ArrayList<BarEntry>();
        yVals5.add(new BarEntry(0,
                new float[]{finalTravePass, finalTrave}));

        configChartCompSimples(barChartFinal1, yVals1);
        configChartCompSimples(barChartFinal2, yVals2);
        configChartCompSimples(barChartFinal3, yVals3);
        configChartCompSimples(barChartFinal4, yVals4);
        configChartCompSimples(barChartFinal5, yVals5);
        barChartFinal1.getLegend().setEnabled(true);
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
        set1.setColors(getColors());
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

    private void mostrarGraficoGols() {
        if (golsFeitos == 0) {
            lytTiposGol.setVisibility(View.GONE);
            pieChartGols.setVisibility(View.GONE);
            return;
        } else {
            lytTiposGol.setVisibility(View.VISIBLE);
            pieChartGols.setVisibility(View.VISIBLE);
        }

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "";
        String[] ordem = new String[]{"OU", "CA", "PE", "PD"};

        //initializing data
        Map<String, Integer> typeAmountMap = new HashMap<>();
        ArrayList<Integer> colors = new ArrayList<>();

        typeAmountMap.put("PD", golsPD);
        colors.add(Color.parseColor("#64AF30")); // PD

        typeAmountMap.put("PE", golsPE);
        colors.add(Color.parseColor("#2196F3")); // PE

        typeAmountMap.put("CA", golsCA);
        colors.add(Color.parseColor("#ED0000")); // OU

        typeAmountMap.put("OU", golsOU);
        colors.add(Color.parseColor("#FFEB3B")); // CA

        defineOrdemGols(ordem, 4);
        printarOrdemGols(ordem);
        //initializing colors for the entries

        colors.add(Color.parseColor("#476567")); //Toys?
        colors.add(Color.parseColor("#a35567")); //Snacks?
        colors.add(Color.parseColor("#3ca567")); //Clothes?
        colors.add(Color.parseColor("#304567")); //Toys PD
        colors.add(Color.parseColor("#309967")); //Clothes CA

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

        pieChartGols.setData(pieData);
        pieChartGols.setCenterText("Tipos\n%");
        pieChartGols.setDrawCenterText(true);
        pieChartGols.setCenterTextSize(16f);
        pieChartGols.setDrawEntryLabels(false);
        pieChartGols.getLegend().setEnabled(false);
        pieChartGols.getDescription().setEnabled(false);
        pieChartGols.setRotationEnabled(false);
        pieChartGols.setUsePercentValues(true);
        pieChartGols.setEntryLabelColor(R.color.black);
        pieChartGols.animateX(1000);
        pieChartGols.animateY(1000);
        pieChartGols.invalidate();
    }

    private void mostrarGraficoCompGols() {
        if (golsFeitos == 0 && golsFeitosPass == 0) {
            txtGolsComp.setVisibility(View.VISIBLE);
            barChartGolsMes.setVisibility(View.GONE);
            return;
        } else {
            txtGolsComp.setVisibility(View.GONE);
            barChartGolsMes.setVisibility(View.VISIBLE);
        }

        // change the position of the y-labels
        YAxis leftAxis = barChartGolsMes.getAxisLeft();
        //leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setDrawGridLines(false);
        leftAxis.setEnabled(false);

        XAxis xLabels = barChartGolsMes.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setEnabled(false);
        xLabels.setDrawGridLines(false);

        Legend l = barChartGolsMes.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.add(new BarEntry(0,
                new float[]{golsFeitosPass, golsFeitos}));

        BarDataSet set1;

        if (barChartGolsMes.getData() != null &&
                barChartGolsMes.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChartGolsMes.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            barChartGolsMes.getData().notifyDataChanged();
            barChartGolsMes.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "");
            set1.setDrawIcons(false);
            set1.setColors(getColors());
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
            barChartGolsMes.setData(data);
        }

        barChartGolsMes.setPinchZoom(false);
        barChartGolsMes.setDrawValueAboveBar(false);
        barChartGolsMes.getAxisRight().setEnabled(false);
        barChartGolsMes.setFitBars(true);
        barChartGolsMes.getDescription().setEnabled(false);
        barChartGolsMes.setDrawGridBackground(false);
        barChartGolsMes.setDrawBarShadow(false);
        barChartGolsMes.setHighlightFullBarEnabled(false);
        barChartGolsMes.setClickable(false);
        barChartGolsMes.setHighlightPerTapEnabled(false);
        barChartGolsMes.setDoubleTapToZoomEnabled(false);
        barChartGolsMes.setHighlightPerDragEnabled(false);
        barChartGolsMes.animateX(1000);
        barChartGolsMes.animateY(1000);
        barChartGolsMes.invalidate();
    }

    private void printarOrdemPen(String[] arr) {
        printOrdemPen(lytPen2, txtPen2, arr[0]);
        printOrdemPen(lytPen1, txtPen1, arr[1]);
    }

    private void printOrdemPen(LinearLayout layout, TextView texto, String tipo) {
        String txt = null;
        boolean zerado = true;
        if ("Certos".equals(tipo)) {
            if (penCertos != 0) {
                zerado = false;
                txt = "Certos: " + penCertos;
            }
        } else {
            if (penErrados != 0) {
                zerado = false;
                txt = "Errados: " + penErrados;
            }
        }
        if (!zerado) {
            texto.setText(txt);
            texto.setVisibility(View.VISIBLE);
            layout.setBackgroundResource(defineCorPosicaoPen(tipo));
            layout.setVisibility(View.VISIBLE);
        } else {
            texto.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
        }
    }

    private int defineCorPosicaoPen(String pos) {
        if ("Certos".equals(pos)) {
            return R.color.Cor_Meia;
        }
        return R.color.Cor_Atacante;
    }

    private void defineOrdemPen(String[] arr, int n) {
        // Base case
        if (n == 1)
            return;
        // One pass of bubble sort. After
        // this pass, the largest element
        // is moved (or bubbled) to end.
        for (int i = 0; i < n - 1; i++) {
            if ("Certos".equals(arr[i])) {
                if (arr[i + 1].equals("Errados")) {
                    if (penCertos >= penErrados) {
                        String temp = arr[i];
                        arr[i] = arr[i + 1];
                        arr[i + 1] = temp;
                    }
                }
            } else {
                if (arr[i + 1].equals("Certos")) {
                    if (penErrados > penCertos) {
                        String temp = arr[i];
                        arr[i] = arr[i + 1];
                        arr[i + 1] = temp;
                    }
                }
            }
        }
        // Largest element is fixed,
        // recur for remaining array
        defineOrdemPen(arr, n - 1);
    }

    private void printarOrdemFinal(String[] arr) {
        printOrdemFinal(lytFin4, txtFin4, arr[0]);
        printOrdemFinal(lytFin3, txtFin3, arr[1]);
        printOrdemFinal(lytFin2, txtFin2, arr[2]);
        printOrdemFinal(lytFin1, txtFin1, arr[3]);
    }

    private void printOrdemFinal(LinearLayout layout, TextView texto, String tipo) {
        String txt = null;
        boolean zerado = true;
        switch (tipo) {
            case "Certas":
                if (finalCer != 0) {
                    zerado = false;
                    txt = "Certas " + finalCer;
                }
                break;
            case "Erradas":
                if (finalErr != 0) {
                    zerado = false;
                    txt = "Erradas: " + finalErr;
                }
                break;
            case "Bloq":
                if (finalBloq != 0) {
                    zerado = false;
                    txt = "Bloq: " + finalBloq;
                }
                break;
            default:
                if (finalTrave != 0) {
                    zerado = false;
                    txt = "Trave: " + finalTrave;
                }
                break;
        }
        if (!zerado) {
            texto.setText(txt);
            texto.setVisibility(View.VISIBLE);
            layout.setBackgroundResource(defineCorPosicaoFinal(tipo));
            layout.setVisibility(View.VISIBLE);
        } else {
            texto.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
        }
    }

    private int defineCorPosicaoFinal(String pos) {
        switch (pos) {
            case ("Trave"):
                return R.color.Cor_Goleiro;
            case "Bloq":
                return R.color.Cor_Defensor;
            case "Certas":
                return R.color.Cor_Meia;
            default:
                return R.color.Cor_Atacante;
        }
    }

    private void defineOrdemFinal(String[] arr, int n) {
        // Base case
        if (n == 1)
            return;
        // One pass of bubble sort. After
        // this pass, the largest element
        // is moved (or bubbled) to end.
        for (int i = 0; i < n - 1; i++) {
            switch (arr[i]) {
                case "Certas":
                    if (arr[i + 1].equals("Erradas")) {
                        if (finalCer >= finalErr) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else if (arr[i + 1].equals("Bloq")) {
                        if (finalCer >= finalBloq) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else {
                        if (finalCer >= finalTrave) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    }
                    break;
                case "Erradas":
                    if (arr[i + 1].equals("Certas")) {
                        if (finalErr > finalCer) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else if (arr[i + 1].equals("Bloq")) {
                        if (finalErr >= finalBloq) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else {
                        if (finalErr >= finalTrave) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    }
                    break;
                case "Bloq":
                    if (arr[i + 1].equals("Certas")) {
                        if (finalBloq > finalCer) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else if (arr[i + 1].equals("Erradas")) {
                        if (finalBloq > finalErr) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else {
                        if (finalBloq >= finalTrave) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    }
                    break;
                default:
                    if (arr[i + 1].equals("Certas")) {
                        if (finalTrave > finalCer) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else if (arr[i + 1].equals("Erradas")) {
                        if (finalTrave > finalErr) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else {
                        if (finalTrave > finalBloq) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    }
                    break;
            }
        }
        // Largest element is fixed,
        // recur for remaining array
        defineOrdemFinal(arr, n - 1);
    }

    private void printarOrdemGols(String[] arr) {
        printOrdemGols(lytGol4, txtGol4, arr[0]);
        printOrdemGols(lytGol3, txtGol3, arr[1]);
        printOrdemGols(lytGol2, txtGol2, arr[2]);
        printOrdemGols(lytGol1, txtGol1, arr[3]);
    }

    private void printOrdemGols(LinearLayout layout, TextView texto, String tipo) {
        String txt = null;
        boolean zerado = true;
        if (tipo.equals("PD")) {
            if (golsPD != 0) {
                zerado = false;
                txt = "Direita: " + golsPD;
            }
        } else if (tipo.equals("PE")) {
            if (golsPE != 0) {
                zerado = false;
                txt = "Esquerda: " + golsPE;
            }
        } else if (tipo.equals("CA")) {
            if (golsCA != 0) {
                zerado = false;
                txt = "Cabeça: " + golsCA;
            }
        } else {
            if (golsOU != 0) {
                zerado = false;
                txt = "Outro: " + golsOU;
            }
        }
        if (!zerado) {
            texto.setText(txt);
            texto.setVisibility(View.VISIBLE);
            layout.setBackgroundResource(defineCorPosicaoGols(tipo));
            layout.setVisibility(View.VISIBLE);
        } else {
            texto.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
        }
    }

    private int defineCorPosicaoGols(String pos) {
        switch (pos) {
            case ("CA"):
                return R.color.Cor_Goleiro;
            case "PE":
                return R.color.Cor_Defensor;
            case "PD":
                return R.color.Cor_Meia;
            default:
                return R.color.Cor_Atacante;
        }
    }

    private void defineOrdemGols(String[] arr, int n) {
        // Base case
        if (n == 1)
            return;
        // One pass of bubble sort. After
        // this pass, the largest element
        // is moved (or bubbled) to end.
        for (int i = 0; i < n - 1; i++) {
            switch (arr[i]) {
                case "PD":
                    if (arr[i + 1].equals("PE")) {
                        if (golsPD > golsPE) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        } else if (golsPD == golsPE) {
                            if (usuario.getPerna().equals("PD")) {
                                String temp = arr[i];
                                arr[i] = arr[i + 1];
                                arr[i + 1] = temp;
                            }
                        }
                    } else if (arr[i + 1].equals("CA")) {
                        if (golsPD >= golsCA) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else {
                        if (golsPD >= golsOU) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    }
                    break;
                case "PE":
                    if (arr[i + 1].equals("PD")) {
                        if (golsPE > golsPD) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        } else if (golsPE == golsPD) {
                            if (usuario.getPerna().equals("PE")) {
                                String temp = arr[i];
                                arr[i] = arr[i + 1];
                                arr[i + 1] = temp;
                            }
                        }
                    } else if (arr[i + 1].equals("CA")) {
                        if (golsPE >= golsCA) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else {
                        if (golsPE >= golsOU) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    }
                    break;
                case "CA":
                    if (arr[i + 1].equals("PD")) {
                        if (golsCA > golsPD) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else if (arr[i + 1].equals("PE")) {
                        if (golsCA > golsPE) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else {
                        if (golsCA >= golsOU) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    }
                    break;
                default:
                    if (arr[i + 1].equals("PD")) {
                        if (golsOU > golsPD) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else if (arr[i + 1].equals("PE")) {
                        if (golsOU > golsPE) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else {
                        if (golsOU > golsCA) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    }
                    break;
            }
        }
        // Largest element is fixed,
        // recur for remaining array
        defineOrdemGols(arr, n - 1);
    }

    private int[] getColors() {
        // have as many colors as stack-values per entry
        int[] colors = new int[2];
        System.arraycopy(cores, 0, colors, 0, 2);
        return colors;
    }

    public final int[] cores = {
            rgb("#2196F3"), rgb("#64AF30")
    };

    private int[] getColorsPen() {
        // have as many colors as stack-values per entry
        int[] colors = new int[2];
        System.arraycopy(coresPen, 0, colors, 0, 2);
        return colors;
    }

    public final int[] coresPen = {
            rgb("#ED0000"), rgb("#64AF30")
    };

    private void somaStats() {
        String data = "";
        for (Partida partida : listaPartida) {
            golsFeitos += partida.getGolsFeitos();
            golsPD += partida.getGolsPD();
            golsPE += partida.getGolsPE();
            golsCA += partida.getGolsCA();
            golsOU += partida.getGolsOU();
            finalizacoes += partida.getFinalTotal();
            finalCer += partida.getFinalCertas();
            finalErr += partida.getFinalErradas();
            finalBloq += partida.getFinalBloq();
            finalTrave += partida.getFinalTrave();
            vitorias += partida.getVitorias();
            empates += partida.getEmpates();
            derrotas += partida.getDerrotas();
            minutos += partida.getDuracao() *
                    (partida.getVitorias() + partida.getEmpates() + partida.getDerrotas());
            penCertos += partida.getPenaltiAc();
            penErrados += partida.getPenaltiEr();
            assists += partida.getAssistencias();
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
        penCobrados = penCertos + penErrados;
        txtGols.setText(String.valueOf(golsFeitos));
        txtFinalizacoes.setText(String.valueOf(finalizacoes));
        txtPenCob.setText(String.valueOf(penCobrados));
        txtAssist.setText(String.valueOf(assists));
        calculaMediasGols();
        calculaMediasFinalizacoes();
        calculaMediasPenaltis();
        calculaMediasAssist();
    }

    private void somaStatsPassados() {
        String data = "";
        for (Partida partida : listaPartidaPass) {
            golsFeitosPass += partida.getGolsFeitos();
            golsPDPass += partida.getGolsPD();
            golsPEPass += partida.getGolsPE();
            golsCAPass += partida.getGolsCA();
            golsOUPass += partida.getGolsOU();
            finalizacoesPass += partida.getFinalTotal();
            finalCerPass += partida.getFinalCertas();
            finalErrPass += partida.getFinalErradas();
            finalBloqPass += partida.getFinalBloq();
            finalTravePass += partida.getFinalTrave();
            vitoriasPass += partida.getVitorias();
            empatesPass += partida.getEmpates();
            derrotasPass += partida.getDerrotas();
            minutosPass += partida.getDuracao() *
                    (partida.getVitorias() + partida.getEmpates() + partida.getDerrotas());
            penCertosPass += partida.getPenaltiAc();
            penErradosPass += partida.getPenaltiEr();
            assistsPass += partida.getAssistencias();
            diasPass++;
            if (partida.getTipoRegistro().equals("Partida Única"))
                if (!data.isEmpty())
                    if (data.equals(partida.getData().substring(0, 10)))
                        diasPass--;
                    else
                        data = partida.getData().substring(0, 10);
        }
        jogosPass = vitoriasPass + empatesPass + derrotasPass;
        penCobradosPass = penCertosPass + penErradosPass;
    }

    private void calculaMediasGols() {
        long med = 0;
        if (golsFeitos > 0) {
            lytTiposGol.setVisibility(View.VISIBLE);
            medMinuto = (double) minutos / golsFeitos;
            med = Math.round(medMinuto);
            if (med > 1) {
                txtGolMinuto.setText("1 gol a cada " + med + "min");
            } else if (med == 1) {
                txtGolMinuto.setText(med + " gol por min");
            } else {
                medMinuto = (double) golsFeitos / minutos;
                med = Math.round(medMinuto);
                txtGolMinuto.setText(med + " gols por min");
            }
            medJogo = (double) jogos / golsFeitos;
            med = Math.round(medJogo);
            if (med > 1) {
                txtGolJogos.setText("1 gol a cada " + med + " jogos");
            } else if (med == 1) {
                txtGolJogos.setText(med + " gol por jogo");
            } else {
                medJogo = (double) golsFeitos / jogos;
                med = Math.round(medJogo);
                txtGolJogos.setText(med + " gols por jogo");
            }
            medDia = (double) golsFeitos / dias;
            med = Math.round(medDia);
            if (med > 1) {
                txtGolDia.setText(med + " gols por dia");
            } else if (med == 1) {
                txtGolDia.setText(med + " gol por dia");
            } else {
                medDia = (double) dias / golsFeitos;
                med = Math.round(medDia);
                txtGolDia.setText("1 gol a cada " + med + " dias");
            }

        } else {
            lytTiposGol.setVisibility(View.GONE);
        }

    }

    private void calculaMediasFinalizacoes() {
        long med = 0;
        if (finalizacoes > 0) {
            lytTiposFin.setVisibility(View.VISIBLE);
            medMinuto = (double) minutos / finalizacoes;
            med = Math.round(medMinuto);
            if (med > 1) {
                txtFinalMinuto.setText("1 finalização a cada " + med + "min");
            } else if (med == 1) {
                txtFinalMinuto.setText(med + " finalização por min");
            } else {
                medMinuto = (double) finalizacoes / minutos;
                med = Math.round(medMinuto);
                txtFinalMinuto.setText(med + " finalizações por min");
            }
            medJogo = (double) jogos / finalizacoes;
            med = Math.round(medJogo);
            if (med > 1) {
                txtFinalJogos.setText("1 finalização a cada " + med + " jogos");
            } else if (med == 1) {
                txtFinalJogos.setText(med + " finalização por jogo");
            } else {
                medJogo = (double) finalizacoes / jogos;
                med = Math.round(medJogo);
                txtFinalJogos.setText(med + " finalizações por jogo");
            }
            medDia = (double) finalizacoes / dias;
            med = Math.round(medDia);
            if (med > 1) {
                txtFinalDia.setText(med + " finalizações por dia");
            } else if (med == 1) {
                txtFinalDia.setText(med + " finalização por dia");
            } else {
                medDia = (double) dias / finalizacoes;
                med = Math.round(medDia);
                txtFinalDia.setText("1 finalização a cada " + med + " dias");
            }

        } else {
            lytTiposFin.setVisibility(View.GONE);
        }

    }

    private void calculaMediasPenaltis() {
        long med = 0;
        if (penCobrados > 0) {
            lytPenCobMedias.setVisibility(View.VISIBLE);
            medMinuto = (double) minutos / penCobrados;
            med = Math.round(medMinuto);
            if (med > 1) {
                txtPenCobMinuto.setText("1 pênalti a cada " + med + "min");
            } else if (med == 1) {
                txtPenCobMinuto.setText(med + " pênalti por min");
            } else {
                medMinuto = (double) penCobrados / minutos;
                med = Math.round(medMinuto);
                txtPenCobMinuto.setText(med + " pênaltis por min");
            }
            medJogo = (double) jogos / penCobrados;
            med = Math.round(medJogo);
            if (med > 1) {
                txtPenCobJogos.setText("1 pênalti a cada " + med + " jogos");
            } else if (med == 1) {
                txtPenCobJogos.setText(med + " pênalti por jogo");
            } else {
                medJogo = (double) penCobrados / jogos;
                med = Math.round(medJogo);
                txtPenCobJogos.setText(med + " pênaltis por jogo");
            }
            medDia = (double) penCobrados / dias;
            med = Math.round(medDia);
            if (med > 1) {
                txtPenCobDia.setText(med + " pênaltis por dia");
            } else if (med == 1) {
                txtPenCobDia.setText(med + " pênalti por dia");
            } else {
                medDia = (double) dias / penCobrados;
                med = Math.round(medDia);
                txtPenCobDia.setText("1 pênalti a cada " + med + " dias");
            }

        } else {
            lytPenCobMedias.setVisibility(View.GONE);
        }

    }

    private void calculaMediasAssist() {
        long med = 0;
        if (assists > 0) {
            lytAssistMedias.setVisibility(View.VISIBLE);
            medMinuto = (double) minutos / assists;
            med = Math.round(medMinuto);
            if (med > 1) {
                txtAssistMinuto.setText("1 assistência a cada " + med + "min");
            } else if (med == 1) {
                txtAssistMinuto.setText(med + " assistência por min");
            } else {
                medMinuto = (double) assists / minutos;
                med = Math.round(medMinuto);
                txtAssistMinuto.setText(med + " assistências por min");
            }
            medJogo = (double) jogos / assists;
            med = Math.round(medJogo);
            if (med > 1) {
                txtAssistJogos.setText("1 assistência a cada " + med + " jogos");
            } else if (med == 1) {
                txtAssistJogos.setText(med + " assistência por jogo");
            } else {
                medJogo = (double) assists / jogos;
                med = Math.round(medJogo);
                txtAssistJogos.setText(med + " assistências por jogo");
            }
            medDia = (double) assists / dias;
            med = Math.round(medDia);
            if (med > 1) {
                txtAssistDia.setText(med + " assistências por dia");
            } else if (med == 1) {
                txtAssistDia.setText(med + " assistência por dia");
            } else {
                medDia = (double) dias / assists;
                med = Math.round(medDia);
                txtAssistDia.setText("1 assistência a cada " + med + " dias");
            }

        } else {
            lytAssistMedias.setVisibility(View.GONE);
        }

    }

    private void resetarStats() {
        golsFeitos = 0;
        golsPD = 0;
        golsPE = 0;
        golsCA = 0;
        golsOU = 0;
        finalizacoes = 0;
        finalCer = 0;
        finalErr = 0;
        finalBloq = 0;
        finalTrave = 0;
        vitorias = 0;
        empates = 0;
        derrotas = 0;
        minutos = 0;
        jogos = 0;
        assists = 0;
        penCobrados = 0;
        penCertos = 0;
        penErrados = 0;
        golsFeitosPass = 0;
        golsPDPass = 0;
        golsPEPass = 0;
        golsCAPass = 0;
        golsOUPass = 0;
        finalizacoesPass = 0;
        finalCerPass = 0;
        finalErrPass = 0;
        finalBloqPass = 0;
        finalTravePass = 0;
        vitoriasPass = 0;
        empatesPass = 0;
        derrotasPass = 0;
        minutosPass = 0;
        jogosPass = 0;
        penCobradosPass = 0;
        penCertosPass = 0;
        penErradosPass = 0;
        assistsPass = 0;
        dias = 0;
        diasPass = 0;

        txtGols.setText(String.valueOf(golsFeitos));
        txtFinalizacoes.setText(String.valueOf(finalizacoes));
        txtPenCob.setText(String.valueOf(penCobrados));
        txtAssist.setText(String.valueOf(assists));
    }

    private void inicializarComponentes(View view) {
        barChartGolsMes = view.findViewById(R.id.barChart_relatorio_golsMes);
        pieChartGols = view.findViewById(R.id.pieChart_relatorio_gols);
        txtGols = view.findViewById(R.id.txt_relatorio_gols);
        txtGol1 = view.findViewById(R.id.txt_relatorio_gol);
        txtGol2 = view.findViewById(R.id.txt_relatorio_gol2);
        txtGol3 = view.findViewById(R.id.txt_relatorio_gol3);
        txtGol4 = view.findViewById(R.id.txt_relatorio_gol4);
        txtGolsComp = view.findViewById(R.id.txt_relatorio_golsComp);
        lytGol1 = view.findViewById(R.id.layout_relatorio_gol1);
        lytGol2 = view.findViewById(R.id.layout_relatorio_gol2);
        lytGol3 = view.findViewById(R.id.layout_relatorio_gol3);
        lytGol4 = view.findViewById(R.id.layout_relatorio_gol4);
        lytTiposGol = view.findViewById(R.id.layout_relatorio_tiposGol);
        txtGolMinuto = view.findViewById(R.id.txt_relatorio_golMinuto);
        txtGolJogos = view.findViewById(R.id.txt_relatorio_golJogo);
        txtGolDia = view.findViewById(R.id.txt_relatorio_golDia);

        barChartFinal1 = view.findViewById(R.id.barChart_relatorio_sDefMes1);
        barChartFinal2 = view.findViewById(R.id.barChart_relatorio_sDefMes2);
        barChartFinal3 = view.findViewById(R.id.barChart_relatorio_sDefMes3);
        barChartFinal4 = view.findViewById(R.id.barChart_relatorio_sDefMes4);
        barChartFinal5 = view.findViewById(R.id.barChart_relatorio_sDefMes5);
        pieChartFinal = view.findViewById(R.id.pieChart_relatorio_final);
        txtFinalizacoes = view.findViewById(R.id.txt_relatorio_finalizacoes);
        txtFin1 = view.findViewById(R.id.txt_relatorio_final1);
        txtFin2 = view.findViewById(R.id.txt_relatorio_final2);
        txtFin3 = view.findViewById(R.id.txt_relatorio_final3);
        txtFin4 = view.findViewById(R.id.txt_relatorio_final4);
        txtFinalComp = view.findViewById(R.id.txt_relatorio_finalComp);
        lytFin1 = view.findViewById(R.id.layout_relatorio_final1);
        lytFin2 = view.findViewById(R.id.layout_relatorio_final2);
        lytFin3 = view.findViewById(R.id.layout_relatorio_final3);
        lytFin4 = view.findViewById(R.id.layout_relatorio_final4);
        lytTiposFin = view.findViewById(R.id.layout_relatorio_tiposFinalizacoes);
        lytLabelTpFinalMes = view.findViewById(R.id.layout_relatorio_tipoSDefMes);
        txtFinalMinuto = view.findViewById(R.id.txt_relatorio_finalMinuto);
        txtFinalJogos = view.findViewById(R.id.txt_relatorio_finalJogo);
        txtFinalDia = view.findViewById(R.id.txt_relatorio_finalDia);

        barChartPenCob = view.findViewById(R.id.barChart_relatorio_penCobrados);
        barChartPenCob1 = view.findViewById(R.id.barChart_relatorio_penCobMes1);
        barChartPenCob2 = view.findViewById(R.id.barChart_relatorio_penCobMes2);
        barChartPenCob3 = view.findViewById(R.id.barChart_relatorio_penCobMes3);
        txtPenCob = view.findViewById(R.id.txt_relatorio_penCobrados);
        txtPen1 = view.findViewById(R.id.txt_relatorio_pen1);
        txtPen2 = view.findViewById(R.id.txt_relatorio_pen2);
        lytPen1 = view.findViewById(R.id.layout_relatorio_pen1);
        lytPen2 = view.findViewById(R.id.layout_relatorio_pen2);
        txtPenCobComp = view.findViewById(R.id.txt_relatorio_penCobComp);
        lytGrafPenCob = view.findViewById(R.id.layout_relatorio_graficoPenCobrados);
        lytGrafPenCobComp = view.findViewById(R.id.layout_relatorio_graficoPenCobPass);
        lytPenCobMedias = view.findViewById(R.id.layout_relatorio_penCobradoMedias);
        txtPenCobMinuto = view.findViewById(R.id.txt_relatorio_penCobradoMinuto);
        txtPenCobJogos = view.findViewById(R.id.txt_relatorio_penCobradoJogo);
        txtPenCobDia = view.findViewById(R.id.txt_relatorio_penCobradoDia);

        barChartAssistComp = view.findViewById(R.id.barChart_relatorio_assistsComp);
        txtAssist = view.findViewById(R.id.txt_relatorio_assists);
        lytAssistMedias = view.findViewById(R.id.layoutGeral_relatorio_assistsMedias);
        txtAssistMinuto = view.findViewById(R.id.txt_relatorio_assistMinuto);
        txtAssistJogos = view.findViewById(R.id.txt_relatorio_assistJogo);
        txtAssistDia = view.findViewById(R.id.txt_relatorio_assistDia);
        txtAssistComp = view.findViewById(R.id.txt_relatorio_assistComp);
    }
}