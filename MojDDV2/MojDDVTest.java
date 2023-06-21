package MojDDV2;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);
    }
}

class AmountNotAllowedException extends Exception {
    public AmountNotAllowedException(String message) {
        super(message);
    }
}

class Product {
    int price;
    char type;

    public Product(int price, char type) {
        this.price = price;
        this.type = type;
    }

    public double tax() {
        if (type == 'A')
            return price * 0.18 * 0.15;
        else if (type == 'B')
            return price * 0.05 * 0.15;
        else return 0;
    }

    public int getPrice() {
        return price;
    }

    public char getType() {
        return type;
    }
}

class Receipt {
    String id;
    List<Product> products;

    public Receipt(String id, List<Product> products) throws AmountNotAllowedException {
        this.id = id;
        this.products = products;
        if (sum() > 30000) {
            throw new AmountNotAllowedException("Receipt with amount " + sum() + " is not allowed to be scanned");
        }
    }

    public int sum() {
        return products.stream().mapToInt(Product::getPrice).sum();
    }

    public double taxReturn() {
        return products.stream().mapToDouble(Product::tax).sum();
    }

    @Override
    public String toString() {
        return String.format("%10s\t%10d\t%10.5f", id, sum(), taxReturn());
    }
}


class MojDDV {
    List<Receipt> receipts;

    public MojDDV() {
        receipts = new ArrayList<>();
    }


    public void readRecords(InputStream in) {
        Scanner sc = new Scanner(in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            String id = parts[0];
            List<Product> productsList = new ArrayList<>();
            for (int i = 1; i < parts.length; i += 2) {
                int price = Integer.parseInt(parts[i]);
                char type = parts[i + 1].charAt(0);
                productsList.add(new Product(price, type));
            }
            try {
                Receipt r = new Receipt(id, productsList);
                receipts.add(r);
            } catch (AmountNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void printTaxReturns(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);
        for (Receipt receipt : receipts) {
            pw.println(receipt);
        }
        pw.flush();
    }

    public void printStatistics(OutputStream out) {
        PrintWriter pw = new PrintWriter(out);
        DoubleSummaryStatistics ds = receipts
                .stream()
                .mapToDouble(Receipt::taxReturn)
                .summaryStatistics();
        pw.printf("min:\t%.3f\n", ds.getMin());
        pw.printf("max:\t%.3f\n", ds.getMax());
        pw.printf("sum:\t%.3f\n", ds.getSum());
        pw.printf("count:\t%d\n", ds.getCount());
        pw.printf("avg:\t%.3f\n", ds.getAverage());
        pw.flush();
    }
}

