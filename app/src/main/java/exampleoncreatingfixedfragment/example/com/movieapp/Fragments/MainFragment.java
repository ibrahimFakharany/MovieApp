package exampleoncreatingfixedfragment.example.com.movieapp.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
import java.util.StringTokenizer;

import exampleoncreatingfixedfragment.example.com.movieapp.Activities.Listener;
import exampleoncreatingfixedfragment.example.com.movieapp.Adapters.customAdapter;
import exampleoncreatingfixedfragment.example.com.movieapp.Activities.DetailActivity;
import exampleoncreatingfixedfragment.example.com.movieapp.itemsClasses.Movie;
import exampleoncreatingfixedfragment.example.com.movieapp.R;


/**
 * Created by 450 G1 on 21/10/2016.
 */

public class MainFragment extends android.support.v4.app.Fragment {
    GridView gridView = null;
    GridView gridViewFavourit = null;
    String resultJSONstr = null;
    SharedPreferences pr;
    List<Integer> IDs;
    SharedPreferences.Editor editor;
    String overview = null, release_date = null, vote_count = null, title = null, value = null,
            backDropPath = null;
    int id = 0;
    List<Movie> postersPaths;
    List<Movie> concatenateTwoList;
    Listener listener ;

    public void setListener(Listener listener){
        this.listener = listener;
    }
    @Override

