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
package com.example.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.Jpa21Utils;
import org.springframework.data.jpa.repository.query.JpaEntityGraph;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Subgraph;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.*;

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
	public List<T> findAll(Specification<T> spec, String... attributeGraph) {
		final TypedQuery<T> typedQuery = getQuery(spec, (Sort) null);

		if (attributeGraph != null && attributeGraph.length > 0) {
			final EntityGraph<T> entityGraph = this.entityManager.createEntityGraph(getDomainClass());
			buildEntityGraph(entityGraph, attributeGraph);
			typedQuery.setHint("javax.persistence.loadgraph", entityGraph);
		}
        return typedQuery.getResultList();
	}

	@Override
	public Page<T> findAll(Specification<T> spec, Pageable pageable, String... attributeGraph) {
		final TypedQuery<T> typedQuery = getQuery(spec, pageable);

		if (attributeGraph != null && attributeGraph.length > 0) {
			final EntityGraph<T> entityGraph = this.entityManager.createEntityGraph(getDomainClass());
			buildEntityGraph(entityGraph, attributeGraph);
			typedQuery.setHint("javax.persistence.loadgraph", entityGraph);
		}

        return pageable == null ? new PageImpl<T>(typedQuery.getResultList()) : readPage(typedQuery, pageable, spec);
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
}