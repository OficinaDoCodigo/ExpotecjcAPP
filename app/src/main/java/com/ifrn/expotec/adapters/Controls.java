package com.ifrn.expotec.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ifrn.expotec.R;
import com.ifrn.expotec.models.Atividade;
import com.ifrn.expotec.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Fabiano on 11/04/2016.
 */
public class Controls {
    public static void alert(Context context, String mensage) {
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(context);
        alBuilder.setMessage(mensage).setNeutralButton("okey", null).show();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean isValidEmailAddress(EditText editText, TextInputLayout textInputLayout) {
        String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

        boolean isValid = editText.getText().toString().matches(EMAIL_REGEX);
        if (isValid){
            textInputLayout.setErrorEnabled(false);
            return true;
        }else{
            textInputLayout.setError("Email inválido!");
            editText.requestFocus();
            return false;
        }
    }
    public static boolean check(TextInputLayout textInputLayout,EditText editText,int condicao,String mensage){
        boolean isValid = true;
        if(editText.getText().toString().trim().length() < condicao){
            textInputLayout.setError(mensage);
            editText.requestFocus();
            isValid = false;
        }else{
            textInputLayout.setErrorEnabled(false);
        }
        return  isValid;
    }
    public static User getUser(String data) throws JSONException {
        User user = new User();
        JSONObject jSonUser = new JSONObject(data).getJSONObject("user");
        user.setId(jSonUser.getString("id"));
        user.setNome(jSonUser.getString("name"));
        user.setEmail(jSonUser.getString("email"));
        user.setPhoto(jSonUser.getString("photo"));
        return user;
    }
    public static List<Integer> getMinhasAtividades(String data) throws JSONException {
        JSONArray jSonAtividades = new JSONObject(data).getJSONArray("activities");
        List<Integer> minhasAtividades = new ArrayList<>();
        for (int i = 0; i <jSonAtividades.length() ; i++) {
            minhasAtividades.add(jSonAtividades.getInt(i));
        }
        return minhasAtividades;
    }
    public static List<Atividade> getAtividades(JSONArray jsonArray) throws JSONException {
        List<Atividade> atividadeList = new ArrayList<>();
        for (int i = 0; i <jsonArray.length() ; i++) {
            JSONObject jSonAtividade = (JSONObject) jsonArray.get(i);
            Atividade atividade = new Atividade();
            atividade.setId(jSonAtividade.getInt("id"));
            atividade.setDescricao(jSonAtividade.getString("description"));
            atividade.setDuracao(jSonAtividade.getString("duration"));
            atividade.setTipo(jSonAtividade.getString("type"));
            atividade.setTitulo(jSonAtividade.getString("title"));
            atividade.setVagas(jSonAtividade.getString("vacancies"));

            JSONObject jSonUser = jSonAtividade.getJSONObject("speaker");
            User user = new User();
            user.setId(jSonUser.getString("id"));
            user.setNome(jSonUser.getString("name"));
            user.setEmail(jSonUser.getString("email"));
            user.setPhoto(jSonUser.getString("photo"));
            atividade.setUser(user);
            atividadeList.add(atividade);
        }
        return atividadeList;
    }
    public static List<Atividade> recarregar(String data){
        try {
            JSONObject jsonObject = new JSONObject(data).getJSONObject("results");

            JSONArray jsonResult = jsonObject.getJSONArray("activity");
            return getAtividades(jsonResult);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    public static HashMap<String, List<Atividade>> getData(String data, TabLayout tabLayout) {
        HashMap<String, List<Atividade>> atividadeHash = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(data);

            JSONArray jsonResults = jsonObject.getJSONArray("results");
            JSONArray jsoncategories = jsonObject.getJSONArray("categories");

            if (tabLayout != null) {
                for (int i = 0; i < jsoncategories.length(); i++) {
                    if (i == 0 && tabLayout.getTabCount() > 0) {
                        break;
                    }
                    tabLayout.addTab(tabLayout.newTab().setText(jsoncategories.get(i).toString().toLowerCase()));
                }
            }

            JSONArray jSonMinicursos = jsonResults.getJSONObject(0).getJSONArray("minicurso");
            JSONArray jSonOficinas = jsonResults.getJSONObject(1).getJSONArray("oficina");
            JSONArray jSonPalestra = jsonResults.getJSONObject(2).getJSONArray("palestra");
            JSONArray jSonMesaRedonda = jsonResults.getJSONObject(3).getJSONArray("mesa Redonda");
            JSONArray jSonResumo = jsonResults.getJSONObject(4).getJSONArray("resumo");
            JSONArray jSonOutro = jsonResults.getJSONObject(5).getJSONArray("outro");

            atividadeHash.put(tabLayout.getTabAt(0).getText().toString(),getAtividades(jSonMinicursos));
            atividadeHash.put(tabLayout.getTabAt(1).getText().toString(),getAtividades(jSonOficinas));
            atividadeHash.put(tabLayout.getTabAt(2).getText().toString(),getAtividades(jSonPalestra));
            atividadeHash.put(tabLayout.getTabAt(3).getText().toString(),getAtividades(jSonMesaRedonda));
            atividadeHash.put(tabLayout.getTabAt(4).getText().toString(),getAtividades(jSonResumo));
            atividadeHash.put(tabLayout.getTabAt(5).getText().toString(),getAtividades(jSonOutro));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return atividadeHash;
    }
    public static Drawable setColor(Drawable drawable, int color){
        drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        return drawable;
    }
}