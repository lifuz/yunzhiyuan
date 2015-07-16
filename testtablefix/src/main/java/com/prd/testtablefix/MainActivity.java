package com.prd.testtablefix;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inqbarna.tablefixheaders.TableFixHeaders;
import com.inqbarna.tablefixheaders.adapters.BaseTableAdapter;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TableFixHeaders tfh = (TableFixHeaders) findViewById(R.id.main_table);
        tfh.setAdapter(new MyAdapter(this));

//        tfh.setFocusableInTouchMode(true);



    }

    public class MyAdapter extends BaseTableAdapter {

        private final Context context;
        private final LayoutInflater inflater;

        private final int width;
        private final int height;

        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);

            //从
            Resources rs = context.getResources();

            width = rs.getDimensionPixelSize(R.dimen.table_width);
            height = rs.getDimensionPixelSize(R.dimen.table_height);


        }

        public Context getContext() {
            return context;
        }

        public LayoutInflater getInflater() {
            return inflater;
        }


        /**
         * 设置行数
         * @return
         */
        @Override
        public int getRowCount() {
            return 20;
        }

        /**
         * 设置列数
         * @return
         */
        @Override
        public int getColumnCount() {
            return 6;
        }

        /**
         * 为每个View设置布局和值
         * @param row
         *            The row of the item within the adapter's data table of the
         *            item whose view we want. If the row is <code>-1</code> it is
         *            the header.
         * @param column
         *            The column of the item within the adapter's data table of the
         *            item whose view we want. If the column is <code>-1</code> it
         *            is the header.
         * @param view
         * @param viewGroup
         * @return
         */
        @Override
        public View getView(int row, int column, View view, ViewGroup viewGroup) {

            if(view == null){
                view = inflater.inflate(getLayoutResource(row,column),viewGroup,false);
            }

            ((TextView)view.findViewById(R.id.item_tv)).setText(getCellString(row,column));

            return view;
        }

        /**
         * 设置每个View的值
         * @param row
         * @param column
         * @return
         */

        public String getCellString(int row,int column){
            return  "Lorem (" + row + ", " + column + ")";
        }

        /**
         * 设置每个View的宽度
         * @param i
         * @return
         */
        @Override
        public int getWidth(int i) {
            return width;
        }

        /**
         * 设置每个View的高度
         * @param i
         * @return
         */
        @Override
        public int getHeight(int i) {
            return height;
        }

        /**
         *
         * 根据行数确定view的类型
         *
         * @param row
         *            The row of the item within the adapter's data table of the
         *            item whose view we want. If the row is <code>-1</code> it is
         *            the header.
         * @param column
         *            The column of the item within the adapter's data table of the
         *            item whose view we want. If the column is <code>-1</code> it
         *            is the header.
         * @return
         */
        @Override
        public int getItemViewType(int row, int column) {
            if(row < 0){
                return 0;
            } else {
                return 1;
            }

        }

        /**
         * 根据view的类型选择相应的布局
         * @param row
         * @param column
         * @return
         */
        public int getLayoutResource(int row,int column){
            final int layoutResource;
            switch (getItemViewType(row,column)){
                case 0:
                    layoutResource = R.layout.table_header_item;
                    break;
                case 1:
                    layoutResource =R.layout.table_item;
                    break;
                default:
                    throw new RuntimeException("wtf?");
            }
            return layoutResource;
        }


        /**
         * 设置View的类型总数
         * @return
         */
        @Override
        public int getViewTypeCount() {
            return 2;
        }
    }


}
