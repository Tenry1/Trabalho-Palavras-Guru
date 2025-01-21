import java.util.*;
import java.io.*;

public class Game {
    private int currentLevel;
    private int coins;
    private List<String> guessedWords; // Lista de palavras adivinhadas
    private Map<Integer, Level_interface> levels;

    public Game() {
        this.currentLevel = 1;
        this.coins = 0;
        this.guessedWords = new ArrayList<>(); // Inicializa a lista de palavras adivinhadas
        this.levels = new HashMap<>();
    }

    /* Coin related functions - move to dedicated class later */
    public int addCoins(int coinsToAdd) {
        this.coins += coinsToAdd;
        return this.coins;
    }

    public int removeCoins(int coinsToRemove) {
        this.coins -= coinsToRemove;
        return this.coins;
    }

    public int getCoins(){
        return this.coins;
    }

    /* Level related Functions*/

    public void loadLevels() {
        File levelFile = new File("src/ficheiro_niveis.txt");
        if (levelFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(levelFile))) {
                String line;
                List<String> levelWords = new ArrayList<>(); // Armazena as palavras de um nível
                int levelNumber = 1; // Contador para o número do nível

                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        // Linha em branco indica o fim de um nível
                        if (!levelWords.isEmpty()) {
                            Level_prefab level = Level_prefab.createFromWords(levelWords);
                            levels.put(levelNumber, level); // Armazena o nível no mapa
                            levelNumber++; // Incrementa o número do nível
                            levelWords.clear();
                        }
                    } else {
                        levelWords.add(line.trim());
                    }
                }

                // Adiciona o último nível, se houver
                if (!levelWords.isEmpty()) {
                    Level_prefab level = Level_prefab.createFromWords(levelWords);
                    levels.put(levelNumber, level);
                }

                System.out.println("Níveis carregados com sucesso!");
            } catch (IOException e) {
                System.err.println("Erro ao carregar os níveis: " + e.getMessage());
            }
        } else {
            System.err.println("Ficheiro de níveis não encontrado.");
        }
    }

    public void generateLevels() {
        try {
            Dictionary dictionary = new Dictionary("src/portuguese-large.txt"); // Carrega o dicionário
            Random random = new Random();

            // Gera um número aleatório de níveis (por exemplo, entre 1 e 4)
            int numberOfLevels = random.nextInt(4) + 1;
            System.out.println("Gerando " + numberOfLevels + " níveis...");

            // Limpa o mapa de níveis antes de gerar novos níveis
            levels.clear();

            for (int i = 1; i <= numberOfLevels; i++) {
                // Gera um novo nível usando a classe Level_gen
                Level_gen level = new Level_gen(dictionary);

                // Adiciona o nível ao mapa de níveis
                levels.put(i, level);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar o dicionário: " + e.getMessage());
            System.exit(1); // Encerra o programa com código de erro 1
        }
    }

    public Map<Integer, Level_interface> getLevels() {
        return this.levels;
    }

    public List<String> getGuessedWords() {
        return this.guessedWords;
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
