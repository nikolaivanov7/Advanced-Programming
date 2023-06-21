package PhoneBook;

import java.util.*;

class DuplicateNumberException extends Exception
{
    public DuplicateNumberException(String message) {
        super(String.format("Duplicate number: [%s}",message));
    }
}

class Contact implements Comparable<Contact>
{
    String name;
    String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return String.format("%s %s",name,number);
    }

    @Override
    public int compareTo(Contact o) {
        int comparison = this.name.compareTo(o.getName());
        if(comparison != 0)
        {
            return comparison;
        }
        return number.compareTo(o.getNumber());
    }
}

class PhoneBook
{
    Map<String,Contact> contactByNumber;
    Map<String,Set<Contact>> contactsByName;

    public PhoneBook ()
    {
        contactByNumber = new HashMap<>();
        contactsByName = new TreeMap<>();
    }

    public void addContact(String name,String number) throws DuplicateNumberException {
        if(contactByNumber.containsKey(number))
        {
            throw new DuplicateNumberException(number);
        }
        Contact contact = new Contact(name,number);
        contactByNumber.put(number,contact);
        contactsByName.putIfAbsent(name,new TreeSet<>());
        contactsByName.computeIfPresent(name, (k,v) -> {
            v.add(contact);
            return v;
        });
    }

    public void contactsByNumber(String number)
    {
        if(!noContactWithNumber(number))
        {
            System.out.println("NOT FOUND");
            return;
        }
        contactByNumber
                .entrySet()
                .stream()
                .filter(each -> each.getKey().contains(number))
                .map(each -> each.getValue())
                .sorted(Contact::compareTo)
                .forEach(System.out::println);
    }
    private boolean noContactWithNumber(String number)
    {
        return !contactByNumber
                .keySet()
                .stream()
                .filter(n->n.contains(number))
                .findFirst().orElse("").equals("");
    }

    public void contactsByName(String name)
    {
        if(!noContactWithName(name)){
            System.out.println("NOT FOUND");
            return;
        }
        contactsByName.get(name)
                .stream()
                .forEach(System.out::println);
    }

    private boolean noContactWithName(String name)
    {
        return contactsByName.containsKey(name);
    }
}

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}

// Вашиот код овде


