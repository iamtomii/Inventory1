package com.example.inventoryapplication.adapters;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.inventoryapplication.R;
import com.example.inventoryapplication.common.constants.Constants;
import com.example.inventoryapplication.common.entities.InforProductEntity;

import java.util.LinkedList;

/**
 * List View Adapter for Search Data Screen
 *
 * @author Tai-LQ
 * @since 2019/06/28
 */
public class ListViewSearchAdapter extends BaseAdapter {

    private LinkedList<InforProductEntity> listProduct;
    private Activity activity;
    private int offset;
    private int sizeList;

    public ListViewSearchAdapter(Activity activity, LinkedList<InforProductEntity> listProduct, int offset) {

        super();
        this.listProduct = listProduct;
        this.activity = activity;
        this.offset = offset;
        this.sizeList = listProduct.size();
    }

    /**
     * Init View Holder
     */
    private class ViewHolder {

        TextView lv_title_column1;
        TextView lv_title_column2;
        TextView lv_title_column3;
        TextView lv_title_column4;

    }

    /**
     * Get count item
     */
    @Override
    public int getCount() {
        return listProduct.size();
    }

    /**
     * Get item at index
     */
    @Override
    public Object getItem(int position) {
        return listProduct.get(position);
    }

    /**
     * Get Item Id with position
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Set custom layout for list view
     *
     * @param position    int
     * @param convertView {@link View}
     * @param parent      {@link ViewGroup}
     */
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ViewHolder viewHolder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if(convertView == null) {
            // Init custom layout list scan
            convertView = inflater.inflate(R.layout.adapter_list_scan1, null);
            viewHolder = new ViewHolder();

            // Init column list view
            viewHolder.lv_title_column1 = (TextView) convertView.findViewById(R.id.list_column1);
            viewHolder.lv_title_column2 = (TextView) convertView.findViewById(R.id.list_column2);
            viewHolder.lv_title_column3 = (TextView) convertView.findViewById(R.id.list_column3);
            viewHolder.lv_title_column4 = (TextView) convertView.findViewById(R.id.list_column4);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Set information for each item in list view
        InforProductEntity item = listProduct.get(position);

        viewHolder.lv_title_column1.setText(String.valueOf(sizeList - position));
        viewHolder.lv_title_column2.setText(item.getGoodName());
        viewHolder.lv_title_column3.setText(item.getBarcodeCD1());
        viewHolder.lv_title_column4.setText(String.valueOf(item.getQuantity()));

        // Set background color and text color
/*        if(position%2 == 0) {
            // Set even line
            convertView.setBackgroundColor(Color.parseColor(Constants.BACKGROUND_COLOR_BLUE_GRAY_LIGHT));
            viewHolder.lv_title_column1.setTextColor(Color.BLACK);
            viewHolder.lv_title_column2.setTextColor(Color.BLACK);
            viewHolder.lv_title_column3.setTextColor(Color.BLACK);
            viewHolder.lv_title_column4.setTextColor(Color.BLACK);
        } else {
            // Set odd line
            convertView.setBackgroundColor(Color.parseColor(Constants.BACKGROUND_COLOR_BLUE_GRAY_LIGHT));
            viewHolder.lv_title_column1.setTextColor(Color.BLACK);
            viewHolder.lv_title_column2.setTextColor(Color.BLACK);
            viewHolder.lv_title_column3.setTextColor(Color.BLACK);
            viewHolder.lv_title_column4.setTextColor(Color.BLACK);
        }

        // #HUYNHQUANGVINH change text color column quantity when is exist rfid code
        if (item.getRfidCode() != null && !item.getRfidCode().equals("")) {
            viewHolder.lv_title_column1.setTypeface(null, Typeface.BOLD);
            viewHolder.lv_title_column2.setTypeface(null, Typeface.BOLD);
            viewHolder.lv_title_column3.setTypeface(null, Typeface.BOLD);
            viewHolder.lv_title_column4.setTypeface(null, Typeface.BOLD);
        }*/

        return convertView;

    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

}
