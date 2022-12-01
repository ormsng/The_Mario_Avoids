package com.example.mario_avoids;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private int enemy_height = 4;
    private int random_number = (int) Math.floor(Math.random()*3);
    private int curr_hero_x = 1;
    private int lives = 3;

    private ImageButton game_BTN_right;
    private ImageButton game_BTN_left;
    private ImageView[] game_IMG_hearts;
    private ImageView[] game_IMG_mario;
    private ImageView[] game_IMG_left_enemies, game_IMG_mid_enemies, game_IMG_right_enemies;
    private ImageView[][] game_IMG_columns;

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView game_background = findViewById(R.id.game_background);
        Glide
                .with(this)
                .load(R.drawable.mario_background)
                .centerCrop()
                .into(game_background);
        findViews();
        initButtons();
    }

    @Override
    protected void onStart() {
        super.onStart();
        timer = new Timer();
        game_IMG_columns[random_number][enemy_height].setVisibility(View.VISIBLE);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshUI();
                    }
                });
            }
        }, 0, 500);
    }

    private void refreshUI() {

        if(enemy_height>0){
            game_IMG_columns[random_number][enemy_height].setVisibility(View.INVISIBLE);
            game_IMG_columns[random_number][enemy_height-1].setVisibility(View.VISIBLE);
            enemy_height--;
        } else {
            if(curr_hero_x == random_number && lives > 0){
                vibrate(1000);
                lives--;
                Toast.makeText(getApplicationContext(),"COLLISION",Toast.LENGTH_LONG).show();
                game_IMG_hearts[lives].setVisibility(View.INVISIBLE);
            }

            game_IMG_columns[random_number][0].setVisibility(View.INVISIBLE);
            random_number = (int) Math.floor(Math.random()*3);
            enemy_height=4;
            game_IMG_columns[random_number][enemy_height].setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

    private void findViews() {
        game_BTN_right = findViewById(R.id.game_BTN_r);
        game_BTN_left = findViewById(R.id.game_BTN_l);

        game_IMG_mario = new ImageView[]{
                findViewById(R.id.game_IMG_mario_left),
                findViewById(R.id.game_IMG_mario_mid),
                findViewById(R.id.game_IMG_mario_right),
        };

        game_IMG_hearts = new ImageView[] {
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3),
        };


        // --------------- Find Obstacle Columns ---------------
        game_IMG_left_enemies = new ImageView[] {
                findViewById(R.id.game_IMG_col0_l),
                findViewById(R.id.game_IMG_col1_l),
                findViewById(R.id.game_IMG_col2_l),
                findViewById(R.id.game_IMG_col3_l),
                findViewById(R.id.game_IMG_col4_l),
        };

        game_IMG_mid_enemies = new ImageView[] {
                findViewById(R.id.game_IMG_col0_m),
                findViewById(R.id.game_IMG_col1_m),
                findViewById(R.id.game_IMG_col2_m),
                findViewById(R.id.game_IMG_col3_m),
                findViewById(R.id.game_IMG_col4_m),
        };

        game_IMG_right_enemies = new ImageView[] {
                findViewById(R.id.game_IMG_col0_r),
                findViewById(R.id.game_IMG_col1_r),
                findViewById(R.id.game_IMG_col2_r),
                findViewById(R.id.game_IMG_col3_r),
                findViewById(R.id.game_IMG_col4_r),
        };

        game_IMG_columns = new ImageView[][]{
                game_IMG_left_enemies,
                game_IMG_mid_enemies,
                game_IMG_right_enemies};

        // --------------- --------------- ---------------

    }

    private void move(boolean dir) {
        if(dir && curr_hero_x <=1){
            game_IMG_mario[curr_hero_x].setVisibility(View.INVISIBLE);
            curr_hero_x++;
            game_IMG_mario[curr_hero_x].setVisibility(View.VISIBLE);
        } else if (!dir && curr_hero_x >=1){
            game_IMG_mario[curr_hero_x].setVisibility(View.INVISIBLE);
            curr_hero_x--;
            game_IMG_mario[curr_hero_x].setVisibility(View.VISIBLE);
        }
    }

    private void initButtons() {
        game_BTN_left.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(false);
            }
        }));

        game_BTN_right.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(true);
            }
        }));
    }

    private void vibrate(int millisecond) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(millisecond, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(millisecond);
        }
    }
}

