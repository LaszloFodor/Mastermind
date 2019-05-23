package com.buttons.mastermind.controller;

import com.buttons.mastermind.exception.*;
import com.buttons.mastermind.model.Game;
import com.buttons.mastermind.model.Guess;
import com.buttons.mastermind.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/")
    public String home() {
        return "Welcome to the Mastermind game!";
    }

    @GetMapping("/create")
    public Game createGame() {
        Game game = gameService.startNewGame();
        return game;
    }

    @PostMapping("/{gameId}/guess")
    public Guess guess(@PathVariable String gameId, @RequestParam List<String> colours) throws GameNotFoundException,
            NumberOfGuessedColoursException, ColourNotFoundException, WinnerException, LoserException {
        return gameService.checkGuess(Integer.valueOf(gameId), colours);
    }

    @GetMapping("/{gameId}")
    public List<Guess> getHistory(@PathVariable String gameId) throws GameNotFoundException {
        return gameService.getHistory(Integer.valueOf(gameId));
    }

    @GetMapping("/{gameId}/get")
    public Iterable<Game> getAll(@PathVariable String gameId) throws GameNotFoundException {
        return gameService.getAll();
    }
}
