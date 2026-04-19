import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Lixeira {
    String tipo;
    int x;
    Image imagem;
    int feedback = 0;
    int tempoFeedback = 0;

    Lixeira(String tipo, int x, Image imagem, int feedback, int tempoFeedback) {
        this.tipo = tipo;
        this.x = x;
        this.imagem = imagem;
        this.feedback = feedback;
        this.tempoFeedback = tempoFeedback;
    }
}
