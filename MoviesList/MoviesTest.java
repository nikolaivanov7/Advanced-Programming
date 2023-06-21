package MoviesList;

import java.util.*;
import java.util.stream.Collectors;

class Movie {
    String title;
    int[] ratings;

    public Movie(String title, int[] ratings) {
        this.title = title;
        this.ratings = ratings;
    }

    public String getTitle() {
        return title;
    }

    public int[] getRatings() {
        return ratings;
    }

    public double average() {
        int sum = 0;
        for (int i = 0; i < ratings.length; i++) {
            sum += ratings[i];
        }
        return (double) sum / ratings.length;
    }

    public int getSize() {
        return ratings.length;
    }

    public double coef() {
        return average() * getSize() / getSize();
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings", title, average(), getSize());
    }
}

class MoviesList {
    List<Movie> movies;

    public MoviesList() {
        movies = new ArrayList<>();
    }


    public void addMovie(String title, int[] ratings) {
        Movie movie = new Movie(title, ratings);
        movies.add(movie);
    }


    public List<Movie> top10ByAvgRating() {
        return movies
                .stream()
                .sorted(Comparator.comparing(Movie::average).reversed().thenComparing(Movie::getTitle))
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Movie> top10ByRatingCoef() {
        int max = movies.stream()
                .mapToInt(movie -> movie.getRatings().length)
                .max().getAsInt();
        Comparator<Movie> comparator = (o1, o2) -> {
            double first = o1.average() * o1.getSize() / max;
            double second = o2.average() * o2.getSize() / max;

            if (Double.compare(second, first) == 0)
                return o1.getTitle().compareTo(o2.getTitle());

            return Double.compare(second, first);
        };
        return movies
                .stream()
                .sorted(comparator)
                .limit(10)
                .collect(Collectors.toList());
    }
}

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde
