package ck4.nvb.rsmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.time.ZoneId;
import java.util.TimeZone;


@SpringBootApplication
public class RsManagementApplication {

  public static void main(String[] args) {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh")); // to fix bug: time zone deprecated in postgreSQL (Asia/Sai_Gon)
    System.out.println(">>> JVM Default TimeZone: " + ZoneId.systemDefault());
    SpringApplication.run(RsManagementApplication.class, args);
  }
}
