package exampleoncreatingfixedfragment.example.com.movieapp.itemsClasses;

/**
 * Created by 450 G1 on 14/11/2016.
 */

public class Trailers {
    private String count;
    public Trailers(int count) {
        this.count = " Trailer "+count;
    }
    public String getTrailerStr()
    {
        return count;
    }
}

