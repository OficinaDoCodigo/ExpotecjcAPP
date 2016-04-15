package com.ifrn.expotec;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ifrn.expotec.adapters.Controls;
import com.ifrn.expotec.adapters.DAO;
import com.ifrn.expotec.adapters.HttpConnection;
import com.ifrn.expotec.interfaces.AsyncResponse;
import com.ifrn.expotec.models.User;

import org.json.JSONException;

import java.util.HashMap;

public class Login extends AppCompatActivity implements AsyncResponse{
    private Toolbar tbLogin ;
    private TextInputLayout ipEmail;
    private EditText editEmail;
    private TextInputLayout ipSenha;
    private EditText editSenha;
    private Button btnEntrar;
    private LinearLayout llLogin;
    private Button btnCadastrar;
    private Button btnEsqueceuSenha;
    private HashMap<String,String> login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        llLogin = (LinearLayout) findViewById(R.id.llLogin);
        tbLogin = (Toolbar) findViewById(R.id.tbLogin);
        btnEntrar = (Button)findViewById(R.id.btnEntrar);
        btnCadastrar = (Button)findViewById(R.id.btnCadastrar);
        btnEsqueceuSenha = (Button)findViewById(R.id.btnEsqueceuSenha);
        editEmail = (EditText) findViewById(R.id.edtEmail);
        editSenha = (EditText) findViewById(R.id.edtSenha);
        ipEmail = (TextInputLayout) findViewById(R.id.iptEmail);
        ipSenha = (TextInputLayout) findViewById(R.id.iptSenha);
        login = new HashMap<>();
        setSupportActionBar(tbLogin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Controls.isValidEmailAddress(editEmail,ipEmail) && Controls.check(ipSenha,editSenha,2,"no mínimo 6 dígitos!") ){
                    login.put("email",editEmail.getText().toString());
                    login.put("password",editSenha.getText().toString());

                    HttpConnection httpConnection = new HttpConnection(Login.this,login);
                    httpConnection.delegate = Login.this;
                    httpConnection.execute("http://exporest.hol.es/v1/auth","post");
                }
            }
        });
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Cadastro.class));
            }
        });
    }

    @Override
    public void processFinish(String output) {
        try {
            User userLogged = Controls.getUser(output);
            if (userLogged.getId().equals("null")){
                Snackbar.make(llLogin,"Email ou senha incorretos!",Snackbar.LENGTH_LONG);
            }else{
                try{
                    userLogged.setPassword(login.get("password"));
                    DAO dao = new DAO(this);
                    dao.insert(userLogged);
                    dao.close();
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
