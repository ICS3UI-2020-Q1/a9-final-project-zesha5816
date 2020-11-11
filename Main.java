import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main implements Runnable, ActionListener{

  // Class Variables 
	JPanel gameScreen;
	JPanel panelSwitcher;
	JPanel gameEnd;
	JPanel about;

	ImageIcon redImage;
	ImageIcon blueImage;
	ImageIcon startingScreen;
	ImageIcon endingScreen;
	ImageIcon aboutImage;

	JLabel imageLabel;
	JLabel endLabel;
	JLabel winnerLabel;
	JLabel aboutLabel;

	CardLayout screens;

	JButton playButton;
	JButton aboutButton;
	JButton backButton;

	int x;

	JButton[] buttonGrid = new JButton[30];
	JTextArea instructionText;
	Font font;
	boolean playerRedTrun = true;
	String winner;
  // Method to assemble our GUI
  public void run(){
    // Creats a JFrame that is 800 pixels by 600 pixels, and closes when you click on the X
    JFrame frame = new JFrame("Connect 4");
    // Makes the X button close the program
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // makes the windows 800 pixel wide by 600 pixels tall
    frame.setSize(800,600);
    // shows the window
    frame.setVisible(true);
		
		//==========================code for the title screen==============================
		//create the game screen
		gameScreen = new JPanel();
		gameScreen.setLayout(null);

		//create the end screen
		gameEnd = new JPanel();
		gameEnd.setLayout(null);

		//create the about label
		about = new JPanel();
		about.setLayout(null);
		
		//create the play button to start the game
		playButton = new JButton("PLAY");
		playButton.addActionListener(this);
		playButton.setActionCommand("play");

		//create the rules button 
		aboutButton = new JButton("ABOUT");
		aboutButton.addActionListener(this);
		aboutButton.setActionCommand("about");

		//create the back button 
		backButton = new JButton("<--BACK");
		backButton.addActionListener(this);
		backButton.setActionCommand("back");
	
		//set up the ImageIcon
		startingScreen = new ImageIcon("Loadingscreen.jpg");
		redImage       = new ImageIcon("red.jpg");
		blueImage      = new ImageIcon("blue.png");
		endingScreen   = new ImageIcon("endscreen.jpg");
		aboutImage		 = new ImageIcon("about.jpg");

		//make the jlabel for the imageLabel
		imageLabel = new JLabel(startingScreen);
		imageLabel.setBounds(0,0,800,600);

		//make the jlabel for the end screen
		endLabel = new JLabel(endingScreen);
		endLabel.setBounds(0,0,800,600);

		//make the jlabel for the about page 
		aboutLabel = new JLabel(aboutImage);
		aboutLabel.setBounds(0,0,800,600);

		//make the winner label 
		winnerLabel = new JLabel();
		winnerLabel.setBounds(295,160,280,35);
		Font font0 = new Font("System", Font.PLAIN, 28);
		winnerLabel.setFont(font0);


		//set the location and size of the buttons
		playButton.setBounds(330,450,180,45);
		aboutButton.setBounds(330,500,180,45);
		backButton.setBounds(100,515,100,45);

		//add it to the game screen
		gameScreen.add(aboutButton);
		gameScreen.add(playButton);
		gameScreen.add(imageLabel);

		//add the ending screen to the gameEnd
		gameEnd.add(winnerLabel);
		gameEnd.add(endLabel);

		//add the about insturctions and the back button to the game panel
		about.add(backButton);
		about.add(aboutLabel);
		

	
		//======Here the code starts for the actual game========
		//our main Panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		//the button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(5,6));

		//create all of my buttons
		for(int i = 0; i < buttonGrid.length; i++){
			//create a blank button
			buttonGrid[i] = new JButton();
			// add the action command 
			String buttonNumber = String.valueOf(i);
			buttonGrid[i].setActionCommand(buttonNumber);
			//add the action ActionListener
			buttonGrid[i].addActionListener(this);

			//add the button to the grid 
			buttonPanel.add(buttonGrid[i]);
		}

		//create the textArea for the instruction textArea
		instructionText = new JTextArea("=================Red Circle goes first!==================");
		//make the font darker for the instructions
	  Font font = new Font("Arial", Font.PLAIN, 20);
		instructionText.setFont(font);
    instructionText.setForeground(Color.RED);
	
		//add the pieces together
		mainPanel.add(instructionText, BorderLayout.PAGE_START);
		mainPanel.add(buttonPanel, BorderLayout.CENTER);
		
		//create the screen jpanel manager
		screens = new CardLayout();
		
		panelSwitcher = new JPanel();
		panelSwitcher.setLayout(screens);

		// add screens to the card layout 
		panelSwitcher.add(gameScreen, "GameScreen");
		panelSwitcher.add(mainPanel, "Game");
		panelSwitcher.add(gameEnd, "EndGame");
		panelSwitcher.add(about, "About");

		//add it to the frame
		frame.add(panelSwitcher);

		//sets the screen to show by asking the card layout
		screens.show(panelSwitcher, "GameScreen");
  }

  // method called when a button is pressed
  public void actionPerformed(ActionEvent e){
    // get the command from the action
    String command = e.getActionCommand();
		System.out.println(x);
		//sets which screen to be displayed if the "play" button is pressed 
		if(command.equals("play")){
			//switch the screen to the game
			screens.show(panelSwitcher, "Game");
		}else if(command.equals("about")){
			//switch screens to the abou panel
			screens.show(panelSwitcher, "About");
		}else if(command.equals("back")){
			//switch back to starting screen
			screens.show(panelSwitcher, "GameScreen");
		}
		//determine which button was pressed using the action command 
		int buttonIndex = Integer.parseInt(command);
		if(isSpotFree(buttonIndex)){
			//check if it is playerRedTrun
			if(playerRedTrun){
				buttonGrid[buttonIndex].setIcon(redImage);
				//make it so that the user cant change the color of one which is already selected
				buttonGrid[buttonIndex].setActionCommand("disabled");
			}else{
				//its blues turn: place the blue circle on the button
				buttonGrid[buttonIndex].setIcon(blueImage);
				//make it so that the user cant change the color of one which is already selected
				buttonGrid[buttonIndex].setActionCommand("disabled");
			}
			//change the player
				changePlayer();
			//check for a winner
			if(didWeWin()){
				if(playerRedTrun){
					winnerLabel.setText("Winner is Blue!");
					winnerLabel.setForeground(Color.BLUE);
				}else{
					winnerLabel.setText("Winner is Red!");
					winnerLabel.setForeground(Color.RED);
				}
				screens.show(panelSwitcher, "EndGame");
			}
  	}
  }

	public boolean isSpotFree(int buttonNumber){
		//check to see if there is an red or blue in the button
		String buttonText = buttonGrid[buttonNumber].getText();
		if(buttonText.equals(redImage) || buttonText.equals(blueImage)){
			return false;
		}else{
			return true;
		}
	}

	public boolean didWeWin(){
		//check for horizontal win 
		for(int row=0; row<5; row++){
			for(int col = 0; col < 3; col++){
				int buttonPosition = (row*6)+(col);
				Icon button0 = buttonGrid[buttonPosition].getIcon(); 
				Icon button1 = buttonGrid[buttonPosition + 1].getIcon();
				Icon button2 = buttonGrid[buttonPosition + 2].getIcon();
				Icon button3 = buttonGrid[buttonPosition + 3].getIcon();
				if(button0.equals(button1) && button1.equals(button2) && button2.equals(button3)){
					return true;
	
				}
			}
		}

		//check vertical win
	for(int col = 0; col<6; col++){
			for(int row = 0; row < 2; row++){
				int buttonPosition = (col*5)+(row);
				Icon button0 = buttonGrid[buttonPosition].getIcon(); 
				Icon button1 = buttonGrid[buttonPosition + 6].getIcon();
				Icon button2 = buttonGrid[buttonPosition + 12].getIcon();
				Icon button3 = buttonGrid[buttonPosition + 18].getIcon();
				if(button0.equals(button1) && button1.equals(button2) && button2.equals(button3)){
					return true;
				}
			}
		}

		//get all the buttons 
		Icon button0 = buttonGrid[0].getIcon();
		Icon button1 = buttonGrid[1].getIcon();
		Icon button2 = buttonGrid[2].getIcon();
		Icon button3 = buttonGrid[3].getIcon();
		Icon button4 = buttonGrid[4].getIcon();
		Icon button5 = buttonGrid[5].getIcon();
		Icon button6 = buttonGrid[6].getIcon();
		Icon button7 = buttonGrid[7].getIcon();
		Icon button8 = buttonGrid[8].getIcon();
		Icon button9 = buttonGrid[9].getIcon();
		Icon button10= buttonGrid[10].getIcon();
		Icon button11= buttonGrid[11].getIcon();
		Icon button12= buttonGrid[12].getIcon();
		Icon button13= buttonGrid[13].getIcon();
		Icon button14= buttonGrid[14].getIcon();
		Icon button15= buttonGrid[15].getIcon();
		Icon button16= buttonGrid[16].getIcon();
		Icon button17= buttonGrid[17].getIcon();
		Icon button18= buttonGrid[18].getIcon();
		Icon button19= buttonGrid[19].getIcon();
		Icon button20= buttonGrid[20].getIcon();
		Icon button21= buttonGrid[21].getIcon();
		Icon button22= buttonGrid[22].getIcon();
		Icon button23= buttonGrid[23].getIcon();
		Icon button24= buttonGrid[24].getIcon();
		Icon button25= buttonGrid[25].getIcon();
		Icon button26= buttonGrid[26].getIcon();
		Icon button27= buttonGrid[27].getIcon();
		Icon button28= buttonGrid[28].getIcon();
		Icon button29= buttonGrid[29].getIcon();

		//check to see if diagnol match
		if(button0.equals(button7) && button7.equals(button14) && button14.equals(button21)){
			return true;
		}else	if(button21.equals(button14) && button14.equals(button7) && button7.equals(button0)){
			return true;
		}else	if(button6.equals(button13) && button13.equals(button20) && button20.equals(button27)){
			return true;
		}else	if(button18.equals(button13)&& button13.equals(button8)  && button8.equals(button3)){
			return true;
		}else	if(button24.equals(button19)&& button19.equals(button14) && button14.equals(button9)){
			return true;
		}else	if(button19.equals(button14)&& button14.equals(button9)  && button9.equals(button4)){
			return true;
		}else	if(button1.equals(button8)  && button8.equals(button15)  && button15.equals(button22)){
			return true;
		}else	if(button8.equals(button15) && button15.equals(button22) && button22.equals(button29)){
			return true;
		}else	if(button1.equals(button2)  && button2.equals(button3)   && button3.equals(button4)){
			return true;
		}else	if(button2.equals(button9)  && button9.equals(button16)  && button16.equals(button23)){
			return true;
		}else	if(button5.equals(button10) && button10.equals(button15) && button15.equals(button20)){
			return true;
		}else	if(button10.equals(button15)&& button15.equals(button20) && button20.equals(button25)){
			return true;
		}else	if(button11.equals(button16)&& button16.equals(button21) && button21.equals(button26)){
			return true;
		}
			//no one has won the game yet
			return false;
	}
	public void changePlayer(){
		//if it was red make it blues turn 
		if(playerRedTrun){
			playerRedTrun = false;
			instructionText.setForeground(Color.BLUE);
			instructionText.setText("====================Blue's Turn!====================");
		}else{
			playerRedTrun = true;
			instructionText.setForeground(Color.RED);
			instructionText.setText("====================Red's Turn!=======================");
		}
	}

  // Main method to start our program
  public static void main(String[] args){
    // Creates an instance of our program
    Main gui = new Main();
    // Lets the computer know to start it in the event thread
    SwingUtilities.invokeLater(gui);
  }
}
