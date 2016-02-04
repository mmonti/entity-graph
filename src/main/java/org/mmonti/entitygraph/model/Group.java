package org.mmonti.entitygraph.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mmonti
 */
@Data
@Entity
@Table(name = "groups", uniqueConstraints = {
	@UniqueConstraint(name = "uk_groups", columnNames = {"name"})
})
@DynamicUpdate
@EqualsAndHashCode(callSuper = false, of = {"id", "name"})
public class Group extends AbstractEntity {

	@Id
	@GeneratedValue(generator = "custom-uuid")
	@GenericGenerator(name = "custom-uuid", strategy = "org.mmonti.entitygraph.generator.UUIDWithAppender", parameters = { @Parameter(name = "appender", value = "@group") })
	@Column(name = "id")
	private String id;

	@NotEmpty
	@Column(name = "name", nullable = false, length = 64)
	private String name;

	@Column(name = "description")
	private String description;

	@JsonIgnore
	@ManyToMany
	@JoinTable(
			name = "identities_groups",
			joinColumns = {@JoinColumn(name="group_id", nullable = false, unique = false)},
			inverseJoinColumns = {@JoinColumn(name="identity_id", nullable = false, unique = false)})
	private Set<Identity> identities = new HashSet<Identity>();

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(
			name = "groups_policies",
			joinColumns = {@JoinColumn(name="group_id", nullable = false, unique = false)},
			inverseJoinColumns = {@JoinColumn(name="policy_id", nullable = false, unique = false)})
	private Set<Policy> policies = new HashSet<Policy>();

	/**
	 *
	 * @param identity
	 */
	public void add(Identity identity) {
		this.identities.add(identity);
		identity.getGroups().add(this);
	}

	/**
	 *
	 * @param identity
	 */
	public void remove(Identity identity) {
		this.identities.remove(identity);
		identity.getGroups().remove(this);
	}

	/**
	 *
	 * @param policy
	 */
	public void add(Policy policy) {
		this.policies.add(policy);
		policy.getGroups().add(this);
	}

	/**
	 *
	 * @param policy
	 */
	public void remove(Policy policy) {
		this.policies.remove(policy);
		policy.getGroups().remove(this);
	}

	@Override
	public String toString() {
		return "Group{" +
				"name='" + name + '\'' +
				", id='" + id + '\'' +
				'}';
	}
}