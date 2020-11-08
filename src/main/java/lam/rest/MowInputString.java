package lam.rest;

import javax.json.bind.annotation.JsonbCreator;

public class MowInputString {
    String coord;
    String instructions;

    @JsonbCreator
    public MowInputString(String coord, String instructions) {
        this.coord = coord;
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return "MowInputString{" +
                "coord='" + coord + '\'' +
                ", instructions='" + instructions + '\'' +
                '}';
    }
}
