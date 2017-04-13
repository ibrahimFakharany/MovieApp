package exampleoncreatingfixedfragment.example.com.movieapp.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import exampleoncreatingfixedfragment.example.com.movieapp.Adapters.ReviewAdapter;
import exampleoncreatingfixedfragment.example.com.movieapp.Adapters.TrailerAdapter;
import exampleoncreatingfixedfragment.example.com.movieapp.R;
import exampleoncreatingfixedfragment.example.com.movieapp.itemsClasses.Reviews;
import exampleoncreatingfixedfragment.example.com.movieapp.itemsClasses.Trailers;



public class DetailFragment extends android.support.v4.app.Fragment {
    private static final String posterBase = "http://image.tmdb.org/t/p/w185/";
    List<Reviews> reviewList;
    ListView reviewsLV;
    ListView listView ;
    String resultJSONstr;
    List<String> keysList;
    Integer id;
    String backDropPath;
    String poster;
    String title;
    String year = "";
    String vote;
    String overview;
    SharedPreferences.Editor editor;
    SharedPreferences pr;


    @Override
    public void onStart() {
        super.onStart();
        getData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detail_fragment_layout, container, false);
        pr = PreferenceManager.getDefaultSharedPreferences(getActivity());
        reviewsLV = (ListView) v.findViewById(R.id.review);
        listView = (ListView) v.findViewById(R.id.trailer);
        Bundle bundle = getArguments();

        poster = bundle.getString("poster");
        title = bundle.getString("title");
        vote = bundle.getString("vote");
        String date = bundle.getString("date");
        overview = bundle.getString("overview");
        id = bundle.getInt("id",0);
        backDropPath = bundle.getString("backdropPath");
        System.out.println(poster+" "+title+" "+vote+" "+date+" "+overview+" "+id+" "+backDropPath +"hello from " +
                "fragment detail ");

        /*ImageView imageView = (ImageView) v.findViewById(R.id.backDropImage);
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185///" + backDropPath).into(imageView);*/

        TextView nameMovie = (TextView) v.findViewById(R.id.title);
        nameMovie.setText(title);
        ImageView posterMovie = (ImageView) v.findViewById(R.id.poster_movie);
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + poster).into(posterMovie);

        TextView yearTF = (TextView) v.findViewById(R.id.date_movie);


        for (int i = 0; i <= 3; i++) {
            year += date.charAt(i);
        }
        yearTF.setText(year);

        TextView vote_avg = (TextView) v.findViewById(R.id.vote_avg_movie);
        vote_avg.setText(vote);

        TextView over = (TextView) v.findViewById(R.id.overView);
        over.setText("\n" + overview);
        Button btn = (Button) v.findViewById(R.id.add_favorit_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertFavouritData(Integer.toString(id));
            }
        });



        return v;
    }

    public void insertFavouritData(String idValue) {
        String idTemp = pr.getString("id", "");
        Boolean found = false;
        if (idTemp != "") {
            found = idTemp.contains(idValue);
        }
        if (found == false) {
            idTemp += idValue + ",";
            editor = pr.edit();
            editor.putString("id", idTemp);
            editor.commit();
            Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "this film is already in favourit", Toast.LENGTH_LONG).show();
        }

    }

    public void getData() {
        FetchVideos fetchVideos = new FetchVideos();
        FetchReviews fetchReviews = new FetchReviews();
        fetchReviews.execute(id);
        fetchVideos.execute(id);

    }

    private class FetchVideos extends AsyncTask<Integer, Void, List<String>> {
        private String LOG_TAG = FetchVideos.class.getSimpleName();

        @Override
        protected void onPostExecute(List<String> list) {

            List<Trailers> trailerText = new ArrayList<>();



            for (int i = 0; i < list.size(); i++) {
                trailerText.add(new Trailers(i + 1));
            }

            TrailerAdapter trailerAdapter = new TrailerAdapter(getActivity(), trailerText);

            listView.setAdapter(trailerAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch")
                            .buildUpon()
                            .appendQueryParameter("v", keysList.get(position))
                            .build()));
                }
            });
        }

        @Override
        protected List<String> doInBackground(Integer... params) {

            final String APIKEY_key = "api_key";
            final String APIKEY_value = "98424b94af97868ad653ab4fc3f9b912";
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(Integer.toString(params[0]))
                    .appendPath("videos")
                    .appendQueryParameter(APIKEY_key, APIKEY_value);

            String strUri = builder.build().toString();
            if (params.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {


                URL url = new URL(strUri);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                resultJSONstr = buffer.toString();
                // Log.v(LOG_TAG, "respond string: " + resultJSONstr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "error", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.v(LOG_TAG, "error closing", e);
                    }
                }
            }


            try {
                return getTrailers(resultJSONstr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public List<String> getTrailers(String respond) throws JSONException {
            JSONObject parentObject = new JSONObject(respond);
            JSONArray resultsArray = parentObject.getJSONArray("results");
            keysList = new ArrayList<>();
            JSONObject objects;
            String key;
            for (int i = 0; i < resultsArray.length(); i++) {
                objects = resultsArray.getJSONObject(i);
                key = objects.getString("key");
                keysList.add(key);
            }
            return keysList;
        }
    }

    private class FetchReviews extends AsyncTask<Integer, Void, List<Reviews>> {
        private String LOG_TAG = FetchReviews.class.getSimpleName();

        public List<Reviews> getReviews(String respond) throws JSONException {
            JSONObject parentObject = new JSONObject(respond);
            JSONArray resultsArray = parentObject.getJSONArray("results");

            reviewList = new ArrayList<>();
            JSONObject objects;

            String author, content;
            for (int i = 0; i < resultsArray.length(); i++) {
                objects = resultsArray.getJSONObject(i);
                author = objects.getString("author");
                content = objects.getString("content");
                System.out.println("author------>" + author);
                reviewList.add(new Reviews(author, content));
            }
            return reviewList;
        }

        @Override
        protected List<Reviews> doInBackground(Integer... params) {
            final String APIKEY_key = "api_key";
            final String APIKEY_value = "98424b94af97868ad653ab4fc3f9b912";
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(Integer.toString(params[0]))
                    .appendPath("reviews")
                    .appendQueryParameter(APIKEY_key, APIKEY_value);

            String strUri = builder.build().toString();
            if (params.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {


                URL url = new URL(strUri);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                resultJSONstr = buffer.toString();
                Log.v(LOG_TAG, "respond string: " + resultJSONstr);


            } catch (IOException e) {
                Log.e(LOG_TAG, "error", e);

                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.v(LOG_TAG, "error closing", e);
                    }
                }
            }


            try {
                return getReviews(resultJSONstr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Reviews> reviewses) {
            super.onPostExecute(reviewses);
            ReviewAdapter reviewAdapter = new ReviewAdapter(getActivity(), reviewses);
            reviewsLV.setAdapter(reviewAdapter);
        }
    }


}

