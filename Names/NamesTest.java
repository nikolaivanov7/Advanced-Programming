package Names;

import com.sun.source.doctree.SeeTree;

import java.util.*;
import java.util.stream.Collectors;

class Name
{
    String name;
    int count;

    public Name(String name) {
        this.name = name;
        count = 0;
    }

    public int uniqueLetters()
    {
        Set<Character> bukvi = new HashSet<>();
        for(Character c : name.toCharArray())
        {
            bukvi.add(Character.toLowerCase(c));
        }
        return bukvi.size();
    }

    @Override
    public String toString() {
        return String.format("%s (%d) %d",name,count,uniqueLetters());
    }
}

class Names
{
    Map<String,Name> names;

    public Names() {
        names = new TreeMap<>();
    }

    public void addName(String name)
    {
        names.computeIfAbsent(name, key -> new Name(name));
        names.get(name).count += 1;
    }

    public void printN(int n)
    {
        names.values().stream()
                .filter(name -> name.count >= n).forEach(System.out::println);
    }

    public String findName(int len, int x)
    {
        List<String> lista = names
                .keySet()
                .stream()
                .filter(name -> name.length() < len)
                .collect(Collectors.toList());
        return lista.get(x % lista.size());
    }
}

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

// vashiot kod ovde
