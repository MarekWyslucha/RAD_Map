package model;

import application.MainWindowController;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

public class Mapa {

    private BufferedImage image;
    private final int TOLERANCE = 200;
    Random r;
    List<List<Point>> fields = new ArrayList<>();
    Map<Point, Set<Integer>> borders = new HashMap<>();

    MainWindowController mainWindowController;

    public Mapa(BufferedImage image, MainWindowController windowController) {
        this.image = image;
        r = new Random();
        this.mainWindowController = windowController;
    }

    public BufferedImage getColoredImage() {
        sharpMap();
        while (findWhiteField()) {
        }
        makeRelation();
        return image;
    }

    private void makeRelation() {
        List<Set<Integer>> relation = new ArrayList<>();
        List<Integer> relationSize = new ArrayList<>();
        for (int i = 0; i < fields.size(); i++) {
            Set<Integer> temp = new HashSet<Integer>();
            for (Set<Integer> neigbours : borders.values()) {
                if (neigbours.contains(relation.size())) {
                    temp.addAll(neigbours);
                }
            }
            temp.remove(relation.size());
            ArrayList<Integer> arrayList = new ArrayList<Integer>(temp);
            Collections.sort(arrayList);
            relation.add(temp);
            relationSize.add(temp.size());
        }

        Set<Integer> sortedValue = new HashSet<>(relationSize);
        relationSize = new ArrayList<>(sortedValue);
        Collections.sort(relationSize);
        Collections.reverse(relationSize);

        for (Integer size : relationSize) {
            for (int i = 0; i < relation.size(); i++) {
                if (relation.get(i).size() == size) {
                    colorField(i, pickColorForField(relation.get(i)));
                }
            }
        }
    }

    private Color pickColorForField(Set<Integer> relation) {
        List<Color> neighboursColor = new ArrayList<Color>();
        for (Integer index : relation) {
            Point firstPoint = fields.get(index).get(0);
            neighboursColor.add(new Color(image.getRGB(firstPoint.x, firstPoint.y)));
        }

        if (!neighboursColor.contains(Color.BLUE)) {
            return Color.BLUE;
        } else if (!neighboursColor.contains(Color.GREEN)) {
            return Color.GREEN;
        } else if (!neighboursColor.contains(Color.YELLOW)) {
            return Color.YELLOW;
        } else if (!neighboursColor.contains(Color.RED)) {
            return Color.RED;
        } else {
            return Color.PINK;
        }
    }

    private void colorField(int fieldNumber, Color color) {
        
        double i = 0, size = (double)fields.get(fieldNumber).size();
        
        for (Point point : fields.get(fieldNumber)) {
            i++;
            image.setRGB(point.x, point.y, color.getRGB());
        }
    }

    private void sharpMap() {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color pixel = new Color(image.getRGB(x, y));
                if (pixel.getBlue() > TOLERANCE && pixel.getGreen() > TOLERANCE && pixel.getRed() > TOLERANCE) {
                    image.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    image.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
    }

    private boolean findWhiteField() {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (isWhite(x, y, false)) {
                    Point white = new Point(x, y);
                    findNeighbourWhitePixel(white, setColor());
                    return true;
                }
            }
        }
        return false;
    }

    private Color setColor() {
        Color color = new Color(r.nextInt(TOLERANCE), r.nextInt(TOLERANCE), r.nextInt(TOLERANCE));
        return color;
    }

    private void findNeighbourWhitePixel(Point pixel, Color color) {
        Stack<Point> stack = new Stack<>();
        stack.push(pixel);
        Set<Point> singleField = new HashSet<Point>();

        while (!stack.isEmpty()) {
            Point point = stack.pop();
            singleField.add(point);
            image.setRGB(point.x, point.y, Color.GRAY.getRGB());
            checkAllNeigbours(point, stack);
        }
        fields.add(new ArrayList<Point>(singleField));
    }

    private void checkAllNeigbours(Point point, Stack<Point> stack) {
        Point right = new Point(point.x + 1, point.y);
        if (right.x < image.getWidth() && isWhite(right.x, right.y, true)) {
            stack.push(right);
        }

        Point left = new Point(point.x - 1, point.y);
        if (left.x >= 0 && isWhite(left.x, left.y, true)) {
            stack.push(left);
        }

        Point down = new Point(point.x, point.y + 1);
        if (down.y < image.getHeight() && isWhite(down.x, down.y, true)) {
            stack.push(down);
        }

        Point up = new Point(point.x, point.y - 1);
        if (up.y >= 0 && isWhite(up.x, up.y, true)) {
            stack.push(up);
        }
    }

    private boolean isWhite(int x, int y, boolean checkBlack) {
        Color pixel = new Color(image.getRGB(x, y));
        if (pixel.getRGB() == Color.WHITE.getRGB()) {
            return true;
        } else if (checkBlack) {
            Point point = new Point(x, y);
            if (borders.containsKey(point)) {
                borders.get(point).add(fields.size());
            } else {
                Set<Integer> neighbours = new HashSet<Integer>();
                neighbours.add(fields.size());
                borders.put(point, neighbours);
            }
        }
        return false;
    }

}
