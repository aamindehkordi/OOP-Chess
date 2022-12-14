package com.chess.model.pgn;

public class GameFactory {
    //TODO ADD COMMENTS

    public static Game createGame(final PGNGameTags tags,
                                  final String gameText,
                                  final String outcome) {
        try {
            return new ValidGame(tags, PGNUtilities.processMoveText(gameText), outcome);
        } catch(final ParsePGNException e) {
            return new InvalidGame(tags, gameText, outcome);
        }
    }
}