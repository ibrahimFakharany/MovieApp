package exampleoncreatingfixedfragment.example.com.movieapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import exampleoncreatingfixedfragment.example.com.movieapp.Fragments.DetailFragment;
import exampleoncreatingfixedfragment.example.com.movieapp.R;

/**
 * Created by 450 G1 on 03/11/2016.
 */

public class DetailActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity_layout);
        DetailFragment detailFragment = new DetailFragment();
        Intent sentIntent = getIntent();
        Bundle sentBundle = sentIntent.getExtras();
        detailFragment.setArguments(sentBundle);
        if(savedInstanceState == null)
        getSupportFragmentManager().beginTransaction().replace(R.id.fldetail, detailFragment, "").commit();



    }
}/**/
