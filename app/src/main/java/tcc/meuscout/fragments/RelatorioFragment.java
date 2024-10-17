package tcc.meuscout.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tcc.meuscout.R;
import tcc.meuscout.activities.EnderecoListActivity;
import tcc.meuscout.activities.MenuDrawerActivity;
import tcc.meuscout.database.ControleBanco;
import tcc.meuscout.firebase.ConfiguracaoFirebase;
import tcc.meuscout.model.Endereco;
import tcc.meuscout.model.Partida;
import tcc.meuscout.model.Usuario;
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.Base64Custom;
import tcc.meuscout.util.Constantes;

public class RelatorioFragment extends Fragment {

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference partidaPassRef, partidaRef;
    private static RelatorioFragment instance;
    private View view1;
    private MaterialCalendarView calendarView;
    private String mesAno, ano;
    private static String mesS = "", mesP = "";
    private ValueEventListener valueEventListenerPartidasPass, valueEventListenerPartidas;
    private List<Partida> listaPartida = new ArrayList<>(), listaPartidaPass = new ArrayList<>();
    private List<Partida> listaPartidaBanco = new ArrayList<>();
    private Fragment childFragment;
    private FragmentTransaction transaction;
    private Dialog dialog;
    final Calendar today = Calendar.getInstance();
    private int anoEscolhido = today.get(Calendar.YEAR), mesEscolhido = today.get(Calendar.MONTH);
    private EditText edtMes, edtAno;
    private final Map<Integer, String> meses = new HashMap<>();
    private Usuario usuario;
    private TextInputLayout edtEndereco, edtPosicao, edtPosicaoSec;
    private Button btnAplicar;
    private Endereco endereco;

    public static RelatorioFragment newInstance() {
        instance = new RelatorioFragment();
        return instance;
    }

    public static RelatorioFragment getInstance() {
        if (instance == null)
            instance = new RelatorioFragment();
        return instance;
    }

    public List<Partida> getListaPartida() {
        return listaPartida;
    }

    private void implementaMeses() {
        meses.put(1, "Jan");
        meses.put(2, "Fev");
        meses.put(3, "Mar");
        meses.put(4, "Abr");
        meses.put(5, "Mai");
        meses.put(6, "Jun");
        meses.put(7, "Jul");
        meses.put(8, "Ago");
        meses.put(9, "Set");
        meses.put(10, "Out");
        meses.put(11, "Nov");
        meses.put(12, "Dez");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_relatorio, container, false);

        //fragment = view.findViewById(R.id.fragment_tab_relatorio);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            view1 = view.getRootView();
            calendarView = view.findViewById(R.id.calendarView);
            criaDialogCarregando();
            usuario = ControleBanco.getInstance().recuperaUsuarioLogado(getActivity());
            listaPartidaBanco = ControleBanco.getInstance().recuperaPartidas(getActivity(), usuario);
            implementaMeses();
            configuraCalendarView();

