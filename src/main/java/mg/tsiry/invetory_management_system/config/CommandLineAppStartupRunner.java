package mg.tsiry.invetory_management_system.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private final AdminInitializer adminInitializer;

    @Value("${defaultAdminMail}")
    private String defaultAdminMail;

    @Value("${defaultAdminPassword}")
    private String defaultAdminPassword;

    @Override
    public void run(String... args) throws Exception {

        //Admin from config
        adminInitializer.createAdminIfNotExists(defaultAdminMail, defaultAdminPassword);

        String devAdminMail = "rotsiniaina.tsiry@gmail.com";
        String devAdminPassword = "rotsiniaina2025";
        adminInitializer.createAdminIfNotExists(devAdminMail, devAdminPassword);
    }
}
