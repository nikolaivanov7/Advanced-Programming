package ShapesApplication1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Shape
{
    private String id;
    private List<Integer> sizes;

    public Shape() {
        id = "empty";
        sizes = new ArrayList<>();
    }

    public Shape(String idCopy, List<Integer> sizesCopy) {
        this.id = idCopy;
        this.sizes = new ArrayList<>();
        for(int i = 0; i < sizesCopy.size(); i++)
        {
            sizes.add(sizesCopy.get(i));
        }
    }

    public static Shape createShape(String line)
    {
        String [] parts = line.split("\\s+");
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 1; i < parts.length; i++)
        {
            list.add(Integer.parseInt(parts[i]));
        }
        return new Shape(parts[0],list);
    }

    public int getSize()
    {
        return sizes.size();
    }

    @Override
    public String toString() {
        return String.format("%s %d %d",id,getSize(),getPerimeter());
    }

    public int getPerimeter()
    {
        int suma = 0;
        for(int i = 0; i < sizes.size(); i++)
        {
            suma += sizes.get(i);
        }
        return suma * 4;
    }
}

class ShapesApplication
{
    List<Shape> shapes;

    public ShapesApplication() {
        shapes = new ArrayList<>();
    }

    public int readCanvases(InputStream inputStream)
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        shapes = br.lines()
                .map(Shape::createShape)
                .collect(Collectors.toList());
        int sum = 0;
        for(int i = 0; i < shapes.size(); i++)
        {
            sum += shapes.get(i).getSize();
        }
        return sum;
    }

    void printLargestCanvasTo(OutputStream outputStream)
    {
        PrintWriter pw = new PrintWriter(outputStream);
        Shape max = shapes.stream()
                .max((c1,c2) -> Integer.compare(c1.getPerimeter(),c2.getPerimeter()))
                .orElse(null);
        pw.println(max);
        pw.flush();
    }
}

public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}
