package com.opisvn.kanhome;

import com.opisvn.kanhome.config.ApplicationProperties;
import com.opisvn.kanhome.config.DefaultProfileUtil;
import com.opisvn.kanhome.mqtt.KanHomeMqttCallback;
import com.opisvn.kanhome.mqtt.TLSSocketFactory;

import io.github.jhipster.config.JHipsterConstants;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.annotation.PostConstruct;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;

@ComponentScan
@EnableAutoConfiguration(exclude = {MetricFilterAutoConfiguration.class, MetricRepositoryAutoConfiguration.class})
@EnableConfigurationProperties({ApplicationProperties.class})
@EnableCaching
public class KanHomeApp {

    private static final Logger log = LoggerFactory.getLogger(KanHomeApp.class);

    private final Environment env;
    
    private SimpMessagingTemplate template;

    public KanHomeApp(Environment env, SimpMessagingTemplate template) {
        this.env = env;
        this.template = template;
    }

    /**
     * Initializes KanHome.
     * <p>
     * Spring profiles can be configured with a program arguments --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="http://jhipster.github.io/profiles/">http://jhipster.github.io/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run " +
                "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)) {
            log.error("You have misconfigured your application! It should not" +
                "run with both the 'dev' and 'cloud' profiles at the same time.");
        }
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     * @throws UnknownHostException if the local host name could not be resolved into an address
     */
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(KanHomeApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running! Access URLs:\n\t" +
                "Local: \t\t{}://localhost:{}\n\t" +
                "External: \t{}://{}:{}\n\t" +
                "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            env.getProperty("server.port"),
            protocol,
            InetAddress.getLocalHost().getHostAddress(),
            env.getProperty("server.port"),
            env.getActiveProfiles());
    }
    
    @Bean
	public MqttAsyncClient mqttAsyncClient() throws MqttException, KeyManagementException, NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException, InterruptedException {
    	// Config value
    	String protocol = env.getProperty("spring.mqtt.protocol");
    	String host = env.getProperty("spring.mqtt.host");
    	String port = env.getProperty("spring.mqtt.port");
    	String username = env.getProperty("spring.mqtt.username");
    	String password = env.getProperty("spring.mqtt.password");
    	String caFile = env.getProperty("spring.mqtt.caFile");
    	
    	// serverURI
    	String serverURI = protocol + "://" + host + ":" + port;
    	
    	MqttAsyncClient client = new MqttAsyncClient(serverURI, MqttClient.generateClientId(), new MemoryPersistence());
    	TLSSocketFactory tlsSocketFactory = new TLSSocketFactory(caFile); 
    	MqttConnectOptions options = new MqttConnectOptions();
    	options.setSocketFactory(tlsSocketFactory);
    	options.setUserName(username);
    	options.setPassword(password.toCharArray());
    	client.connect(options);
    	Thread.sleep(5000);
    	client.setCallback(new KanHomeMqttCallback(template));
    	
    	if (client.isConnected()) {
    		log.info("MqttAsyncClient connected.");
    		client.subscribe("notification", 0);
        } else {
        	log.warn("MqttAsyncClient not connected.");
        }
		//client.subscribe("notification/1", 0);
    	return client;
	}
}
