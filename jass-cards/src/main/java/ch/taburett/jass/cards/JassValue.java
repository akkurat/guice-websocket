package ch.taburett.jass.cards;

public enum JassValue {
    // names can't start with number
    // for constiency: start all with underscore
    _6(6 ),
    _7( 7 ),
    _8(8 ),
    _9(9),
    _10(10 ),
    _B(11),
    _D(12),
    _K(13),
    _A(14);

    public final int ordinal;


    JassValue(int ordinal ) {
        this.ordinal = ordinal;
    }

}
