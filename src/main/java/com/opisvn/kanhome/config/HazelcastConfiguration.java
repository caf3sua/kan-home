package com.opisvn.kanhome.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;

@Configuration
public class HazelcastConfiguration {

	@Bean
    public Config hazelCastConfig(){
		Config hazelCastConfig = new Config()
                .setInstanceName("hazelcast-instance");
                
        // Map instruments
        MapConfig userMapConfig = new MapConfig()
                .setName("user")
                .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.USED_HEAP_SIZE))
                .setEvictionPolicy(EvictionPolicy.LRU)
                .setMaxIdleSeconds(300)
                .setTimeToLiveSeconds(600);
        MapConfig deviceMapConfig = new MapConfig()
                .setName("device")
                .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.USED_HEAP_SIZE))
                .setEvictionPolicy(EvictionPolicy.LRU)
                .setMaxIdleSeconds(300)
                .setTimeToLiveSeconds(600);
        MapConfig modelMapConfig = new MapConfig()
                .setName("model")
                .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.USED_HEAP_SIZE))
                .setEvictionPolicy(EvictionPolicy.LRU)
                .setMaxIdleSeconds(300)
                .setTimeToLiveSeconds(600);
        MapConfig deviceStatMapConfig = new MapConfig()
                .setName("deviceStat")
                .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.USED_HEAP_SIZE))
                .setEvictionPolicy(EvictionPolicy.LRU)
                .setMaxIdleSeconds(300)
                .setTimeToLiveSeconds(600);
        
        // Add map config
        hazelCastConfig.addMapConfig(userMapConfig);
        hazelCastConfig.addMapConfig(deviceMapConfig);
        hazelCastConfig.addMapConfig(modelMapConfig);
        hazelCastConfig.addMapConfig(deviceStatMapConfig);
        
		return hazelCastConfig;
    }
}
