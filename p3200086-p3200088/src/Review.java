import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.BufferedWriter;

public class Review{
	
	public static void randomFill(int N, int[] arr, int[] arr1){
		Random random = new Random();
		int num;
		for (int i=0;i<N;i++){
			num= random.nextInt(1000000);
			arr[i]=num;
			arr1[i]=num;
		}
	}
	public static String fileWrite(String algName, int[] fileData, int N,int count) throws IOException {
		
		String name="z"+algName+"_reviewFile_"+N+"-Folders_"+"fileNumber_"+count+".txt";
		File file = new File(name);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw =new BufferedWriter(fw);
		for (int i = 0; i <N; i++) {
			bw.write(Integer.toString(fileData[i]));
			if (!(i==N-1)){
				bw.newLine();
			}
		}
		bw.close();
		return name;
	}
	
	public static int[] startProcessing(int N,int i) throws FileNotFoundException, EmptyListException, IOException{
		int greedySum=0;
		int sortSum=0;
		float greedyTB=0.00F;
		float sortTB=0.00F;
		int[] sortArray= new int[N];
		int[] greedyArray=new int[N];
		MaxPQ<Disk> greedyDiskList = new MaxPQ<>(new DiskComparator());
		ListInterface<Integer> greedyfolderList = new List<>();
		MaxPQ<Disk> sortDiskList = new MaxPQ<>(new DiskComparator());
		ListInterface<Integer> sortfolderList = new List<>();
		randomFill(N,sortArray,greedyArray);
		Sort.intquicksort(sortArray,0,N-1);
		
		String gName= fileWrite("Greedy",greedyArray,N,i);
		String sName= fileWrite("Sort",sortArray,N,i);
		
		Greedy.fetchdata(gName,greedyfolderList);
		Greedy.fetchdata(sName,sortfolderList);

		greedyTB=Greedy.fitIntoDisks(greedyTB, 0, greedyDiskList, greedyfolderList);
		sortTB=Greedy.fitIntoDisks(sortTB, 0, sortDiskList, sortfolderList);
		
		greedySum+=greedyDiskList.getSize();
		sortSum+=sortDiskList.getSize();
		
		int[] arr= new int[2];
		arr[0]=greedySum;
		arr[1]=sortSum;
		return arr;

	}
		
	public static void main(String args[]) throws FileNotFoundException, EmptyListException, IOException{
		int[] Sums= new int[2];
		int [] temp= new int[2];
		float avg1=0.00F;
		float avg2=0.00F;
		
		Sums[0]=0;
		Sums[1]=0;
		for (int i=0;i<10;i++){
			temp=startProcessing(100,i);
			Sums[0]=Sums[0]+temp[0];
			Sums[1]=Sums[1]+temp[1];
		}
		avg1=(float)Sums[0]/10;
		avg2=(float)Sums[1]/10;
		System.out.println("Greedy required "+avg1+ " while Sort required "+avg2+ " disks on 10 files containing 100 folders");
		
		Sums[0]=0;
		Sums[1]=0;
		for (int i=0;i<10;i++){
			temp=startProcessing(500,i);
			Sums[0]=Sums[0]+temp[0];
			Sums[1]=Sums[1]+temp[1];
		}
		avg1=(float)Sums[0]/10;
		avg2=(float)Sums[1]/10;
		System.out.println("Greedy required "+avg1+ " while Sort required "+avg2+ " disks on 10 files containing 500 folders");
		
		Sums[0]=0;
		Sums[1]=0;
		for (int i=0;i<10;i++){
			temp=startProcessing(1000,i);
			Sums[0]=Sums[0]+temp[0];
			Sums[1]=Sums[1]+temp[1];
		}
		avg1=(float)Sums[0]/10;
		avg2=(float)Sums[1]/10;
		System.out.println("Greedy required "+avg1+ " while Sort required "+avg2+ " disks on 10 files containing 1000 folders");
	
	}
	
	
}