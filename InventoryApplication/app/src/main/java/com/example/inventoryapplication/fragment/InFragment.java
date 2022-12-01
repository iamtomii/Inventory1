package com.example.inventoryapplication.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.inventoryapplication.R;
import com.example.inventoryapplication.interfaces.Callable;

public class InFragment extends android.app.Fragment implements View.OnClickListener {
    private Context context;
    private Callable callable;
    private ImageView btn_save,btn_back;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_in, container, false);

        initView(view);
        return view;
    }
    public InFragment(){
    }
    @SuppressLint("ValidFragment")
    public InFragment(Context context, Callable callable){
        this.context=context;
        this.callable=callable;

    }

    private void initView(View view) {
        btn_save=(ImageView) view.findViewById(R.id.btn_save_data);
        btn_back=(ImageView) view.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                eventClickBack();
                break;
        }
    }
    private void close(){
        getActivity().getFragmentManager().beginTransaction().remove(InFragment.this).commit();
    }
    private void eventClickBack() {
        callable.call(false);
        close();
    }
}