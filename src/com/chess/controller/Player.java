package com.chess.controller;

import com.chess.model.board.Board;
import com.chess.model.board.Move;
import com.chess.model.pieces.King;
import com.chess.model.pieces.Piece;

import java.util.*;

public abstract class Player {

    /** The board */
    protected final Board board;
    /** The player's king */
    protected final King playerKing;
    /** The player's legal moves */
    protected final Collection<Move> legalMoves;
    /** The opponent's legal moves */
    protected final Collection<Move> opponentLegalMoves;
    /** If the player is in check */
    private final boolean isInCheck;
    private int moveNumber;

    /** Constructor
     *
     * @param board the board
     * @param legalMoves the player's legal moves
     * @param opponentMoves the opponent's legal moves
     */
    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing = establishKing();
        Collection<Move> kingAttacks = calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves);
        this.isInCheck = !kingAttacks.isEmpty();
        Collection<Move> castleMoves = calculateKingCastles(legalMoves, opponentMoves);
        if (castleMoves != null) {
            this.legalMoves = new ArrayList<>(legalMoves);
            this.legalMoves.addAll(castleMoves);
        } else {
            this.legalMoves = legalMoves;
        }
        this.opponentLegalMoves = Collections.unmodifiableList(new LinkedList<>(opponentMoves));
        this.moveNumber = 0;

    }

    /** Calculate the attacks on a tile
     *
     * @param piecePosition the position of the piece
     * @param opponentLegalMoves the opponent's legal moves
     * @return a collection of legal moves
     */
    public static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> opponentLegalMoves) {
        final List<Move> attackMoves = new ArrayList<>(); /* List of legal moves */
        for(final Move move : opponentLegalMoves){ /* For each legal move */
            if(piecePosition == move.getDestinationCoordinate()){ /* If the piece's position is the same as the destination coordinate */
                attackMoves.add(move); /* Add the move to the list of legal moves */
            }
        }
        return Collections.unmodifiableList(new LinkedList<>(attackMoves)); /* Return the list of legal moves */
    }

    /** Get the player's king
     *
     * @return the player's king
     */
    private King establishKing() {
        for (final Piece piece: getActivePieces()) { /* For each piece */
            if (piece.getPieceType().isKing()) { /* If the piece is a king */
                return (King) piece; /* Return the king */
            }
        }
        throw new RuntimeException("There is no king! Not a valid board");
    }

    /** Get the player's legal escape moves
     *
     * @return the player's legal moves
     */
    protected boolean hasEscapeMoves() {
        for (final Move move : this.legalMoves) { /* For each legal move */
            final MoveTransition transition = makeMove(move); /* Make the move */
            if (transition.getMoveStatus().isDone()) { /* If the move is done */
                return true; /* Return true */
            }
        }
        return false; /* Return false */
    }

    /** Checks if a move is legal
     *
     * @param move the move
     * @return true if the move is legal
     */
    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }

    /** Checks if the player is in check
     *
     * @return true if the player is in check
     */
    public boolean isInCheck() {
        return this.isInCheck;
    }

    /** Checks if the player is in checkmate
     *
     * @return true if the player is in checkmate
     */
    public boolean isInCheckMate() {
        return this.isInCheck && !hasEscapeMoves();
    }

    /** Checks if the player is in stalemate
     *
     * @return true if the player is in stalemate
     */
    public boolean isInStaleMate() {
        return !this.isInCheck && !hasEscapeMoves();
    }

    /** Checks if the player is castled
     *
     * @return true if the player is castled
     */
    public boolean isCastled() {
        return this.playerKing.isCastled();
    }

    /** Make a move
     *
     * @param move the move
     * @return the move transition
     */
    public MoveTransition makeMove(final Move move){

        if(!isMoveLegal(move)){ /* If the move is not legal */
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE); /* Return an illegal move */
        }

        final Board transitionBoard = move.execute(); /* Execute the move on a temporary board*/


        /* Create a new collection of legal moves for the opponent on the new board */
        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.currentPlayer().getLegalMoves());

        /* If the king is attacked from the move, the move is illegal */
        if(!kingAttacks.isEmpty()){
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        /* Otherwise, the move is legal and then made */
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    /** Get the player's king
     *
     * @return the player's king
     */
    public King getPlayerKing() {
        return this.playerKing;
    }

    /** Get the player's legal moves
     *
     * @return the player's legal moves
     */
    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }


    /** Gets the active pieces of a player
     *
     * @return the active pieces
     */
    public abstract Collection<Piece> getActivePieces();


    /** Gets the opponent of a player
     *
     * @return the opponent
     */
    public abstract Player getOpponent();

    /** Calculate the king castles
     *
     * @param playerLegals the player's legal moves
     * @param opponentLegals the opponent's legal moves
     * @return a collection of legal moves
     */
    protected  abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals);

    /** Check if the player has castle opportunities
     *
     * @return true if the player can castle on the king or queen side
     */
    protected boolean hasCastleOpportunities() {
        return !this.isInCheck && !this.isCastled();
    }

    public int getMoveNumber() {
        return this.moveNumber;
    }

    /** Get the alliance of the player
     *
     * @return the alliance
     */
    public abstract Alliance getAlliance();

    /** Can the player castle on the king side?
     *
     * @return true if the player can castle on the king side
     */
    public boolean isKingSideCastleCapable() {
        return this.playerKing.isKingSideCastleCapable();
    }

    /** Can the player castle on the queen side?
     *
     * @return true if the player can castle on the queen side
     */
    public boolean isQueenSideCastleCapable() {
        return this.playerKing.isQueenSideCastleCapable();
    }

    public MoveTransition unMakeMove(final Move move) {
        return new MoveTransition(this.board, move.undo(), move, MoveStatus.DONE);
    }
}
