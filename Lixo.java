import java.awt.*;

public class Lixo {
    String tipo;
    int x, y;
    Color cor;
    boolean arrastando = false;

    Lixo(String tipo, int x, int y, Color cor) {
        this.tipo = tipo;
        this.x = x;
        this.y = y;
        this.cor = cor;
    }
}
