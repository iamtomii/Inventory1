package com.example.inventoryapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventoryapplication.R;
import com.example.inventoryapplication.database.SQLiteDatabaseHandler;

public class MenuBussinessActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_back_menu,btn_inventory,btn_incomming;
    SQLiteDatabaseHandler db;
    TextView inventory_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bussiness);
        initViews();
    }

    private void initViews(){

        db = new SQLiteDatabaseHandler(this);
        btn_back_menu = (Button) findViewById(R.id.btn_back_menu);
        btn_inventory = (Button) findViewById(R.id.btn_inventory);
        btn_incomming = (Button) findViewById(R.id.btn_incomming);
        inventory_number = (TextView) findViewById(R.id.inventory_number);

        btn_back_menu.setOnClickListener(this);
        btn_inventory.setOnClickListener(this);
        btn_incomming.setOnClickListener(this);

        if(db.getProductsCount()==0)
            inventory_number.setText("");
        else    inventory_number.setText(db.getProductsCount()+"");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_inventory:
                startActivity(new Intent(MenuBussinessActivity.this,ScanDataActivity.class));
                break;
            case R.id.btn_back_menu:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(db.getProductsCount()==0)
            inventory_number.setText("");
        else    inventory_number.setText(db.getProductsCount()+"");
    }
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bussiness);
    }*/
}