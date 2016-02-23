import java.util.Arrays;

public class Heap<E extends Comparable<E>>{
    
    private E[] arr;
    
    private int size = 0;
    
    public Heap(){
        arr = (E[]) new Comparable[8];
    }
    
    
    public E findMin(){
        //is known that the min element in a heap is the root.
        return arr[0];
    }

    public E removeMin(){
        //is known that the min element in a heap is the root
        int minix = 0;
        //storing value in a variable
        E min = arr[minix];
        //checking if there are elements in the heap
        if (!(arr[0] == null)) {
            //shifting the root(min) with the last element
            E temp = arr[size - 1];
            arr[minix] = arr[size - 1];
            //deleting the min element
            arr[size - 1] = null;
            //decreasing the value for the quantity of elements in the heap
            size--;

            //is known that the child of the root is at the indexes 1 and 2
            int childLx = 1;
            int childRx = 2;
            //variable for the smallest child index
            int minChild = 0;
            //if the children are not null
            if (!(arr[childLx] == null) && !(arr[childRx] == null)) {
                //checking which children is the smallest and assign index to the variable
                if (arr[childLx].compareTo(arr[childRx]) > 0) minChild = childRx;
                else if (arr[childLx].compareTo(arr[childRx]) < 0) minChild = childLx;
                else minChild = childLx;
                //checking if there is just the left child
            } else if (arr[childLx] != null && arr[childRx] == null) {
                //assigning its index to the variable
                minChild = childLx;
                //if there is no children then just return the value of the root
            } else return min;
            //loop to downheap elements to satisfy the heap invariant
            while (arr[minChild].compareTo(arr[minix]) < 0) {
                //shifting elements to satisfy heap invariant
                E temp1 = arr[minix];
                arr[minix] = arr[minChild];
                arr[minChild] = temp1;
                //once shifted assign new index location of element
                minix = minChild;

                //finding its new children
                childLx = minix * 2 + 1;
                childRx = minix * 2 + 2;
                //checking if the left child is not out of bounds(being a heap means that the binary tree is complete and if there is nothing in the
                //left child so consequently there will be nothing in the right child)
                if (childLx <= size) {
                    //checking if children are not null
                    if (!(arr[childLx] == null) && !(arr[childRx] == null)) {
                        //checking for the smallest child and assign its index to the variable
                        if (arr[childLx].compareTo(arr[childRx]) > 0) minChild = childRx;
                        else if (arr[childLx].compareTo(arr[childRx]) < 0) minChild = childLx;
                        else minChild = childLx;
                        //if there is just the left child then it is its index we are looking for
                    } else if (arr[childLx] != null && arr[childRx] == null) {
                        minChild = childLx;
                    }
                }
            }
        }
        //returning value of deleted element
            return min;

    }
    
    
    
    public void add(E elem){
        //checking if array is full. If so create an array which has double size and transfer all elements to there
        if (size >= arr.length){
            E[] newArr = (E[]) new Comparable[size*2];
            for (int i = 0;i<arr.length;i++) newArr[i] = arr[i];
            arr = newArr;
        }
        //adding element to the end of the array
        arr[size] = elem;
        //increasing size for the next element
        size++;
        //variable for the parent index
        int parentIndex = 0;
        //variable to keep track of the element's index
        int elemIndex= size -1;
        //if array is not null
        if(size > 0) {
            //finding the index of the parent
            if (((size-1) % 2 == 0)) {
                parentIndex = ((size-1) - 2) / 2;
            } else {
                parentIndex = ((size-1) - 1) / 2;
            }
        }
        //loop to upheap element so it will satisfy the heap invariant
        while((!(parentIndex < 0)) && elem.compareTo(arr[parentIndex]) < 0){
            //shifting elements
            E temp = arr[parentIndex];
            arr[parentIndex] = arr[elemIndex];
            arr[elemIndex] = temp;
            //storing the new index of the element
            elemIndex = parentIndex;
            //checking for new parents
            if((elemIndex%2==0)){
                parentIndex = ((elemIndex) - 2) / 2;
            }else{
                parentIndex = ((elemIndex) - 1) / 2;
            }
        }


    }
    
    public int size(){
        return size;
    }
    
    public boolean isEmpty(){
        return size <= 0;
    }

    public String toString(){
        return Arrays.toString(arr);
    }
    
}