package com.company;

import java.awt.Rectangle;
import java.io.Serializable;

// this class can be much improved, better encapsulation
// draw itself, update itself etc. etc.
public class Alien{
    public final double created;
    double x;
    double y;

    public Alien(double created, double x, double y) {
        this.created = created;
        this.x = x;
        this.y = y;
    }
}
