package com.ifrn.expotec;

import android.app.DatePickerDialog;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ifrn.expotec.adapters.Controls;
import com.ifrn.expotec.adapters.Mask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Cadastro extends AppCompatActivity {
    private AppCompatSpinner spnPerfil;
    private AppCompatSpinner spnSexo;
    private EditText edtDiaNasci;
    private Date date;
    private Toolbar tbCadastro;
    private Button btnCadastrar;
    private LinearLayout llMainCadastro;

    private EditText edtCpf;
    private TextInputLayout iptNome;
    private TextInputLayout iptCpf;
    private TextInputLayout iptEmail;
    private TextInputLayout iptSenha;
    private TextInputLayout iptDiaNasci;
    private TextInputLayout iptMatricula;
    private TextInputLayout iptMiniCurriculo;

    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtMatricula;
    private EditText edtSenha;
    private EditText edtMinicurriculo;
    private SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        date = new Date();
        instance();
        setSupportActionBar(tbCadastro);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edtCpf.addTextChangedListener(Mask.insert(Mask.CPF_MASK, edtCpf));
        ArrayAdapter<String> adtSpnPerfil = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);

        adtSpnPerfil.add("Aluno do IFRN");
        adtSpnPerfil.add("Servidor do IFRN");
        adtSpnPerfil.add("Externo");
        adtSpnPerfil.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spnPerfil.setAdapter(adtSpnPerfil);

        ArrayAdapter<String> adtSpnSexo = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        adtSpnSexo.add("Masculino");
        adtSpnSexo.add("Feminino");
        adtSpnSexo.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spnSexo.setAdapter(adtSpnSexo);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Controls.check(iptNome,edtNome,1,"Digite seu nome!")&& Mask.isCPF(iptCpf,edtCpf) && Controls.isValidEmailAddress(edtEmail,iptEmail)&&Controls.check(iptSenha,edtSenha,6,"No mínimo 6 dígitos!")&&Controls.check(iptDiaNasci,edtDiaNasci,1,"Digite o dia do seu nascimemto!")&&Controls.check(iptMiniCurriculo,edtMinicurriculo,1,"Digite o seu minicurriculo!")&&Controls.check(iptSenha,edtSenha,6,"No mínimo 6 dígitos!")&&Controls.check(iptDiaNasci,edtDiaNasci,1,"Digite o dia do seu nascimemto!")&&Controls.check(iptMiniCurriculo,edtMinicurriculo,1,"Digite o seu minicurriculo!")){
                    HashMap<String,String> dadosCadastro = new HashMap<>();
                    dadosCadastro.put("nome",edtNome.getText().toString());
                    dadosCadastro.put("cpf",Mask.unmask(edtCpf.getText().toString()));
                    dadosCadastro.put("email",edtEmail.getText().toString());
                    dadosCadastro.put("senha",edtSenha.getText().toString());
                    dadosCadastro.put("data",edtDiaNasci.getText().toString());
                    dadosCadastro.put("minicurriculo",edtMinicurriculo.getText().toString());
                    dadosCadastro.put("tipo",spnPerfil.getSelectedItem().toString());
                    dadosCadastro.put("sexo",spnSexo.getSelectedItem().toString());
                    if(spnPerfil.getSelectedItemPosition() != 2){
                        dadosCadastro.put("matricula",edtMatricula.getText().toString());
                    }
                }else{
                    Snackbar.make(llMainCadastro,"Informe todos os dados corretamente!",Snackbar.LENGTH_LONG).show();
                }
            }
        });

        spnPerfil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 2){
                    iptMatricula.setVisibility(View.VISIBLE);
                }else{
                    iptMatricula.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        edtDiaNasci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat ano = new SimpleDateFormat("yyyy");
                DateFormat mes = new SimpleDateFormat("MM");
                DateFormat dia = new SimpleDateFormat("dd");
                DatePickerDialog dlg = new DatePickerDialog(Cadastro.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dia = dayOfMonth+"";
                        String mes = (monthOfYear+1)+"";
                        String ano = year+"";
                        if (dia.length() == 1){
                            dia = "0"+dia;
                        }
                        if (mes.length() == 1){
                            mes = "0"+mes;
                        }
                        edtDiaNasci.setText(dia + "/" + mes + "/" + ano);
                    }
                }, Integer.parseInt(ano.format(date)), Integer.parseInt(mes.format(date)) - 1, Integer.parseInt(dia.format(date)));
                dlg.show();
            }
        });

        edtSenha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    switchCompat.setVisibility(View.VISIBLE);
                } else {
                    switchCompat.setVisibility(View.GONE);
                }
            }
        });
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // show password
                    edtSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    edtSenha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

    }
    public void instance(){
        spnPerfil = (AppCompatSpinner) findViewById(R.id.spnPerfil);
        spnSexo = (AppCompatSpinner) findViewById(R.id.spnSexo);
        tbCadastro = (Toolbar)findViewById(R.id.tbcadastro);
        btnCadastrar = (Button)findViewById(R.id.btnCadastrar);
        llMainCadastro = (LinearLayout) findViewById(R.id.llMainCadastro);
        edtDiaNasci = (EditText)findViewById(R.id.edtDiaNasci);
        switchCompat = (SwitchCompat) findViewById(R.id.switcher);
        edtCpf = (EditText)findViewById(R.id.edtCpf);
        edtNome = (EditText)findViewById(R.id.edtNome);
        edtMinicurriculo = (EditText)findViewById(R.id.edtMiniCurriculo);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtSenha = (EditText)findViewById(R.id.edtSenha);
        edtMatricula = (EditText)findViewById(R.id.edtMatricula);

        iptMatricula = (TextInputLayout)findViewById(R.id.iptMatricula);
        iptCpf = (TextInputLayout)findViewById(R.id.iptCPF);
        iptMiniCurriculo = (TextInputLayout)findViewById(R.id.iptMiniCurriculo);
        iptNome = (TextInputLayout)findViewById(R.id.iptNome);
        iptDiaNasci = (TextInputLayout)findViewById(R.id.iptDiaDaAvaliacao);
        iptSenha= (TextInputLayout)findViewById(R.id.iptSenha);
        iptEmail= (TextInputLayout)findViewById(R.id.iptEmail);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}

