package jira6.fate.domain.card.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import jira6.fate.domain.card.dto.CardCreateRequestDto;
import jira6.fate.domain.card.dto.CardDetailResponseDto;
import jira6.fate.domain.card.dto.CardListResponseDto;
import jira6.fate.domain.card.dto.CardOrderListRequestDto;
import jira6.fate.domain.card.dto.CardOrderRequestDto;
import jira6.fate.domain.card.dto.CardResponseDto;
import jira6.fate.domain.card.dto.CardUpdateRequestDto;
import jira6.fate.domain.card.service.CardService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CardController {

    private final CardService cardService;

    /**
     * 카드 생성 ( 인가 필요 )
     *
     * @param columnId    : 컬럼 아이디
     * @param requestDto  : 생성할 카드의 정보
     * @param userDetails : 카드를 생성하려는 사용자의 정보
     * @return : 카드 생성 성공 상태 코드 및 메시지 반환
     */
    @PostMapping("/columns/{columnId}/cards")
    public ResponseEntity<MessageResponse> createCard(
        @Min(1) @PathVariable Long columnId,
        @Valid @RequestBody CardCreateRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        cardService.createCard(columnId, requestDto, userDetails.getUser());
        MessageResponse response = new MessageResponse(201, "카드 생성 성공");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 카드 순서 이동 ( 인가 필요 )
     *
     * @param columnId    : 컬럼 아이디
     * @param requestDto  : 이동된 카드 순서의 정보
     * @return : 카드 순서 이동 성공 상태 코드 및 메시지 반환
     */
    @PutMapping("/columns/{columnId}/cards/order")
    public ResponseEntity<MessageResponse> updateCardOrder(
        @Min(1) @PathVariable Long columnId,
        @Valid @RequestBody CardOrderListRequestDto requestDto
    ) {
        cardService.updateCardOrder(columnId, requestDto);
        MessageResponse response = new MessageResponse(200, "카드 순서 이동 성공");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 카드 전체 조회 ( 인가 필요 )
     *
     * @param boardId : 보드 아이디
     * @return : 카드 전체 조회 성공 상태 코드 및 메시지 반환
     */
    @GetMapping("/boards/{boardId}/cards")
    public ResponseEntity<DataResponse<List<CardListResponseDto<List<CardResponseDto>>>>> getAllCard(
        @Min(1) @PathVariable Long boardId
    ) {
        List<CardListResponseDto<List<CardResponseDto>>> responseDto = cardService.getAllCard(
            boardId);
        DataResponse<List<CardListResponseDto<List<CardResponseDto>>>> response = new DataResponse<List<CardListResponseDto<List<CardResponseDto>>>>(
            200, "카드 컬렴럼 조회 성공", responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 카드 조회 ( 인가 필요 )
     *
     * @param columnId : 컬럼 아이디
     * @param cardId   : 카드 아이디
     * @return : 카드 상세 조회 성공 상태 코드 및 메시지 반환
     */
    @GetMapping("/columns/{columnId}/cards/{cardId}")
    public ResponseEntity<DataResponse<CardDetailResponseDto>> getCard(
        @Min(1) @PathVariable Long columnId,
        @Min(1) @PathVariable Long cardId
    ) {
        CardDetailResponseDto responseDto = cardService.getCard(columnId, cardId);
        DataResponse<CardDetailResponseDto> response = new DataResponse<CardDetailResponseDto>(200,
            "카드 상세 조회 성공", responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 카드 컬럼별 조회 ( 인가 필요 )
     *
     * @param columnId : 컬럼 아이디
     * @return : 카드 컬럼별 조회 성공 상태 코드 및 메시지 반환
     */
    @GetMapping("/columns/{columnId}/cards")
    public ResponseEntity<DataResponse<List<CardResponseDto>>> getAllCardByColumn(
        @Min(1) @PathVariable Long columnId
    ) {
        List<CardResponseDto> responseDto = cardService.getAllCardByColumn(
            columnId);
        DataResponse<List<CardResponseDto>> response = new DataResponse<>(
            200, "카드 컬렴럼 조회 성공", responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 카드 작업자별 조회 ( 인가 필요 )
     *
     * @return : 카드 작업자별 조회 성공 상태 코드 및 메시지 반환
     */
    @GetMapping("/cards/teams/{teamId}")
    public ResponseEntity<DataResponse<List<CardListResponseDto<List<CardResponseDto>>>>> getAllCardByTeam(
        @Min(1) @PathVariable Long teamId
    ) {
        List<CardListResponseDto<List<CardResponseDto>>> responseDto = cardService.getAllCardByTeam(teamId);
        DataResponse<List<CardListResponseDto<List<CardResponseDto>>>> response = new DataResponse<>(
            200, "카드 작업자별 조회 성공", responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 카드 수정 ( 인가 필요 )
     *
     * @param columnId    : 컬럼 아이디
     * @param cardId      : 카드 아이디
     * @param requestDto  : 수정할 카드의 정보
     * @param userDetails : 카드를 수정하려는 사용자의 정보
     * @return : 카드 수정 성공 상태 코드 및 메시지 반환
     */
    @PutMapping("/columns/{columnId}/cards/{cardId}")
    public ResponseEntity<MessageResponse> updateCard(
        @Min(1) @PathVariable Long columnId,
        @Valid @Min(1) @PathVariable Long cardId,
        @RequestBody CardUpdateRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        cardService.updateCard(columnId, cardId, requestDto, userDetails.getUser());
        MessageResponse response = new MessageResponse(200, "카드 수정 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 카드 삭제 ( 인가 필요 )
     *
     * @param columnId    : 컬럼 아이디
     * @param cardId      : 카드 아이디
     * @param userDetails : 카드를 삭제하려는 사용자의 정보
     * @return : 카드 삭제 성공 상태 코드 및 메시지 반환
     */
    @DeleteMapping("/columns/{columnId}/cards/{cardId}")
    public ResponseEntity<MessageResponse> deleteCard(
        @Min(1) @PathVariable Long columnId,
        @Valid @Min(1) @PathVariable Long cardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        cardService.deleteCard(columnId, cardId, userDetails.getUser());
        MessageResponse response = new MessageResponse(204, "카드 삭제 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
