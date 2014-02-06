package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;


public class Window extends JFrame implements KeyListener , ActionListener
{
	private final Timer timer = new Timer( 30 , this );
	
	public ArrayList<Point> player;
	public int lastDir = 3;
	public int dir = 3;
	
	public int[][] tiles;
	
	public int movementTimer = 0;
	
	public int grow = 0;
	
	public int gameStatus = 0;
	
	public int topBarDecal;
	
	public Window()
	{
		initGame();
		
		setSize( 320 , 340 );
		setResizable(false);
		setVisible( true );
		
		timer.start();
		
		this.addKeyListener(this);
		
		JFrame frame = this;
		JMenuBar menubar = frame.getJMenuBar();
		int mbh = (menubar != null ? menubar.getSize().height : 0);
		Insets insets = frame.getInsets();
		
		topBarDecal = insets.top + insets.bottom + mbh;
	}
	
	public void initGame()
	{
		gameStatus = 0;
		player = new ArrayList();
		player.add( new Point(0,0) );
		tiles = new int[30][30];
	}
	
	public void movePlayer()
	{
		int dx = 0;
		int dy = 0;
		
		if ( dir == 1 ) { dx =  0 ; dy = -1 ; }
		if ( dir == 2 ) { dx =  1 ; dy =  0 ; }
		if ( dir == 3 ) { dx =  0 ; dy =  1 ; }
		if ( dir == 4 ) { dx = -1 ; dy =  0 ; }
		
		Point head = player.get( player.size()-1 );
		
		player.add( new Point( head.x+dx , head.y+dy ) );
		
		if ( grow != 0 )
			grow -= 1;
		else
			player.remove(0);
		
		head = player.get( player.size()-1 );
		
		if ( head.x < 0 ) head.x += tiles.length;
		if ( head.y < 0 ) head.y += tiles[0].length;
		if ( head.x >= tiles.length ) head.x -= tiles.length;
		if ( head.y >= tiles[0].length ) head.y -= tiles[0].length;
		
		for ( int i = 0 ; i < player.size()-2 ; i++ )
		{
			Point p = player.get(i);
			if ( p.x == head.x && p.y == head.y )
			{
				gameStatus = -1;
				break;
			}
		}
		
		if ( tiles[head.x][head.y] == 1 )
		{
			tiles[head.x][head.y] = 0;
			grow += 1;
		}
	}
	
	public void updatePlayer()
	{
		lastDir = dir;
		
		movementTimer += 1;
		if ( movementTimer > 1 )
		{
			movePlayer();
			movementTimer = 0;
		}
	}
	
	public void updateWorld()
	{
		if ( Math.random() > 0.9 )
		{
			int x = (int)( Math.random()*tiles.length );
			int y = (int)( Math.random()*tiles[0].length );
			tiles[x][y] = 1;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		//This is the main update
		
		if ( gameStatus == 1 )
		{
			updatePlayer();
			
			updateWorld();
		}
		else if ( gameStatus == -1 )
			initGame();
		
		this.repaint();
	}
	
	@Override
	public void paint( Graphics g )
	{
		g.setColor(Color.WHITE);
		g.fillRect( 0 , 0 , 400 , 400 );
		
		//draw tiles
		g.setColor( Color.RED );
		for ( int x = 0 ; x < tiles.length ; x++ )
			for ( int y = 0 ; y < tiles.length ; y++ )
				if ( tiles[x][y] == 1 )
					g.fillRect( x*10 + 10 , y*10 + topBarDecal + 10 , 10 , 10 );
		
		//drawBorders
		//top
		g.setColor( Color.GRAY );
		for ( int i = 0 ; i < tiles.length+2 ; i++ )
			g.fillRect( i*10 + 0 , topBarDecal , 10 , 10 );
		//bottom
		for ( int i = 0 ; i < tiles.length+2 ; i++ )
			g.fillRect( i*10 + 0 , topBarDecal+tiles[0].length*10 + 10 , 10 , 10 );
		//left
		for ( int i = 0 ; i < tiles.length+1 ; i++ )
			g.fillRect( 0 , i*10 + topBarDecal , 10 , 10 );
		//right
		for ( int i = 0 ; i < tiles.length+1 ; i++ )
			g.fillRect( 10+tiles[0].length*10 , i*10 + topBarDecal , 10 , 10 );
		
		//drawPlayer
		g.setColor( Color.black );
		for ( Point p : player )
			g.fillRect( p.x*10 + 10 , p.y*10 + topBarDecal + 10 , 10 , 10 );
		
		if ( gameStatus == 0 )
			g.drawString( "Press space to play" , 100 , 100 );
	}
	
	@Override
	public void keyPressed(KeyEvent ke)
	{
		int key = ke.getKeyCode();
		
		if ( gameStatus == 1 )
		{
			if ( key >= 37 && key <= 40 )
			{
				if      ( key == 38 ) dir = 1;
				else if ( key == 39 ) dir = 2;
				else if ( key == 40 ) dir = 3;
				else if ( key == 37 ) dir = 4;

				if ( Math.abs(dir-lastDir) == 2 )
					dir = lastDir;
			}
		}
		else if ( gameStatus == 0 )
			if ( key == 32 )
				gameStatus = 1;
	}
	
	@Override
	public void keyReleased(KeyEvent ke) {}
	
	@Override
	public void keyTyped(KeyEvent ke) {}
}
