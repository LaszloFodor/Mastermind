package com.buttons.mastermind.service;

import com.buttons.mastermind.exception.*;
import com.buttons.mastermind.model.Colour;
import com.buttons.mastermind.model.Game;
import com.buttons.mastermind.model.Guess;
import com.buttons.mastermind.repository.GameRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GameServiceTest {

    private final static Logger logger = Logger.getLogger(GameServiceTest.class.getName());

    @InjectMocks
    GameService gameService;

    @Mock
    GameRepository gameRepository;

    private static final List<String> colours = Arrays.asList("RED", "GREEN", "BLUE", "PURPLE", "ORANGE", "YELLOW");

    private Game game1 = new Game();

    private Game game2 = new Game();

    @Before
    public void setUp() {
        logger.info("Test initialization");
        MockitoAnnotations.initMocks(this);

        List<Colour> pickedColourList = Arrays.asList(new Colour("RED"), new Colour("GREEN"), new Colour("BLUE"), new Colour("PURPLE"));
        List<Guess> guessList = Arrays.asList(new Guess(1,1), new Guess(2, 0));
        game1.setPickedColours(pickedColourList);
        game1.setGuessList(guessList);
    }

    @Test
    public void testStartnewGame() {
        logger.info("Test new game");
        when(gameRepository.save(any())).thenReturn(game1);
        assertTrue(colours.contains(game1.getPickedColours().get(0).getName()));
        assertTrue(colours.contains(game1.getPickedColours().get(1).getName()));
        assertTrue(colours.contains(game1.getPickedColours().get(2).getName()));
        assertTrue(colours.contains(game1.getPickedColours().get(3).getName()));
        assertEquals(4, game1.getPickedColours().size());
    }

    @Test(expected = ColourNotFoundException.class)
    public void testCheckGuess_colourNotAccepted() throws WinnerException, ColourNotFoundException, NumberOfGuessedColoursException, LoserException, GameNotFoundException, GuessOverFlowException {
        logger.info("Test check guess with wrong colour");
        List<String> cols = Arrays.asList("white", "RED", "RED", "RED");
        when(gameRepository.save(any())).thenReturn(game1);
        gameService.checkGuess(1, cols);
    }

    @Test(expected = NumberOfGuessedColoursException.class)
    public void testCheckGuess_notAdequateNumberOfColours() throws ColourNotFoundException, GuessOverFlowException, GameNotFoundException, NumberOfGuessedColoursException, LoserException, WinnerException {
        logger.info("Test check guess with wrong number of colours");
        List<String> cols = Arrays.asList("RED", "RED");
        when(gameRepository.save(any())).thenReturn(game1);
        gameService.checkGuess(1, cols);
    }

    @Test(expected = GameNotFoundException.class)
    public void testCheckGuess_gameNotFound() throws ColourNotFoundException, GuessOverFlowException, GameNotFoundException, NumberOfGuessedColoursException, LoserException, WinnerException {
        logger.info("Test check game with wrong game id");
        List<String> cols = Arrays.asList("RED", "RED", "RED", "RED");
        gameService.checkGuess(5, cols);
    }

    @Test(expected = GuessOverFlowException.class)
    public void testCheckGuess_moreThan10Guesses() throws ColourNotFoundException, GuessOverFlowException, GameNotFoundException, NumberOfGuessedColoursException, LoserException, WinnerException {
        logger.info("Test check game with more than 10 guesses");
        List<Guess> guessList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            guessList.add(new Guess(1,1));
        }
        game2.setGuessList(guessList);
        when(gameRepository.findById(any())).thenReturn(Optional.of(game2));
        List<String> cols = Arrays.asList("RED", "RED", "RED", "RED");
        gameService.checkGuess(1, cols);
    }

    @Test(expected = LoserException.class)
    public void testCheckGuess_youLost() throws ColourNotFoundException, GuessOverFlowException, GameNotFoundException, NumberOfGuessedColoursException, LoserException, WinnerException {
        logger.info("Test check game when you lost");
        List<Guess> guessList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            guessList.add(new Guess(1,1));
        }
        game1.setGuessList(guessList);
        when(gameRepository.findById(any())).thenReturn(Optional.of(game1));
        List<String> cols = Arrays.asList("RED", "RED", "RED", "RED");
        gameService.checkGuess(1, cols);
    }

//    @Test(expected = WinnerException.class)
//    public void testCheckGuess_youWon() throws ColourNotFoundException, GuessOverFlowException, GameNotFoundException, NumberOfGuessedColoursException, LoserException, WinnerException {
//        logger.info("Test check game when you won");
//        when(gameRepository.findById(any())).thenReturn(Optional.of(game1));
//        List<String> cols = Arrays.asList("RED", "GREEN", "BLUE", "PURPLE");
//        gameService.checkGuess(1, cols);
//    }

    @Test
    public void testGetHistory_succesful() throws GameNotFoundException {
        logger.info("Test game history");
        List<Guess> guessList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            guessList.add(new Guess(1,1));
        }
        game2.setGuessList(guessList);
        when(gameRepository.findById(any())).thenReturn(Optional.of(game2));
        for (int i = 0; i < 5; i++ ){
            assertEquals(gameService.getHistory(1).get(i).getBlack(), 1);
            assertEquals(gameService.getHistory(1).get(i).getWhite(), 1);
        }
    }

    @Test(expected = GameNotFoundException.class)
    public void testGetHistory_gameIdNotFound() throws GameNotFoundException {
        logger.info("Test game history when game id not exists");
        gameService.getHistory(1);
    }



}
