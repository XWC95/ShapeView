package com.github.xwc.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.github.xwc.view.ShapeView;


public class MainActivity extends AppCompatActivity {

    ShapeView shapeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shapeView = findViewById(R.id.heartShapeView);
        shapeView.setClickListener(new com.github.xwc.view.View.ClickListener() {
            @Override
            public void onClick(View var1) {
                Toast.makeText(MainActivity.this,"点击了",Toast.LENGTH_SHORT).show();
            }
        });
//
//
//
//        findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("xwc","点击了");
//            }
//        });

//        shapeView.setHeartRadian(0.5f);
//        shapeView.invalidate();
    }
}
