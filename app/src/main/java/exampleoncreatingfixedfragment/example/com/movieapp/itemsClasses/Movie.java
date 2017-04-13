package exampleoncreatingfixedfragment.example.com.movieapp.itemsClasses;

/**
 * Created by 450 G1 on 04/11/2016.
 */

public class Movie {
    private String poster;
    private String title;
    private String overview;
    private String vote;
    private String data;
    private Integer id;
    private String backdrop_path;

    public int getId() {
        return id;
    }

    public Movie(String poster, String title, String vote, String data, String overview, int id, String backdrop_path) {
        this.poster = poster;
        this.title = title;
        this.overview = overview;
        this.vote = vote;
        this.data = data;
        this.id=id;
        this.backdrop_path= backdrop_path;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }

    public String getPoster() {
        return poster;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getVote() {
        return vote;
    }

    public String getData() {
        return data;
    }
}
