package com.company;

import javax.swing.ImageIcon;

public class Green extends Main {
    private Shot shot;
    private int xdirection = 1;
    private int ydirection = 1;

    public Green(int x, int y) {

        initAlieng(x, y);
    }

    private void initAlieng(int x, int y) {

        this.x = x;
        this.y = y;

        shot = new Shot(x, y);

        var greenImg = "src/images/green.png";
        var ii = new ImageIcon(greenImg);

        //setImage(ii.getImage());
    }

    public void act(int xdirection, int ydirection) {
        this.x += xdirection;
        this.y += ydirection;
    }

    public Shot getShot() {

        return shot;
    }
}
class Shot extends Main {

    private boolean destroyed;

    public Shot(int x, int y) {

        initShot(x, y);
    }

    private void initShot(int x, int y) {

        setDestroyed(true);

        this.x = x;
        this.y = y;

        var shotImg = "src/shot.png";
        var si = new ImageIcon(shotImg);
        //setImage(si.getImage());
    }

    public void setDestroyed(boolean destroyed) {

        this.destroyed = destroyed;
    }

}
