import java.util.*;

public interface Level_interface {
    String getAvailableLetters(); // Retorna as letras disponíveis como uma string
    List<String> getSelectedWords(); // Retorna as palavras selecionadas
    String getWordRepresentation(String word); // Retorna a representação visual de uma palavra
    boolean isCorrectWord(String word); // Verifica se uma palavra é válida
    Map<Character, Integer> getAvailableCharacters(); // Retorna os caracteres disponíveis com suas contagens
    boolean isValidWordLength(String word); // Verifica se o comprimento da palavra é válido
}
