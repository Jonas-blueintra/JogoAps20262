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

    Lixo lixoEspecial;
    List<Lixo> lixos;

    public static Image[] imagensBonus = {
            new ImageIcon("Image/Bonus/bonus1.png").getImage(),
            new ImageIcon("Image/Bonus/bonus2.png").getImage()
    };

    PassaroBonus(int x, int y, List<Lixo> lixos) {
        this.x = x;
        this.y = y;
        this.lixos = lixos;

        velocidade = 3 + (int) (Math.random() * 3);

        frames = new Image[19];
        for (int i = 0; i < 19; i++) {
            frames[i] = new ImageIcon(
                    "Image/bem/spr_ababil_walk (" + (i + 1) + ").png").getImage();
        }

        // 🔥 escolhe imagem aleatória
        Image img = imagensBonus[(int) (Math.random() * imagensBonus.length)];

        // 🔥 cria lixo especial
        lixoEspecial = new Lixo("especial", x, y, img);

        pontoSoltar = 100 + (int) (Math.random() * 800);
    }

    void atualizar() {
        x += velocidade;
        frameAtual = (frameAtual + 1) % frames.length;

        if (!soltou && x > pontoSoltar) {
            lixoEspecial.x = x + 30;
            lixoEspecial.y = y + 40;

            lixos.add(lixoEspecial);
            soltou = true;
        }
    }

    void desenhar(Graphics g) {
        g.drawImage(frames[frameAtual], x, y, 80, 80, null);

        if (!soltou) {
            g.drawImage(lixoEspecial.imagem, x + 30, y + 40, 30, 30, null);
        }
    }
}