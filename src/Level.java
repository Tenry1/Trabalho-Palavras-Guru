import java.util.*;

public class Level {
    private List<Character> availableLetters;
    private List<String> correctWords;

    public Level(List<Character> availableLetters, List<String> words) {
        this.availableLetters = availableLetters;
        this.correctWords = words;
    }

    public boolean checkWord(String word) {
        return correctWords.contains(word);
    }

    public List<Character> getAvailableLetters() {
        return availableLetters;
    }

    public List<String> getCorrectWords() {
        return correctWords;
    }
}

