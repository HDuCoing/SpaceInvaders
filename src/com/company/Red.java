package com.company;

import javax.swing.ImageIcon;

public class Red extends Main {
    private Shot shot;
    private int xdirection = 1;
    private int ydirection = -1;
    public Red(int x, int y) {

        initAlienr(x, y);
    }

    public void initAlienr(int x, int y) {

        this.x = x;
        this.y = y;

        shot = new Shot(x, y);

        var redImg = "src/red.png";
        var explodeImg = "src/explode.png";
        var ri = new ImageIcon(redImg);
        var ex = new ImageIcon(explodeImg);
        //setImage(ri.getImage());
        //setImage(ex.getImage());
    }

    public void act(int xdirection, int ydirection) {
        this.x += xdirection;
        this.y += ydirection;
    }
    private void explode() {
        g.drawexplosion(ex.getImage, ex.);
        setDestroyed(true);

    }
    public void setDestroyed(boolean destroyed) {

        this.destroyed = destroyed;
    }
}