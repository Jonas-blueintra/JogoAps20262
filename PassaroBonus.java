import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;

public class PassaroBonus {
    Image[] frames;
    int frameAtual = 0;
    int x, y;
    int velocidade;
    int pontoSoltar;
    boolean soltou = false;

    ItemBonus item;
    List<ItemBonus> bonus;

    PassaroBonus(int x, int y, List<ItemBonus> bonus) {
        this.x = x;
        this.y = y;
        this.bonus = bonus;

        velocidade = 3 + (int) (Math.random() * 3);

        // 🔥 carregar frames (1 até 19)
        frames = new Image[19];
        for (int i = 0; i < 19; i++) {
            frames[i] = new ImageIcon(
                    "Image/bem/spr_ababil_walk (" + (i + 1) + ").png").getImage();
        }

        item = new ItemBonus(x, y);

        pontoSoltar = 100 + (int) (Math.random() * 800);
    }

    void atualizar() {
        x += velocidade;
        frameAtual = (frameAtual + 1) % frames.length;

        if (!soltou && x > pontoSoltar) {
            item.x = x;
            item.y = y;
            bonus.add(item);
            soltou = true;
        }
    }

    void desenhar(Graphics g) {
        g.drawImage(frames[frameAtual], x, y, 80, 80, null);

        if (!soltou) {
            g.setColor(Color.WHITE);
            g.fillOval(x + 30, y + 40, 15, 15);
        }
    }
}