            //recuperaPartidas2022();
        } catch (Exception e) {
            Alerta.exibeSnackbarLongo(view, Constantes.MSG_GENERICA_ERRO);
            e.printStackTrace();
        }
    }

    public void configuraCalendarView() throws Exception { //Método que configura o CalendarView
        int mes = calendarView.getCurrentDate().getMonth() + 1;
        ano = String.valueOf(calendarView.getCurrentDate().getYear());
        String zero = "", anoMesP = "";
        if (mes < 10) {
            zero = "0";
        }
        mesS = zero + mes;
        if (mes == 1) {
            mesP = "12";
            anoMesP = (Integer.parseInt(ano) - 1) + "/" + mesP;

        } else {
            mesP = zero + (mes - 1);
            anoMesP = ano + "/" + mesP;
        }

        /*recuperaPartidasPassadas(mesAnoP);
        recuperaPartidas();*/
        recuperaPartidasMesPassadoBanco(anoMesP);
        recuperaPartidasMesBanco(ano + "/" + mesS);
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                try {
                    int mesI = calendarView.getCurrentDate().getMonth() + 1;
                    ano = String.valueOf(calendarView.getCurrentDate().getYear());
                    String zero = "", anoMesP = "";
                    if (mesI < 10) {
                        zero = "0";
                    }
                    mesS = zero + mesI;
                    if (mesI == 1) {
                        mesP = "12";
                        anoMesP = (Integer.parseInt(ano) - 1) + "/" + mesP;
                    } else {
                        if (mesI - 1 < 10)
                            zero = "0";
                        mesP = zero + (mesI - 1);
                        anoMesP = ano + "/" + mesP;
                    }
                    //Antes de anexar mais um evento, devemos excluir o evento adicionado anteriormente
                    /*partidaRef.removeEventListener(valueEventListenerPartidas);
                    partidaPassRef.removeEventListener(valueEventListenerPartidasPass);*/
                    //getChildFragmentManager().popBackStack();

                    //transaction.remove(RelatorioTabFragment.getInstance()).commit();
                    dialog.show();
                    //recuperaPartidasPassadas(mesAnoP);
                    //recuperaPartidas();
                    recuperaPartidasMesPassadoBanco(anoMesP);
                    recuperaPartidasMesBanco(ano + "/" + mesS);
                } catch (Exception e) {
                    Alerta.exibeSnackbarLongo(getView(), Constantes.MSG_GENERICA_ERRO);
                    e.printStackTrace();
                }
            }
        });
    }

    //Método que recupera as partidas feitas naquele mês
    public void recuperaPartidas() {
        //Recebe o email do usuário logado e o codifica para formar o id
        String idUsuario = Base64Custom.codificar(autenticacao.getCurrentUser().getEmail());
        //Recebe a referência ao nó do usuário logado
        partidaRef = firebaseRef.child("usuarios").child(idUsuario).child("partidas");
        valueEventListenerPartidas = partidaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    listaPartida.clear(); //Limpa a lista para ser adicionado os nós do firebase do mês específico
                /*
                OBS.: A lista deve ser reciclada, ou seja, há uma lista de movimentações do mês 3,
                mas quando o mês for, por exmplo: 4, a lista deve ser limpada para adicionar as
                movimentações do mês 4
                 */
                    //Recuperar todos as movimentações feitas no mês
                    for (DataSnapshot mov : snapshot.getChildren()) {
                        for (DataSnapshot snap : mov.getChildren()) {
                            Partida partida = snap.getValue(Partida.class); //Recupera um nó
                            if (partida != null)
                                if (!partida.getTipoRegistro().equals("Treino"))
                                    listaPartida.add(partida); //Adiciona os nós recuperados na lista
                        }
                    }
                    if (childFragment == null)
                        childFragment = RelatorioTabFragment.newInstance(listaPartida, listaPartidaPass);
                    else
                        RelatorioTabFragment.getInstance().setListaPartida(listaPartida, listaPartidaPass);
                    dialog.dismiss();
                    transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.child_fragment_container, childFragment).commit();
                    partidaRef.removeEventListener(valueEventListenerPartidas);
                } catch (Exception e) {
                    Alerta.exibeSnackbarLongo(view1, Constantes.MSG_GENERICA_ERRO);
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Alerta.exibeSnackbarLongo(view1, "Não foi possível recuperar as informações. Tente novamente!");
            }
        });
    }

    public void recuperaPartidasMesBanco(String anoMes) throws Exception {
        //listaPartida = ControleBanco.getInstance().recuperaPartidasMes(getActivity(), usuario, mesAno);
        listaPartida = new ArrayList<>();
        for (Partida partida : listaPartidaBanco) {
            if (partida.getData().contains(anoMes))
                listaPartida.add(partida);
        }
        if (childFragment == null)
            childFragment = RelatorioTabFragment.newInstance(listaPartida, listaPartidaPass);
        else
            RelatorioTabFragment.getInstance().setListaPartida(listaPartida, listaPartidaPass);
        dialog.dismiss();
        transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, childFragment).commit();
    }

    //Método que recupera as partidas feitas no mês passado
    public void recuperaPartidasPassadas(String anoMes) {
        //Recebe o email do usuário logado e o codifica para formar o id
        String idUsuario = Base64Custom.codificar(autenticacao.getCurrentUser().getEmail());
        //Recebe a referência ao nó do usuário logado
        partidaPassRef = firebaseRef.child("usuarios").child(idUsuario).child("partidas").child(anoMes);
        valueEventListenerPartidasPass = partidaPassRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaPartidaPass.clear(); //Limpa a lista para ser adicionado os nós do firebase do mês específico
                /*
                OBS.: A lista deve ser reciclada, ou seja, há uma lista de movimentações do mês 3,
                mas quando o mês for, por exmplo: 4, a lista deve ser limpada para adicionar as
                movimentações do mês 4
                 */

                //Recuperar todos as movimentações feitas no mês
                for (DataSnapshot mov : snapshot.getChildren()) {
                    Partida partida = mov.getValue(Partida.class); //Recupera um nó
                    if (!partida.getTipoRegistro().equals("Treino"))
                        listaPartidaPass.add(partida); //Adiciona os nós recuperados na lista
                }
                partidaPassRef.removeEventListener(valueEventListenerPartidasPass);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Alerta.exibeSnackbarLongo(view1, "Não foi possível recuperar as informações. Tente novamente!");
            }
        });
    }

    public void recuperaPartidasMesPassadoBanco(String anoMes) {
        listaPartidaPass = new ArrayList<>();
        for (Partida partida : listaPartidaBanco) {
            if (partida.getData().contains(anoMes))
                listaPartidaPass.add(partida);
        }
    }

    private void recuperaPartidas2022() {
        //Recebe o email do usuário logado e o codifica para formar o id
        String idUsuario = Base64Custom.codificar(autenticacao.getCurrentUser().getEmail());
        //Recebe a referência ao nó do usuário logado
        Query ref = firebaseRef.child("usuarios").child(idUsuario).child("partidas");
        valueEventListenerPartidas = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    listaPartida.clear(); //Limpa a lista para ser adicionado os nós do firebase do mês específico
                /*
                OBS.: A lista deve ser reciclada, ou seja, há uma lista de movimentações do mês 3,
                mas quando o mês for, por exmplo: 4, a lista deve ser limpada para adicionar as
                movimentações do mês 4
                 */
                    //Recuperar todos as movimentações feitas no mês
                    for (DataSnapshot mov : snapshot.getChildren()) {
                        for (DataSnapshot mov2 : mov.getChildren()) {
                            Partida partida = mov2.getValue(Partida.class); //Recupera um nó
                            if (!partida.getTipoRegistro().equals("Treino") && partida.getData().contains("/2022"))
                                listaPartida.add(partida); //Adiciona os nós recuperados na lista
                        }
                    }
                    /*if (childFragment == null)
                        childFragment = RelatorioTabFragment.newInstance(listaPartida, listaPartidaPass);
                    else
                        RelatorioTabFragment.getInstance().setListaPartida(listaPartida, listaPartidaPass);

                    transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.child_fragment_container, childFragment).commit();*/
                    ref.removeEventListener(valueEventListenerPartidas);
                } catch (Exception e) {
                    Alerta.exibeSnackbarLongo(view1, Constantes.MSG_GENERICA_ERRO);
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Alerta.exibeSnackbarLongo(view1, "Não foi possível recuperar as informações. Tente novamente!");
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.orz_menu_actionbar_filtro, menu);
        menu.findItem(R.id.action_visualizacao).setVisible(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_visualizacao) {
            dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.dialog_escolhe_visualizacao);
            dialog.setCancelable(true);
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            //dialog.getWindow().setLayout((6 * width)/7, (4 * height)/5);
            //dialog.getWindow().setLayout((6 * width) / 7, height / 4);
            //(MonthPickerDialog) dialog.findViewById(R.id.yearView);
            dialog.create();
            edtMes = dialog.findViewById(R.id.edt_relatorio_mes);
            edtAno = dialog.findViewById(R.id.edt_relatorio_ano);
            edtEndereco = dialog.findViewById(R.id.edt_relatorio_endereco);
            edtPosicao = dialog.findViewById(R.id.edt_relatorio_posicao);
            edtPosicaoSec = dialog.findViewById(R.id.edt_relatorio_posicaosec);
            btnAplicar = dialog.findViewById(R.id.btn_relatorio_aplicar);
            edtEndereco.getEndIconDrawable().setVisible(true, false);
            if (mesEscolhido != -1) {
                mesEscolhido = calendarView.getCurrentDate().getMonth() + 1;
                edtMes.setText(meses.get(mesEscolhido));
            } else {
                edtMes.setText(null);
            }
            if (anoEscolhido != -1) {
                anoEscolhido = calendarView.getCurrentDate().getYear();
                edtAno.setText(String.valueOf(anoEscolhido));
            } else {
                edtAno.setText(null);
            }
            // mes.setRawInputType(InputType.TYPE_NULL);
            //mes.setFocusable(true);

            dialog.show();

            edtMes.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        float rawX = event.getRawX();
                        float edtMesRight = edtMes.getRight();
                        float edtMesWidth = edtMes.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                        if (rawX >= (edtMesRight - edtMesWidth)) { //Remover o filtro
                            edtMes.setText("");
                            mesEscolhido = -1;
                        } else {
                            MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getActivity(),
                                    new MonthPickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(int selectedMonth, int selectedYear) {
                                            mesEscolhido = selectedMonth + 1;
                                            edtMes.setText(meses.get(mesEscolhido));
                                        }
                                    }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));
                            builder.setActivatedMonth(mesEscolhido != -1 ? mesEscolhido - 1 : today.get(Calendar.MONTH))
                                    .setTitle("Mês escolhido")
                                    .showMonthOnly()
                                    .build().show();
                        }
                        return true;
                    }
                    return false;
                }
            });

            edtAno.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        float rawX = event.getRawX();
                        float edtMesRight = edtAno.getRight();
                        float edtMesWidth = edtAno.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                        if (rawX >= (edtMesRight - edtMesWidth)) { //Remover o filtro
                            edtAno.setText("");
                            anoEscolhido = -1;
                        } else {
                            MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getActivity(),
                                    new MonthPickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(int selectedMonth, int selectedYear) {
                                            anoEscolhido = selectedYear;
                                            edtAno.setText(String.valueOf(anoEscolhido));
                                        }
                                    }, today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1);

                            String anoMinimo = "" + today.get(Calendar.YEAR);
                            if (listaPartidaBanco.size() > 0)
                                anoMinimo = listaPartidaBanco.get(0).getData().substring(6, 10);

                            builder.setActivatedYear(anoEscolhido != -1 ? anoEscolhido : today.get(Calendar.YEAR))
                                    .setMinYear(Integer.parseInt(anoMinimo))
                                    .setTitle("Ano escolhido")
                                    .setMaxYear(today.get(Calendar.YEAR))
                                    .showYearOnly()
                                    .build().show();
                        }
                        return true;
                    }
                    return false;
                }
            });

            edtEndereco.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        edtEndereco.requestFocus();
                        Intent it = new Intent(getContext(), EnderecoListActivity.class);
                        if (endereco != null) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Constantes.SERIALIZABLE_OBJ, endereco);
                            it.putExtras(bundle);
                        }
                        startActivityForResult(it, Constantes.REQUESTCODE_100);
                        return true;
                    }
                    return false;
                }
            });

            edtPosicao.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });

            edtPosicaoSec.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });

            btnAplicar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (requestCode == Constantes.REQUESTCODE_100) {
                if (data != null && resultCode == Activity.RESULT_OK) {
                    endereco = (Endereco) data.getSerializableExtra(Constantes.RESULT);
                    if (endereco != null && !endereco.getNomelocal().isEmpty()) {
                        Endereco end = ControleBanco.getInstance()
                                .recuperaEnderecoPorId(getActivity(), usuario, endereco.getFirebase_id());
                        if (end != null && (!end.getNomelocal().equals(endereco.getNomelocal()) ||
                                !end.getEndereco().equals(endereco.getEndereco())))
                            endereco = end;
                        edtEndereco.getEditText().setText(endereco.getNomelocal());
                    } else
                        edtEndereco.getEditText().setText("");
                }
            } else if (requestCode == Constantes.REQUESTCODE_200) {

            } else if (requestCode == Constantes.REQUESTCODE_300) {

            }
        } catch (Exception e) {
            Alerta.exibeSnackbarCurto(view1, Constantes.MSG_GENERICA_ERRO);
            e.printStackTrace();
        }
    }

    private void criaDialogCarregando() {
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