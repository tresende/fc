package com.tresende.catalog.infrastructure.castmember.persistence;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CastMemberRepository extends ElasticsearchRepository<CastMemberDocument, String> {
}
