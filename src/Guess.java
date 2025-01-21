import java.util.*;

public class Guess {
    private final Level_interface level; // Current level
    private final Game game; // Reference to the game for updating coins and guessed words
    private final List<String> validWordsDiscovered; // List of valid but not-to-be-guessed words already discovered

    public Guess(Level_interface level, Game game) {
        this.level = level;
        this.game = game;
        this.validWordsDiscovered = new ArrayList<>(); // Initialize the list of valid discovered words
    }

    // Checks if a word can be formed with available characters
    private boolean canFormWord(String word) {
        Map<Character, Integer> availableChars = level.getAvailableCharacters();
        Map<Character, Integer> wordCharCount = getCharAmount(word);

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
                System.out.println("Você adivinhou todas as palavras! Nível concluído!");
                return true; // Nível concluído
            }

            System.out.print("Digite uma palavra \n(pressione 'Enter' para voltar ao menu)\n(pressione 'T' para uma dica): ");
            String input = sc.nextLine().trim().toUpperCase(); // Converte para maiúsculas

            if (input.equals("T")) {
                askForHint();
                continue;
            }

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
                // Normaliza as selectedWords para maiúsculas antes de comparar
                List<String> selectedWordsUpper = new ArrayList<>();
                for (String word : level.getSelectedWords()) {
                    selectedWordsUpper.add(word.toUpperCase());
                }

                // Verifica se a palavra é uma das palavras exibidas (selectedWords)
                if (selectedWordsUpper.contains(input)) {
                    System.out.println("Parabéns! Palavra certa: " + input); // Mensagem para selectedWords
                    game.getEconomy().addCoins(10); // Adiciona 10 moedas por palavra correta
                    game.getGuessedWords().add(input); // Adiciona a palavra à lista de palavras adivinhadas
                    System.out.println("Moedas acumuladas: " + game.getEconomy().getCoins());
                }
                // Verifica se a palavra é válida, mas não é uma das exibidas
                else if (!validWordsDiscovered.contains(input)) {
                    System.out.println("Parabéns! Palavra descoberta: " + input); // Mensagem para palavras válidas não exibidas
                    game.getEconomy().addCoins(10); // Adiciona 10 moedas por palavra válida
                    validWordsDiscovered.add(input); // Armazena a palavra para evitar repetições
                    System.out.println("Moedas acumuladas: " + game.getEconomy().getCoins());
                } else {
                    System.out.println("Palavra descoberta anteriormente: " + input);
                }
            } else {
                System.out.println("Palavra inválida. Tente outra.");
            }

            // Verifica novamente se o nível foi concluído após adivinhar uma palavra
            if (hasCompletedLevel()) {
                return true; // Nível concluído
            }
        }
    }

    public void askForHint() {
        Scanner sc = new Scanner(System.in);

        // Verifica se o jogador tem moedas suficientes
        if (game.getEconomy().getCoins() < 100) {
            System.out.println("Você não tem moedas suficientes para pedir uma dica. Moedas necessárias: 100");
            return;
        }

        // Exibe as palavras disponíveis para dicas
        System.out.println("Escolha uma palavra para receber uma dica:");
        List<String> selectedWords = level.getSelectedWords();
        for (int i = 0; i < selectedWords.size(); i++) {
            System.out.println((i + 1));
        }

        // O jogador escolhe uma palavra
        int wordChoice = sc.nextInt();
        sc.nextLine(); // Limpa o buffer do scanner

        if (wordChoice < 1 || wordChoice > selectedWords.size()) {
            System.out.println("Escolha inválida. Tente novamente.");
            return;
        }

        String chosenWord = selectedWords.get(wordChoice - 1).toUpperCase();

        // O jogador escolhe uma posição da palavra
        System.out.println("Escolha uma posição da palavra (1 a " + chosenWord.length() + "):");
        int position = sc.nextInt();
        sc.nextLine(); // Limpa o buffer do scanner

        if (position < 1 || position > chosenWord.length()) {
            System.out.println("Posição inválida. Tente novamente.");
            return;
        }

        // Revela a letra na posição escolhida
        char revealedLetter = chosenWord.charAt(position - 1);
        System.out.println("A letra na posição " + position + " é: " + revealedLetter);

        // Deduz 100 moedas do saldo do jogador
        game.getEconomy().removeCoins(100);
        System.out.println("100 moedas foram deduzidas. Moedas restantes: " + game.getEconomy().getCoins());
    }

    public boolean hasCompletedLevel() {
        // Converte as palavras do nível para maiúsculas
        List<String> selectedWordsUpper = new ArrayList<>();
        for (String word : level.getSelectedWords()) {
            selectedWordsUpper.add(word.toUpperCase()); // Converte para maiúsculas
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
