package net.toilet.my.is.where.com.whereismytoilet.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.toilet.my.is.where.com.whereismytoilet.R;
import net.toilet.my.is.where.com.whereismytoilet.Models.Toilette;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by quentinbaillet on 23/03/15.
 */
public class ToiletListAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<Object> mData = new ArrayList<Object>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    public ToiletListAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final Toilette item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.view_data_list, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.view_data_list_text);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.view_header_list, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.view_header_list_text);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        switch (rowType) {
            case TYPE_ITEM:
                holder.textView.setText(((Toilette)mData.get(position)).getAdresse());
                break;
            case TYPE_SEPARATOR:
                holder.textView.setText((String)mData.get(position));
                break;
        }


        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
    }

}
