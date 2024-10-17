package tcc.meuscout.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tcc.meuscout.R;
import tcc.meuscout.activities.EnderecoListActivity;
import tcc.meuscout.activities.TimeListActivity;
import tcc.meuscout.adapter.BasicoSpinner;
import tcc.meuscout.adapter.PosicaoSpinner;
import tcc.meuscout.database.ControleBanco;
import tcc.meuscout.model.Endereco;
import tcc.meuscout.model.Partida;
import tcc.meuscout.model.Posicao;
import tcc.meuscout.model.Time;
import tcc.meuscout.model.Usuario;
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.Constantes;
import tcc.meuscout.util.Conversao;
import tcc.meuscout.util.Rotinas;

public class PartidaInfosFragment extends Fragment {

    private static PartidaInfosFragment instance;
    private TextInputLayout edtLocal, edtDuracao,
            edtVitorias, edtEmpates, edtDerrotas, edtCasa, edtFora,
            edtNotaInd, edtNotaPart, edtData, edtHora, edtTime, edtTimeAdv;
    private CheckBox checkBoxPosicao, checkBoxTime;
    private Spinner spnTipoRegistro, spnPosicao, spnPosicaoSec;
    private PosicaoSpinner mPosicaoObjSpn, mPosicaoSecObjSpn;
    private BasicoSpinner mBasicoObjSpn;
    private ArrayList<BasicoSpinner> basicoSpinnerList;
    private ArrayList<PosicaoSpinner> posicaoSpinnerList, posicaoSecSpinnerList;
    private Calendar mCurrentDate;
    private int mDia, mMes, mAno, mHora, mMinuto;
    private static String mOperacao;
    private LinearLayout layoutTime, layoutPosicao, layoutPlacar, layoutResults, layoutGeral;
    private Usuario usuario;
    private Time meuTime, timeAdv;
    private static Partida mPartida;
    private int notaInd = -1, notaPart = -1, vitorias = 0, empates = 0,
            derrotas = 0, placarCasa = 0, placarFora = 0;
    private View view1;
    private TextView textVisu;
    private String data, dataAtual, dia, mes, ano;
    private Endereco endereco;

    public static PartidaInfosFragment newInstance(Partida partida, String operacao) {
        instance = new PartidaInfosFragment();
        mPartida = partida;
        mOperacao = operacao;
        return instance;
    }

    public static PartidaInfosFragment getInstance() {
        if (instance == null)
            instance = new PartidaInfosFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_partida_infos, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view1 = view.getRootView();
        try {
            inicializarComponentes(view);
            getDataCorrente();
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                if (intent.getStringExtra(Constantes.OPERACAO) != null)
                    mOperacao = intent.getStringExtra(Constantes.OPERACAO);
                if (intent.getSerializableExtra(Constantes.PARCELABLE_OBJ) != null)
                    mPartida = intent.getParcelableExtra(Constantes.PARCELABLE_OBJ);
                else
                    mPartida = new Partida();
            }

            carregarDados();

            edtData.getEditText().
                    setOnTouchListener(new View.OnTouchListener() {
                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                edtData.requestFocus();
                                if (mOperacao.equals(Constantes.EDITAR)) {
                                    String ano = edtData.getEditText().getText().toString().substring(6, 10);
                                    String mes = edtData.getEditText().getText().toString().substring(3, 5);
                                    String dia = edtData.getEditText().getText().toString().substring(0, 2);
                                    mAno = Integer.parseInt(ano);
                                    mMes = Integer.parseInt(mes) - 1;
                                    mDia = Integer.parseInt(dia);
                                    /*mAno = 2023;
                                    mMes = 5-1;
                                    mDia = 23;*/
                                }
                                @SuppressLint("SetTextI18n")
                                DatePickerDialog datePickerDialog = new DatePickerDialog
                                        (getContext(), (view, year, month, dayOfMonth) ->
                                                edtData.getEditText().setText(
                                                        ((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth) + "/" +
                                                                ((month + 1) < 10 ? "0" + (month + 1) : (month + 1)) + "/" +
                                                                year),
                                                mAno, mMes, mDia);
                                datePickerDialog.show();
                                return true;
                            }
                            return false;
                        }
                    });

