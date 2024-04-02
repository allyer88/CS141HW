import java.io.*;
import java.util.Hashtable;

class Disk
    // extends Thread
{
    static final int NUM_SECTORS = 2048;
    StringBuffer sectors[] = new StringBuffer[NUM_SECTORS];
    static final int DISK_DELAY = 1;
    int free_sector;
    Disk()
    {
        //When you initialize an array of object, 
        //memory is allocated for each object reference in the array, but the actual objects themselves 
        //are not created until they are explicitly instantiated using the new keyword.
        for(int i=0;i< NUM_SECTORS;i++){
            sectors[i] = new StringBuffer();
        }
        free_sector=0;
    }
    void write(int sector, StringBuffer data) throws InterruptedException  // call sleep
    {
        Thread.sleep(DISK_DELAY);
        sectors[sector] = data;
        free_sector++;
    }
    void read(int sector, StringBuffer data) throws InterruptedException  // call sleep
    {
        Thread.sleep(DISK_DELAY);
        data.setLength(0); // Clear the buffer before reading
        data.append(sectors[sector]);
    }
}

class Printer
    // extends Thread
{
    static final int PRINT_DELAY = 1;
    String fileName;
    Printer(int id)
    {
        fileName = "PRINTER"+id;
    }
    //This is an exception that sleep throws when another thread 
    //interrupts the current thread while sleep is active.
    void print(StringBuffer data) throws InterruptedException, IOException// call sleep
    {
        Thread.sleep(PRINT_DELAY);
        FileWriter myWriter = new FileWriter(fileName, true);
        //System.out.print("print this line to file(");
        //System.out.print(fileName);
        //System.out.print("): ");
        //System.out.println(data.toString());
        myWriter.write(data.toString());
        myWriter.write("\n");
        myWriter.close();
    }
}

class PrintJobThread
    extends Thread
{
    StringBuffer line = new StringBuffer(); // only allowed one line to reuse for read from disk and print to printer
    StringBuffer printFileName;
    PrintJobThread(StringBuffer fileToPrint)
    {
        printFileName = fileToPrint;
    }

    public void run()
    {
	try{
        FileInfo f = MainClass.diskManager.directoryManager.lookup(printFileName);
        if(f==null){
            System.out.println("No such file name exist");
            return;
        }
        int diskNumber = f.diskNumber;
        int startingSector = f.startingSector;
        int fileLength=f.fileLength;
        int printerNum = MainClass.printerManager.request();
        for(int i=0;i<fileLength;i++){
            MainClass.disks[diskNumber].read(startingSector+i, line);
            MainClass.printers[printerNum].print(line);
            //System.out.print("printing: ");
            //System.out.print(line.toString());
            //System.out.println("successfully");
        }
        MainClass.printerManager.release(printerNum);
	}catch(Exception e){
		e.printStackTrace();
	}
    }
}

//use in DirectoryManager and PrintJobThread
class FileInfo
{
    int diskNumber;
    int startingSector;
    int fileLength;
    FileInfo(int newdiskNumber, int newstartingSector, int newfileLength){
        diskNumber = newdiskNumber;
        startingSector = newstartingSector;
        fileLength = newfileLength;
    }
}

class DirectoryManager
{
    private Hashtable<String, FileInfo> T = new Hashtable<String, FileInfo>();

    DirectoryManager()
    {
    }

    void enter(StringBuffer fileName, FileInfo file)
    {
        T.put(fileName.toString(), file);
    }

    FileInfo lookup(StringBuffer fileName)
    {
        return T.get(fileName.toString());
    }
}
//Given Code
class ResourceManager {
    boolean[] isFree;

    ResourceManager(int numberOfItems) {
        isFree = new boolean[numberOfItems];
        for (int i = 0; i < isFree.length; ++i)
            isFree[i] = true;
    }

