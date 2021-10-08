package com.company;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Main extends GameEngine {
    boolean left, right;
    boolean gameOver;
    boolean fire;

    double bulletX, bulletY;
    double bulletVX, bulletVY;
    double playerX, playerY;
    double playerVX, playerVY;
    int alienInitX = 150;
    int alienInitY = 5;

    int lives = 3;
    int score = 0;

    // Image variables
    Image backgroundImg;
    Image planet1;
    Image planet2;
    Image galaxy;
    Image playerIMG;
    Image enemyIMG;
    Image menuIMG;
    Image enemyImg;

    // Audio variables
    AudioClip titleTheme;
    AudioClip battleTheme;
    AudioClip deathTheme;
    AudioClip shootSound;
    AudioClip alienDeathSound;
    AudioClip explosionSound;

    // Width and height
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
        playerIMG = subImage(shipSheet, 0, 0, 18, 20);
        enemyIMG = subImage(shipSheet, 10, 60, 10, 10);
    }
    public void loadAudio(){
        deathTheme = loadAudio("src/Mission Over.wav");
        titleTheme = loadAudio("src/space invder sound/9. Space Invaders.wav");
        battleTheme = loadAudio("src/space invder sound/9. Space Invaders.wav");
        shootSound = loadAudio("src/space invder sound/shoot.wav");
        alienDeathSound = loadAudio("src/space invder sound/invaderkilled.wav");
        explosionSound = loadAudio("src/space invder sound/explosion.wav");

    }

    // Player
    public void initPlayer() {
        playerX = screenW * 0.5;
        playerY = screenH - 100.0;
    }

    public void updatePlayer(double dt) {
        playerX += playerVX * dt;
        playerY += playerVY * dt;
        if (playerX >= screenW || playerY >= screenH) {
            gameOver = true;
        }
        if (playerX <= 0 || playerY <= 0){
            gameOver = true;
        }
        if (left) {
            playerX = playerX - 10;
        }
        if (right) {
            playerX = playerX + 10;
        }

    }

    public void drawPlayer() {
        drawImage(playerIMG, playerX, playerY, 50, 50);
    }
    // Bullet
    public void initBullet() {
        playAudio(shootSound);
        bulletX = playerX;
        bulletY = playerY;
    }

    public void updateBullet(double dt) {
        bulletY = bulletY-10;
        // Allows to only fire 1 bullet until bullets left the screen
        if(bulletY <= 0){
            fire = false;
        }
    }

    public void drawBullet() {
        changeColor(white);
        drawSolidCircle(bulletX,bulletY,5);
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
        drawText(screenW-150, 30, "Score: "+ score, "Font.PLAIN", 12);
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
        if(fire) {
            drawBullet();
        }
    }

    @Override
    // Main game methods
    public void init() {
        playAudio(battleTheme);
        fire = false;
        gameOver = false;
        loadImages();
        loadAudio();
        setWindowSize(screenW, screenH);
        initPlayer();
        initAliens();
    }

    @Override
    public void update(double dt) {
        updatePlayer(dt);
        updateAliens();
        drawText(screenW-150, 30, "Score: "+ score, "Font.PLAIN", 12);
        if(fire){
            updateBullet(dt);
        }
        if (gameOver) {
            playerVX = 0;
            playerVY = 0;
            drawMenu();
        }
        if(lives == 0){
            gameOver = true;
        }
    }

    @Override
    public void paintComponent() {
        if(gameOver){
            drawMenu();
        }
        else {
            playGame();
        }
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
        if (event.getKeyCode() == KeyEvent.VK_SPACE) {
            if(!fire) {
                fire = true;
                initBullet();
            }
        }
        // Play game
        if (event.getKeyCode() == KeyEvent.VK_P) {
            gameOver = false;
            clearBackground(screenW,screenH);
            init();
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
