package com.example.inventoryapplication.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.inventoryapplication.R;
import com.example.inventoryapplication.activities.MenuBussinessActivity;
import com.example.inventoryapplication.activities.ScanDataInActivity;
import com.example.inventoryapplication.database.SQLiteDatabaseHandler;
import com.example.inventoryapplication.interfaces.Callable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class InFragment extends android.app.Fragment implements View.OnClickListener {
    private Context context;
    private Callable callable;
    private ImageView btn_save,btn_back,btn_scan_good;
    private Date currentTime;
    private EditText edt_serial, edt_date,edt_ivt_name,edt_supplier,edt_cmt;
    SQLiteDatabaseHandler db;


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
        db = new SQLiteDatabaseHandler(getActivity().getApplication());
        btn_save=(ImageView) view.findViewById(R.id.btn_save_data);
        btn_back=(ImageView) view.findViewById(R.id.btn_back);
        edt_serial=(EditText) view.findViewById(R.id.edt_serial);
        edt_date=(EditText) view.findViewById(R.id.edt_date);
        edt_ivt_name=(EditText) view.findViewById(R.id.edt_ivt_name);
        edt_supplier=(EditText) view.findViewById(R.id.edt_supplier);
        edt_cmt=(EditText) view.findViewById(R.id.edt_cmt);
        btn_scan_good=(ImageView) view.findViewById(R.id.btn_scan_good);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        String  currentTime= df.format(Calendar.getInstance().getTime());
        String serialdate="";
        for (String w : currentTime.split("/")) {
            serialdate+=w;
        }
        Integer serialNumber;
        if(db.getProductsbyTypeCount("incoming")==0)
            serialNumber=1;
        else    serialNumber= Integer.valueOf(db.getProductsbyTypeCount("incoming")+"")+1;
        edt_serial.setText("0"+serialdate+serialNumber);
        edt_date.setText(currentTime);
        btn_scan_good.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                eventClickBack();
                break;
            case R.id.btn_scan_good:
                String serial=edt_serial.getText().toString();
                String ivt_name=edt_ivt_name.getText().toString();
                String date=edt_date.getText().toString();
                Intent intent = new Intent(getActivity().getApplication(),ScanDataInActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("typeproduct","incoming");
                bundle.putString("serial",serial);
                bundle.putString("ivt_name",ivt_name);
                bundle.putString("date",date);
                intent.putExtras(bundle);
                startActivity(intent);
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