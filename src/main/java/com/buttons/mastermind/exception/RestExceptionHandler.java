package com.buttons.mastermind.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.net.ssl.HttpsURLConnection;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({GameNotFoundException.class})
    public ResponseEntity<Object> handleGameNotFoundException() {
        return new ResponseEntity<Object>("Game not found with that id", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NumberOfGuessedColoursException.class})
    public ResponseEntity<Object> handleNumberOfGuessedColours() {
        return new ResponseEntity<Object>("The number of guessed colours are not 4. Please try again!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ColourNotFoundException.class})
    public ResponseEntity<Object> handleColourNotFound() {
        return new ResponseEntity<Object>("The given colour is not exists. Please add another one!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({WinnerException.class})
    public ResponseEntity<Object> handleWinner() {
        return new ResponseEntity<Object>("You WON! Congratulations!", HttpStatus.OK);
    }

    @ExceptionHandler({LoserException.class})
    public ResponseEntity<Object> handleLoser() {
        return new ResponseEntity<Object>("You LOST!", HttpStatus.OK);
    }
}
