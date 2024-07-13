package jira6.fate.domain.board.controller;

import jakarta.validation.Valid;
import java.util.List;
import jira6.fate.domain.board.dto.BoardRequestDto;
import jira6.fate.domain.board.dto.BoardResponseDto;
import jira6.fate.domain.board.dto.UserInviteDto;
import jira6.fate.domain.board.service.BoardService;
import jira6.fate.domain.user.service.UserService;
import jira6.fate.global.dto.DataResponse;
import jira6.fate.global.dto.MessageResponse;
import jira6.fate.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final UserService userService;

    @PostMapping
    public ResponseEntity<DataResponse<BoardResponseDto>> createBoard(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody BoardRequestDto boardRequestDto) {
        Long userId = userDetails.getUser().getId();
        BoardResponseDto responseDto = boardService.createBoard(boardRequestDto, userId);
        DataResponse<BoardResponseDto> response = new DataResponse<>(201, "보드 생성 성공",
            responseDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<DataResponse<BoardResponseDto>> updateBoard(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long boardId,
        @Valid @RequestBody BoardRequestDto boardRequestDto) {
        Long userId = userDetails.getUser().getId();
        BoardResponseDto boardResponseDto = boardService.updateBoard(boardId, boardRequestDto,
            userId);
        DataResponse<BoardResponseDto> response = new DataResponse<>(200, "보드 수정 성공",
            boardResponseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<MessageResponse> deleteBoard(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long boardId) {
        Long userId = userDetails.getUser().getId();
        boardService.deleteBoard(boardId, userId);
        MessageResponse response = new MessageResponse(200, "보드 삭제 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<BoardResponseDto>>> getAllBoard(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<BoardResponseDto> boardList = boardService.getAllBoard();
        DataResponse<List<BoardResponseDto>> response = new DataResponse<>(200, "전체 보드 조회 성공",
            boardList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<DataResponse<BoardResponseDto>> getBoardId(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long boardId) {
        BoardResponseDto responseDto = boardService.getBoardId(boardId);
        DataResponse<BoardResponseDto> response = new DataResponse<>(200, "보드 조회 성공", responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{boardId}/invite")
    public ResponseEntity<MessageResponse> userInvite(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long boardId,
        @Valid @RequestBody UserInviteDto userInviteDto) {
        Long userId = userDetails.getUser().getId();
        boardService.userInvite(boardId, userInviteDto.getUserId(), userId);
        MessageResponse response = new MessageResponse(200, "사용자 초대 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
