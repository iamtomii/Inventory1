package com.example.inventoryapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventoryapplication.R;
import com.example.inventoryapplication.common.constants.Message;
import com.example.inventoryapplication.database.SQLiteDatabaseHandler;
import com.example.inventoryapplication.fragment.DialogYesNoFragment;
import com.example.inventoryapplication.fragment.InFragment;
import com.example.inventoryapplication.fragment.OutFragment;
import com.example.inventoryapplication.interfaces.Callable;

public class MenuBussinessActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton btn_inventory,btn_in,btn_out,btn_setting;
    ImageView btn_back_menu;
    SQLiteDatabaseHandler db;
    TextView inventory_number,incoming_number,outgoing_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productmanager);
        initViews();
    }

    private void initViews(){

        db = new SQLiteDatabaseHandler(this);
        btn_back_menu = (ImageView) findViewById(R.id.btn_back_menu);
        btn_inventory = (ImageButton) findViewById(R.id.btn_inventory);
        inventory_number = (TextView) findViewById(R.id.inventory_number);
        incoming_number = (TextView) findViewById(R.id.incoming_number);
        outgoing_number = (TextView) findViewById(R.id.outgoing_number);
        btn_in=(ImageButton) findViewById(R.id.btn_in);
        btn_out=(ImageButton) findViewById(R.id.btn_out);
        btn_back_menu.setOnClickListener(this);
        btn_inventory.setOnClickListener(this);
        btn_in.setOnClickListener(this);
        btn_out.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_inventory:
                startActivity(new Intent(MenuBussinessActivity.this,ScanDataActivity.class));
                break;
            case R.id.btn_in:
                clickIn();
                break;
            case R.id.btn_out:
                clickOut();
                break;
            case R.id.btn_back_menu:
                startActivity(new Intent(MenuBussinessActivity.this, MenuAppActivity.class));
                break;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        if(db.getProductsbyTypeCount("inventory")==0)
            inventory_number.setText("(0)");
        else    inventory_number.setText("("+db.getProductsbyTypeCount("inventory")+")");
        if(db.getProductsbyTypeCount("incoming")==0)
            incoming_number.setText("(0)");
        else    incoming_number.setText("("+db.getProductsbyTypeCount("incoming")+")");
        if(db.getProductsbyTypeCount("outgoing")==0)
            outgoing_number.setText("(0)");
        else    outgoing_number.setText("("+db.getProductsbyTypeCount("outgoing")+")");

    }
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bussiness);
    }*/
    private void clickIn() {
        //showDialogMessageConfirmExport();
        InFragment inFragment=new InFragment(this,new Callable() {
            @Override
            public void call(boolean result) {
                if(result==true){

                }else{

                }
            }
        });
        loadFragment(inFragment);
    }
    private void clickOut() {
        OutFragment outFragment=new OutFragment(this, new Callable() {
            @Override
            public void call(boolean result) {
                if (result == true) {
                } else {
                }
            }
            });
        loadFragment(outFragment);
    }
    private void loadFragment(Fragment fragment) {
// create a FragmentManager
        try {
            System.out.println("loadFragment: ");
            FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
            android.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
            fragmentTransaction.replace(R.id.product_manager_layout, fragment);
            fragmentTransaction.commit(); // save the changes
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void countNumberProductWithType(){

    }

}