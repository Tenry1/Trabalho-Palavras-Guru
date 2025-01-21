import java.util.*;

public class Level_gen implements Level_interface {
    private final int MIN_WORDS = 2; // Número mínimo de palavras por nível
    private final int MAX_WORDS = 5; // Número máximo de palavras por nível
    private final int MIN_WORD_LENGTH = 2; // Comprimento mínimo das palavras

    private Map<Character, Integer> availableCharacters;
    private List<Character> availableLettersOrder; // Armazena a ordem das letras
    private List<String> selectedWords;
    private List<String> correctWords;

    public Level_gen(Dictionary dictionary) {
        try {
            // Seleciona uma palavra aleatória do dicionário
            String randomWord = dictionary.selectRandomWord();

            // Remove espaços da palavra principal e converte para maiúsculas
            String cleanedWord = randomWord.replaceAll(" ", "").toUpperCase();

            // Obtém os caracteres disponíveis e suas quantidades
            this.availableCharacters = getCharAmount(cleanedWord);

            // Armazena a ordem original das letras
            this.availableLettersOrder = new ArrayList<>();
            for (char c : cleanedWord.toCharArray()) {
                availableLettersOrder.add(c);
            }

            // Encontra subpalavras válidas a partir da palavra principal
            List<String> allSubWords = dictionary.findSubWords(randomWord);

            // Filtra as subpalavras para incluir apenas aquelas com pelo menos 2 caracteres
            allSubWords.removeIf(word -> word.length() < MIN_WORD_LENGTH);

            // Inicializa a lista de palavras selecionadas
            this.selectedWords = new ArrayList<>();

            // Adiciona subpalavras à lista de palavras selecionadas
            Random random = new Random();
            int numberOfWords = random.nextInt(MAX_WORDS - MIN_WORDS + 1) + MIN_WORDS; // Número de palavras entre MIN_WORDS e MAX_WORDS

            while (selectedWords.size() < numberOfWords && !allSubWords.isEmpty()) {
                int index = random.nextInt(allSubWords.size());
                String word = allSubWords.get(index);

                if (!selectedWords.contains(word)) {
                    selectedWords.add(word);
                }
            }

            // Ordena as palavras selecionadas alfabeticamente
            selectedWords.sort(String::compareTo);

            // A lista de palavras válidas é a mesma que as palavras selecionadas
            this.correctWords = new ArrayList<>(selectedWords);
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
        return selectedWords;
    }

    @Override
    public String getWordRepresentation(String word) {
        return "_ ".repeat(word.length()).trim(); // Exemplo: "AR" -> "_ _"
    }

    // Metodo auxiliar para contar os caracteres de uma palavra (ignorando espaços)
    private static Map<Character, Integer> getCharAmount(String word) {
        Map<Character, Integer> charCount = new HashMap<>();
        for (char c : word.toCharArray()) {
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);
        }
        return charCount;
    }

    // Verifica se uma palavra está na lista de palavras selecionadas
    public boolean isSelectedWord(String word) {
        return selectedWords.contains(word);
    }

    @Override
    public boolean isCorrectWord(String word) {
        // Normaliza a palavra (remove espaços e converte para maiúsculas)
        String normalizedWord = word.trim().toUpperCase();

        // Passo 1: Verifica se a palavra pode ser formada com as letras disponíveis
        if (!canFormWord(normalizedWord)) {
            System.out.println("A palavra não pode ser formada com as letras disponíveis.");
            return false;
        }

        // Passo 2: Verifica se a palavra está na lista de palavras válidas
        for (String correctWord : correctWords) {
            if (correctWord.trim().toUpperCase().equals(normalizedWord)) {
                return true;
            }
        }

        System.out.println("A palavra não está na lista de palavras válidas.");
        return false;
    }

    @Override
    public Map<Character, Integer> getAvailableCharacters() {
        return availableCharacters; // Return the map of available characters
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