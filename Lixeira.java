import java.awt.*;

public class Lixeira {
    String tipo;
    int x, y;
    int largura, altura;
    Image imagem;
    
    // Feedback visual (0: neutro, 1: acerto, 2: erro)
    int feedback = 0;
    int tempoFeedback = 0;

    /**
     * Construtor atualizado para suportar dimensionamento dinâmico
     */
    public Lixeira(String tipo, int x, int y, int largura, int altura, Image imagem) {
        this.tipo = tipo;
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.imagem = imagem;
    }

    /**
     * Retorna a área de colisão da lixeira.
     * Isso permite que o JogoReciclagem verifique colisões sem "chutar" o tamanho da lixeira.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, largura, altura);
    }

    /**
     * Desenha a lixeira e o texto centralizado
     */
    public void desenhar(Graphics g) {
        // Desenha a imagem da lixeira usando as dimensões dinâmicas
        g.drawImage(imagem, x, y, largura, altura, null);

        // Configuração para centralizar o texto acima ou dentro da lixeira
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        int textoX = x + (largura - fm.stringWidth(tipo)) / 2;
        int textoY = y - 5; // Posiciona o texto 5 pixels acima da lixeira
        
        g.drawString(tipo.toUpperCase(), textoX, textoY);
        
        // Lógica de feedback visual (opcional: desenhar borda colorida se feedback > 0)
        if (tempoFeedback > 0) {
            if (feedback == 1) g.setColor(Color.GREEN);
            else if (feedback == 2) g.setColor(Color.RED);
            
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(x, y, largura, altura);
            tempoFeedback--;
        }
    }
}