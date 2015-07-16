package com.prd.testlistview;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.prd.testlistview.bean.TestBean;
import com.prd.testlistview.view.TwoWayView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {


    private ListView mListView;
    private List<TestBean> mVerticalList = new ArrayList<TestBean>();
    private List<String> mHorizontalList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int j = 1; j <= 50; j++) {
            mHorizontalList = new ArrayList<String>();
            for (int i = 1; i <= 50; i++) {
                mHorizontalList.add("Item " + i);
            }
            mVerticalList.add(new TestBean("list"+j,mHorizontalList, 0, 0));
        }

        mListView = (ListView) findViewById(R.id.main_list);
        MyAdapter adapter = new MyAdapter();
        mListView.setAdapter(adapter);
    }

    class MyAdapter extends BaseAdapter implements TwoWayView.OnScrollListener
    {
        LayoutInflater inflater;

        public MyAdapter() {
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mVerticalList.size();
        }

        @Override
        public Object getItem(int position) {
            return mVerticalList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder{
            TwoWayView mTwoWayView;
            MyTwoWayAdapter adapter;
            TextView mtv;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if(convertView == null){

                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.vertical_row, parent, false);
                holder.mTwoWayView = (TwoWayView) convertView.findViewById(R.id.twv_v);
                holder.mTwoWayView.setOnScrollListener(this);

                holder.adapter = new MyTwoWayAdapter();
                holder.mTwoWayView.setAdapter(holder.adapter);

                holder.mtv = (TextView) convertView.findViewById(R.id.tv_v);

                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mTwoWayView.setTag(position);

            holder.adapter.setData(mVerticalList.get(position).getList());// notify Adapter with data
            holder.mTwoWayView.setSelectionFromOffset(mVerticalList.get(position).getIndex(), mVerticalList.get(position).getOffSet()); // setting the exact state of ListView as scrolled previously

            holder.mtv.setText(mVerticalList.get(position).getName());

            return convertView;
        }

        @Override
        public void onScrollStateChanged(TwoWayView view, int scrollState) {

            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                // do nothing
            }
            else{
                // set the previous state of ListView as scrolled
                int index = view.getFirstVisiblePosition();
                View v = view.getChildAt(0);
                int left = (v == null) ? 0 : v.getLeft();

                mVerticalList.get((Integer)view.getTag()).setIndex(index);
                mVerticalList.get((Integer)view.getTag()).setOffSet(left);
            }
        }

        @Override
        public void onScroll(TwoWayView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }

    class MyTwoWayAdapter extends BaseAdapter
    {
        LayoutInflater inflater;

        public MyTwoWayAdapter() {
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        private void setData(List<String> mList) {
            mHorizontalList = mList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mHorizontalList.size();
        }

        @Override
        public Object getItem(int position) {
            return mHorizontalList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder{
            TextView mTextView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if(convertView == null){

                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.horizontal_row, parent, false);
                holder.mTextView = (TextView) convertView.findViewById(R.id.mTextView);

                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }

            if(position % 2 == 0){
                holder.mTextView.setBackgroundColor(Color.BLUE);
                holder.mTextView.setTextColor(Color.YELLOW);
            }
            else{
                holder.mTextView.setBackgroundColor(Color.YELLOW);
                holder.mTextView.setTextColor(Color.BLUE);
            }
            holder.mTextView.setText(mHorizontalList.get(position).toString());

            return convertView;
        }
    }



}
