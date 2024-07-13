package jira6.fate.domain.column.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jira6.fate.domain.column.dto.ColumnOrderDto;
import jira6.fate.domain.column.dto.ColumnRequestDto;
import jira6.fate.domain.column.dto.ColumnResponseDto;
import jira6.fate.domain.column.service.ColumnService;
import jira6.fate.global.dto.MessageResponse;
import jira6.fate.global.exception.CustomException;
import jira6.fate.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ColumnController {

    private final ColumnService columnService;

    @PostMapping("/boards/{boardId}/columns")
    public ResponseEntity<MessageResponse> createColumn(
        @Min(1) @PathVariable Long boardId,
        @Valid @RequestBody ColumnRequestDto columnRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        columnService.createColumn(boardId, columnRequestDto, userDetails.getUser());

        MessageResponse response = MessageResponse.builder()
            .statusCode(201)
            .message("컬럼 생성 성공")
            .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/boards/{boardId}/columns/{columnId}")
    public ResponseEntity<MessageResponse> updateColumn(
        @Min(1) @PathVariable Long boardId,
        @Min(1) @PathVariable Long columnId,
        @RequestBody ColumnRequestDto columnRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        columnService.updateColumn(columnId, columnRequestDto, userDetails.getUser().getUserName());

        MessageResponse response = MessageResponse.builder()
            .statusCode(200)
            .message("컬럼 수정 성공")
            .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/boards/{boardId}/columns/{columnId}")
    public ResponseEntity<?> deleteColumn(
        @PathVariable Long columnId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            columnService.deleteColumn(columnId, userDetails.getUser().getUserName());
            return ResponseEntity.ok().build();
        } catch (CustomException e) {
            return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(e.getErrorCode().getMessage());
        }
    }

    @GetMapping("/boards/{boardId}/columns")
    public ResponseEntity<List<ColumnResponseDto>> getColumns(
        @PathVariable Long boardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            List<ColumnResponseDto> columns = columnService.getColumns(boardId,
                userDetails.getUser().getUserName());
            return ResponseEntity.ok(columns);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(null);
        }
    }

    @PostMapping("/boards/{boardId}/columns/{columnId}/order")
    public ResponseEntity<?> updateColumnOrder(
        @PathVariable Long boardId,
        @PathVariable Long columnId,
        @RequestBody List<ColumnOrderDto> columnOrderDtos,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            columnService.updateColumnOrder(boardId, columnOrderDtos, userDetails.getUser().getUserName());
            return ResponseEntity.ok().body("컬럼 순서 이동 성공");
        } catch (CustomException e) {
            return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(e.getErrorCode().getMessage());
        }
    }
}
