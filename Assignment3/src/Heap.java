import java.util.Arrays;

public class Heap<E extends Comparable<E>>{
    
    private E[] arr;
    
    private int size = 0;
    
    public Heap(){
        arr = (E[]) new Comparable[8];
    }
    
    
    public E findMin(){
        // This works but is way too slow!
        E min = null;
        for(E e : arr)
            if (min == null || e.compareTo(min)<0)
                min = e;
        
        return min;        
    }
    
    public E removeMin(){
        // This works but is way too slow!
        int minix = 0;
//        for(int i = 1;i<size;i++)
//            if (arr[i].compareTo(arr[minix]) < 0)
//                minix = i;
        
        E min = arr[minix];
        
//        int i;
//        for(i = minix;i<size-1;i++)
//            arr[i]=arr[i+1];
//        arr[size-1]=null;

        E temp = arr[size];
        arr[minix] = arr[size];
        arr[size] = null;
        size--;


        int childLx = 0 * 2 + 1;
        int childRx = 0 * 2 + 2;
        int minChild = 0;
        if(arr[childLx].compareTo(arr[childRx]) > 0) minChild = childRx;
        else if(arr[childLx].compareTo(arr[childRx]) < 0) minChild = childLx;
        else minChild = childLx;

        while(arr[minChild].compareTo(arr[minix]) < 0){
            E temp1 = arr[minix];
            arr[minix] = arr[minChild];
            arr[minChild] = temp1;
            minix = minChild;

            childLx = minix * 2 + 1;
            childRx = minix * 2 + 2;

            if(arr[childLx].compareTo(arr[childRx]) > 0) minChild = childRx;
            else if(arr[childLx].compareTo(arr[childRx]) < 0) minChild = childLx;
            else minChild = childLx;
        }
        return min;   
        
    }
    
    
    
    public void add(E elem){
        if (size >= arr.length){
            E[] newArr = (E[]) new Comparable[size*2];
            for (int i = 0;i<arr.length;i++) newArr[i] = arr[i];
            arr = newArr;
        }

        arr[size] = elem;
        size++;
        int parentIndex = 0;
        int currentIndex = 0;
        int elemIndex= size -1;
        // Something more should probably be done here?
        if(size > 0) {
            if (((size-1) % 2 == 0)) {
                parentIndex = ((size-1) - 2) / 2;
            } else {
                parentIndex = ((size-1) - 1) / 2;
            }

        }
        while((!(parentIndex < 0)) && elem.compareTo(arr[parentIndex]) < 0){
            E temp = arr[parentIndex];
            arr[parentIndex] = arr[elemIndex];
            arr[elemIndex] = temp;
            elemIndex = parentIndex;



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