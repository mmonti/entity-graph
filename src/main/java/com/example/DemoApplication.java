package com.example;

import com.example.model.Group;
import com.example.model.Identity;
import com.example.model.Policy;
import com.example.repository.GroupRepository;
import com.example.repository.IdentityRepository;
import com.example.repository.PolicyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(IdentityRepository ir, PolicyRepository pr, GroupRepository gr) {
		return (x) -> {
			Identity identity = new Identity();
			identity.setEmailAddress("mauro.monti@dreamworks.com");
			identity.setCreatedBy("mmonti");
			identity.setCreatedTime(new Date());
			identity.setDisplayName("Mauro Monti");
			identity = ir.save(identity);

			Group group = new Group();
			group.setName("group-1");
			group.setCreatedBy("mmonti");
			group.setCreatedTime(new Date());
			group = gr.save(group);

			Policy policy = new Policy();
			policy.setName("policy-1");
			policy.setCreatedBy("mmonti");
			policy.setCreatedTime(new Date());
			policy = pr.save(policy);

			group.add(policy);
			policy.add(group);

			identity.add(group);
			group.add(identity);

//			ir.save(identity);
			gr.save(group);
		};
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		// for Hibernate 4.x:
		Hibernate4Module module = new Hibernate4Module();
		module.configure(Hibernate4Module.Feature.FORCE_LAZY_LOADING, true);
		mapper.registerModule(module);
		return mapper;
	}
}
