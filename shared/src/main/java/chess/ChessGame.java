package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor currentTeam;
    private ChessBoard board;


    public ChessGame() {
        currentTeam = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeam;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeam = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        /**
         *LOGIC: First I need to check if the current team's king is in check. If it is, limit moves to only those that will
         * stop the king from being in check. To do this, get the pieceMoves and test each one to see if isInCheck returns false.
         * If isInCheck returns false, then add to valid moves. If isInCheck is still true, then move does not take king out of check.
         *
         * A move is valid if it is a "piece move" for the piece at the input location and making that move would not leave the team’s king in danger of check.
         */
        Collection<ChessMove> validMoves = new ArrayList<>();
        Collection<ChessMove> pieceMoves;
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null){
            return null;
        }
        TeamColor teamColor = piece.getTeamColor();
        pieceMoves = piece.pieceMoves(board, startPosition);

        for (ChessMove move : pieceMoves){
            ChessPosition end = move.getEndPosition();
            ChessPiece cap = board.getPiece(end);
            board.addPiece(startPosition,null);
            board.addPiece(end, piece);
            if (!isInCheck(teamColor)){
                validMoves.add(move);
            }
            board.addPiece(end, cap);
            board.addPiece(startPosition, piece);
        }

        return validMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition target = move.getEndPosition();
        ChessPiece piece = board.getPiece(start);
        Collection<ChessMove> moves = new ArrayList<>();
        if  (piece == null){
            throw new InvalidMoveException("You can't do that");
        }
        if (piece.getTeamColor() != currentTeam){
            throw new InvalidMoveException("Wrong team");
        }
        moves = validMoves(move.getStartPosition());
        if(moves == null || !moves.contains(move)){
            throw new InvalidMoveException("Not a valid move");
        }
        if (move.getPromotionPiece() != null) {
            piece = new ChessPiece(currentTeam, move.getPromotionPiece());
        }
        board.addPiece(start, null);
        board.addPiece(move.getEndPosition(), piece);

        if (currentTeam == TeamColor.WHITE){
            currentTeam = TeamColor.BLACK;
        }
        else{
            currentTeam = TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        /**
         * LOGIC: Get the teamColor's king position. Then get pieceMoves for all enemy pieces, append into a list.
         * If kingPosition is in list, return true. If not, return false.
         * teamColor = color of current team
         */

        ChessPosition kingPosition = getKingPosition(teamColor);
        Collection<ChessMove> enemyMoves = new ArrayList<>();

        if (teamColor.equals(TeamColor.WHITE)){
            enemyMoves = getEnemyMoves(TeamColor.BLACK);
        }
        if (teamColor.equals(TeamColor.BLACK)){
            enemyMoves = getEnemyMoves(TeamColor.WHITE);
        }
        for (ChessMove target : enemyMoves){
            if (target.getEndPosition().equals(kingPosition)){
                return true;
            }
        }
        return false;

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)){
            return false;
        }
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition target = new ChessPosition(i, j);
                if (sameColor(target, teamColor)) {
                    Collection<ChessMove> moves = validMoves(target);
                    if (moves != null && !moves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        /**
         *  Get king position and possible moves. Check to make sure not in check or checkmate.
         *  Check all possible moves and see if they match an enemy move.
         *  If there are no possible moves, return true
         */
        if (isInCheck(teamColor)) {
            return false;
        }
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition target = new ChessPosition(i, j);
                if (sameColor(target, teamColor)) {
                    Collection<ChessMove> moves = validMoves(target);
                    if (moves != null && !moves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    //returns true if color passed in matches color at position
    public boolean sameColor(ChessPosition position, ChessGame.TeamColor color){
        if (board.getPiece(position) == null){
            return false;
        }
        return board.getPiece(position).getTeamColor() == color;
    }

    //returns location of given color's king
    public ChessPosition getKingPosition(TeamColor color){
        for (int i=1; i<9; i++){
            for (int j=1; j<9;j++){
                ChessPosition target = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(target);
                if (sameColor(target, color) && piece.getPieceType().equals(ChessPiece.PieceType.KING)){
                    return target;
                }
            }
        }
        return null;
    }

    public Collection<ChessMove> getEnemyMoves(TeamColor color){
        Collection<ChessMove> enemyMoves = new ArrayList<>();
        for (int i=1; i<9; i++){
            for (int j=1; j<9; j++){
                ChessPosition target = new ChessPosition(i,j);
                if (sameColor(target, color)){
                    ChessPiece piece = board.getPiece(target);
                    enemyMoves.addAll(piece.pieceMoves(board, target));
                }
            }
        }
        return enemyMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return currentTeam == chessGame.currentTeam && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentTeam, board);
    }
}
