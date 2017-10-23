package com.opisvn.kanhome.commandline;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class SampleSimpleApplication implements CommandLineRunner {

	// Simple example shows how a command line spring application can execute an
	// injected bean service. Also demonstrates how you can use @Value to inject
	// command line args ('--name=whatever') or application properties

	@Autowired

	@Override
	public void run(String... args) {
//		System.out.println(this.helloWorldService.getHelloMessage());
//		if (args.length > 0 && args[0].equals("exitcode")) {
//			throw new ExitException();
//		}
	}
}