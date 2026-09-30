package chess;

import java.util.Arrays;
import java.util.Objects;

import static chess.ChessGame.TeamColor.*;
import static chess.ChessPiece.PieceType.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] board;

    public ChessBoard() {
        this.board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow();
        int col = position.getColumn();
        board[row - 1][col - 1] = piece;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        return board[row - 1][col - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int r=0; r < 8; r++){
            for (int c=0; c<8; c++){
                board[r][c] = null;
            }
        }

        for (int i=0; i < 8; i++) {
            board[1][i] = new ChessPiece(WHITE, PAWN);
        }
        addPiece(new ChessPosition (1,1), new ChessPiece (WHITE, ROOK));
        addPiece(new ChessPosition (1,2), new ChessPiece (WHITE, KNIGHT));
        addPiece(new ChessPosition (1,3), new ChessPiece (WHITE, BISHOP));
        addPiece(new ChessPosition (1,4), new ChessPiece (WHITE, QUEEN));
        addPiece(new ChessPosition (1,5), new ChessPiece (WHITE, KING));
        addPiece(new ChessPosition (1,6), new ChessPiece (WHITE, BISHOP));
        addPiece(new ChessPosition (1,7), new ChessPiece (WHITE, KNIGHT));
        addPiece(new ChessPosition (1,8), new ChessPiece (WHITE, ROOK));

        addPiece(new ChessPosition (8,1), new ChessPiece (BLACK, ROOK));
        addPiece(new ChessPosition (8,2), new ChessPiece (BLACK, KNIGHT));
        addPiece(new ChessPosition (8,3), new ChessPiece (BLACK, BISHOP));
        addPiece(new ChessPosition (8,4), new ChessPiece (BLACK, QUEEN));
        board[7][4] = new ChessPiece(BLACK, KING);
        board[7][5] = new ChessPiece(BLACK, BISHOP);
        board[7][6] = new ChessPiece(BLACK, KNIGHT);
        board[7][7] = new ChessPiece(BLACK, ROOK);
        for (int i=0; i < 8; i++){
            board[6][i] = new ChessPiece(BLACK, PAWN);
        }
    }
}
