import java.util.*;

public class Level {
    private final int LEVEL_WORD_LENGTH = new Random().nextInt(4) + 1;

    private Map<Character, Integer> availableCharacters;
    private List<String> selectedWords;
    private List<String> correctWords;

    public Level(Dictionary dictionary) {
        try {
            String randomWord = dictionary.selectRandomWord();

            this.availableCharacters = dictionary.getCharAmount(randomWord);
            this.correctWords = dictionary.findSubWords(randomWord);

            this.selectedWords = new ArrayList<>();
            this.selectedWords.add(randomWord);


            for (int i = 0; i <= LEVEL_WORD_LENGTH; i++) {
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

    public boolean isSelectedWord(String word) {
        return selectedWords.contains(word);
    }

    public boolean isCorrectWord(String word) {
        return correctWords.contains(word);
    }

    public Map<Character, Integer> getAvailableCharacters() {
        return availableCharacters;
    }

    public List<String> getCorrectWords() {
        return correctWords;
    }

    public List<String> getSelectedWords() {
        return selectedWords;
    }

}

