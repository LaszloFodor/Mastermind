package com.buttons.mastermind.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Guess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    private int correctPositionAndColour;
    private int black;
//    private int correctColour;
    private int white;

    @ManyToOne
    @JsonIgnore
    private Game game;

    public Guess() {
    }

    public Guess(int black, int white) {
        this.black = black;
        this.white = white;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBlack() {
        return black;
    }

    public void setBlack(int black) {
        this.black = black;
    }

    public int getWhite() {
        return white;
    }

    public void setWhite(int white) {
        this.white = white;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
