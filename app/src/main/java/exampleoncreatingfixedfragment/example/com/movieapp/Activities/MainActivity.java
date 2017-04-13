package exampleoncreatingfixedfragment.example.com.movieapp.Activities;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import exampleoncreatingfixedfragment.example.com.movieapp.Fragments.DetailFragment;
import exampleoncreatingfixedfragment.example.com.movieapp.Fragments.MainFragment;
import exampleoncreatingfixedfragment.example.com.movieapp.R;
import exampleoncreatingfixedfragment.example.com.movieapp.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity implements Listener{
    boolean checkTwoPane = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainFragment mainFragment = new MainFragment();
        mainFragment.setListener(this);
        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.flmain, mainFragment, "").commit();

        }
        if(null != findViewById(R.id.fldetail))
        {
            checkTwoPane = true;
        }


    }

    @Override
    public void viewFilmDetail(String poster, String title, String vote, String date, String overview, int id, String backdropPath) {
        if(!checkTwoPane){
            Intent intent = new Intent(this, DetailActivity.class);
                            intent.putExtra("poster", poster);
                            intent.putExtra("title", title);
                            intent.putExtra("vote", vote);
                            intent.putExtra("date", date);
                            intent.putExtra("overview", overview);
                            intent.putExtra("id", id);
                            intent.putExtra("backdropPath", backdropPath);
                            startActivity(intent);
        }
        else {
            DetailFragment detailFragment = new DetailFragment();
            Bundle extras = new Bundle();
            extras.putString("poster", poster);
            extras.putString("title", title);
            extras.putString("vote", vote);
            extras.putString("date", date);
            extras.putString("overview", overview);
            extras.putInt("id", id);
            extras.putString("backdropPath", backdropPath);
            System.out.println(poster+" "+title+" "+vote+" "+overview+" "+id+" "+backdropPath);
            detailFragment.setArguments(extras);

            getSupportFragmentManager().beginTransaction().replace(R.id.fldetail, detailFragment, "").commit();

        }
    }
}
