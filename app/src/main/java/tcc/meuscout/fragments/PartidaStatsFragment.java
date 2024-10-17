package tcc.meuscout.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import tcc.meuscout.R;
import tcc.meuscout.model.Partida;
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.Constantes;
import tcc.meuscout.util.Rotinas;

public class PartidaStatsFragment extends Fragment {

    private ImageButton imgAbrirAtaque, imgAbrirDefesa, imgAbrirDisciplina;
    private TextInputLayout edtGols, edtGolsPE, edtGolsPD, edtGolsOU, edtGolsCA,
            edtFinalizacoes, edtFinalCertas, edtFinalErradas, edtFinalBloq, edtFinalTrave,
            edtPenCerto, edtPenErr, edtAssist, edtDefesas, edtGolsSof, edtPenDef, edtPenLev,
            edtDesarmes, edtCortes, edtBloqueios, edtErros, edtFaltasCom, edtFaltasRec,
            edtCartoesAma, edtCartoesVerm, edtCartoesAzul;
    private LinearLayout layoutAtaque, layoutDivisorAtaque, layoutDefesa, layoutDivisorDefesa,
            layoutDisciplina, layoutDivisorDisciplina, layoutGeral;
    private int gols, golsPE, golsPD, golsOU, golsCA, finalizacoes, finalCertas, finalErradas, finalBloq,
            finalTrave, penCertos, penErrados, assists, defesas, golsSof, penDef, penLev, desarmes,
            cortes, bloq, erros, faltasCom, faltasRec, cartoesAma, cartoesVerm, cartoesAzul;
    private int somaGols, somaFinaliz;
    private static Partida mPartida;
    private static PartidaStatsFragment instance;
    private static String operacao;
    private View view1;
    private TextView textVisu;

