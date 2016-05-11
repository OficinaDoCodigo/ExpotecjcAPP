package com.ifrn.expotec;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ifrn.expotec.adapters.Controls;
import com.ifrn.expotec.adapters.DAO;
import com.ifrn.expotec.adapters.Progress;
import com.ifrn.expotec.interfaces.AsyncResponse;
import com.ifrn.expotec.models.User;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

public class Login extends AppCompatActivity implements AsyncResponse{
    private Toolbar tbLogin ;
    private TextInputLayout ipEmail;
    private EditText editEmail;
    private TextInputLayout ipSenha;
    private EditText editSenha;
    private Button btnEntrar;
    private LinearLayout llLogin;
    private Button btnCadastrar;
    private SwitchCompat switchCompat;
    private HashMap<String,String> login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        llLogin = (LinearLayout) findViewById(R.id.llLogin);
        tbLogin = (Toolbar) findViewById(R.id.tbLogin);
        btnEntrar = (Button)findViewById(R.id.btnEntrar);
        btnCadastrar = (Button)findViewById(R.id.btnCadastrar);
        editEmail = (EditText) findViewById(R.id.edtEmail);
        editSenha = (EditText) findViewById(R.id.edtSenha);
        ipEmail = (TextInputLayout) findViewById(R.id.iptEmail);
        ipSenha = (TextInputLayout) findViewById(R.id.iptSenha);
        switchCompat = (SwitchCompat) findViewById(R.id.switcher);
        login = new HashMap<>();
        setSupportActionBar(tbLogin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Controls.isValidEmailAddress(editEmail,ipEmail) && Controls.check(ipSenha,editSenha,2,"no mínimo 6 dígitos!") ){
                    login.put("email",editEmail.getText().toString());
                    login.put("password",editSenha.getText().toString());

                    Progress progress = new Progress(Login.this,login);
                    progress.delegate = Login.this;
                    progress.execute("http://exporest.hol.es/v1/auth","post");
                }
            }
        });
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www2.ifrn.edu.br/expotecjc/view/cadastro/"));
                startActivity(it);
            }
        });
        editSenha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

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
                    editSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    editSenha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }

    @Override
    public void processFinish(String output) {
        try {
            User userLogged = Controls.getUser(output);
            List<Integer> minhasAtividades = Controls.getMinhasAtividades(output);
            if (userLogged.getId().equals("null")){
                Snackbar.make(llLogin,"Email ou senha incorretos!",Snackbar.LENGTH_LONG).show();
            }else{
                try{
                    userLogged.setPassword(login.get("password"));
                    DAO dao = new DAO(this);
                    dao.insert(userLogged,true);
                    dao.insert(minhasAtividades,Integer.parseInt(userLogged.getId()));
                    dao.close();

                    Intent it = new Intent(this, MinhaConta.class);
                    it.putExtra("logged",userLogged);
                    it.putExtra("recarregar","");
                    startActivity(it);


                    Login.this.finish();
                }catch (Exception e){

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
