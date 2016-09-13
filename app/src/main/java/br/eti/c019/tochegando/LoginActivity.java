package br.eti.c019.tochegando;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import localStorage.Session;
import network.NetworkManager;
import network.NetworkObserved;

public class LoginActivity extends AppCompatActivity implements NetworkObserved {

    private TextView tvEmail, tvErroLogin;
    private EditText etSenha;
    private Button btLogin;

    private NetworkManager networkManager;
    private static final String URL_LOGIN = "VerificarLogin.php";
    private Map<String, String> params;
    private Session session;

    private Intent intent;

    private JSONObject object;

//    private DataUser dataUser;

    private void onClickListener() {



        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tvEmail.getText().toString();
                String senha = etSenha.getText().toString();

                if (!email.isEmpty() && (email.contains("@") && !email.equals("@")) && !senha.isEmpty()) {

                    params.put("email", email);
                    params.put("senha", senha);
                    networkManager.post(params, URL_LOGIN);

                } else if (email.isEmpty() && senha.isEmpty()) {
                    tvErroLogin.setText("Preencha os campos de EMAIL e SENHA");
                } else {
                    if (email.isEmpty()) {
                        tvErroLogin.setText("Preenha o campo EMAIL");
                    } else if (!email.contains("@") || email.equals("@") || (email.length() <= 4)) {
                        tvErroLogin.setText("Campo EMAIL inválido");
                    } else if (senha.isEmpty()) {
                        tvErroLogin.setText("Preencha o campo SENHA");
                    }
                }
            }
        });
    }

    @Override
    public void doOnResponse(String response) {

        if (!response.isEmpty()) {

            try {
                object = new JSONObject(response);
                int id = object.getInt("id");
                boolean status = object.getBoolean("status");

                if (id != 0 && status) {
                    session = new Session(this);
                    session.setLogin(true);
                    session.setID(id);
                    intent = new Intent(LoginActivity.this, ChegandoActivity.class);
                    finish();
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        networkManager = new NetworkManager();
        networkManager.setNetworkObserved(this);
        params = new HashMap<>();
        session = new Session(this);
        intent = new Intent();

        tvEmail = (TextView) findViewById(R.id.login_tv_email);
        tvErroLogin = (TextView) findViewById(R.id.login_tv_erroLogin);
        etSenha = (EditText) findViewById(R.id.login_et_senha);
        btLogin = (Button) findViewById(R.id.login_bt_login);

        onClickListener();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_about) {
            Intent intent = new Intent(LoginActivity.this, About.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
