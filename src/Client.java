import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.*;

public class Client {
	public static void main(String[] args) {
		Gui g = new Gui();
	}
}

class Gui extends JFrame implements ActionListener{
	Socket s;
	DataOutputStream dout;
	DataInputStream din;
	JFrame frame, updateJFrame, messageFrame,makeFrame,deleteFrame,moveFrame,renameFrame,listFrame,listHomeDirectory;
	JTextField user, for_commands, command, directoryName,fromTextField,toTextField,directory,directoryToMove,toLocationtoTextField,deleteDirectory,deselect,select,selectIdentifier;
	JLabel Username,output,file_generated,makeFile,messageLabel;
	JButton Ok,commandEnter,enterButton,makeDirectoryEnter,moveEnterButton,Alright,deleteEnter,ListAlright,listEnter,synchronize,desynchronize,chooseIdentiferEnter;
	JMenuBar menubar;
	JMenuItem exit;
	JMenu File;
	File file, updateFile;
	String User,success;
	String[] localDirectory;
	ArrayList<String> directoryList;
	
	//constructor for a main frame
	Gui() {
		frame = new JFrame();
		menubar = new JMenuBar();
		File = new JMenu("File");
		exit = new JMenuItem("Exit");
		Username = new JLabel("Username: ");
		output = new JLabel();
		file_generated = new JLabel();
		user = new JTextField(10);
		Ok = new JButton("Enter");
		
		
		for_commands = new JTextField(20);
		
		frame.setLayout(new FlowLayout(100, 30, 15)	);
		frame.setVisible(true);
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(menubar);
		menubar.add(File);
		
		File.add(exit);
		frame.add(Username);
		frame.add(user);
		frame.add(Ok);
		
		frame.add(output);
		frame.add(file_generated);
		
		Ok.addActionListener(this);
		exit.addActionListener(this);
	}
	// this are all action when button is clicked in all frames
	public void actionPerformed(ActionEvent ae) {
		
		if(ae.getSource()== exit) {
			try {
				dout.writeUTF("Exit");
				this.s.close();
				dout.flush();
				dout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
		}
		if(ae.getSource()== Ok) {
			try {
             
                processInformation();
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
		}
		
		if(ae.getSource() == commandEnter) {
			try {
				updateJFrame.dispose();
				directoryInHomeDirectory();
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		if(ae.getSource() ==  enterButton) {
			try {
				renameDirectory();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		if(ae.getSource() == makeDirectoryEnter) {
			try {
				makeDirectory();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(ae.getSource() == moveEnterButton) {
			try {
				moveDirectory();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(ae.getSource() == synchronize) {
			try {
				listHomeDirectory.dispose();
				process();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(ae.getSource() == desynchronize) {
			try {
				listHomeDirectory.dispose();
				desyncProcess();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(ae.getSource() == deleteEnter) {
			try {
				deleteDirectory();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(ae.getSource() == listEnter) {
			try {
				listFrame.dispose();
				updateGui();
			}catch(IOException e) {}
		}
		
		if(ae.getSource() == Alright) {
			try {
				messageFrame.dispose();
				updateGui();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	// This is method is invoked when the main frame enter button clicked event
	public void processInformation() throws UnknownHostException, IOException {
		s = new Socket("localhost",35000);
		String name = user.getText();
		if(name.matches("^[a-zA-Z]*$")) {
	    dout = new DataOutputStream(s.getOutputStream());
	    din = new DataInputStream(s.getInputStream());
	    dout.writeUTF(name);
		
	    success = din.readUTF();
	    
	    // If socket is connected in server side , it will send message of success, failure due to name conflict or limit exceeded
	    if(success.matches("success1")) {
			frame.dispose();
			messageGui();
		}
	    	
	    else if(success.matches("success2")) {
	    	//listHomeDirectory(success);
	    	frame.dispose();
	    	messageGui();
	    }
	    else if(success.matches("success3") ) {
	    	frame.dispose();
	    	messageGui();
	    }
	    else if(success.matches("Failure")){
	    	System.out.println("Username already exists");
	    	s.close();
	    	frame.dispose();
	    	Gui g = new Gui();
	    	
	    }
	    else if(success.matches("limitExceeded")){
	    	System.out.println("Limit Exceed");
	    	s.close();
	    	frame.dispose();
	    	Gui g = new Gui();
	    }
	   
		}
		else {
			s.close();
			frame.dispose();
			System.out.println("Only Letters are allowed");
			Gui g = new Gui();
		}
	}
	// This Gui will invoke when user successfully connected 
	public void updateGui() throws IOException {
		
		updateJFrame = new JFrame();
		menubar = new JMenuBar();
		File = new JMenu("File");
		exit = new JMenuItem("Exit");
		JLabel forCommand = new JLabel("Command: ");
		command = new JTextField(10);
		command.setBounds(200, 50, 150, 40);
		commandEnter = new JButton("Enter");
		
		updateJFrame.setLayout(new FlowLayout(300, 100, 15)	);
		updateJFrame.setVisible(true);
		updateJFrame.setSize(500, 500);
		updateJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		updateJFrame.setJMenuBar(menubar);
		menubar.add(File);
		File.add(exit);
		updateJFrame.add(forCommand);
		updateJFrame.add(command);
		//updateJFrame.add(dirName);
		//updateJFrame.add(directoryName);
		updateJFrame.add(commandEnter);	
		
		// Calling action listener
		commandEnter.addActionListener(this);
		exit.addActionListener(this);
		
		
		
	}
	
	
	public void process() throws IOException {
		localDirectory = select.getText().split(",");
		if(success.matches("success1")) {
			Thread localClientHandler = new localClientHandler("A",localDirectory);
			localClientHandler.start();
		}else  if(success.matches("success2")) {
			Thread localClientHandler = new localClientHandler("B",localDirectory);
			localClientHandler.start();
		}
		else if(success.matches("success3")){
			Thread localClientHandler = new localClientHandler("C",localDirectory);
			localClientHandler.start();
		}	
		

		updateGui();
	}
	public void desyncProcess() throws IOException {
		if(success.matches("success1")) {
			localClientHandler localClientHandler = new localClientHandler("A");
			localClientHandler.desync();
		}else  if(success.matches("success2")) {
			localClientHandler localClientHandler = new localClientHandler("B");
			localClientHandler.desync();
		}
		else if(success.matches("success3")){
			localClientHandler localClientHandler = new localClientHandler("C");
			localClientHandler.desync();
		}	
		

		updateGui();
	}
	
	//This method will invoke when server sends confirmation mesaage after processing any command
	public void messageGui() throws IOException {
		messageFrame = new JFrame();
		String message = din.readUTF();
		messageLabel = new JLabel(message);
		messageFrame.add(messageLabel);
		Alright = new JButton("Alright");
		
		messageFrame.setLayout(new FlowLayout(300, 100, 15)	);
		messageFrame.setVisible(true);
		messageFrame.setSize(500, 500);
		messageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		messageFrame.add(messageLabel);
		messageFrame.add(Alright);
		
		Alright.addActionListener(this);
		
	}
	
		
	
	// This method of Gui will invoke when user enters make command
	public void makeGui() {
		makeFrame = new JFrame();
		JLabel directoryName = new JLabel("Directory Name: ");
		directory = new JTextField(10);
		makeDirectoryEnter = new JButton("Enter");
		
		makeFrame.setLayout(new FlowLayout(300, 100, 15)	);
		makeFrame.setVisible(true);
		makeFrame.setSize(500, 500);
		makeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		makeFrame.add(directoryName);
		makeFrame.add(directory);
		makeFrame.add(makeDirectoryEnter);
		
		makeDirectoryEnter.addActionListener(this);
	}
	//This method of gui will invoke when user enters move command
	public void moveGui() {
		moveFrame = new JFrame();
		JLabel directoryName = new JLabel("Directory name: ");
		directoryToMove = new JTextField(15);
		directoryToMove.setBounds(200, 50, 150, 40);
		JLabel toLocation = new JLabel("To Location: ");
		toLocationtoTextField = new JTextField(15);
		toLocationtoTextField.setBounds(200, 50, 150, 40);
		moveEnterButton = new JButton("Enter");
		
		moveFrame.setLayout(new FlowLayout(300, 100, 15)	);
		moveFrame.setVisible(true);
		moveFrame.setSize(500, 500);
		moveFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		moveFrame.add(directoryName);
		moveFrame.add(directoryToMove);
		moveFrame.add(toLocation);
		moveFrame.add(toLocationtoTextField);
		moveFrame.add(moveEnterButton);
		
		moveEnterButton.addActionListener(this);
		
	}
	//This method of gui will invoke when user enters delete command
	public void deleteGui() {
		deleteFrame = new JFrame();
		JLabel delDir = new JLabel("Directory Name:");
		deleteDirectory = new JTextField(10);
		deleteDirectory.setBounds(200, 50, 150, 40);
		deleteEnter = new JButton("Enter");
		
		deleteFrame.setLayout(new FlowLayout(300, 100, 15)	);
		deleteFrame.setVisible(true);
		deleteFrame.setSize(500, 500);
		deleteFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		deleteFrame.add(delDir);
		deleteFrame.add(deleteDirectory);
		deleteFrame.add(deleteEnter);
		
		deleteEnter.addActionListener(this);
	}
	
	//This method of gui will invoke when user enters rename command
	public void renameGui() {
		renameFrame = new JFrame();
		JLabel from = new JLabel("From: ");
		fromTextField = new JTextField(15);
		fromTextField.setBounds(200, 50, 150, 40);
		JLabel to = new JLabel("To: ");
		toTextField = new JTextField(15);
		toTextField.setBounds(200, 50, 150, 40);
		enterButton = new JButton("Enter");
		
		renameFrame.setLayout(new FlowLayout(300, 100, 15)	);
		renameFrame.setVisible(true);
		renameFrame.setSize(500, 500);
		renameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		renameFrame.add(from);
		renameFrame.add(fromTextField);
		renameFrame.add(to);
		renameFrame.add(toTextField);
		renameFrame.add(enterButton);
		
		enterButton.addActionListener(this);
	}
	
public void displayGui() {
		
		listHomeDirectory = new JFrame();
		
		JSplitPane splitPane = new JSplitPane();
		///JPanel panel2 = new JPanel();
		JPanel topPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		JPanel inputPanel = new JPanel();
		JLabel copy = new JLabel("Select the Home Directory to synchronize:");
		select = new JTextField(15);
		synchronize = new JButton("Enter");
		desynchronize = new JButton("Desyncronize");
		//listHomeDirectory.setLayout(new FlowLayout(300, 100, 15)	);
		
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);  // we want it to split the window verticaly
        splitPane.setDividerLocation(100);                    // the initial position of the divider is 200 (our window is 400 pixels high)
        splitPane.setTopComponent(topPanel);                  // at the top we want our "topPanel"
        splitPane.setBottomComponent(bottomPanel);            // and at the bottom we want our "bottomPanel"

        listHomeDirectory.setLayout(new GridLayout());  // the default GridLayout is like a grid with 1 column and 1 row,
        // we only add one element to the window itself
        listHomeDirectory.add(splitPane);  
		
		listHomeDirectory.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		listHomeDirectory.setLayout(new GridLayout()); 
        // we only add one element to the window itself
		listHomeDirectory.add(splitPane); 
		
		//BoxLayout boxlayout2 = new BoxLayout(panel2, BoxLayout.X_AXIS);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS)); // BoxLayout.Y_AXIS will arrange the content vertically
		//listHomeDirectory.add(panel);
		//panel2.setLayout(boxlayout2);
		//panel.setLayout(boxlayout);
        
        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));     // we set the max height to 75 and the max width to (almost) unlimited
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));  
        
		topPanel.add(inputPanel);
		inputPanel.add(copy);
		inputPanel.add(select);
		inputPanel.add(synchronize);
		inputPanel.add(desynchronize);
	
		
		
	//	listHomeDirectory.add(panel2);
		
		
		File file =new File("D:\\Study\\DS-Lab\\lab").getAbsoluteFile();
		
		for(File subfile: file.listFiles()) {
			JLabel listDirectory = new JLabel();
			bottomPanel.add(listDirectory);

			
			
			listDirectory.setText(subfile.getName());
			
			String[] fileArray = subfile.list();
			try {
				int size = fileArray.length;
				for(int i = 0 ; i<size ; i++) {
					JLabel listSubDirectory = new JLabel();
					listSubDirectory.setBounds(100, 100, 30, 30);
					bottomPanel.add(listSubDirectory);

					listSubDirectory.setText("-------"+fileArray[i]);
				}
			}catch(Exception e) {}
			
		}
		
		//pack();
		listHomeDirectory.setSize(700, 700);
		listHomeDirectory.setVisible(true);
		synchronize.addActionListener(this);
		desynchronize.addActionListener(this);
	}
	
	public void listGui() throws IOException {
		int size = din.readInt();		
		
		listFrame = new JFrame();
		JLabel list = new JLabel("List: ");
		listEnter = new JButton("Alright");
		
		
		listFrame.setLayout(new FlowLayout(300, 100, 15)	);
		listFrame.setVisible(true);
		listFrame.setSize(500, 500);
		listFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		listFrame.add(list);
		
		for(int i=0; i<size ; i++) {
			JLabel listDirectory = new JLabel();
			listFrame.add(listDirectory);
			listDirectory.setText(din.readUTF());
		}
		listFrame.add(listEnter);
		listEnter.addActionListener(this);
	}
	//This method  will invoke when user enters Directory name that which directory user wants to rename
	public void renameDirectory() throws IOException {
		String fromDirectoryName = fromTextField.getText();
		String toDirectoryName = toTextField.getText();
		dout.writeUTF("rename");
		dout.writeUTF("");
		dout.writeUTF(fromDirectoryName);
		dout.writeUTF(toDirectoryName);
		renameFrame.dispose();
		updateGui();
	}
	
	//This method will process make directory command
	public void makeDirectory() throws IOException {
		String directoryName = directory.getText();
		String textCommand = command.getText().toLowerCase();
		dout.writeUTF(textCommand);
		dout.writeUTF(directoryName);
		makeFrame.dispose();
		messageGui();
	}
	
	//This method will process delete directory command
	public void deleteDirectory() throws IOException {
		String directoryName = deleteDirectory.getText();
		String textCommand = command.getText().toLowerCase();
		dout.writeUTF(textCommand);
		dout.writeUTF(directoryName);
		deleteFrame.dispose();
		messageGui();
	}
	//This method will process move directory command
	public void moveDirectory() throws IOException {
		String dirName = directoryToMove.getText();
		String to = toLocationtoTextField.getText();
		String textCommand = command.getText().toLowerCase();
		dout.writeUTF(textCommand);
		dout.writeUTF("");
		dout.writeUTF(dirName);
		dout.writeUTF(to);
		moveFrame.dispose();
		updateGui();
	}
	
//	public void listDirectory() {
//		
//	}
	//This method will invoke when user apply commands like make,delete, rename ,etc 
	public void directoryInHomeDirectory() throws IOException, StringIndexOutOfBoundsException {
		try {
		
			String textCommand = command.getText().toLowerCase();
			
			if(textCommand.matches("make")) { 
				makeGui();
			}
			
			if(textCommand.matches("display")) {
				displayGui();
			}
			
			
			if(textCommand.matches("delete")) {
				deleteGui();
			}
			
			if(textCommand.matches("list")) {
				dout.writeUTF(textCommand);
				dout.writeUTF("");
				
				//in.close();
				//out.close();
				listGui();
			}
			
			if(textCommand.matches("rename")) {
				renameGui();
			}
			if(textCommand.matches("move")) {
				moveGui();
			}
			if(textCommand.matches("exit")) {
				System.out.println("In exit");
				dout.writeUTF(textCommand);
				dout.writeUTF("");
				s.close();
				dout.flush();
				dout.close();
			}
			
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}



