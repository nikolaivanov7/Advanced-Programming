package Shopping;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class InvalidOperationException extends Exception {
    public InvalidOperationException(String id) {
        super(String.format("The quantity of the product with id %s can not be 0.", id));
    }

    public InvalidOperationException()
    {
        super(String.format("There are no products with discount."));
    }
}

class Product {
    String type;
    int productId;
    String productName;
    int price;
    String quantity;

    public Product(String type, int productId, String productName, int price, String quantity) {
        this.type = type;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public double calculatePrice() {
        double quantityInKg = 0.0;
        if (type.equals("WS"))
            return price * Integer.parseInt(quantity);
        else
            quantityInKg = Double.parseDouble(quantity) / 1000.0;
        return price * quantityInKg;
    }

    @Override
    public String toString() {
        return String.format("%d - %.2f", productId, calculatePrice());
    }
}

class ShoppingCart {
    List<Product> products;

    public ShoppingCart() {
        products = new ArrayList<>();
    }

    public void addItem(String itemData) throws InvalidOperationException {
        String[] parts = itemData.split(";");
        if (parts[4].equals("0")) {
            throw new InvalidOperationException(parts[1]);
        } else {
            Product product = new Product(parts[0], Integer.parseInt(parts[1]), parts[2], Integer.parseInt(parts[3]), parts[4]);
            products.add(product);
        }
    }

    public void printShoppingCart(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        products.stream()
                .sorted(Comparator.comparing(Product::calculatePrice).reversed())
                .forEach(pw::println);
        pw.flush();
    }


    public void blackFridayOffer(List<Integer> discountItems, OutputStream os) throws InvalidOperationException {
        PrintWriter pw = new PrintWriter(os);
        double discountedPrice = 0.0;
        double discount = 0.0;
        if(discountItems.isEmpty())
        {
            throw new InvalidOperationException();
        }
        for(Product product : products)
        {
            discountedPrice = 0.0;
            discount = 0.0;
            if(discountItems.contains(product.getProductId()))
            {
                discount = product.calculatePrice() * 0.10;
                pw.printf("%d - %.2f\n",product.getProductId(),discount);
            }
        }
        pw.flush();
    }
}

public class ShoppingTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();

        int items = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < items; i++) {
            try {
                cart.addItem(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Integer> discountItems = new ArrayList<>();
        int discountItemsCount = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < discountItemsCount; i++) {
            discountItems.add(Integer.parseInt(sc.nextLine()));
        }

        int testCase = Integer.parseInt(sc.nextLine());
        if (testCase == 1) {
            cart.printShoppingCart(System.out);
        } else if (testCase == 2) {
            try {
                cart.blackFridayOffer(discountItems, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}
