package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static ui.EscapeSequences.*;

public class DrawBoard {
    private static final String WHITE_SQUARE = SET_BG_COLOR_LIGHT_GREY;
    private static final String BLACK_SQUARE = SET_BG_COLOR_BLACK;
    private static final String BORDER_TEXT = SET_TEXT_COLOR_WHITE;
    private static final String BORDER_BG = SET_BG_COLOR_DARK_GREY;

    public static String drawBoard(ChessBoard board, boolean whitePlayer) {
        String result = "";
        //draw header first
        result += header(whitePlayer);
        //draw rows based if whitePlayer is true or not
        if (whitePlayer) {
            for (int i = 8; i >= 1; i--) {
                //adds row to result to print
                result += row(board, i, true);
            }
        } else {
            for (int i = 1; i <= 8; i++) {
                result += row(board, i, false);
            }
        }
        //add bottom column letters
        result += header(whitePlayer);
        return result;
    }

    private static String header(boolean whitePlayer) {
        String columns;
        if (whitePlayer) {
            columns = "  a  b  c  d  e  f  g  h  ";
        } else {
            columns = "  h  g  f  e  d  c  b  a  ";
        }
        return BORDER_BG + BORDER_TEXT + "  " + columns + "  " + RESET_BG_COLOR + RESET_TEXT_COLOR + "\n";
    }
    private static String row(ChessBoard board, int row, boolean whitePlayer) {
        String result = BORDER_BG + BORDER_TEXT + " " + row + " ";
        if (whitePlayer) {
            for (int j=1; j <= 8; j++) {
                result += square(board, row, j);
            }
        } else {
            for (int k=8; k >= 1; k--) {
                result += square(board, row, k);
            }
        }
        result += BORDER_BG + BORDER_TEXT + " " + row + " ";
        result += RESET_BG_COLOR + RESET_TEXT_COLOR + "\n";
        return result;
    }

    private static String square(ChessBoard board, int row, int col) {
        String bg;
        //get background color
        if ((row + col) % 2 == 1) {
            bg = WHITE_SQUARE;
        } else {
            bg = BLACK_SQUARE;
        }
        //get text color
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (piece == null) {
            return bg + EMPTY;
        }
        return bg + pieceSymbol(piece);
    }

    private static String pieceSymbol(ChessPiece p) {
        boolean isWhite = p.getTeamColor() == ChessGame.TeamColor.WHITE;
        String textColor = isWhite ? SET_TEXT_COLOR_RED : SET_TEXT_COLOR_BLUE;
        String symbol = switch (p.getPieceType()) {
            case KING -> isWhite ? WHITE_KING : BLACK_KING;
            case QUEEN -> isWhite ? WHITE_QUEEN : BLACK_QUEEN;
            case BISHOP -> isWhite ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> isWhite ? WHITE_KNIGHT : BLACK_KNIGHT;
            case ROOK -> isWhite ? WHITE_ROOK : BLACK_ROOK;
            case PAWN -> isWhite ? WHITE_PAWN : BLACK_PAWN;
        };
        return textColor + symbol;
    }
}
