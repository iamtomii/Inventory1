package com.example.inventoryapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventoryapplication.R;
import com.example.inventoryapplication.adapters.ListViewSearchAdapter;
import com.example.inventoryapplication.common.constants.Constants;
import com.example.inventoryapplication.common.constants.Message;
import com.example.inventoryapplication.common.entities.InforProductEntity;
import com.example.inventoryapplication.database.SQLiteDatabaseHandler;

import java.util.LinkedList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lvSearchResult;
    LinearLayout btn_search,btn_back;
    EditText search_cd1, search_name;
    LinkedList<InforProductEntity> arrDataInfoProduct;
    SQLiteDatabaseHandler db;
    Parcelable stateListSearch = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
    }

    private void initView(){
        // Set variable for list view
        lvSearchResult = (ListView) findViewById(R.id.list_result_search);
        btn_search  = (LinearLayout) findViewById(R.id.btn_search);
        btn_back  = (LinearLayout) findViewById(R.id.btn_back);
        search_cd1  = (EditText) findViewById(R.id.search_cd1);
        search_name  = (EditText) findViewById(R.id.search_name);
        arrDataInfoProduct= new LinkedList<>();
        db= new SQLiteDatabaseHandler(this);

        btn_search.setOnClickListener(this);
        btn_back.setOnClickListener(this);

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
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchActivity.this);
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
                    messageText.setGravity(Gravity.CENTER);

                    // Close camera


                return true;
            }

        });

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
                    for(InforProductEntity i : db.getProductByBarcode(bar)){
                        arrDataInfoProduct.add(i);
                    }
                    restartListView();
                }
                else if(!name.isEmpty()){
                    for(InforProductEntity i : db.getProductByName(name)){
                        arrDataInfoProduct.add(i);
                    }
                    restartListView();
                }
                else {
                    for(InforProductEntity i : db.getAllProducts()){
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