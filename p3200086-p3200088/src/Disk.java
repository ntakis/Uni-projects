public class Disk implements Comparable<Disk>{
	private static int Capacity= 1000000;
	private int Remaining = 1000000;
	private String id = "Disk ";
	private MaxPQ<Integer> folders = new MaxPQ<>(new IntegerComparator());
	
	public Disk(){
		
	}
	public int getFreeSpace(){
		return Remaining;
	}
			
	public void addFolder(int newFolder){
		this.folders.insert(newFolder);
		Remaining-=newFolder;
	}
	
	public int removeFolder(){
		Remaining+=this.folders.peek();
		return this.folders.getMax();
	}
	
	public int checkFolder(){
		return this.folders.peek();
	}
	
	@Override
	public int compareTo(Disk B) {
    return Integer.compare(this.getFreeSpace(), B.getFreeSpace());
	}
	public void setId(int idCount){
		id= id+idCount;
	}
	
	public String getId(){
	return id;
	}
	
	public void printDisk(){
		int size= folders.getSize();
		System.out.print(this.getId() + " " + this.getFreeSpace()+": ");
		for (int i=0; i<size; i++){
			System.out.print( folders.getMax()+ " ");
		}
		System.out.println("");
	}
}