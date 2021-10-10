import javax.swing.ImageIcon;


public class Shot extends Sprite {

    private String shot = "/img/shot.png";
    private final int H_SPACE = 6;
    private final int V_SPACE = 1;
    Audio aud = new Audio();
    Audio.AudioClip shootSND = aud.loadAudio("space invder sound/shoot.wav");

    public Shot() {
    }

    public Shot(int x, int y) {
        aud.playAudio(shootSND);
        ImageIcon ii = new ImageIcon(this.getClass().getResource(shot));
        setImage(ii.getImage());
        setX(x + H_SPACE);
        setY(y - V_SPACE);
    }
}