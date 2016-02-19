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
        for(int i = 1;i<size;i++)
            if (arr[i].compareTo(arr[minix]) < 0)
                minix = i;
        
        E min = arr[minix];
        
        int i;
        for(i = minix;i<size-1;i++)
            arr[i]=arr[i+1];
        arr[size-1]=null;
        
        size--;
        
        return min;   
        
    }
    
    
    
    public void add(E elem){
        if (size >= arr.length){
            E[] newArr = (E[]) new Comparable[size*2];
            for (int i = 0;i<arr.length;i++) newArr[i] = arr[i];
            arr = newArr;
        }

        arr[size] = elem;
        
        // Something more should probably be done here?
        
        size++;
    }
    
    public int size(){
        return size;
    }
    
    public boolean isEmpty(){
        return size <= 0;
    }
    
}