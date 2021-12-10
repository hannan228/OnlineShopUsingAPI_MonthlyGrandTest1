package jav.app.monthlygrandtest1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private int SLEEP_TIMER = 2;
    Animation animBounce,animSlideUp,animSlideDown;
    ImageView imageView,imageView2;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageView = findViewById(R.id.basket);
        textView = findViewById(R.id.textView);

        animBounce = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
//        textView.setVisibility(View.VISIBLE);
        textView.startAnimation(animBounce);

        animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        imageView.startAnimation(animSlideUp);
        imageView.startAnimation(animSlideDown);



        Splashcreen aplashscreen = new Splashcreen();
        aplashscreen.start();

    }

    private class Splashcreen extends Thread {
        public void run() {
            try {
                sleep(1000 * SLEEP_TIMER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

                Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
                startActivity(intent);
                finish();

        }
    }
}