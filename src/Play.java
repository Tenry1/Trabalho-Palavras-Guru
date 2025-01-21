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
                    playLevels(game, true); // Inicia o jogo com níveis pré-feitos
                    break;
                case 2:
                    System.out.println("Modo selecionado: Níveis gerados");
                    playLevels(game, false); // Inicia o jogo com níveis gerados
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

    private static void playLevels(Game game, boolean isPreMade) {
        if (isPreMade) {
            game.loadLevels(); // Carrega os níveis pré-feitos
        } else {
            game.generateLevels(); // Gera os níveis
        }

        int totalLevels = game.getLevels().size();
        int completedLevels = 0;

        // Itera sobre os níveis armazenados no mapa
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
            game.getGuessedWords().clear(); // <-- Limpa a lista antes de cada nível

            // Cria uma instância de Guess para gerenciar a interação com o nível
            Guess guess = new Guess(level, game);

            // Inicia o loop de tentativas
            boolean levelCompleted = guess.makeGuess();

            // Verifica se o nível foi concluído
            if (levelCompleted) {
                System.out.println("Nível " + levelNumber + " concluído!");
                completedLevels++; // Incrementa o contador de níveis concluídos
            } else {
                System.out.println("Nível " + levelNumber + " não foi concluído. Voltando ao menu...");
                break; // Sai do loop e volta ao menu
            }
        }

        if (completedLevels == totalLevels) {
            System.out.println("Todos os níveis foram concluídos! Parabéns!");
        } else {
            System.out.println("Alguns níveis não foram concluídos. Voltando ao menu...");
        }
    }

}