package com.theexceptionist.org;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import com.theexceptionist.org.input.Input;

public class GameMain extends Canvas implements Runnable{
	private static final long serialVersionUID = 525662174577943305L;
	private static final int WIDTH = 16*10, HEIGHT = 16 *10, SCALE = 4; 
	private static final String name = "Ancient Ruins";
	private boolean running = false;
	private Input input;
	private int currentState = 0;
	
	public GameMain(){
		input = new Input(this);
	}
	
	public void setState(int state){
		this.currentState = state;
	}
	
	public void resetState(){
		this.currentState = 0;
	}

	public void run() {
		long lastTime = System.nanoTime();
		double unprocessed = 0;
		double nsPerTick = 1000000000.0 / 60;
		int frames = 0;
		int ticks = 0;
		long lastTimer1 = System.currentTimeMillis();

		while (running) {
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			while (unprocessed >= 1) {
				ticks++;
				tick();
				unprocessed -= 1;
				shouldRender = true;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer1 > 1000) {
				lastTimer1 += 1000;
				System.out.println(ticks + " ticks, " + frames + " fps");
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	public void start(){
		running = true;
		new Thread(this).start();
	}
	
	public void stop(){
		running = false;
	}
	
	public void tick(){
		if(input.down.down){
			System.out.println("Hello");
		}
	}
	
	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.dispose();
		bs.show();
	}

	public static void main(String args[]){
		GameMain game = new GameMain();
		game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT* SCALE));
		game.setMaximumSize(new Dimension(WIDTH* SCALE, HEIGHT* SCALE));
		game.setPreferredSize(new Dimension(WIDTH* SCALE, HEIGHT* SCALE));

		JFrame frame = new JFrame(GameMain.name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(game, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		game.start();
	}
}
