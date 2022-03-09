package ru.liga.view;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public class ConsoleViewTest {
    private final ByteArrayOutputStream arrayOutStream = new ByteArrayOutputStream();
    private final ByteArrayOutputStream arrayErrStream = new ByteArrayOutputStream();
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private View view;

    @Before
    public void setUpStreams() {
        String testMessage = "CurrencyForecast\n";
        InputStream inStream = new ByteArrayInputStream(testMessage.getBytes());
        PrintStream outStream = new PrintStream(arrayOutStream);
        PrintStream errStream = new PrintStream(arrayErrStream);

        System.setIn(inStream);
        System.setOut(outStream);
        System.setErr(errStream);

        view = new ConsoleView();
    }

    @After
    public void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testDisplayForecast() {
        Map<LocalDate, Double> param = new TreeMap<>();
        param.put(LocalDate.of(1970, 1, 11), 123.456789);

        view.displayForecast(param);

        String expected = "Вс 11.01.1970 - 123,46" + System.lineSeparator();
        String actual = arrayOutStream.toString();

        Assert.assertEquals(expected, actual);

        arrayOutStream.reset();
    }

    @Test
    public void testWriteMessage() {
        view.writeMessage("Hello!");

        String expected = "Hello!" + System.lineSeparator();
        String actual = arrayOutStream.toString();

        Assert.assertEquals(expected, actual);

        arrayOutStream.reset();
    }

    @Test
    public void testReadString() {
        String expected = "CurrencyForecast";
        String actual = view.readString();

        Assert.assertEquals(expected, actual);
    }
}