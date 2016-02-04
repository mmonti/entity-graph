package org.mmonti.entitygraph;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mmonti.entitygraph.model.Group;
import org.mmonti.entitygraph.model.Identity;
import org.mmonti.entitygraph.repository.IdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mmonti.entitygraph.repository.specifications.IdentitySpecifications.emailAddress;
import static org.springframework.data.jpa.domain.Specifications.where;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ApplicationTests extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private IdentityRepository identityRepository;

	@Before
	public void setup() {}

	@Test
	public void testIdentityLazyManyToMany() throws Exception {
		final Specification<Identity> identitySpecification = where(emailAddress("monti.mauro@gmail.com"));
		final Identity identity = identityRepository.findOne(identitySpecification);

		String serializedIdentity = objectMapper.writeValueAsString(identity);
		Map<String, Object> objectMap = objectMapper.readValue(serializedIdentity, Map.class);

		Assert.assertNotNull(objectMap);
		// = ISSUE #21 - objectMap.containsKey('groups') -> should be false since FORCE_LAZY_LOADING is disabled
		// = in JacksonConfig.
		Assert.assertTrue(objectMap.containsKey("groups"));
		Assert.assertNull(objectMap.get("groups"));

		// = We force the collection loading.
		Set<Group> groups = identity.getGroups();
		Assert.assertNotNull(groups);
		Assert.assertTrue(!groups.isEmpty());

		serializedIdentity = objectMapper.writeValueAsString(identity);
		Assert.assertTrue(!serializedIdentity.isEmpty());

		objectMap = objectMapper.readValue(serializedIdentity, Map.class);
		Assert.assertNotNull(objectMap);

		Assert.assertTrue(objectMap.containsKey("groups"));
		Assert.assertNotNull(objectMap.get("groups"));

		// = Now the collection is not null, nor empty.
		Assert.assertTrue(!((List<Group>)objectMap.get("groups")).isEmpty());

		Assert.assertNotNull(identity.getGroups());
		Assert.assertTrue(!identity.getGroups().isEmpty());
	}
}
