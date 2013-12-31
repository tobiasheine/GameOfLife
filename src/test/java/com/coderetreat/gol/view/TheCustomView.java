package com.coderetreat.gol.view;


import com.coderetreat.gol.misc.CustomView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class TheCustomView {
    CustomView tested;

    @Before
    public void setUp() throws Exception {
        tested = new CustomView(Robolectric.application);
    }

    @Test
    public void testShouldWork() throws Exception {
        tested.setEnabled(false);
        assertThat(tested.isEnabled()).isFalse();
    }
}
