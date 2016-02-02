package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

/**
 * @author mmonti
 */
@Data
@Entity
@Table(name = "identities", uniqueConstraints = {
	@UniqueConstraint(name = "uk_identities", columnNames = {"email_address"})
})
@DynamicUpdate
@EqualsAndHashCode(callSuper = false, of = {"id", "emailAddress"})
public class Identity extends AbstractEntity {

	private static final String EMAIL_ADDRESS_REGEXP = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";
	private static final String DISPLAY_NAME_REGEXP = "^[a-zA-Z0-9 ,.'-]+$";

	@Id
	@GeneratedValue(generator = "custom-uuid")
	@GenericGenerator(name = "custom-uuid", strategy = "com.example.generator.UUIDWithAppender", parameters = { @Parameter(name = "appender", value = "@identity") })
	@Column(name = "id")
	private String id;

	@NotEmpty
	@Pattern(regexp = EMAIL_ADDRESS_REGEXP, message = "emailAddress is not valid.")
	@Column(name = "email_address", nullable = false)
	private String emailAddress;

	@NotEmpty
	@Pattern(regexp = DISPLAY_NAME_REGEXP, message = "displayName is not valid.")
	@Column(name = "display_name", nullable = false, length = 100)
	private String displayName;

	@ManyToMany(fetch = LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(
			name = "identities_groups",
			joinColumns = {@JoinColumn(name="identity_id", nullable = false, unique = false)},
			inverseJoinColumns = {@JoinColumn(name="group_id", nullable = false, unique = false)})
	private Set<Group> groups = new HashSet<Group>();

	/**
	 *
	 * @param group
     */
	public void add(Group group) {
		this.groups.add(group);
		group.getIdentities().add(this);
	}

	/**
	 *
	 * @param group
     */
	public void remove(Group group) {
		this.groups.remove(group);
		group.getIdentities().remove(this);
	}

	/**
	 * Force emailAddress to be lowercase.
	 *
	 * @param emailAddress
     */
	public void setEmailAddress(final String emailAddress) {
		this.emailAddress = (emailAddress == null) ? emailAddress : emailAddress.toLowerCase();
	}

	@Override
	public String toString() {
		return "Identity{" +
				"displayName='" + displayName + '\'' +
				", id='" + id + '\'' +
				'}';
	}
}
