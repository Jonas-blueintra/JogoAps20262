import java.awt.*;
import java.util.Random;

public class Confete {

    int x, y;
    double vx, vy;
    int vida = 120; // duração (~2 segundos)
    Color cor;

    Random rand = new Random();

    public Confete(int x, int y) {
        this.x = x;
        this.y = y;

        // velocidade aleatória (explosão)
        vx = rand.nextDouble() * 8 - 4; // -4 a +4
        vy = rand.nextDouble() * -20; // sobe

        // cor aleatória
        cor = new Color(
                rand.nextInt(255),
                rand.nextInt(255),
                rand.nextInt(255));
    }

    public void atualizar() {
        x += vx;
        y += vy;

        // gravidade (faz cair)
        vy += 0.3;

        vida--;
    }

    public boolean morreu() {
        return vida <= 0;
    }

    public void desenhar(Graphics g) {
        g.setColor(cor);
        g.fillRect(x, y, 5, 5);
    }
}