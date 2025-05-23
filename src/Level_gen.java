import java.util.*;

public class Level_gen implements Level_interface {
    private final int MIN_WORDS = 1; // Número mínimo de palavras obrigatórias
    private final int MAX_WORDS = 4; // Número máximo de palavras obrigatórias
    private final int MIN_WORD_LENGTH = 2; // Comprimento mínimo das palavras

    private Map<Character, Integer> availableCharacters;
    private List<Character> availableLettersOrder; // Armazena a ordem das letras
    private List<String> selectedWords; // Palavras obrigatórias para passar de nível
    private List<String> correctWords; // Todas as subpalavras válidas (dão moedas, mas não são obrigatórias)

    public Level_gen(Dictionary dictionary) {
        try {
            // Seleciona uma palavra aleatória do dicionário
            String randomWord = dictionary.selectRandomWord();

            // Remove converte para maiúsculas
            String cleanedWord = randomWord.toUpperCase();

            // Obtém os caracteres disponíveis e suas quantidades
            this.availableCharacters = getCharAmount(cleanedWord);

            // Armazena a ordem original das letras
            this.availableLettersOrder = new ArrayList<>();
            for (char c : cleanedWord.toCharArray()) {
                availableLettersOrder.add(c);
            }

            // Encontra todas as subpalavras válidas a partir da palavra principal
            List<String> allSubWords = dictionary.findSubWords(randomWord);

            // Filtra as subpalavras para incluir apenas aquelas com pelo menos 2 caracteres
            allSubWords.removeIf(word -> word.length() < MIN_WORD_LENGTH);

            // Todas as subpalavras válidas são as correctWords
            this.correctWords = new ArrayList<>(allSubWords);

            // Inicializa a lista de palavras selecionadas (obrigatórias)
            this.selectedWords = new ArrayList<>();

            // Escolhe um número aleatório de palavras obrigatórias (entre MIN_WORDS e MAX_WORDS)
            Random random = new Random();
            int numberOfWords = random.nextInt(MAX_WORDS - MIN_WORDS + 1) + MIN_WORDS;

            // Adiciona palavras aleatórias de correctWords à lista de palavras obrigatórias
            while (selectedWords.size() < numberOfWords && !allSubWords.isEmpty()) {
                int index = random.nextInt(allSubWords.size());
                String word = allSubWords.get(index);

                if (!selectedWords.contains(word)) {
                    selectedWords.add(word);
                }
            }

            // Ordena as palavras selecionadas alfabeticamente
            selectedWords.sort(String::compareTo);
        } catch (Exception e) {
            System.err.println("Erro ao criar o nível: " + e.getMessage());
        }
    }

    @Override
    public String getAvailableLetters() {
        StringBuilder letters = new StringBuilder();
        for (char letter : availableLettersOrder) {
            letters.append(letter).append(" ");
        }
        return letters.toString().trim(); // Remove o espaço extra no final
    }

    @Override
    public List<String> getSelectedWords() {
        return selectedWords; // Retorna as palavras obrigatórias
    }

    @Override
    public String getWordRepresentation(String word, List<String> guessedWords) {
        // Se a palavra já foi adivinhada, retorna a palavra completa
        if (guessedWords.contains(word.toUpperCase())) {
            return word.toUpperCase();
        }
        // Caso contrário, retorna a representação com underscores
        return "_ ".repeat(word.length()).trim();
    }

    // Metodo auxiliar para contar os caracteres de uma palavra (ignorando espaços)
    private static Map<Character, Integer> getCharAmount(String word) {
        Map<Character, Integer> charCount = new HashMap<>();
        for (char c : word.toCharArray()) {
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);
        }
        return charCount;
    }

    @Override
    public boolean isValidWordLength(String word) {
        // Obtém o comprimento da palavra inserida
        int wordLength = word.trim().length();

        // Verifica se o comprimento da palavra corresponde ao comprimento de uma das selectedWords
        for (String selectedWord : selectedWords) {
            if (selectedWord.length() == wordLength) {
                return true; // O comprimento é válido
            }
        }

        return false; // O comprimento não é válido
    }

    @Override
    public boolean isCorrectWord(String word) {
        // Normaliza a palavra (remove espaços e converte para maiúsculas)
        String normalizedWord = word.trim().toUpperCase();

        // Verifica se o comprimento da palavra é válido
        if (!isValidWordLength(normalizedWord)) {
            System.out.println("A palavra deve ter o mesmo comprimento de uma das palavras a adivinhar.");
            return false;
        }

        // Passo 1: Verifica se a palavra pode ser formada com as letras disponíveis
        if (!canFormWord(normalizedWord)) {
            System.out.println("A palavra não pode ser formada com as letras disponíveis.");
            return false;
        }

        // Passo 2: Verifica se a palavra está na lista de palavras válidas (correctWords)
        for (String correctWord : correctWords) {
            if (correctWord.trim().toUpperCase().equals(normalizedWord)) {
                return true; // A palavra é válida (dá moedas, mas não é obrigatória)
            }
        }

        System.out.println("A palavra não está na lista de palavras válidas.");
        return false;
    }

    @Override
    public Map<Character, Integer> getAvailableCharacters() {
        return availableCharacters; // Retorna o mapa de caracteres disponíveis
    }

    // Verifica se a palavra pode ser formada com as letras disponíveis
    private boolean canFormWord(String word) {
        // Normaliza a palavra (remove espaços e converte para maiúsculas)
        String normalizedWord = word.trim().toUpperCase();

        // Obtém a contagem de caracteres da palavra
        Map<Character, Integer> wordCharAmount = getCharAmount(normalizedWord);

        // Verifica se há letras suficientes
        for (Map.Entry<Character, Integer> entry : wordCharAmount.entrySet()) {
            char letter = entry.getKey();
            int requiredCount = entry.getValue();
            int availableCount = availableCharacters.getOrDefault(letter, 0);

            if (availableCount < requiredCount) {
                return false; // Não há letras suficientes
            }
        }

        return true; // A palavra pode ser formada
    }
}