package FINKI_Onion;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.*;

class Category {
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

abstract class NewsItem {
    private String title;
    private Date date;
    private Category category;

    public NewsItem(String title, Date date, Category category) {
        this.title = title;
        this.date = date;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public Category getCategory() {
        return category;
    }

    public abstract String getTeaser();
}

class TextNewsItem extends NewsItem {
    private String text;

    public TextNewsItem(String title, Date date, Category category, String text) {
        super(title, date, category);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String getTeaser() {
        StringBuilder teaser = new StringBuilder();
        teaser.append(getTitle()).append("\n");
        long minutesAgo = (System.currentTimeMillis() - getDate().getTime()) / (1000 * 60);
        teaser.append(minutesAgo).append("\n");
        teaser.append(text.substring(0, Math.min(80, text.length()))).append("\n");
        return teaser.toString();
    }
}

class MediaNewsItem extends NewsItem {
    private String url;
    private int views;

    public MediaNewsItem(String title, Date date, Category category, String url, int views) {
        super(title, date, category);
        this.url = url;
        this.views = views;
    }

    public String getUrl() {
        return url;
    }

    public int getViews() {
        return views;
    }

    @Override
    public String getTeaser() {
        StringBuilder teaser = new StringBuilder();
        teaser.append(getTitle()).append("\n");
        long minutesAgo = (System.currentTimeMillis() - getDate().getTime()) / (1000 * 60);
        teaser.append(minutesAgo).append("\n");
        teaser.append(url).append("\n");
        teaser.append(views).append("\n");
        return teaser.toString();
    }
}

class FrontPage {
    private List<NewsItem> newsItems;
    private List<Category> categories;

    public FrontPage(Category[] categories) {
        this.newsItems = new ArrayList<>();
        this.categories = Arrays.asList(categories);
    }

    public void addNewsItem(NewsItem newsItem) {
        newsItems.add(newsItem);
    }

    public List<NewsItem> listByCategory(Category category) {
        List<NewsItem> items = new ArrayList<>();
        for (NewsItem newsItem : newsItems) {
            if (newsItem.getCategory().equals(category)) {
                items.add(newsItem);
            }
        }
        return items;
    }

    public List<NewsItem> listByCategoryName(String categoryName) throws CategoryNotFoundException {
        for (Category category : categories) {
            if (category.getName().equals(categoryName)) {
                return listByCategory(category);
            }
        }
        throw new CategoryNotFoundException(categoryName);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (NewsItem newsItem : newsItems) {
            result.append(newsItem.getTeaser());
        }
        return result.toString();
    }
}

class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(String categoryName) {
        super("Category " + categoryName + " was not found");
    }
}


public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for (Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch (CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}


// Vasiot kod ovde