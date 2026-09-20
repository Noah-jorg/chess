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

        if (this.pieceType.equals(PieceType.QUEEN)) {
            return queenMoves(board, myPosition);
        }

        if (this.pieceType.equals(PieceType.BISHOP)) {
            return bishopMoves(board, myPosition);
        }

        if (this.pieceType.equals(PieceType.KNIGHT)) {
            return knightMoves(board, myPosition);
        }
        if (this.pieceType.equals(PieceType.ROOK)) {
            return rookMoves(board,myPosition);
        }
        if (this.pieceType.equals(PieceType.PAWN)) {
            return pawnMoves(board, myPosition);
        }
        return moves;
    }

    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        Collection<ChessMove> rook = new ArrayList<ChessMove>();
        Collection<ChessMove> bishop = new ArrayList<ChessMove>();
        rook = rookMoves(board, myPosition);
        bishop = bishopMoves(board, myPosition);
        moves.addAll(rook);
        moves.addAll(bishop);
        return moves;
    }




    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        //checks forward
        int i = 1;
        ChessPosition target = new ChessPosition(myPosition.getRow() + 1 , myPosition.getColumn());
        while (inbound(target)) {
            if (checkNull(board, target)){
                moves.add(new ChessMove(myPosition, target, null));
            } else if (checkColor(board, target)) {
                break;
            } else {
                moves.add(new ChessMove(myPosition, target, null));
                break;
            } i++;
            target = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());
        }
        //checks left
        int j = 1;
        ChessPosition target2 = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
        while (inbound(target2)) {
            if (checkNull(board, target2)){
                moves.add(new ChessMove(myPosition, target2, null));
            } else if (checkColor(board, target2)) {
                break;
            } else {
                moves.add(new ChessMove(myPosition, target2, null));
                break;
            } j++;
            target2 = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - j);
        }

        //checks down
        int k = 1;
        ChessPosition target3 = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
        while (inbound(target3)) {
            if (checkNull(board, target3)){
                moves.add(new ChessMove(myPosition, target3, null));
            } else if (checkColor(board, target3)) {
                break;
            } else {
                moves.add(new ChessMove(myPosition, target3, null));
                break;
            } k++;
            target3 = new ChessPosition(myPosition.getRow() - k, myPosition.getColumn());
        }

        //checks right
        int l = 1;
        ChessPosition target4 = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
        while (inbound(target4)) {
            if (checkNull(board, target4)){
                moves.add(new ChessMove(myPosition, target4, null));
            } else if (checkColor(board, target4)) {
                break;
            } else {
                moves.add(new ChessMove(myPosition, target4, null));
                break;
            } l++;
            target4 = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + l);
        }
    return moves;
    }






    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        /**
        Logic:
         First see if piece is in starting position (WHITE= row 2 (true location row 1), BLACK= row 7 (true location row 6))
         if in start location -> check additional space directly in front if in bounds
         if spot in front is ANY piece, cannot move forward.
         if spot infront left and right are open and opposite color, can move
        Check inbounds with every move
        */
        boolean start = (pieceColor == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2) || (pieceColor == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7);

        //white pawn checks rows going up (+)
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            System.out.println("Pawn is WHITE");
            //checks spaces in front is in bounds
            if (inbound(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()))) {
                System.out.println("Space in front is in bounds");
                // checks if space in front is null. If null -> add. if not -> next.
                if (checkNull(board, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()))) {
                    if (myPosition.getRow() + 1 == 8) {
                        addPromotion(moves, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()));
                    }
                    else {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), null));
                    }
                    System.out.println("Added space to moves list");
                    //if start == true, then check the next piece. NEEDS TO BE BELOW PREVIOUS IF STATEMENT
                    if (start && checkNull(board, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn()))) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn()),null));
                    }
                }
            }
            //check right corner
            if (inbound(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() +1))) {
                //check if null
                if (!checkNull(board,new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() +1))) {
                    //check if piece is black to the right corner
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1)).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        if (myPosition.getRow() + 1 == 8) {
                            addPromotion(moves, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()+1));
                        }
                        else {
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()+1), null));
                        }
                    }
                }
            }
            //check left corner
            if (inbound(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() -1))) {
                //check if null
                if (!checkNull(board,new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() -1))) {
                    //check if piece is black to the left corner
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1)).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        if (myPosition.getRow() + 1 == 8) {
                            addPromotion(moves, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()-1));
                        }
                        else {
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()-1), null));
                        }
                    }
                }
            }
        }

        //BLACK PAWN
        if (pieceColor == ChessGame.TeamColor.BLACK) {
            //checks spaces in front is in bounds
            if (inbound(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()))) {
                // checks if space in front is null. If null -> add. if not -> next.
                if (checkNull(board, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()))) {
                    if (myPosition.getRow() - 1 == 1) {
                        addPromotion(moves, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()));
                    }
                    else {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), null));
                    }
                    //if start == true, then check the next piece. NEEDS TO BE BELOW PREVIOUS IF STATEMENT
                    if (start && checkNull(board, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn()))) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn()),null));
                    }
                }
            }
            //check right corner
            if (inbound(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1))) {
                //check if null
                if (!checkNull(board, new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()+1) )) {
                    //check if piece is white to the right corner
                    if (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1)).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        if (myPosition.getRow() - 1 == 1) {
                            addPromotion(moves, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()+1));
                        }
                        else {
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()+1), null));
                        }
                    }
                }
            }
            //check left corner
            if (inbound(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1))) {
                //check if null
                if (!checkNull(board, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1))) {
                    //check if piece is white to the left corner
                    if (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1)).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        if (myPosition.getRow() - 1 == 1) {
                            addPromotion(moves, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()-1));
                        }
                        else {
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()-1), null));
                        }
                    }
                }
            }
        }
        return moves;
    }

        public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        //up 2 right 1
        if (inbound(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1))) {
            if (checkNull(board, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1))) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1), null));
            } else if (!checkColor(board, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1))) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1), null));
            }
        }

        //up 1 right 2
        if (inbound(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2))) {
            if (checkNull(board, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2))) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2), null));
            } else if (!checkColor(board, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2))) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2), null));
            }
        }

        //up 2 left 1
        if (inbound(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1))) {
            if (checkNull(board, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1))) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1), null));
            } else if (!checkColor(board, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + -1))) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + -1), null));
            }
        }
        //up 1 left 2
        if (inbound(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2))) {
            if (checkNull(board, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2))) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2), null));
            } else if (!checkColor(board, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2))) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2), null));
            }
        }

        //down 2 right 1
        if (inbound(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1))) {
            if (checkNull(board, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1))) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1), null));
            } else if (!checkColor(board, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1))) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1), null));
            }
        }

        //down 1 right 2
        if (inbound(new ChessPosition(myPosition.getRow() -1 , myPosition.getColumn() + 2))) {
            if (checkNull(board, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2))) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2), null));
            } else if (!checkColor(board, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2))) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2), null));
            }
        }

        //down 2 left 1
        if (inbound(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1))) {
            if (checkNull(board, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1))) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1), null));
            } else if (!checkColor(board, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1))) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1), null));
            }
        }

        //down 1 left 2
        if (inbound(new ChessPosition(myPosition.getRow() -1 , myPosition.getColumn() -2))) {
            if (checkNull(board, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2))) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2), null));
            } else if (!checkColor(board, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2))) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2), null));
            }
        }

        return moves;
    }

    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        //checks up and right
        int i = 1;
        ChessPosition target = new ChessPosition(myPosition.getRow() + 1 , myPosition.getColumn() + 1);
        while (inbound(target)) {
            if (checkNull(board, target)){
                moves.add(new ChessMove(myPosition, target, null));
            } else if (checkColor(board, target)) {
                break;
            } else {
                moves.add(new ChessMove(myPosition, target, null));
                break;
            } i++;
            target = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
        }
        //checks up and left
        int j = 1;
        ChessPosition target2 = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
        while (inbound(target2)) {
            if (checkNull(board, target2)){
                moves.add(new ChessMove(myPosition, target2, null));
            } else if (checkColor(board, target2)) {
                break;
            } else {
                moves.add(new ChessMove(myPosition, target2, null));
                break;
            } j++;
            target2 = new ChessPosition(myPosition.getRow() + j, myPosition.getColumn() - j);
        }

        //checks down and right
        int k = 1;
        ChessPosition target3 = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
        while (inbound(target3)) {
            if (checkNull(board, target3)){
                moves.add(new ChessMove(myPosition, target3, null));
            } else if (checkColor(board, target3)) {
                break;
            } else {
                moves.add(new ChessMove(myPosition, target3, null));
                break;
            } k++;
            target3 = new ChessPosition(myPosition.getRow() - k, myPosition.getColumn() + k);
        }

        //checks down and left
        int l = 1;
        ChessPosition target4 = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
        while (inbound(target4)) {
            if (checkNull(board, target4)){
                moves.add(new ChessMove(myPosition, target4, null));
            } else if (checkColor(board, target4)) {
                break;
            } else {
                moves.add(new ChessMove(myPosition, target4, null));
                break;
            } l++;
            target4 = new ChessPosition(myPosition.getRow() - l, myPosition.getColumn() - l);
        }
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


    public boolean checkInbound(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        if (row - 1 >= 8 || row - 1 < 0 || col - 1 >= 8 || col - 1 < 0) {
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

    //checks position's color vs this.piece color
    public boolean checkColor(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        if (piece == null) {
            return false;
        }
        ChessGame.TeamColor c = piece.getTeamColor();
        return pieceColor.equals(c);
    }

    public boolean checkNull(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        return piece == null;
    }

    public boolean inbound(ChessPosition position) {
        int r = position.getRow() - 1;
        int c = position.getColumn() - 1;
        return r >= 0 && r < 8 && c >= 0 && c < 8;
    }
    private void addPromotion(Collection<ChessMove> moves, ChessPosition start, ChessPosition end) {
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
    }
}
