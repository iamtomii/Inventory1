package com.example.inventoryapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Insets;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventoryapplication.R;
import com.example.inventoryapplication.adapters.ListViewScanAdapter;
import com.example.inventoryapplication.common.constants.Constants;
import com.example.inventoryapplication.common.constants.Message;
import com.example.inventoryapplication.common.entities.InforProductEntity;
import com.example.inventoryapplication.common.function.CsvExport;
import com.example.inventoryapplication.common.function.SupModRfidCommon;
import com.example.inventoryapplication.database.SQLiteDatabaseHandler;
import com.example.inventoryapplication.fragment.DialogYesNoFragment;
import com.example.inventoryapplication.interfaces.Callable;
import com.example.inventoryapplication.thread.ConnectThread;
import com.example.inventoryapplication.thread.HttpPostRfid;
import com.example.inventoryapplication.thread.HttpRfidResponse;
import com.google.android.material.navigation.NavigationView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ScanDataActivity extends AppCompatActivity implements View.OnClickListener, HttpRfidResponse {
    // #ADD
    // #ADD_ERROR
    Button btn_error;
    private int MODE_SCAN = 0;
    private int PAUSE_DEVICE = 0;
    private int IS_SHOW_DIALOG_LIMIT = 0;
    private TextView total_quantity, total_money,total_error;
    private EditText edt_receive_barcode_wireless; // #ADD_BARCODE
    private ImageView btnBack, btnSearch, btnDelete_all, btnDelete, btnSave, btnMode;
    // #ADD_BARCODE
    private int flagCustom = 0;
    private int scan_size;
    // #TranVuHoangSon Set custom
    // SDK_ADD
    Set<String> setCustomInput = new HashSet<>();
    Set<String> setCustomOutput = new HashSet<>();

    private LinkedList<InforProductEntity> arrDataInList;
    private LinkedList<InforProductEntity> arrDataInListSum;
    private InforProductEntity inforProductEntity;
    private boolean isKeepScanMagazine = false;
    private ListView lvProduct;
    private Toolbar nav_icon;
    private DrawerLayout drawer_layout;
    private NavigationView nav_views;
    // #HUYNHQUANGVINH list rfid not found
    Set<String> setRfidNotFound = new HashSet<>();
    SQLiteDatabaseHandler db;
    List<HttpPostRfid> listHttp = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        db = new SQLiteDatabaseHandler(this);
        initViews();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    showProgress();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                initDeviceScanVN();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private Callable callable = new Callable() {
        @Override
        public void call(boolean result) {
            if(result==true){
                dismissProgress();
                showToast("Connected Scanner!!!");
            }
        }
    };
    ConnectThread connectThread = null;
    private void initDeviceScanVN(){
        //bluetoothDeviceConnect();
        connectThread = new ConnectThread();
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectThread.connect(bluetoothDeviceConnected2(), ScanDataActivity.this, new Callable() {
                    @Override
                    public void call(boolean result) {
                        showToast("Starting...");
                        dismissProgress();
                    }
                });

            }
        }).start();

    }

    private BluetoothDevice bluetoothDeviceConnected2(){
        BluetoothDevice deviceTemp = null;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            int i=0;
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                if(i==0)
                    deviceTemp=device;
                if(deviceHardwareAddress.equals(Constants.CONFIG_MAC_HANDWARE)) {
                    System.out.println("okok");
                    return device;
                }
                i++;
            }
        }
        return deviceTemp;
    }
    /**
     * プログレス表示フラグ
     */
    private boolean isShowProgress = false;
    /**
     * ライブラリアクセス中プログレス
     */
    private ProgressBar mProgressBar = null;

    // #SON_RECONNECT
    protected void showProgress() {
        if (mProgressBar == null) {
            mProgressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
            //スクリーンサイズを取得する
            int width;
            int height;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                WindowMetrics display = this.getWindowManager().getCurrentWindowMetrics();
                // 画面サイズ取得
                Insets insets = display.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars() | WindowInsets.Type.displayCutout());
                width = display.getBounds().width() - (insets.right + insets.left);
                height = display.getBounds().height() - (insets.top + insets.bottom);
            } else {
                Display display = this.getWindowManager().getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                width = point.x;
                height = point.y;
            }
            //ルートビューにProgressBarを貼り付ける
            ViewGroup rootView = (ViewGroup) getWindow().getDecorView();
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mProgressBar.setPadding(width * 3 / 8, height * 3 / 8, width * 3 / 8, height * 3 / 8);
            rootView.addView(mProgressBar, params);

        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        isShowProgress = true;
    }
    private static LinkedList<InforProductEntity> groupByCustom(LinkedList<InforProductEntity> listInfoProductEntity){
        Map<String, List<InforProductEntity>> map = new HashMap<String, List<InforProductEntity>>();

        for (InforProductEntity product : listInfoProductEntity) {
            String key  = product.getBarcodeCD1();
            if(map.containsKey(key)){
                List<InforProductEntity> list = map.get(key);
                list.add(product);

            }else{
                List<InforProductEntity> list = new ArrayList<InforProductEntity>();
                list.add(product);
                map.put(key, list);
            }

        }
        System.out.println(map);
        LinkedList<InforProductEntity> listReturn = new LinkedList<>();
        for(Map.Entry<String,List<InforProductEntity>> entry : map.entrySet()){
            int quantity = entry.getValue().size();
            InforProductEntity a = entry.getValue().get(0);
            a.setQuantity(quantity);
            listReturn.add(a);
        }

        return listReturn;
    }
    /**
     * プログレスディスミス用ハンドラー
     */
    private Handler mDissmissProgressHandler = new Handler(Looper.getMainLooper());
    /**
     * プログレスディスミス用ランナブル
     */
    private Runnable mDissmissProgressRunnable = null;
    /**
     * アクセス中のプログレス消去
     */
    protected void dismissProgress() {
        mDissmissProgressRunnable = new Runnable() {
            @Override
            public void run() {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                if (null != mProgressBar) {
                    mProgressBar.setVisibility(ProgressBar.GONE);
                }
            }
        };
        if (null != mDissmissProgressHandler) {
            mDissmissProgressHandler.post(mDissmissProgressRunnable);
            isShowProgress = false;
        }
    }


    /**
     * Refresh list view
     */
    private void restartListView() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Update list view
                ListViewScanAdapter adapterBook = new ListViewScanAdapter(ScanDataActivity.this,
                        arrDataInList);
                lvProduct.setAdapter(adapterBook);
                // Show total number and price
                callTotalNumberAndPrice();

                // Get size of data scan in list view
                scan_size = arrDataInList.size();
            }
        });

    }
    /**
     * Update list view after scan
     * flag = 0 -> barcode Scan
     * flag = 1 -> RFID Scan -> will hide disableScanHoneyWell
     */
    private void updateCurrentView() {

        arrDataInList.add(0, inforProductEntity);
        inforProductEntity = new InforProductEntity();

        // Insert current record to database
        //checkAndInsert1RecordDatabase();

        restartListView();

    }



    private void initViews(){
        total_quantity = (TextView) findViewById(R.id.total_quantity);
        total_money = (TextView) findViewById(R.id.total_money);
        total_error = (TextView) findViewById(R.id.total_error);
        //edt_receive_barcode_wireless = (EditText) findViewById(R.id.edt_receive_barcode_wireless);
        inforProductEntity = new InforProductEntity();
        arrDataInList = new LinkedList<>();
        lvProduct = (ListView) findViewById(R.id.list_scan);

        btnBack = (ImageView) findViewById(R.id.btn_back);
        btnSearch = (ImageView) findViewById(R.id.btn_search);
        //btnDelete = (LinearLayout) findViewById(R.id.btn_delete);
        btnSave = (ImageView) findViewById(R.id.btn_save_data);
        btnDelete_all = (ImageView) findViewById(R.id.btn_delete_all);
        //btnMode = (LinearLayout) findViewById(R.id.btn_mode);
        // #ADD_ERROR
        btn_error = (Button) findViewById(R.id.btn_error);
        //#Navigation
        nav_icon=(Toolbar) findViewById((R.id.nav_icon));
        drawer_layout=(DrawerLayout) findViewById((R.id.drawer_layout));
        nav_views=(NavigationView) findViewById(R.id.nav_views);
        btnBack.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        //btnDelete.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnDelete_all.setOnClickListener(this);
        //btnMode.setOnClickListener(this);
        // #ADD_ERROR
        btn_error.setOnClickListener(this);
        //# navigation
        nav_icon.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.openDrawer(GravityCompat.START);
            }
        });
        View headerView = nav_views.getHeaderView(0);
        ImageView nav_back=(ImageView) headerView.findViewById(R.id.nav_back);
        nav_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.closeDrawer(GravityCompat.START);
                eventClickBack();
                }
        });

        nav_views.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                drawer_layout.closeDrawer(GravityCompat.START);
                switch (id)
                {
                    case R.id.nav_account:
                        Toast.makeText(ScanDataActivity.this,"coming soon!",Toast.LENGTH_SHORT).show();break;
                    case R.id.nav_menu_app:
                        eventClickItemMenuApp();
                    case R.id.nav_manager:
                        drawer_layout.closeDrawer((GravityCompat.START));
                        eventClickBack();
                    case R.id.nav_setting:
                        Toast.makeText(ScanDataActivity.this,"coming soon!",Toast.LENGTH_SHORT).show();break;
                    case R.id.nav_themes:
                        Toast.makeText(ScanDataActivity.this,"coming soon!",Toast.LENGTH_SHORT).show();break;
                    case R.id.nav_logout:
                        Toast.makeText(ScanDataActivity.this,"coming soon!",Toast.LENGTH_SHORT).show();break;
                    default:
                        return true;
                }
                return true;
            }
        });
        reloadSQLiteData();

    }


    private void reloadSQLiteData(){
        setCustomOutput.clear();
        setCustomOutput.clear();
        //ADD SQLITE DATA
        for(InforProductEntity i : db.getAllProductsbyType("inventory")){
            setCustomInput.add(i.getRfidCode());
            setCustomOutput.add(i.getRfidCode());
        }
    }
    /**
     * Update Price + Number Return
     */
    private void callTotalNumberAndPrice() {

        int intQuantity = 0;
        double intMoney = 0;
        for (int i = 0; i < arrDataInList.size(); i++) {
            intQuantity += arrDataInList.get(i).getQuantity();
            intMoney += arrDataInList.get(i).getBasePrice();
        }
        total_quantity.setText(MessageFormat.format("{0} : {1}", String.valueOf(getText(R.string.total_quantity)), intQuantity));
        total_money.setText(MessageFormat.format("{0} : {1}", getText(R.string.total_amount), intMoney+""));

    }

    /* private void eventDisconnectDevice(){
         close(new CallbackListener() {
             @Override
             public void callback(boolean success, String msg) {
                 showToast("DISCONNECTED!!!");
             }
         });
     }*/
    @Override
    public void onDestroy() {
        connectThread.cancel();
        //eventDisconnectDevice();
        //btConnect.cancel();
        super.onDestroy();


    }
