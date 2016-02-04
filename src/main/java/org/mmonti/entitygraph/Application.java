package org.mmonti.entitygraph;

import org.mmonti.entitygraph.model.Group;
import org.mmonti.entitygraph.model.Identity;
import org.mmonti.entitygraph.model.Policy;
import org.mmonti.entitygraph.repository.GroupRepository;
import org.mmonti.entitygraph.repository.IdentityRepository;
import org.mmonti.entitygraph.repository.PolicyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 *
	 * @param ir
	 * @param pr
	 * @param gr
     * @return
     */
	@Bean
	public CommandLineRunner commandLineRunner(IdentityRepository ir, PolicyRepository pr, GroupRepository gr) {
		return (x) -> {
			Identity identity = new Identity();
			identity.setEmailAddress("monti.mauro@gmail.com");
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

			gr.save(group);
		};
	}
}
