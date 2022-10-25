package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Pawn extends Piece {

    /* 8: up
    * 7: up and right
    * 9: up and left
    */
    private final static int[] CANDIDATE_MOVE_COORDINATE = {8, 16, 7, 9};

    /* Constructor
     *
     * @param piecePosition the position of the piece
     * @param pieceAlliance the alliance of the piece
     */
    public Pawn(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.PAWN ,piecePosition, pieceAlliance);
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }
    @Override
    /* Calculate the legal moves for the Pawn
     *
     * @param board the board
     * @return a collection of legal moves
     */
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {

            /* add the offset to the current position */
            final int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidateOffset);

            /* if the tile is not occupied, add the move to the list of legal moves */
            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }

            /* if you are moving one tile forward and the tile is not occupied, add the move to the list of legal moves */
            if(!board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                //TODO Promotions
                legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
            }

            /* if you are moving two tiles forward and the tile is not occupied, add the move to the list of legal moves */
            else if( currentCandidateOffset == 16 && this.isFirstMove() &&
                    (BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
                    (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.getPieceAlliance().isWhite()) ) {
                /* between the current position and the destination position, there must be an empty tile */
                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                /* if both tiles are not occupied, add the move to the list of legal moves */
                if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                        !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                }
            }

            /* if you are moving one tile diagonally and the tile is occupied, add the move to the list of legal moves */
            else if(currentCandidateOffset == 7 &&                                                          /* if you are moving one tile diagonally */
                    !(BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||       /* and you are not on the eighth column, and you are white */
                     (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()) ) ) {    /* or you are not on the first column, and you are black  (because of promotions)*/
                /* if the tile is occupied*/
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    /* get the piece on the tile */
                    final Piece pieceAtDestination = board.getTile(candidateDestinationCoordinate).getPiece();
                    /* if the piece is not the same color as the pawn*/
                    if(this.pieceAlliance != pieceAtDestination.getPieceAlliance()) {
                        //TODO Promotions
                        legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination)); /* add the move to the list of legal moves */
                    }
                }
            }
            else if(currentCandidateOffset == 9 &&                                                       /* if you are moving one tile diagonally */
                  !(BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||       /* and you are not on the first column, and you are white */
                   (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()) ) ) {  /* or you are not on the eighth column, and you are black  (because of promotions)*/
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) { /* if the tile is occupied*/
                    final Piece pieceAtDestination = board.getTile(candidateDestinationCoordinate).getPiece(); /* get the piece on the tile */
                    if(this.pieceAlliance != pieceAtDestination.getPieceAlliance()) { /* if the piece is not the same color as the pawn,*/
                        //TODO Promotions
                        legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination)); /* add the move to the list of legal moves */
                    }
                }
            }
        }


        return Collections.unmodifiableList(legalMoves); /* return an unmodifiable list of legal moves */
    }
}
