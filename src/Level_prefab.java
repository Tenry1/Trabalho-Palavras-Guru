import java.util.*;

public class Level_prefab implements Level_interface {
    private final Map<Character, Integer> availableCharacters;
    private final List<Character> availableLettersOrder; // Armazena a ordem das letras
    private final List<String> selectedWords;
    private final List<String> correctWords;

    // Construtor privado para forçar o uso do método estático
    private Level_prefab(Map<Character, Integer> availableCharacters, List<Character> availableLettersOrder, List<String> selectedWords, List<String> correctWords) {
        this.availableCharacters = availableCharacters;
        this.availableLettersOrder = availableLettersOrder;
        this.selectedWords = selectedWords;
        this.correctWords = correctWords;
    }

    // Método estático para criar um Level a partir de uma lista de palavras
    public static Level_prefab createFromWords(List<String> words) {
        if (words == null || words.isEmpty()) {
            throw new IllegalArgumentException("A lista de palavras não pode estar vazia.");
        }

        // A primeira palavra é a lista de letras disponíveis
        String mainWord = words.get(0).replaceAll(" ", ""); // Remove espaços

        // Obtém os caracteres disponíveis e as suas quantidades
        Map<Character, Integer> availableCharacters = getCharAmount(mainWord);

        // Armazena a ordem original das letras
        List<Character> availableLettersOrder = new ArrayList<>();
        for (char c : mainWord.toCharArray()) {
            availableLettersOrder.add(c);
        }

        // As subpalavras válidas são todas as palavras, exceto a primeira (lista de letras)
        List<String> correctWords = new ArrayList<>(words.subList(1, words.size())); // Cria uma cópia independente

        // As palavras selecionadas incluem apenas as subpalavras (ignorando a primeira linha)
        List<String> selectedWords = new ArrayList<>(correctWords);

        // Cria e retorna um novo nível
        return new Level_prefab(availableCharacters, availableLettersOrder, selectedWords, correctWords);
    }

    // Método auxiliar para contar os caracteres de uma palavra (ignorando espaços)
    private static Map<Character, Integer> getCharAmount(String word) {
        Map<Character, Integer> charCount = new HashMap<>();
        for (char c : word.toCharArray()) {
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);
        }
        return charCount;
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

        // Verifica se a palavra está na lista de palavras válidas (correctWords)
        return correctWords.contains(normalizedWord);
    }

    // Getters
    @Override
    public Map<Character, Integer> getAvailableCharacters() {
        return availableCharacters; // Return the map of available characters
    }

    public List<String> getCorrectWords() {
        return correctWords;
    }
}