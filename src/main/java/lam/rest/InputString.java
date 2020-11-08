package lam.rest;

import javax.json.bind.annotation.JsonbCreator;
import java.util.List;

public class InputString {
    public String lawn;
    public List<MowInputString> mowers;

    @JsonbCreator
    public InputString(String lawn, List<MowInputString> mowers) {
        this.lawn = lawn;
        this.mowers = mowers;
    }

    @Override
    public String toString() {
        return "InputString{" +
                "lawn='" + lawn + '\'' +
                ", mowers=" + mowers +
                '}';
    }
}