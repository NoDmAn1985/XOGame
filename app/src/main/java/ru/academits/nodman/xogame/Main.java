package ru.academits.nodman.xogame;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageSwitcher;
import android.widget.RadioGroup;

import ru.academits.nodman.support.Sound;

public class Main extends AppCompatActivity {
    ImageSwitcher switcher;
    RadioGroup radioGroup;
    CheckBox checkIsWinnerGoX;
    int difficulty;
    Sound sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        switcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        switcher.setTag(switcher.getCurrentView().getTag());
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.check(R.id.difficulty1);
        checkIsWinnerGoX = (CheckBox) findViewById(R.id.toChangeSign);
        checkIsWinnerGoX.setChecked(true);

        sound = new Sound(this);
        sound.initBack();
        sound.play("back");
    }

    @Override
    protected void onResume() {
        super.onResume();
        sound.play("back");
    }

    @Override
    protected void onPause() {
        super.onPause();
        sound.pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sound.play("back");
    }

    @Override
    protected void onStop() {
        super.onStop();
        sound.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sound.stopBack();
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonPlay:
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.difficulty1:
                        difficulty = 1;
                        break;
                    case R.id.difficulty2:
                        difficulty = 2;
                        break;
                    default:
                        difficulty = 3;
                }
                Intent intent = new Intent(Main.this, Game.class);
                intent.putExtra("sign", switcher.getTag().toString());
                intent.putExtra("difficulty", difficulty);
                intent.putExtra("isWinnerGoX", checkIsWinnerGoX.isChecked());
                startActivity(intent);
                break;
            case R.id.buttonExit:
                finish();
                break;
            case R.id.difficulty1:
                difficulty = 1;
                break;
            case R.id.difficulty2:
                difficulty = 2;
                break;
            case R.id.difficulty3:
                difficulty = 3;
                break;
        }
    }

    public void onSwitcherClick(View view) {
        switcher.showNext();
        switcher.setTag(switcher.getCurrentView().getTag());
    }
}
