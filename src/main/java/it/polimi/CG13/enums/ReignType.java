package it.polimi.CG13.enums;

import java.util.Arrays;

public enum ReignType {
    ANIMAL, FUNGI, INSECT, PLANT;

    public ReignType correspondingReignType(int position) {
        return Arrays.stream(ReignType.values())
                .filter(reignType -> reignType.ordinal() == position)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid position for ReignType: " + position));
    }
}
