import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class Greedy {
	
	
	public static void fetchdata(String filename, ListInterface<Integer> folderList) throws FileNotFoundException, EmptyListException{
		int currentfile;
		File myObj = new File(filename);
		Scanner myReader = new Scanner(myObj);
		while (myReader.hasNextLine()) {
			String data = myReader.nextLine();
			currentfile = Integer.parseInt(data);
			if (currentfile<0 || currentfile>1000000){
				throw new ArithmeticException("File size cannot be properly processed.");
			}
			else {
				folderList.insertAtBack(currentfile);
			}
		}
	}
	
	public static void printOutput(String filename, float sum, Disk[] arr){
		sum=(float)sum/1000000;
		int size=arr.length;
		System.out.println("\n% java Greedy " + filename);
		System.out.println("Sum of all folders: " +sum+ " TB.");
		System.out.println("Total number of disks used: " + size);
		for (int i=0; i<size; i++){
			arr[i].printDisk();
		}
		
	}

	public static float fitIntoDisks(float sum, int id, MaxPQ<Disk> DL, ListInterface<Integer> folderList)
	throws FileNotFoundException, EmptyListException {
		Disk diskCheck=null;
		int counter=0;
		while (!folderList.isEmpty()){
			if (counter==0){
				Disk disk=new Disk();
				disk.setId(id);
				id++;
				int folderElement=folderList.removeFromFront();
				disk.addFolder(folderElement);
				counter++;
				DL.insert(disk);
				sum+=folderElement;
			}
			else{
				int folderElement=folderList.removeFromFront();
				diskCheck=DL.peek();
				if (diskCheck.getFreeSpace()>=folderElement){
					Disk temp= DL.getMax();
					temp.addFolder(folderElement);
					DL.insert(temp);
				}
				else{
					Disk disk=new Disk();
					disk.setId(id);
					id++;
					disk.addFolder(folderElement);
					DL.insert(disk);
				}
				sum+=folderElement;
			}
				
		}
		return sum;
	}
		
	
	
	public static void main(String args[]) throws FileNotFoundException, EmptyListException{
		int id=0;
		float sum=0.00F;
		
		MaxPQ<Disk> DiskList = new MaxPQ<>(new DiskComparator());
		ListInterface<Integer> folderList = new List<>();
		fetchdata(args[0], folderList);
		sum=fitIntoDisks(sum, id, DiskList, folderList);
		int listsize=DiskList.getSize();
		Disk diskArr[]= new Disk[listsize];
		for (int i=0; i<listsize; i++){
			diskArr[i]=DiskList.getMax();
		}
		printOutput(args[0],sum,diskArr);
	}
}