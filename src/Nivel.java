import java.util.*;

public class Nivel {
    private List<Character> letrasDisponiveis;
    private List<Palavra> palavrasCorretas;

    public Nivel(List<Character> letrasDisponiveis, List<Palavra> palavras) {
        this.letrasDisponiveis = letrasDisponiveis;
        this.palavrasCorretas = palavras;
    }

    public boolean verificarPalavra(Palavra palavra) {
        return palavrasCorretas.contains(palavra);
    }

    public List<Character> getLetrasDisponiveis() {
        return letrasDisponiveis;
    }

    public List<Palavra> getPalavrasCorretas() {
        return palavrasCorretas;
    }
}

