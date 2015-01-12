package br.com.manta.credit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JGabrielFreitas on 12/01/2015 - 19:44.
 */
public class Group {

    public String string;
    public final List<String> children = new ArrayList<>();

    public Group(String string) {
        this.string = string;
    }

}
