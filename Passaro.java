import javax.swing.ImageIcon;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import java.util.List;

public class Passaro {
    Image[] frames;
    int frameAtual = 0;
    Color[] cores = { Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.PINK, Color.BLACK, Color.WHITE };
    String[] tipos = { "papel", "plastico", "vidro", "metal", "organico", "Madeira", "hospitalar" };
    int pontoSoltar;
    int x, y;
    int velocidade;
    Lixo lixoCarregado;
    boolean soltou = false;
    List<Lixo> lixos;

    Passaro(int x, int y, List<Lixo> lixos) {
        this.x = x;
        this.y = y;
        this.lixos = lixos;
        velocidade = 2 + (int) (Math.random() * 3);

        // frames
        frames = new Image[12];
        int index = 0;
        for (int linha = 0; linha < 3; linha++) {
            for (int col = 0; col < 4; col++) {
                frames[index++] = new ImageIcon(
                        "Image/corvo/frame_" + linha + "_" + col + ".png").getImage();
            }
        }

        // 🔥 cria lixo que ele vai carregar
        int i = (int) (Math.random() * tipos.length);
        lixoCarregado = new Lixo(
                tipos[i],
                x,
                y,
                cores[i]);

        // 🔥 define ponto aleatório da tela
        pontoSoltar = 100 + (int) (Math.random() * 800);
    }

    void atualizar() {
        x += velocidade;

        // animação
        frameAtual = (frameAtual + 1) % frames.length;

        // flutuação
        y += Math.sin(x * 0.05) * 2;

        // 🔥 SOLTAR LIXO no meio da tela
        if (!soltou && x > pontoSoltar) {
            lixoCarregado.x = x + 30;
            lixoCarregado.y = y + 40;
            lixos.add(lixoCarregado); // adiciona no jogo
            soltou = true;
        }
    }

    void desenhar(Graphics g) {
        // pássaro
        g.drawImage(
                frames[frameAtual],
                x + 80, y,
                -80, 80,
                null);

        // 🔥 desenha lixo enquanto está carregando
        if (!soltou) {
            g.setColor(lixoCarregado.cor);
            g.fillRect(x + 30, y + 40, 20, 20);
        }
    }
}
