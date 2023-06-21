package Discount;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Discounts
 */
public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

class Product {
    int cena_popust;
    int cena;

    public Product (int cena_popust, int cena) {
        this.cena_popust = cena_popust;
        this.cena = cena;
    }

    public Product() {
    }

    public Product createProduct(String line){
        String[] parts = line.split(":");
        int cenaP = Integer.parseInt(parts[0]);
        int cena = Integer.parseInt(parts[1]);
        return new Product (cenaP, cena);
    }

    @Override
    public String toString() {
        return String.format("%d%% %d/%d",average(), cena_popust,cena);
    }

    public int average(){
        return (int) ((1 - ((float)cena_popust / cena))*100);
    }

    public int getCena_popust() {
        return cena_popust;
    }

    public int getCena() {
        return cena;
    }
}

// Vashiot kod ovde
class Store implements Comparator<Store> {
    String name;
    List<Product> products;

    public Store() {
    }

    public Store(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    public Store createStore(String line){
        String[] parts = line.split("\\s+");
        String name = parts[0];
        List<Product> p = new ArrayList<>();
        for (int i = 1; i < parts.length; i++){
            Product pr = new Product();
            pr = pr.createProduct(parts[i]);
            p.add(pr);
        }
        return new Store(name, p);
    }

    public double averageByStore(){
        double sum = products.stream().mapToDouble(product -> {
            return product.average();
        }).sum();
        return sum / products.size();
    }

    public int discountByStore(){
        int sum = products.stream().mapToInt(product -> {
            return product.cena - product.cena_popust;
        }).sum();
        return sum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name+"\n");
        String av = String.format("Average discount: %.1f%%", averageByStore());
        sb.append(av+"\n");
        sb.append(String.format("Total discount %d\n", discountByStore()));
        Comparator comparator1 = Comparator.comparing(Product::average).thenComparing(Product::getCena_popust);
        products = (List<Product>) products.stream().sorted(comparator1.reversed()).collect(Collectors.toList());
        IntStream.range(0, products.size()).forEach(i -> {
            if (i == products.size()-1){
                sb.append(products.get(i));
            }else {
                sb.append(products.get(i)+"\n");
            }
        });

        return sb.toString();
    }

    @Override
    public int compare(Store o1, Store o2) {
        return o1.name.compareTo(o2.name);
    }
}

class Discounts{
    TreeMap<String, Store> stores;

    public Discounts(TreeMap<String, Store> stores) {
        this.stores = stores;
    }

    public Discounts() {
    }

    public int readStores(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        TreeMap<String,Store> s = new TreeMap<>();
        br.lines().forEach(line -> {
            Store st = new Store();
            st = st.createStore(line);
            s.put(st.name, st);
        });
        this.stores = s;
        return stores.size();
    }

    public List<Store> byAverageDiscount() {
        Comparator comparator = Comparator.comparing(Store::averageByStore);
        return (List<Store>) stores.values().stream().sorted(comparator.reversed()).limit(3).collect(Collectors.toList());
    }

    public List<Store> byTotalDiscount() {
        Comparator comparator = Comparator.comparing(Store::discountByStore);
        return (List<Store>) stores.values().stream().sorted(comparator).limit(3).collect(Collectors.toList());
    }
}