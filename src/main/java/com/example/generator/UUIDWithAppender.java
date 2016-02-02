package com.example.generator;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author mmonti
 */
public class UUIDWithAppender implements IdentifierGenerator, Configurable {

	private String appender;

	@Override
	public Serializable generate(final SessionImplementor session, final Object object) throws HibernateException {
		String id = java.util.UUID.randomUUID().toString();
		if (appender != null) {
			id = id + appender;
		}
		return id;
	}

	@Override
	public void configure(final Type type, final Properties params, final Dialect dialect) throws MappingException {
		appender = params.getProperty("appender");
	}

}
