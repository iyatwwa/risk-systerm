package com.lab.risk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@MapperScan("com.lab.risk.mapper")
public class LabRiskSystemApplication {

	public static void main(String[] args) throws UnknownHostException {
		ConfigurableApplicationContext application = SpringApplication.run(LabRiskSystemApplication.class, args);
		Environment env = application.getEnvironment();
		String ip = InetAddress.getLocalHost().getHostAddress();
		String port = env.getProperty("server.port");
		String path = env.getProperty("server.servlet.context-path", "");

		System.out.println("\n----------------------------------------------------------");
		System.out.println("\t应用启动成功! 访问地址:");
		System.out.println("\t本地访问: \thttp://localhost:" + port + path);
		System.out.println("\t外部访问: \thttp://" + ip + ":" + port + path);
		System.out.println("\tAPI文档: \thttp://localhost:" + port + path + "/doc.html");
		System.out.println("\tDruid监控: \thttp://localhost:" + port + path + "/druid");
		System.out.println("----------------------------------------------------------\n");
	}
}