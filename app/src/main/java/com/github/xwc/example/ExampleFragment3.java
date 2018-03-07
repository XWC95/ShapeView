package com.github.xwc.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.xwc.view.ShapeView;


public class ExampleFragment3 extends Fragment {

    private String mImageUrl = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2399593750,1890890896&fm=27&gp=0.jpg";

    private ShapeView shapeView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_example3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shapeView = view.findViewById(R.id.shapeView);

        ImageLoader.loadImage(getContext(), mImageUrl, shapeView, dp2px(150), dp2px(150));

    }

    public  int dp2px(float dipValue) {
        final float scale =  getActivity().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