//2002000005526
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return arrDataInList;
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_back:
                eventClickBack();
                break;
            case R.id.btn_search:
                eventCLickSearch();
                //initDataCustom();
               break;
            case R.id.btn_delete_all:
                eventClickDeleteAll();
                //initOneDataCustom();
                break;
            case R.id.btn_save_data:
                eventClickExport();
                //eventManualInput();
                break;
            //case R.id.btn_delete:
            //initDataCustom();
            //eventClickDelete();
            //break;
            case R.id.btn_error:
                showDialog();
                break;
           /* case R.id.btn_mode:
                eventClickMode();
                break;*/
        }
    }

    private void showToast(String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ScanDataActivity.this,s+"",Toast.LENGTH_LONG).show();
            }
        });
    }



    /**
     * Setting list_view in register data screen
     */
    private void initListViewScreen() {

        // check array null
        arrDataInList = (LinkedList<InforProductEntity>) getLastCustomNonConfigurationInstance();

        if (arrDataInList == null) {
            arrDataInList = new LinkedList<>();
        }

        // Check if array is not null
        restartListView();

    }
    /**
     * Function click MenuApp in navigation
     */
    private void eventClickItemMenuApp() {
        /*if (arrDataInList.size() > 0) {
            isKeepScanMagazine = checkOldBarcode();
        } else {
            isKeepScanMagazine = false;
        }*/

        if (arrDataInList.size() > 0) {
            // Stop scan honeywell

            // Disable event onClick
            eventDisableButton();
            //eventOpenButton(false);

            showDialogConfirmMenuBack();
        } else {

            startActivity(new Intent(ScanDataActivity.this, MenuAppActivity.class));
        }

    }
    /**
     * Function show dialog confirm back to MenuApp
     */
    private void showDialogConfirmMenuBack(){
        DialogYesNoFragment dialogYesNoFragment=new DialogYesNoFragment(this, "CONFIRM GO TO MENU", Message.MESSAGE_CONFIRM_REGISTER_DATA, new Callable() {
            @Override
            public void call(boolean result) {
                if(result==true){
                    db.insertAllProducts(arrDataInList);
                    //eventDisconnectDevice();
                    startActivity(new Intent(ScanDataActivity.this, MenuAppActivity.class));
                }else{
                    startActivity(new Intent(ScanDataActivity.this, MenuAppActivity.class));
                }
            }
        });
        loadFragment(dialogYesNoFragment);
    }

    /**
     * Function click back
     */

    private void eventClickBack() {
        /*if (arrDataInList.size() > 0) {
            isKeepScanMagazine = checkOldBarcode();
        } else {
            isKeepScanMagazine = false;
        }*/
        if (arrDataInList.size() > 0) {
            // Stop scan honeywell

            // Disable event onClick
            eventDisableButton();
            //eventOpenButton(false);
            showDialogConfirmBack();
        } else {
            onBackPressed();
        }

    }
    /**
     * Function show dialog confirm back
     */
    private void showDialogConfirmBack(){
        DialogYesNoFragment dialogYesNoFragment=new DialogYesNoFragment(this, "CONFIRM BACK", Message.MESSAGE_CONFIRM_REGISTER_DATA, new Callable() {
            @Override
            public void call(boolean result) {
                if(result==true){
                    db.insertAllProducts(arrDataInList);
                    //eventDisconnectDevice();
                    onBackPressed();
                }else{
                    onBackPressed();
                }
            }
        });
        loadFragment(dialogYesNoFragment);
    }


    /**sea
     * Function show dialog confirm back
     */
    private void showDialogMessageConfirmSaveToContinue() {
        if(IS_SHOW_DIALOG_LIMIT==0) {
            //STOP DEVICE SCAN
            IS_SHOW_DIALOG_LIMIT=1;
            PAUSE_DEVICE = 1;
            //Show message confirm
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScanDataActivity.this);
            alertDialog.setMessage(String.format(Message.MESSAGE_CONFIRM_OVER_DATA, Constants.LIMIT_ONCE));

            alertDialog.setCancelable(false);

            // Configure alert dialog button
            alertDialog.setPositiveButton(Message.YES_REGISTER_DATA, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Save list view to database
                    showProgressRunUi();
                    db.insertAllProductsCallBack(arrDataInList, new Callable() {
                        @Override
                        public void call(boolean result) {
                            if(result==true){
                                showToast(arrDataInList.size()+"");
                                //arrDataInList.clear();
                                restartListView();
                                eventEnableButton();
                                IS_SHOW_DIALOG_LIMIT=0;
                                dismissProgress();
                            }
                        }
                    });

                }
            });
            alertDialog.setNegativeButton(Message.NOT_REGISTER_DATA, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    eventEnableButton();
                    IS_SHOW_DIALOG_LIMIT=0;
                }
            });

            AlertDialog alert = alertDialog.show();
            eventDisableButton();
            //eventOpenButton(false);
            TextView messageText = (TextView) alert.findViewById(android.R.id.message);
            assert messageText != null;
            messageText.setGravity(Gravity.CENTER);
        }
    }


    /**
     * Function show dialog confirm search
     */
    private void showDialogMessageConfirmSearch() {

        //Show message confirm
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScanDataActivity.this);
        alertDialog.setMessage(Message.MESSAGE_CONFIRM_REGISTER_DATA);

        alertDialog.setCancelable(false);

        // Configure alert dialog button
        alertDialog.setPositiveButton(Message.YES_REGISTER_DATA, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Save list view to database

                db.insertAllProducts(arrDataInList);
                eventEnableButton();
                arrDataInList.clear();
                reloadSQLiteData();
                restartListView();
                startActivity(new Intent(ScanDataActivity.this,SearchActivity.class));

            }
        });
        alertDialog.setNegativeButton(Message.NOT_REGISTER_DATA, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //onBackPressed();
                eventEnableButton();
            }
        });

        AlertDialog alert = alertDialog.show();
        eventDisableButton();
        //eventOpenButton(false);
        TextView messageText = (TextView) alert.findViewById(android.R.id.message);
        assert messageText != null;
        messageText.setGravity(Gravity.CENTER);

    }
    /**
     * Function show dialog confirm back
     */
    //private void showDialogMessageConfirmExport() {

    //Show message confirm
    //  AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScanDataActivity.this);
    //alertDialog.setTitle("EXPORT");
    //alertDialog.setMessage(Message.MESSAGE_CONFIRM_EXPORT_DATA);
    //alertDialog.setCancelable(false);

    // Configure alert dialog button
    //alertDialog.setPositiveButton(Message.YES_REGISTER_DATA, new DialogInterface.OnClickListener() {
    //@Override
    //public void onClick(DialogInterface dialog, int which) {
    // Save list view to database

    //db.insertAllProducts(arrDataInList);
    //actionDeleteAll();
    //if(!db.getAllProducts().isEmpty()){
    //showProgressRunUi();
    //ExcelExporter.exportObj(setRfidNotFound, ScanDataActivity.this, new Callable() {
    //@Override
    //public void call(boolean result) {
    //if(result){
    //showToast("Exporting Success!!!");
    //dismissProgress();
    //showDialogMessageExport();
    //}
    //}
    //});

    //}
    //else showToast("No data to export!!!");

    //}
    //});
    //alertDialog.setNegativeButton(Message.NOT_REGISTER_DATA, new DialogInterface.OnClickListener() {
    //@Override
    // public void onClick(DialogInterface dialog, int i) {
    //onBackPressed();
    //   eventEnableButton();
    // }
    //   });

    //  AlertDialog alert = alertDialog.show();
    //  eventDisableButton();
    //  //eventOpenButton(false);
    // TextView messageText = (TextView) alert.findViewById(android.R.id.message);
    // assert messageText != null;
    // messageText.setGravity(Gravity.CENTER);

    // }
    private void showProgressRunUi(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgress();
            }
        });
    }

    /**
     * Function click button search
     */
    private void eventCLickSearch() {
        if(!arrDataInList.isEmpty())
            showDialogMessageConfirmSearch();
        else{
            Intent intent=new Intent(ScanDataActivity.this,SearchActivity.class);
            intent.putExtra("type",Constants.TYPE_TABLE_INVENTORY);
            startActivity(intent);
        }
    }
    /**
     * Function click Export
     */
    private void eventClickExport() {
        //showDialogMessageConfirmExport();
        DialogYesNoFragment dialogYesNoFragment=new DialogYesNoFragment(this, "EXPORT", Message.MESSAGE_CONFIRM_EXPORT_DATA, new Callable() {
            @Override
            public void call(boolean result) {
                if(result==true){
                    eventExportYes();
                }else{
                    dismissProgress();
                }
            }
        });
        loadFragment(dialogYesNoFragment);
    }
    /**
     * Function eventExport
     */
    private void eventExportYes(){
        db.insertAllProducts(arrDataInList);
        if(!db.getAllProductsbyType("inventory").isEmpty()){
            showProgressRunUi();
            String[] header = new String[] {"rfid", "product_name", "quantity", "barcode"};
            CsvExport.writeData(this, header,Constants.TYPE_TABLE_INVENTORY,new Callable(){
                @Override
                public void call(boolean result) {
                    showToast("Export success!!!");
                    dismissProgress();
                    showDialogMessageExportYes();
                }
            });
            /*ExcelExporter.exportObj(setRfidNotFound, ScanDataActivity.this, new Callable() {
                @Override
                public void call(boolean result) {
                    if(result){
                        showToast("Exporting Success!!!");
                        dismissProgress();
                        showDialogMessageExportYes();
                    }
                }
            });*/

        }
        else showToast("No data to export!!!");
    }
    /**
     * Function showDialogMessageExport
     */
    private void showDialogMessageExportYes(){
        DialogYesNoFragment dialogYesNoFragment=new DialogYesNoFragment(this, "EXPORT", Message.MESSAGE_CONFIRM_REMOVE_ADD_DATA, new Callable() {
            @Override
            public void call(boolean result) {
                if(result==true){
                    db.deleteAllProductsbyTypeTable(Constants.TYPE_TABLE_INVENTORY);
                    onBackPressed();
                }else{
                    dismissProgress();
                }
            }
        });
        loadFragment(dialogYesNoFragment);
    }
    //private void showDialogMessageExport() {

    //Show message confirm
    //AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScanDataActivity.this);
    //alertDialog.setMessage(Message.MESSAGE_CONFIRM_REMOVE_ADD_DATA);

    //alertDialog.setCancelable(false);

    // Configure alert dialog button
    // alertDialog.setPositiveButton(Message.YES_REGISTER_DATA, new DialogInterface.OnClickListener() {
    //   @Override
    //  public void onClick(DialogInterface dialog, int which) {
    // Save list view to database
    //    db.deleteAllProducts();
    //   onBackPressed();
    // }
    // });
    // alertDialog.setNegativeButton(Message.NOT_REGISTER_DATA, new DialogInterface.OnClickListener() {
    //  @Override
    //  public void onClick(DialogInterface dialog, int i) {
    //      onBackPressed();
    //   }
    //  });

    // AlertDialog alert = alertDialog.show();
    //  eventDisableButton();
    //eventOpenButton(false);
    // TextView messageText = (TextView) alert.findViewById(android.R.id.message);
    // assert messageText != null;
    // messageText.setGravity(Gravity.CENTER);

    // }


    /**
     * loadfragment
     */
    private void loadFragment(Fragment fragment) {
// create a FragmentManager
        try {
            System.out.println("loadFragment: ");
            FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
            fragmentTransaction.replace(R.id.linear_fragment, fragment);
            fragmentTransaction.commit(); // save the changes
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Function click delete all
     */
    private void eventClickDeleteAll() {
        //showDialogMessageConfirmExport();
        if (arrDataInList.size() > 0) {
            DialogYesNoFragment dialogYesNoFragment=new DialogYesNoFragment(this, "DELETE ALL", Message.MESSAGE_CONFIRM_DELETE_ALL, new Callable() {
                @Override
                public void call(boolean result) {
                    if(result==true){
                        actionDeleteAll(Constants.TYPE_TABLE_INVENTORY);
                        showToast("Delete Success!!!");
                    }else{
                        dismissProgress();
                    }
                }
            });
            loadFragment(dialogYesNoFragment);
        } else {
            showToast("No any record to delete!!!");
        }
    }
/*    private void eventDeleteAll() {

        if (arrDataInList.size() > 0) {
            //Show message confirm
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ScanDataActivity.this);
            alertDialog.setMessage(Message.MESSAGE_CONFIRM_DELETE_ALL);

            alertDialog.setCancelable(false);

            // Configure alert dialog button
            alertDialog.setPositiveButton(Message.MESSAGE_SELECT_YES, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    actionDeleteAll();
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
        } else {
            showToast("No any record to delete!!!");
        }


    }*/

    /**
     * Action delete all record
     */
    private void actionDeleteAll(String type) {
        if (!arrDataInList.isEmpty()) {
            setCustomInput.clear();
            setCustomOutput.clear();
            reloadSQLiteData();
            inforProductEntity = new InforProductEntity();
            db.deleteAllProductsbyTypeTable(type);
            initListViewScreen();
        }
    }
    /**
     * Action change mode
     */
    private void actionChangeMode() {
        if(MODE_SCAN==0){

            MODE_SCAN=1;
        }
        else if(MODE_SCAN==1){


            MODE_SCAN=0;
        }
    }

    /**
     * Function click delete
     */
    private void eventClickDelete() {


    }

    /**
     * Function click delete
     */
    private void eventClickMode() {


    }

    private void setDataEntity(JSONObject obj) {
        String bar1= null;
        String bar2= null;
        String rfid= null;
        String name = null;
        int cost = 0;
        int tax = 0;
        try {
            bar1 = obj.getString(Constants.KEY_JANCODE_1);
/*            if(!obj.getString(Constants.KEY_TAX).equals("null"))
                bar2 = obj.getString(Constants.KEY_JANCODE_2);*/
            name = obj.getString(Constants.KEY_GOOD_NAME);
            if(!obj.getString(Constants.KEY_TAX).equals("null"))
                tax = obj.getInt(Constants.KEY_TAX);
            else tax=0;
            if(!obj.getString(Constants.KEY_COST).equals("null"))
                cost = obj.getInt(Constants.KEY_COST);
            else cost=0;
            rfid = obj.getString(Constants.KEY_RFID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        inforProductEntity.setBarcodeCD1(bar1);
        inforProductEntity.setBasePrice(cost);
        inforProductEntity.setTypeProduct(Constants.TYPE_TABLE_INVENTORY);
        inforProductEntity.setQuantity(1);
        inforProductEntity.setTaxIncludePrice(tax);
        inforProductEntity.setGoodName(name);
        inforProductEntity.setRfidCode(rfid);
        processBarcode(bar1,cost);
/*        inforProductEntity.setBarcodeCD2(bar2);
        processBarcode(bar2);*/

    }
    private void processBarcode(String strBarcode,int cost){
        //CHECK OVER LIMIT ONCE
        if(arrDataInList.size()>=Constants.LIMIT_ONCE){
            showDialogMessageConfirmSaveToContinue();
            return;
        }
        if(strBarcode.isEmpty()){
            return;
        }
        //int first3character = Integer.parseInt(strBarcode.substring(0,3));
        int price = cost;

        digestBarcode(strBarcode,price);
    }
    /**
     * Digest type of barcode (magazine, japan magazine, others)
     */
    private void digestBarcode(String bar_code,int money) {
        addOtherBarcode(bar_code);


    }
    /* private void digestBarcode(String bar_code, int first3character, int money) {

     switch (first3character) {
            case Constants.CD1_978:
                inforProductEntity.setBarcodeCD1(bar_code);
                inforProductEntity.setBarcodeCD2(Constants.BLANK);
                break;
            case Constants.CD2_191:
            case Constants.CD2_192:
                inforProductEntity.setBarcodeCD1(Constants.BLANK);
                inforProductEntity.setBarcodeCD2(bar_code);
                inforProductEntity.setBasePrice(money);
                break;
        }
        updateCurrentView();*/
    /*

        // If first scan
        if (arrDataInList.size() == 0) {
            // SA-204#comment-1319296895 - ADD START
            if (bar_code.length() == 12 && bar_code.substring(0, 2).equals(Constants.CD2_3)) {
                addOtherBarcode(bar_code);
            } else {
                // SA-204#comment-1319296895 - ADD END
                switch (first3character) {
                    case Constants.CD1_978:
                    case Constants.CD2_191:
                    case Constants.CD2_192:
                        addBarcodeMagazine(bar_code, first3character, money);
                        break;
                    case Constants.CD1_491:
                        addBarcodeJapanMagazine(bar_code, money);
                        break;
                    default:
                        addOtherBarcode(bar_code);
                        break;
                }
            }
        } else {
            // Check last product is finish or not
            isKeepScanMagazine = checkOldBarcode();
            if (isKeepScanMagazine) {
                // Last product not finish

                String strBarcode1 = arrDataInList.get(0).getBarcodeCD1();
                String strBarcode2 = arrDataInList.get(0).getBarcodeCD2();
                int intBarcode1_3Character = 0;
                int intBarcode2_3Character = 0;

                if (!Constants.BLANK.equals(strBarcode1) && strBarcode1 != null) {
                    intBarcode1_3Character = Integer.parseInt(strBarcode1.substring(0, 3));
                }

                if (!Constants.BLANK.equals(strBarcode2) && strBarcode2 != null) {
                    intBarcode2_3Character = Integer.parseInt(strBarcode2.substring(0, 3));
                }

                switch (first3character) {
                    case Constants.CD1_978:
                        if (intBarcode1_3Character == 0) {
                            appendBarcodeMagazine(bar_code, first3character, money);
                        } else {
                            toastBarcodeInvalidate();
                            //showDialogMessageInvalidBarcode();
                        }
                        break;
                    case Constants.CD2_191:
                    case Constants.CD2_192:
                        if (intBarcode2_3Character == 0) {
                            appendBarcodeMagazine(bar_code, first3character, money);
                        } else {
                            toastBarcodeInvalidate();
                            //showDialogMessageInvalidBarcode();
                        }
                        break;
                    case Constants.CD1_491:
                    default:
                        toastBarcodeInvalidate();
                        //showDialogMessageInvalidBarcode();
                        break;
                }
            } else {
                // Last product finish
                // SA-204#comment-1319296895 - ADD START
                if (bar_code.length() == 12 && bar_code.substring(0, 2).equals(Constants.CD2_3)) {
                    addOtherBarcode(bar_code);
                } else {
                    // SA-204#comment-1319296895 - ADD END
                    switch (first3character) {
                        case Constants.CD1_978:
                        case Constants.CD2_191:
                        case Constants.CD2_192:
                            addBarcodeMagazine(bar_code, first3character, money);
                            break;
                        case Constants.CD1_491:
                            addBarcodeJapanMagazine(bar_code, money);
                            break;
                        default:
                            addOtherBarcode(bar_code);
                            break;
                    }
                }
            }
        }
    }*/
    /**
     * Show Toast barcode in validate
     */
    private void toastBarcodeInvalidate() {

        //noReturnSound.start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ScanDataActivity.this, Constants.INVALID_BARCODE, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * Add barcode japan magazine
     */
    private void addBarcodeJapanMagazine(String bar_code, int money) {

        if (money == 0) {
            inforProductEntity.setBarcodeCD1(bar_code);
            inforProductEntity.setBarcodeCD2(Constants.BLANK);
            inforProductEntity.setBasePrice(money);
            updateCurrentView();
            showDialogMessageInvalidBarcode();
        } else {
            inforProductEntity.setBarcodeCD1(bar_code);
            inforProductEntity.setBarcodeCD2(Constants.BLANK);
            inforProductEntity.setBasePrice(money);
            updateCurrentView();
        }

    }
    /**
     * Function show dialog message
     */
    private void showDialogMessageInvalidBarcode() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                eventDisableButton();
                //eventOpenButton(false);

                AlertDialog.Builder dialog =
                        new AlertDialog.Builder(ScanDataActivity.this);
                dialog
                        .setMessage(Message.NOTIFICATION_BARCODE_INVALID)
                        .setCancelable(false)
                        .setNegativeButton(Message.MESSAGE_YES,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        eventClickDelete();
                                        eventEnableButton();
                                        //eventOpenButton(true);
                                    }
                                });

                AlertDialog alert = dialog.show();
                TextView messageText = (TextView) alert.findViewById(android.R.id.message);
                assert messageText != null;
                messageText.setGravity(Gravity.CENTER);
            }
        });

    }
    /**
     * Disable onclick action
     */
    private void eventDisableButton() {

        btnSearch.setClickable(false);
        btnBack.setClickable(false);
        //btnDelete.setClickable(false);
        btnDelete_all.setClickable(false);
        btnSave.setClickable(false);
        // #ADD_ERROR
        btn_error.setClickable(true);

    }
    /**
     * Enable onclick action
     */
    private void eventEnableButton() {

        btnSearch.setClickable(true);
        btnBack.setClickable(true);
        //btnDelete.setClickable(true);
        btnDelete_all.setClickable(true);
        btnSave.setClickable(true);
        // #ADD_ERROR
        btn_error.setClickable(true);

    }

    /**
     * Add barcode magazine
     */
    private void addBarcodeMagazine(String bar_code, int first3character, int money) {

        switch (first3character) {
            case Constants.CD1_978:
                inforProductEntity.setBarcodeCD1(bar_code);
                inforProductEntity.setBarcodeCD2(Constants.BLANK);
                break;
            case Constants.CD2_191:
            case Constants.CD2_192:
                inforProductEntity.setBarcodeCD1(Constants.BLANK);
                inforProductEntity.setBarcodeCD2(bar_code);
                inforProductEntity.setBasePrice(money);
                break;
        }
        updateCurrentView();

    }

    /**
     * Append barcode magazine when old barcode not finish
     */
    private void appendBarcodeMagazine(String bar_code, int first3character, int money) {

        switch (first3character) {
            case Constants.CD1_978:
                arrDataInList.get(0).setBarcodeCD1(bar_code);
                break;
            case Constants.CD2_191:
            case Constants.CD2_192:
                arrDataInList.get(0).setBarcodeCD2(bar_code);
                arrDataInList.get(0).setBasePrice(money);
                break;
        }
        restartListView();

    }
    /**
     * Add barcode others
     */
    ToneGenerator toneG;
    private void addOtherBarcode(String bar_code) {

        // SA-150 修正_UPC-A対応 EDIT START
//        inforProductEntity.setProductCode1(bar_code);
        int bar_code_length = bar_code.length();
        switch (bar_code_length) {
            case 8:
                inforProductEntity.setBarcodeCD1(bar_code + "     ");
                break;
            case 12:
                inforProductEntity.setBarcodeCD1("0" + bar_code);
                break;
            default:
                inforProductEntity.setBarcodeCD1(bar_code);
                break;
        }
        // SA-150 修正_UPC-A対応 EDIT END
        inforProductEntity.setBarcodeCD2(Constants.BLANK);
        // #SON_CLOSED
        //inforProductEntity.setBasePrice(0);
        //SOUND_CLOSED
        updateCurrentView();
        try {
            toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
            //toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_INCALL_LITE, 200);
            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);

        }catch (RuntimeException e ){e.printStackTrace();}
        //CHECK OVER LIMIT ONCE
        if(arrDataInList.size()>=Constants.LIMIT_ONCE){
            showDialogMessageConfirmSaveToContinue();
        }

    }


    /**
     * Check old barcode is finish or not
     */
   /* private boolean checkOldBarcode() {

        // SA-204 短縮JANをスキャンすると「スキャンしたバーコードが不正です。」と表示される。 - ADD START
        if (arrDataInList.size() == 0) {
            return false;
        }
        // SA-204 短縮JANをスキャンすると「スキャンしたバーコードが不正です。」と表示される。 - ADD END

        String strBarcode1 = arrDataInList.get(0).getBarcodeCD1();
        String strBarcode2 = arrDataInList.get(0).getBarcodeCD2();
        String intBarcode1_3Character;
        String intBarcode2_3Character;

        if (!Constants.BLANK.equals(strBarcode1) && strBarcode1 != null) {
            intBarcode1_3Character = strBarcode1.substring(0, 3);
        }

        if (!Constants.BLANK.equals(strBarcode2) && strBarcode2 != null) {
            intBarcode2_3Character = strBarcode2.substring(0, 3);
        }

        if (intBarcode2_3Character == Constants.CD2_191 || intBarcode2_3Character == Constants.CD2_192) {
            if (intBarcode1_3Character == 0) {
                return true;
            }
        } else if (intBarcode1_3Character == Constants.CD1_978) {
            // SA-204#comment-1319947855 - ADD START
            if (Constants.BARCODE_INVENTORY_REGISTRATION.equals(strBarcode1)) {
                return false;
            }
            // SA-204#comment-1319947855 - ADD END
            return true;
        }
        return false;
    }*/

    @Override
    public void progressRfidFinish(String output, int typeRequestApi, String fileName) {
        // KILL ALL HTTP
        if(output.contains("Exception")){
            for(HttpPostRfid http : listHttp){
                http.cancel(true);
            }
        }
        System.out.println(output);
        System.out.println(output);
        try {
            Log.d("OUTPUT", output);
            System.out.println("KKKS: "+output);
            JSONObject jsonObject = new JSONObject(output);
            if (SupModRfidCommon.isStatusHttpOk(output)) {
                //if (jsonObject.getString(Constants.KEY_CODE).equals(Constants.VALUE_CODE_OK)) {
                if (jsonObject.getString(Constants.KEY_CODE).equals(Constants.VALUE_CODE_OK)) {
                    //setDataEntity(jsonObject.getJSONArray(Constants.KEY_DATA));
                    //JSONArray jArray = jsonObject.getJSONArray(Constants.KEY_DATA);
                    JSONArray jArray = jsonObject.getJSONArray(Constants.KEY_DATA);
                    //for(int i= 0 ; i < jArray.length();i++){
                        JSONArray jArray1 = jArray.getJSONArray(0);
                        /*String stringRfid= obj.getString(Constants.KEY_RFID);
                        // #MARK_2
                        if(setCustomOutput.add(stringRfid)){
                            setDataEntity(obj);
                        }*/
                        for (int j=0;j<jArray1.length();j++){
                            JSONObject obj2 = jArray1.getJSONObject(j);
                            String stringRfid= obj2.getString(Constants.KEY_RFID);
                            // #MARK_2
                            if(setCustomOutput.add(stringRfid)){

                                setDataEntity(obj2);
                            }
                        }

                    JSONArray err = jArray.getJSONArray(1);
                    if (err != null) {
                        for (int i = 0; i < err.length(); i++) {
                            // #MARK_4
                            setRfidNotFound.add(err.get(i).toString());
                            // #ADD_ERROR
                            if(btn_error.getVisibility()==View.INVISIBLE)
                                btn_error.setVisibility(View.VISIBLE);
                        }

                        total_error.setText(MessageFormat.format("{0} : {1}", getText(R.string.total_error), setRfidNotFound.size()+""));
                        //btConnect.removeRfidWrong(setRfidNotFound);
                        //showDialog(err.get(0).toString(), err);
                    }
                    //}
                }  /*else{
                    if (jsonObject.getString(Constants.KEY_CODE).equals(Constants.VALUE_CODE_RFID_ERROR)) {
                        try{
                            //setDataEntity(jsonObject.getJSONArray(Constants.KEY_DATA));
                            JSONArray jArray = jsonObject.getJSONArray(Constants.KEY_DATA);
                            for(int i= 0 ; i < jArray.length();i++){
                                JSONObject obj = jArray.getJSONObject(i);
                                String stringRfid= obj.getString(Constants.KEY_RFID);
                                // #MARK_5
                                if(setCustomOutput.add(stringRfid))
                                    setDataEntity(obj);
                            }
                        }catch(JSONException ex){
                            ex.printStackTrace();
                        }

                        JSONArray err = jsonObject.getJSONArray(Constants.KEY_ERROR);
                        if (err != null) {
                            for (int i = 0; i < err.length(); i++) {
                                // #MARK_4
                                setRfidNotFound.add(err.get(i).toString());
                                // #ADD_ERROR
                                if(btn_error.getVisibility()==View.INVISIBLE)
                                    btn_error.setVisibility(View.VISIBLE);
                            }

                            total_error.setText(MessageFormat.format("{0} : {1}", getText(R.string.total_error), setRfidNotFound.size()+""));
                            //btConnect.removeRfidWrong(setRfidNotFound);
                            //showDialog(err.get(0).toString(), err);
                        }
                    } else {
                        SupModRfidCommon.ToastMessage(ScanDataActivity.this, jsonObject.getString(Constants.KEY_MESSAGE)).show();
                    }
                }*/
            } else {
                SupModRfidCommon.showNotifyErrorDialog(ScanDataActivity.this).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // #ADD_ERROR
    private void showDialog() {
        PAUSE_DEVICE=1;
        String message = "" ;
        for(String i : setRfidNotFound){
            message+=i+"\r\n";
        }
        String title = "▲　RFID Invalid";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PAUSE_DEVICE=0;
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }




}
