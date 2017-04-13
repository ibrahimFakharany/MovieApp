package exampleoncreatingfixedfragment.example.com.movieapp.itemsClasses;

/**
 * Created by 450 G1 on 14/11/2016.
 */

public class Reviews {
    private String author;
    private String content;

    public Reviews(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
