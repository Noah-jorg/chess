package chess;

import java.util.Arrays;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.ROOK;
import static chess.ChessPiece.PieceType.KNIGHT;
import static chess.ChessPiece.PieceType.BISHOP;
import static chess.ChessPiece.PieceType.KING;
import static chess.ChessPiece.PieceType.QUEEN;
import static chess.ChessPiece.PieceType.PAWN;
import static chess.ChessPiece.PieceType.ROOK;
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
        if (equals(board[row - 1][col - 1])) {
            return null;
        }
        return board[row - 1][col - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board[0][0] = new ChessPiece(BLACK, ROOK);
        board[0][1] = new ChessPiece(BLACK, KNIGHT);
        board[0][2] = new ChessPiece(BLACK, BISHOP);
        board[0][3] = new ChessPiece(BLACK, QUEEN);
        board[0][4] = new ChessPiece(BLACK, KING);
        board[0][5] = new ChessPiece(BLACK, BISHOP);
        board[0][6] = new ChessPiece(BLACK, KNIGHT);
        board[0][7] = new ChessPiece(BLACK, ROOK);
        for (int i=0; i < 8; i++){
            board[1][i] = new ChessPiece(BLACK, PAWN);
        }
        for (int i=0; i < 8; i++) {
            board[6][i] = new ChessPiece(WHITE, PAWN);
        }
        board[7][0] = new ChessPiece(WHITE, ROOK);
        board[7][1] = new ChessPiece(WHITE, KNIGHT);
        board[7][2] = new ChessPiece(WHITE, BISHOP);
        board[7][3] = new ChessPiece(WHITE, QUEEN);
        board[7][4] = new ChessPiece(WHITE, KING);
        board[7][5] = new ChessPiece(WHITE, BISHOP);
        board[7][6] = new ChessPiece(WHITE, KNIGHT);
        board[7][7] = new ChessPiece(WHITE, ROOK);
    }
}
