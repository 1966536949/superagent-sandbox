package com.controller;

import com.entity.ToupiaohuodongEntity;
import com.entity.ToupiaojiluEntity;
import com.service.ToupiaohuodongService;
import com.service.ToupiaojiluService;
import com.utils.R;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ToupiaojiluControllerTest {

    @Mock
    private ToupiaojiluService toupiaojiluService;

    @Mock
    private ToupiaohuodongService toupiaohuodongService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ToupiaojiluController controller;

    private ToupiaojiluEntity vote(Long userId, Long activityId) {
        ToupiaojiluEntity vote = new ToupiaojiluEntity();
        vote.setCrossuserid(userId);
        vote.setCrossrefid(activityId);
        vote.setPiaoshu(0);
        return vote;
    }

    private ToupiaohuodongEntity activity(long id, int piaoshu) {
        ToupiaohuodongEntity activity = new ToupiaohuodongEntity();
        activity.setId(id);
        activity.setPiaoshu(piaoshu);
        return activity;
    }

    @Test
    void duplicateVoteIsRejectedWithoutChangingActivity() {
        when(toupiaohuodongService.selectByIdForUpdate(2L)).thenReturn(activity(2L, 5));
        when(toupiaojiluService.selectCount(any())).thenReturn(1);

        R result = controller.save(vote(1L, 2L), request);

        assertEquals(Integer.valueOf(500), result.get("code"));
        assertEquals("您已参与过投票！", result.get("msg"));
        verify(toupiaohuodongService, never()).updateById(any());
        verify(toupiaojiluService, never()).insert(any());
    }

    @Test
    void firstVoteInsertsRecordAndIncreasesActivityByOne() {
        when(toupiaojiluService.selectCount(any())).thenReturn(0);
        when(toupiaohuodongService.selectByIdForUpdate(2L)).thenReturn(activity(2L, 5));
        when(toupiaohuodongService.updateById(any())).thenReturn(true);
        when(toupiaojiluService.insert(any())).thenReturn(true);
        ToupiaojiluEntity vote = vote(1L, 2L);
        vote.setPiaoshu(100);

        R result = controller.save(vote, request);

        assertEquals(Integer.valueOf(0), result.get("code"));
        ArgumentCaptor<ToupiaohuodongEntity> update = ArgumentCaptor.forClass(ToupiaohuodongEntity.class);
        verify(toupiaohuodongService).updateById(update.capture());
        assertEquals(Long.valueOf(2L), update.getValue().getId());
        assertEquals(Integer.valueOf(6), update.getValue().getPiaoshu());
        verify(toupiaojiluService).insert(any(ToupiaojiluEntity.class));
    }

    @Test
    void failedActivityIncrementLeavesNoVoteRecord() {
        when(toupiaojiluService.selectCount(any())).thenReturn(0);
        when(toupiaohuodongService.selectByIdForUpdate(2L)).thenReturn(activity(2L, 5));
        when(toupiaohuodongService.updateById(any())).thenReturn(false);

        R result = controller.save(vote(1L, 2L), request);

        assertEquals(Integer.valueOf(500), result.get("code"));
        verify(toupiaojiluService, never()).insert(any());
    }

    @Test
    void addEndpointAlsoRejectsDuplicateVote() {
        when(toupiaohuodongService.selectByIdForUpdate(2L)).thenReturn(activity(2L, 5));
        when(toupiaojiluService.selectCount(any())).thenReturn(1);

        R result = controller.add(vote(1L, 2L), request);

        assertEquals(Integer.valueOf(500), result.get("code"));
        assertEquals("您已参与过投票！", result.get("msg"));
        verify(toupiaohuodongService, never()).updateById(any());
        verify(toupiaojiluService, never()).insert(any());
    }

    @Test
    void directVoteWithoutCrossuseridFallsBackToSessionUser() {
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn(9L);
        when(toupiaohuodongService.selectByIdForUpdate(2L)).thenReturn(activity(2L, 5));
        when(toupiaojiluService.selectCount(any())).thenReturn(1);

        R result = controller.save(vote(null, 2L), request);

        assertEquals(Integer.valueOf(500), result.get("code"));
        assertEquals("您已参与过投票！", result.get("msg"));
        verify(toupiaojiluService, never()).insert(any());
    }
}
