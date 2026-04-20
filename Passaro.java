import javax.swing.ImageIcon;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import java.util.List;

public class Passaro {
    Image[] frames;
    int frameAtual = 0;
    String[] tipos = { "papel", "plastico", "vidro", "metal", "organico", "hospitalar" };
    Color[] cores = { Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.BLACK, Color.WHITE };

    public static Image[][] imagensLixo = {
            // PAPEL (10 imagens)
            {
                    new ImageIcon("Image/papel/cadernopapel.png").getImage(),
                    new ImageIcon("Image/papel/caixapapel.png").getImage(),
                    new ImageIcon("Image/papel/caixapapel2.png").getImage(),
                    new ImageIcon("Image/papel/envelopepapel.png").getImage(),
                    new ImageIcon("Image/papel/folhapapel1.png").getImage(),
                    new ImageIcon("Image/papel/folhapapel2.png").getImage(),
                    new ImageIcon("Image/papel/jornalpapel.png").getImage(),
                    new ImageIcon("Image/papel/revistapapel.png").getImage(),
                    new ImageIcon("Image/papel/rolopapel.png").getImage(),
            },

            // PLÁSTICO
            {
                    new ImageIcon("Image/plastico/canudoplastico.png").getImage(),
                    new ImageIcon("Image/plastico/copoplastico.png").getImage(),
                    new ImageIcon("Image/plastico/garraplastico1.png").getImage(),
                    new ImageIcon("Image/plastico/garraplastico2.png").getImage(),
                    new ImageIcon("Image/plastico/garraplastico3.png").getImage(),
                    new ImageIcon("Image/plastico/garraplastico4.png").getImage(),
                    new ImageIcon("Image/plastico/garraplastico5.png").getImage(),
                    new ImageIcon("Image/plastico/poteplastico.png").getImage(),
                    new ImageIcon("Image/plastico/pratoplastico.png").getImage(),
                    new ImageIcon("Image/plastico/sacoplastico.png").getImage()
            },
            {
                    new ImageIcon("Image/Vidro/copovidro.png").getImage(),
                    new ImageIcon("Image/Vidro/frascovidro1.png").getImage(),
                    new ImageIcon("Image/Vidro/frascovidro2.png").getImage(),
                    new ImageIcon("Image/Vidro/frascovidro3.png").getImage(),
                    new ImageIcon("Image/Vidro/garrafavidro2.png").getImage(),
                    new ImageIcon("Image/Vidro/garrafavidro3.png").getImage(),
                    new ImageIcon("Image/Vidro/garrafavidro4.png").getImage(),
                    new ImageIcon("Image/Vidro/garrafavirdro1.png").getImage(),
                    new ImageIcon("Image/Vidro/taçavidro.png").getImage(),
                    new ImageIcon("Image/Vidro/vasilhavidro1.png").getImage(),
                    new ImageIcon("Image/Vidro/vasilhavidro2.png").getImage()

            },

            {
                    new ImageIcon("Image/Metal/aramemetal.png").getImage(),
                    new ImageIcon("Image/Metal/clipemetal.png").getImage(),
                    new ImageIcon("Image/Metal/facametal.png").getImage(),
                    new ImageIcon("Image/Metal/garfometal.png").getImage(),
                    new ImageIcon("Image/Metal/latametal.png").getImage(),
                    new ImageIcon("Image/Metal/latametal2.png").getImage(),
                    new ImageIcon("Image/Metal/latametal3.png").getImage(),
                    new ImageIcon("Image/Metal/latametal4.png").getImage(),
                    new ImageIcon("Image/Metal/pregometal.png").getImage(),
                    new ImageIcon("Image/Metal/pregometal.png").getImage()
            },
            {
                    new ImageIcon("Image/Organico/cascaorg1.png").getImage(),
                    new ImageIcon("Image/Organico/cascaorg2.png").getImage(),
                    new ImageIcon("Image/Organico/cascaorg3.png").getImage(),
                    new ImageIcon("Image/Organico/cascaorg4.png").getImage(),
                    new ImageIcon("Image/Organico/org5.png").getImage(),
            },
            {
                    new ImageIcon("Image/Hospitalar/atadurahosp.png").getImage(),
                    new ImageIcon("Image/Hospitalar/curativohosp.png").getImage(),
                    new ImageIcon("Image/Hospitalar/gazehosp.png").getImage(),
                    new ImageIcon("Image/Hospitalar/luvashosp.png").getImage(),
                    new ImageIcon("Image/Hospitalar/mascarahosp.png").getImage(),
                    new ImageIcon("Image/Hospitalar/seringahosp.png").getImage(),
            },
    };

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

        frames = new Image[12];
        int index = 0;
        for (int linha = 0; linha < 3; linha++) {
            for (int col = 0; col < 4; col++) {
                frames[index++] = new ImageIcon(
                        "Image/corvo/frame_" + linha + "_" + col + ".png").getImage();
            }
        }
        int tipoIndex = (int) (Math.random() * tipos.length);
        Image[] imagensDoTipo = imagensLixo[tipoIndex];
        Image imagemEscolhida = imagensDoTipo[(int) (Math.random() * imagensDoTipo.length)];

        lixoCarregado = new Lixo(
                tipos[tipoIndex],
                x,
                y,
                imagemEscolhida);
        pontoSoltar = 100 + (int) (Math.random() * 800);
    }

    void atualizar() {
        x += velocidade;

        frameAtual = (frameAtual + 1) % frames.length;

        y += Math.sin(x * 0.05) * 2;

        if (!soltou && x > pontoSoltar) {
            lixoCarregado.x = x + 30;
            lixoCarregado.y = y + 40;
            lixos.add(lixoCarregado);
            soltou = true;
        }
    }

    void desenhar(Graphics g) {

        g.drawImage(
                frames[frameAtual],
                x + 80, y,
                -80, 80,
                null);

        if (!soltou) {
            g.drawImage(lixoCarregado.imagem, x + 30, y + 40, 60, 60, null);

            // g.setColor(lixoCarregado.cor);
            // g.fillRect(x + 30, y + 40, 20, 20);
        }
    }
}
