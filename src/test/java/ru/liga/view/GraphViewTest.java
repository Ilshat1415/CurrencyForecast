package ru.liga.view;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.File;

public class GraphViewTest {
    private final View view = new GraphView();

    @Test
    public void testGetAnswer() {
        Assertions.assertThat(view.getAnswer() instanceof File).isTrue();
    }
}