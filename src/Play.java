import java.util.*;

public class Play {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int op = 0;
        Game game = new Game();

        // Menu de seleção de modo de jogo
        while (true) {
            System.out.println("-- Selecione o modo de jogo --");
            System.out.println("1 - Níveis pré-feitos");
            System.out.println("2 - Níveis gerados");
            System.out.println("3 - Carregar jogo salvo");
            System.out.println("4 - Sair");
            System.out.print("Escolha uma opção: ");

            try {
                op = sc.nextInt();
                sc.nextLine(); // Limpa o buffer do scanner
            } catch (Exception e) {
                System.out.println("Entrada inválida. Por favor, insira um número.");
                sc.nextLine(); // Limpa o buffer do scanner
                continue;
            }

            switch (op) {
                case 1:
                    System.out.println("Modo selecionado: Níveis pré-feitos");
                    playPreMadeLevels(game, sc); // Inicia o jogo com níveis pré-feitos
                    break;
                case 2:
                    System.out.println("Modo selecionado: Níveis gerados");
                    playGeneratedLevels(game, sc); // Inicia o jogo com níveis gerados
                    break;
                case 3:
                    System.out.println("Carregando jogo salvo...");
                    game.loadGame(); // Carrega o jogo salvo
                    break;
                case 4:
                    System.out.println("Saindo do jogo...");
                    return; // Sai do jogo
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }
    }

    private static void playPreMadeLevels(Game game, Scanner sc) {
        game.loadLevels(); // Carrega os níveis do arquivo

        // Itera sobre os níveis armazenados no mapa
        for (Map.Entry<Integer, Level_interface> entry : game.getLevels().entrySet()) {
            Level_interface level = entry.getValue();
            System.out.println("Nível " + entry.getKey() + ":");
            System.out.println("Letras disponíveis: " + level.getAvailableLetters());
            System.out.println("Palavras:");
            for (String word : level.getSelectedWords()) {
                System.out.println(level.getWordRepresentation(word));
            }

            // Cria uma instância de Guess para gerenciar a interação com o nível
            Guess guess = new Guess(level, game);
            guess.makeGuess(); // Inicia o loop de tentativas

            // Verifica se o jogador completou o nível
            if (guess.hasCompletedLevel()) {
                System.out.println("Nível " + entry.getKey() + " concluído!");
            } else {
                System.out.println("Nível " + entry.getKey() + " não foi concluído. Voltando ao menu...");
                break; // Sai do loop e volta ao menu
            }
        }
    }

    private static void playGeneratedLevels(Game game, Scanner sc) {
        game.generateLevels(); // Gera os níveis

        // Itera sobre os níveis gerados
        for (Map.Entry<Integer, Level_interface> entry : game.getLevels().entrySet()) {
            int levelNumber = entry.getKey();
            Level_interface level = entry.getValue();

            // Exibe informações do nível
            System.out.println("Nível " + levelNumber + ":");
            System.out.println("Letras disponíveis: " + level.getAvailableLetters());
            System.out.println("Palavras:");
            for (String word : level.getSelectedWords()) {
                System.out.println(level.getWordRepresentation(word));
            }

            // Debug: Exibe as palavras que o jogador deve adivinhar
            System.out.println(level.getSelectedWords());

            // Limpa a lista de palavras adivinhadas antes de iniciar o nível
            game.getGuessedWords().clear();

            // Cria uma instância de Guess para gerenciar a interação com o nível
            Guess guess = new Guess(level, game);

            // Inicia o loop de tentativas
            boolean levelCompleted = guess.makeGuess();

            // Verifica se o nível foi concluído
            if (levelCompleted) {
                System.out.print("");
            } else {
                System.out.print("");
                break; // Sai do loop e volta ao menu
            }
        }

        // Verifica se todos os níveis foram concluídos
        if (allLevelsCompleted(game)) {
            System.out.println("Todos os níveis gerados foram concluídos! Parabéns!");
        } else {
            System.out.println("Alguns níveis não foram concluídos. Voltando ao menu...");
        }
    }

    // Helper method to check if all levels are completed
    private static boolean allLevelsCompleted(Game game) {
        for (Map.Entry<Integer, Level_interface> entry : game.getLevels().entrySet()) {
            Level_interface level = entry.getValue();
            for (String word : level.getSelectedWords()) {
                if (!game.getGuessedWords().contains(word)) {
                    return false; // There are unguessed words remaining
                }
            }
        }
        return true; // All words in all levels have been guessed
    }

}
