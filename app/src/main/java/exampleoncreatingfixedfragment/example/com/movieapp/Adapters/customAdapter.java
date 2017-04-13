package exampleoncreatingfixedfragment.example.com.movieapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import exampleoncreatingfixedfragment.example.com.movieapp.itemsClasses.Movie;
import exampleoncreatingfixedfragment.example.com.movieapp.R;


public class customAdapter extends ArrayAdapter {
    List<Movie> mylist;
    Context context;

    public customAdapter(Context con,List<Movie> mylists){
        super(con,0, mylists);
        context = con;
        mylist = mylists;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null )
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.image_item, parent,false);
        }
        ImageView iv_moviewPoster = (ImageView)convertView.findViewById(R.id.iv_movieposter);
                Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w185/"+mylist.get(position).getPoster().toString())
                .into(iv_moviewPoster);

        return convertView;
    }
}
