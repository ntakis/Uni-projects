import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files


public class Sort{
	
	public static void intquicksort(int[] arr, int low, int high){
		if(low < high){
			int p = intpartition(arr, low, high);
			intquicksort(arr, low, p-1);
			intquicksort(arr, p+1, high);
		}
	}

	static void intswap(int[] arr, int low, int pivot){
		int tmp = arr[low];
		arr[low] = arr[pivot];
		arr[pivot] = tmp;
	}
	
	static int intpartition(int[] arr, int low, int high){
		int p = low, j;
		for(j=low+1; j <= high; j++){
			if(arr[j] > arr[low]){
			intswap(arr, ++p, j);}
		}

		intswap(arr, low, p);
		return p;
	}
	
	public static void main(String args[]) throws FileNotFoundException, EmptyListException{
		int id=0;
		float sum=0.00F;
		MaxPQ<Disk> DiskList = new MaxPQ<>(new DiskComparator());
		ListInterface<Integer> folderList = new List<>();
		Greedy.fetchdata(args[0], folderList);
		int size=folderList.getSize();
		int[] array= new int[size];
		for (int i=0; i<size; i++){
			array[i]=folderList.removeFromFront();
		}
		intquicksort(array,0,size-1);
		for (int i=0; i<size; i++){
			folderList.insertAtBack(array[i]);
		}
		sum=Greedy.fitIntoDisks(sum, id, DiskList, folderList);
		int listsize=DiskList.getSize();
		Disk diskArr[]= new Disk[listsize];
		for (int i=0; i<listsize; i++){
			diskArr[i]=DiskList.getMax();
		}
		Greedy.printOutput(args[0],sum,diskArr);
	}
}