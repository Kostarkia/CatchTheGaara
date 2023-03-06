package com.kostarkia.catchthegaara;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Random random;
    private Runnable runnable;
    private Handler handler;
    private Button buttonStart;
    int score;

    private TextView textTimer, textScore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageGaara);
        textTimer = findViewById(R.id.textTimer);
        textScore = findViewById(R.id.textScore);
        buttonStart = findViewById(R.id.buttonStart);

        score = 0;

        buttonStart.setOnClickListener(view -> {
            changedImage();

            clockDown();

            buttonStart.setEnabled(false);
        });
    }

    public void buttonGaara(View view) {
        score++;

        textScore.setText(getString(R.string.score) +" "+ score);
    }

    public void changedImage() {
        int gridWidth = 25;
        int gridHeight = 35;

        handler = new Handler();
        int delay = 500;//0.5 sn

        runnable = new Runnable() {
            @Override
            public void run() {
                random = new Random();
                int cellX = random.nextInt(gridWidth);
                int cellY = random.nextInt(gridHeight);

                int x = cellX * gridWidth;
                int y = cellY * gridHeight;

                imageView.setX(x);
                imageView.setY(y);

                handler.postDelayed(this, delay);
            }
        };

        handler.postDelayed(runnable, delay);
    }

    public void clockDown() {
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                textTimer.setText(getString(R.string.time) + " "+ l / 1000);
            }

            @Override
            public void onFinish() {
                handler.removeCallbacks(runnable);
                textTimer.setText(getString(R.string.time_out));
                imageView.setEnabled(false);

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle(getString(R.string.restart));
                alert.setMessage(getString(R.string.are_you_sure_to_restart_game));
                alert.setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                });

                alert.setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {
                    Toast.makeText(MainActivity.this, getString(R.string.game_over), Toast.LENGTH_SHORT).show();
                });

                alert.show();
            }
        }.start();
    }
}