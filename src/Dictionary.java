import java.util.*;
import java.io.*;

public class Dictionary {
    private final int MAX_WORD_LENGTH = 5;

    private List<String> words;

    // Construtor que recebe o caminho do ficheiro e carrega as palavras sem incluir palavras com hífens e com mais que MAX_WORD_LENGTH caracteres
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

    // Devolve a lista de palavras do dicionário
    public List<String> getWords() {
        return words;
    }

    // Devolve uma palavra aleatória do dicionário
    public String selectRandomWord() {
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }

    // Devolve uma lista de palavras que são sub-palavras da palavra recebida
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

    // Devolve um mapa com os caracteres e a respetiva quantidade de vezes que aparecem na palavra recebida
    public Map<Character, Integer> getCharAmount(String word) {
        Map<Character, Integer> charAmount = new HashMap<>();
        for (char c : word.toCharArray()) {
            charAmount.put(c, charAmount.getOrDefault(c, 0) + 1);
        }
        return charAmount;
    }

    // Verifica se a palavra w é uma sub-palavra utilizando o mapa de caracteres da palavra recebida
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