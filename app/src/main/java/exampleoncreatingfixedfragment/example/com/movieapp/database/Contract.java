package exampleoncreatingfixedfragment.example.com.movieapp.database;

import android.provider.BaseColumns;

/**
 * Created by 450 G1 on 15/11/2016.
 */

public class Contract {

    public static final class DetailEntry  {

        public static final String TABLE_NAME = "Detail";

        public static final String COLUMN_ID = "id";

        public static final String COLUMN_BACKDROP = "back_drop";

        public static final String COLUMN_POSTER = "poster";

        public static final String COLUMN_FILM_NAME = "film_name";

        public static final String COLUMN_YEAR = "year";

        public static final String COLUMN_VOTE = "vote";

        public static final String COLUMN_OVER_VIEW = "over_view";

        public static final String COLUMN_TRIALER_KEY = "trailer_key";

    }


    public static final class TrailerEntry {

        public static final String TABLE_NAME = "Trailer";

        public static final String COLUMN_M_ID = "M_id";

        public static final String COLUMN_LINK = "path";

    }
    public static final class ReviewEntry {

        public static final String TABLE_NAME = "Review";

        public static final String COLUMN_M_ID = "M_id";

        public static final String COLUMN_AUTHOR = "author";

        public static final String COLUMN_CONTENT = "content";
    }
}