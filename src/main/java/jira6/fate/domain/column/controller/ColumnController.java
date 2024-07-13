package jira6.fate.domain.column.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import jira6.fate.domain.column.dto.ColumnOrderListRequestDto;
import jira6.fate.domain.column.dto.ColumnOrderRequestDto;
import jira6.fate.domain.column.dto.ColumnRequestDto;
import jira6.fate.domain.column.dto.ColumnResponseDto;
import jira6.fate.domain.column.service.ColumnService;
import jira6.fate.global.dto.DataResponse;
import jira6.fate.global.dto.MessageResponse;
import jira6.fate.global.exception.CustomException;
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
        columnService.updateColumn(boardId, columnId, columnRequestDto, userDetails.getUser());

        MessageResponse response = MessageResponse.builder()
            .statusCode(200)
            .message("컬럼 수정 성공")
            .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/boards/{boardId}/columns/{columnId}")
    public ResponseEntity<?> deleteColumn(
        @Min(1) @PathVariable Long boardId,
        @Min(1) @PathVariable Long columnId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        columnService.deleteColumn(boardId, columnId, userDetails.getUser());

        MessageResponse response = MessageResponse.builder()
            .statusCode(204)
            .message("컬럼 삭제 성공")
            .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/boards/{boardId}/columns")
    public ResponseEntity<DataResponse<List<ColumnResponseDto>>> getColumns(
        @PathVariable Long boardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<ColumnResponseDto> responseDtos = columnService.getColumns(boardId,
            userDetails.getUser());

        DataResponse<List<ColumnResponseDto>> response = DataResponse.<List<ColumnResponseDto>>builder()
            .statusCode(204)
            .message("컬럼 조회 성공")
            .data(responseDtos)
            .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/boards/{boardId}/columns/order")
    public ResponseEntity<?> updateColumnOrder(
        @PathVariable Long boardId,
        @RequestBody ColumnOrderListRequestDto columnOrderListRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        columnService.updateColumnOrder(boardId, columnOrderListRequestDto, userDetails.getUser());

        MessageResponse response = MessageResponse.builder()
            .statusCode(200)
            .message("컬럼 순서 이동 성공")
            .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
