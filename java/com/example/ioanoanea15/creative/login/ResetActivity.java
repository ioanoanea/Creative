package com.example.ioanoanea15.creative.login;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ioanoanea15.creative.R;

public class ResetActivity extends AppCompatActivity {

    private TextView error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);


        error = findViewById(R.id.error);

        getSupportFragmentManager().beginTransaction().replace(R.id.reset, new sendFragment()).addToBackStack(null).commit();

    }

    public void displayError(String err){
         error.setVisibility(View.VISIBLE);
         if(err.equals("Done"))
             error.setBackgroundColor(getResources().getColor(R.color.err_color3));
         error.setText(err);

         if(err.equals("INVISIBLE"))
             error.setVisibility(View.INVISIBLE);
    }
}
