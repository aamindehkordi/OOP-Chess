package tests.com.chess.tests;

import com.chess.controller.Alliance;
import com.chess.model.board.Board;
import com.chess.model.pieces.King;
import com.chess.model.pieces.Pawn;
import com.chess.controller.ai.PawnStructureAnalyzer;
import com.chess.controller.ai.StandardBoardEvaluator;
import com.chess.model.pgn.FenUtilities;
import org.junit.Test;

import static com.chess.model.board.Board.Builder;
import static com.chess.model.board.Board.createStandardBoard;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPawnStructure {


    @Test
    public void testIsolatedPawnsOnStandardBoard() {
        final Board board = createStandardBoard();
        assertEquals(PawnStructureAnalyzer.get().isolatedPawnPenalty(board.whitePlayer()), 0);
        assertEquals(PawnStructureAnalyzer.get().isolatedPawnPenalty(board.blackPlayer()), 0);
    }

    @Test
    public void testIsolatedPawnByExample1() {
        final Board board = FenUtilities.createGameFromFEN("r1bq1rk1/pp2bppp/1np2n2/6B1/3P4/1BNQ4/PP2NPPP/R3R1K1 b - - 0 13");
        assertEquals(PawnStructureAnalyzer.get().isolatedPawnPenalty(board.whitePlayer()), PawnStructureAnalyzer.ISOLATED_PAWN_PENALTY);
        assertEquals(PawnStructureAnalyzer.get().isolatedPawnPenalty(board.blackPlayer()), 0);
    }

    @Test
    public void testIsolatedPawnByExample2() {
        final Board board = FenUtilities.createGameFromFEN("r1bq1rk1/p3bppp/1np2n2/6B1/3P4/1BNQ4/PP2NPPP/R3R1K1 b - - 0 1");
        assertEquals(PawnStructureAnalyzer.get().isolatedPawnPenalty(board.whitePlayer()), PawnStructureAnalyzer.ISOLATED_PAWN_PENALTY);
        assertEquals(PawnStructureAnalyzer.get().isolatedPawnPenalty(board.blackPlayer()), PawnStructureAnalyzer.ISOLATED_PAWN_PENALTY * 2);
    }

    @Test
    public void testIsolatedPawnByExample3() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new King(Alliance.BLACK, 4, false, false));
        builder.setPiece(new Pawn(12, Alliance.BLACK));
        builder.setPiece(new Pawn(20, Alliance.BLACK));
        builder.setPiece(new Pawn(28, Alliance.BLACK));
        builder.setPiece(new Pawn(8, Alliance.BLACK));
        builder.setPiece(new Pawn(16, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new King(Alliance.WHITE, 60, false, false));
        builder.setMoveMaker(Alliance.WHITE);
        // Set the current player
        final Board board = builder.build();
        System.out.println(FenUtilities.createFENFromGame(board));

        assertEquals(PawnStructureAnalyzer.get().isolatedPawnPenalty(board.whitePlayer()), PawnStructureAnalyzer.ISOLATED_PAWN_PENALTY);
        assertEquals(PawnStructureAnalyzer.get().isolatedPawnPenalty(board.blackPlayer()), PawnStructureAnalyzer.ISOLATED_PAWN_PENALTY * 5);
    }

    @Test
    public void testIsolatedPawnByExample4() {
        final Board board = FenUtilities.createGameFromFEN("4k3/2p1p1p1/8/8/8/8/2P1P1P1/4K3 w KQkq -");
        assertEquals(PawnStructureAnalyzer.get().isolatedPawnPenalty(board.whitePlayer()), PawnStructureAnalyzer.ISOLATED_PAWN_PENALTY * 3);
        assertEquals(PawnStructureAnalyzer.get().isolatedPawnPenalty(board.blackPlayer()), PawnStructureAnalyzer.ISOLATED_PAWN_PENALTY * 3);
        final StandardBoardEvaluator boardEvaluator = (StandardBoardEvaluator) StandardBoardEvaluator.get();
        assertEquals(boardEvaluator.evaluate(board, 1), 0);
    }

    @Test
    public void testIsolatedPawnByExample5() {
        final Board board = FenUtilities.createGameFromFEN("6k1/p6p/8/8/8/8/P6P/6K1 b - - 0 1");
        assertEquals(PawnStructureAnalyzer.get().isolatedPawnPenalty(board.whitePlayer()), PawnStructureAnalyzer.ISOLATED_PAWN_PENALTY * 2);
        assertEquals(PawnStructureAnalyzer.get().isolatedPawnPenalty(board.blackPlayer()), PawnStructureAnalyzer.ISOLATED_PAWN_PENALTY * 2);
        final StandardBoardEvaluator boardEvaluator = (StandardBoardEvaluator) StandardBoardEvaluator.get();
        assertEquals(boardEvaluator.evaluate(board, 1), 0);
    }

    @Test
    public void testIsolatedPawnByExample6() {
        final Board board = FenUtilities.createGameFromFEN("6k1/4p3/4p3/8/8/4P3/4P3/6K1 b - - 0 1");
        assertEquals(PawnStructureAnalyzer.get().isolatedPawnPenalty(board.whitePlayer()), PawnStructureAnalyzer.ISOLATED_PAWN_PENALTY * 2);
        assertEquals(PawnStructureAnalyzer.get().isolatedPawnPenalty(board.blackPlayer()), PawnStructureAnalyzer.ISOLATED_PAWN_PENALTY * 2);
        final StandardBoardEvaluator boardEvaluator = (StandardBoardEvaluator) StandardBoardEvaluator.get();
        assertEquals(boardEvaluator.evaluate(board, 1), 0);
    }

    @Test
    public void testDoubledPawnByExample1() {
        final Board board = createStandardBoard();
        assertEquals(PawnStructureAnalyzer.get().doubledPawnPenalty(board.whitePlayer()), 0);
        assertEquals(PawnStructureAnalyzer.get().doubledPawnPenalty(board.blackPlayer()), 0);
        final StandardBoardEvaluator boardEvaluator = (StandardBoardEvaluator) StandardBoardEvaluator.get();
        assertEquals(boardEvaluator.evaluate(board, 1), 0);
    }

    @Test
    public void testDoubledPawnByExample2() {
        final Board board = FenUtilities.createGameFromFEN("6k1/4p3/4p3/8/8/4P3/4P3/6K1 b - - 0 1");
        assertEquals(PawnStructureAnalyzer.get().doubledPawnPenalty(board.whitePlayer()), PawnStructureAnalyzer.DOUBLED_PAWN_PENALTY * 2);
        assertEquals(PawnStructureAnalyzer.get().doubledPawnPenalty(board.blackPlayer()), PawnStructureAnalyzer.DOUBLED_PAWN_PENALTY * 2);
        final StandardBoardEvaluator boardEvaluator = (StandardBoardEvaluator) StandardBoardEvaluator.get();
        assertEquals(boardEvaluator.evaluate(board, 1), 0);
    }

    @Test
    public void testDoubledPawnByExample3() {
        final Board board = FenUtilities.createGameFromFEN("6k1/8/8/P7/P7/P7/8/6K1 b - - 0 1");
        assertEquals(PawnStructureAnalyzer.get().doubledPawnPenalty(board.whitePlayer()), PawnStructureAnalyzer.DOUBLED_PAWN_PENALTY * 3);
        assertEquals(PawnStructureAnalyzer.get().doubledPawnPenalty(board.blackPlayer()), 0);
    }

    @Test
    public void testDoubledPawnByExample4() {
        final Board board = FenUtilities.createGameFromFEN("6k1/8/8/P6p/P6p/P6p/8/6K1 b - - 0 1");
        assertEquals(PawnStructureAnalyzer.get().doubledPawnPenalty(board.whitePlayer()), PawnStructureAnalyzer.DOUBLED_PAWN_PENALTY * 3);
        assertEquals(PawnStructureAnalyzer.get().doubledPawnPenalty(board.blackPlayer()), PawnStructureAnalyzer.DOUBLED_PAWN_PENALTY * 3);
        assertEquals(PawnStructureAnalyzer.get().isolatedPawnPenalty(board.whitePlayer()), PawnStructureAnalyzer.ISOLATED_PAWN_PENALTY * 3);
        assertEquals(PawnStructureAnalyzer.get().isolatedPawnPenalty(board.blackPlayer()), PawnStructureAnalyzer.ISOLATED_PAWN_PENALTY * 3);
    }


}