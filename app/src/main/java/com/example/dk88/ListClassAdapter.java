package com.example.dk88;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListClassAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<GroupInfo> namelist;

    public ListClassAdapter(Context context, int layout, List<GroupInfo> namelist) {
        this.context = context;
        this.layout = layout;
        this.namelist = namelist;
    }

    @Override
    public int getCount() {
        return namelist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class ViewHolder{
        TextView txtclass;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if(view==null){
            LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view =inflater.inflate(layout, null);
            holder =new ViewHolder();

            //ánh xạ view
            holder.txtclass=(TextView) view.findViewById(R.id.name);


            view.setTag(holder);
        }
        else{
            holder = (ViewHolder) view.getTag();
        }
        GroupInfo classname = namelist.get(position);
        holder.txtclass.setText(classname.getLophp());




        return view;
    }
}
