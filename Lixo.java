import java.awt.*;

public class Lixo {
    String tipo;
    int x, y;
    Image imagem;
    boolean arrastando = false;
    boolean sendoPuxado = false;

    public Lixo(String tipo, int x, int y, Image imagem) {
        this.tipo = tipo;
        this.x = x;
        this.y = y;
        this.imagem = imagem;
    }
}
