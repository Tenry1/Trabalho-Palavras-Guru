import java.util.*;

public interface Level_interface {
    String getAvailableLetters(); // Returns available letters as a string
    List<String> getSelectedWords(); // Returns the selected words
    String getWordRepresentation(String word); // Returns the visual representation of a word
    boolean isCorrectWord(String word); // Verifies if a word is valid
    Map<Character, Integer> getAvailableCharacters(); // Returns available characters with their counts
}
