import java.util.*;
import java.io.*;

public class Dictionary {
    private final int MAX_WORD_LENGTH = 5;

    private List<String> words;

    public Dictionary(String filePath) throws IOException {
        words = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("Ficheiro não encontrado: " + filePath);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.contains("-") && line.length() <= MAX_WORD_LENGTH) {
                    words.add(line);
                }
            }
        }
    }

    public List<String> getWords() {
        return words;
    }

    public String selectRandomWord() {
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }

    public List<String> findSubWords(String word) {
        List<String> result = new ArrayList<>();
        Map<Character, Integer> wordCharAmount = getCharAmount(word);

        for (String w : words) {
            if (isSubWord(wordCharAmount, w)) {
                result.add(w);
            }
        }
        return result;
    }

    public Map<Character, Integer> getCharAmount(String word) {
        Map<Character, Integer> charAmount = new HashMap<>();
        for (char c : word.toCharArray()) {
            charAmount.put(c, charAmount.getOrDefault(c, 0) + 1);
        }
        return charAmount;
    }

    private boolean isSubWord(Map<Character, Integer> wordCharAmount, String w) {
        Map<Character, Integer> wCharAmount = getCharAmount(w);
        for (Map.Entry<Character, Integer> entry : wCharAmount.entrySet()) {
            if (wordCharAmount.getOrDefault(entry.getKey(), 0) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

}