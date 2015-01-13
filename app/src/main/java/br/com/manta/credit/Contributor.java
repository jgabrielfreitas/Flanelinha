package br.com.manta.credit;

import java.util.ArrayList;
import java.util.List;

// Created by JGabrielFreitas on 12/01/2015 - 19:44.

public class Contributor {

    public String string;

    public final List<Link> children = new ArrayList<>();

    public Contributor(String string) {
        this.string = string;
    }

}
