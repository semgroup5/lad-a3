/**
 * Created by Raphael on 22/02/2016.
 */
public class Test {

    public static void main(String[] args){
    Heap test = new Heap();
        test.add(2);
        test.add(6);
      test.add(4);
        test.add(1);
       test.add(7);
        test.add(5);
        test.add(3);

        test.removeMin();
        test.removeMin();
        test.removeMin();
        test.removeMin();
        test.removeMin();
        test.removeMin();
        test.removeMin();



//        test.removeMin();

        System.out.println(test.toString());
    }
}
