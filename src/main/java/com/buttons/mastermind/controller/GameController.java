package com.buttons.mastermind.controller;

import com.buttons.mastermind.exception.*;
import com.buttons.mastermind.model.Game;
import com.buttons.mastermind.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
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
    public ResponseEntity<String> createGame() {
        Game game = gameService.startNewGame();
        return new ResponseEntity<String>("New game created! Let's play!", HttpStatus.CREATED);
    }

    @PostMapping("/games/{gameId}/guess")
    public ResponseEntity<?> guess(@PathVariable String gameId, @RequestParam List<String> colours) throws GameNotFoundException,
            NumberOfGuessedColoursException, ColourNotFoundException, WinnerException, LoserException, GuessOverFlowException {
        return new ResponseEntity<>(gameService.checkGuess(Integer.valueOf(gameId), colours), HttpStatus.OK);
    }

    @GetMapping("/games/{gameId}/history")
    public ResponseEntity<?> getHistory(@PathVariable String gameId) throws GameNotFoundException {
        return new ResponseEntity<>(gameService.getHistory(Integer.valueOf(gameId)), HttpStatus.OK);
    }
}
