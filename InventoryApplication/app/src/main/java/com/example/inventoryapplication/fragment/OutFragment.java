package com.example.inventoryapplication.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.inventoryapplication.R;
import com.example.inventoryapplication.interfaces.Callable;


public class OutFragment extends Fragment implements View.OnClickListener {
    private  Callable callable;
    private Context context;
    private ImageView btn_back,btn_save;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_out, container, false);
        initView(view);
        return view;
    }
    public OutFragment(){
    }
    @SuppressLint("ValidFragment")
    public OutFragment(Context context, Callable callable){
        this.context=context;
        this.callable=callable;

    }

    private void initView(View view) {
        btn_back=(ImageView) view.findViewById(R.id.btn_back);
        btn_save=(ImageView) view.findViewById(R.id.btn_save_data);
        btn_back.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                eventClickBack();
                break;


        }
    }

    private void eventClickBack() {
        callable.call(false);
        close();
    }

    private void close() {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }
}