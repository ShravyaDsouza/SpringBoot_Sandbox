package example.util;

import example.cli.ParsedCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {

    private CommandParser commandParser;

    @BeforeEach
    void setUp() {
        commandParser = new CommandParser();
    }

    @Test
    void shouldParseAddCommandWithAllOptions() {
        ParsedCommand parsed = commandParser.parse(
                "add",
                "--desc=Buy milk",
                "--priority=high"
        );

        assertEquals("add", parsed.getCommandName());
        assertEquals("Buy milk", parsed.getOption("desc"));
        assertEquals("high", parsed.getOption("priority"));
        assertTrue(parsed.hasOption("desc"));
        assertTrue(parsed.hasOption("priority"));
    }

    @Test
    void shouldParseListCommandWithStatus() {
        ParsedCommand parsed = commandParser.parse(
                "list",
                "--status=done"
        );

        assertEquals("list", parsed.getCommandName());
        assertEquals("done", parsed.getOption("status"));
    }

    @Test
    void shouldParseListCommandWithPriority() {
        ParsedCommand parsed = commandParser.parse(
                "list",
                "--priority=high"
        );

        assertEquals("list", parsed.getCommandName());
        assertEquals("high", parsed.getOption("priority"));
    }

    @Test
    void shouldParseListCommandWithStatusAndPriority() {
        ParsedCommand parsed = commandParser.parse(
                "list",
                "--status=todo",
                "--priority=medium"
        );

        assertEquals("list", parsed.getCommandName());
        assertEquals("todo", parsed.getOption("status"));
        assertEquals("medium", parsed.getOption("priority"));
    }

    @Test
    void shouldThrowWhenNoCommandProvided() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                commandParser.parse()
        );

        assertEquals("No command provided", exception.getMessage());
    }

    @Test
    void shouldThrowWhenArgumentDoesNotStartWithDoubleDash() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                commandParser.parse("update", "1", "--desc=New task")
        );

        assertTrue(exception.getMessage().contains("Invalid argument format"));
    }

    @Test
    void shouldThrowWhenOptionDoesNotContainEquals() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                commandParser.parse("add", "--desc")
        );

        assertTrue(exception.getMessage().contains("Invalid option format"));
    }

    @Test
    void shouldThrowWhenKeyIsEmpty() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                commandParser.parse("add", "--=value")
        );

        assertTrue(exception.getMessage().contains("Invalid option"));
    }

    @Test
    void shouldThrowWhenValueIsEmpty() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                commandParser.parse("add", "--desc=")
        );

        assertTrue(exception.getMessage().contains("Invalid option"));
    }
}