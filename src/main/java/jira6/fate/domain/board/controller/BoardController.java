package jira6.fate.domain.board.controller;

import jakarta.validation.Valid;
import jira6.fate.domain.board.dto.BoardRequestDto;
import jira6.fate.domain.board.dto.BoardResponseDto;
import jira6.fate.domain.board.service.BoardService;
import jira6.fate.global.dto.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<DataResponse<BoardResponseDto>> createBoard(@Valid @RequestBody
    BoardRequestDto boardRequestDto) {
        BoardResponseDto responseDto = boardService.createBoard(boardRequestDto);
        DataResponse<BoardResponseDto> response = new DataResponse<>(200, "보드가 생성되었습니다.",
            responseDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
