abstract class Shape{
        String name;
        Shape(String newName){
                name = newName;
        }
        void print(){
                System.out.print(name);
                System.out.print("(");
                dimensions();
                System.out.print(") : ");
                System.out.println(Double.toString(area()));

        }
        void dimensions(){
                System.out.print("Shape dimensions");
        }
        abstract void draw();
        double area(){
                return 0.0;
        }
}
class Circle extends Shape{
    int radius;
    static final double PI = 3.14;
    Circle(String newName, int newRadius){
            super(newName);
            radius = newRadius;
    }
    void dimensions(){
            System.out.print(String.valueOf(radius));
    }
    void draw(){
            System.out.println("   ****  ");
            System.out.println(" *      *");
            System.out.println("*        *");
            System.out.println("*        *");
            System.out.println(" *      *");
            System.out.println("   ****  ");
    }
    double area(){
            return radius*radius*PI;
    }
}
class Square extends Shape{
    int side;
    Square(String newName, int newSide){
            super(newName);
            side=newSide;
    }
    void dimensions(){
            System.out.print(String.valueOf(side));
    }
    void draw(){
            System.out.println("*******");
            System.out.println("*     *");
            System.out.println("*     *");
            System.out.println("*     *");
            System.out.println("*******");
    }
    double area(){
            return side*side;
    }
}
class Triangle extends Shape{
    int base;
    int height;
    Triangle(String newName, int newBase, int newHeight){
            super(newName);
            base = newBase;
            height = newHeight;
    }
    void dimensions(){
            System.out.print(String.valueOf(base) + ", " + String.valueOf(height));
    }
    void draw(){
            System.out.println("   *   ");
            System.out.println("  * *  ");
            System.out.println(" *   * ");
            System.out.println("*******");
    }
    double area(){
            return (base*height)/2.0;
    }
}
class Rectangle extends Square{
    int width;
    Rectangle(String newName, int newSide, int newWidth){
          super(newName, newSide);
            width = newWidth;
    }
    void dimensions(){
            super.dimensions();
            System.out.print(", " + String.valueOf(width));
    }
    void draw(){
            System.out.println("****");
            System.out.println("*  *");
            System.out.println("*  *");
            System.out.println("*  *");
            System.out.println("****");
    }
    double area(){
            return side*width;
    }
}
class ListNode{
    Shape s;
    ListNode next;
    ListNode(Shape s, ListNode next){
            this.s = s;
            this.next = next;
    }
}
class Picture{
    ListNode head;
    Picture(){
            this.head = null;
    }
    void add(Shape sh){
            //insert shape at the front of the linked list
            ListNode tmphead = new ListNode(sh, this.head);
            this.head = tmphead;
    }
    void printAll(){
            ListNode tmphead = this.head;
            while(tmphead!=null){
                    tmphead.s.print();
                    tmphead = tmphead.next;
            }
    }
    void drawAll(){
            ListNode tmphead = this.head;
            while(tmphead!=null){
                    tmphead.s.draw();
                    tmphead = tmphead.next;
            }
    }
    double totalArea(){
            ListNode tmphead = this.head;
            double total = 0.0;
            while(tmphead!=null){
                    total += tmphead.s.area();
                    tmphead = tmphead.next;
            }
            return total;
    }
}
public class mainClass{
    public static void main(String[] args){
            int firstArg, secondArg;
            if(args.length>=2){
                    firstArg = Integer.valueOf(args[0]);
                    secondArg = Integer.valueOf(args[1]);
            }else{
                    System.out.println("Arguments should be 2");
                    return;
            }
            Picture picture=new Picture();

            picture.add(new Triangle("FirstTriangle", firstArg, secondArg));
            picture.add(new Triangle("SecondTriangle", firstArg-1, secondArg-1));
            picture.add(new Circle("FirstCircle", firstArg));
            picture.add(new Circle("SecondCircle", firstArg-1));
            picture.add(new Square("FirstSquare", firstArg));
            picture.add(new Square("SecondSquare", firstArg-1));
            picture.add(new Rectangle("FirstRectangle", firstArg, secondArg));
            picture.add(new Rectangle("SecondRectangle", firstArg-1, secondArg-1));
            picture.printAll();
            picture.drawAll();
            System.out.println("Total: " + Double.toString(picture.totalArea()));
    }
}

