/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package COP3530.Project3;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Mr.Waddle
 */
public class MergeSort {
    private List<Book> input;
    private String type;
    private boolean ascending;
    
    public MergeSort(List<Book> input, String type, boolean ascending){
        this.input = input;
        this.type = type;
        this.ascending = ascending;
    }
    
    boolean compare(Book left, Book right) {
        if (type.equals("title") && ascending)
            return (left.getTitle().compareToIgnoreCase(right.getTitle()) <= 0);
        if (type.equals("title") && !ascending)
            return (left.getTitle().compareToIgnoreCase(right.getTitle()) >= 0);
        if (type.equals("desc") && ascending)
            return (left.getDesc().compareToIgnoreCase(right.getDesc()) <= 0);
        if (type.equals("desc") && !ascending)
            return (left.getDesc().compareToIgnoreCase(right.getDesc()) >= 0);
        if (type.equals("ratings") && ascending)
            return (left.getRatingsCount() <= right.getRatingsCount());
        if (type.equals("ratings") && !ascending)
            return (left.getRatingsCount() >= right.getRatingsCount());
        
        return false;
    }
    
    void merge(int start, int mid, int end)
    {
        List<Book> merged = new ArrayList<>();
        
        // Find sizes of two subarrays to be merged
        int left = start;
        int right = mid + 1;
 
        while (left <= mid && right <= end){
            if (compare(input.get(left), input.get(right))){
                merged.add(input.get(left));
                left++;
            }
            else
            {
                merged.add(input.get(right));
                right++;
            }
        }       
        
        // Either of below while loop will execute
        while (left <= mid){
            merged.add(input.get(left));
            left++;
        }
        
        while (right <= end){
            merged.add(input.get(right));
            right++;
        }
        
        int i = 0;
        int j = start;
        //Setting sorted array to original one
        while(i < merged.size()){
            input.set(j, merged.get(i++));
            j++;
        }
    }
 
    // Main function call
    public void sort(int start, int end)
    {
        if (start < end && (end - start) >= 1) {
            // Get midpoint of array
            int mid = (start + end)/ 2;
           
            // Sort each half
            sort(start, mid);
            sort(mid + 1, end);
 
            // Merge halves
            merge(start, mid, end);
        }
    }
    
    public List<Book> getSorted() {
        return input;
    }
}
