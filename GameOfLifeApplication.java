import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.JFrame;
import java.lang.Math;

public class GameOfLifeApplication extends JFrame implements Runnable, MouseListener
{
	private static final Dimension WindowSize = new Dimension(800,800);
	private BufferStrategy strategy;
	private boolean cells[][][];
	private boolean graphicsIsInitialised = false; //paint method will only run if isRunning is true
	private boolean gameState = false;
	private int gameBuffer;
	private int neighboursAlive = 0;
	
	public GameOfLifeApplication()
	{	
		this.setTitle("Game Of Life"); //set title
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int x = screensize.width/2 - screensize.width/2;//create JFrame
		int y = screensize.height/2 - screensize.height/2;
		setBounds(x,y,WindowSize.width,WindowSize.height);
		setVisible(true);
		
		cells = new boolean[40][40][2]; //creates a boolean array for each 20x20 cell on the screen
		
		createBufferStrategy(2); // double buffering to prevent flickering
		strategy = getBufferStrategy();
		
		Thread t = new Thread(this);//create a thread
		t.start();//start a thread
		
		addMouseListener(this); //adds mouse listener
		
		gameBuffer = 0;
		graphicsIsInitialised = true; //boolean set to true here to prevent errors the first time paint is called
	}
	
	public void mousePressed(MouseEvent e)
	{ 
		int x = e.getX()/20; //gets the coordinates of mouse click and divides by 20 to find the cell
		int y = e.getY()/20;
		
		cells[x][y][0] = !cells[x][y][0]; //flips the boolean value of the cell
		
		if(! gameState)
		{
			x = e.getX();
			y = e.getY();
			if(x>=50 && x<=125 && y>=50 && y<= 100)
			{
				gameState = true;
			}
			else if(x>=200 && x<=430 && y>=50 && y<=100)//if random is clicked the game board is randomised.
			{
				for(int i=0;i<40;i++)
				{
					for(int j=0;j<40;j++)
					{
						double rand = (Math.floor(Math.random() * 10))%2;
						if(rand==1)
						{
							cells[i][j][0] = false;
						}
						else if(rand==0)
						{
							cells[i][j][0] = true;
						}
					}
				}
			}
		}
		this.repaint();
	}
	public void mouseReleased(MouseEvent e){} //following methods must be implemented for MouseListener
	
	public void mouseEntered(MouseEvent e) {}
	
	public void mouseExited(MouseEvent e) {}
	
	public void mouseClicked(MouseEvent e) {}
	
	public void run()
	{
		while(1>0)
		{
			try 
			{
				Thread.sleep(250);//thread sleeps for this amount of time
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
			this.repaint();//calls paint method
		}
	}
	
	public void paint(Graphics g)
	{
		if(graphicsIsInitialised)//paint only runs if isRunning is true
		{	
			g = strategy.getDrawGraphics();
			
			for(int i=0;i<40;i++)
			{
				for(int j=0;j<40;j++)//paints each cell black or white depending on boolean value
				{
					if(cells[i][j][0])
					{
						g.setColor(Color.white);
						g.fillRect(i*20,j*20,20,20);
					}
					else
					{
						g.setColor(Color.black);
						g.fillRect(i*20, j*20, 20, 20);
					}
				}
			}
			
			if(! gameState) //creates menu buttons when game is not running.
			{
				Font f = new Font( "Times", Font.PLAIN,30);
				g.setFont(f);
				String start = "Start";
				String rand = "Random";
				g.setColor(Color.green);
				g.fillRect(50, 50, 75, 50);
				g.setColor(Color.white);
				g.drawString(start, 50, 75);
				g.setColor(Color.green);
				g.fillRect(200, 50, 130, 50);
				g.setColor(Color.white);
				g.drawString(rand, 200, 75);
			}
			else if(gameState)
			{
				for (int x=0;x<40;x++)
				{
					for (int y=0;y<40;y++)
					{
						neighboursAlive = 0;
						// count the live neighbors of cell [x][y][0]
						for (int xx=-1;xx<=1;xx++) 
						{
							for (int yy=-1;yy<=1;yy++) 
							{
								if (xx!=0 || yy!=0) 
								{
									if(cells[Math.floorMod(x+xx,40)][Math.floorMod(y+yy,40)][0])
									{
										neighboursAlive++;
									}
								}
							}
						}
						if(neighboursAlive<2 && cells[x][y][0])
						{
							cells[x][y][1] = false;
						}
						else if((neighboursAlive==2 || neighboursAlive==3) && cells[x][y][0])
						{
							cells[x][y][1] = true;
						}
						else if(neighboursAlive>3 && cells[x][y][0])
						{
							cells[x][y][1] = false;
						}
						else if(neighboursAlive==3 && !cells[x][y][0])
						{
							cells[x][y][1] = true;
						}
					}
				}
				for(int i=0;i<40;i++)
				{
					for(int j=0;j<40;j++)//paints each cell black or white depending on boolean value
					{
						cells[i][j][0] = cells[i][j][1];
					}
				}
			}
			strategy.show();
		}
	}
	
	public static void main(String[] args)
	{
		GameOfLifeApplication gol = new GameOfLifeApplication();
	}
}