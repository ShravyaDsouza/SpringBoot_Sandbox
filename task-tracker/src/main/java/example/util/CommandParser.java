package example.util;
import example.cli.ParsedCommand;
import org.springframework.stereotype.Component;

import java.awt.font.TextHitInfo;
import java.util.Map;
import java.util.HashMap;

@Component
public class CommandParser {

    public ParsedCommand parse(String... args){
        if(args == null || args.length == 0){
            throw new RuntimeException("No command provided");
        }
        String commandName = args[0];
        Map<String,String> options = new HashMap<>();

        for (int i=1 ; i<args.length;i++){
            String arg = args[i];

            if(!arg.startsWith("--")){
                throw new RuntimeException("Invalid argument format:"+arg);
            }
            int equalsIndex = arg.indexOf('=');

            if (equalsIndex == -1){
                throw new RuntimeException("Invalid option format:" + arg + ". Use --key=value");
            }
            String key = arg.substring(2,equalsIndex).trim();
            String value = arg.substring((equalsIndex+1)).trim();

            if(key.isEmpty() || value.isEmpty()){
                throw new RuntimeException("Invalid option"+arg);
            }
            options.put(key,value);
        }
        return new ParsedCommand(commandName,options);
    }
}
