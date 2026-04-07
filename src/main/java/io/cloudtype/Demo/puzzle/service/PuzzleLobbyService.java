package io.cloudtype.Demo.puzzle.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;

import io.cloudtype.Demo.puzzle.model.PuzzleRoom;
import io.cloudtype.Demo.puzzle.model.PuzzleType;

@Service
public class PuzzleLobbyService
{
    private final Map<String, PuzzleType> puzzleTypeMap = new LinkedHashMap<String, PuzzleType>();
    private final Map<String, List<PuzzleRoom>> roomMapByType = new ConcurrentHashMap<String, List<PuzzleRoom>>();

    public PuzzleLobbyService()
    {
        registerType(new PuzzleType("logic-grid", "로직 그리드",
            "단서를 연결해 정답 조합을 찾는 협동 추리 퍼즐",
            "중간"));
        registerType(new PuzzleType("word-chain", "단어 체인",
            "제한 시간 안에 연상 단어를 이어 가며 공통 해답을 찾는 퍼즐",
            "가벼움"));
        registerType(new PuzzleType("escape-code", "탈출 코드",
            "숫자와 힌트를 모아 잠금 코드를 해독하는 협동 퍼즐",
            "도전"));

        seedRoom("logic-grid", "초보 환영 로직방", "단서 정리와 표 업데이트를 연습하는 방입니다.", 2, 4, "모집 중");
        seedRoom("logic-grid", "심화 추리 스터디", "중간 난이도 문제를 천천히 함께 푸는 방입니다.", 3, 4, "진행 중");
        seedRoom("word-chain", "점심시간 스피드전", "짧게 한 판 돌려보는 캐주얼 협동방입니다.", 1, 6, "모집 중");
        seedRoom("escape-code", "금고 해제 팀", "힌트를 분담해 빠르게 코드를 맞추는 방입니다.", 4, 5, "막바지");
    }

    public List<PuzzleType> findAllTypes()
    {
        return new ArrayList<PuzzleType>(puzzleTypeMap.values());
    }

    public PuzzleType findType(String puzzleTypeCode)
    {
        return puzzleTypeMap.get(puzzleTypeCode);
    }

    public List<PuzzleRoom> findRoomsByType(String puzzleTypeCode)
    {
        List<PuzzleRoom> roomList = roomMapByType.get(puzzleTypeCode);

        if (roomList == null)
        {
            return Collections.emptyList();
        }

        return new ArrayList<PuzzleRoom>(roomList);
    }

    public PuzzleRoom findRoom(String puzzleTypeCode, String roomId)
    {
        List<PuzzleRoom> roomList = roomMapByType.get(puzzleTypeCode);

        if (roomList == null)
        {
            return null;
        }

        for (PuzzleRoom room : roomList)
        {
            if (room.getRoomId().equals(roomId))
            {
                return room;
            }
        }

        return null;
    }

    public PuzzleRoom createRoom(String puzzleTypeCode)
    {
        PuzzleType puzzleType = findType(puzzleTypeCode);

        if (puzzleType == null)
        {
            return null;
        }

        List<PuzzleRoom> roomList = roomMapByType.get(puzzleTypeCode);
        int roomNumber = roomList.size() + 1;

        PuzzleRoom room = new PuzzleRoom(
            UUID.randomUUID().toString(),
            puzzleTypeCode,
            puzzleType.getTitle() + " 협동방 " + roomNumber,
            "DB 연결 전까지 메모리에서 생성되는 기본 풀이방입니다.",
            1,
            6,
            "준비 중");

        roomList.add(0, room);

        return room;
    }

    public List<String> buildSuggestedRoles(PuzzleType puzzleType)
    {
        if (puzzleType == null)
        {
            return Collections.emptyList();
        }

        if ("logic-grid".equals(puzzleType.getCode()))
        {
            return Arrays.asList("단서 정리", "표 업데이트", "검산 담당");
        }

        if ("word-chain".equals(puzzleType.getCode()))
        {
            return Arrays.asList("키워드 제안", "시간 체크", "정답 확정");
        }

        return Arrays.asList("힌트 기록", "코드 조합", "진행 관리");
    }

    private void registerType(PuzzleType puzzleType)
    {
        puzzleTypeMap.put(puzzleType.getCode(), puzzleType);
        roomMapByType.put(puzzleType.getCode(), new CopyOnWriteArrayList<PuzzleRoom>());
    }

    private void seedRoom(String puzzleTypeCode, String roomName, String roomDescription, int currentPlayers, int maxPlayers,
        String statusLabel)
    {
        roomMapByType.get(puzzleTypeCode).add(new PuzzleRoom(
            UUID.randomUUID().toString(),
            puzzleTypeCode,
            roomName,
            roomDescription,
            currentPlayers,
            maxPlayers,
            statusLabel));
    }
}
