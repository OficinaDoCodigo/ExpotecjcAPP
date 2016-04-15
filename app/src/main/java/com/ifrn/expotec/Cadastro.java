package com.ifrn.expotec;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.ifrn.expotec.adapters.Controls;
import com.ifrn.expotec.adapters.Mask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Cadastro extends AppCompatActivity {
    private AppCompatSpinner spnPerfil;
    private AppCompatSpinner spnSexo;
    private EditText edtDiaNasci;
    private Date date;
    private EditText edtCpf;
    private Toolbar tbCadastro;
    private Button btnCadastrar;
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
                Controls.alert(Cadastro.this,Mask.isCPF(Mask.unmask(edtCpf.getText().toString()))+"");
            }
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

    }
    public void instance(){
        spnPerfil = (AppCompatSpinner) findViewById(R.id.spnPerfil);
        spnSexo = (AppCompatSpinner) findViewById(R.id.spnSexo);
        tbCadastro = (Toolbar)findViewById(R.id.tbcadastro);
        btnCadastrar = (Button)findViewById(R.id.btnCadastrar);
        edtDiaNasci = (EditText)findViewById(R.id.edtDiaNasci);
        edtCpf = (EditText)findViewById(R.id.edtCpf);
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

