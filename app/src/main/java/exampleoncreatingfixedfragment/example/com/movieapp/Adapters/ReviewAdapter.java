package exampleoncreatingfixedfragment.example.com.movieapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import exampleoncreatingfixedfragment.example.com.movieapp.R;
import exampleoncreatingfixedfragment.example.com.movieapp.itemsClasses.Reviews;

/**
 * Created by 450 G1 on 14/11/2016.
 */

public class ReviewAdapter extends ArrayAdapter {
    List<Reviews> mylist;
    Context context;
    public ReviewAdapter(Context context, List<Reviews> objects) {
        super(context, 0, objects);
        mylist = objects;
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null )
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.review_layout_element, parent,false);
        }
        TextView authorTV = (TextView) convertView.findViewById(R.id.author_tv);
        authorTV.setText(" "+mylist.get(position).getAuthor());
        TextView contentTv = (TextView) convertView.findViewById(R.id.content_tv);
        contentTv.setText(mylist.get(position).getContent());
        return convertView;
    }
}
