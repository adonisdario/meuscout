package tcc.meuscout.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import tcc.meuscout.R;
import tcc.meuscout.adapter.BasicoSpinner;
import tcc.meuscout.adapter.PosicaoSpinner;
import tcc.meuscout.database.ControleBanco;
import tcc.meuscout.firebase.ConfiguracaoFirebase;
import tcc.meuscout.model.Posicao;
import tcc.meuscout.model.Time;
import tcc.meuscout.model.Usuario;
import tcc.meuscout.util.Alerta;
import tcc.meuscout.util.Base64Custom;
import tcc.meuscout.util.Constantes;
import tcc.meuscout.util.Conversao;

public class CadastroActivity extends Activity {

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth autenticacao;
    private TextInputLayout edtEmail, edtSenha, edtNome, edtCidade,
            edtTime, edtSigla, edtDtNasc, edtTimeBanco;
    private Spinner spnPerna, spnPosicao;
    private PosicaoSpinner mPosicaoObjSpn;
    private BasicoSpinner mPernaObjSpn;
    private ArrayList<BasicoSpinner> pernaSpinnerList;
    private ArrayList<PosicaoSpinner> posicaoSpinnerList;
    private LinearLayout layoutTime, layoutTimeBanco;
    private CheckBox checkbox;
    private Button btnCadastrar;
    private Usuario usuario = new Usuario();
    private Calendar mCurrentDate;
    private String dataAtual;
    private int mDia, mMes, mAno;
    private EditText edtFoco;
    private String TAG = "CadastroActivity";
    private String email, senha, nome, time, siglaTime, cidade, data;
    private ProgressBar progressBar;
    private Posicao posicao;
    private Time meuTime = new Time();
    private String operacao = "";
    private boolean tinhaTimeAntes = false;
    private String ano, mes, dia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        try {
            inicializarComponentes();
            getDataCorrente();
            Intent intent = getIntent();
            if (intent != null) {
                operacao = intent.getStringExtra(Constantes.OPERACAO);
            }
            if (operacao.equals(Constantes.EDITAR))
                carregarDados();


            btnCadastrar.setOnClickListener(new View.OnClickListener() { //O Botão Cadastrar tem um Listener
                @Override
                public void onClick(View v) {
                    try {
                        email = edtEmail.getEditText().getText().toString().trim();
                        senha = edtSenha.getEditText().getText().toString().trim();
                        nome = edtNome.getEditText().getText().toString().trim();
                        cidade = edtCidade.getEditText().getText().toString().trim();
                        data = edtDtNasc.getEditText().getText().toString().trim();
                        data = data.replace(".", "/");
                        data = data.replace("-", "/");
                        data = data.replace(" ", "");

                        posicao = mPosicaoObjSpn.getPosicao();
                        time = edtTime.getEditText().getText().toString().trim();
                        siglaTime = edtSigla.getEditText().getText().toString().trim();

                        if (validarDados(v)) {
                            nome = nome.replaceAll("[ ]+", " "); //Retira os espaços em excesso
                            usuario.setEmail(email);
                            usuario.setSenha(senha);
                            usuario.setNome(nome);
                            usuario.setPerna(mPernaObjSpn.getNome());
                            usuario.setCidade(cidade);
                            usuario.setPosicao_nome(posicao.getNome());
                            usuario.setPosicao_sigla(posicao.getSigla());
                            usuario.setPosicao_num(posicao.getNum());

                            //usuario.setData_nascimento(ano + "/" + mes + "/" + dia);
                            usuario.setData_nascimento(data);
                            usuario.setLogado("S");
                            usuario.setFirebase_id(Base64Custom.codificar(usuario.getEmail()));

                            if (checkbox.isChecked()) { //USUÁRIO TEM TIME
                                meuTime.setNome(time);
                                meuTime.setSigla(siglaTime);
                                meuTime.setTime_usuario("S");
                                meuTime.setUsuario_id_firebase(usuario.getFirebase_id());

                                usuario.setTime_nome(meuTime.getNome());
                                usuario.setTime_sigla(meuTime.getSigla());
                                usuario.setTime_id_firebase(meuTime.getFirebase_id());

                            } else if (usuario.getTime_nome() != null) { //USUÁRIO TINHA TIME E NÃO TEM MAIS
                                tinhaTimeAntes = true;
                                usuario.setTime_nome(null);
                                usuario.setTime_id_firebase(null);
                                usuario.setTime_sigla(null);
                            }

                            botaoNaFrente(false);

                            if (operacao.equals(Constantes.INSERIR))
                                cadastrarUsuarioFirebase(v); //Chama o método para cadastrar no Firebase
                            else
                                usuario.atualizarUsuarioFirebase(CadastroActivity.this);

                        }
                    } catch (Exception e) {
                        botaoNaFrente(true);
                        trataExcecao(e);
                    }
                }
            });

            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkbox.isChecked()) {
                        if (operacao.equals(Constantes.EDITAR)) {
                            layoutTime.setVisibility(View.GONE);
                            layoutTimeBanco.setVisibility(View.VISIBLE);
                        } else {
                            layoutTime.setVisibility(View.VISIBLE);
                            layoutTimeBanco.setVisibility(View.GONE);
                        }
                        edtFoco.requestFocus();

                    } else {
                        layoutTime.setVisibility(View.GONE);
                        layoutTimeBanco.setVisibility(View.GONE);
                    }
                }
            });

            edtTimeBanco.getEditText().
                    setOnTouchListener(new View.OnTouchListener() {
                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                edtTimeBanco.requestFocus();
                                Intent it = new Intent(getApplicationContext(), TimeListActivity.class);
                                it.putExtra(Constantes.ADVERSARIO, false);
                                startActivityForResult(it, Constantes.REQUESTCODE_100);
                                return true;
                            }
                            return false;
                        }
                    });

            edtDtNasc.getEditText().
                    setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                edtDtNasc.requestFocus();
                                if (operacao.equals(Constantes.EDITAR)) {
                                    String ano = edtDtNasc.getEditText().getText().toString().substring(6, 10);
                                    String mes = edtDtNasc.getEditText().getText().toString().substring(3, 5);
                                    String dia = edtDtNasc.getEditText().getText().toString().substring(0, 2);
                                    mAno = Integer.parseInt(ano);
                                    mMes = Integer.parseInt(mes) - 1;
                                    mDia = Integer.parseInt(dia);
                                }
                                @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog
                                        (CadastroActivity.this, (view, year, month, dayOfMonth) ->
                                                edtDtNasc.getEditText().setText(
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

            spnPerna.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position,
                                           long id) {
                    mPernaObjSpn = (BasicoSpinner) parent.getSelectedItem();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

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

        } catch (Exception e) {
            botaoNaFrente(true);
            trataExcecao(e);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constantes.REQUESTCODE_100) {
                if (data != null && resultCode == Activity.RESULT_OK) {
                    //meuTime = (Time) data.getSerializableExtra(Constantes.RESULT);
                    meuTime = data.getParcelableExtra(Constantes.RESULT);
                    meuTime.setTime_usuario("S");
                    edtTimeBanco.getEditText().setText(meuTime.getNome() + " - " + meuTime.getSigla());
                    edtTime.getEditText().setText(meuTime.getNome());
                    edtSigla.getEditText().setText(meuTime.getSigla());
                } else {
                    if (meuTime != null)
                        meuTime = ControleBanco.getInstance()
                                .recuperaTimePorId(CadastroActivity.this, usuario, meuTime.getFirebase_id());
                    if (meuTime == null || meuTime.getNome() == null) {
                        edtTimeBanco.getEditText().setText("");
                        edtTime.getEditText().setText("");
                        edtSigla.getEditText().setText("");
                        checkbox.setChecked(false);
                        layoutTimeBanco.setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e) {
            botaoNaFrente(true);
            trataExcecao(e);
        }
    }

    private boolean validarDados(View v) {
        int cont = 0;

        if (validarEmail(v)) cont++;
        if (validarSenha(v)) cont++;
        if (validarNome(v)) cont++;
        //if (validarData(v)) cont++;
        if (validarSenha(v)) cont++;
        if (validarCidade(v)) cont++;
        if (validarTime(v)) cont++;
        if (validarSigla(v)) cont++;

        return cont == 7;
    }

    private boolean validarEmail(View v) {
        if (email.equals("") || email.isEmpty()) { //O Email não pode ser vazio ou nulo
            Snackbar.make(v, "Digite seu email!", Snackbar.LENGTH_SHORT).show();
            edtEmail.setError("Obrigatório");
            return false;
        } else if (email.contains(" ")) {
            Snackbar.make(v, "Email mal formatado.", Snackbar.LENGTH_SHORT).show();
            edtEmail.setError("Inválido");
            return false;
        }
        edtEmail.setError(null);
        return true;
    }

    private boolean validarSenha(View v) {
        if (operacao.equals(Constantes.EDITAR))
            return true;
        if (senha.equals("") || senha.isEmpty()) { //A Senha não pode ser vazia ou nula
            Snackbar.make(v, "Digite sua senha!", Snackbar.LENGTH_SHORT).show();
            edtSenha.setError("Obrigatório");
            return false;
        } else if (senha.contains(" ")) {
            Snackbar.make(v, "Senha mal formatada.", Snackbar.LENGTH_SHORT).show();
            edtSenha.setError("Inválido");
            return false;
        }
        edtSenha.setError(null);
        return true;
    }

    private boolean validarNome(View v) {
        if (nome.equals("") || nome.isEmpty()) { // O Nome não pode ser vazio ou nulo
            Snackbar.make(v, "Digite seu nome!", Snackbar.LENGTH_SHORT).show();
            edtNome.setError("Obrigatório");
            return false;
        } else if (!nome.matches("[a-zA-Z\\u00C0-\\u00FF ]+")) { //O nome deve conter letras
            Snackbar.make(v, "Nome Inválido.", Snackbar.LENGTH_SHORT).show();
            edtNome.setError("Inválido");
            return false;
        }
        edtNome.setError(null);
        return true;

    }

    private boolean validarCidade(View v) {
        if (cidade.isEmpty()) {
            Snackbar.make(v, "Digite sua cidade!", Snackbar.LENGTH_SHORT).show();
            edtCidade.setError("Obrigatório");
            return false;
        }
        edtCidade.setError(null);
        return true;
    }

    private boolean validarSigla(View v) {
        if (siglaTime.length() < 2 && checkbox.isChecked()) {
            Snackbar.make(v, "Sigla do time muito pequena.", Snackbar.LENGTH_SHORT).show();
            edtSigla.setError("Mínimo 2");
            return false;
        }
        edtSigla.setError(null);
        return true;
    }

    private boolean validarTime(View v) {
        if (time.length() < 2 && checkbox.isChecked()) {
            Snackbar.make(v, "Nome do time muito pequeno.", Snackbar.LENGTH_SHORT).show();
            edtTime.setError("Mínimo 3");
            return false;
        }
        edtTime.setError(null);
        return true;
    }

    private boolean validarData(View v) {
        Date dataInformada = Conversao.StringParaData(data, "dd/MM/yyyy");
        Date dataAgora = Conversao.StringParaData(dataAtual, "dd/MM/yyyy");

        if (dataInformada == null || data.length() != 10/*dia == null || dia.isEmpty() ||
                mes == null || mes.isEmpty() ||
                ano == null || ano.isEmpty() ||*/) {
            Snackbar.make(v, "Data de nascimento inválida.", Snackbar.LENGTH_SHORT).show();
            edtDtNasc.setError("Inválido");
            return false;
        }

        ano = data.substring(6, 10);
        mes = data.substring(3, 5);
        dia = data.substring(0, 2);

        if (dataAtual.equals(data) || dataInformada.after(dataAgora) || Integer.parseInt(dia) < 1 ||
                Integer.parseInt(dia) > 31 || Integer.parseInt(mes) < 1 || Integer.parseInt(mes) > 12 ||
                Integer.parseInt(ano) > mAno) {
            Snackbar.make(v, "Data de nascimento inválida.", Snackbar.LENGTH_SHORT).show();
            edtDtNasc.setError("Inválido");
            return false;
        }
        edtDtNasc.setError(null);
        edtDtNasc.getEditText().setText(data);

        return true;
    }

    private void cadastrarUsuarioFirebase(View view) {
        try {
            autenticacao = ConfiguracaoFirebase.getAutenticacao(); //Chama o método que retorna o atributo estático para a autenticação do Firebase
            View finalView = view;

            autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()) //Cria umdataInformada usuário com o email e senha passados
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            try {
                                if (task.isSuccessful()) {
                                    usuario.cadastrarUsuarioFirebase();
                                    usuario = usuario.cadastrarUsuarioBanco(CadastroActivity.this);
                                    if (checkbox.isChecked()) {
                                        meuTime.cadastrarTimeFirebase(usuario, CadastroActivity.this, dataAtual);

                                    } else {
                                        entrarNoApp();
                                    }

                                } else { //Caso o Login não tenha sido bem sucedido
                                    botaoNaFrente(true);
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthWeakPasswordException e) {
                                        Alerta.exibeSnackbarCurto(finalView, "Senha precisa de pelo menos 6 caracteres válidos!");
                                        Alerta.gravarExcecao(TAG, e);

                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        Alerta.exibeSnackbarCurto(finalView, "Email inválido!");
                                        Alerta.gravarExcecao(TAG, e);

                                    } catch (FirebaseAuthUserCollisionException e) {
                                        Alerta.exibeSnackbarCurto(finalView, "Email já cadastrado!");
                                        Alerta.gravarExcecao(TAG, e);

                                    } catch (Exception e) {
                                        Alerta.exibeSnackbarCurto(finalView, Constantes.MSG_GENERICA_ERRO);
                                        Alerta.gravarExcecao(TAG, e);
                                    }
                                    Log.e("CreateUser", "ERRO: Usuário não cadastrado!");

                                }
                            } catch (Exception e) {
                                botaoNaFrente(true);
                                trataExcecao(e);
                            }
                        }
                    });
        } catch (Exception e) {
            botaoNaFrente(true);
            trataExcecao(e);
        }
    }

    public void entrarNoApp() {
        Log.i("CreateUser", "Usuário cadastrado!");
        Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
        finish(); //Finaliza esta activity
        Intent intent = new Intent(CadastroActivity.this, MenuDrawerActivity.class);
        startActivity(intent); //Vai para a Activity de Login
    }

    public void voltaPerfil() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        Toast.makeText(getApplicationContext(), "Dados atualizados!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void resetaTimeUsuario(Usuario usuario, Activity activity) {
        try {
            View view1 = activity.findViewById(android.R.id.content);
            DatabaseReference usuariosFB = reference.child("usuarios").child(usuario.getFirebase_id())
                    .child("times");
            usuariosFB.addValueEventListener(new ValueEventListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Time post = postSnapshot.getValue(Time.class);
                            post.setFirebase_id(postSnapshot.getKey());
                            post.setUsuario_id_firebase(usuario.getFirebase_id());
                            if (post.getTime_usuario().equals("S")) {
                                post.setTime_usuario("N");
                                post.atualizarTimeFirebase(CadastroActivity.this);
                            }
                            if (post.atualizarTimeBanco(activity) < 1)
                                post.cadastrarTimeBanco(activity);
                        }
                        if (!tinhaTimeAntes)
                            meuTime.atualizarTimeFirebase(CadastroActivity.this);
                        usuariosFB.removeEventListener(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Alerta.exibeDialog(activity.getApplicationContext(), "Erro", Constantes.MSG_GENERICA_ERRO,
                                R.string.string_ok);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Alerta.exibeSnackbarCurto(view1, Constantes.MSG_ERRO_CONEXAO);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Alerta.exibeDialog(activity.getApplicationContext(), "Erro", Constantes.MSG_GENERICA_ERRO,
                    R.string.string_ok);
        }
    }


    public void atualizarInfosFirebase() throws Exception {
        int a = usuario.atualizarUsuarioBanco(CadastroActivity.this);
        if (checkbox.isChecked()) { // USUÁRIO COM TIME
            resetaTimeUsuario(usuario, this);

        } else if (tinhaTimeAntes) { //USUÁRIO TINHA TIME E NÃO TEM MAIS
            resetaTimeUsuario(usuario, this);
        } else {
            voltaPerfil();
        }

    }

    public void atualizarInfos2Firebase() throws Exception {
        int a = usuario.atualizarUsuarioBanco(CadastroActivity.this);
        if (meuTime.atualizarTimeBanco(CadastroActivity.this) < 1) //USUÁRIO JÁ TINHA TIME ANTES
            meuTime = meuTime.cadastrarTimeBanco(CadastroActivity.this); //USUÁRIO NÃO TINHA TIME ANTES
        voltaPerfil();
    }

    public void atualizarInfos3Firebase() throws Exception {
        meuTime.removerTimeBanco(CadastroActivity.this);
        voltaPerfil();
    }


    private void carregarDados() throws Exception {
        usuario = ControleBanco.getInstance().recuperaUsuarioLogado(this);
        meuTime = ControleBanco.getInstance().recuperaTimePrincipalUsuario(this, usuario);
        edtEmail.getEditText().setText(usuario.getEmail());
        edtSenha.setVisibility(View.GONE);
        edtNome.getEditText().setText(usuario.getNome());
        Date data = Conversao.StringParaData(usuario.getData_nascimento(), "dd/MM/yyyy");
        String dataFormatada = Conversao.DataParaString(data, "dd/MM/yyyy");
        String dia = "" + (data.getDay() < 10 ? "0" + data.getDay() : data.getDay());
        String mes = "" + (data.getMonth() + 1 < 10 ? "0" + (data.getMonth() + 1) : (data.getMonth() + 1));
        String ano = "" + data.getYear();
        //String dataFormatada = dia + "/" + mes + "/" + ano;
        edtDtNasc.getEditText().setText(dataFormatada);
        edtCidade.getEditText().setText(usuario.getCidade());
        if (meuTime != null) {
            if (meuTime.getNome() != null && !meuTime.getNome().equals("") && !meuTime.getNome().equals("null")) {
                edtTime.getEditText().setText(meuTime.getNome());
                if (meuTime.getSigla() != null)
                    edtSigla.getEditText().setText(meuTime.getSigla());
                edtTimeBanco.getEditText().setText(meuTime.getNome() + " - " + meuTime.getSigla());
                layoutTimeBanco.setVisibility(View.VISIBLE);
                layoutTime.setVisibility(View.GONE);
                checkbox.setChecked(true);
            }
        }

        for (PosicaoSpinner posicaoSpinner : posicaoSpinnerList) {
            if (usuario.getPosicao_num() == (posicaoSpinner.getPosicao().getNum())) {
                spnPosicao.setSelection(posicaoSpinner.getId());
                break;
            }
        }

        for (BasicoSpinner basicoSpinner : pernaSpinnerList) {
            if (usuario.getPerna().equals(basicoSpinner.getNome())) {
                spnPerna.setSelection(basicoSpinner.getId());
                break;
            }
        }

    }

    private void getDataCorrente() {
        mCurrentDate = Calendar.getInstance();
        mDia = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        mMes = mCurrentDate.get(Calendar.MONTH);
        mAno = mCurrentDate.get(Calendar.YEAR);
        //Preencher os componentes qdo for um novo apontamento
        edtDtNasc.getEditText().setText((mDia < 10 ? "0" + mDia : mDia) + "/" +
                ((mMes + 1) < 10 ? "0" + (mMes + 1) : (mMes + 1)) + "/" + mAno);
        //edtDtNasc.getEditText().setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
        dataAtual = edtDtNasc.getEditText().getText().toString();
    }

    private void inicializarComponentes() throws Exception {
        edtEmail = findViewById(R.id.edt_perfil_email);
        edtSenha = findViewById(R.id.edt_perfil_senha);
        edtNome = findViewById(R.id.edt_perfil_nome);
        edtDtNasc = findViewById(R.id.edt_perfil_data);
        edtCidade = findViewById(R.id.edt_perfil_cidade);
        spnPerna = findViewById(R.id.spn_perfil_perna);
        spnPosicao = findViewById(R.id.spn_perfil_posicao);
        edtTime = findViewById(R.id.edt_perfil_time);
        edtSigla = findViewById(R.id.edt_perfil_sigla);
        checkbox = findViewById(R.id.chk_perfil_checkbox);
        btnCadastrar = findViewById(R.id.btn_perfil_entrar);
        layoutTime = findViewById(R.id.layout_perfil_time);
        edtFoco = findViewById(R.id.getFoco);
        progressBar = findViewById(R.id.progress_cadastro);
        edtTimeBanco = findViewById(R.id.edt_perfil_timeBanco);
        layoutTimeBanco = findViewById(R.id.layout_perfil_timeBanco);
        //edtDtNasc.getEditText().addTextChangedListener(mTxtEdtWatcherData);
        carregarSpinners();
    }

    private void carregarSpinners() throws Exception {
        carregarSpinnerPerna();
        carregarSpinnerPosicao();
    }

    private void carregarSpinnerPerna() throws Exception {
        pernaSpinnerList = new ArrayList<>();
        pernaSpinnerList.add(new BasicoSpinner(0, "Direita"));
        pernaSpinnerList.add(new BasicoSpinner(1, "Esquerda"));
        pernaSpinnerList.add(new BasicoSpinner(2, "Ambidestro"));

        ArrayAdapter<BasicoSpinner> adapter = new ArrayAdapter(
                CadastroActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                pernaSpinnerList);
        spnPerna.setAdapter(adapter);
    }

    private void carregarSpinnerPosicao() throws Exception {
        posicaoSpinnerList = new ArrayList<>();
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
                CadastroActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                posicaoSpinnerList);
        spnPosicao.setAdapter(adapter);
    }

    private void botaoNaFrente(boolean valor) {
        if (valor) {
            btnCadastrar.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        } else {
            btnCadastrar.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void trataExcecao(Exception e) {
        botaoNaFrente(true);
        e.printStackTrace();
        View view = findViewById(android.R.id.content);
        Alerta.exibeSnackbarLongo(view, Constantes.MSG_GENERICA_ERRO);
    }

}