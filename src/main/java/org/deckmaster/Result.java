package org.deckmaster;

public enum Result {
    CONSUME_FUNDS,
    DRAIN,
    FINAL_BOSS_DEFEATED,
    MORTAL,
    TAINT,
    WITHER,
    ;

    static final Player player = Game.game.player;

    Result() {

    }

    void execute(int value) {
        switch (this) {
            case CONSUME_FUNDS:
                consumeFunds(value);
                break;
            case DRAIN:
                drain(value);
                break;
            case FINAL_BOSS_DEFEATED:
                finalBossDefeated();
                break;
            case MORTAL:
                mortal(value);
                break;
            case TAINT:
                taint(value);
                break;
            case WITHER:
                wither(value);
                break;
        }
    }

    private void consumeFunds(int value) {
        // TODO: remove x fund cards
    }

    private void drain(int value) {
        // TODO: turn x magika into drainage
    }

    private void finalBossDefeated() {
        // TODO: end game
        player.hasWon = true;
    }

    private void mortal(int value) {
        // TODO: turn x fitness or withering into mortality
    }

    private void taint(int value) {
        // TODO: turn x magika or drainage into taint
    }

    private void wither(int value) {
        // TODO: turn x fitness into withering
    }
}
