package OnlinePayments;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OnlinePaymentsTest {
    public static void main(String[] args) {
        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);

        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> {

            onlinePayments.printStudentReport(id, System.out);

        });
    }
}

class Payment {

    String id;
    String itemName;
    int price;

    public Payment(String id, String itemName, int price) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public int getPrice() {
        return price;
    }


    @Override
    public String toString() {
        return String.format("%s %d\n", itemName, price);
    }


}

class OnlinePayments {
    Map<String, List<Payment>> paymentsById;

    public OnlinePayments() {
        this.paymentsById = new HashMap<>();
    }


    void readItems(InputStream is) {

        Scanner scanner = new Scanner(is);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] parts = line.split(";");
            String id = parts[0];
            String item = parts[1];
            int price = Integer.parseInt(parts[2]);

            Payment payment = new Payment(id, item, price);

            paymentsById.putIfAbsent(id, new ArrayList<>());
            paymentsById.computeIfPresent(id, (k, v) -> {
                v.add(payment);
                return v;
            });
        }
    }


    void printStudentReport(String index, OutputStream os) {
        PrintWriter pw = new PrintWriter(os);

        if (!paymentsById.containsKey(index))
            pw.printf("Student %s not found!\n", index);
        else {
            int sum = paymentsById.get(index)
                    .stream().mapToInt(p -> p.price).sum();


            int fee = (int) Math.round(sum * 0.0114);

            if (fee < 3)
                fee = 3;
            else if (fee > 300)
                fee = 300;

            pw.printf("Student: %s Net: %d Fee: %d Total: %d\nItems:\n", index, sum, fee, sum + fee);


            Comparator<Payment> comparator = Comparator.comparing(Payment::getPrice).reversed();
            List<Payment> payments = paymentsById.get(index).stream()
                    .sorted(comparator).collect(Collectors.toList());

            for (int i = 0; i < payments.size(); i++) {
                pw.printf("%d. %s", i + 1, payments.get(i).toString());
            }

        }


        pw.flush();

    }

}