    synchronized int request() {
        while (true) {
            for (int i = 0; i < isFree.length; ++i)
                if (isFree[i]) {
                    isFree[i] = false;
                    return i;
                }
            try {
                this.wait(); 
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    synchronized void release(int index ) {
        isFree[index] = true;
        this.notify(); 
    }
}
//should contain the DirectoryManager for finding file sectors on Disk.
class DiskManager extends ResourceManager
{
    DirectoryManager directoryManager;
    DiskManager (int numDisks){
        super(numDisks);
        directoryManager = new DirectoryManager();
    }
    public int getNextFreeSectorOnDisk(int d){
        return MainClass.disks[d].free_sector;
    }
}

class PrinterManager extends ResourceManager
{
    PrinterManager(int numPrinters){
        super(numPrinters);
    }
}

class UserThread
    extends Thread
{
    String fileName;
    String line;
    UserThread(int id) // my commands come from an input file with name USERi where i is my user id
    {
        fileName = "USER"+id;
    }

    public void run()
    {
        try{
            FileInputStream inputStream = new FileInputStream(fileName);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(inputStream));
            while((line = myReader.readLine())!=null){
                if(line.startsWith(".save")){
                    StringBuffer saveFileName = new StringBuffer(line.substring(".save".length()).trim());
                    //System.out.println(saveFileName.toString());
                    saveFile(saveFileName, myReader);
                }else if(line.startsWith(".print")){
                    //System.out.println("start printing!");
                    StringBuffer printFileName = new StringBuffer(line.substring(".print".length()).trim());
                    printFile(printFileName);
                }else{
                    System.out.print("Unknown command: ");
                    System.out.println(line);
                    return;
                }
            }
            inputStream.close();
        }catch(Exception e){e.printStackTrace();}
    }
    private void saveFile(StringBuffer saveFileName, BufferedReader myReader) throws InterruptedException, IOException{
        int fileLine = 0;
        int disk_num = MainClass.diskManager.request();
        int offset = MainClass.diskManager.getNextFreeSectorOnDisk(disk_num);
       // System.out.print("disknum is ");
        //System.out.println(disk_num);
        //System.out.print("offset is ");
        //System.out.println(offset);
        int fileLines = 0;
        while((line = myReader.readLine())!=null){
            //System.out.println(line);
            if(line.startsWith(".end")){
               // System.out.print("end of file");
                //System.out.println(saveFileName);
                FileInfo file = new FileInfo(disk_num, offset, fileLine);
                MainClass.diskManager.directoryManager.enter(saveFileName, file);
                break;
            }else{
                StringBuffer newLine = new StringBuffer(line);//.substring(saveFileName.length()).trim()
                MainClass.disks[disk_num].write(offset + fileLine, newLine);
                //System.out.print("saving: ");
                //System.out.print(newLine);
                //System.out.println("successfully");
                fileLine++;
            }
        }
        MainClass.diskManager.release(disk_num);
    }
    private void printFile(StringBuffer printFileName) throws InterruptedException, IOException{
        PrintJobThread printJobThread = new PrintJobThread(printFileName);
        printJobThread.start();
	printJobThread.join();
    }   
}


public class MainClass
{
        /*
        Member Vars:
            - store a singleton of MainClass
            - array of UserThreads
            - array of Disks
            - array of printers
            - num of disks, users, printers
            - printer manager
            - disk manager
    */
    private static MainClass instance;
    static int NUM_USERS=4, NUM_DISKS=2, NUM_PRINTERS=3;
    static String userFileNames[];
    static UserThread users[];
	static Disk disks[];
	static Printer printers[];
	static DiskManager diskManager;
	static PrinterManager printerManager;

    private MainClass(){}
    
    public static void main(String[] args) throws InterruptedException {
        if(args.length ==3) {
            // get the singleton of MainClass
            getInstance();
            // call configure
            configure(args);
            // start user threads
            for(int i=0;i<NUM_USERS;i++){
                users[i].start();
            }
            // join user threads
            for(int i=0;i<NUM_USERS;i++){
                users[i].join();
            }
        } else {
            System.out.println("Not enough arguments provided.");
        }
    }
    static MainClass getInstance() {
        if (instance == null) {
            instance = new MainClass();
        }
        return instance;
    }
    static void configure(String[] argv) {
        // get the num of users, disks, and printers from the command line
        //java -jar 141OS.jar -#ofUsers -#ofDisks -#ofPrinters
        //substring(1) remove the '-'
        NUM_USERS = Integer.valueOf(argv[0].substring(1));
        NUM_DISKS = Integer.valueOf(argv[1].substring(1));
        NUM_PRINTERS = Integer.valueOf(argv[2].substring(1));
        // initialize all your member variables
        // populate the arrays; userthreads, disks, and printers
            // i.e loop through and intialize 
        users = new UserThread[NUM_USERS];
        disks = new Disk[NUM_DISKS];
        printers = new Printer[NUM_PRINTERS];
        for(int i=0;i<users.length;i++){
            users[i] = new UserThread(i);
        }
        for(int i=0;i<printers.length;i++){
            printers[i] = new Printer(i);
        }
        for(int i=0;i<disks.length;i++){
            disks[i] = new Disk();
        }
        diskManager = new DiskManager(NUM_DISKS);
        printerManager = new PrinterManager(NUM_PRINTERS);
    }

}
