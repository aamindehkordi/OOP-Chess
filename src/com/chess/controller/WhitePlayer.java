package com.chess.controller;

import com.chess.model.board.Board;
import com.chess.model.board.BoardUtils;
import com.chess.model.board.Move;
import com.chess.model.board.Move.KingSideCastleMove;
import com.chess.model.pieces.Piece;
import com.chess.model.pieces.Rook;

import java.util.*;

import static com.chess.model.pieces.PieceType.ROOK;

public class WhitePlayer extends Player {

    private final Collection<Move> opponentLegals;

    /** Constructor
     *
     * @param board the board
     * @param whiteStandardLegalMoves the white player's legal moves
     * @param blackStandardLegalMoves the black player's legal moves
     */
    public WhitePlayer(final Board board, final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.opponentLegals = blackStandardLegalMoves;
    }

    @Override
    public String toString() {
        return Alliance.WHITE.toString() + " Player";
    }
    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    /**
     * @param playerLegals the player's legal moves
     * @param opponentLegals Opponent's legal moves
     * @return the legal moves for the king castles
     */
    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,
                                                    final Collection<Move> opponentLegals) {

        // If the king has no castle opportunities
        if(!hasCastleOpportunities()) {
            return Collections.emptyList();
        }

        final List<Move> kingCastles = new ArrayList<>(); // List of legal moves

        //White Castling requirements
        if(this.playerKing.isFirstMove() && this.playerKing.getPiecePosition() == 60 && !this.isInCheck()) {
            //WHITE KING SIDE CASTLE
            // If the tiles on the kingside are not occupied
            if(!this.board.getTile(61).isTileOccupied()  && !this.board.getTile(62).isTileOccupied()) {
                // Get the tile of the kingside rook
                final Piece kingSideRook = this.board.getTile(63).getPiece();
                // If the rook has not moved and its tile is not occupied
                if(kingSideRook != null && kingSideRook.isFirstMove()) {
                    // If the tiles on the kingside are not attacked
                    if(Player.calculateAttacksOnTile(61, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(62, opponentLegals).isEmpty() &&
                            kingSideRook.getPieceType() == ROOK) {
                        // Add the move to the list of legal moves
                        if(!BoardUtils.isKingPawnTrap(this.board, this.playerKing, 52)) {
                            Move kingSideCastle = new KingSideCastleMove(this.board, this.playerKing, 62,
                                    (Rook) kingSideRook, kingSideRook.getPiecePosition(), 61);
                            kingCastles.add(kingSideCastle);
                        }
                    }
                }
            }
            //WHITE QUEEN SIDE CASTLE
            // If the tiles on the queenside are not occupied
            if(!this.board.getTile(59).isTileOccupied()  && !this.board.getTile(58).isTileOccupied() &&
                    !this.board.getTile(57).isTileOccupied()) {
                // Get the tile of the queenside rook
                final Piece queenSideRook = this.board.getTile(56).getPiece();
                // If the rook has not moved and its tile is not occupied
                if(queenSideRook != null && queenSideRook.isFirstMove()) {
                    // If the tiles on the queenside are not attacked
                    if(Player.calculateAttacksOnTile(58, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(59, opponentLegals).isEmpty() &&
                            queenSideRook.getPieceType() == ROOK) {
                        // Add the move to the list of legal moves
                        if(!BoardUtils.isKingPawnTrap(this.board, this.playerKing, 52)) {
                            Move queenSideCastle = new Move.QueenSideCastleMove(this.board, this.playerKing, 58,
                                    (Rook) queenSideRook, queenSideRook.getPiecePosition(), 59);
                            kingCastles.add(queenSideCastle);
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableList(new LinkedList<>(kingCastles));
    }

}
