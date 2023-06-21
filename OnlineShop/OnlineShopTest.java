package OnlineShop;

import java.time.LocalDateTime;
import java.util.*;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class Product {
    private String category;
    private String id;
    private String name;
    private LocalDateTime createdAt;
    private double price;
    private int soldQuantity;

    public Product(String category, String id, String name, LocalDateTime createdAt, double price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
        this.soldQuantity = 0;
    }

    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void incrementSoldQuantity(int quantity) {
        soldQuantity += quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", price=" + price +
                ", quantitySold=" + soldQuantity +
                '}';
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String message) {
        super(message);
    }
}

class OnlineShop {
    private Map<String, List<Product>> productsByCategory;
    private Map<String, Product> productsById;

    public OnlineShop() {
        productsByCategory = new HashMap<>();
        productsById = new HashMap<>();
    }

    public void addProduct(String category, String id, String name, LocalDateTime createdAt, double price) {
        Product product = new Product(category, id, name, createdAt, price);
        productsById.put(id, product);
        productsByCategory.computeIfAbsent(category, key -> new ArrayList<>()).add(product);
    }

    public double buyProduct(String id, int quantity) throws ProductNotFoundException {
        Product product = productsById.get(id);
        if (product == null) {
            throw new ProductNotFoundException("Product with id " + id + " does not exist in the online shop!");
        }
        double totalPrice = product.getPrice() * quantity;
        product.incrementSoldQuantity(quantity);
        return totalPrice;
    }

    public List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<Product> products;
        if (category != null) {
            products = productsByCategory.getOrDefault(category, new ArrayList<>());
        } else {
            products = new ArrayList<>(productsById.values());
        }

        Comparator<Product> comparator = getComparator(comparatorType);
        products.sort(comparator);

        List<List<Product>> result = new ArrayList<>();
        int totalProducts = products.size();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        for (int i = 0; i < totalPages; i++) {
            int fromIndex = i * pageSize;
            int toIndex = Math.min((i + 1) * pageSize, totalProducts);
            List<Product> page = products.subList(fromIndex, toIndex);
            result.add(page);
        }

        return result;
    }

    private Comparator<Product> getComparator(COMPARATOR_TYPE comparatorType) {
        switch (comparatorType) {
            case NEWEST_FIRST:
                return Comparator.comparing(Product::getCreatedAt).reversed();
            case OLDEST_FIRST:
                return Comparator.comparing(Product::getCreatedAt);
            case LOWEST_PRICE_FIRST:
                return Comparator.comparing(Product::getPrice);
            case HIGHEST_PRICE_FIRST:
                return Comparator.comparing(Product::getPrice).reversed();
            case MOST_SOLD_FIRST:
                return Comparator.comparing(Product::getSoldQuantity).reversed();
            case LEAST_SOLD_FIRST:
                return Comparator.comparing(Product::getSoldQuantity);
            default:
                throw new IllegalArgumentException("Invalid comparator type: " + comparatorType);
        }
    }
}

public class OnlineShopTest {
    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category = null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);
    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}

