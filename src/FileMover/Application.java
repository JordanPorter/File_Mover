package FileMover;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class Application extends JFrame	{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// GUI Variables
	private JPanel contentPane;
	private JTextField sourceFolder;
	private JTextField destinationFolder;
	private JComboBox<String> selectedFileType;
	private JLabel fileType;
	private JPanel panel;
	private JTextArea infoScreen;
	
	private String[] fileTypes;

	public static void main(String[] args)	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application frame = new Application();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public Application()	{
	    setDefaultCloseOperation(3);
	    setBounds(100, 100, 450, 300);
	    setTitle("The Files Mover");
	    setResizable(false);
	
	    this.contentPane = new JPanel();
	    this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    setContentPane(this.contentPane);
	    this.contentPane.setLayout(null);
	
	    JButton selectSourceFolderButton = new JButton("Select Source Folder");
	    selectSourceFolderButton.addMouseListener(new MouseAdapter()	{
	      public void mouseClicked(MouseEvent arg0) {	
	        JFileChooser source = new JFileChooser();
	        source.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        source.showOpenDialog(Application.this.contentPane);
	        Application.this.sourceFolder.setText(source.getSelectedFile().getPath());
	        fileTypes = getFileTypes(sourceFolder.getText());
	        for(String s : fileTypes)	{
	        	selectedFileType.addItem(s);
	        }
	        selectedFileType.setModel(new DefaultComboBoxModel<String>(fileTypes));
	      }
	    });
	    selectSourceFolderButton.setBounds(6, 6, 183, 29);
	    this.contentPane.add(selectSourceFolderButton);
	
	    JButton selectDestinationButton = new JButton("Select Destination Folder");
	    selectDestinationButton.addMouseListener(new MouseAdapter()	{
	      public void mouseClicked(MouseEvent e) {
	        JFileChooser destination = new JFileChooser();
	        destination.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        destination.showOpenDialog(Application.this.contentPane);
	        Application.this.destinationFolder.setText(destination.getSelectedFile().getPath());
	      }
	    });
	    selectDestinationButton.setBounds(6, 34, 183, 29);
	    this.contentPane.add(selectDestinationButton);
	
	    this.sourceFolder = new JTextField("No Folder Selected");
	    this.sourceFolder.setBounds(201, 5, 243, 28);
	    this.sourceFolder.setEditable(false);
	    this.contentPane.add(this.sourceFolder);
	    this.sourceFolder.setColumns(10);
	
	    this.destinationFolder = new JTextField("No Folder Selected");
	    this.destinationFolder.setBounds(201, 33, 243, 28);
	    this.destinationFolder.setEditable(false);
	    this.contentPane.add(this.destinationFolder);
	    this.destinationFolder.setColumns(10);
	
	    this.selectedFileType = new JComboBox<String> (new String[] {"Please Select A Source Folder"});
	    this.selectedFileType.setBounds(153, 69, 57, 28);
	    this.contentPane.add(this.selectedFileType);
	
	    this.fileType = new JLabel("Type Of File To Move:");
	    this.fileType.setBounds(16, 75, 173, 16);
	    this.contentPane.add(this.fileType);
	
	    this.panel = new JPanel();
	    this.panel.setBorder(new TitledBorder(null, "Information:", 4, 2, null, null));
	    this.panel.setBounds(0, 113, 450, 165);
	    this.contentPane.add(this.panel);
	    this.panel.setLayout(null);
	
	    this.infoScreen = new JTextArea("");
	    this.infoScreen.setEditable(false);
	    JScrollPane j = new JScrollPane(this.infoScreen);
	    j.setBounds(6, 18, 438, 141);
	    this.panel.add(j);
	
	    JSeparator s = new JSeparator();
	    s.setBounds(6, 97, 438, 10);
	    s.setForeground(Color.BLACK);
	    this.contentPane.add(s);
	
	    JButton btnMoveFiles = new JButton("Move Files");
	    btnMoveFiles.addMouseListener(new MouseAdapter()	{
	      public void mouseClicked(MouseEvent e)	{
	        if (Application.this.selectedFileType.hashCode() <= 0) {
	          Application.this.infoScreen.append("Please Input The Type Of File To Move.\n");
	          return;
	        }
	
	        if ((Application.this.sourceFolder.getText().equalsIgnoreCase("no folder selected")) || 
	          (Application.this.destinationFolder.getText().equalsIgnoreCase("no folder selected"))) {
	          Application.this.infoScreen.append("No Source or Destination Folder Selected\n");
	          return;
	        }
	
	        Application.this.infoScreen.append("Moving Files...\n");
	        Application.this.moveFiles();
	      }
	    });
	    btnMoveFiles.setBounds(211, 70, 117, 29);
	    this.contentPane.add(btnMoveFiles);
	}
	
	/*
	 * Get the extension of a file.
	 */  
	public static String getExtension(File f) {
	    String ext = null;
	    String s = f.getName();
	    int i = s.lastIndexOf('.');

	    if (i > 0 &&  i < s.length() - 1) {
	        ext = s.substring(i+1).toLowerCase();
	    }
	    return ext;
	}
	
	private String[] getFileTypes(String pathToSource)	{
		ArrayList<String> fileTypes = new ArrayList<String>();
		for (File f : new File(this.sourceFolder.getText()).listFiles())	{
			if(fileTypes.contains(getExtension(f)))	{
				continue;
			}
			else {
				fileTypes.add(getExtension(f));
			}
		}
		System.out.println(fileTypes.toString());
		return fileTypes.toArray(new String[] {});
	}
	
	 public void moveFiles()
	  {
	    ArrayList <FileCopy> copyTasks = new ArrayList <FileCopy> ();
	    for (File f : new File(this.sourceFolder.getText()).listFiles()) {
	      if (f.getName().toLowerCase().contains(this.selectedFileType.getSelectedItem().toString())) {
	        copyTasks.add(new FileCopy(new File(this.destinationFolder.getText() + "/" + f.getName()), f));
	      }
	    }
	    this.infoScreen.append("There Are " + copyTasks.size() + " Files To Copy\n");
	    for (FileCopy f : copyTasks) {
	    	this.infoScreen.append("Copying File " + f.toString() + ":\n");
	    	f.start();
	    	System.out.println("Copying Started");
	    }
	    int completed = 0;
	    while(completed < copyTasks.size())	{
		    for (FileCopy f : copyTasks)	{
		    	if(f.getSourceFile().getTotalSpace() == f.getDestinationFile().getTotalSpace())	{
		    		System.out.println("Completed");
		    		completed++;
		    		this.infoScreen.append("Finished Copying " + f.getSourceFile().getName() + ".\t|\n");
		    		this.infoScreen.append("Total Time Taken: " + f.getTimeTaken() + "seconds.\t|\n");
		    	}
		    }
	    }
	    this.infoScreen.append("Copying Complete");
	  }
}