package jira6.fate.domain.column.service;

import java.util.List;
import java.util.stream.Collectors;
import jira6.fate.domain.board.entity.Board;
import jira6.fate.domain.board.repository.BoardRepository;
import jira6.fate.domain.column.dto.ColumnOrderListRequestDto;
import jira6.fate.domain.column.dto.ColumnOrderRequestDto;
import jira6.fate.domain.column.dto.ColumnRequestDto;
import jira6.fate.domain.column.dto.ColumnResponseDto;
import jira6.fate.domain.column.entity.Columns;
import jira6.fate.domain.column.repository.ColumnRepository;
import jira6.fate.domain.user.entity.User;
import jira6.fate.domain.user.entity.UserRole;
import jira6.fate.global.exception.CustomException;
import jira6.fate.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ColumnService {

    private final ColumnRepository columnRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void createColumn(Long boardId, ColumnRequestDto columnRequestDto, User user) {
        Board board = findBoard(boardId);

        if (!hasAccessToBoard(user, board)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MANAGER);
        }

        Columns column = Columns.builder()
            .columnName(columnRequestDto.getColumnName())
            .columnOrder(columnRequestDto.getColumnOrder())
            .board(board)
            .build();

        columnRepository.save(column);
    }

    @Transactional
    public void updateColumn(Long boardId, Long columnId, ColumnRequestDto columnRequestDto,
        User user) {
        Board board = findBoard(boardId);
        Columns column = findColumn(columnId);

        if (!hasAccessToBoard(user, board)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MANAGER);
        }

        column.update(columnRequestDto.getColumnName());
    }

    @Transactional
    public void deleteColumn(Long boardId, Long columnId, User user) {
        Board board = findBoard(boardId);
        Columns column = findColumn(columnId);

        if (!hasAccessToBoard(user, board)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MANAGER);
        }

        columnRepository.delete(column);
    }

    @Transactional(readOnly = true)
    public List<ColumnResponseDto> getColumns(Long boardId, User user) {
        Board board = findBoard(boardId);

        if (!hasAccessToBoard(user, board)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MANAGER);
        }

        List<Columns> columns = columnRepository.findByBoardId(boardId);

        return columns.stream()
            .map(column -> ColumnResponseDto.builder()
                .id(column.getId())
                .columnName(column.getColumnName())
                .build())
            .collect(Collectors.toList());
    }

    @Transactional
    public void updateColumnOrder(Long boardId, ColumnOrderListRequestDto requestDto, User user) {
        Board board = findBoard(boardId);

        if (!hasAccessToBoard(user, board)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MANAGER);
        }

        List<ColumnOrderRequestDto> columnOrders = requestDto.getOrderData();

        for (ColumnOrderRequestDto columnOrder : columnOrders) {
            Long columnId = columnOrder.getColumnId();
            Long order = columnOrder.getColumnOrder();

            Columns column = columnRepository.findById(columnId).orElseThrow(
                () -> new CustomException(ErrorCode.CARD_NOT_FOUND)
            );

            column.updateColumnOrder(order);

            columnRepository.save(column); // 순서 업데이트 저장
        }
    }

    private boolean hasAccessToBoard(User user, Board board) {
        return user.getUserRole() == UserRole.MANAGER;
    }

    private Board findBoard(Long boardId) {
        return boardRepository.findById(boardId)
            .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    }

    private Columns findColumn(Long columnId) {
        return columnRepository.findById(columnId)
            .orElseThrow(() -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));
    }

}
