package exampleoncreatingfixedfragment.example.com.movieapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import exampleoncreatingfixedfragment.example.com.movieapp.R;
import exampleoncreatingfixedfragment.example.com.movieapp.itemsClasses.Movie;
import exampleoncreatingfixedfragment.example.com.movieapp.itemsClasses.Trailers;

/**
 * Created by 450 G1 on 14/11/2016.
 */

public class TrailerAdapter extends ArrayAdapter {
    List<Trailers> mylist;
    Context context;

    public TrailerAdapter(Context con,List<Trailers> mylists){
        super(con,0, mylists);
        context = con;
        mylist = mylists;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null )
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_view_item, parent,false);
        }


        TextView trailerText = (TextView) convertView.findViewById(R.id.trailer_count);
        trailerText.setText(mylist.get(position).getTrailerStr());
        return convertView;
    }
}
