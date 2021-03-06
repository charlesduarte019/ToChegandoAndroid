package br.eti.c019.tochegando;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import localStorage.Session;
import network.NetworkManager;
import network.NetworkObserved;
import network.VerifyConnection;

public class ChegandoActivity extends AppCompatActivity implements NetworkObserved {

    // Botões e Lista
    private Button bt05min, bt10min, bt15min, bt20min, btAux;
    private Spinner spinner;
    private ListView listView;

    // Session
    private Session session;

    // Lista Filhos e Registro
    private HashMap<String, Integer> hashFilhos;
    private ArrayList<String> arrayFilhos;
    private ArrayList<String> arrayRegistro;

    //Requisição
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private NetworkManager networkManager;
    private static final String URL_TEMPO = "EnviarTempo.php";
    private static final String URL_BUSCA_FILHO = "BuscaFilhosResponsavel.php";
    private Map<String, String> params;

    //Alert
    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    private void onCLickListener() {

        bt05min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btAux = bt05min;
                requestTempo(String.valueOf(spinner.getSelectedItem()), "05");
            }
        });

        bt10min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btAux = bt10min;
                requestTempo(String.valueOf(spinner.getSelectedItem()), "10");
            }
        });

        bt15min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btAux = bt15min;
                requestTempo(String.valueOf(spinner.getSelectedItem()), "15");
            }
        });

        bt20min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btAux = bt20min;
                requestTempo(String.valueOf(spinner.getSelectedItem()), "20");
            }
        });
    }

    private void requestTempo(final String valueFilhos, final String valueTempo) {

        if(VerifyConnection.verifyConnection(ChegandoActivity.this)){
            builder.setMessage("Pegará seu filho(a) " + valueFilhos + " daqui à 00:" + valueTempo + ":00 minutos?").setCancelable(false)
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            params.clear();
                            params.put("tempo", valueTempo);
                            params.put("idResp", session.getIdResponIn().toString());
                            params.put("idAlun", String.valueOf(hashFilhos.get(valueFilhos)));
                            networkManager.post(params, URL_TEMPO);

                            dialog.dismiss();

                        }
                    }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            alertDialog = builder.create();
            alertDialog.setTitle("Confirmar chegada");
            alertDialog.show();
        }else{
            Toast.makeText(ChegandoActivity.this, "Sem conexão com internet", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void doOnResponse(final String response) {

        if (response.contains("id") && response.contains("nome")) { // Filhos

            try {
                jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = new JSONObject(jsonArray.getString(i));
                    hashFilhos.put(jsonObject.getString("nome"), jsonObject.getInt("id"));
                    arrayFilhos.add(jsonObject.getString("nome"));
                }

                if (!arrayFilhos.isEmpty()) {

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayFilhos);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (response.contains("nome") && response.contains("tempo")) { // Registro

            try {
                jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = new JSONObject(jsonArray.getString(i));
                    arrayRegistro.add(jsonObject.getString("nome"));
                    arrayRegistro.add(jsonObject.getString("tempo"));
                }

                if (!arrayRegistro.isEmpty()) {
                    ArrayAdapter<String> adapterRegistro = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, arrayRegistro);
                    listView.setAdapter(adapterRegistro);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (response.contains("status")) { // Enviar

            try {
                jsonObject = new JSONObject(response);


                bt05min.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#079EE3")));
                bt10min.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#079EE3")));
                bt15min.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#079EE3")));
                bt20min.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#079EE3")));

                btAux.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4DD0E1")));

                Toast.makeText(ChegandoActivity.this, jsonObject.getString("status"), Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onErrorResponse() {
        Toast.makeText(ChegandoActivity.this, "Erro", Toast.LENGTH_SHORT).show();
    }

    private void verificarConexao(Context context) {
        if (!VerifyConnection.verifyConnection(context)) {
            Toast.makeText(ChegandoActivity.this, "Sem conexão com internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(this);
        setContentView(R.layout.activity_chegando);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        networkManager = new NetworkManager();
        networkManager.setNetworkObserved(this);
        params = new HashMap<>();

        hashFilhos = new HashMap<>();
        arrayFilhos = new ArrayList<>();
        arrayRegistro = new ArrayList<>();

        bt05min = (Button) findViewById(R.id.chegando_bt_5min);
        bt10min = (Button) findViewById(R.id.chegando_bt_10min);
        bt15min = (Button) findViewById(R.id.chegando_bt_15min);
        bt20min = (Button) findViewById(R.id.chegando_bt_20min);
        btAux = new Button(this);
        spinner = (Spinner) findViewById(R.id.listaFilhos);
        builder = new AlertDialog.Builder(this);

        verificarConexao(this);
        buscaFilhos();
        onCLickListener();
    }

    private void buscaFilhos() {

        Thread t = new Thread() {

            public void run() {

                params.clear();
                params.put("id", session.getIdResponIn().toString());
                networkManager.post(params, URL_BUSCA_FILHO);

            }

        };

        t.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.menu_chegando, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Menu_configuracao) {
            return true;
        }

        if (id == R.id.chegando_menu_sair) {
            session.setLogin(false);
            Intent intent = new Intent(ChegandoActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
