package FileSystem;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Partial exam II 2016/2017
 */
public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}

// Your code here
class File implements Comparable<File>
{
    String name;
    int size;
    LocalDateTime dateTime;

    public File(String name, int size, LocalDateTime dateTime) {
        this.name = name;
        this.size = size;
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public LocalDateTime getDateOfCreation() {
        return dateTime;
    }

    @Override
    public String toString() {
        //%-10[name] %5[size]B %[createdAt]
        return String.format("%-10s %5dB %s", name, size, dateTime);
    }

    public String printWithMonthAndDay(){
        return String.format("%s-%s",dateTime.getMonth(),dateTime.getDayOfMonth());
    }

    @Override
    public int compareTo(File o) {
        int x = this.dateTime.compareTo(o.dateTime);
        if (x == 0){
            int d = this.name.compareTo(o.name);
            if (d == 0){
                return Integer.compare(this.size, o.size);
            }
            return d;
        }
        return x;
    }
}

class FileSystem
{
    Map<String,TreeSet<File>> filesByName;
    Map<Integer,Set<File>> filesByYear;
    List<File> allFiles;

    public FileSystem()
    {
        filesByName = new TreeMap<>();
        filesByYear = new TreeMap<>();
        allFiles = new ArrayList<>();
    }

    public void addFile(char charAt, String name, int size, LocalDateTime createdAt) {
        File f = new File(name,size,createdAt);
        filesByName.putIfAbsent(String.valueOf(charAt),new TreeSet<>());
        filesByName.get(String.valueOf(charAt)).add(f);
        int year = createdAt.getYear();
        filesByYear.putIfAbsent(year,new TreeSet<>());
        filesByYear.get(year).add(f);
        allFiles.add(f);
    }

    public List<File> findAllHiddenFilesWithSizeLessThen(int size) {
        List<File> hiddenFiles = new ArrayList<>();
        if(filesByName.get(".") != null)
        {
            filesByName.get(".").stream().filter(file -> {
                return file.size < size;
            }).forEach(file -> {
                hiddenFiles.add(file);
            });
        }
        filesByName.values().stream().forEach(files -> {
            files.stream().filter(file -> file.name.charAt(0) == '.').forEach(file -> {
                if(!hiddenFiles.contains(file))
                    hiddenFiles.add(file);
            });
        });
        return hiddenFiles;
    }
    public int totalSizeOfFilesFromFolders(List<Character> folders) {
        int size = 0;
        for (Character c: folders) {
            size += filesByName.get(String.valueOf(c))
                    .stream()
                    .mapToInt(file -> file.size)
                    .sum();
        }
        return size;
    }

    public Map<Integer, Set<File>> byYear() {
        return filesByYear;
    }

    public Map<String, Long> sizeByMonthAndDay() {
        return allFiles.stream()
                .collect(Collectors.groupingBy(
                        file -> file.printWithMonthAndDay(),
                        TreeMap::new,
                        Collectors.summingLong(File::getSize)
                ));
    }
}
