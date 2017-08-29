package sample;

import javafx.scene.image.Image;


/**
 * Created by Mihai on 8/28/2017.
 */
public class Card {

    private String sign;
    private String number;
    private Image image;

    public Card(String number, String sign, Image image) {
        this.sign = sign;
        this.number = number;
        this.image = image;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
