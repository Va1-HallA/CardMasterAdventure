package org.deckmaster;

public enum Result {
    WITHER(false),
    MORTAL(false),
    DRAIN(false),
    TAINT(false),
    CONSUME_FUNDS(false);

    boolean successfulResult;

    Result(boolean successfulResult) {
        this.successfulResult = successfulResult;
    }

    void execute(int value) {
        switch (this) {
            case WITHER:
                wither(value);
                break;
            case MORTAL:
                mortal(value);
                break;
            case DRAIN:
                drain(value);
                break;
            case TAINT:
                taint(value);
                break;
            case CONSUME_FUNDS:
                consumeFunds(value);
                break;
        }
    }

    private void wither(int value) {
        // TODO: turn x fitness into withering
    }

    private void mortal(int value) {
        // TODO: turn x fitness or withering into mortality
    }

    private void drain(int value) {
        // TODO: turn x magika into drainage
    }

    private void taint(int value) {
        // TODO: turn x magika or drainage into taint
    }

    private void consumeFunds(int value) {
        // TODO: remove x fund cards
    }
}
