package com.buttons.mastermind.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Colour> pickedColours;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Guess> guessList;


    public Game() {
    }

    public Game(List<Colour> pickedColours) {
        this.pickedColours = pickedColours;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Colour> getPickedColours() {
        return pickedColours;
    }

    public void setPickedColours(List<Colour> pickedColours) {
        this.pickedColours = pickedColours;
        for (Colour colour : pickedColours) {
            colour.setGame(this);
        }
    }

    public void addColours(Colour colour) {
        this.pickedColours.add(colour);
    }

    public List<Guess> getGuessList() {
        return guessList;
    }

    public void setGuessList(List<Guess> guessList) {
        this.guessList = guessList;
    }

    public void addGuess(Guess guess) {
        this.guessList.add(guess);
        guess.setGame(this);
    }
}
