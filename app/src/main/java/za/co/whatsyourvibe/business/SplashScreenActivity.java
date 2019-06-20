package za.co.whatsyourvibe.business;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import za.co.whatsyourvibe.business.activities.authentication.SignInActivity;


public class SplashScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ViewPump.init(ViewPump.builder()
                              .addInterceptor(new CalligraphyInterceptor(
                                      new CalligraphyConfig.Builder()
                                              .setDefaultFontPath("fonts/regular.ttf")
                                              .setFontAttrId(R.attr.fontPath)
                                              .build()))
                              .build());
        ImageView logo = findViewById(R.id.splash_screen_ivLogo);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.animation);
        logo.startAnimation(animation);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                if (firebaseAuth.getCurrentUser() !=null){
                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(SplashScreenActivity.this, SignInActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 7000);
    }
}
