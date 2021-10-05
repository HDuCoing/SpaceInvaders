package com.company;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Main extends GameEngine {
    boolean left, right;
    boolean gameOver;
    boolean fire;

    double playerX, playerY;
    double playerVX, playerVY;

    // Init variables
    Image backgroundImg;
    Image planet1;
    Image planet2;
    Image galaxy;
    Image playerIMG;
    Image enemyIMG;
    Image menuIMG;

    Image enemyImg;
    int screenW = 1000;
    int screenH = 600;

    public static void main(String[] args) {
        createGame(new Main());
    }

    public void loadImages() {
        backgroundImg = loadImage("src/space_background.png");
        menuIMG = loadImage("src/menu_Background.png");
        Image planetSheet = loadImage("src/Planets.png");
        Image shipSheet = loadImage("src/SpaceShipAsset.png");
        planet1 = subImage(planetSheet, 0, 0, 64, 64);
        planet2 = subImage(planetSheet, 63, 63, 64, 64);
        galaxy = subImage(planetSheet, 260, 100, 120, 120);
        playerIMG = subImage(shipSheet, 0, 0, 10, 10);
        enemyIMG = subImage(shipSheet, 10, 60, 10, 10);
    }

    // Player
    public void initPlayer() {
        playerX = screenW * 0.5;
        playerY = screenH - 100.0;
    }

    public void updatePlayer(double dt) {
        playerX += playerVX * dt;
        playerY += playerVY * dt;
        if (playerX >= screenW || playerX <= 0) {
            gameOver = true;
            drawMenu();
        }
        if (left) {
            playerX = playerX - 5;
        }
        if (right) {
            playerX = playerX + 5;
        }

    }

    public void drawPlayer() {
        drawImage(playerIMG, playerX, playerY, 50, 50);

    }

    // Bullet
    public void initBullet() {

    }

    public void updateBullet() {

    }

    public void drawBullet() {

    }

    // Aliens
    public void initAliens() {

    }

    public void updateAliens() {

    }

    public void drawAliens() {

    }

    // Menu
    public void drawMenu() {
        clearBackground(screenW, screenH);
        drawImage(menuIMG, 0, 0, screenW, screenH);
        changeColor(white);
        drawText(350, 100, "Space Invaders");
        drawText(50, screenH * 0.5, "P to Play Game");
        drawText(50, screenH * 0.5 + 50, "Q to Quit");
    }

    public void playGame() {
        // Set up background
        clearBackground(screenW, screenH);
        drawImage(backgroundImg, 0, 0, screenW, screenH);
        drawImage(galaxy, screenW - 175.0, 200, 300, 300);
        drawImage(planet1, 0, 0, 150, 150);
        drawImage(planet2, screenW - 100.0, 0, 100, 100);
        // Draws player/effects/aliens
        drawPlayer();
        drawAliens();
        if (fire) {
            drawBullet();
        }
    }

    @Override
    // Main game methods
    public void init() {
        fire = false;
        loadImages();
        setWindowSize(screenW, screenH);
        initPlayer();
        initAliens();
        if (fire) {
            initBullet();
        }
    }

    @Override
    public void update(double dt) {
        if (gameOver) {
            drawMenu();
        }
        updatePlayer(dt);
        updateAliens();
        if (fire) {
            updateBullet();
        }
    }

    @Override
    public void paintComponent() {
        playGame();
    }

    // KeyPressed Event Handler
    public void keyPressed(KeyEvent event) {
        // Left Arrow
        if (event.getKeyCode() == KeyEvent.VK_LEFT || event.getKeyCode() == KeyEvent.VK_A) {
            left = true;
        }
        // Right Arrow
        if (event.getKeyCode() == KeyEvent.VK_RIGHT || event.getKeyCode() == KeyEvent.VK_D) {
            right = true;
        }
        if (event.getKeyCode() == KeyEvent.VK_F) {
            fire = true;
        }
        // Play game
        if (event.getKeyCode() == KeyEvent.VK_P) {
            playGame();
        }
        // Quit
        if (event.getKeyCode() == KeyEvent.VK_Q) {
            mFrame.setVisible(false);
        }
        }
        // KeyReleased Event Handler
        public void keyReleased (KeyEvent event){
            // Left Arrow
            if (event.getKeyCode() == KeyEvent.VK_LEFT || event.getKeyCode() == KeyEvent.VK_A) {
                left = false;
            }

            // Right Arrow
            if (event.getKeyCode() == KeyEvent.VK_RIGHT || event.getKeyCode() == KeyEvent.VK_D) {
                right = false;
            }
        }
    }
