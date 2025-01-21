import java.util.*;

public class Guess {
    private final Level_interface level; // Current level
    private final Game game; // Reference to the game for updating coins and guessed words

    public Guess(Level_interface level, Game game) {
        this.level = level;
        this.game = game;
    }

    // Checks if a word can be formed with the available characters
    private boolean canFormWord(String word) {
        Map<Character, Integer> availableChars = level.getAvailableCharacters(); // Fetch available characters
        Map<Character, Integer> wordCharCount = getCharAmount(word); // Count characters in the word

        for (Map.Entry<Character, Integer> entry : wordCharCount.entrySet()) {
            char letter = entry.getKey();
            int requiredCount = entry.getValue();
            int availableCount = availableChars.getOrDefault(letter, 0);

            if (availableCount < requiredCount) {
                return false; // Insufficient characters to form the word
            }
        }
        return true;
    }

    // Helper method to count characters in a word
    private Map<Character, Integer> getCharAmount(String word) {
        Map<Character, Integer> charCount = new HashMap<>();
        for (char c : word.toCharArray()) {
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);
        }
        return charCount;
    }

    public boolean makeGuess() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            // Verifica se o nível foi concluído após cada tentativa
            if (hasCompletedLevel()) {
                System.out.println("Você concluiu todas as palavras! Nível concluído!");
                return true; // Nível concluído
            }else {

                System.out.print("Digite uma palavra (pressione 'Enter' para voltar ao menu): ");
                String input = sc.nextLine().trim().toUpperCase();

                // Se o input estiver vazio (usuário pressionou Enter)
                if (input.isEmpty()) {
                    if (hasCompletedLevel()) {
                        System.out.println("Você concluiu o nível! Voltando ao menu...");
                        return true; // Nível concluído
                    } else {
                        System.out.println("Nível não foi concluído. Voltando ao menu...");
                        return false; // Nível não concluído
                    }
                }

                // Verifica se a palavra já foi adivinhada
                if (game.getGuessedWords().contains(input)) {
                    System.out.println("Essa palavra já foi adivinhada. Tente outra.");
                    continue; // Volta ao início do loop
                }

                // Verifica se a palavra pode ser formada com as letras disponíveis
                if (!canFormWord(input)) {
                    System.out.println("A palavra não pode ser formada com as letras disponíveis.");
                    continue; // Volta ao início do loop
                }

                // Verifica se a palavra é válida
                if (level.isCorrectWord(input)) {
                    System.out.println("Parabéns! Você acertou a palavra: " + input);
                    game.addCoins(10); // Adiciona 10 moedas por palavra correta
                    game.getGuessedWords().add(input); // Adiciona a palavra à lista de palavras adivinhadas
                    System.out.println("Moedas acumuladas: " + game.getCoins());
                } else {
                    System.out.println("Palavra válida, mas não está na lista de palavras válidas.");
                }
            }
        }
    }

    public boolean hasCompletedLevel() {
        // Converte as palavras do nível para maiúsculas
        List<String> selectedWordsUpper = new ArrayList<>();
        for (String word : level.getSelectedWords()) {
            selectedWordsUpper.add(word.toUpperCase());
        }

        // Verifica se todas as palavras do nível foram adivinhadas
        for (String word : selectedWordsUpper) {
            if (!game.getGuessedWords().contains(word)) {
                return false; // Ainda há palavras não adivinhadas
            }
        }
        return true; // Todas as palavras foram adivinhadas
    }
}
