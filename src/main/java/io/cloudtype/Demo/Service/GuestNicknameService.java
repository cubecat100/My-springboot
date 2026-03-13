package io.cloudtype.Demo.Service;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

@Service
public class GuestNicknameService
{
    private static final String[] PREFIXES = {
            "푸른", "조용한", "빠른", "작은", "은은한", "단단한"
    };

    private static final String[] NOUNS = {
            "고양이", "여우", "별", "파도", "구름", "나무"
    };

    public String createGuestNickname()
    {
        String prefix = PREFIXES[ThreadLocalRandom.current().nextInt(PREFIXES.length)];
        String noun = NOUNS[ThreadLocalRandom.current().nextInt(NOUNS.length)];
        int number = ThreadLocalRandom.current().nextInt(1000, 10000);

        return "게스트_" + prefix + "_" + noun + "_" + number;
    }
}