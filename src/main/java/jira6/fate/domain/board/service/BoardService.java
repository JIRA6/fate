package jira6.fate.domain.board.service;

import jira6.fate.domain.board.dto.BoardRequestDto;
import jira6.fate.domain.board.dto.BoardResponseDto;
import jira6.fate.domain.board.entity.Board;
import jira6.fate.domain.board.repository.BoardRepository;
import jira6.fate.global.exception.CustomException;
import jira6.fate.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto) {

        if (boardRequestDto.getTitle() == null || boardRequestDto.getTitle().isEmpty()
            || boardRequestDto.getIntro() == null || boardRequestDto.getIntro().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        Board board = Board.builder()
            .title(boardRequestDto.getTitle())
            .intro(boardRequestDto.getIntro())
            .build();

        boardRepository.save(board);

        return new BoardResponseDto(board);
    }

    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto boardRequestDto) {

        Board board = boardRepository.findById(boardId).orElseThrow(
            () -> new CustomException(ErrorCode.BOARD_NOT_FOUND)
        );

        board.update(boardRequestDto.getTitle(), boardRequestDto.getIntro());
        boardRepository.save(board);

        return new BoardResponseDto(board);
    }


    public void deleteBoard(Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(
            () -> new CustomException(ErrorCode.BOARD_NOT_FOUND)
        );

        boardRepository.delete(board);
    }
}
