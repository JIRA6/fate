package jira6.fate.domain.board.controller;

import jakarta.validation.Valid;
import java.util.List;
import jira6.fate.domain.board.dto.BoardRequestDto;
import jira6.fate.domain.board.dto.BoardResponseDto;
import jira6.fate.domain.board.service.BoardService;
import jira6.fate.global.dto.DataResponse;
import jira6.fate.global.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        DataResponse<BoardResponseDto> response = new DataResponse<>(200, "보드 생성 성공",
            responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<DataResponse<BoardResponseDto>> updateBoard(@PathVariable Long boardId,
        @Valid @RequestBody BoardRequestDto boardRequestDto) {
        BoardResponseDto boardResponseDto = boardService.updateBoard(boardId, boardRequestDto);
        DataResponse<BoardResponseDto> response = new DataResponse<>(200, "보드 수정 성공",
            boardResponseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<MessageResponse> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        MessageResponse response = new MessageResponse(200, "보드 삭제 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<BoardResponseDto>>> getAllBoard() {
        List<BoardResponseDto> boardList = boardService.getAllBoard();
        DataResponse<List<BoardResponseDto>> response = new DataResponse<>(200, "전체 보드 조회 성공",
            boardList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
