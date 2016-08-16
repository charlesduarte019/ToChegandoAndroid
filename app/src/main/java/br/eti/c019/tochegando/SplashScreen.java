package br.eti.c019.tochegando;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import localStorage.Session;

public class SplashScreen extends AppCompatActivity{

    private Session session;

    private void startVerificarLogin() {

        Thread t = new Thread() {
            public void run() {

//                new Timer().schedule(new TimerTask() {
//
//                    @Override
//                    public void run() {
                        if (session.getLoggedIn()) {
                            Intent intent = new Intent();
                            intent.setClass(SplashScreen.this, ChegandoActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent();
                            intent.setClass(SplashScreen.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
//                    }
//
//                }, 1000);
            }

        };

        t.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        session = new Session(this);

        startVerificarLogin();

    }

}
