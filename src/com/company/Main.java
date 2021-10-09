package com.company;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main extends GameEngine {
    boolean left, right;
    boolean gameOver;
    boolean fire;
    boolean destroyed;
    boolean hit;
    int bossHealth = 10;
    boolean enemyFire;

    double bulletX, bulletY;
    double playerX, playerY;
    double playerVX, playerVY;
    double bossX, bossY;
    double alienX, alienY;
    double enemyBulletX, enemyBulletY;

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
    Image bossIMG;
    int screenW = 1000;
    int screenH = 600;

    // Audio variables
    AudioClip titleTheme;
    AudioClip battleTheme;
    AudioClip deathTheme;
    AudioClip shootSound;
    AudioClip alienDeathSound;
    AudioClip explosionSound;
    AudioClip bossTheme;

    private List<Alien> alienList;

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
        enemyIMG = subImage(shipSheet, 20, 45, 15, 10);
        bossIMG = loadImage("src/boss.png");
    }
    public void loadAudio(){
        deathTheme = loadAudio("src/space invder sound/Mission Over.wav");
        titleTheme = loadAudio("src/space invder sound/5. Romance on Neon.wav");
        battleTheme = loadAudio("src/space invder sound/9. Space Invaders.wav");
        shootSound = loadAudio("src/space invder sound/shoot.wav");
        alienDeathSound = loadAudio("src/space invder sound/invaderkilled.wav");
        explosionSound = loadAudio("src/space invder sound/explosion.wav");
        bossTheme = loadAudio("src/space invder sound/11. Flanger Party.wav");
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

    public void updateBullet() {
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
    public void initAliens(){
        alienX = 400;
        alienY = 300;
        enemyFire = true;
        hit = false;
        destroyed = false;
    }
    public void updateAliens(){
        // collision here
        if(hit){
            alienList.remove(0);
            hit = false;
            score=score+1;
        }
        for (Alien alien : alienList) {
            alien.y = alien.y+0.5;
            for(double i=alien.x;i<alien.x+25;i++){
                for(double j=alien.y;j<alien.y+25;j++){
                    if (bulletX == i && bulletY == j) {
                        fire = false;
                        hit = true;
                        playAudio(explosionSound);
                    }
                    if(alien.y >= screenH){
                        lives = lives-1;
                    }
                }
            }
        }
        if(alienList.isEmpty()){
            destroyed = true;
        }
    }
    public void drawAliens(){
        if(!destroyed) {
            // draw the aliens
            for (Alien alien : alienList) {
                drawImage(enemyIMG, alien.x, alien.y, 45, 45);
            }
        }
    }
    // Boss level ending
    public void initBoss() {
        bossX = 450;
        bossY = 100;
        enemyFire = true;
        hit = false;
        enemyBulletX = bossX;
        enemyBulletY = bossY;
    }

    public void updateBoss() {
        if(enemyBulletY <= screenH){
            enemyFire = false;
        }
        if(enemyFire){
            enemyBulletY = enemyBulletY-3;
        }
    }

    public void drawBoss() {
        drawImage(bossIMG, bossX, bossY, 150,150);
        if(enemyFire){
            changeColor(red);
            drawCircle(enemyBulletX, enemyBulletY, 20);
        }
    }
    // Menu
    public void drawMenu() {
        stopAudioLoop(battleTheme);
        stopAudioLoop(bossTheme);
        startAudioLoop(titleTheme);
        clearBackground(screenW, screenH);
        drawImage(menuIMG, 0, 0, screenW, screenH);
        changeColor(white);
        drawText(350, 100, "Space Invaders");
        drawText(50, screenH * 0.5, "P to Play Game");
        drawText(50, screenH * 0.5 + 50, "Q to Quit");
        drawText(screenW-150, 30, "Score: "+ score, "Font.PLAIN", 16);
    }

    public void playGame() {
        // Set up background
        stopAudioLoop(titleTheme);
        stopAudioLoop(bossTheme);
        clearBackground(screenW, screenH);
        drawImage(backgroundImg, 0, 0, screenW, screenH);
        drawImage(galaxy, screenW - 175.0, 200, 300, 300);
        drawImage(planet1, 0, 0, 150, 150);
        drawImage(planet2, screenW - 100.0, 0, 100, 100);
        drawPlayer();
        drawAliens();
        if(fire) {
            drawBullet();
        }
    }

    @Override
    // Main game methods
    public void init() {
        this.alienList = new ArrayList<>();
        fire = false;
        gameOver = false;
        loadImages();
        loadAudio();
        startAudioLoop(battleTheme);
        setWindowSize(screenW, screenH);
        initPlayer();
        initAliens();
        if(alienList.isEmpty()){
            for(int i=0;i<10;i++){
                int randomNum = ThreadLocalRandom.current().nextInt(50, screenW-50 + 1);
                alienList.add(new Alien(10,randomNum,0));
            }
        }
    }

    @Override
    public void update(double dt) {
        updatePlayer(dt);
        updateAliens();
        drawText(screenW-150, 30, "Score: "+ score, "Font.PLAIN", 12);
        if(alienList.isEmpty()) {
            initBoss();
            updateBoss();
        }
        if(fire){
            updateBullet();
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
        if(alienList.isEmpty()) {
            drawBoss();
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
