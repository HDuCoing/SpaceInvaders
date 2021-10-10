package com.company;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main extends GameEngine {
    // Game booleans
    boolean inGame;
    boolean left, right;
    boolean gameOver;
    boolean fire;
    boolean destroyed;
    boolean hit;
    boolean hitPlayer;
    boolean enemyFire;
    boolean bossHit;
    double bulletX, bulletY;
    double playerX, playerY;
    double playerVX, playerVY;
    double bossX, bossY;
    double alienX, alienY;
    double enemyBulletX, enemyBulletY;
    // Conditions
    int bossHealth = 10;
    int lives = 3;
    int score = 0;
    // Image variables
    Image backgroundImg;
    Image planet1;
    Image planet2;
    Image galaxy;
    Image playerIMG;
    Image enemyIMG;
    Image gameOverIMG;
    Image bossIMG;
    Image winIMG;
    Image bulletIMG;
    Image enemyBulletIMG;
    Image explosionIMG;
    // Screen size
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
    public static void main(String[] args) {createGame(new Main());}

    public void loadImages() {
        backgroundImg = loadImage("src/space_background.png");
        gameOverIMG = loadImage("src/gameover.png");
        winIMG = loadImage("src/won.jpg");
        Image planetSheet = loadImage("src/Planets.png");
        Image shipSheet = loadImage("src/SpaceShipAsset.png");
        planet1 = subImage(planetSheet, 0, 0, 64, 64);
        planet2 = subImage(planetSheet, 63, 63, 64, 64);
        galaxy = subImage(planetSheet, 260, 100, 120, 120);
        playerIMG = subImage(shipSheet, 0, 0, 18, 20);
        enemyIMG = subImage(shipSheet, 20, 45, 15, 10);
        bossIMG = loadImage("src/boss.png");
        bulletIMG = loadImage("src/shot.png");
        enemyBulletIMG = loadImage("src/bomb.png");
        explosionIMG = loadImage("src/explosion.png");
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
    public void drawMenu() {
        clearBackground(screenW, screenH);
        // game over background
        if(gameOver) { drawImage(gameOverIMG, 0, 0, screenW, screenH); }
        // game won background
        if(!inGame){
            drawImage(backgroundImg, 0, 0, screenW, screenH);
        }
        else{ drawImage(winIMG, 0, 0, screenW, screenH); }
        changeColor(white);
        drawText(350, 100, "Space Invaders");
        drawText(50, screenH * 0.5, "P to Play Game");
        drawText(50, screenH * 0.5 + 50, "Q to Quit");
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
        drawImage(bulletIMG, bulletX, bulletY,20,20);
    }
    //enemy fire
    public void initEnemyFire(){
        enemyFire=true;
        enemyBulletX = rand(screenW);
        enemyBulletY = bossY;
    }
    public void updateEnemyFire(){
        enemyBulletY = enemyBulletY-5;
        if(enemyBulletY >= screenH){
            enemyFire = false;
            initEnemyFire();
        }
        if(enemyBulletX == playerX && enemyBulletY == playerY){
            playAudio(alienDeathSound);
            lives=lives-1;
        }
    }
    public void drawEnemyFire(){
        drawImage(enemyBulletIMG, enemyBulletX, enemyBulletY, 20,20);
      }
    // Aliens
    public void initAliens(){
        alienX = 400;
        alienY = 300;
        hit = false;
        destroyed = false;
        hitPlayer = false;
    }
    public void updateAliens(){
        // collision here
        if(hit){
            hit = false;
            alienList.remove(0);
            score=score+1;
        }
        if(hitPlayer){
            hitPlayer=false;
            lives = lives-1;
            alienList.remove(0);
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
                        hitPlayer=true;
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
        if(hit){
            drawImage(explosionIMG,bulletX,bulletY,30,30);
        }
    }
    // Boss level ending
    public void initBoss() {
        bossX = screenW*0.5;
        bossY = screenH*0.5;
        bossHit=false;
    }

    public void updateBoss() {
        bossHit=false;
        if (bossHit) {
            playAudio(explosionSound);
            fire=false;
            bossHealth = bossHealth - 1;
            bossHit = false;
        }
        for(double i=bossX;i<bossX+125;i++) {
            for(double j=bossX;j<bossX+125;j++) {
                if (bulletX == i && bulletY == j) {
                    bossHit = true;
                }
            }
        }
    }

    public void drawBoss() {
        drawImage(bossIMG, bossX, bossY, 150,150);
    }


    public void playGame() {
        // Set up background
        clearBackground(screenW, screenH);
        drawImage(backgroundImg, 0, 0, screenW, screenH);
        drawImage(galaxy, screenW - 175.0, 200, 300, 300);
        drawImage(planet1, 0, 0, 150, 150);
        drawImage(planet2, screenW - 100.0, 0, 100, 100);
        changeColor(white);
        drawText(screenW-120, 30, "Score: "+ score, "Font.BOLD", 18);
        drawText(screenW-120, 45, "Lives: "+ lives, "Font.BOLD", 18);
        drawPlayer();
        drawAliens();
        if(destroyed){
            drawBoss();
            drawText(screenW/2, 20, "BOSS HEALTH: "+ bossHealth, "Font.BOLD", 24);
        }
        if(fire) {
            drawBullet();
        }
        if(enemyFire){
            drawEnemyFire();
        }
    }

    @Override
    // Main game methods
    public void init() {
        setWindowSize(screenW, screenH);
        loadImages();
        loadAudio();
        startAudioLoop(titleTheme);
        gameOver=false;
        this.alienList = new ArrayList<>();
        drawMenu();
        if(inGame) {
            stopAudioLoop(titleTheme);
            fire = false;
            enemyFire = true;
            gameOver = false;
            initPlayer();
            initAliens();
            startAudioLoop(battleTheme);
            if (alienList.isEmpty()) {
                for (int i = 0; i < 10; i++) {
                    int randomNum = ThreadLocalRandom.current().nextInt(50, screenW - 50 + 1);
                    alienList.add(new Alien(10, randomNum, 0));
                    alienList.add(new Alien(10, randomNum, 50));
                    alienList.add(new Alien(10, randomNum, 120));
                }
            }
            if (destroyed) {
                initBoss();
                initEnemyFire();
            }
        }
    }

    @Override
    public void update(double dt) {
        updatePlayer(dt);
        updateAliens();
        if(enemyFire){
            updateEnemyFire();
        }
        if(destroyed) {
            updateBoss();
        }
        if(fire){
            updateBullet();
        }
        if(lives == 0){
            gameOver = true;
        }
        if(gameOver){
            stopAudioLoop(battleTheme);
        }
    }

    @Override
    public void paintComponent() {
        playGame();
        if (gameOver || bossHealth <= 0) {
            playerVX = 0;
            playerVY = 0;
            clearBackground(screenW,screenH);
            fire=false;
            bossHit=false;
            drawMenu();
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
            inGame = true;
            gameOver = false;
            clearBackground(screenW,screenH);
            score=0;
            lives=3;
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
