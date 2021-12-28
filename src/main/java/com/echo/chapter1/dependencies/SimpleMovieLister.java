package com.echo.chapter1.dependencies;

public class SimpleMovieLister {
    private MovieFinder movieFinder;

    public void setMovieFinder(MovieFinder movieFinder){
        this.movieFinder = movieFinder;
    }
}
