package com.github.xwc.example;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.xwc.view.ButtonShapeView;
import com.github.xwc.view.Shape;
import com.github.xwc.view.ShapeView;


public class ExampleFragment3 extends Fragment {

    private String mImageUrl = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2399593750,1890890896&fm=27&gp=0.jpg";

    private ShapeView shapeView;

    private ButtonShapeView buttonShape;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_example3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        shapeView = view.findViewById(R.id.shapeView);
        buttonShape = view.findViewById(R.id.buttonShape);
//
//        addTextView(btnShapeView);
//
//        ImageLoader.loadImage(getContext(), mImageUrl, shapeView, dp2px(150), dp2px(150));
//        btnShapeView.setClickListener(new com.github.xwc.view.Shape.ClickListener() {
//            @Override
//            public void onClick(View var1) {
//                Toast.makeText(getActivity(),"点击",Toast.LENGTH_SHORT).show();
//            }
//        });

        buttonShape.setOnShapeClickListener(v -> {
            Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
        });

    }

    private void addTextView(ShapeView view) {
        TextView textView = new TextView(getActivity());
        textView.setText("addTextView");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.parseColor("#87CEEB"));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        view.addView(textView,FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
    }

    public  int dp2px(float dipValue) {
        final float scale =  getActivity().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
