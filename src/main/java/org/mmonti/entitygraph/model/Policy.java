package org.mmonti.entitygraph.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.NotEmpty;
import org.mmonti.entitygraph.model.views.GroupView;
import org.mmonti.entitygraph.model.views.PolicyView;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mmonti
 */
@Data
@Entity
@Table(name = "policies", uniqueConstraints = {
	@UniqueConstraint(name = "uk_policies", columnNames = {"name"})
})
@DynamicUpdate
@EqualsAndHashCode(callSuper = false, of = {"id", "name"})
public class Policy extends AbstractEntity implements PolicyView {

	@Id
	@GeneratedValue(generator = "custom-uuid")
	@GenericGenerator(name = "custom-uuid", strategy = "org.mmonti.entitygraph.generator.UUIDWithAppender", parameters = { @Parameter(name = "appender", value = "@policy") })
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
			name = "groups_policies",
			joinColumns = {@JoinColumn(name="policy_id", nullable = false, unique = false)},
			inverseJoinColumns = {@JoinColumn(name="group_id", nullable = false, unique = false)})
	private Set<Group> groups = new HashSet<>();

	/**
	 *
	 * @param group
	 */
	public void add(Group group) {
		this.groups.add(group);
		group.getPolicies().add(this);
	}

	/**
	 *
	 * @param group
	 */
	public void remove(Group group) {
		this.groups.remove(group);
		group.getPolicies().remove(this);
	}

	@Override
	public String toString() {
		return "Policy{" +
				"name='" + name + '\'' +
				", id='" + id + '\'' +
				'}';
	}
}
