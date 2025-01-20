import java.util.*;

public class Jogo {
    private int nivelAtual;
    private int moedas;

    private List<String> palavrasAdivinhadas;
    private Map<Integer, Nivel> niveis;

    public Jogo() {
        this.nivelAtual = 1;
        this.moedas = 0;
        this.palavrasAdivinhadas = new ArrayList<>();
        this.niveis = new HashMap<>();
    }

    public void iniciarJogo(){
        System.out.println("O jogo começou!");
    }


}
