package jira6.fate.domain.board.service;

import java.util.List;
import java.util.stream.Collectors;
import jira6.fate.domain.board.dto.BoardRequestDto;
import jira6.fate.domain.board.dto.BoardResponseDto;
import jira6.fate.domain.board.entity.Board;
import jira6.fate.domain.board.entity.Team;
import jira6.fate.domain.board.repository.BoardRepository;
import jira6.fate.domain.user.entity.User;
import jira6.fate.domain.user.entity.UserRole;
import jira6.fate.domain.user.repository.UserRepository;
import jira6.fate.global.exception.CustomException;
import jira6.fate.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, Long userId) {

        User user = validateUser(userId);
        validateBoardRequest(boardRequestDto);
        validateManager(user);

        Board board = Board.builder()
            .title(boardRequestDto.getTitle())
            .intro(boardRequestDto.getIntro())
            .user(user)
            .build();

        boardRepository.save(board);

        return BoardResponseDto.builder()
            .board(board)
            .includeMembers(false)
            .build();
    }

    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto boardRequestDto,
        Long userId) {
        Board board = validateBoard(boardId);
        User user = validateUser(userId);
        validateManager(user);
        validateBoardRequest(boardRequestDto);

        board.update(boardRequestDto.getTitle(), boardRequestDto.getIntro());
        boardRepository.save(board);

        return BoardResponseDto.builder()
            .board(board)
            .includeMembers(false)
            .build();
    }


    public void deleteBoard(Long boardId, Long userId) {
        Board board = validateBoard(boardId);
        User user = validateUser(userId);
        validateManager(user);

        boardRepository.delete(board);
    }

    public List<BoardResponseDto> getAllBoard() {
        List<Board> boards = boardRepository.findAll();
        return boards.stream()
            .map(board -> BoardResponseDto.builder()
                .board(board)
                .includeMembers(false)
                .build())
            .collect(Collectors.toList());
    }

    public BoardResponseDto getBoardId(Long boardId) {
        Board board = validateBoard(boardId);
        return BoardResponseDto.builder()
            .board(board)
            .includeMembers(true)
            .build();
    }

    public void userInvite(Long boardId, Long username, Long userId) {
        Board board = validateBoard(boardId);
        User user = validateUser(userId);
        validateManager(user);

        Team team = Team.builder()
            .board(board)
            .user(user)
            .build();

        board.getTeams().add(team);
        boardRepository.save(board);
    }

    private Board validateBoard(Long boardId) {
        return boardRepository.findById(boardId)
            .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    }

    private User validateUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private void validateManager(User user) {
        if (user.getUserRole() != UserRole.MANAGER) {
            throw new CustomException(ErrorCode.NOT_UNAUTHORIZED);
        }
    }

    private void validateBoardRequest(BoardRequestDto boardRequestDto) {
        if (boardRequestDto.getTitle() == null || boardRequestDto.getTitle().isEmpty() ||
            boardRequestDto.getIntro() == null || boardRequestDto.getIntro().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
    }
}
