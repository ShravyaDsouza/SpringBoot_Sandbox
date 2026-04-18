package example.cli;

import example.GithubActivity;
import example.service.ActivityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActivityCliRunner implements CommandLineRunner {
    private final ActivityService activityService;

    public ActivityCliRunner (ActivityService activityService){
        this.activityService = activityService;
    }

    @Override
    public void run (String...args){
        try{
            if(args.length != 1){
                printUsage();
                return;
            }
            String username = args[0].trim();
            if (username.isBlank()){
                throw new RuntimeException("Username cannot be empty");
            }

            List<String> activities = activityService.fetch(username);

            if (activities.isEmpty()){
                System.out.println("No recent activity for user" + username);
                return;
            }
            for (String activity : activities){
                System.out.println(activity);
            }
        } catch (RuntimeException e){
            System.out.println("Error" + e.getMessage());
        }
    }

     private void printUsage() {
        System.out.println("Usage: mvn spring-boot:run -Dspring-boot.run.arguments=\"<github-username>\"");
    }
}
