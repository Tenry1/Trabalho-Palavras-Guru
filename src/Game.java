import java.util.*;
import java.io.*;

public class Game {
    private int currentLevel;
    private Economy economy; // Instância de Economy para gerenciar moedas
    private List<String> guessedWords; // Lista de palavras adivinhadas
    private Map<Integer, Level_interface> levels;

    public Game() {
        this.currentLevel = 1;
        this.economy = new Economy(); // Inicializa a economia com 0 moedas
        this.guessedWords = new ArrayList<>(); // Inicializa a lista de palavras adivinhadas
        this.levels = new HashMap<>();
    }

    // Retorna a instância de Economy para manipular moedas
    public Economy getEconomy() {
        return economy;
    }

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
                            levels.put(Integer.valueOf(levelNumber), level); // Armazena o nível no mapa
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
                    levels.put(Integer.valueOf(levelNumber), level);
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
                levels.put(Integer.valueOf(i), level);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar o dicionário: " + e.getMessage());
            System.exit(1); // Encerra o programa com código de erro 1
        }
    }

    public Map<Integer, Level_interface> getLevels() {
        return this.levels;
    }

    public int getCurrentLevel() {return this.currentLevel;}

    public List<String> getGuessedWords() {
        return this.guessedWords;
    }

    public void loadGame() {
        File saveFile = new File("save.txt");
        if (saveFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(saveFile))) {
                String line;
                String availableLetters = "";
                guessedWords.clear(); // Limpa as palavras adivinhadas antes de carregar novas

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("NUM_MOEDAS: ")) {
                        int coins = Integer.parseInt(line.substring("NUM_MOEDAS: ".length()));
                        economy.setCoins(coins);
                    } else if (line.startsWith("NIVEL_ATUAL: ")) {
                        currentLevel = Integer.parseInt(line.substring("NIVEL_ATUAL: ".length()));
                    } else if (line.startsWith("LETRAS_DISPONIVEIS: ")) {
                        availableLetters = line.substring("LETRAS_DISPONIVEIS: ".length());
                    } else if (line.startsWith("PALAVRAS_A_ADIVINHAR:")) {
                        while ((line = reader.readLine()) != null) {
                            if (!line.trim().isEmpty()) {
                                guessedWords.add(line.trim().toUpperCase());
                            }
                        }
                    }
                }

                // Verifica se os níveis foram carregados ou gerados
                if (levels.isEmpty()) {
                    System.out.println("Nenhum nível carregado. Tentando carregar níveis...");
                    loadLevels(); // Tenta carregar os níveis pré-feitos
                }

                // Verifica se o nível atual existe no mapa de níveis
                Level_interface currentLevelObj = levels.get(currentLevel);
                if (currentLevelObj == null) {
                    System.err.println("Erro ao carregar: Nível atual não encontrado no mapa de níveis.");
                    return;
                }

                // Atualiza o estado do nível atual com as informações carregadas
                if (currentLevelObj instanceof Level_prefab) {
                    updateLevelState((Level_prefab) currentLevelObj, availableLetters, guessedWords);
                } else if (currentLevelObj instanceof Level_gen) {
                    updateLevelState((Level_gen) currentLevelObj, availableLetters, guessedWords);
                } else {
                    System.err.println("Erro ao carregar: Nível atual não encontrado no mapa de níveis.");
                }

                System.out.println("Jogo carregado com sucesso!");
            } catch (IOException e) {
                System.err.println("Erro ao carregar o jogo: " + e.getMessage());
            }
        } else {
            System.out.println("Arquivo de save não encontrado. Iniciando novo jogo...");
        }
    }

    public void saveGame() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("save.txt"))) {
            // Salva o número de moedas
            writer.println("NUM_MOEDAS: " + economy.getCoins());

            // Salva o nível atual
            writer.println("NIVEL_ATUAL: " + currentLevel);

            // Salva os níveis concluídos
            StringBuilder completedLevels = new StringBuilder("NIVEIS_CONCLUIDOS: ");
            for (int level : levels.keySet()) {
                Level_interface levelObj = levels.get(level);
                if (levelObj != null && hasCompletedLevel(levelObj)) {
                    completedLevels.append(level).append(",");
                }
            }
            writer.println(completedLevels.toString().replaceAll(",$", "")); // Remove a última vírgula

            // Salva as letras disponíveis no nível atual
            Level_interface currentLevelObj = levels.get(currentLevel);
            if (currentLevelObj != null) {
                writer.println("LETRAS_DISPONIVEIS: " + currentLevelObj.getAvailableLetters());
            }

            // Salva as palavras adivinhadas em cada nível
            writer.println("PALAVRAS_A_ADIVINHAR:");
            for (int level : levels.keySet()) {
                Level_interface levelObj = levels.get(level);
                if (levelObj != null) {
                    writer.println("NIVEL_" + level + ":");
                    for (String word : levelObj.getSelectedWords()) {
                        if (guessedWords.contains(word.toUpperCase())) {
                            writer.println(word.toUpperCase());
                        }
                    }
                }
            }

            System.out.println("Jogo guardado com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao guardar o jogo: " + e.getMessage());
        }
    }

    // Metodo auxiliar para verificar se um nível foi concluído
    private boolean hasCompletedLevel(Level_interface level) {
        List<String> selectedWordsUpper = new ArrayList<>();
        for (String word : level.getSelectedWords()) {
            selectedWordsUpper.add(word.toUpperCase());
        }
        for (String word : selectedWordsUpper) {
            if (!guessedWords.contains(word)) {
                return false;
            }
        }
        return true;
    }

    private void updateLevelState(Level_prefab level, String availableLetters, List<String> guessedWords) {
        level.getAvailableCharacters().clear();
        for (char c : availableLetters.replaceAll(" ", "").toCharArray()) {
            level.getAvailableCharacters().put(c, level.getAvailableCharacters().getOrDefault(c, 0) + 1);
        }
    }

    private void updateLevelState(Level_gen level, String availableLetters, List<String> guessedWords) {
        level.getAvailableCharacters().clear();
        for (char c : availableLetters.replaceAll(" ", "").toCharArray()) {
            level.getAvailableCharacters().put(c, level.getAvailableCharacters().getOrDefault(c, 0) + 1);
        }
    }

}
