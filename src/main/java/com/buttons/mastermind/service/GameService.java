package com.buttons.mastermind.service;

import com.buttons.mastermind.exception.*;
import com.buttons.mastermind.model.Colour;
import com.buttons.mastermind.model.Game;
import com.buttons.mastermind.model.Guess;
import com.buttons.mastermind.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class GameService {

    private static final Logger logger = Logger.getLogger(GameService.class.getName());
    private static final List<String> colours = Arrays.asList("RED", "GREEN", "BLUE", "PURPLE", "ORANGE", "YELLOW");

    @Autowired
    private GameRepository gameRepository;

    public Game startNewGame() {
        logger.info("New game started");
        return gameRepository.save(createNewGame());
    }

    private Game createNewGame() {

        List<Colour> colourList = new ArrayList<>();
        Game game = new Game();
        Random rand = new Random();
        for (int i = 0; i < 4; i++) {
            colourList.add(new Colour(colours.get(rand.nextInt(6))));
        }
        game.setPickedColours(colourList);
        return game;
    }

    public Guess checkGuess(int gameId, List<String> guessedColours) throws GameNotFoundException,
            NumberOfGuessedColoursException, ColourNotFoundException, WinnerException, LoserException {
        if (guessedColours.size() != 4){
            throw new NumberOfGuessedColoursException();
        }
        if (!colours.containsAll(guessedColours.stream().map(String::toUpperCase).collect(Collectors.toList()))) {
            throw new ColourNotFoundException();
        }

        Game game = gameRepository.findById(gameId).orElseThrow( () -> new GameNotFoundException());
        List<Colour> col = game.getPickedColours();
        List<String> guessedCol = guessedColours;

        int black = 0;
        int white = 0;

        for (int i = 0; i < col.size(); i++) {
            if (guessedCol.get(i).toUpperCase().equals(col.get(i).getName())) {
                black++;
                guessedCol.set(i, "NULL");
                col.get(i).setName("NULL");
            }
        }

        for (int i = 0; i < col.size(); i++ ) {
            if (col.get(i).getName().equals("NULL")) {
                continue;
            }

            String colour = col.get(i).getName();
            int index = guessedCol.indexOf(colour);
            if (guessedCol.contains(colour)) {
                white++;
                guessedCol.set(index, "NULL");
            }
        }

        Guess guess = new Guess(black, white);
        game.addGuess(guess);
        gameRepository.save(game);
        int winnerCounter = 0;
        for (int i = 0; i < game.getPickedColours().size(); i++) {
            if (game.getPickedColours().get(i).getName().equals("NULL")) {
                winnerCounter++;
            }
        }
        if (winnerCounter == 4 && game.getGuessList().size() <= 10) {
            throw new WinnerException();
        } else if (winnerCounter != 4 && game.getGuessList().size() == 10) {
            throw new LoserException();
        }
        return guess;
    }

    public List<Guess> getHistory(int Id) throws GameNotFoundException {
        Game game = gameRepository.findById(Id).orElseThrow(GameNotFoundException::new);
        return game.getGuessList();
    }

}
