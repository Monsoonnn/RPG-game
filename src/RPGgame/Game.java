
package RPGgame;

import Levels.LevelManager;
import States.GameSetting;
import States.Gamestate;
import States.Menu;
import States.Playing;
import UI.AudioSetings;
import entities.Player;
import entities.*;
import java.awt.Graphics;

public class Game implements Runnable{
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private Player player;
    
    private Playing playing;
    private Menu menu;
    private AudioSetings audio;
    private GameSetting gameSetting;
    private LevelManager levelManager;
    
    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 1.5f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
            
    public Game(){
        init();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        startGameLoop();
    }
    private void init() {
        audio = new AudioSetings();
        menu = new Menu(this);
	playing = new Playing(this);
        gameSetting = new GameSetting(this);
    }
        
    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    private void rePlay(){
        playing = new Playing(this);
    }
    public void update() {
        switch (Gamestate.state) {
		case MENU:
			menu.update();
			break;
		case PLAYING:
                    playing.update();
                    break;	
		case OPTIONS:
			gameSetting.update();
			break;
		case QUIT:
		default:
			System.exit(0);
			break;

		}
    }
    public void render(Graphics g){
                switch (Gamestate.state) {
		case MENU:
			menu.draw(g);
			break;
		case PLAYING:
			playing.draw(g);
			break;
		case OPTIONS:
			gameSetting.draw(g);
			break;
		default:
			break;
		}
       
    }
        @Override
	public void run() {

		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;

		long previousTime = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;

		while (true) {
			long currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if (deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}

			if (deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF--;
			}
			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				//System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;

			}
		}

	}        
        
    public void windowFocusLost() {
		if (Gamestate.state == Gamestate.PLAYING)
			playing.getPlayer().resetPosBoolean();
	}

	public Menu getMenu() {
		return menu;
	}

	public Playing getPlaying() {
		return playing;
	}
        public GameSetting getGameSetting() {
		return gameSetting;
	}
        public AudioSetings getAudioSetings(){
            return audio;
        }
}
