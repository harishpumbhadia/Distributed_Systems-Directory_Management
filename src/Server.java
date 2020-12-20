//Name: Harish Pumbhadia
//Student ID: 1001773121

import java.util.logging.*;
import java.util.regex.Pattern;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.io.FileUtils;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
//Main class where main method is present 

public class Server {
	public static void main(String[] args) throws IOException {
		ServerGui server = new ServerGui();
	}		
}


// This class will process all commands
class ServerGui extends JFrame implements ActionListener {
	String receivedFileName,Name;
	private final  static Logger myLog = Logger.getLogger("Server");
	HashMap<Integer, String> map = new HashMap<>(); 
	JTextArea log = new JTextArea();
	int count = 1;
	String userin;
	JLabel serverStart;
	JTextField forUndo;
	JPanel inputPanel,bottomPanel;
	JFrame serverFrame;
	JMenuBar serverMenubar;
	JMenu serverMenu;
	JMenuItem exit;
	JButton undo;
	ArrayList<String> arr = new ArrayList<String>();
	// THis server Gui constructor will make frame of server
	ServerGui() throws IOException {
		// TODO Auto-generated constructor stub
		serverFrame= new JFrame();
		
		JSplitPane splitPane = new JSplitPane();
		///JPanel panel2 = new JPanel();
		JPanel topPanel = new JPanel();
		bottomPanel = new JPanel();
		inputPanel = new JPanel();
		
		
		
		
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);  // we want it to split the window verticaly
        splitPane.setDividerLocation(100);                    // the initial position of the divider is 200 (our window is 400 pixels high)
        splitPane.setTopComponent(topPanel);                  // at the top we want our "topPanel"
        splitPane.setBottomComponent(bottomPanel);            // and at the bottom we want our "bottomPanel"
 
		
		serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serverFrame.setLayout(new GridLayout()); 
        // we only add one element to the window itself
		serverFrame.add(splitPane); 
		
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		
		inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));     // we set the max height to 75 and the max width to (almost) unlimited
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));  
        
        serverStart = new JLabel(); 
        forUndo = new JTextField(15);
        undo = new JButton("Undo");
        topPanel.add(inputPanel);
		
		bottomPanel.add(log);
		serverStart.setText("Waiting for the clients");
		inputPanel.add(serverStart);
		inputPanel.add(forUndo);
		inputPanel.add(undo);
		
		undo.addActionListener((ActionListener) this);
		serverFrame.setSize(700, 700);
		serverFrame.setVisible(true);
		ServerSocket ss = new ServerSocket(35000);
		
			
				
		final AtomicInteger runningCount = new AtomicInteger(0);
	    final Integer limit = 4; //Here logic for only 3 clients can able to connect
		while(true) {
			Socket s = null;
			try {
				s= ss.accept(); //socket object for a client request
				
					
				DataInputStream din = new DataInputStream(s.getInputStream());
				DataOutputStream dout = new DataOutputStream(s.getOutputStream()); 
					
				userin = din.readUTF();
				// Here user name confliction and limit are checked
				boolean check = false;
				for(String i : arr) {
					if(i.matches(userin)) {
						myLog.log(Level.INFO,"Username is already present");
						log.setText(log.getText() + "\n" +count+ " Username is already present");
						map.put(count, "Username is already present");
						count++;
						System.out.println(count);
						bottomPanel.add(log);
						String failure = "Failure";
						dout.writeUTF(failure);	
						s.close();
						check = true;
						break;
					}
				}
					// If everything is good user will be connected and server gui will show username
					if(!check) {
						if (runningCount.incrementAndGet() < limit){
						arr.add(userin);
						String success = "success";
						dout.writeUTF(success+runningCount);
						//	clientConnected = new JLabel();
						
						File file =new File("D:\\Study\\DS-Lab\\lab\\"+userin).getAbsoluteFile();
						if(file.exists()) {
							String fileSaved = "D:\\Study\\DS-Lab\\lab\\"+userin+"  Parent Directory is already present at this path---";
							log.setText(log.getText() + "\n" +count+ " "+fileSaved);
							map.put(count, fileSaved);
							count++;
							bottomPanel.add(log);
							dout.writeUTF(fileSaved);
						}
						else {
							file.mkdir();
							String fileSaved = "D:\\Study\\DS-Lab\\lab\\"+userin+"  Parent Directory has been generated at this path---";
							log.setText(log.getText() + "\n" +count+" "+ fileSaved);
							map.put(count, fileSaved);
							count++;
							bottomPanel.add(log);
							dout.writeUTF(fileSaved);
						}
						 // create a new thread object 
		                Thread t = new ClientHandler(s,userin, din, dout,file); 
		                // Invoking the start() method 
		                t.start(); 
					}
						else {
							dout.writeUTF("limitExceeded");
							runningCount.decrementAndGet();
							s.close();
						}
				}
			}catch(Exception e){
				s.close();
			}
		}
		
	}
	
	class ClientHandler extends Thread {
		final DataInputStream din;
		final DataOutputStream dout;
		final Socket s;
		String name;
		JLabel user;
		File file;
		public ClientHandler(Socket s , String name,DataInputStream din, DataOutputStream dout, File file) {
			// TODO Auto-generated constructor stub
			this.s = s;
			this.name = name;
			this.din = din;
			this.dout = dout;
			this.file = file;
			user = new JLabel(name+" is connected");
			inputPanel.add(user);
		}
		
		public void run() {
			String receivedCommand;
			String receivedDirectoryName;
			
			
			//This while loop will able to serve command of the user
			while(true) {
				try {
					receivedCommand = din.readUTF();
					receivedDirectoryName = din.readUTF();
					switch(receivedCommand) {
						case "make":
							//String receivedFileName = din.readUTF();
							receivedFileName = file.getAbsolutePath()+"\\"+receivedDirectoryName;
							File newFile = new File(receivedFileName);
							newFile.mkdir();
							dout.writeUTF(receivedDirectoryName+ " has been created");
							log.setText(log.getText() + "\n" +count+ " "+receivedFileName+ " has been created");
							map.put(count, receivedFileName+ "  has been created");
							count++;
							break;
						case "delete":
							File deleteFile = new File(file.getAbsolutePath()+"\\"+receivedDirectoryName);
							File deletedFile  = new File("D:\\Study\\Trash");
							FileUtils.moveDirectoryToDirectory(deleteFile, deletedFile, true);
							dout.writeUTF(receivedDirectoryName+ " has been deleted");
							log.setText(log.getText() + "\n" +count+" "+ deleteFile+ "  has been deleted");
							map.put(count, deleteFile+ "  has been deleted");
							count++;
							//  String receivedDeleteFileName = din.readUTF(); 
//							  for(File subfile: file.listFiles()) {
//								  if(subfile.exists() && subfile.getName().matches(receivedDirectoryName)) { 
//									  subfile.delete();
//									  dout.writeUTF(receivedDirectoryName+ " has been deleted");
//								  }
//							  }
							break;
						case "rename":
							String fromDirectoryName = din.readUTF();
							String toDirectoryName = din.readUTF();
							
							for(File subfile: file.listFiles()) {
								  if(subfile.exists() && subfile.getName().matches(fromDirectoryName)) { 
										
									  File renameFile = new File(file.getAbsoluteFile()+"\\"+toDirectoryName);
									subfile.renameTo(renameFile);
									 
								  }
							  }
							log.setText(log.getText() + "\n" +count+" "+ file.getAbsolutePath()+"\\"+fromDirectoryName + "  has been renamed to  " + file.getAbsolutePath()+"\\"+toDirectoryName);
							map.put(count, file.getAbsolutePath()+"\\"+fromDirectoryName + "  has been renamed to  " + file.getAbsolutePath()+"\\"+toDirectoryName);
							count++;
							break;
						case "list":
							ArrayList<String> list = new ArrayList<String>();
							//DataOutputStream out = new DataOutputStream(s.getOutputStream());
							
							
							for(File subfile: file.listFiles()) {
								list.add(subfile.getName());
							}
							int len = list.size();
							dout.writeInt(len);
							for(String arr : list) {
								dout.writeUTF(arr);
							}
							
							
							//out.close();
							break;
						case "exit":
							System.out.println("Client " + this.s + " sends exit..."); 
		                    System.out.println("Closing this connection."); 
		                    this.s.close(); 
		                    arr.remove(name);
		                    user.setText("");
		                    System.out.println("Connection closed"); 
		                    log.setText(log.getText() + "\n" +count+" "+ name+ " has been disconnected" );
							map.put(count, name+ "  has been disconnected" );
							count++;
							break;
					          
						case "move":
							String moveDirectoryName = din.readUTF();
							String toLocation = din.readUTF();
							File src = new File(file.getAbsoluteFile()+"\\"+moveDirectoryName);
							File dest = new File(file.getAbsoluteFile()+"\\"+toLocation);
							FileUtils.moveDirectoryToDirectory(src,  dest, true);
							log.setText(log.getText() + "\n" +count+" "+ src.getPath()+ "  has been moved to  " + dest.getPath());
							map.put(count, src.getPath()+ "  has been moved to  " + dest.getPath());
							count++;
							break;
					          
					}//switch ends here
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
				if(s.isClosed()) {
					break;
				}
				
			}
		}
	}
	
	public void actionPerformed(ActionEvent ae) {
		
		if(ae.getSource()== undo) {
			
			if(log.getText().contains(forUndo.getText())) {
				String one = map.get(Integer.parseInt(forUndo.getText()));
				if(one.contains("created")){
					String[] name = one.split("  ");
					File fileName = new File(name[0]);
					
					try {
						
							FileUtils.deleteDirectory(fileName);
							String[] logArray = log.getText().split("\n");
							logArray[Integer.parseInt(forUndo.getText())] = "";
							log.setText(String.join("\n", logArray));
							//log.replaceRange(null, log.getText().indexOf(forUndo.getText()),log.getText().lastIndexOf("\n"));
							
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(one.contains("generated")||one.contains("already present")) {
					String[] Name = one.split("  ");
					File fileName = new File(Name[0]);
					
					String pattern = Pattern.quote(System.getProperty("file.separator"));
					String[] splittedFileName = Name[0].split(pattern);
					int length = splittedFileName.length;
					try {
						FileUtils.deleteDirectory(fileName);
						String[] logArray = log.getText().split("\n");
						for(int i=0;i<logArray.length;i++) {
							if(logArray[i].contains(splittedFileName[length-1])) {
								logArray[i] = "";
								log.setText(String.join("\n", logArray));
							}
						}
					}catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(one.contains("deleted")) {
					String[] name = one.split("  ");
					File fileName = new File("D:\\Study\\Trash");
					for(File file : fileName.listFiles()) {
						if(name[0].contains(file.getName())) {
							
							String rollbackFile = name[0].replaceAll(file.getName(), "");
							File newFile = new File(rollbackFile);
								try {
									FileUtils.moveDirectoryToDirectory(file, newFile, true);
									String[] logArray = log.getText().split("\n");
									logArray[Integer.parseInt(forUndo.getText())] = "";
									log.setText(String.join("\n", logArray));
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						}
					}
					
				}
				if(one.contains("renamed")) {
					String[] name = one.split("  ");
					File fileName = new File(name[0]);
					File deleteFile = new File(name[2]);
					try {
						FileUtils.deleteDirectory(deleteFile);
						fileName.mkdir();
						String[] logArray = log.getText().split("\n");
						logArray[Integer.parseInt(forUndo.getText())] = "";
						log.setText(String.join("\n", logArray));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(one.contains("moved")) {
					String[] name = one.split("  ");
					File fileMoved =new File(name[0]);
					File toFile = new File(name[2]);
					for(File file : toFile.listFiles()) {
						if(file.getName().matches(fileMoved.getName())) {
							File destFile = new File(name[0]);
							try {
								FileUtils.moveDirectory(file, destFile);
								String[] logArray = log.getText().split("\n");
								logArray[Integer.parseInt(forUndo.getText())] = "";
								log.setText(String.join("\n", logArray));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
}

