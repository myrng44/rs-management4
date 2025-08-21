package ck4.nvb.rsmanagement;

import java.time.ZoneId;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RsManagementApplication {

  public static void main(String[] args) {
    TimeZone.setDefault(
        TimeZone.getTimeZone(
            "Asia/Ho_Chi_Minh")); // to fix bug: time zone deprecated in postgreSQL (Asia/Sai_Gon)
    System.out.println(">>> JVM Default TimeZone: " + ZoneId.systemDefault());
    SpringApplication.run(RsManagementApplication.class, args);
  }
}
