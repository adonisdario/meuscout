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
import tcc.meuscout.model.Usuario;
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.BaseFragment;
import tcc.meuscout.util.Constantes;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class RelatorioDisciplinaFragment extends BaseFragment {

    private static RelatorioDisciplinaFragment instance;
    private PieChart pieChartCartoes;
    private HorizontalBarChart barChartFaltas, barChartFal1, barChartFal2, barChartFal3,
            barChartCar1, barChartCar2, barChartCar3, barChartCar4;
    private static List<Partida> listaPartida = new ArrayList<>(),
            listaPartidaPass = new ArrayList<>();
    private int faltas, faltasRec, faltasCom, cartoes, cartoesAm, cartoesAz, cartoesVe,
            vitorias, empates, derrotas, minutos, jogos, dias;
    private int faltasPass, faltasRecPass, faltasComPass, cartoesPass, cartoesAmPass,
            cartoesAzPass, cartoesVePass,
            vitoriasPass, empatesPass, derrotasPass, minutosPass, jogosPass, diasPass;
    private double medMinuto, medJogo, medDia;
    private TextView txtFaltas, txtFal1, txtFal2, txtFalMinuto, txtFalJogo, txtFalDia, txtFalComp,
            txtCartoes, txtCar1, txtCar2, txtCar3, txtCarMinuto, txtCarJogo, txtCarDia, txtCarComp;
    private LinearLayout lytFaltasQtdMed, lytFal1, lytFal2, lytTipoFalMes,
            lytCartoesQtdMed, lytCar1, lytCar2, lytCar3, lytTipoCarMes;
    private View view1;
    private Usuario usuario;

    public static RelatorioDisciplinaFragment newInstance(List<Partida> lista, List<Partida> listaPassada) {
        instance = new RelatorioDisciplinaFragment();
        listaPartida = lista;
        listaPartidaPass = listaPassada;
        return instance;
    }

    public static RelatorioDisciplinaFragment getInstance() {
        if (instance == null)
            instance = new RelatorioDisciplinaFragment();
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
        return R.layout.fragment_relatorio_disciplina;
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
        mostrarGraficoCartoes();
        mostrarGraficoFaltas();
        if (listaPartidaPass.size() > 0)
            somaStatsPassados();
        mostrarGraficoCompCartoes();
        mostrarGraficoCompFaltas();
    }

    private void mostrarGraficoCompFaltas() {
        if (faltas == 0 && faltasPass == 0) {
            txtFalComp.setVisibility(View.VISIBLE);
            lytTipoFalMes.setVisibility(View.GONE);
            barChartFal1.setVisibility(View.GONE);
            barChartFal2.setVisibility(View.GONE);
            barChartFal3.setVisibility(View.GONE);
            return;
        } else {
            txtFalComp.setVisibility(View.GONE);
            lytTipoFalMes.setVisibility(View.VISIBLE);
            barChartFal1.setVisibility(View.VISIBLE);
            barChartFal2.setVisibility(View.VISIBLE);
            barChartFal3.setVisibility(View.VISIBLE);
        }

        Legend l = barChartFal1.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.add(new BarEntry(0,
                new float[]{faltasPass, faltas}));
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        yVals2.add(new BarEntry(0,
                new float[]{faltasRecPass, faltasRec}));
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        yVals3.add(new BarEntry(0,
                new float[]{faltasComPass, faltasCom}));

        configChartCompSimples(barChartFal1, yVals1);
        configChartCompSimples(barChartFal2, yVals2);
        configChartCompSimples(barChartFal3, yVals3);
        barChartFal1.getLegend().setEnabled(true);
    }

    private void mostrarGraficoFaltas() {
        if (faltas == 0) {
            barChartFaltas.setVisibility(View.GONE);
            lytFaltasQtdMed.setVisibility(View.GONE);
            return;
        } else {
            barChartFaltas.setVisibility(View.VISIBLE);
            lytFaltasQtdMed.setVisibility(View.VISIBLE);
        }

        Legend l = barChartFaltas.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.add(new BarEntry(0,
                new float[]{faltasCom, faltasRec}));

        String[] ordem = new String[]{"Cometidas", "Recebidas"};
        defineOrdemFaltas(ordem, 2);
        printarOrdemFaltas(ordem);

        YAxis leftAxis = barChartFaltas.getAxisLeft();
        //leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setDrawGridLines(false);
        leftAxis.setEnabled(false);

        XAxis xLabels = barChartFaltas.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setEnabled(false);
        xLabels.setDrawGridLines(false);

        BarDataSet set1;
        set1 = new BarDataSet(yVals1, "");
        set1.setDrawIcons(false);
        set1.setColors(getColorsErradoCerto());
        set1.setStackLabels(new String[]{"Cometidas", "Recebidas"});

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                long med = Math.round((value / faltas) * 100);
                if (med == 0)
                    return "";
                else
                    return "" + (int) med + "%";
            }
        });
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(12f);

        barChartFaltas.setData(data);
        barChartFaltas.getLegend().setEnabled(false);
        barChartFaltas.setPinchZoom(false);
        barChartFaltas.setDrawValueAboveBar(false);
        barChartFaltas.getAxisRight().setEnabled(false);
        barChartFaltas.setFitBars(true);
        barChartFaltas.getDescription().setEnabled(false);
        barChartFaltas.setDrawGridBackground(false);
        barChartFaltas.setDrawBarShadow(false);
        barChartFaltas.setHighlightFullBarEnabled(false);
        barChartFaltas.setClickable(false);
        barChartFaltas.setHighlightPerTapEnabled(false);
        barChartFaltas.setDoubleTapToZoomEnabled(false);
        barChartFaltas.setHighlightPerDragEnabled(false);
        barChartFaltas.animateX(1000);
        barChartFaltas.animateY(1000);
        barChartFaltas.invalidate();
        barChartFaltas.getLegend().setEnabled(true);
    }

    private void defineOrdemFaltas(String[] arr, int n) {
        if (n == 1)
            return;

        for (int i = 0; i < n - 1; i++) {
            if ("Recebidas".equals(arr[i])) {
                if (arr[i + 1].equals("Cometidas")) {
                    if (faltasRec >= faltasCom) {
                        String temp = arr[i];
                        arr[i] = arr[i + 1];
                        arr[i + 1] = temp;
                    }
                }
            } else {
                if (arr[i + 1].equals("Recebidas")) {
                    if (faltasCom > faltasRec) {
                        String temp = arr[i];
                        arr[i] = arr[i + 1];
                        arr[i + 1] = temp;
                    }
                }
            }
        }
        defineOrdemFaltas(arr, n - 1);
    }

    private void printarOrdemFaltas(String[] arr) {
        printOrdemFaltas(lytFal2, txtFal2, arr[0]);
        printOrdemFaltas(lytFal1, txtFal1, arr[1]);
    }

    private void printOrdemFaltas(LinearLayout layout, TextView texto, String tipo) {
        String txt = null;
        boolean zerado = true;
        if ("Recebidas".equals(tipo)) {
            if (faltasRec != 0) {
                zerado = false;
                txt = "Recebidas: " + faltasRec;
            }
        } else {
            if (faltasCom != 0) {
                zerado = false;
                txt = "Cometidas: " + faltasCom;
            }
        }
        if (!zerado) {
            texto.setText(txt);
            texto.setVisibility(View.VISIBLE);
            layout.setBackgroundResource(defineCorPosicaoFaltas(tipo));
            layout.setVisibility(View.VISIBLE);
        } else {
            texto.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
        }
    }

    private int defineCorPosicaoFaltas(String pos) {
        if ("Recebidas".equals(pos)) {
            return R.color.Cor_Meia;
        }
        return R.color.Cor_Atacante;
    }

    private void mostrarGraficoCompCartoes() {
        if (cartoes == 0 && cartoesPass == 0) {
            txtCarComp.setVisibility(View.VISIBLE);
            lytTipoCarMes.setVisibility(View.GONE);
            barChartCar1.setVisibility(View.GONE);
            barChartCar2.setVisibility(View.GONE);
            barChartCar3.setVisibility(View.GONE);
            barChartCar4.setVisibility(View.GONE);
            return;
        } else {
            txtCarComp.setVisibility(View.GONE);
            lytTipoCarMes.setVisibility(View.VISIBLE);
            barChartCar1.setVisibility(View.VISIBLE);
            barChartCar2.setVisibility(View.VISIBLE);
            barChartCar3.setVisibility(View.VISIBLE);
            barChartCar4.setVisibility(View.VISIBLE);
        }

        Legend l = barChartCar1.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.add(new BarEntry(0,
                new float[]{cartoesPass, cartoes}));
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        yVals2.add(new BarEntry(0,
                new float[]{cartoesAmPass, cartoesAm}));
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        yVals3.add(new BarEntry(0,
                new float[]{cartoesVePass, cartoesVe}));
        ArrayList<BarEntry> yVals4 = new ArrayList<BarEntry>();
        yVals4.add(new BarEntry(0,
                new float[]{cartoesAzPass, cartoesAz}));

        configChartCompSimples(barChartCar1, yVals1);
        configChartCompSimples(barChartCar2, yVals2);
        configChartCompSimples(barChartCar3, yVals3);
        configChartCompSimples(barChartCar4, yVals4);
        barChartCar1.getLegend().setEnabled(true);
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

    private void mostrarGraficoCartoes() {
        if (cartoes == 0) {
            lytCartoesQtdMed.setVisibility(View.GONE);
            pieChartCartoes.setVisibility(View.GONE);
            return;
        } else {
            lytCartoesQtdMed.setVisibility(View.VISIBLE);
            pieChartCartoes.setVisibility(View.VISIBLE);
        }

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "";
        String[] ordem = new String[]{"Azuis", "Vermelhos", "Amarelos"};

        //initializing data
        Map<String, Integer> typeAmountMap = new HashMap<>();
        ArrayList<Integer> colors = new ArrayList<>();

        typeAmountMap.put(ordem[2], cartoesAm);
        typeAmountMap.put(ordem[1], cartoesVe);
        typeAmountMap.put(ordem[0], cartoesAz);

        colors.add(Color.parseColor("#FFEB3B")); // Vermelho
        colors.add(Color.parseColor("#ED0000")); // Azul
        colors.add(Color.parseColor("#2196F3")); // Amarelo

        defineOrdemCartoes(ordem, 3);
        printarOrdemCartoes(ordem);

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

        pieChartCartoes.setData(pieData);
        pieChartCartoes.setCenterText("Tipo\nCartões\n%");
        pieChartCartoes.setDrawCenterText(true);
        pieChartCartoes.setCenterTextSize(16f);
        pieChartCartoes.getDescription().setEnabled(false);
        pieChartCartoes.setRotationEnabled(false);
        pieChartCartoes.setUsePercentValues(true);
        pieChartCartoes.setEntryLabelColor(R.color.black);
        pieChartCartoes.animateX(1000);
        pieChartCartoes.animateY(1000);
        pieChartCartoes.invalidate();
    }

    private void defineOrdemCartoes(String[] arr, int n) {
        if (n == 1)
            return;

        for (int i = 0; i < n - 1; i++) {
            switch (arr[i]) {
                case "Vermelhos":
                    if (arr[i + 1].equals("Azuis")) {
                        if (cartoesVe >= cartoesAz) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else {
                        if (cartoesVe >= cartoesAm) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    }
                    break;
                case "Azuis":
                    if (arr[i + 1].equals("Vermelhos")) {
                        if (cartoesAz > cartoesVe) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else {
                        if (cartoesAz >= cartoesAm) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    }
                    break;
                default:
                    if (arr[i + 1].equals("Vermelhos")) {
                        if (cartoesAm > cartoesVe) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    } else {
                        if (cartoesAm > cartoesAz) {
                            String temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    }
                    break;
            }
        }
        defineOrdemCartoes(arr, n - 1);
    }

    private void printarOrdemCartoes(String[] arr) {
        printOrdemCartoes(lytCar3, txtCar3, arr[0]);
        printOrdemCartoes(lytCar2, txtCar2, arr[1]);
        printOrdemCartoes(lytCar1, txtCar1, arr[2]);
    }

    private void printOrdemCartoes(LinearLayout layout, TextView texto, String tipo) {
        String txt = null;
        boolean zerado = true;
        if (tipo.equals("Amarelos")) {
            if (cartoesAm != 0) {
                zerado = false;
                txt = "Amarelos: " + cartoesAm;
            }
        } else if (tipo.equals("Azuis")) {
            if (cartoesAz != 0) {
                zerado = false;
                txt = "Azuis: " + cartoesAz;
            }
        } else {
            if (cartoesVe != 0) {
                zerado = false;
                txt = "Vermelhos: " + cartoesVe;
            }
        }
        if (!zerado) {
            texto.setText(txt);
            texto.setVisibility(View.VISIBLE);
            layout.setBackgroundResource(defineCorPosicaoCartoes(tipo));
            layout.setVisibility(View.VISIBLE);
        } else {
            texto.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
        }
    }

    private int defineCorPosicaoCartoes(String pos) {
        switch (pos) {
            case "Amarelos":
                return R.color.Cor_Goleiro;
            case "Azuis":
                return R.color.Cor_Defensor;
            default:
                return R.color.Cor_Atacante;
        }
    }

    private void somaStats() {
        String data = "";
        if (listaPartida.size() > 0)
            for (Partida partida : listaPartida) {
                cartoesAm += partida.getCartoesAma();
                cartoesAz += partida.getCartoesAzul();
                cartoesVe += partida.getCartoesVerm();
                faltasRec += partida.getFaltasRec();
                faltasCom += partida.getFaltasCom();
                vitorias += partida.getVitorias();
                empates += partida.getEmpates();
                derrotas += partida.getDerrotas();
                minutos += partida.getDuracao() *
                        (partida.getVitorias() + partida.getEmpates() + partida.getDerrotas());
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
        faltas = faltasRec + faltasCom;
        cartoes = cartoesAm + cartoesAz + cartoesVe;
        txtFaltas.setText(String.valueOf(faltas));
        txtCartoes.setText(String.valueOf(cartoes));

        calculaMediasFaltas();
        calculaMediasCartoes();
    }

    private void somaStatsPassados() {
        String data = "";
        for (Partida partida : listaPartidaPass) {
            cartoesAmPass += partida.getCartoesAma();
            cartoesAzPass += partida.getCartoesAzul();
            cartoesVePass += partida.getCartoesVerm();
            faltasRecPass += partida.getFaltasRec();
            faltasComPass += partida.getFaltasCom();
            vitoriasPass += partida.getVitorias();
            empatesPass += partida.getEmpates();
            derrotasPass += partida.getDerrotas();
            minutosPass += partida.getDuracao() *
                    (partida.getVitorias() + partida.getEmpates() + partida.getDerrotas());
            diasPass++;
            if (partida.getTipoRegistro().equals("Partida Única"))
                if (!data.isEmpty())
                    if (data.equals(partida.getData().substring(0, 10)))
                        diasPass--;
                    else
                        data = partida.getData().substring(0, 10);
        }
        jogosPass = vitoriasPass + empatesPass + derrotasPass;
        faltasPass = faltasRecPass + faltasComPass;
        cartoesPass = cartoesAmPass + cartoesAzPass + cartoesVePass;
    }

    private void calculaMediasFaltas() {
        long med = 0;
        if (faltas > 0) {
            lytFaltasQtdMed.setVisibility(View.VISIBLE);
            barChartFaltas.setVisibility(View.VISIBLE);
            medMinuto = (double) minutos / faltas;
            med = Math.round(medMinuto);
            if (med > 1) {
                txtFalMinuto.setText("1 falta a cada " + med + "min");
            } else if (med == 1) {
                txtFalMinuto.setText(med + " falta por min");
            } else {
                medMinuto = (double) faltas / minutos;
                med = Math.round(medMinuto);
                txtFalMinuto.setText(med + " faltas por min");
            }
            medJogo = (double) jogos / faltas;
            med = Math.round(medJogo);
            if (med > 1) {
                txtFalJogo.setText("1 falta a cada " + med + " jogos");
            } else if (med == 1) {
                txtFalJogo.setText(med + " falta por jogo");
            } else {
                medJogo = (double) faltas / jogos;
                med = Math.round(medJogo);
                txtFalJogo.setText(med + " faltas por jogo");
            }
            medDia = (double) faltas / dias;
            med = Math.round(medDia);
            if (med > 1) {
                txtFalDia.setText(med + " faltas por dia");
            } else if (med == 1) {
                txtFalDia.setText(med + " falta por dia");
            } else {
                medDia = (double) dias / faltas;
                med = Math.round(medDia);
                txtFalDia.setText("1 falta a cada " + med + " dias");
            }

        } else {
            lytFaltasQtdMed.setVisibility(View.GONE);
            barChartFaltas.setVisibility(View.GONE);
        }

    }

    private void calculaMediasCartoes() {
        long med = 0;
        if (cartoes > 0) {
            lytCartoesQtdMed.setVisibility(View.VISIBLE);
            pieChartCartoes.setVisibility(View.VISIBLE);
            medMinuto = (double) minutos / cartoes;
            med = Math.round(medMinuto);
            if (med > 1) {
                txtCarMinuto.setText("1 cartão a cada " + med + "min");
            } else if (med == 1) {
                txtCarMinuto.setText(med + " cartão por min");
            } else {
                medMinuto = (double) cartoes / minutos;
                med = Math.round(medMinuto);
                txtCarMinuto.setText(med + " cartões por min");
            }
            medJogo = (double) jogos / cartoes;
            med = Math.round(medJogo);
            if (med > 1) {
                txtCarJogo.setText("1 cartão a cada " + med + " jogos");
            } else if (med == 1) {
                txtCarJogo.setText(med + " cartão por jogo");
            } else {
                medJogo = (double) cartoes / jogos;
                med = Math.round(medJogo);
                txtCarJogo.setText(med + " cartões por jogo");
            }
            medDia = (double) cartoes / dias;
            med = Math.round(medDia);
            if (med > 1) {
                txtCarDia.setText(med + " cartões por dia");
            } else if (med == 1) {
                txtCarDia.setText(med + " cartão por dia");
            } else {
                medDia = (double) dias / cartoes;
                med = Math.round(medDia);
                txtCarDia.setText("1 cartão a cada " + med + " dias");
            }

        } else {
            lytCartoesQtdMed.setVisibility(View.GONE);
            pieChartCartoes.setVisibility(View.GONE);
        }
    }

    private void resetarStats() {
        cartoes = 0;
        cartoesAm = 0;
        cartoesAz = 0;
        cartoesVe = 0;
        vitorias = 0;
        empates = 0;
        derrotas = 0;
        minutos = 0;
        jogos = 0;
        faltasRec = 0;
        faltasCom = 0;
        faltas = 0;
        dias = 0;

        cartoesPass = 0;
        cartoesAmPass = 0;
        cartoesAzPass = 0;
        cartoesVePass = 0;
        vitoriasPass = 0;
        empatesPass = 0;
        derrotasPass = 0;
        minutosPass = 0;
        jogosPass = 0;
        faltasRecPass = 0;
        faltasComPass = 0;
        faltasPass = 0;
        diasPass = 0;

        txtCartoes.setText(String.valueOf(cartoes));
        txtFaltas.setText(String.valueOf(faltas));
    }

    private void inicializarComponentes(View view) {
        txtFaltas = view.findViewById(R.id.txt_relatorio_faltas);
        barChartFaltas = view.findViewById(R.id.barChart_relatorio_faltas);
        lytFaltasQtdMed = view.findViewById(R.id.layout_relatorio_faltasQtdMedias);
        lytFal1 = view.findViewById(R.id.layout_relatorio_fal1);
        lytFal2 = view.findViewById(R.id.layout_relatorio_fal2);
        txtFal1 = view.findViewById(R.id.txt_relatorio_fal1);
        txtFal2 = view.findViewById(R.id.txt_relatorio_fal2);
        txtFalMinuto = view.findViewById(R.id.txt_relatorio_faltasMinuto);
        txtFalJogo = view.findViewById(R.id.txt_relatorio_faltasJogo);
        txtFalDia = view.findViewById(R.id.txt_relatorio_faltasDia);
        txtFalComp = view.findViewById(R.id.txt_relatorio_faltasComp);
        lytTipoFalMes = view.findViewById(R.id.layout_relatorio_tiposFaltasMes);
        barChartFal1 = view.findViewById(R.id.barChart_relatorio_falMes1);
        barChartFal2 = view.findViewById(R.id.barChart_relatorio_falMes2);
        barChartFal3 = view.findViewById(R.id.barChart_relatorio_falMes3);

        txtCartoes = view.findViewById(R.id.txt_relatorio_cartoes);
        pieChartCartoes = view.findViewById(R.id.pieChart_relatorio_scoutsDef);
        lytCartoesQtdMed = view.findViewById(R.id.layout_relatorio_grafScoutDef);
        lytCar1 = view.findViewById(R.id.layout_relatorio_sdef1);
        lytCar2 = view.findViewById(R.id.layout_relatorio_sdef2);
        lytCar3 = view.findViewById(R.id.layout_relatorio_sdef3);
        txtCar1 = view.findViewById(R.id.txt_relatorio_sdef1);
        txtCar2 = view.findViewById(R.id.txt_relatorio_sdef2);
        txtCar3 = view.findViewById(R.id.txt_relatorio_sdef3);
        txtCarMinuto = view.findViewById(R.id.txt_relatorio_desMinuto);
        txtCarJogo = view.findViewById(R.id.txt_relatorio_desJogo);
        txtCarDia = view.findViewById(R.id.txt_relatorio_desDia);
        txtCarComp = view.findViewById(R.id.txt_relatorio_scoutsDefComp);
        lytTipoCarMes = view.findViewById(R.id.layout_relatorio_tipoSDefMes);
        barChartCar1 = view.findViewById(R.id.barChart_relatorio_sDefMes1);
        barChartCar2 = view.findViewById(R.id.barChart_relatorio_sDefMes2);
        barChartCar3 = view.findViewById(R.id.barChart_relatorio_sDefMes3);
        barChartCar4 = view.findViewById(R.id.barChart_relatorio_sDefMes4);
    }
}