    public static PartidaStatsFragment newInstance(Partida partida, String OPERACAO) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constantes.PARCELABLE_OBJ, partida);
        bundle.putString(Constantes.OPERACAO, operacao);
        mPartida = partida;
        operacao = OPERACAO;
        instance = new PartidaStatsFragment();
        instance.setArguments(bundle);
        return instance;
    }

    public static PartidaStatsFragment getInstance() {
        if (instance == null) {
            instance = new PartidaStatsFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_partida_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            inicializarComponentes(view);
            view1 = view.getRootView();
            if (!operacao.equals(Constantes.INSERIR)) {
                carregarDados();

            }

            controlarSessoes();
            if (operacao.equals(Constantes.VISUALIZAR)) {
                textVisu.setVisibility(View.VISIBLE);
                layoutAtaque.setVisibility(View.VISIBLE);
                layoutDefesa.setVisibility(View.VISIBLE);
                layoutDisciplina.setVisibility(View.VISIBLE);
                Rotinas.desabilitarTela(false, layoutGeral);


            }
            controladoresMaisMenos();
        } catch (Exception e) {
            Alerta.exibeSnackbarCurto(view1, Constantes.MSG_GENERICA_ERRO);
            e.printStackTrace();
        }
    }

    private void carregarDados() {
        gols = mPartida.getGolsFeitos();
        edtGols.getEditText().setText(String.valueOf(gols));
        golsPE = mPartida.getGolsPE();
        edtGolsPE.getEditText().setText(String.valueOf(golsPE));
        golsPD = mPartida.getGolsPD();
        edtGolsPD.getEditText().setText(String.valueOf(golsPD));
        golsCA = mPartida.getGolsCA();
        edtGolsCA.getEditText().setText(String.valueOf(golsCA));
        golsOU = mPartida.getGolsOU();
        edtGolsOU.getEditText().setText(String.valueOf(golsOU));
        finalizacoes = mPartida.getFinalTotal();
        edtFinalizacoes.getEditText().setText(String.valueOf(finalizacoes));
        finalCertas = mPartida.getFinalCertas();
        edtFinalCertas.getEditText().setText(String.valueOf(finalCertas));
        finalErradas = mPartida.getFinalErradas();
        edtFinalErradas.getEditText().setText(String.valueOf(finalErradas));
        finalBloq = mPartida.getFinalBloq();
        edtFinalBloq.getEditText().setText(String.valueOf(finalBloq));
        finalTrave = mPartida.getFinalTrave();
        edtFinalTrave.getEditText().setText(String.valueOf(finalTrave));
        penCertos = mPartida.getPenaltiAc();
        edtPenCerto.getEditText().setText(String.valueOf(penCertos));
        penErrados = mPartida.getPenaltiEr();
        edtPenErr.getEditText().setText(String.valueOf(penErrados));
        assists = mPartida.getAssistencias();
        edtAssist.getEditText().setText(String.valueOf(assists));
        defesas = mPartida.getDefesas();
        edtDefesas.getEditText().setText(String.valueOf(defesas));
        golsSof = mPartida.getGolsSofridos();
        edtGolsSof.getEditText().setText(String.valueOf(golsSof));
        penDef = mPartida.getDefesasPen();
        edtPenDef.getEditText().setText(String.valueOf(penDef));
        penLev = mPartida.getGolsSofridosPen();
        edtPenLev.getEditText().setText(String.valueOf(penLev));
        desarmes = mPartida.getDesarmes();
        edtDesarmes.getEditText().setText(String.valueOf(desarmes));
        cortes = mPartida.getCortes();
        edtCortes.getEditText().setText(String.valueOf(cortes));
        bloq = mPartida.getBloqueios();
        edtBloqueios.getEditText().setText(String.valueOf(bloq));
        erros = mPartida.getErros();
        edtErros.getEditText().setText(String.valueOf(erros));
        faltasCom = mPartida.getFaltasCom();
        edtFaltasCom.getEditText().setText(String.valueOf(faltasCom));
        faltasRec = mPartida.getFaltasRec();
        edtFaltasRec.getEditText().setText(String.valueOf(faltasRec));
        cartoesAma = mPartida.getCartoesAma();
        edtCartoesAma.getEditText().setText(String.valueOf(cartoesAma));
        cartoesVerm = mPartida.getCartoesVerm();
        edtCartoesVerm.getEditText().setText(String.valueOf(cartoesVerm));
        cartoesAzul = mPartida.getCartoesAzul();
        edtCartoesAzul.getEditText().setText(String.valueOf(cartoesAzul));
    }

    public Partida executar(Partida partida) {
        partida.setGolsFeitos(gols);
        partida.setGolsPE(golsPE);
        partida.setGolsPD(golsPD);
        partida.setGolsCA(golsCA);
        partida.setGolsOU(golsOU);
        partida.setFinalTotal(finalizacoes);
        partida.setFinalCertas(finalCertas);
        partida.setFinalErradas(finalErradas);
        partida.setFinalBloq(finalBloq);
        partida.setFinalTrave(finalTrave);
        partida.setPenaltiAc(penCertos);
        partida.setPenaltiEr(penErrados);
        partida.setAssistencias(assists);
        partida.setDefesas(defesas);
        partida.setGolsSofridos(golsSof);
        partida.setDefesasPen(penDef);
        partida.setGolsSofridosPen(penLev);
        partida.setDesarmes(desarmes);
        partida.setCortes(cortes);
        partida.setBloqueios(bloq);
        partida.setErros(erros);
        partida.setFaltasCom(faltasCom);
        partida.setFaltasRec(faltasRec);
        partida.setCartoesAma(cartoesAma);
        partida.setCartoesVerm(cartoesVerm);
        partida.setCartoesAzul(cartoesAzul);
        return partida;
    }

    public Partida retornaStatsPartida() {
        return mPartida;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (operacao.equalsIgnoreCase(Constantes.VISUALIZAR))
            menu.findItem(R.id.cadastrar).setVisible(false);
    }

    private void inicializarComponentes(View view) {
        layoutDivisorAtaque = view.findViewById(R.id.layout_relatorio_sessao_gols);
        layoutDivisorDefesa = view.findViewById(R.id.layout_partida_sessao_defesa);
        layoutDivisorDisciplina = view.findViewById(R.id.layout_partida_sessao_disciplina);
        layoutAtaque = view.findViewById(R.id.layout_partida_ataque);
        layoutDefesa = view.findViewById(R.id.layout_partida_defesa);
        layoutDisciplina = view.findViewById(R.id.layout_partida_disciplina);
        layoutGeral = view.findViewById(R.id.layout_partida_geral);
        imgAbrirAtaque = view.findViewById(R.id.img_partida_ataque_add);
        imgAbrirDefesa = view.findViewById(R.id.img_partida_defesa_add);
        imgAbrirDisciplina = view.findViewById(R.id.img_partida_disciplina_add);
        edtGols = view.findViewById(R.id.edt_partida_gols);
        edtGolsPE = view.findViewById(R.id.edt_partida_golspe);
        edtGolsPD = view.findViewById(R.id.edt_partida_golspd);
        edtGolsOU = view.findViewById(R.id.edt_partida_golsou);
        edtGolsCA = view.findViewById(R.id.edt_partida_golsca);
        edtFinalizacoes = view.findViewById(R.id.edt_partida_finalizacoes);
        edtFinalCertas = view.findViewById(R.id.edt_partida_finalCertas);
        edtFinalErradas = view.findViewById(R.id.edt_partida_finalErradas);
        edtFinalBloq = view.findViewById(R.id.edt_partida_finalBloq);
        edtFinalTrave = view.findViewById(R.id.edt_partida_finalTrave);
        edtPenCerto = view.findViewById(R.id.edt_partida_penaltisCertos);
        edtPenErr = view.findViewById(R.id.edt_partida_penaltisErrados);
        edtAssist = view.findViewById(R.id.edt_partida_assistencias);
        edtDefesas = view.findViewById(R.id.edt_partida_defesas);
        edtGolsSof = view.findViewById(R.id.edt_partida_golsSofridos);
        edtPenDef = view.findViewById(R.id.edt_partida_penaltisDef);
        edtPenLev = view.findViewById(R.id.edt_partida_penaltisLev);
        edtDesarmes = view.findViewById(R.id.edt_partida_desarmes);
        edtCortes = view.findViewById(R.id.edt_partida_cortes);
        edtBloqueios = view.findViewById(R.id.edt_partida_bloqueios);
        edtErros = view.findViewById(R.id.edt_partida_erros);
        edtFaltasCom = view.findViewById(R.id.edt_partida_faltasCom);
        edtFaltasRec = view.findViewById(R.id.edt_partida_faltasRec);
        edtCartoesAma = view.findViewById(R.id.edt_partida_cartoesAma);
        edtCartoesVerm = view.findViewById(R.id.edt_partida_cartoesVerm);
        edtCartoesAzul = view.findViewById(R.id.edt_partida_cartoesAzul);
        textVisu = view.findViewById(R.id.txt_partidaStats_visu);
    }

    private void controlarSessoes() {
        cliqueDivisor(layoutDivisorAtaque, layoutAtaque, imgAbrirAtaque);
        cliqueDivisor(layoutDivisorDefesa, layoutDefesa, imgAbrirDefesa);
        cliqueDivisor(layoutDivisorDisciplina, layoutDisciplina, imgAbrirDisciplina);
    }

    private void cliqueDivisor(LinearLayout divisor, LinearLayout sessao, ImageButton imgDivisor) {
        imgDivisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirFecharSessao(sessao, imgDivisor);
            }
        });
        divisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirFecharSessao(sessao, imgDivisor);
            }
        });
    }

    private void abrirFecharSessao(LinearLayout layout, ImageButton img) {
        if (layout.getVisibility() == View.GONE) {
            img.setImageDrawable(getResources().getDrawable(R.drawable.ic_menos_circulo));
            layout.setVisibility(View.VISIBLE);
        } else {
            img.setImageDrawable(getResources().getDrawable(R.drawable.ic_adicionar_circulo));
            layout.setVisibility(View.GONE);
        }
    }

    private void controladoresMaisMenos() {
        edtGols.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtGols.getEditText().setText(String.valueOf(gols = gols + 1));
            }
        });
        edtGols.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gols > 0) {
                    edtGols.getEditText().setText(String.valueOf(gols = gols - 1));
                    verificaGolsRem();
                }
            }
        });
        edtGolsPE.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtGolsPE.getEditText().setText(String.valueOf(golsPE = golsPE + 1));
                verificaSomaGolsAdd();
            }
        });
        edtGolsPE.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (golsPE > 0)
                    edtGolsPE.getEditText().setText(String.valueOf(golsPE = golsPE - 1));
            }
        });
        edtGolsPD.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtGolsPD.getEditText().setText(String.valueOf(golsPD = golsPD + 1));
                verificaSomaGolsAdd();
            }
        });
        edtGolsPD.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (golsPD > 0)
                    edtGolsPD.getEditText().setText(String.valueOf(golsPD = golsPD - 1));
            }
        });
        edtGolsCA.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtGolsCA.getEditText().setText(String.valueOf(golsCA = golsCA + 1));
                verificaSomaGolsAdd();
            }
        });
        edtGolsCA.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (golsCA > 0)
                    edtGolsCA.getEditText().setText(String.valueOf(golsCA = golsCA - 1));
            }
        });
        edtGolsOU.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtGolsOU.getEditText().setText(String.valueOf(golsOU = golsOU + 1));
                verificaSomaGolsAdd();
            }
        });
        edtGolsOU.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (golsOU > 0)
                    edtGolsOU.getEditText().setText(String.valueOf(golsOU = golsOU - 1));
            }
        });
        edtFinalizacoes.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtFinalizacoes.getEditText().setText(String.valueOf(finalizacoes = finalizacoes + 1));
            }
        });
        edtFinalizacoes.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalizacoes > 0)
                    edtFinalizacoes.getEditText().setText(String.valueOf(finalizacoes = finalizacoes - 1));
                verificaFinalzRem();
            }
        });
        edtFinalCertas.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtFinalCertas.getEditText().setText(String.valueOf(finalCertas = finalCertas + 1));
                verificaSomaFinaliz();
            }
        });
        edtFinalCertas.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalCertas > 0)
                    edtFinalCertas.getEditText().setText(String.valueOf(finalCertas = finalCertas - 1));
            }
        });
        edtFinalErradas.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtFinalErradas.getEditText().setText(String.valueOf(finalErradas = finalErradas + 1));
                verificaSomaFinaliz();
            }
        });
        edtFinalErradas.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalErradas > 0)
                    edtFinalErradas.getEditText().setText(String.valueOf(finalErradas = finalErradas - 1));
            }
        });
        edtFinalBloq.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtFinalBloq.getEditText().setText(String.valueOf(finalBloq = finalBloq + 1));
                verificaSomaFinaliz();
            }
        });
        edtFinalBloq.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalBloq > 0)
                    edtFinalBloq.getEditText().setText(String.valueOf(finalBloq = finalBloq - 1));
            }
        });
        edtFinalTrave.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtFinalTrave.getEditText().setText(String.valueOf(finalTrave = finalTrave + 1));
                verificaSomaFinaliz();
            }
        });
        edtFinalTrave.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalTrave > 0)
                    edtFinalTrave.getEditText().setText(String.valueOf(finalTrave = finalTrave - 1));
            }
        });
        edtPenCerto.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPenCerto.getEditText().setText(String.valueOf(penCertos = penCertos + 1));
            }
        });
        edtPenCerto.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (penCertos > 0)
                    edtPenCerto.getEditText().setText(String.valueOf(penCertos = penCertos - 1));
            }
        });
        edtPenErr.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPenErr.getEditText().setText(String.valueOf(penErrados = penErrados + 1));
            }
        });
        edtPenErr.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (penErrados > 0)
                    edtPenErr.getEditText().setText(String.valueOf(penErrados = penErrados - 1));
            }
        });
        edtAssist.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtAssist.getEditText().setText(String.valueOf(assists = assists + 1));
            }
        });
        edtAssist.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (assists > 0)
                    edtAssist.getEditText().setText(String.valueOf(assists = assists - 1));
            }
        });
        edtDefesas.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtDefesas.getEditText().setText(String.valueOf(defesas = defesas + 1));
            }
        });
        edtDefesas.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (defesas > 0)
                    edtDefesas.getEditText().setText(String.valueOf(defesas = defesas - 1));
            }
        });
        edtGolsSof.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtGolsSof.getEditText().setText(String.valueOf(golsSof = golsSof + 1));
            }
        });
        edtGolsSof.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (golsSof > 0)
                    edtGolsSof.getEditText().setText(String.valueOf(golsSof = golsSof - 1));
            }
        });
        edtPenDef.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPenDef.getEditText().setText(String.valueOf(penDef = penDef + 1));
            }
        });
        edtPenDef.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (penDef > 0)
                    edtPenDef.getEditText().setText(String.valueOf(penDef = penDef - 1));
            }
        });
        edtPenLev.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPenLev.getEditText().setText(String.valueOf(penLev = penLev + 1));
            }
        });
        edtPenLev.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (penLev > 0)
                    edtPenLev.getEditText().setText(String.valueOf(penLev = penLev - 1));
            }
        });
        edtDesarmes.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtDesarmes.getEditText().setText(String.valueOf(desarmes = desarmes + 1));
            }
        });
        edtDesarmes.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (desarmes > 0)
                    edtDesarmes.getEditText().setText(String.valueOf(desarmes = desarmes - 1));
            }
        });
        edtCortes.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtCortes.getEditText().setText(String.valueOf(cortes = cortes + 1));
            }
        });
        edtCortes.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cortes > 0)
                    edtCortes.getEditText().setText(String.valueOf(cortes = cortes - 1));
            }
        });
        edtBloqueios.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtBloqueios.getEditText().setText(String.valueOf(bloq = bloq + 1));
            }
        });
        edtBloqueios.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bloq > 0)
                    edtBloqueios.getEditText().setText(String.valueOf(bloq = bloq - 1));
            }
        });
        edtErros.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtErros.getEditText().setText(String.valueOf(erros = erros + 1));
            }
        });
        edtErros.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (erros > 0)
                    edtErros.getEditText().setText(String.valueOf(erros = erros - 1));
            }
        });
        edtFaltasCom.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtFaltasCom.getEditText().setText(String.valueOf(faltasCom = faltasCom + 1));
            }
        });
        edtFaltasCom.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (faltasCom > 0)
                    edtFaltasCom.getEditText().setText(String.valueOf(faltasCom = faltasCom - 1));
            }
        });
        edtFaltasRec.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtFaltasRec.getEditText().setText(String.valueOf(faltasRec = faltasRec + 1));
            }
        });
        edtFaltasRec.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (faltasRec > 0)
                    edtFaltasRec.getEditText().setText(String.valueOf(faltasRec = faltasRec - 1));
            }
        });
        edtCartoesAma.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtCartoesAma.getEditText().setText(String.valueOf(cartoesAma = cartoesAma + 1));
            }
        });
        edtCartoesAma.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartoesAma > 0)
                    edtCartoesAma.getEditText().setText(String.valueOf(cartoesAma = cartoesAma - 1));
            }
        });
        edtCartoesVerm.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtCartoesVerm.getEditText().setText(String.valueOf(cartoesVerm = cartoesVerm + 1));
            }
        });
        edtCartoesVerm.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartoesVerm > 0)
                    edtCartoesVerm.getEditText().setText(String.valueOf(cartoesVerm = cartoesVerm - 1));
            }
        });
        edtCartoesAzul.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtCartoesAzul.getEditText().setText(String.valueOf(cartoesAzul = cartoesAzul + 1));
            }
        });
        edtCartoesAzul.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartoesAzul > 0)
                    edtCartoesAzul.getEditText().setText(String.valueOf(cartoesAzul = cartoesAzul - 1));
            }
        });
    }

    private void verificaSomaGolsAdd() {
        somaGols = golsPE + golsPD + golsCA + golsOU;
        if (somaGols > gols) {
            gols = somaGols;
            edtGols.getEditText().setText(String.valueOf(gols));
        }
    }

    private void verificaGolsRem() {
        somaGols = golsPE + golsPD + golsCA + golsOU;
        if (somaGols > gols) {
            if (golsPE != 0) {
                golsPE -= 1;
                edtGolsPE.getEditText().setText(String.valueOf(golsPE));
            } else if (golsPD != 0) {
                golsPD -= 1;
                edtGolsPD.getEditText().setText(String.valueOf(golsPD));
            } else if (golsCA != 0) {
                golsCA -= 1;
                edtGolsCA.getEditText().setText(String.valueOf(golsCA));
            } else if (golsOU != 0) {
                golsOU -= 1;
                edtGolsOU.getEditText().setText(String.valueOf(golsOU));
            }
        }

    }

    private void verificaSomaFinaliz() {
        somaFinaliz = finalCertas + finalErradas + finalBloq + finalTrave;
        if (somaFinaliz > finalizacoes) {
            finalizacoes = somaFinaliz;
            edtFinalizacoes.getEditText().setText(String.valueOf(finalizacoes));
        }
    }

    private void verificaFinalzRem() {
        somaFinaliz = finalCertas + finalErradas + finalBloq + finalTrave;
        if (somaFinaliz > finalizacoes) {
            if (finalCertas != 0) {
                finalCertas -= 1;
                edtFinalCertas.getEditText().setText(String.valueOf(finalCertas));
            } else if (finalErradas != 0) {
                finalErradas -= 1;
                edtFinalErradas.getEditText().setText(String.valueOf(finalErradas));
            } else if (finalBloq != 0) {
                finalBloq -= 1;
                edtFinalBloq.getEditText().setText(String.valueOf(finalBloq));
            } else if (finalTrave != 0) {
                finalTrave -= 1;
                edtFinalTrave.getEditText().setText(String.valueOf(finalTrave));
            }
        }
    }
}