    public void onStart() {
        super.onStart();
        getData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void getData() {
        FetchData moviewTask = new FetchData();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String preferedSortedBy = prefs.getString(getString(R.string.sort_by), getString(R.string.sort_by_default_value));
        moviewTask.execute(preferedSortedBy);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_fragment_layout, container, false);
        gridViewFavourit = (GridView) v.findViewById(R.id.gridview);
        gridView = (GridView) v.findViewById(R.id.gridview);
        pr = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = pr.edit();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_option_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.setting_option_item) {
            editor.clear().commit();
            //startActivity(new Intent(getActivity(), SettingActivity.class));
            return true;
        } else if (id == R.id.favourit_option_menu) {
            IDs = new ArrayList<>();
            String idTemp = pr.getString("id", "no id");

            if (!(idTemp == "no id")) {

                StringTokenizer st = new StringTokenizer(idTemp, ",", false);

                while (st.hasMoreTokens()) {
                    String s = st.nextToken(",");
                    Integer i = Integer.valueOf(s);
                    IDs.add(i);
                }
                System.out.println("the size of list of IDs is -- > "+ IDs.size());
                generateFavouritPoseters();
            } else {
                System.out.println("no favourits found");
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void generateFavouritPoseters() {
        FetchFavouritData fetchFavouritData = new FetchFavouritData();
        FetchFavouritData fetch2  = new FetchFavouritData();
        concatenateTwoList = new ArrayList<>();
        fetchFavouritData.execute("top_rated");

        fetch2.execute("popular");

        /*if (!listPopular.isEmpty())
            for (int i = 0; i < listPopular.size(); i++) {
                concatenateTwoList.add(listPopular.get(i));
            }
         if (!listTopRated.isEmpty())
            for (int i = 0; i < listTopRated.size(); i++) {
                concatenateTwoList.add(listTopRated.get(i));
            }*/

    }


    //ASYNCTASK
    public class FetchData extends AsyncTask<String, Void, List<Movie>> {
        private String LOG_TAG = FetchData.class.getSimpleName();
        String res;
        public List<Movie> getImagesFromJSON(String respond) throws JSONException {

            JSONObject parentObject = new JSONObject(respond);
            JSONArray resultsArray = parentObject.getJSONArray("results");
            postersPaths = new ArrayList<>();

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject objects = resultsArray.getJSONObject(i);
                value = objects.getString("poster_path");
                id = objects.getInt("id");
                overview = objects.getString("overview");
                release_date = objects.getString("release_date");
                vote_count = objects.getString("vote_average");
                title = objects.getString("title");
                backDropPath = objects.getString("backdrop_path");
                postersPaths.add(new Movie(value, title, vote_count, release_date, overview, id, backDropPath));
            }
            return postersPaths;
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            //String path = "http://api.themoviedb.org/3/movie/top_rated?api_key=774c9627467113733738773c9c96c8c8" ;
            final String APIKEY_key = "api_key";
            final String APIKEY_value = "98424b94af97868ad653ab4fc3f9b912";

            //  final String sortBy_key = "sort_by";
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(params[0])
                    .appendQueryParameter(APIKEY_key, APIKEY_value);

            String strUri = builder.build().toString();

            // If there's no zip code, there's nothing to look up.  Verify size of params.
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
                return getImagesFromJSON(resultJSONstr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> strings) {
            if (strings != null) {
                customAdapter adapter = new customAdapter(getContext(), strings);
                gridView.setAdapter(adapter);

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if(IDs != null&& listener!= null){
                                System.out.println("in concatenateTwoList");
                                listener.viewFilmDetail(
                                        concatenateTwoList.get(position).getPoster(),
                                        concatenateTwoList.get(position).getTitle(),
                                        concatenateTwoList.get(position).getVote(),
                                        concatenateTwoList.get(position).getData(),
                                        concatenateTwoList.get(position).getOverview(),
                                        concatenateTwoList.get(position).getId(),
                                        concatenateTwoList.get(position).getBackdropPath()
                                );
                                IDs = null;
                            }
                            else if(listener != null){
                                System.out.println("sending data ");
                                listener.viewFilmDetail(
                                        postersPaths.get(position).getPoster(),
                                        postersPaths.get(position).getTitle(),
                                        postersPaths.get(position).getVote(),
                                        postersPaths.get(position).getData(),
                                        postersPaths.get(position).getOverview(),
                                        postersPaths.get(position).getId(),
                                        postersPaths.get(position).getBackdropPath()
                                );
                            }

                            /*Intent intent = new Intent(getActivity(), DetailActivity.class);
                            intent.putExtra("poster", postersPaths.get(position).getPoster());
                            intent.putExtra("title", postersPaths.get(position).getTitle());
                            intent.putExtra("vote", postersPaths.get(position).getVote());
                            intent.putExtra("date", postersPaths.get(position).getData());
                            intent.putExtra("overview", postersPaths.get(position).getOverview());
                            intent.putExtra("id", postersPaths.get(position).getId());
                            intent.putExtra("backdropPath", postersPaths.get(position).getBackdropPath());
                            startActivity(intent);*/
                        }
                    });




            }

        }
    }

    public class FetchFavouritData extends AsyncTask<String, List<Movie>, List<Movie>> {
        private String LOG_TAG = FetchData.class.getSimpleName();

        public List<Movie> getImagesFromJSON(String respond) throws JSONException {

            JSONObject parentObject = new JSONObject(respond);
            JSONArray resultsArray = parentObject.getJSONArray("results");


            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject objects = resultsArray.getJSONObject(i);
                value = objects.getString("poster_path");
                id = objects.getInt("id");
                overview = objects.getString("overview");
                release_date = objects.getString("release_date");
                vote_count = objects.getString("vote_average");
                title = objects.getString("title");
                backDropPath = objects.getString("backdrop_path");
                if(IDs.contains(id))
                concatenateTwoList.add(new Movie(value, title, vote_count, release_date, overview, id, backDropPath));
            }
            return concatenateTwoList;
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            //String path = "http://api.themoviedb.org/3/movie/top_rated?api_key=774c9627467113733738773c9c96c8c8" ;
            final String APIKEY_key = "api_key";
            final String APIKEY_value = "98424b94af97868ad653ab4fc3f9b912";
            //  final String sortBy_key = "sort_by";
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    // .appendPath("discover")
                    .appendPath("movie")
                    .appendPath(params[0])
                    //  .appendQueryParameter(sortBy_key, params[0])
                    .appendQueryParameter(APIKEY_key, APIKEY_value);

            String strUri = builder.build().toString();
            // If there's no zip code, there's nothing to look up.  Verify size of params.
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
                return getImagesFromJSON(resultJSONstr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(List<Movie> strings) {
            if (strings != null) {
                customAdapter adapter2 = new customAdapter(getContext(), strings);
                gridView.setAdapter(adapter2);

            }

        }

    }

}
