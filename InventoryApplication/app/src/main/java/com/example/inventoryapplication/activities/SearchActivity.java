package com.example.inventoryapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.inventoryapplication.R;
import com.example.inventoryapplication.adapters.ListViewSearchAdapter;
import com.example.inventoryapplication.common.constants.Message;
import com.example.inventoryapplication.common.entities.InforProductEntity;
import com.example.inventoryapplication.database.SQLiteDatabaseHandler;
import com.example.inventoryapplication.fragment.DialogYesNoFragment;
import com.example.inventoryapplication.interfaces.Callable;

import java.util.LinkedList;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lvSearchResult;
    private String typeString = null;
    LinearLayout btn_search;
    ImageView btn_back;
    EditText search_cd1, search_name;
    LinkedList<InforProductEntity> arrDataInfoProduct;
    SQLiteDatabaseHandler db;
    Parcelable stateListSearch = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search1);
        Intent intentget=getIntent();
        Bundle intentbundle=intentget.getExtras();
        if(intentbundle!=null){
            typeString=(String)intentbundle.get("type");
        }
        initView();
    }

    private void initView(){
        // Set variable for list view
        lvSearchResult = (ListView) findViewById(R.id.list_result_search);
        btn_search  = (LinearLayout) findViewById(R.id.btn_search);
        btn_back  = (ImageView) findViewById(R.id.btn_back);
        search_cd1  = (EditText) findViewById(R.id.search_cd1);
        search_name  = (EditText) findViewById(R.id.search_name);
        arrDataInfoProduct= new LinkedList<>();
        db= new SQLiteDatabaseHandler(this);

        btn_search.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        //intent getstring


        //Event long press item
        onDeleteSelectedProduct();
    }
    /**
     * Call when long click product
     */
    private void onDeleteSelectedProduct() {

        lvSearchResult.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                    /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchActivity.this);
                    alertDialog.setMessage(String.format(Message.MESSAGE_CONFIRM_DELETE_RECORD, arrDataInfoProduct.size() - position));

                    alertDialog.setCancelable(false);

                    // Configure alert dialog button
                    alertDialog.setPositiveButton(Message.MESSAGE_SELECT_YES, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Delete item selected
                            db.deleteProduct(arrDataInfoProduct.get(position));
                            arrDataInfoProduct.remove(position);
                            restartListView();
                        }
                    });
                    alertDialog.setNegativeButton(Message.MESSAGE_SELECT_NO, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();

                        }
                    });

                    AlertDialog alert = alertDialog.show();
                    TextView messageText = (TextView) alert.findViewById(android.R.id.message);
                    assert messageText != null;
                    messageText.setGravity(Gravity.CENTER);*/


                    // Close camera
                showDialogDeleteDecord(position);

                return true;
            }

        });

    }
    /**
     * Loadfragment
     **/
    private void showDialogDeleteDecord(final int position){
        DialogYesNoFragment dialogYesNoFragment=new DialogYesNoFragment(SearchActivity.this, "DELETE DECORD", Message.MESSAGE_CONFIRM_DELETE_RECORD, new Callable() {
            @Override
            public void call(boolean result) {
                if(result==true){
                    db.deleteProductbyTypeTable(arrDataInfoProduct.get(position),typeString);
                    arrDataInfoProduct.remove(position);
                    restartListView();

                }else{

                }
            }
        });
        loadFragment(dialogYesNoFragment);
    }
    private void loadFragment(Fragment fragment){
        try {
            System.out.println("loadFragment");
            FragmentManager fm=getFragmentManager();
            FragmentTransaction fragmentTransaction=fm.beginTransaction();
            fragmentTransaction.replace(R.id.linear_fragment,fragment);
            fragmentTransaction.commit();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /**
     * Refresh list view
     */
    private void restartListView() {

        // Update list view
        ListViewSearchAdapter adapterBook = new ListViewSearchAdapter(this, arrDataInfoProduct, 0);
        lvSearchResult.setAdapter(adapterBook);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                String bar = search_cd1.getText().toString();
                String name = search_name.getText().toString();
                if(!bar.isEmpty()){
                    for(InforProductEntity i : db.getProductByBarcodeAndType(bar,typeString)){
                        arrDataInfoProduct.add(i);
                    }
                    restartListView();
                }
                else if(!name.isEmpty()){
                    for(InforProductEntity i : db.getProductByNameAndType(name,typeString)){
                        arrDataInfoProduct.add(i);
                    }
                    restartListView();
                }
                else {
                    for(InforProductEntity i : db.getAllProductsbyType(typeString)){
                        arrDataInfoProduct.add(i);
                    }
                    restartListView();
                }
                break;
            case R.id.btn_back:
                finish();
                break;

        }
    }

}