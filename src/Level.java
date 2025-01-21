import java.util.*;

public class Level {
    private final int LEVEL_WORDS_LENGTH = new Random().nextInt(4) + 1;

    private Map<Character, Integer> availableCharacters;
    private List<String> selectedWords;
    private List<String> correctWords;

    // Construtor que recebe um dicionário e cria um nível com uma palavra aleatória e outras LEVEL_WORDS_LENGTH palavras
    public Level(Dictionary dictionary) {
        try {
            String randomWord = dictionary.selectRandomWord();

            this.availableCharacters = dictionary.getCharAmount(randomWord);
            this.correctWords = dictionary.findSubWords(randomWord);

            this.selectedWords = new ArrayList<>();
            this.selectedWords.add(randomWord);

            for (int i = 0; i <= LEVEL_WORDS_LENGTH; i++) {
                int index = new Random().nextInt(correctWords.size());
                if (!selectedWords.contains(correctWords.get(index))) {
                    selectedWords.add(correctWords.get(index));
                }
            }
        }
        catch (Exception e) {
            System.err.println("Erro a criar o nível: " + e.getMessage());
        }
    }

    // Verifica se a palavra recebida é uma palavra selecionada a ser adivinhada pelo jogador
    public boolean isSelectedWord(String word) {
        return selectedWords.contains(word);
    }

    // Verifica se a palavra recebida é uma palavra que pode ser adivinhada com os caracteres disponíveis
    public boolean isCorrectWord(String word) {
        return correctWords.contains(word);
    }

    // Devolve um mapa com os caracteres disponíveis
    public Map<Character, Integer> getAvailableCharacters() {
        return availableCharacters;
    }

    // Devolve a lista de palavras corretas que podem ser adivinhadas com os caracteres disponíveis
    public List<String> getCorrectWords() {
        return correctWords;
    }

    // Devolve a lista de palavras selecionadas a serem adivinhadas como "principais"
    public List<String> getSelectedWords() {
        return selectedWords;
    }

}

