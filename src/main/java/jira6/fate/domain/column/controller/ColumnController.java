package jira6.fate.domain.column.controller;

import jira6.fate.domain.column.dto.ColumnOrderDto;
import jira6.fate.domain.column.dto.ColumnRequestDto;
import jira6.fate.domain.column.dto.ColumnResponseDto;
import jira6.fate.domain.column.service.ColumnService;
import jira6.fate.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/boards/{boardId}/columns")
@RequiredArgsConstructor
public class ColumnController {

  private final ColumnService columnService;

  @PostMapping
  public ResponseEntity<?> createColumn(@RequestBody ColumnRequestDto columnRequestDto, @PathVariable Long boardId, Principal principal) {
    try {
      columnRequestDto.setBoardId(boardId);
      ColumnResponseDto columnResponseDto = columnService.createColumn(columnRequestDto, principal.getName());
      return ResponseEntity.ok().body(columnResponseDto);
    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getErrorCode().getMessage());
    }
  }

  @PutMapping("/{columnId}")
  public ResponseEntity<?> updateColumn(@PathVariable Long columnId, @RequestBody ColumnRequestDto columnRequestDto, Principal principal) {
    try {
      ColumnResponseDto columnResponseDto = columnService.updateColumn(columnId, columnRequestDto, principal.getName());
      return ResponseEntity.ok().body(columnResponseDto);
    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getErrorCode().getMessage());
    }
  }

  @DeleteMapping("/{columnId}")
  public ResponseEntity<?> deleteColumn(@PathVariable Long columnId, Principal principal) {
    try {
      columnService.deleteColumn(columnId, principal.getName());
      return ResponseEntity.ok().build();
    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getErrorCode().getMessage());
    }
  }

  @GetMapping
  public ResponseEntity<List<ColumnResponseDto>> getColumns(@PathVariable Long boardId, Principal principal) {
    try {
      List<ColumnResponseDto> columns = columnService.getColumns(boardId, principal.getName());
      return ResponseEntity.ok(columns);
    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorCode().getStatus()).body(null);
    }
  }

  @PostMapping("/{columnId}/order")
  public ResponseEntity<?> updateColumnOrder(@PathVariable Long boardId, @PathVariable Long columnId, @RequestBody List<ColumnOrderDto> columnOrderDtos, Principal principal) {
    try {
      columnService.updateColumnOrder(boardId, columnOrderDtos, principal.getName());
      return ResponseEntity.ok().body("컬럼 순서 이동 성공");
    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getErrorCode().getMessage());
    }
  }
}
