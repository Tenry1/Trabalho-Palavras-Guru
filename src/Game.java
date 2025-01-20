import java.util.*;
import java.io.*;

public class Game {
    private int currentLevel;
    private int coins;

    private List<String> guessedWords;
    private Map<Integer, Level> levels;

    public Game() {
        this.currentLevel = 1;
        this.coins = 0;
        this.guessedWords = new ArrayList<>();
        this.levels = new HashMap<>();
    }

    public void loadGame(){
        File saveFile = new File("save.txt");
        if (saveFile.exists()){
            try (BufferedReader reader = new BufferedReader(new FileReader(saveFile))) {
                currentLevel = Integer.parseInt(reader.readLine());
                coins = Integer.parseInt(reader.readLine());
                String line;
                while ((line = reader.readLine()) != null) {
                    guessedWords.add(line);
                }
                System.out.println("Jogo carregado com sucesso!");
            } catch (IOException e) {
                System.err.println("Erro ao carregar o jogo: " + e.getMessage());
            }
        }
        else {
            new Game();
        }
    }

    public void saveGame(){
        try (PrintWriter writer = new PrintWriter(new FileWriter("save.txt"))) {
            writer.println(currentLevel);
            writer.println(coins);
            for (String palavra : guessedWords) {
                writer.println(palavra);
            }
            System.out.println("Jogo guardado com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao guardar o jogo: " + e.getMessage());
        }
    }
}
