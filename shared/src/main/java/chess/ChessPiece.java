package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        if (this.pieceType.equals(PieceType.KING)) {
            return kingMoves(board, myPosition);
        }
        /**
        if (this.pieceType.equals(PieceType.QUEEN)) {

        }
        if (this.pieceType.equals(PieceType.BISHOP)) {

        }
        if (this.pieceType.equals(PieceType.KNIGHT)) {

        }
        if (this.pieceType.equals(PieceType.ROOK)) {

        }
        if (this.pieceType.equals(PieceType.PAWN)) {

        }
         */
        return moves;
    }

    //instead of running a for loop, check each of the 9 squares surrounding the piece
    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        //check row above
        for (int i=-1; i < 2; i++){
            int targetRow = myPosition.getRow() + 1;
            int targetCol = myPosition.getColumn() + i;
            if (checkInbound(new ChessPosition(targetRow, targetCol)) && checkSpace(board, new ChessPosition(targetRow, targetCol))) {
                moves.add(new ChessMove(myPosition, new ChessPosition(targetRow, targetCol), null));
            }
        }

        //check row below
        for (int i=-1; i < 2; i++){
            int targetRow = myPosition.getRow() - 1;
            int targetCol = myPosition.getColumn() + i;
            if (checkInbound(new ChessPosition(targetRow, targetCol)) && checkSpace(board, new ChessPosition(targetRow, targetCol))) {
                moves.add(new ChessMove(myPosition, new ChessPosition(targetRow, targetCol), null));
            }
        }

        // check left and right
        if (checkInbound(new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1)) && checkSpace(board, new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1))) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1), null));
        }
        if (checkInbound(new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1)) && checkSpace(board, new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1))) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1), null));
        }
        // print out list of available moves
        System.out.println("Available moves:");
        for (ChessMove move : moves) {
            System.out.println("{" + move.toString());
        }
        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

    public String outString() {
        if (this.pieceType == PieceType.KING) {
            return "King";
        } else if (this.pieceType == PieceType.QUEEN) {
            return "Queen";
        } else if (this.pieceType == PieceType.BISHOP) {
            return "Bishop";
        } else if (this.pieceType == PieceType.KNIGHT) {
            return "Knight";
        } else if (this.pieceType == PieceType.ROOK) {
            return "Rook";
        } else if (this.pieceType == PieceType.PAWN) {
            return "Pawn";
        } return "null";
    }


    public boolean checkInbound(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        if (row >= 8 || row < 0 || col >= 8 || col < 0) {
            return false;
        }
        return true;
    }

    public boolean checkSpace(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        if (piece == null) {
            return true;
        }
        ChessGame.TeamColor spaceColor = piece.getTeamColor();
        if (pieceColor.equals(spaceColor)) {
            return false;
        }
        return true;
    }
}