            edtHora.getEditText().
                    setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                edtHora.requestFocus();
                                @SuppressLint("SetTextI18n")
                                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) ->
                                        edtHora.getEditText().setText((hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" +
                                                (minute < 10 ? "0" + minute : minute)),
                                        mHora, mMinuto, true);
                                timePickerDialog.show();
                                return true;
                            }
                            return false;
                        }
                    });

            checkBoxPosicao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        layoutPosicao.setVisibility(View.VISIBLE);
                        spnPosicaoSec.requestFocus();
                    } else {
                        layoutPosicao.setVisibility(View.GONE);
                    }

                }
            });

            checkBoxTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        layoutTime.setVisibility(View.VISIBLE);
                        edtTime.requestFocus();
                    } else {
                        layoutTime.setVisibility(View.GONE);
                    }

                }
            });

            spnPosicao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position,
                                           long id) {
                    mPosicaoObjSpn = (PosicaoSpinner) parent.getSelectedItem();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spnPosicaoSec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position,
                                           long id) {
                    mPosicaoSecObjSpn = (PosicaoSpinner) parent.getSelectedItem();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spnTipoRegistro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position,
                                           long id) {
                    mBasicoObjSpn = (BasicoSpinner) parent.getSelectedItem();
                    if (mBasicoObjSpn.getNome().equals("Dia de Jogos")) {
                        layoutPlacar.setVisibility(View.GONE);
                        layoutResults.setVisibility(View.VISIBLE);
                        checkBoxTime.setVisibility(View.VISIBLE);
                    } else if (mBasicoObjSpn.getNome().equals("Partida Única")) {
                        layoutPlacar.setVisibility(View.VISIBLE);
                        layoutResults.setVisibility(View.GONE);
                        checkBoxTime.setVisibility(View.VISIBLE);
                    } else {
                        layoutPlacar.setVisibility(View.GONE);
                        layoutResults.setVisibility(View.GONE);
                        checkBoxTime.setChecked(false);
                        checkBoxTime.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            edtTime.getEditText().
                    setOnTouchListener(new View.OnTouchListener() {
                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                edtTime.requestFocus();
                                Intent it = new Intent(getContext(), TimeListActivity.class);
                                it.putExtra(Constantes.ADVERSARIO, false);
                                startActivityForResult(it, Constantes.REQUESTCODE_100);
                                return true;
                            }
                            return false;
                        }
                    });

            edtTimeAdv.getEditText().
                    setOnTouchListener(new View.OnTouchListener() {
                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                edtTimeAdv.requestFocus();
                                Intent it = new Intent(getContext(), TimeListActivity.class);
                                it.putExtra(Constantes.ADVERSARIO, true);
                                startActivityForResult(it, Constantes.REQUESTCODE_200);
                                return true;
                            }
                            return false;
                        }
                    });

            edtLocal.getEditText().
                    setOnTouchListener(new View.OnTouchListener() {
                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                edtLocal.requestFocus();
                                Intent it = new Intent(getContext(), EnderecoListActivity.class);
                                if (endereco != null) {
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(Constantes.SERIALIZABLE_OBJ, endereco);
                                    it.putExtras(bundle);
                                }
                                startActivityForResult(it, Constantes.REQUESTCODE_300);
                                return true;
                            }
                            return false;
                        }
                    });

            controladoresMaisMenos();

        } catch (Exception e) {
            trataExcecao(e, view1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (requestCode == Constantes.REQUESTCODE_100) {
                if (data != null && resultCode == Activity.RESULT_OK) {
                    meuTime = data.getParcelableExtra(Constantes.RESULT);
                    edtTime.getEditText().setText(meuTime.getNome() + " - " + meuTime.getSigla());
                    edtTimeAdv.requestFocus();
                } else {
                    if (meuTime != null)
                        meuTime = ControleBanco.getInstance()
                                .recuperaTimePorId(getActivity(), usuario, meuTime.getFirebase_id());
                    if (meuTime == null || meuTime.getNome() == null) {
                        edtTime.getEditText().setText("");
                        if (edtTimeAdv.getEditText().getText().toString().isEmpty()) {
                            checkBoxTime.setChecked(false);
                            layoutTime.setVisibility(View.GONE);
                        }
                    }
                }
            } else if (requestCode == Constantes.REQUESTCODE_200) {
                if (data != null && resultCode == Activity.RESULT_OK) {
                    timeAdv = data.getParcelableExtra(Constantes.RESULT);
                    edtTimeAdv.getEditText().setText(timeAdv.getNome() + " - " + timeAdv.getSigla());
                } else {
                    if (timeAdv != null)
                        timeAdv = ControleBanco.getInstance()
                                .recuperaTimePorId(getActivity(), usuario, timeAdv.getFirebase_id());
                    if (timeAdv == null || timeAdv.getNome() == null) {
                        edtTimeAdv.getEditText().setText("");
                        if (edtTime.getEditText().getText().toString().isEmpty()) {
                            checkBoxTime.setChecked(false);
                            layoutTime.setVisibility(View.GONE);
                        }
                    }
                }
            } else if (requestCode == Constantes.REQUESTCODE_300) {
                if (data != null && resultCode == Activity.RESULT_OK) {
                    endereco = (Endereco) data.getSerializableExtra(Constantes.RESULT);
                    if (endereco != null && !endereco.getNomelocal().isEmpty()) {
                        Endereco end = ControleBanco.getInstance()
                                .recuperaEnderecoPorId(getActivity(), usuario, endereco.getFirebase_id());
                        if (end != null && (!end.getNomelocal().equals(endereco.getNomelocal()) ||
                                !end.getEndereco().equals(endereco.getEndereco())))
                            endereco = end;
                        edtLocal.getEditText().setText(endereco.getNomelocal());
                    } else
                        edtLocal.getEditText().setText("");
                }
            }
        } catch (Exception e) {
            Alerta.exibeSnackbarCurto(view1, Constantes.MSG_GENERICA_ERRO);
            e.printStackTrace();
        }
    }

    public Partida executar() {
        if (mOperacao.equals(Constantes.EDITAR)) {
            String firebaseId = mPartida.getFirebase_id();
            mPartida = new Partida();
            mPartida.setFirebase_id(firebaseId);
        }
        String ano = edtData.getEditText().getText().toString().substring(6, 10);
        String mes = edtData.getEditText().getText().toString().substring(3, 5);
        String dia = edtData.getEditText().getText().toString().substring(0, 2);
        String hora = edtHora.getEditText().getText().toString();
        mPartida.setData(ano + "/" + mes + "/" + dia + " " + hora);
        mPartida.setNomeLocal(edtLocal.getEditText().getText().toString().trim());
        mPartida.setEndereco(endereco.getEndereco());
        mPartida.setDuracao(Integer.parseInt(edtDuracao.getEditText().getText().toString().trim()));
        if (mBasicoObjSpn.getNome().equals("Dia de Jogos")) {
            mPartida.setVitorias(vitorias);
            mPartida.setEmpates(empates);
            mPartida.setDerrotas(derrotas);
            mPartida.setQtdPartidas(vitorias + empates + derrotas);
        } else if (mBasicoObjSpn.getNome().equals("Partida Única")) {
            mPartida.setPlacarCasa(placarCasa);
            mPartida.setPlacarFora(placarFora);
            if (placarCasa > placarFora)
                mPartida.setVitorias(1);
            else if (placarCasa < placarFora)
                mPartida.setDerrotas(1);
            else
                mPartida.setEmpates(1);
            mPartida.setQtdPartidas(1);
        }
        mPartida.setTipoRegistro(mBasicoObjSpn.getNome());
        mPartida.setPosicao_nome(mPosicaoObjSpn.getPosicao().getNome());
        mPartida.setPosicao_sigla(mPosicaoObjSpn.getPosicao().getSigla());
        mPartida.setPosicao_num(mPosicaoObjSpn.getPosicao().getNum());
        if (checkBoxPosicao.isChecked()) {
            mPartida.setPosicaosec_nome(mPosicaoSecObjSpn.getPosicao().getNome());
            mPartida.setPosicaosec_sigla(mPosicaoSecObjSpn.getPosicao().getSigla());
            mPartida.setPosicaosec_num(mPosicaoSecObjSpn.getPosicao().getNum());
        }
        if (checkBoxTime.isChecked()) {
            mPartida.setTime_id_firebase(meuTime != null ? meuTime.getFirebase_id() : null);
            mPartida.setTime_adv_id_firebase(timeAdv != null ? timeAdv.getFirebase_id() : null);
        }
        mPartida.setNotaInd(notaInd);
        mPartida.setNotaPart(notaPart);
        mPartida.setUsuario_id_firebase(usuario.getFirebase_id());
        //mPartida.setFirebase_id(ano + "/" + mes + "/" + dia + " " + hora);
        return mPartida;
    }

    public boolean validarDados() {
        int cont = 0;
        //if (validarData(view1))
        if (validarCampo(edtLocal)) cont++;
        if (validarCampo(edtDuracao)) cont++;
        if (validarCamposResult()) cont++;
        validarTimes();

        return cont == 3;
    }

    private boolean validarData(View v) {
        data = edtData.getEditText().getText().toString().trim();
        data = data.replace(".", "/");
        data = data.replace("-", "/");
        data = data.replace(" ", "");
        Date dataInformada = Conversao.StringParaData(data, "dd/MM/yyyy");
        Date dataAgora = Conversao.StringParaData(dataAtual, "dd/MM/yyyy");

        if (dataInformada == null || data.length() != 10) {
            Snackbar.make(v, "Data inválida.", Snackbar.LENGTH_SHORT).show();
            edtData.setError("Inválido");
            return false;
        }

        ano = data.substring(6, 10);
        mes = data.substring(3, 5);
        dia = data.substring(0, 2);

        if (dataAtual.equals(data) || dataInformada.after(dataAgora) || Integer.parseInt(dia) < 1 ||
                Integer.parseInt(dia) > 31 || Integer.parseInt(mes) < 1 || Integer.parseInt(mes) > 12 ||
                Integer.parseInt(ano) > mAno) {
            Snackbar.make(v, "Data inválida.", Snackbar.LENGTH_SHORT).show();
            edtData.setError("Inválido");
            return false;
        }
        edtData.setError(null);
        edtData.getEditText().setText(data);

        return true;
    }

    private boolean validarCampo(TextInputLayout campo) {
        String txt = campo.getEditText().getText().toString().trim();
        if (txt.isEmpty()) {
            campo.setError("Obrigatório");
            campo.requestFocus();
        } else {
            campo.setError(null);
            return true;
        }
        return false;
    }

    private boolean validarCamposResult() {
        if (mBasicoObjSpn.getNome().equals("Dia de Jogos")) {
            int soma = vitorias + empates + derrotas;
            if (soma == 0) {
                edtVitorias.setError("");
                edtEmpates.setError("");
                edtDerrotas.setError("");
                Alerta.exibeSnackbarLongo(getView(), "Vitórias Empates e Derrotas não podem ser todos 0.");
            } else {
                edtVitorias.setError(null);
                edtEmpates.setError(null);
                edtDerrotas.setError(null);
                return true;
            }
        } else
            return true;
        return false;
    }

    private void validarTimes() {
        String txt = edtTime.getEditText().getText().toString();
        if (txt.isEmpty())
            meuTime = null;
        txt = edtTimeAdv.getEditText().getText().toString();
        if (txt.isEmpty())
            timeAdv = null;
    }

    private void carregarDados() throws Exception {
        usuario = ControleBanco.getInstance().recuperaUsuarioLogado(getActivity());

        if (mOperacao.equals(Constantes.INSERIR)) {
            if (usuario.getTime_nome() != null) {
                meuTime = ControleBanco.getInstance().recuperaTimePrincipalUsuario(getActivity(), usuario);
                checkBoxTime.setChecked(true);
                layoutTime.setVisibility(View.VISIBLE);
                edtTime.getEditText().setText(meuTime.getNome() + " - " + meuTime.getSigla());
            }
            for (PosicaoSpinner posicaoSpinner : posicaoSpinnerList) {
                if (usuario.getPosicao_num() == (posicaoSpinner.getPosicao().getNum())) {
                    spnPosicao.setSelection(posicaoSpinner.getId());
                    break;
                }
            }
        } else {
            if (mOperacao.equals(Constantes.VISUALIZAR)) {
                textVisu.setVisibility(View.VISIBLE);
                Rotinas.desabilitarTela(false, layoutGeral);
            }
            if (mPartida != null) {
                String ano = mPartida.getData().substring(0, 4);
                String mes = mPartida.getData().substring(5, 7);
                String dia = mPartida.getData().substring(8, 10);
                String hora = mPartida.getData().substring(11, 16);
                edtData.getEditText().setText(dia + "/" + mes + "/" + ano);
                edtHora.getEditText().setText(hora);

                endereco = new Endereco();
                endereco.setNomelocal(mPartida.getNomeLocal());
                endereco.setEndereco(mPartida.getEndereco());
                edtLocal.getEditText().setText(mPartida.getNomeLocal());
                for (BasicoSpinner basicoSpinner : basicoSpinnerList) {
                    if (mPartida.getTipoRegistro().equals(basicoSpinner.getNome())) {
                        spnTipoRegistro.setSelection(basicoSpinner.getId());
                        if (basicoSpinner.getNome().equals("Dia de Jogos")) {
                            layoutResults.setVisibility(View.VISIBLE);
                            checkBoxTime.setVisibility(View.VISIBLE);
                            vitorias = mPartida.getVitorias();
                            empates = mPartida.getEmpates();
                            derrotas = mPartida.getDerrotas();
                            edtVitorias.getEditText().setText(vitorias + "");
                            edtEmpates.getEditText().setText(empates + "");
                            edtDerrotas.getEditText().setText(derrotas + "");
                        } else if (basicoSpinner.getNome().equals("Partida Única")) {
                            layoutPlacar.setVisibility(View.VISIBLE);
                            checkBoxTime.setVisibility(View.VISIBLE);
                            placarCasa = mPartida.getPlacarCasa();
                            placarFora = mPartida.getPlacarFora();
                            edtCasa.getEditText().setText(placarCasa + "");
                            edtFora.getEditText().setText(placarFora + "");
                        }
                        break;
                    }
                }
                edtDuracao.getEditText().setText(mPartida.getDuracao() + "");

                for (PosicaoSpinner posicaoSpinner : posicaoSpinnerList) {
                    if (mPartida.getPosicao_sigla().equals(posicaoSpinner.getPosicao().getSigla())) {
                        spnPosicao.setSelection(posicaoSpinner.getId());
                        break;
                    }
                }

                if (mPartida.getPosicaosec_nome() != null && !mPartida.getPosicaosec_nome().equals("")) {
                    for (PosicaoSpinner posicaoSpinner : posicaoSpinnerList) {
                        if (mPartida.getPosicaosec_sigla().equals(posicaoSpinner.getPosicao().getSigla())) {
                            spnPosicaoSec.setSelection(posicaoSpinner.getId());
                            break;
                        }
                    }
                    checkBoxPosicao.setChecked(true);
                    layoutPosicao.setVisibility(View.VISIBLE);
                }

                if (mPartida.getTime_id_firebase() != null) {
                    meuTime = ControleBanco.getInstance().recuperaTimePorId(getActivity(), usuario, mPartida.getTime_id_firebase());
                    edtTime.getEditText().setText(meuTime.getNome() + " - " + meuTime.getSigla());
                    if (!checkBoxTime.isChecked())
                        checkBoxTime.setChecked(true);
                    layoutTime.setVisibility(View.VISIBLE);
                }
                if (mPartida.getTime_adv_id_firebase() != null) {
                    timeAdv = ControleBanco.getInstance().recuperaTimePorId(getActivity(), usuario, mPartida.getTime_adv_id_firebase());
                    edtTimeAdv.getEditText().setText(timeAdv.getNome() + " - " + timeAdv.getSigla());
                    if (!checkBoxTime.isChecked())
                        checkBoxTime.setChecked(true);
                    layoutTime.setVisibility(View.VISIBLE);
                }
                notaPart = mPartida.getNotaPart();
                notaInd = mPartida.getNotaInd();
                if (notaInd < 0)
                    edtNotaPart.getEditText().setText("Sem Nota");
                else
                    edtNotaPart.getEditText().setText(notaPart + "");

                if (notaPart < 0)
                    edtNotaInd.getEditText().setText("Sem Nota");
                else
                    edtNotaInd.getEditText().setText(notaInd + "");
            }
        }
    }

    private void controladoresMaisMenos() {
        edtVitorias.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtVitorias.getEditText().setText(String.valueOf(vitorias = vitorias + 1));
            }
        });
        edtVitorias.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vitorias > 0)
                    edtVitorias.getEditText().setText(String.valueOf((vitorias = vitorias - 1)));
            }
        });
        edtEmpates.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtEmpates.getEditText().setText(String.valueOf(empates = empates + 1));
            }
        });
        edtEmpates.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (empates > 0)
                    edtEmpates.getEditText().setText(String.valueOf((empates = empates - 1)));
            }
        });
        edtDerrotas.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtDerrotas.getEditText().setText(String.valueOf(derrotas = derrotas + 1));
            }
        });
        edtDerrotas.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (derrotas > 0)
                    edtDerrotas.getEditText().setText(String.valueOf((derrotas = derrotas - 1)));
            }
        });
        edtCasa.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtCasa.getEditText().setText(String.valueOf(placarCasa = placarCasa + 1));
            }
        });
        edtCasa.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (placarCasa > 0)
                    edtCasa.getEditText().setText(String.valueOf((placarCasa = placarCasa - 1)));
            }
        });
        edtFora.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtFora.getEditText().setText(String.valueOf(placarFora = placarFora + 1));
            }
        });
        edtFora.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (placarFora > 0)
                    edtFora.getEditText().setText(String.valueOf((placarFora = placarFora - 1)));
            }
        });
        edtNotaInd.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notaInd < 5)
                    edtNotaInd.getEditText().setText(String.valueOf((notaInd = notaInd + 1)));
            }
        });
        edtNotaInd.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notaInd -= 1;
                if (notaInd < 0) {
                    edtNotaInd.getEditText().setText("Sem Nota");
                    notaInd = -1;
                } else
                    edtNotaInd.getEditText().setText(String.valueOf(notaInd));
            }
        });
        edtNotaPart.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notaPart < 5)
                    edtNotaPart.getEditText().setText(String.valueOf((notaPart = notaPart + 1)));
            }
        });
        edtNotaPart.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notaPart -= 1;
                if (notaPart < 0) {
                    edtNotaPart.getEditText().setText("Sem Nota");
                    notaPart = -1;
                } else
                    edtNotaPart.getEditText().setText(String.valueOf(notaPart));
            }
        });
    }

    private void inicializarComponentes(View view) throws Exception {
        edtData = view.findViewById(R.id.edt_partida_data);
        edtHora = view.findViewById(R.id.edt_partida_hora);
        edtLocal = view.findViewById(R.id.edt_partida_nomeLocal);
        spnTipoRegistro = view.findViewById(R.id.spn_partida_tipoRegistro);
        edtDuracao = view.findViewById(R.id.edt_partida_duracao);
        spnPosicao = view.findViewById(R.id.spn_partida_posicao);
        checkBoxPosicao = view.findViewById(R.id.chk_partida_checkbox_posicao);
        checkBoxTime = view.findViewById(R.id.chk_partida_checkbox_time);
        layoutTime = view.findViewById(R.id.layout_partida_time);
        edtTime = view.findViewById(R.id.edt_partida_time);
        edtTimeAdv = view.findViewById(R.id.edt_partida_time_adv);
        spnPosicaoSec = view.findViewById(R.id.spn_partida_posicaosec);
        layoutPosicao = view.findViewById(R.id.layout_partida_posicaosec);
        layoutPlacar = view.findViewById(R.id.layout_partida_placar);
        layoutResults = view.findViewById(R.id.layout_partida_diaJogos_resultados);
        layoutGeral = view.findViewById(R.id.layout_partidaInfos_geral);
        edtNotaInd = view.findViewById(R.id.edt_partida_notaInd);
        //edtNotaInd.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        edtNotaPart = view.findViewById(R.id.edt_partida_notaPart);
        //edtNotaPart.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        edtVitorias = view.findViewById(R.id.edt_partida_vitorias);
        edtEmpates = view.findViewById(R.id.edt_partida_empates);
        edtDerrotas = view.findViewById(R.id.edt_partida_derrotas);
        edtCasa = view.findViewById(R.id.edt_partida_golsCasa);
        edtFora = view.findViewById(R.id.edt_partida_golsFora);
        textVisu = view.findViewById(R.id.txt_partidaInfo_visu);
        carregarSpinners();
    }

    private void carregarSpinners() throws Exception {
        carregarSpinnerRegistro();
        carregarSpinnerPosicao();
    }

    private void carregarSpinnerRegistro() {
        basicoSpinnerList = new ArrayList<>();
        basicoSpinnerList.add(new BasicoSpinner(0, "Dia de Jogos"));
        basicoSpinnerList.add(new BasicoSpinner(1, "Partida Única"));
        basicoSpinnerList.add(new BasicoSpinner(2, "Treino"));

        ArrayAdapter<BasicoSpinner> adapter = new ArrayAdapter(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                basicoSpinnerList);
        spnTipoRegistro.setAdapter(adapter);
    }

    private void carregarSpinnerPosicao() {
        posicaoSpinnerList = new ArrayList<>();
        //List<Posicao> listaPosicoes = ;
        posicaoSpinnerList.add(new PosicaoSpinner(0, new Posicao("ATACANTE", "ATA", 4)));
        posicaoSpinnerList.add(new PosicaoSpinner(1, new Posicao("Centroavante", "CA", 4)));
        posicaoSpinnerList.add(new PosicaoSpinner(2, new Posicao("Ponta", "PD/PE", 4)));
        posicaoSpinnerList.add(new PosicaoSpinner(3, new Posicao("Segundo Atacante", "SA", 4)));
        posicaoSpinnerList.add(new PosicaoSpinner(4, new Posicao("MEIA", "MEI", 3)));
        posicaoSpinnerList.add(new PosicaoSpinner(5, new Posicao("Meia Atacante", "MAT", 3)));
        posicaoSpinnerList.add(new PosicaoSpinner(6, new Posicao("Meia Aberto", "MD/ME", 3)));
        posicaoSpinnerList.add(new PosicaoSpinner(7, new Posicao("Meia Central", "MC", 3)));
        posicaoSpinnerList.add(new PosicaoSpinner(8, new Posicao("Volante", "VOL", 3)));
        posicaoSpinnerList.add(new PosicaoSpinner(9, new Posicao("DEFENSOR", "DEF", 2)));
        posicaoSpinnerList.add(new PosicaoSpinner(10, new Posicao("Ala", "AD/AE", 2)));
        posicaoSpinnerList.add(new PosicaoSpinner(11, new Posicao("Lateral", "LD/LE", 2)));
        posicaoSpinnerList.add(new PosicaoSpinner(12, new Posicao("Zagueiro", "ZAG", 2)));
        posicaoSpinnerList.add(new PosicaoSpinner(13, new Posicao("GOLEIRO", "GOL", 1)));

        ArrayAdapter<PosicaoSpinner> adapter = new ArrayAdapter(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                posicaoSpinnerList);
        spnPosicao.setAdapter(adapter);
        spnPosicaoSec.setAdapter(adapter);
    }

    private void getDataCorrente() {
        mCurrentDate = Calendar.getInstance();
        mDia = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        mMes = mCurrentDate.get(Calendar.MONTH);
        mAno = mCurrentDate.get(Calendar.YEAR);
        mHora = mCurrentDate.get(Calendar.HOUR_OF_DAY);
        mMinuto = mCurrentDate.get(Calendar.MINUTE);
        //Preencher os componentes qdo for um novo apontamento
        edtData.getEditText().setText((mDia < 10 ? "0" + mDia : mDia) + "/" +
                ((mMes + 1) < 10 ? "0" + (mMes + 1) : (mMes + 1)) + "/" + mAno);
        //edtData.getEditText().setInputType(InputType.TYPE_NULL);
        dataAtual = edtData.getEditText().getText().toString();
        edtHora.getEditText().setText((mHora < 10 ? "0" + mHora : mHora) + ":" + (mMinuto < 10 ? "0" + mMinuto : mMinuto));
        edtHora.getEditText().setInputType(InputType.TYPE_NULL);
    }

    private void trataExcecao(Exception e, View view) {
        //View v = getView().findViewById(android.R.id.content);
        Alerta.exibeSnackbarLongo(view, Constantes.MSG_GENERICA_ERRO);
        e.printStackTrace();
    }


}