import java.awt.Graphics;
import java.awt.Image;

public class Nuvem {
    int x, y;
    int velocidade;
    Image imagem;

    int limiteEsquerda;
    int limiteDireita;

    public Nuvem(int x, int y, int velocidade, Image imagem) {
        this.x = x;
        this.y = y;
        this.velocidade = velocidade;
        this.imagem = imagem;
    }

    public void atualizar(int larguraTela) {

        int larguraNuvem = 200;

        int limiteEsquerda = larguraTela / 4;
        int limiteDireita = larguraTela - (larguraTela / 4);

        x += velocidade;

        // limite direito (considerando largura da nuvem)
        if (x + larguraNuvem > limiteDireita) {
            x = limiteDireita - larguraNuvem; // trava certinho
            velocidade *= -1;
        }

        // limite esquerdo
        if (x < limiteEsquerda) {
            x = limiteEsquerda; // trava certinho
            velocidade *= -1;
        }
    }

    public void desenhar(Graphics g) {
        g.drawImage(imagem, x, y, 200, 100, null);
    }
}