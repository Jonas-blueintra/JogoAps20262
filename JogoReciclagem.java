import javax.swing.*;
import javax.swing.text.html.HTMLDocument.Iterator;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class JogoReciclagem extends JPanel implements ActionListener, MouseListener, MouseMotionListener {

    Image background;
    String[] tipos = { "papel", "plastico", "vidro", "metal", "organico", "Madeira", "hospitalar" };
    Color[] cores = { Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.PINK, Color.BLACK, Color.WHITE };
    Image[] imagensLixeira = {
            new ImageIcon("Image/lixeira-papel.png").getImage(),
            new ImageIcon("Image/lixeira-plastico.png").getImage(),
            new ImageIcon("Image/lixeira-vidro.png").getImage(),
            new ImageIcon("Image/lixeira-metal.png").getImage(),
            new ImageIcon("Image/lixeira-organica.png").getImage(),
            new ImageIcon("Image/lixeira-madeira.png").getImage(),
            new ImageIcon("Image/lixeira-ambulantorias.png").getImage() };

    List<Passaro> passaros = new ArrayList<>();
    List<Lixo> lixos = new ArrayList<>();
    List<Lixeira> lixeiras = new ArrayList<>();
    List<ItemBonus> bonus = new ArrayList<>();
    List<PassaroBonus> passarosBonus = new ArrayList<>();
    int pontos = 0;
    int vidas = 55;
    int tempoSpawn = 0;
    int tempoMensagem = 0;
    int tempoJogo = 60;
    int frames = 0;
    boolean jogoIniciado = false;
    boolean gameOver = false;
    boolean venceu = false;
    Lixo lixoSelecionado = null;
    String mensagem = "";
    int feedback = 0; // 0 = nada, 1 = acerto, -1 = erro
    int tempoFeedback = 0;
    ItemBonus bonusSelecionado = null;
    int feedbackCesta = 0; // 1 = acerto
    int tempoFeedbackCesta = 0;
    // int alturaTela = getHeight();

    public JogoReciclagem() {

        background = new ImageIcon("Image/fundo.jpeg").getImage();

        for (int i = 0; i < tipos.length; i++) {
            lixeiras.add(new Lixeira(tipos[i], 0, imagensLixeira[i], 0, 0));
        }

        new javax.swing.Timer(30, this).start();

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void actionPerformed(ActionEvent e) {

        if (!jogoIniciado || gameOver || venceu) {
            repaint();
            return;
        }
        int alturaTela = getHeight();
        tempoSpawn++;
        frames++;

        // ⏱️ contador regressivo
        if (frames >= 30) {
            tempoJogo--;
            frames = 0;

            if (tempoJogo <= 0) {
                venceu = true;
            }
        }

        if (tempoMensagem > 0) {
            tempoMensagem--;
            if (tempoMensagem == 0)
                mensagem = "";
        }
        if (tempoFeedbackCesta > 0) {
            tempoFeedbackCesta--;
            if (tempoFeedbackCesta == 0) {
                feedbackCesta = 0;
            }
        }
        for (Lixeira lixeira : lixeiras) {
            if (lixeira.tempoFeedback > 0) {
                lixeira.tempoFeedback--;
                if (lixeira.tempoFeedback == 0) {
                    lixeira.feedback = 0;
                }
            }
        }
        if (Math.random() < 0.03) {
            passaros.add(new Passaro(-100, 50 + (int) (Math.random() * 200), lixos));
        }
        if (Math.random() < 0.01) {
            passarosBonus.add(new PassaroBonus(-100, 100, bonus));
        }
        for (Passaro p : passaros) {
            p.atualizar();
        }
        for (PassaroBonus p : passarosBonus) {
            p.atualizar();
        }
        java.util.Iterator<ItemBonus> itBonus = bonus.iterator();

        while (itBonus.hasNext()) {
            ItemBonus b = itBonus.next();

            b.y += 3;

            Rectangle r = new Rectangle(b.x, b.y, 20, 20);
            Rectangle cesta = new Rectangle(getWidth() / 2 - 100, getHeight() - 350, 200, 50);

            if (r.intersects(cesta)) {
                pontos += 2;
                vidas++;

                mensagem = "BONUS +2 pontos +1 vida!";
                tempoMensagem = 60;

                feedbackCesta = 1;
                tempoFeedbackCesta = 30;
                itBonus.remove();
            }

            if (b.y > getHeight()) {
                itBonus.remove();
            }
        }
        // remover quando sair da tela
        passaros.removeIf(p -> p.x > getWidth() + 100);
        java.util.Iterator<Lixo> it = lixos.iterator();

        while (it.hasNext()) {
            Lixo lixo = it.next();

            if (!lixo.arrastando) {
                lixo.y += 3;
            }

            if (verificarColisao(lixo)) {
                it.remove();
                continue;
            }

            if (lixo.y > getHeight() + 30) {
                vidas--;
                mensagem = "Perdeu lixo!";
                tempoMensagem = 60;

                it.remove();

                if (vidas <= 0)
                    gameOver = true;
            }
        }
        repaint();
    }

    boolean verificarColisao(Lixo lixo) {

        Rectangle lixoRect = new Rectangle(lixo.x, lixo.y, 30, 30);

        for (Lixeira lixeira : lixeiras) {

            int yLixeira = getHeight() - 180;

            Rectangle lixeiraRect = new Rectangle(
                    lixeira.x,
                    yLixeira,
                    190,
                    190);

            if (lixoRect.intersects(lixeiraRect)) {

                if (lixo.tipo.equals(lixeira.tipo)) {
                    pontos++;
                    mensagem = "Acertou!";
                    lixeira.feedback = 1;
                    lixeira.tempoFeedback = 30;
                } else {
                    pontos--;
                    vidas--;
                    mensagem = "Lixeira errada! -1 vida";
                    lixeira.feedback = -1;
                    lixeira.tempoFeedback = 30;

                    if (vidas <= 0)
                        gameOver = true;
                }

                tempoMensagem = 60;
                return true; // 🔥 só avisa que colidiu
            }
        }

        return false;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        if (background != null) {
            g.drawImage(background, 0, 0, w, h, this);
        }

        for (Passaro p : passaros) {
            p.desenhar(g);
        }
        for (PassaroBonus p : passarosBonus) {
            p.desenhar(g);
        }
        for (ItemBonus b : bonus) {
            g.setColor(Color.WHITE);
            g.fillOval(b.x, b.y, 20, 20);
        }
        if (!jogoIniciado) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("JOGO RECICLAGEM", w / 2 - 200, h / 2 - 50);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Clique para jogar", w / 2 - 100, h / 2);
            return;
        }

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("GAME OVER", w / 2 - 120, h / 2 - 50);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Clique para reiniciar", w / 2 - 120, h / 2);
            return;
        }

        if (venceu) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("VOCÊ VENCEU!", w / 2 - 150, h / 2 - 50);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Pontos: " + pontos, w / 2 - 50, h / 2);
            g.drawString("Clique para jogar novamente", w / 2 - 150, h / 2 + 40);
            return;
        }
        // 3. LIXOS
        for (Lixo lixo : lixos) {
            g.setColor(lixo.cor);
            g.fillRect(lixo.x, lixo.y, 30, 30);
        }

        Image check = new ImageIcon("Image/verifica.png").getImage();
        Image ximg = new ImageIcon("Image/x.png").getImage();

        int larguraLixeira = 190;
        int espacamento = 30; // espaço entre lixeiras

        int totalLixeiras = lixeiras.size();

        // largura total ocupada
        int larguraTotal = totalLixeiras * larguraLixeira + (totalLixeiras - 1) * espacamento;

        // ponto inicial pra centralizar
        int startX = (w - larguraTotal) / 2;

        for (int i = 0; i < lixeiras.size(); i++) {
            Lixeira lixeira = lixeiras.get(i);

            int x = startX + i * (larguraLixeira + espacamento);
            int y = h - 180;

            lixeira.x = x;
            g.setFont(new Font("Arial", Font.BOLD, 20));

            g.drawImage(lixeira.imagem, x, y, larguraLixeira, larguraLixeira, null);

            g.setColor(Color.WHITE);
            g.drawString(lixeira.tipo, x + 20, y + 20);

            // feedback
            if (lixeira.feedback != 0) {
                if (lixeira.feedback == 1) {
                    g.drawImage(check, x + 50, y + 50, 80, 80, null);
                } else {
                    g.drawImage(ximg, x + 50, y + 50, 80, 80, null);
                }
            }
        }
        int cestaX = w / 2 - 100;
        int cestaY = h - 350;

        g.setColor(Color.ORANGE);
        g.fillRect(cestaX, cestaY, 200, 50);
        g.setColor(Color.BLACK);
        g.drawString("BONUS", cestaX + 60, cestaY + 30);
        // 🔥 5. HUD (SEMPRE POR CIMA)
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));

        g.drawString("Pontos: " + pontos, 10, 20);
        g.drawString("Vidas: " + vidas, 10, 40);
        g.drawString("Tempo: " + tempoJogo, 10, 60);
        g.drawString(mensagem, w - 200, 20);

    }

    public void mousePressed(MouseEvent e) {

        if (!jogoIniciado) {
            jogoIniciado = true;
            return;
        }

        if (gameOver || venceu) {
            pontos = 0;
            vidas = 5;
            tempoJogo = 60;
            lixos.clear();
            gameOver = false;
            venceu = false;
            return;
        }

        for (Lixo lixo : lixos) {
            if (new Rectangle(lixo.x, lixo.y, 30, 30).contains(e.getPoint())) {
                lixoSelecionado = lixo;
                lixo.arrastando = true;
                break;
            }
        }

        for (ItemBonus b : bonus) {
            if (new Rectangle(b.x, b.y, 20, 20).contains(e.getPoint())) {
                bonusSelecionado = b;
                b.arrastando = true;
                return;
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (lixoSelecionado != null) {
            lixoSelecionado.x = e.getX() - 15;
            lixoSelecionado.y = e.getY() - 15;
        }

        if (bonusSelecionado != null) {
            bonusSelecionado.x = e.getX() - 10;
            bonusSelecionado.y = e.getY() - 10;
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (lixoSelecionado != null) {
            lixoSelecionado.arrastando = false;
            lixoSelecionado = null;
        }

        if (bonusSelecionado != null) {
            bonusSelecionado.arrastando = false;
            bonusSelecionado = null;
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Jogo de Reciclagem");
        JogoReciclagem jogo = new JogoReciclagem();

        frame.add(jogo);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}