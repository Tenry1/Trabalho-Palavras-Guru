public class Economy {
    private int coins; // Armazena o número de moedas

    // Construtor padrão: inicia com 0 moedas
    public Economy() {
        this.coins = 0;
    }

    // Construtor que permite iniciar com um número específico de moedas
    public Economy(int initialCoins) {
        this.coins = initialCoins;
    }

    // Adiciona moedas e retorna o novo total
    public int addCoins(int coinsToAdd) {
        if (coinsToAdd < 0) {
            throw new IllegalArgumentException("Não é possível adicionar um número negativo de moedas.");
        }
        this.coins += coinsToAdd;
        return this.coins;
    }

    // Remove moedas e retorna o novo total
    public int removeCoins(int coinsToRemove) {
        if (coinsToRemove < 0) {
            throw new IllegalArgumentException("Não é possível remover um número negativo de moedas.");
        }
        if (this.coins < coinsToRemove) {
            throw new IllegalStateException("Moedas insuficientes para remover.");
        }
        this.coins -= coinsToRemove;
        return this.coins;
    }

    // Retorna o número atual de moedas
    public int getCoins() {
        return this.coins;
    }

    // Define o número de moedas (útil para carregar o jogo)
    public void setCoins(int coins) {
        if (coins < 0) {
            throw new IllegalArgumentException("O número de moedas não pode ser negativo.");
        }
        this.coins = coins;
    }
}
