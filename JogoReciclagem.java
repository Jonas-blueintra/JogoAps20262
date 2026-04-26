import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class JogoReciclagem extends JPanel implements ActionListener, MouseListener, MouseMotionListener {

    Image background;
    String[] tipos = { "papel", "plastico", "vidro", "Especial", "metal", "organico", "hospitalar" };
    Color[] cores = { Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.PINK, Color.BLACK, Color.WHITE };
    Image[] imagensLixeira = {
            new ImageIcon("Image/lixeira-papel.png").getImage(),
            new ImageIcon("Image/lixeira-plastico.png").getImage(),
            new ImageIcon("Image/lixeira-vidro.png").getImage(),
            new ImageIcon("Image/cesta.png").getImage(),
            new ImageIcon("Image/lixeira-metal.png").getImage(),
            new ImageIcon("Image/lixeira-organica.png").getImage(),
            new ImageIcon("Image/lixeira-ambulantorias.png").getImage() };

    Image[] imagensLixeiradetalhes = {
            new ImageIcon("Image/lixeira-papel.png").getImage(),
            new ImageIcon("Image/lixeira-plastico.png").getImage(),
            new ImageIcon("Image/lixeira-vidro.png").getImage(),
            // new ImageIcon("Image/cesta.png").getImage(),
            new ImageIcon("Image/lixeira-metal.png").getImage(),
            new ImageIcon("Image/lixeira-organica.png").getImage(),
            new ImageIcon("Image/lixeira-ambulantorias.png").getImage() };

    Image[] cestaImage = {
            new ImageIcon("Image/cesta.png").getImage(),
    };

    List<Passaro> passaros = new ArrayList<>();
    List<Lixo> lixos = new ArrayList<>();
    List<Lixeira> lixeiras = new ArrayList<>();
    List<ItemBonus> bonus = new ArrayList<>();
    List<PassaroBonus> passarosBonus = new ArrayList<>();
    List<Confete> confetes = new ArrayList<>();
    List<Nuvem> nuvens = new ArrayList<>();
    Image imagemNuvem = new ImageIcon("Image/Nuvem.png").getImage();
    int pontos = 0;
    int vidas = 5;
    int tempoSpawn = 0;
    int tempoMensagem = 0;
    double tempoJogo = 0;
    int tempoMax = 0;
    int frames = 0;
    Lixo lixoSelecionado = null;
    String mensagem = "";
    int feedback = 0;
    int tempoFeedback = 0;
    ItemBonus bonusSelecionado = null;
    int feedbackCesta = 0;
    int tempoFeedbackCesta = 0;
    String dificuldade = "MÉDIO";
    int tempoSelecionado = 60;
    Point mousePos = new Point(0, 0);
    Image coracaoCheio = new ImageIcon("Image/coracao.png").getImage();
    Image coracaoVazio = new ImageIcon("Image/coracao_vazio.png").getImage();
    Image imgIma = new ImageIcon("Image/ima.png").getImage();
    int hudW = 210;
    int hudH = 110;
    boolean imaAtivo = false;
    int tempoIma = 0;
    int usosIma = 0;

    enum EstadoJogo {
        MENU, JOGANDO, FIM, DETALHES
    }

    EstadoJogo estado = EstadoJogo.MENU;

    Lixeira encontrarLixeira(String tipo) {
        for (Lixeira l : lixeiras) {
            if (l.tipo.equalsIgnoreCase(tipo)) {
                return l;
            }
        }
        return null;
    }

    public JogoReciclagem() {

        Image img = new ImageIcon("Image/mao-mostrando-o-contorno-da-palma.png").getImage();
        Image cursorImg = img.getScaledInstance(32, 32, Image.SCALE_SMOOTH);

        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg,
                new Point(0, 0),
                "cursor"));
        // cria 4 nuvens no meio
        background = new ImageIcon("Image/fundo.jpeg").getImage();
        nuvens.add(new Nuvem(400, 250, 1, imagemNuvem));
        nuvens.add(new Nuvem(700, 150, 2, imagemNuvem));
        nuvens.add(new Nuvem(1000, 90, 1, imagemNuvem));
        nuvens.add(new Nuvem(1300, 20, 2, imagemNuvem));
        for (int i = 0; i < tipos.length; i++) {
            lixeiras.add(new Lixeira(tipos[i], 0, 0, 0, 0, imagensLixeira[i]));
        }

        new javax.swing.Timer(30, this).start();

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (imaAtivo) {

            tempoIma--;

            for (Lixo lixo : lixos) {

                if (usosIma <= 0)
                    break;

                Lixeira destino = encontrarLixeira(lixo.tipo);

                if (destino != null) {

                    int alvoX = destino.x + 80;
                    int alvoY = getHeight() - 120;

                    // distância até o alvo
                    int diffX = alvoX - lixo.x;
                    int diffY = alvoY - lixo.y;

                    // 🎯 PASSO 1: alinhar no X primeiro
                    if (Math.abs(diffX) > 5) {
                        lixo.x += diffX * 0.2;
                    } else {
                        // 🎯 PASSO 2: depois desce reto
                        lixo.y += diffY * 0.2;
                    }
                    // chegou perto = coleta automática
                    if (Math.abs(lixo.x - alvoX) < 10 && Math.abs(lixo.y - alvoY) < 10) {

                        pontos++;
                        usosIma--;

                        lixos.remove(lixo);
                        break;
                    }
                }
            }

            if (tempoIma <= 0 || usosIma <= 0) {
                imaAtivo = false;
            }
        }
        // SEMPRE atualizar nuvens (menu incluso)
        for (Nuvem n : nuvens) {
            n.atualizar(getWidth());
        }

        if (estado != EstadoJogo.JOGANDO) {
            repaint();
            return;
        }
        tempoSpawn++;
        frames++;

        if (frames >= 30) {
            tempoJogo--;
            frames = 0;

            if (tempoJogo <= 0) {
                estado = EstadoJogo.FIM;
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
        // for (Nuvem n : nuvens) {
        // n.atualizar(getWidth());
        // }

        for (Lixeira lixeira : lixeiras) {
            if (lixeira.tempoFeedback > 0) {
                lixeira.tempoFeedback--;
                if (lixeira.tempoFeedback == 0) {
                    lixeira.feedback = 0;
                }
            }
        }
        double chance = 0.02;
        double quantidade = 10;
        if (dificuldade.equals("FÁCIL"))
            chance = 0.01;
        quantidade = 5;
        if (dificuldade.equals("DIFÍCIL"))
            chance = 0.05;
        quantidade = 15;
        if (passaros.size() < quantidade && Math.random() < chance) {
            passaros.add(new Passaro(-100, 50 + (int) (Math.random() * 200), lixos));
        }
        if (passarosBonus.size() < 1 && Math.random() < 0.01) {
            passarosBonus.add(new PassaroBonus(-100, 100, lixos));
        }
        for (Passaro p : passaros) {
            p.atualizar();
        }
        for (PassaroBonus p : passarosBonus) {
            p.atualizar();
        }
        java.util.Iterator<Confete> itConfete = confetes.iterator();

        while (itConfete.hasNext()) {
            Confete c = itConfete.next();
            c.atualizar();

            if (c.morreu()) {
                itConfete.remove();
            }
        }
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
                    estado = EstadoJogo.FIM;
            }
        }
        repaint();
    }

    boolean verificarColisao(Lixo lixo) {

        // 🔥 BLOQUEIO TOTAL
        if (lixo.sendoPuxado) {
            return false;
        }

        Rectangle lixoRect = new Rectangle(lixo.x, lixo.y, 30, 30);

        Rectangle lixeiraRect = new Rectangle(
            lixeiras.x,
            getHeight() - lixeiras.altura, // Dinâmico com base na altura
            lixeiras.largura,
            lixeiras.altura
        );

            for (Lixeira lixeira : lixeiras) { // O nome aqui é 'lixeira'
            // USANDO O MÉTODO NOVO: 
            // Em vez de criar o Rectangle na mão com valores fixos, usamos o da lixeira
            if (lixoRect.intersects(lixeira.getBounds())) { 

                if (lixo.tipo.equals(lixeira.tipo)) {
                    pontos++;
                    mensagem = "Acertou!";
                    lixeira.feedback = 1; // Ativa o feedback de acerto
                    lixeira.tempoFeedback = 30;
                } else {
                    pontos--;
                    vidas--;
                    mensagem = "Lixeira errada!";
                    lixeira.feedback = 2; // Ativa o feedback de erro
                    lixeira.tempoFeedback = 30;
                }
                return true; 
                }
                return false;
            }
            

            // 🔥 TRATAR LIXO ESPECIAL (CESTA)
            if (lixo.tipo.equalsIgnoreCase("especial")) {

                Rectangle cesta = new Rectangle(
                        getWidth() / 2 - 100,
                        getHeight() - 100,
                        200,
                        50);

                if (lixoRect.intersects(cesta)) {
                    int cestaX = getWidth() / 2;
                    int cestaY = getHeight() - 100;

                    for (int i = 0; i < 300; i++) {
                        confetes.add(new Confete(cestaX, cestaY));
                    }
                    pontos += 2;
                    vidas++;
                    imaAtivo = true;
                    tempoIma = 300; // dura ~10 segundos
                    usosIma = 5; // vai puxar 5 lixos
                    mensagem = "BONUS +2 pontos +1 vida!";
                    tempoMensagem = 60;

                    feedbackCesta = 1;
                    tempoFeedbackCesta = 30;
                    if (vidas > 5)
                        hudW += 30;
                    return true;
                }

                return false; // não testa nas lixeiras
            }
            if (lixoRect.intersects(lixeiraRect)) {

                if (lixo.tipo.equals(lixeiras.tipo)) {
                    pontos++;
                    mensagem = "Acertou!";
                    lixeiras.feedback = 1;
                    lixeira.tempoFeedback = 30;
                } else {
                    pontos--;
                    vidas--;
                    mensagem = "Lixeira errada! -1 vida";
                    lixeira.feedback = -1;
                    lixeira.tempoFeedback = 30;

                    if (vidas <= 0)
                        estado = EstadoJogo.FIM;
                }

                tempoMensagem = 60;
                return true; // 🔥 só avisa que colidiu
            }
        }

        return false;
    }

    void desenharBotao(Graphics2D g2, String texto, Rectangle r, boolean hover) {

        // fundo com gradiente
        GradientPaint grad = new GradientPaint(
                r.x, r.y,
                hover ? new Color(100, 255, 150) : new Color(0, 200, 100), // topo
                r.x, r.y + r.height,
                hover ? new Color(0, 180, 80) : new Color(0, 120, 60) // base
        );
        g2.setPaint(grad);
        g2.fillRoundRect(r.x, r.y, r.width, r.height, 20, 20);

        // borda
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(r.x, r.y, r.width, r.height, 20, 20);

        // texto centralizado
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g2.getFontMetrics();

        int textX = r.x + (r.width - fm.stringWidth(texto)) / 2;
        int textY = r.y + ((r.height - fm.getHeight()) / 2) + fm.getAscent();

        g2.drawString(texto, textX, textY);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();
        g.drawImage(background, 0, 0, w, h, null);
        // 🔥 AVISO DO IMÃ (sempre por cima de tudo)
        
        for (Nuvem n : nuvens) {
            n.desenhar(g);
        }

        // desenha nuvens
        // for (Nuvem n : nuvens) {
        // n.desenhar(g);
        // }

        if (estado == EstadoJogo.MENU) {
            Graphics2D g2 = (Graphics2D) g;

            // suaviza tudo (anti-alias)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // overlay escuro (tipo CSS rgba)
            g2.setColor(new Color(0, 0, 0, 120));
            g2.fillRect(0, 0, w, h);
            g2.setFont(new Font("Arial", Font.BOLD, 60));

            // sombra
            g2.setColor(Color.BLACK);
            g2.drawString("JOGO RECICLAGEM", w / 2 - 248, 152);

            // texto principal
            g2.setColor(Color.WHITE);
            g2.drawString("JOGO RECICLAGEM", w / 2 - 250, 150);

            Rectangle btnPlay = new Rectangle(w / 2 - 120, 220, 240, 50);
            Rectangle btnDetalhes = new Rectangle(w / 2 - 120, 290, 240, 50);
            Rectangle btnTempo = new Rectangle(w / 2 - 120, 360, 240, 50);
            Rectangle btnDificuldade = new Rectangle(w / 2 - 150, 430, 300, 50);

            // desenhar botões com hover
            desenharBotao(g2, "PLAY", btnPlay, btnPlay.contains(mousePos));
            desenharBotao(g2, "DETALHES", btnDetalhes, btnDetalhes.contains(mousePos));
            desenharBotao(g2, "TEMPO: " + tempoSelecionado + "s", btnTempo, btnTempo.contains(mousePos));
            desenharBotao(g2, "DIFICULDADE: " + dificuldade, btnDificuldade, btnDificuldade.contains(mousePos));

            return;
        }
        if (estado == EstadoJogo.DETALHES) {

            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, w, h);

            Graphics2D g2 = (Graphics2D) g;

            // fonte grande
            Font tituloFont = new Font("Arial", Font.BOLD, h / 18);
            g2.setFont(tituloFont);

            FontMetrics fm = g2.getFontMetrics();
            String titulo = "TIPOS DE LIXO";

            // centralizar de verdade (não no olho 👀)
            int xTitulo = (w - fm.stringWidth(titulo)) / 2;
            int yTitulo = h / 12;

            // sombra
            g2.setColor(new Color(0, 0, 0, 180));
            g2.drawString(titulo, xTitulo + 3, yTitulo + 3);

            // gradiente no texto
            GradientPaint gradTitulo = new GradientPaint(
                    xTitulo, yTitulo,
                    new Color(0, 255, 150),
                    xTitulo, yTitulo + 40,
                    new Color(0, 150, 80));
            g2.setPaint(gradTitulo);
            g2.drawString(titulo, xTitulo, yTitulo);

            int y = 90;

            for (int i = 0; i < imagensLixeiradetalhes.length; i++) {

                // 🗑️ LIXEIRA
                g.drawImage(imagensLixeiradetalhes[i], 100, y, 100, 100, null);

                // 🏷️ NOME
                g.setFont(new Font("Arial", Font.CENTER_BASELINE, 40));
                g.setColor(Color.WHITE);
                g.drawString(tipos[i], 220, y + 50);

                // 🧩 ITENS DO LIXO (lado direito)
                int startX = 400;
                int size = 70;
                int espacamento = 10;

                if (i < Passaro.imagensLixo.length) {

                    // largura disponível na tela
                    int larguraDisponivel = w - startX - 50;

                    // quantas imagens cabem por linha
                    int colunas = larguraDisponivel / (size + espacamento);

                    for (int j = 0; j < Passaro.imagensLixo[i].length; j++) {

                        int col = j % colunas;
                        int row = j / colunas;

                        int xItem = startX + col * (size + espacamento);
                        int yItem = y + row * (size + 10);

                        g.drawImage(Passaro.imagensLixo[i][j], xItem, yItem, size, size, null);
                    }
                }
                int larguraDisponivel = w - startX - 50;

                int colunas = larguraDisponivel / (size + espacamento);

                int linhas = (int) Math.ceil((double) Passaro.imagensLixo[i].length / colunas);
                y += Math.max(100, linhas * (size + 10) + 20);
            }
            g.drawImage(cestaImage[0], 100, y, 100, 100, null);
            g.setFont(new Font("Arial", Font.CENTER_BASELINE, 40));
            g.setColor(Color.WHITE);
            g.drawString("Especial", 220, y + 50);

            int startX = 400;
            int size = 80;
            int espacamento = 10;

            if (PassaroBonus.imagensBonus != null) {

                // largura disponível na tela
                int larguraDisponivel = w - startX - 50;

                // quantas imagens cabem por linha
                int colunas = larguraDisponivel / (size + espacamento);

                for (int j = 0; j < PassaroBonus.imagensBonus.length; j++) {

                    int col = j % colunas;
                    int row = j / colunas;

                    int xItem = startX + col * (size + espacamento);
                    int yItem = y + row * (size + 10);

                    g.drawImage(PassaroBonus.imagensBonus[j], xItem, yItem, size, size, null);
                }
            }

            Rectangle btnVoltar = new Rectangle(w / 2 - 150, h - 80, 300, 50);

            // reaproveita seu método!
            desenharBotao(g2, "VOLTAR", btnVoltar, btnVoltar.contains(mousePos));

            return;
        }

        if(estado == EstadoJogo.JOGANDO){
             Graphics2D g2 = (Graphics2D) g;
            // A. DESENHAR NUVENS [cite: 407]
            for (Nuvem n : nuvens) {
                n.desenhar(g);
            }
            // B. DIMENSIONAMENTO RESPONSIVO DAS LIXEIRAS
            int margem = 30;
            int espacamento = 15;
            int quantidade = lixeiras.size();

            if (quantidade > 0) {
            // Calcula a largura proporcional para que todas caibam na largura da tela 
            int larguraLixeira = (w - (2 * margem) - (espacamento * (quantidade - 1))) / quantidade;
            int alturaLixeira = (int) (larguraLixeira * 1.2); // Mantém a proporção vertical
            int yPos = h - alturaLixeira - 30; // 30px de margem do fundo

                for (int i = 0; i < quantidade; i++) {
                    Lixeira l = lixeiras.get(i);
                    
                    // Atualiza as propriedades dinâmicas antes de desenhar
                    l.largura = larguraLixeira;
                    l.altura = alturaLixeira;
                    l.x = margem + i * (larguraLixeira + espacamento);
                    l.y = yPos;

                    // Chama o método de desenho da própria lixeira
                    l.desenhar(g);
                }
            }

            // C. DESENHAR PÁSSAROS E LIXO ESPECIAL [cite: 424, 425]
            for (Passaro p : passaros) p.desenhar(g);
            for (PassaroBonus pb : passarosBonus) pb.desenhar(g);

            // D. DESENHAR LIXOS CAINDO/ARRASTANDO [cite: 430]
            for (Lixo lixo : lixos) {
                // Tamanho do lixo pode ser fixo ou levemente proporcional ao HUD
                g.drawImage(lixo.imagem, lixo.x, lixo.y, 50, 50, null);
            }

            // E. DESENHAR CONFETES (Bônus) [cite: 427]
            for (Confete c : confetes) {
                c.desenhar(g);
            }
            // Fundo do HUD [cite: 300]
            g2.setColor(new Color(0, 0, 0, 140));
            g2.fillRoundRect(10, 10, hudW, hudH, 20, 20);

            // Pontos e Vidas [cite: 301]
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            g2.drawString("PONTOS: " + pontos, 30, 40);

            // Desenhar corações para as vidas [cite: 388]
            for (int i = 0; i < 5; i++) {
                Image imgCoracao = (i < vidas) ? coracaoCheio : coracaoVazio;
                g2.drawImage(imgCoracao, 30 + (i * 35), 55, 30, 30, null);
            }

            // G. BARRA DE TEMPO [cite: 338, 339]
            int barraLargura = 180;
            int barraAltura = 15;
            int xBarra = 25;
            int yBarra = 95;
            double porcentagem = (double) tempoJogo / tempoSelecionado;
            int larguraAtual = (int) (barraLargura * porcentagem);
            g2.setColor(Color.DARK_GRAY);
            g2.fillRoundRect(xBarra, yBarra, barraLargura, barraAltura, 10, 10);
            g2.setColor(porcentagem > 0.3 ? Color.GREEN : Color.RED);
            g2.fillRoundRect(xBarra, yBarra, larguraAtual, barraAltura, 10, 10);

            // H. CAIXA DE MENSAGEM (FEEDBACK) [cite: 340-342]
            if (!mensagem.isEmpty()) {
                int msgW = 350;
                int msgX = (w - msgW) / 2;
                g2.setColor(new Color(0, 0, 0, 180));
                g2.fillRoundRect(msgX, 20, msgW, 40, 15, 15);
                g2.setColor(Color.CYAN);
                g2.drawString(mensagem, msgX + 20, 47);
            }
        }

        if (estado == EstadoJogo.FIM) {

            Graphics2D g2 = (Graphics2D) g;

            // suavização
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 🔳 overlay escuro (fundo)
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRect(0, 0, w, h);

            // 📦 painel central
            int boxW = 500;
            int boxH = 300;
            int boxX = (w - boxW) / 2;
            int boxY = (h - boxH) / 2;

            // fundo do painel
            g2.setColor(new Color(20, 20, 20, 220));
            g2.fillRoundRect(boxX, boxY, boxW, boxH, 30, 30);

            // borda estilo neon
            GradientPaint borda = new GradientPaint(
                    boxX, boxY,
                    new Color(0, 255, 150),
                    boxX + boxW, boxY + boxH,
                    new Color(0, 150, 80));
            g2.setPaint(borda);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(boxX, boxY, boxW, boxH, 30, 30);

            // 🎯 TÍTULO
            String titulo = "FIM DE JOGO";
            Font fontTitulo = new Font("Arial", Font.BOLD, 48);
            g2.setFont(fontTitulo);

            FontMetrics fmTitulo = g2.getFontMetrics();
            int xTitulo = boxX + (boxW - fmTitulo.stringWidth(titulo)) / 2;
            int yTitulo = boxY + 70;

            // sombra
            g2.setColor(Color.BLACK);
            g2.drawString(titulo, xTitulo + 3, yTitulo + 3);

            // cor principal
            g2.setColor(new Color(0, 255, 150));
            g2.drawString(titulo, xTitulo, yTitulo);

            // ⭐ PONTOS
            String textoPontos = "PONTOS: " + pontos;
            Font fontPontos = new Font("Arial", Font.BOLD, 28);
            g2.setFont(fontPontos);

            FontMetrics fmPontos = g2.getFontMetrics();
            int xPontos = boxX + (boxW - fmPontos.stringWidth(textoPontos)) / 2;
            int yPontos = boxY + 140;

            g2.setColor(Color.BLACK);
            g2.drawString(textoPontos, xPontos + 2, yPontos + 2);

            g2.setColor(Color.WHITE);
            g2.drawString(textoPontos, xPontos, yPontos);

            // 🔘 BOTÃO VOLTAR
            Rectangle btnVoltar = new Rectangle(boxX + 100, boxY + 190, 300, 60);

            desenharBotao(g2, "VOLTAR AO MENU", btnVoltar, btnVoltar.contains(mousePos));

            return;
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
        for (Confete c : confetes) {
            c.desenhar(g);
        }

        // 3. LIXOS
        for (Lixo lixo : lixos) {
            g.drawImage(lixo.imagem, lixo.x, lixo.y, 60, 60, null);
        }

        Image check = new ImageIcon("Image/verifica.png").getImage();
        Image ximg = new ImageIcon("Image/x.png").getImage();
        int larguraLixeira = 190;
        int espacamento = 30;
        int totalLixeiras = lixeiras.size();
        int larguraTotal = totalLixeiras * larguraLixeira + (totalLixeiras - 1) * espacamento;
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
        int cestaY = h - 100;

        if (feedbackCesta == 1) {
            g.drawImage(check, cestaX + 60, cestaY + 10, 80, 80, null);
        }

        Graphics2D g2 = (Graphics2D) g;

        // suavização
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 🔳 FUNDO DO PLACAR
        int hudX = 10;
        int hudY = 10;

        g2.setColor(new Color(0, 0, 0, 140)); // transparente
        g2.fillRoundRect(hudX, hudY, hudW, hudH, 20, 20);

        // borda
        g2.setColor(new Color(0, 255, 150));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(hudX, hudY, hudW, hudH, 20, 20);

        // 🔤 TEXTO
        // g2.setFont(new Font("Arial", Font.BOLD, 16));

        // // sombra texto
        // g2.setColor(Color.BLACK);
        // g2.drawString("PONTOS: " + pontos, hudX + 12, hudY + 28);
        // g2.drawString("VIDAS: " + vidas, hudX + 12, hudY + 50);
        // g2.drawString("TEMPO: " + tempoJogo, hudX + 12, hudY + 72);

        // texto principal
        g2.setColor(Color.WHITE);
        g2.drawString("PONTOS: " + pontos, hudX + 10, hudY + 26);
        int tamanho = 30;
        int espacamento2 = 5;

        int startX2 = hudX + 10;
        int startY = hudY + 40;

        // desenha corações

        for (int i = 0; i < vidas; i++) {

            int x = startX2 + i * (tamanho + espacamento2);

            if (i < vidas) {
                g2.drawImage(coracaoCheio, x, startY, tamanho, tamanho, null);
            } else {
                g2.drawImage(coracaoVazio, x, startY, tamanho, tamanho, null);
            }
        }
        int barraLargura = 200;
        int barraAltura = 20;

        int xBarra = 15;
        int yBarra = 90;

        // fundo (barra vazia)
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(xBarra, yBarra, barraLargura, barraAltura, 20, 20);

        // calcula porcentagem
        double porcentagem = tempoMax > 0 ? (double) tempoJogo / tempoMax : 0;

        int larguraAtual = (int) (barraLargura * porcentagem);

        // cor dinâmica (verde → amarelo → vermelho)
        if (porcentagem > 0.6) {
            g.setColor(new Color(0, 200, 0)); // verde
        } else if (porcentagem > 0.3) {
            g.setColor(new Color(255, 200, 0)); // amarelo
        } else {
            g.setColor(new Color(200, 0, 0)); // vermelho
        }

        // barra preenchida
        g.fillRoundRect(xBarra, yBarra, larguraAtual, barraAltura, 20, 20);

        // borda
        g.setColor(Color.WHITE);
        g.drawRoundRect(xBarra, yBarra, barraLargura, barraAltura, 20, 20);

        // texto em cima
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("Tempo", xBarra + 70, yBarra + 15);

        if (!mensagem.isEmpty()) {

            int msgW = 300;
            int msgH = 40;
            int msgX = w - msgW - 20;
            int msgY = 20;

            // fundo
            g2.setColor(new Color(0, 0, 0, 140));
            g2.fillRoundRect(msgX, msgY, msgW, msgH, 15, 15);

            // borda (verde ou vermelho dependendo)
            if (mensagem.contains("Acertou") || mensagem.contains("BONUS")) {
                g2.setColor(new Color(0, 255, 100));
            } else {
                g2.setColor(new Color(255, 80, 80));
            }

            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(msgX, msgY, msgW, msgH, 15, 15);

            // texto centralizado
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            FontMetrics fm = g2.getFontMetrics();

            int textX = msgX + (msgW - fm.stringWidth(mensagem)) / 2;
            int textY = msgY + ((msgH - fm.getHeight()) / 2) + fm.getAscent();

            // sombra
            g2.setColor(Color.BLACK);
            g2.drawString(mensagem, textX + 2, textY + 2);

            // texto
            g2.setColor(Color.WHITE);
            g2.drawString(mensagem, textX, textY);
        }

    }

    public void mousePressed(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        // MENU
        if (estado == EstadoJogo.MENU) {

            if (y >= 220 && y <= 260) {
                tempoJogo = tempoSelecionado;
                tempoMax = tempoSelecionado;
                estado = EstadoJogo.JOGANDO; // 🔥 ESSENCIAL
            }

            else if (y >= 280 && y <= 340) {
                estado = EstadoJogo.DETALHES;
            }

            else if (y >= 400 && y <= 450) {
                if (dificuldade.equals("FÁCIL"))
                    dificuldade = "MÉDIO";
                else if (dificuldade.equals("MÉDIO"))
                    dificuldade = "DIFÍCIL";
                else
                    dificuldade = "FÁCIL";
            } else if (y >= 340 && y <= 400) {
                if (tempoSelecionado == 30)
                    tempoSelecionado = 60;
                else if (tempoSelecionado == 60)
                    tempoSelecionado = 120;
                else
                    tempoSelecionado = 30;
            }
            repaint();
            return;
        }

        // DETALHES → voltar
        if (estado == EstadoJogo.DETALHES) {
            estado = EstadoJogo.MENU;
            return;
        }

        // FIM → reset
        if (estado == EstadoJogo.FIM) {

            Rectangle btnVoltar = new Rectangle(
                    getWidth() / 2 - 250,
                    getHeight() / 2 + 40,
                    300,
                    60);

            if (btnVoltar.contains(e.getPoint())) {
                estado = EstadoJogo.MENU;

                pontos = 0;
                vidas = 5;

                lixos.clear();
                passaros.clear();
                passarosBonus.clear();
                confetes.clear();
            }

            return;
        }

        // JOGO → pegar lixo
        if (estado == EstadoJogo.JOGANDO) {

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
        mousePos = e.getPoint();
        repaint();
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