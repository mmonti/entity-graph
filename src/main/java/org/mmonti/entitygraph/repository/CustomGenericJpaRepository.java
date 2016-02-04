/*
 * Copyright 2008-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mmonti.entitygraph.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Subgraph;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Sample custom repository base class implementing common custom functionality for all derived repository instances.
 * 
 * @author Oliver Gierke
 */
public class CustomGenericJpaRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements CustomGenericRepository<T, ID> {

	private EntityManager entityManager;

	public CustomGenericJpaRepository(JpaEntityInformation<T, ID> metadata, EntityManager entityManager) {
		super(metadata, entityManager);
		this.entityManager = entityManager;
	}

	@Override
	public List<T> findAll(Specification<T> spec, EntityGraphType type, String... attributeGraph) {
		final TypedQuery<T> typedQuery = getQuery(spec, (Sort) null);

		if (attributeGraph != null && attributeGraph.length > 0) {
			final EntityGraph<T> entityGraph = this.entityManager.createEntityGraph(getDomainClass());
			buildEntityGraph(entityGraph, attributeGraph);
			typedQuery.setHint(type.getType(), entityGraph);
		}
        return typedQuery.getResultList();
	}

	@Override
	public Page<T> findAll(Specification<T> spec, Pageable pageable, EntityGraphType type, String... attributeGraph) {
		final TypedQuery<T> typedQuery = getQuery(spec, pageable);

		if (attributeGraph != null && attributeGraph.length > 0) {
			final EntityGraph<T> entityGraph = this.entityManager.createEntityGraph(getDomainClass());
			buildEntityGraph(entityGraph, attributeGraph);
			typedQuery.setHint(type.getType(), entityGraph);
		}

        return pageable == null ? new PageImpl<T>(typedQuery.getResultList()) : readPage(typedQuery, pageable, spec);
	}

	private static Long executeCountQuery(TypedQuery<Long> query) {

		Assert.notNull(query);

		List<Long> totals = query.getResultList();
		Long total = 0L;

		for (Long element : totals) {
			total += element == null ? 0 : element;
		}

		return total;
	}

	@Override
	protected Page<T> readPage(TypedQuery<T> query, Pageable pageable, Specification<T> spec) {
		query.setFirstResult(pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());

		Long total = executeCountQuery(getCountQuery(spec));
		List<T> content = total > pageable.getOffset() ? query.getResultList() : Collections.<T> emptyList();

		return new PageImpl<T>(content, pageable, total);
	}

	private void buildEntityGraph(EntityGraph<T> entityGraph, String[] attributeGraph) {
		List<String> attributePaths = Arrays.asList(attributeGraph);

		// Sort to ensure that the intermediate entity subgraphs are created accordingly.
		Collections.sort(attributePaths);
		Collections.reverse(attributePaths);

		// We build the entity graph based on the paths with highest depth first
		for (String path : attributePaths) {

			// Fast path - just single attribute
			if (!path.contains(".")) {
				entityGraph.addAttributeNodes(path);
				continue;
			}

			// We need to build nested sub fetch graphs
			String[] pathComponents = StringUtils.delimitedListToStringArray(path, ".");
			Subgraph<?> parent = null;

			for (int c = 0; c < pathComponents.length - 1; c++) {
				parent = c == 0 ? entityGraph.addSubgraph(pathComponents[c]) : parent.addSubgraph(pathComponents[c]);
			}

			parent.addAttributeNodes(pathComponents[pathComponents.length - 1]);
		}
	}

	public static enum EntityGraphType {

		FETCH("javax.persistence.fetchgraph"),
		LOAD("javax.persistence.loadgraph");

		private String type;

		EntityGraphType(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}
	}
}