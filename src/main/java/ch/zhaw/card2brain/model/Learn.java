package ch.zhaw.card2brain.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Learn {
    @NonNull
    @Getter
    boolean correct;
    @Getter
    @NonNull
    private Card card;

}
