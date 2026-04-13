package example.cli;

import java.util.Map;

public class ParsedCommand {

    private final String commandName;
    private final Map<String, String> options;

    public ParsedCommand(String commandName, Map<String, String> options) {
        this.commandName = commandName;
        this.options = options;
    }

    public String getCommandName() {
        return commandName;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public String getOption(String key) {
        return options.get(key);
    }

    public boolean hasOption(String key) {
        return options.containsKey(key);
    }
}